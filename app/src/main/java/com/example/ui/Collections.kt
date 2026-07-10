package com.example.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Film
import com.example.data.MovieCollection
import com.example.data.RemoteJson
import com.example.data.SeasonPeriod
import com.example.ui.theme.Bg
import com.example.ui.theme.Dim
import com.example.ui.theme.Green
import com.example.ui.theme.Ink
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ---------- بارگذاری: اول سرور (DATA_BASE/collections.json)، اگر نشد assets ----------
// blocking → فقط روی IO صدا زده می‌شود (در MoodSection).
fun loadCollections(context: Context): List<MovieCollection> = try {
    val json = RemoteJson.fetch("collections.json")
        ?: context.assets.open("collections.json").use { it.bufferedReader().readText() }
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(List::class.java, MovieCollection::class.java)
    val parsed = moshi.adapter<List<MovieCollection>>(type).fromJson(json) ?: emptyList()

    // تزریق داینامیک لیست تابستانه در ماه‌های تیر، مرداد و شهریور
    val today = PersianCalendar.today()
    val isSummer = PersianCalendar.isWithin(today, "04-01", "06-31")

    if (isSummer) {
        val summerCollection = createSummerCollection(today.month)
        listOf(summerCollection) + parsed.filter { it.id != "summer_list" }
    } else {
        parsed
    }
} catch (e: Exception) {
    emptyList()
}

fun createSummerCollection(month: Int): MovieCollection {
    // انتخاب ۱۰ فیلم متنوع و سرگرم‌کننده واقعی برای هر ماه تابستان با رعایت تنوع ژانرها و وجود حداقل یک انیمیشن در هر کدام
    val ids = when (month) {
        4 -> listOf(
            "tt1375666", // Inception (علمی-تخیلی/اکشن)
            "tt0816692", // Interstellar (علمی-تخیلی/ماجراجویی)
            "tt0468569", // The Dark Knight (اکشن/جنایی)
            "tt0133093", // The Matrix (اکشن/علمی-تخیلی)
            "tt0167260", // The Lord of the Rings: The Return of the King (فانتزی/ماجراجویی)
            "tt1000015", // WALL·E (انیمیشن/علمی-تخیلی) - انیمیشن اصلی تیرماه
            "tt0088763", // Back to the Future (ماجراجویی/علمی-تخیلی)
            "tt0172495", // Gladiator (اکشن/تاریخی)
            "tt0120737", // The Lord of the Rings: The Fellowship of the Ring (فانتزی/ماجراجویی)
            "tt2582846"  // Whiplash (درام/موزیکال)
        ) // تیرماه: اکشن، ماجراجویی، فانتزی و علمی‌تخیلی
        5 -> listOf(
            "tt0111161", // The Shawshank Redemption (درام/جنایی)
            "tt0068646", // The Godfather (جنایی/درام)
            "tt0110912", // Pulp Fiction (جنایی/درام)
            "tt0114369", // Se7en (جنایی/معمایی/مهیج)
            "tt0137523", // Fight Club (درام/مهیج)
            "tt0241527", // Spirited Away (انیمیشن/فانتزی) - انیمیشن اصلی مردادماه
            "tt0102926", // The Silence of the Lambs (جنایی/معمایی/مهیج)
            "tt0050083", // 12 Angry Men (درام/جنایی)
            "tt0054215", // Psycho (وحشت/مهیج)
            "tt0108052"  // Schindler's List (درام/تاریخی)
        ) // مردادماه: جنایی، معمایی، مهیج و درام‌های عمیق
        6 -> listOf(
            "tt0109830", // Forrest Gump (درام/کمدی/رمانتیک)
            "tt1000074", // My Neighbor Totoro (انیمیشن/خانوادگی) - انیمیشن اصلی شهریورماه
            "tt0034583", // Casablanca (درام/رمانتیک)
            "tt0027977", // Modern Times (کمدی/رمانتیک)
            "tt0118799", // Life Is Beautiful (کمدی/درام/رمانتیک)
            "tt1675434", // The Intouchables (کمدی/درام)
            "tt0038650", // It's a Wonderful Life (خانوادگی/فانتزی/درام)
            "tt1000086", // Howl's Moving Castle (انیمیشن/فانتزی) - انیمیشن دوم (بونوس)
            "tt1000036", // Amélie (کمدی/رمانتیک)
            "tt1000040"  // Cinema Paradiso (درام/رمانتیک)
        ) // شهریورماه: کمدی، رمانتیک، خانوادگی و موزیکال‌های ماندگار
        else -> listOf("tt1375666", "tt0816692", "tt0468569", "tt0109830", "tt0133093", "tt0167260", "tt0172495", "tt0088763", "tt2582846", "tt1000015")
    }

    val subtitle = when (month) {
        4 -> "تیرماه: ترکیب مهیج از اکشن، ماجراجویی و علمی‌تخیلی همراه با انیمیشن وال-یی"
        5 -> "مردادماه: غرق در دنیای معمایی، جنایی و مهیج همراه با شاهکار انیمیشن شهر اشباح"
        6 -> "شهریورماه: دنیای کمدی، درام‌های خانوادگی و رمانتیک همراه با انیمیشن لطیف توتورو"
        else -> "۱۰ فیلم سرگرم‌کننده و متنوع برای فصل تابستان"
    }

    return MovieCollection(
        id = "summer_list",
        title = "لیست تابستانه",
        subtitle = subtitle,
        emoji = "☀️",
        filmIds = ids,
        gradient = listOf("#D35400", "#F39C12"), // گرادیان گرم خورشید تابستانی
        season = SeasonPeriod(from = "04-01", to = "06-31")
    )
}

private fun hexColor(hex: String): Color {
    val h = hex.trim().removePrefix("#")
    return Color(h.substring(0, 2).toInt(16), h.substring(2, 4).toInt(16), h.substring(4, 6).toInt(16))
}

private fun gradientBrush(hexes: List<String>): Brush {
    val colors = if (hexes.size >= 2) hexes.map { hexColor(it) }
    else listOf(hexColor("#2C3440"), hexColor("#14181C"))
    return Brush.linearGradient(colors)
}

private val faDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
private fun toFa(n: Int): String = n.toString().map { if (it in '0'..'9') faDigits[it - '0'] else it }.joinToString("")

// ---------- بخش حس‌وحال (بنر مناسبتی + قفسه) ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSection(
    all: List<Film>,
    vm: com.example.ui.FilmViewModel,
    onOpen: (MovieCollection) -> Unit
) {
    val context = LocalContext.current
    var collections by remember { mutableStateOf<List<MovieCollection>>(emptyList()) }
    LaunchedEffect(Unit) {
        collections = withContext(Dispatchers.IO) { loadCollections(context) }
    }
    if (collections.isEmpty()) return

    val today = remember { PersianCalendar.today() }
    val seasonal = remember(collections) {
        collections.firstOrNull { it.season != null && PersianCalendar.isWithin(today, it.season!!.from, it.season!!.to) }
    }
    val moods = remember(collections) { collections.filter { it.season == null } }

    val watched by vm.watchedIds.collectAsState()
    val watchlist by vm.watchlistIds.collectAsState()
    val seeds by vm.tasteSeeds.collectAsState()

    val sortedMoods = remember(moods, watched, watchlist, seeds) {
        val allUserIds = watched + watchlist + seeds
        val userFilms = all.filter { allUserIds.contains(it.id) }
        val userGenres = userFilms.flatMap { it.genres }.toSet()
        val userDirectors = userFilms.flatMap { it.director }.toSet()

        moods.map { collection ->
            var score = 0
            val collectionFilms = all.filter { collection.filmIds.contains(it.id) }
            collectionFilms.forEach { film ->
                score += film.genres.count { userGenres.contains(it) } * 2
                score += film.director.count { userDirectors.contains(it) } * 3
                if (watchlist.contains(film.id)) score += 1
                if (watched.contains(film.id)) score += 1
                if (seeds.contains(film.id)) score += 2
            }
            Pair(collection, score)
        }.sortedByDescending { it.second }.map { it.first }
    }

    var showAllBottomSheet by remember { mutableStateOf(false) }

    Column {
        if (seasonal != null) {
            SeasonalBanner(seasonal) { onOpen(seasonal) }
            Spacer(Modifier.height(14.dp))
        }
        if (sortedMoods.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // نشانگر مینیمال و منحصربه‌فرد نوار کپسولی به جای دایره
                    Box(
                        modifier = Modifier
                            .size(width = 3.5.dp, height = 13.dp)
                            .clip(RoundedCornerShape(30))
                            .background(Green)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("حس‌وحال امروز چیه؟", color = Dim, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Text(
                    text = "مشاهده همه",
                    color = Green,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { showAllBottomSheet = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(sortedMoods.take(3)) { MoodCard(it) { onOpen(it) } }
            }
        }
    }

    if (showAllBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAllBottomSheet = false },
            containerColor = Bg,
            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "همه حس‌وحال‌ها",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "بستن",
                        fontSize = 13.sp,
                        color = Dim,
                        modifier = Modifier
                            .clickable { showAllBottomSheet = false }
                            .padding(4.dp)
                    )
                }
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp)
                ) {
                    items(sortedMoods.size) { index ->
                        val mood = sortedMoods[index]
                        MoodCard(mood) {
                            onOpen(mood)
                            showAllBottomSheet = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonalBanner(c: MovieCollection, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(gradientBrush(c.gradient))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text((c.emoji?.plus("  ") ?: "") + c.title, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            if (c.subtitle.isNotBlank()) {
                Spacer(Modifier.height(3.dp))
                Text(c.subtitle, color = Color.White.copy(alpha = .82f), fontSize = 11.5.sp)
            }
        }
        Text("‹", color = Ink, fontSize = 18.sp)
    }
}

@Composable
private fun MoodCard(c: MovieCollection, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(150.dp, 92.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(gradientBrush(c.gradient))
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text((c.emoji?.plus(" ") ?: "") + c.title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        Text(
            if (c.subtitle.isNotBlank()) c.subtitle else "${toFa(c.filmIds.size)} فیلم",
            color = Color.White.copy(alpha = .78f), fontSize = 11.sp
        )
    }
}

// ---------- نمای تفصیلی مجموعه / لیست فیلمباز ----------
@Composable
fun CollectionDetailView(
    collection: MovieCollection,
    allFilms: List<Film>,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState(),
    onFilmClick: (Film) -> Unit,
    onBack: () -> Unit
) {
    val filteredFilms = remember(collection, allFilms) {
        allFilms.filter { film -> collection.filmIds.contains(film.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        // هدر شیک رنگی متناسب با تم مجموعه به همراه دکمه بازگشت
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush(collection.gradient))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    collection.emoji?.let {
                        Text(it, fontSize = 22.sp)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = collection.title,
                        color = Ink,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (collection.subtitle.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = collection.subtitle,
                        color = Color.White.copy(alpha = 0.88f),
                        fontSize = 12.5.sp
                    )
                }
            }

            // دکمه بازگشت مینیمال دایره‌ای با تم شیشه‌ای نیمه‌شفاف
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.22f))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("‹", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // شبکه‌ای از فیلم‌های موجود در این کالکشن یا لیست
        if (filteredFilms.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("فیلمی یافت نشد", color = Dim)
            }
        } else {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(filteredFilms) { index, film ->
                    FilmCard(
                        film = film,
                        index = index,
                        onClick = { onFilmClick(film) }
                    )
                }
            }
        }
    }
}

