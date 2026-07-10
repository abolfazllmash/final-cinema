const fs = require('fs');
const path = require('path');

const apiKey = process.env.GEMINI_API_KEY;
if (!apiKey) {
  console.error('No API key found in process.env.GEMINI_API_KEY');
  process.exit(1);
}

const assetsDir = path.join(__dirname, '..', 'src', 'main', 'assets');
const filmsPath = path.join(assetsDir, 'films.json');
const topListsPath = path.join(assetsDir, 'top_lists.json');

const films = JSON.parse(fs.readFileSync(filmsPath, 'utf8'));
const topLists = JSON.parse(fs.readFileSync(topListsPath, 'utf8'));

// Only process movies that are actually present in our "best films" (top lists)
const topListIds = new Set([
  ...topLists.imdb.map(m => m.id),
  ...topLists.rotten.map(m => m.id),
  ...topLists.metacritic.map(m => m.id)
]);

console.log(`Loaded top lists with ${topListIds.size} unique movie IDs.`);

const filmsNeedingEnrichment = films.filter(f => {
  if (!topListIds.has(f.id)) return false;

  const directorList = f.director || [];
  const castList = f.cast || [];
  const isUnknownDirector = directorList.includes('کارگردان ناشناخته') || directorList.length === 0;
  const isPlaceholderCast = castList.length === 0 || (castList.length === 1 && castList[0].n === 'بازیگر اصلی');
  const isPlaceholderTitle = !f.fa || f.fa === f.marquee;
  return isUnknownDirector || isPlaceholderCast || isPlaceholderTitle;
});

console.log(`Found ${filmsNeedingEnrichment.length} movies in top_lists needing enrichment.`);

const filmMap = new Map(films.map(f => [f.id, f]));

const modelPool = [
  'models/gemini-flash-lite-latest',
  'models/gemini-3.1-flash-lite',
  'models/gemini-3.1-flash-lite-preview',
  'models/gemini-3-flash-preview'
];
let currentModelIndex = 0;

async function queryGeminiBatchWithFallback(batch) {
  let attempts = 0;
  while (attempts < modelPool.length * 2) {
    const model = modelPool[currentModelIndex];
    const url = `https://generativelanguage.googleapis.com/v1beta/${model}:generateContent?key=${apiKey}`;
    const movieInputList = batch.map(m => ({ id: m.id, title: m.marquee, year: m.year }));
    
    const prompt = `You are an expert film database and translation assistant. I will provide a list of movies with their English titles and release years.
For each movie, please provide the following fields in Persian (Farsi):
1. titleFa: The widely accepted Persian title of the movie (e.g. "Seven Samurai" -> "هفت سامورایی").
2. director: A list containing the main director(s) name(s) in Persian (e.g. "Akira Kurosawa" -> ["آکیرا کوروساوا"]).
3. cast: A list of 3-4 main actors of the movie, with their names in Persian (n) and optionally their characters in Persian (c) if known (e.g. [{"n": "توشیرا میفونه", "c": "کیکوچیو"}]).
4. overview: A compelling, brief 1-2 sentence description/synopsis of the movie in Persian (Farsi), written in a professional, engaging tone.

Input list:
${JSON.stringify(movieInputList, null, 2)}

Respond ONLY with a valid JSON array matching this schema:
[
  {
    "id": "tt1000001",
    "titleFa": "هفت سامورایی",
    "director": ["آکیرا کوروساوا"],
    "cast": [
      { "n": "توشیرا میفونه", "c": "کیکوچیو" },
      { "n": "تاکاشی شیمورا", "c": "کامبی شیمادا" }
    ],
    "overview": "داستانی حماسی و ماندگار از هفت سامورایی دلاور که برای دفاع از دهکده‌ای فقیر در برابر هجوم راهزنان استخدام می‌شوند."
  },
  ...
]`;

    try {
      console.log(`  Trying model: ${model}...`);
      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [{
            parts: [{ text: prompt }]
          }],
          generationConfig: {
            responseMimeType: "application/json"
          }
        })
      });

      if (response.status === 429 || response.status === 403) {
        console.warn(`  Model ${model} hit rate limit / quota (Status ${response.status}). Switching to next model...`);
        currentModelIndex = (currentModelIndex + 1) % modelPool.length;
        attempts++;
        await new Promise(res => setTimeout(res, 1000));
        continue;
      }

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status} - ${await response.text()}`);
      }

      const result = await response.json();
      const text = result.candidates[0].content.parts[0].text;
      return JSON.parse(text);
    } catch (err) {
      console.error(`  Error querying model ${model}: ${err.message}`);
      currentModelIndex = (currentModelIndex + 1) % modelPool.length;
      attempts++;
      await new Promise(res => setTimeout(res, 2000));
    }
  }
  throw new Error("All models in the pool failed or hit quota limits.");
}

function saveProgress() {
  // Save updated films
  const updatedFilmsList = Array.from(filmMap.values());
  fs.writeFileSync(filmsPath, JSON.stringify(updatedFilmsList, null, 2), 'utf8');

  // Save updated top lists
  const updateTopList = (list) => {
    return list.map(m => {
      const dbFilm = filmMap.get(m.id);
      if (dbFilm) {
        return {
          ...m,
          titleFa: dbFilm.fa,
          director: dbFilm.director.join('، '),
          desc: dbFilm.overview || m.desc
        };
      }
      return m;
    });
  };

  const updatedTopLists = {
    imdb: updateTopList(topLists.imdb),
    rotten: updateTopList(topLists.rotten),
    metacritic: updateTopList(topLists.metacritic)
  };

  fs.writeFileSync(topListsPath, JSON.stringify(updatedTopLists, null, 2), 'utf8');
  console.log('Saved current progress to both films.json and top_lists.json');
}

async function enrichAll() {
  const batchSize = 10;
  const batches = [];
  for (let i = 0; i < filmsNeedingEnrichment.length; i += batchSize) {
    batches.push(filmsNeedingEnrichment.slice(i, i + batchSize));
  }

  console.log(`Processing in ${batches.length} batches of up to ${batchSize} movies sequentially.`);

  for (let idx = 0; idx < batches.length; idx++) {
    const batch = batches[idx];
    console.log(`Processing batch ${idx + 1}/${batches.length} (${batch.length} movies)...`);
    
    let retries = 3;
    let rateLimitRetries = 5;
    let success = false;
    while (retries > 0 && !success) {
      try {
        const enrichedList = await queryGeminiBatchWithFallback(batch);
        
        for (const enriched of enrichedList) {
          const original = filmMap.get(enriched.id);
          if (original) {
            original.fa = enriched.titleFa || original.fa;
            original.director = enriched.director || original.director;
            original.cast = enriched.cast || original.cast;
            original.overview = enriched.overview || original.overview;
            console.log(`  Enriched: [${original.id}] ${original.marquee} -> ${original.fa}`);
          }
        }
        success = true;
        console.log(`Batch ${idx + 1} completed.`);
        saveProgress();
      } catch (err) {
        const isRateLimit = err.message.includes('All models in the pool failed') || err.message.includes('429') || err.message.includes('Quota exceeded');
        if (isRateLimit && rateLimitRetries > 0) {
          rateLimitRetries--;
          console.warn(`  All models hit quota/rate limits. Waiting 65 seconds to cool down (Retries remaining: ${rateLimitRetries})...`);
          await new Promise(res => setTimeout(res, 65000));
          continue; // Retry batch
        }
        
        retries--;
        console.error(`  Error in batch ${idx + 1}: ${err.message}. Retries left: ${retries}`);
        if (retries > 0) {
          await new Promise(res => setTimeout(res, 3000));
        }
      }
    }
    
    // Pause 3 seconds between batches to avoid spamming the API
    if (idx < batches.length - 1) {
      await new Promise(res => setTimeout(res, 3000));
    }
  }

  console.log('All batches completed successfully!');
}

enrichAll().then(() => {
  console.log('Successfully completed enrichment process!');
}).catch(console.error);
