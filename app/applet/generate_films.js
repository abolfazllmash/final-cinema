const fs = require('fs');
const path = require('path');

// 1. Raw lists from the user
const imdbRaw = `The Shawshank Redemption (1994)
The Godfather (1972)
The Godfather: Part II (1974)
The Dark Knight (2008)
12 Angry Men (1957)
Schindler's List (1993)
Pulp Fiction (1994)
The Good, the Bad and the Ugly (1966)
The Lord of the Rings: The Return of the King (2003)
Fight Club (1999)
The Lord of the Rings: The Fellowship of the Ring (2001)
Star Wars: Episode V - The Empire Strikes Back (1980)
Forrest Gump (1994)
Inception (2010)
One Flew Over the Cuckoo's Nest (1975)
The Lord of the Rings: The Two Towers (2002)
Goodfellas (1990)
The Matrix (1999)
Star Wars (1977)
Seven Samurai (1954)
City of God (2002)
Se7en (1995)
The Silence of the Lambs (1991)
The Usual Suspects (1995)
It's a Wonderful Life (1946)
Life Is Beautiful (1997)
Léon: The Professional (1994)
Once Upon a Time in the West (1968)
Interstellar (2014)
Saving Private Ryan (1998)
American History X (1998)
Spirited Away (2001)
Casablanca (1942)
Raiders of the Lost Ark (1981)
Psycho (1960)
City Lights (1931)
Rear Window (1954)
The Intouchables (2011)
Modern Times (1936)
Terminator 2: Judgment Day (1991)
Whiplash (2014)
The Green Mile (1999)
The Pianist (2002)
Memento (2000)
The Departed (2006)
Gladiator (2000)
Apocalypse Now (1979)
Back to the Future (1985)
Sunset Blvd. (1950)
Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb (1964)
The Prestige (2006)
Alien (1979)
The Lion King (1994)
The Lives of Others (2006)
The Great Dictator (1940)
Inside Out (2015)
Cinema Paradiso (1988)
The Shining (1980)
Paths of Glory (1957)
Django Unchained (2012)
The Dark Knight Rises (2012)
WALL·E (2008)
American Beauty (1999)
Grave of the Fireflies (1988)
Aliens (1986)
North by Northwest (1959)
Princess Mononoke (1997)
Oldeuboi / Oldboy (2003)
Das Boot (1981)
M (1931)
Star Wars: Episode VI - Return of the Jedi (1983)
Once Upon a Time in America (1984)
Amélie (2001)
Witness for the Prosecution (1957)
Reservoir Dogs (1992)
Braveheart (1995)
Toy Story 3 (2010)
A Clockwork Orange (1971)
Double Indemnity (1944)
Taxi Driver (1976)
Requiem for a Dream (2000)
To Kill a Mockingbird (1962)
Eternal Sunshine of the Spotless Mind (2004)
Full Metal Jacket (1987)
The Sting (1973)
Amadeus (1984)
Bicycle Thieves (1948)
Singin' in the Rain (1952)
Monty Python and the Holy Grail (1975)
Snatch (2000)
2001: A Space Odyssey (1968)
The Kid (1921)
L.A. Confidential (1997)
Rashômon (1950)
For a Few Dollars More (1965)
Toy Story (1995)
The Apartment (1960)
Inglourious Basterds (2009)
All About Eve (1950)
The Treasure of the Sierra Madre (1948)
Jodaeiye Nader az Simin / A Separation (2011)
Indiana Jones and the Last Crusade (1989)
Metropolis (1927)
Yojimbo (1961)
The Third Man (1949)
Batman Begins (2005)
Scarface (1983)
Some Like It Hot (1959)
Unforgiven (1992)
3 Idiots (2009)
Up (2009)
Raging Bull (1980)
Downfall (2004)
Mad Max: Fury Road (2015)
Jagten / The Hunt (2012)
Chinatown (1974)
The Great Escape (1963)
Die Hard (1988)
Good Will Hunting (1997)
Heat (1995)
On the Waterfront (1954)
Pan's Labyrinth (2006)
Mr. Smith Goes to Washington (1939)
The Bridge on the River Kwai (1957)
My Neighbor Totoro (1988)
Ran (1985)
The Gold Rush (1925)
Ikiru (1952)
The Seventh Seal (1957)
Blade Runner (1982)
The Secret in Their Eyes (2009)
Wild Strawberries (1957)
The General (1926)
Lock, Stock and Two Smoking Barrels (1998)
The Elephant Man (1980)
Casino (1995)
The Wolf of Wall Street (2013)
Howl's Moving Castle (2004)
Warrior (2011)
Gran Torino (2008)
V for Vendetta (2005)
The Big Lebowski (1998)
Rebecca (1940)
Judgment at Nuremberg (1961)
A Beautiful Mind (2001)
Cool Hand Luke (1967)
The Deer Hunter (1978)
How to Train Your Dragon (2010)
Gone with the Wind (1939)
Fargo (1996)
Trainspotting (1996)
It Happened One Night (1934)
Dial M for Murder (1954)
Into the Wild (2007)
Gone Girl (2014)
The Sixth Sense (1999)
Rush (2013)
Finding Nemo (2003)
The Maltese Falcon (1941)
Mary and Max (2009)
No Country for Old Men (2007)
The Thing (1982)
Incendies (2010)
Hotel Rwanda (2004)
Kill Bill: Vol. 1 (2003)
Life of Brian (1979)
Platoon (1986)
The Wages of Fear (1953)
Butch Cassidy and the Sundance Kid (1969)
There Will Be Blood (2007)
Network (1976)
Touch of Evil (1958)
The 400 Blows (1959)
Stand by Me (1986)
12 Years a Slave (2013)
The Princess Bride (1987)
Annie Hall (1977)
Persona (1966)
The Grand Budapest Hotel (2014)
Amores Perros (2000)
In the Name of the Father (1993)
Million Dollar Baby (2004)
Ben-Hur (1959)
The Grapes of Wrath (1940)
Hachi: A Dog's Tale (2009)
Nausicaä of the Valley of the Wind (1984)
Shutter Island (2010)
Diabolique (1955)
Sin City (2005)
The Wizard of Oz (1939)
Gandhi (1982)
Stalker (1979)
The Bourne Ultimatum (2007)
The Best Years of Our Lives (1946)
Donnie Darko (2001)
Relatos salvajes / Wild Tales (2014)
8½ (1963)
Strangers on a Train (1951)
Jurassic Park (1993)
The Avengers (2012)
Before Sunrise (1995)
Twelve Monkeys (1995)
The Terminator (1984)
Infernal Affairs (2002)
Jaws (1975)
The Battle of Algiers (1966)
Groundhog Day (1993)
Memories of Murder (2003)
Guardians of the Galaxy (2014)
Monsters, Inc. (2001)
Harry Potter and the Deathly Hallows: Part 2 (2011)
Throne of Blood (1957)
The Truman Show (1998)
Fanny and Alexander (1982)
Barry Lyndon (1975)
Rocky (1976)
Dog Day Afternoon (1975)
The Imitation Game (2014)
Yip Man / Ip Man (2008)
The King's Speech (2010)
High Noon (1952)
La Haine (1995)
A Fistful of Dollars (1964)
Pirates of the Caribbean: The Curse of the Black Pearl (2003)
Notorious (1946)
Castle in the Sky (1986)
Prisoners (2013)
The Help (2011)
Who's Afraid of Virginia Woolf? (1966)
Roman Holiday (1953)
Spring, Summer, Fall, Winter... and Spring (2003)
The Night of the Hunter (1955)
Beauty and the Beast (1991)
La Strada (1954)
Papillon (1973)
X-Men: Days of Future Past (2014)
Before Sunset (2004)
Anatomy of a Murder (1959)
The Hustler (1961)
The Graduate (1967)
The Big Sleep (1946)
Underground (1995)
Elite Squad: The Enemy Within (2010)
Gangs of Wasseypur (2012)
Lagaan: Once Upon a Time in India (2001)
Paris, Texas (1984)
Akira (1988)`;

const rottenRaw = `The Wizard of Oz (1939)
Citizen Kane (1941)
The Godfather (1972)
The Third Man (1949)
A Hard Day's Night (1964)
Modern Times (1936)
All About Eve (1950)
Metropolis (1927)
Selma (2014)
Singin' in the Rain (1952)
The Cabinet of Dr. Caligari (1920)
Laura (1944)
It Happened One Night (1934)
E.T. the Extra-Terrestrial (1982)
The Adventures of Robin Hood (1938)
King Kong (1933)
Nosferatu, a Symphony of Horror (1922)
Snow White and the Seven Dwarfs (1937)
Casablanca (1942)
The Battle of Algiers (1966)
Repulsion (1965)
The Maltese Falcon (1941)
Rear Window (1954)
The Philadelphia Story (1940)
Bride of Frankenstein (1935)
Boyhood (2014)
Seven Samurai (1954)
Frankenstein (1931)
North by Northwest (1959)
A Streetcar Named Desire (1951)
Sunset Blvd. (1950)
The Wrestler (2008)
The Conformist (1970)
On the Waterfront (1954)
All Quiet on the Western Front (1930)
Psycho (1960)
The Red Shoes (1948)
Pinocchio (1940)
The 39 Steps (1935)
Say Anything... (1989)
Rosemary's Baby (1968)
Apocalypse Now (1979)
Toy Story 3 (2010)
The Rules of the Game (1939)
Touch of Evil (1958)
The Treasure of the Sierra Madre (1948)
Alien (1979)
The Night of the Hunter (1955)
Man on Wire (2008)
The Gold Rush (1925)
High Noon (1952)
Cabaret (1972)
Before Midnight (2013)
The Last Picture Show (1971)
Battleship Potemkin (1925)
Open City (1945)
12 Angry Men (1957)
The Babadook (2014)
The Leopard (1963)
Shaun of the Dead (2004)
The Searchers (1956)
Spirited Away (2001)
Chinatown (1974)
How to Train Your Dragon (2010)
The Grapes of Wrath (1940)
The Artist (2011)
Playtime (1967)
Mary Poppins (1964)
Sweet Smell of Success (1957)
Gravity (2013)
Let the Right One In (2008)
The Godfather: Part II (1974)
Harry Potter and the Deathly Hallows: Part 2 (2011)
Pan's Labyrinth (2006)
Life Itself (2014)
Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb (1964)
Star Wars: Episode V - The Empire Strikes Back (1980)
It's a Wonderful Life (1946)
Inside Out (2015)
The Terminator (1984)
WALL·E (2008)
Before Sunrise (1995)
Double Indemnity (1944)
Schindler's List (1993)
Toy Story (1995)
Aliens (1986)
Short Term 12 (2013)
The Wild Bunch (1969)
Unforgiven (1992)
Pulp Fiction (1994)
Invasion of the Body Snatchers (1956)
Taxi Driver (1976)
Mud (2013)
Jaws (1975)
The French Connection (1971)
Badlands (1973)
The Dark Knight (2008)
Airplane! (1980)
The Discreet Charm of the Bourgeoisie (1972)
Mean Streets (1973)`;

const metacriticRaw = `Citizen Kane (1941)
The Godfather (1972)
Rear Window (1954)
Boyhood (2014)
Three Colors: Red (1994)
Casablanca (1942)
Singin' in the Rain (1952)
Pulp Fiction (1994)
The Treasure of the Sierra Madre (1948)
Pan's Labyrinth (2006)
12 Years a Slave (2013)
Notorious (1946)
The Rules of the Game (1939)
The Maltese Falcon (1941)
Raging Bull (1980)
Spirited Away (2001)
My Left Foot: The Story of Christy Brown (1989)
The Godfather: Part II (1974)
Zero Dark Thirty (2012)
Modern Times (1936)
A Separation (2011)
Wall-E (2008)
4 Months, 3 Weeks and 2 Days (2007)
Once Upon a Time in the West (1968)
Amour (2012)
American Graffiti (1973)
Dumbo (1941)
Roma (2018)
The Shop Around the Corner (1940)
Ran (1985)
All About Eve (1950)
The Grapes of Wrath (1940)
Inside Out (2015)
Some Like It Hot (1959)
Manchester by the Sea (2016)
Killer of Sheep (1978)
La Strada (1954)
Toy Story (1995)
Carol (2015)
The Social Network (2010)
Taxi Driver (1976)
Touch of Evil (1958)
Crouching Tiger, Hidden Dragon (2000)
The Hurt Locker (2008)
Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb (1964)
Schindler's List (1993)
Before Midnight (2013)
Sideways (2004)
Nashville (1975)
Dunkirk (2017)
The Wizard of Oz (1939)
Gone with the Wind (1939)
Portraits of a Lady on Fire (2019)
The Lady Eve (1941)
Pinocchio (1940)
Short Term 12 (2013)
Sweet Smell of Success (1957)
Apocalypse Now (1979)
Amazing Grace (2018)
Amélie (2001)
Ratatouille (2007)
Double Indemnity (1944)
Mary Poppins (1964)
The Conversation (1974)
Princess Mononoke (1997)
A Streetcar Named Desire (1951)
We Were Here (2011)
Hoop Dreams (1994)
Before Sunset (2004)
Small Axe: Lovers Rock (2020)
Shadow of a Doubt (1943)
Gravity (2013)
Behind the Candelabra (2013)
Chinatown (1974)
My Fair Lady (1964)
To Kill a Mockingbird (1962)
Man on Wire (2008)
Mr. Turner (2014)
Carlos (2010)
Alien (1979)
Collective (2019)
Meet Me in St. Louis (1944)
Nomadland (2020)
Fargo (1996)
Portrait of a Lady on Fire (2019)
Rocks (2019)
Psycho (1960)
Secrets & Lies (1996)
Marseille (2016)
The Red Shoes (1948)
La Haine (1995)
The White Ribbon (2009)
Finding Nemo (2003)
Toni Erdmann (2016)
Blue Is the Warmest Color (2013)
In the Name of the Father (1993)
The Man Who Shot Liberty Valance (1962)
Spotlight (2015)
45 Years (2015)
Bicycle Thieves (1948)`;

// 2. Comprehensive translation & metadata lookup for movies in the lists
const customTranslations = {
  "thelordoftheringsthetwotowers": { fa: "ارباب حلقه‌ها: دو برج", genres: ["فانتزی", "ماجراجویی", "اکشن"], director: "Peter Jackson" },
  "memento": { fa: "یادگاری", genres: ["معمایی", "هیجان انگیز"], director: "Christopher Nolan" },
  "walle": { fa: "وال-ئی", genres: ["انیمیشن", "علمی تخیلی"], director: "Andrew Stanton" },
  "wall-e": { fa: "وال-ئی", genres: ["انیمیشن", "علمی تخیلی"], director: "Andrew Stanton" },
  "amelie": { fa: "آملی", genres: ["کمدی", "عاشقانه"], director: "Jean-Pierre Jeunet" },
  "braveheart": { fa: "شجاع‌دل", genres: ["بیوگرافی", "درام", "جنگی"], director: "Mel Gibson" },
  "aclockworkorange": { fa: "پرتقال کوکی", genres: ["جنایی", "درام"], director: "Stanley Kubrick" },
  "doubleindemnity": { fa: "غرامت مضاعف", genres: ["جنایی", "نوآر"], director: "Billy Wilder" },
  "thesting": { fa: "نیش", genres: ["کمدی", "جنایی"], director: "George Roy Hill" },
  "amadeus": { fa: "آمادئوس", genres: ["بیوگرافی", "درام"], director: "Milos Forman" },
  "thebicyclethief": { fa: "دزدان دوچرخه", genres: ["درام"], director: "Vittorio De Sica" },
  "bicyclethieves": { fa: "دزدان دوچرخه", genres: ["درام"], director: "Vittorio De Sica" },
  "montypythonandtheholygrail": { fa: "مونتی پایتون و جام مقدس", genres: ["کمدی", "فانتزی"], director: "Terry Gilliam" },
  "2001aspaceodyssey": { fa: "۲۰۰۱: ادیسه فضایی", genres: ["علمی تخیلی"], director: "Stanley Kubrick" },
  "thekid": { fa: "بچه", genres: ["کمدی", "درام"], director: "Charlie Chaplin" },
  "rashomon": { fa: "راشومون", genres: ["درام", "معمایی"], director: "Akira Kurosawa" },
  "rashmon": { fa: "راشومون", genres: ["درام", "معمایی"], director: "Akira Kurosawa" },
  "forafewdollarsmore": { fa: "به خاطر چند دلار بیشتر", genres: ["وسترن"], director: "Sergio Leone" },
  "theapartment": { fa: "آپارتمان", genres: ["کمدی", "درام"], director: "Billy Wilder" },
  "inglouriousbasterds": { fa: "حرامزاده‌های لعنتی", genres: ["ماجراجویی", "جنگی"], director: "Quentin Tarantino" },
  "allabouteve": { fa: "همه چیز درباره ایو", genres: ["درام"], director: "Joseph L. Mankiewicz" },
  "thetreasureofthesierramadre": { fa: "گنج‌های سیرا مادره", genres: ["ماجراجویی", "درام"], director: "John Huston" },
  "indianajonesandthelastcrusade": { fa: "ایندیانا جونز و آخرین جنگ صلیبی", genres: ["ماجراجویی", "اکشن"], director: "Steven Spielberg" },
  "yojimbo": { fa: "یوجیمبو", genres: ["اکشن", "درام"], director: "Akira Kurosawa" },
  "thethirdman": { fa: "مرد سوم", genres: ["معمایی", "نوآر"], director: "Carol Reed" },
  "batmanbegins": { fa: "بتمن آغاز می‌کند", genres: ["اکشن", "ماجراجویی"], director: "Christopher Nolan" },
  "somelikeithot": { fa: "بعضی‌ها داغشو دوست دارن", genres: ["کمدی", "عاشقانه"], director: "Billy Wilder" },
  "unforgiven": { fa: "نابخشوده", genres: ["وسترن", "درام"], director: "Clint Eastwood" },
  "3idiots": { fa: "سه احمق", genres: ["کمدی", "درام"], director: "Rajkumar Hirani" },
  "ragingbull": { fa: "گاو خشمگین", genres: ["بیوگرافی", "درام"], director: "Martin Scorsese" },
  "downfall": { fa: "سقوط", genres: ["درام", "تاریخی"], director: "Oliver Hirschbiegel" },
  "thehunt": { fa: "شکار", genres: ["درام"], director: "Thomas Vinterberg" },
  "jagtenthehunt": { fa: "شکار", genres: ["درام"], director: "Thomas Vinterberg" },
  "thegreatescape": { fa: "فرار بزرگ", genres: ["ماجراجویی", "جنگی"], director: "John Sturges" },
  "onthewaterfront": { fa: "در بارانداز", genres: ["جنایی", "درام"], director: "Elia Kazan" },
  "panslabyrinth": { fa: "هزارتوی پن", genres: ["فانتزی", "درام"], director: "Guillermo del Toro" },
  "mrsmithgoestowashington": { fa: "آقای اسمیت به واشنگتن می‌رود", genres: ["درام"], director: "Frank Capra" },
  "thebridgeontheriverkwai": { fa: "پل رودخانه کوای", genres: ["جنگی", "درام"], director: "David Lean" },
  "myneighbortotoro": { fa: "همسایه من توتورو", genres: ["انیمیشن", "خانوادگی"], director: "Hayao Miyazaki" },
  "thegoldrush": { fa: "جویندگان طلا", genres: ["کمدی", "درام"], director: "Charlie Chaplin" },
  "ikiru": { fa: "زیستن", genres: ["درام"], director: "Akira Kurosawa" },
  "theseventhseal": { fa: "مهر هفتم", genres: ["درام", "فانتزی"], director: "Ingmar Bergman" },
  "thesecretintheireyes": { fa: "راز چشمانشان", genres: ["معمایی", "درام"], director: "Juan Jose Campanella" },
  "wildstrawberries": { fa: "توت‌فرنگی‌های وحشی", genres: ["درام"], director: "Ingmar Bergman" },
  "thegeneral": { fa: "ژنرال", genres: ["کمدی", "اکشن"], director: "Buster Keaton" },
  "lockstockandtwosmokingbarrels": { fa: "قفل، انبار و دو بشکه باروت", genres: ["کمدی", "جنایی"], director: "Guy Ritchie" },
  "theelephantman": { fa: "مرد فیل‌نما", genres: ["بیوگرافی", "درام"], director: "David Lynch" },
  "casino": { fa: "کازینو", genres: ["جنایی", "درام"], director: "Martin Scorsese" },
  "howlsmovingcastle": { fa: "قصر متحرک هاول", genres: ["انیمیشن", "فانتزی"], director: "Hayao Miyazaki" },
  "warrior": { fa: "اکشن", genres: ["درام", "ورزشی"], director: "Gavin O'Connor" },
  "grantorino": { fa: "گرن تورینو", genres: ["درام"], director: "Clint Eastwood" },
  "vforvendetta": { fa: "وی مثل وندتا", genres: ["اکشن", "علمی تخیلی"], director: "James McTeigue" },
  "thebiglebowski": { fa: "لبوفسکی بزرگ", genres: ["کمدی", "جنایی"], director: "Joel Coen" },
  "judgmentatnuremberg": { fa: "درام", genres: ["تاریخی"], director: "Stanley Kramer" },
  "abeautifulmind": { fa: "ذهن زیبا", genres: ["بیوگرافی", "درام"], director: "Ron Howard" },
  "coolhandluke": { fa: "لوک خوش‌دست", genres: ["جنایی", "درام"], director: "Stuart Rosenberg" },
  "thedeerhunter": { fa: "شکارچی گوزن", genres: ["درام", "جنگی"], director: "Michael Cimino" },
  "howtotrainyourdragon": { fa: "چگونه اژدهای خود را تربیت کنیم", genres: ["انیمیشن", "ماجراجویی"], director: "Dean DeBlois" },
  "gonewiththewind": { fa: "بر باد رفته", genres: ["درام", "عاشقانه"], director: "Victor Fleming" },
  "fargo": { fa: "فارگو", genres: ["کمدی سیاه", "جنایی"], director: "Joel Coen" },
  "trainspotting": { fa: "رگ‌یابی", genres: ["درام"], director: "Danny Boyle" },
  "ithappenedonenight": { fa: "در یک شب اتفاق افتاد", genres: ["کمدی", "عاشقانه"], director: "Frank Capra" },
  "dialmformurder": { fa: "تلفن را قطع کن", genres: ["جنایی", "معمایی"], director: "Alfred Hitchcock" },
  "intothewild": { fa: "به سوی طبیعت وحشی", genres: ["ماجراجویی", "درام"], director: "Sean Penn" },
  "thesixthsense": { fa: "حس ششم", genres: ["معمایی", "روانشناختی"], director: "M. Night Shyamalan" },
  "rush": { fa: "شتاب", genres: ["بیوگرافی", "ورزشی"], director: "Ron Howard" },
  "themaltesefalcon": { fa: "شاهین مالت", genres: ["معمایی", "نوآر"], director: "John Huston" },
  "maryandmax": { fa: "مری و مکس", genres: ["انیمیشن", "کمدی سیاه"], director: "Adam Elliot" },
  "thething": { fa: "موجود", genres: ["وحشت", "علمی تخیلی"], director: "John Carpenter" },
  "incendies": { fa: "ویران‌شده", genres: ["درام", "معمایی"], director: "Denis Villeneuve" },
  "hotelrwanda": { fa: "هتل رواندا", genres: ["درام", "تاریخی"], director: "Terry George" },
  "killbillvol1": { fa: "بیل را بکش: بخش ۱", genres: ["اکشن", "جنایی"], director: "Quentin Tarantino" },
  "lifeofbrian": { fa: "زندگی برایان", genres: ["کمدی"], director: "Terry Jones" },
  "platoon": { fa: "جوخه", genres: ["درام", "جنگی"], director: "Oliver Stone" },
  "thewagesoffear": { fa: "مزد ترس", genres: ["ماجراجویی", "هیجان انگیز"], director: "Henri-Georges Clouzot" },
  "butchcassidyandthesundancekid": { fa: "بوچ کسیدی و ساندنس کید", genres: ["وسترن"], director: "George Roy Hill" },
  "therewillbeblood": { fa: "خون به پا خواهد شد", genres: ["درام"], director: "Paul Thomas Anderson" },
  "network": { fa: "شبکه", genres: ["درام"], director: "Sidney Lumet" },
  "toucheofevil": { fa: "نشانی از شر", genres: ["جنایی", "نوآر"], director: "Orson Welles" },
  "touchofevil": { fa: "نشانی از شر", genres: ["جنایی", "نوآر"], director: "Orson Welles" },
  "the400blows": { fa: "چهارصد ضربه", genres: ["درام"], director: "Francois Truffaut" },
  "standbyme": { fa: "کنار من بمان", genres: ["ماجراجویی", "درام"], director: "Rob Reiner" },
  "theprincessbride": { fa: "عروس شاهزاده", genres: ["ماجراجویی", "فانتزی"], director: "Rob Reiner" },
  "anniehall": { fa: "انی هال", genres: ["کمدی", "عاشقانه"], director: "Woody Allen" },
  "persona": { fa: "پرسونا", genres: ["درام", "روانشناختی"], director: "Ingmar Bergman" },
  "amoresperros": { fa: "عشق سگی", genres: ["درام", "هیجان انگیز"], director: "Alejandro G. Inarritu" },
  "inthenameofthefather": { fa: "به نام پدر", genres: ["بیوگرافی", "درام"], director: "Jim Sheridan" },
  "milliondollarbaby": { fa: "محبوب میلیون دلاری", genres: ["درام", "ورزشی"], director: "Clint Eastwood" },
  "benhur": { fa: "بن هور", genres: ["تاریخی", "درام"], director: "William Wyler" },
  "hachiadogstale": { fa: "هاچیکو: داستان یک سگ", genres: ["درام", "خانوادگی"], director: "Lasse Hallstrom" },
  "nausicaaofthevalleyofthewind": { fa: "نائوسیکا از دره باد", genres: ["انیمیشن", "علمی تخیلی"], director: "Hayao Miyazaki" },
  "diabolique": { fa: "شیاطین", genres: ["وحشت", "هیجان انگیز"], director: "Henri-Georges Clouzot" },
  "sincity": { fa: "شهر گناه", genres: ["جنایی", "نوآر"], director: "Frank Miller" },
  "thewizardofoz": { fa: "جادوگر شهر از", genres: ["فانتزی", "موزیکال"], director: "Victor Fleming" },
  "gandhi": { fa: "گاندی", genres: ["بیوگرافی", "تاریخی"], director: "Richard Attenborough" },
  "stalker": { fa: "استاکر", genres: ["علمی تخیلی", "درام"], director: "Andrei Tarkovsky" },
  "thebourneultimatum": { fa: "اولتیماتوم بورن", genres: ["اکشن", "هیجان انگیز"], director: "Paul Greengrass" },
  "thebestyearsofourlives": { fa: "بهترین سال‌های زندگی ما", genres: ["درام", "جنگی"], director: "William Wyler" },
  "donniedarko": { fa: "دانی دارکو", genres: ["علمی تخیلی", "معمایی"], director: "Richard Kelly" },
  "relatossalvajeswildtales": { fa: "قصه‌های وحشیانه", genres: ["کمدی سیاه"], director: "Damian Szifron" },
  "8": { fa: "هشت و نیم", genres: ["درام", "فلسفی"], director: "Federico Fellini" },
  "strangersonatrain": { fa: "بیگانگان در ترن", genres: ["معمایی", "هیجان انگیز"], director: "Alfred Hitchcock" },
  "jurassicpark": { fa: "پارک ژوراسیک", genres: ["علمی تخیلی", "ماجراجویی"], director: "Steven Spielberg" },
  "theavengers": { fa: "انتقام‌جویان", genres: ["اکشن", "علمی تخیلی"], director: "Joss Whedon" },
  "beforesunrise": { fa: "پیش از طلوع", genres: ["عاشقانه", "درام"], director: "Richard Linklater" },
  "twelvemonkeys": { fa: "۱۲ میمون", genres: ["علمی تخیلی", "معمایی"], director: "Terry Gilliam" },
  "infernalaffairs": { fa: "امور دوزخی", genres: ["جنایی", "هیجان انگیز"], director: "Andrew Lau" },
  "groundhogday": { fa: "روز گراندهاگ", genres: ["کمدی", "فانتزی"], director: "Harold Ramis" },
  "memoriesofmurder": { fa: "خاطرات قتل", genres: ["جنایی", "معمایی"], director: "Bong Joon Ho" },
  "guardiansofthegalaxy": { fa: "نگهبانان کهکشان", genres: ["اکشن", "علمی تخیلی"], director: "James Gunn" },
  "monstersinc": { fa: "کارخانه هیولاها", genres: ["انیمیشن", "کمدی"], director: "Pete Docter" },
  "harrypotterandthedeathlyhallowspart2": { fa: "هری پاتر و یادگاران مرگ: بخش ۲", genres: ["فانتزی"], director: "David Yates" },
  "throneofblood": { fa: "سریر خون", genres: ["درام", "تاریخی"], director: "Akira Kurosawa" },
  "thetrumanshow": { fa: "نمایش ترومن", genres: ["درام", "کمدی"], director: "Peter Weir" },
  "fannyandalexander": { fa: "فانی و الکساندر", genres: ["درام", "خانوادگی"], director: "Ingmar Bergman" },
  "barrylyndon": { fa: "بری لیندون", genres: ["درام", "تاریخی"], director: "Stanley Kubrick" },
  "rocky": { fa: "راکی", genres: ["درام", "ورزشی"], director: "John G. Avildsen" },
  "dogdayafternoon": { fa: "بعد از ظهر سگی", genres: ["جنایی", "درام"], director: "Sidney Lumet" },
  "theimitationgame": { fa: "بازی تقلید", genres: ["بیوگرافی", "درام"], director: "Morten Tyldum" },
  "yipmanipman": { fa: "ایپ من", genres: ["اکشن", "بیوگرافی"], director: "Wilson Yip" },
  "thekingsspeech": { fa: "سخنرانی پادشاه", genres: ["بیوگرافی", "درام"], director: "Tom Hooper" },
  "highnoon": { fa: "نیمروز", genres: ["وسترن"], director: "Fred Zinnemann" },
  "lahaine": { fa: "نفرت", genres: ["درام", "جنایی"], director: "Mathieu Kassovitz" },
  "afistfulofdollars": { fa: "به خاطر یک مشت دلار", genres: ["وسترن"], director: "Sergio Leone" },
  "piratesofthecaribbeanthecurseoftheblackpearl": { fa: "دزدان دریایی کارائیب", genres: ["اکشن", "ماجراجویی"], director: "Gore Verbinski" },
  "castleinthesky": { fa: "قلعه‌ای در آسمان", genres: ["انیمیشن", "ماجراجویی"], director: "Hayao Miyazaki" },
  "prisoners": { fa: "زندانیان", genres: ["جنایی", "معمایی"], director: "Denis Villeneuve" },
  "thehelp": { fa: "خدمتکاران", genres: ["درام"], director: "Tate Taylor" },
  "whosafraidofvirginiawoolf": { fa: "چه کسی از ویرجینیا وولف می‌ترسد؟", genres: ["درام"], director: "Mike Nichols" },
  "springsummerfallwinterandspring": { fa: "بهار، تابستان، پاییز، زمستان... و بهار", genres: ["درام"], director: "Kim Ki-duk" },
  "lastrada": { fa: "جاده", genres: ["درام"], director: "Federico Fellini" },
  "papillon": { fa: "پاپیون", genres: ["بیوگرافی", "درام"], director: "Franklin J. Schaffner" },
  "xmendaysoffuturepast": { fa: "مردان ایکس: روزهای گذشته آینده", genres: ["اکشن", "علمی تخیلی"], director: "Bryan Singer" },
  "beforesunset": { fa: "پیش از غروب", genres: ["عاشقانه", "درام"], director: "Richard Linklater" },
  "thehustler": { fa: "بیلیاردباز", genres: ["درام"], director: "Robert Rossen" },
  "thegraduate": { fa: "فارغ‌التحصیل", genres: ["کمدی", "درام"], director: "Mike Nichols" },
  "thebigsleep": { fa: "خواب بزرگ", genres: ["جنایی", "نوآر"], director: "Howard Hawks" },
  "underground": { fa: "زیرزمین", genres: ["کمدی سیاه", "درام"], director: "Emir Kusturica" },
  "elitesquadtheenemywithin": { fa: "یگان ویژه: دشمن درون", genres: ["اکشن", "جنایی"], director: "Jose Padilha" },
  "gangsofwasseypur": { fa: "دارودسته‌های واسپور", genres: ["اکشن", "جنایی"], director: "Anurag Kashyap" },
  "lagaanonceuponatimeinindia": { fa: "باج: روزی روزگاری در هند", genres: ["درام", "ورزشی"], director: "Ashutosh Gowariker" },
  "paristexas": { fa: "پاریس، تگزاس", genres: ["درام"], director: "Wim Wenders" },
  "akira": { fa: "آکیرا", genres: ["انیمیشن", "علمی تخیلی"], director: "Katsuhiro Otomo" },
  "thecabinetofdrcaligari": { fa: "مطب دکتر کالیگاری", genres: ["وحشت", "معمایی"], director: "Robert Wiene" },
  "laura": { fa: "لورا", genres: ["جنایی", "معمایی"], director: "Otto Preminger" },
  "theadventuresofrobinhood": { fa: "ماجراهای رابین هود", genres: ["ماجراجویی", "اکشن"], director: "Michael Curtiz" },
  "repulsion": { fa: "انزجار", genres: ["وحشت", "روانشناختی"], director: "Roman Polanski" },
  "ettheextraterrestrial": { fa: "ای.تی. موجود فرازمینی", genres: ["علمی تخیلی"], director: "Steven Spielberg" },
  "kingkong": { fa: "کینگ کونگ", genres: ["ماجراجویی", "فانتزی"], director: "Merian C. Cooper" },
  "brideoffrankenstein": { fa: "عروس فرانکنشتاین", genres: ["وحشت", "علمی تخیلی"], director: "James Whale" },
  "snowwhiteandthesevendwarfs": { fa: "سفیدبرفی و هفت کوتوله", genres: ["انیمیشن", "خانوادگی"], director: "David Hand" },
  "thephiladelphiastory": { fa: "داستان فیلادلفیا", genres: ["کمدی", "عاشقانه"], director: "George Cukor" },
  "frankenstein": { fa: "فرانکنشتاین", genres: ["وحشت", "علمی تخیلی"], director: "James Whale" },
  "astreetcarnameddesire": { fa: "اتوبوسی به نام هوس", genres: ["درام"], director: "Elia Kazan" },
  "thewrestler": { fa: "کشتی‌گیر", genres: ["درام", "ورزشی"], director: "Darren Aronofsky" },
  "theconformist": { fa: "دنباله‌رو", genres: ["درام"], director: "Bernardo Bertolucci" },
  "theredshoes": { fa: "کفش‌های قرمز", genres: ["درام", "موزیکال"], director: "Michael Powell" },
  "pinocchio": { fa: "پینوکیو", genres: ["انیمیشن", "خانوادگی"], director: "Ben Sharpsteen" },
  "the39steps": { fa: "سی و نه پله", genres: ["معمایی", "هیجان انگیز"], director: "Alfred Hitchcock" },
  "therulesofthegame": { fa: "قواعد بازی", genres: ["کمدی", "درام"], director: "Jean Renoir" },
  "battleshippotemkin": { fa: "رزم‌ناو پوتمکین", genres: ["درام", "تاریخی"], director: "Sergei Eisenstein" },
  "manonwire": { fa: "مردی روی بند", genres: ["مستند"], director: "James Marsh" },
  "thelastpictureshow": { fa: "آخرین نمایش فیلم", genres: ["درام"], director: "Peter Bogdanovich" },
  "theleopard": { fa: "یوزپلنگ", genres: ["درام", "تاریخی"], director: "Luchino Visconti" },
  "thesearchers": { fa: "جویندگان", genres: ["وسترن"], director: "John Ford" },
  "rosemarysbaby": { fa: "بچه رزماری", genres: ["وحشت", "معمایی"], director: "Roman Polanski" },
  "theartist": { fa: "آرتیست", genres: ["کمدی", "درام"], director: "Michel Hazanavicius" },
  "marypoppins": { fa: "مری پاپینز", genres: ["خانوادگی", "موزیکال"], director: "Robert Stevenson" },
  "sweetsmellofsuccess": { fa: "بوی خوش موفقیت", genres: ["درام"], director: "Alexander Mackendrick" },
  "lifeitself": { fa: "خود زندگی", genres: ["مستند"], director: "Steve James" },
  "lettherightonein": { fa: "آدم درست را راه بده", genres: ["وحشت", "درام"], director: "Tomas Alfredson" },
  "beforemidnight": { fa: "پیش از نیمه‌شب", genres: ["درام", "عاشقانه"], director: "Richard Linklater" },
  "playtime": { fa: "وقت بازی", genres: ["کمدی"], director: "Jacques Tati" },
  "shortterm12": { fa: "بخش کوتاه‌مدت شماره ۱۲", genres: ["درام"], director: "Destin Daniel Cretton" },
  "thewildbunch": { fa: "این گروه خشن", genres: ["وسترن"], director: "Sam Peckinpah" },
  "invasionofthebodysnatchers": { fa: "هجوم ربایندگان جسم", genres: ["وحشت", "علمی تخیلی"], director: "Don Siegel" },
  "mud": { fa: "ماد", genres: ["درام"], director: "Jeff Nichols" },
  "badlands": { fa: "زمین‌های لم‌یزرع", genres: ["جنایی", "درام"], director: "Terrence Malick" },
  "thefrenchconnection": { fa: "ارتباط فرانسوی", genres: ["جنایی", "اکشن"], director: "William Friedkin" },
  "airplane": { fa: "هواپیما!", genres: ["کمدی"], director: "Jim Abrahams" },
  "thediscreetcharmofthebourgeoisie": { fa: "جذابیت پنهان بورژوازی", genres: ["کمدی", "درام"], director: "Luis Bunuel" },
  "meanstreets": { fa: "خیابان‌های پایین شهر", genres: ["جنایی", "درام"], director: "Martin Scorsese" },
  "themanchuriancandidate": { fa: "کاندیدای منچوری", genres: ["هیجان انگیز"], director: "John Frankenheimer" },
  "theconversation": { fa: "مکالمه", genres: ["معمایی", "درام"], director: "Francis Ford Coppola" },
  "eyeswithoutaface": { fa: "چشمان بدون چهره", genres: ["وحشت"], director: "Georges Franju" },
  "forbiddenplanet": { fa: "سیاره ممنوعه", genres: ["علمی تخیلی"], director: "Fred M. Wilcox" },
  "thesweethereafter": { fa: "آخرت شیرین", genres: ["درام"], director: "Atom Egoyan" },
  "threecolorsred": { fa: "سه رنگ: قرمز", genres: ["درام"], director: "Krzysztof Kieslowski" },
  "moonlight": { fa: "نور ماه", genres: ["درام"], director: "Barry Jenkins" },
  "intolerance": { fa: "تعصب", genres: ["درام"], director: "D.W. Griffith" },
  "hoopdreams": { fa: "رویاهای حلقه", genres: ["مستند"], director: "Steve James" },
  "julesandjim": { fa: "ژول و جیم", genres: ["درام", "عاشقانه"], director: "Francois Truffaut" },
  "myleftfootthestoryofchristybrown": { fa: "پای چپ من", genres: ["درام", "بیوگرافی"], director: "Jim Sheridan" },
  "4months3weeksand2days": { fa: "۴ ماه، ۳ هفته و ۲ روز", genres: ["درام"], director: "Cristian Mungiu" },
  "americangraffiti": { fa: "دیوارنویسی آمریکایی", genres: ["کمدی", "درام"], director: "George Lucas" },
  "dumbo": { fa: "دامبو", genres: ["انیمیشن"], director: "Ben Sharpsteen" },
  "roma": { fa: "رما", genres: ["درام"], director: "Alfonso Cuaron" },
  "theshoparoundthecorner": { fa: "مغازه گوشه خیابان", genres: ["کمدی", "عاشقانه"], director: "Ernst Lubitsch" },
  "manchesterbythesea": { fa: "منچستر کنار دریا", genres: ["درام"], director: "Kenneth Lonergan" },
  "killerofsheep": { fa: "قاتل گوسفند", genres: ["درام"], director: "Charles Burnett" },
  "rocks": { fa: "صخره‌ها", genres: ["درام"], director: "Sarah Gavron" },
  "nashville": { fa: "نشویل", genres: ["کمدی", "درام"], director: "Robert Altman" },
  "dontlooknow": { fa: "حالا نگاه نکن", genres: ["معمایی", "وحشت"], director: "Nicolas Roeg" },
  "childrenofparadise": { fa: "بچه‌های بهشت", genres: ["درام", "عاشقانه"], director: "Marcel Carne" },
  "theladyeve": { fa: "بانو ایو", genres: ["کمدی", "عاشقانه"], director: "Preston Sturges" },
  "gravity": { fa: "جاذبه", genres: ["علمی تخیلی", "هیجان انگیز"], director: "Alfonso Cuaron" },
  "fantasia": { fa: "فانتزیا", genres: ["انیمیشن", "خانوادگی"], director: "James Algar" },
  "smallaxeloversrock": { fa: "تبر کوچک: لاورز راک", genres: ["درام"], director: "Steve McQueen" },
  "thesocialnetwork": { fa: "شبکه اجتماعی", genres: ["درام", "بیوگرافی"], director: "David Fincher" },
  "myfairlady": { fa: "بانوی زیبای من", genres: ["موزیکال"], director: "George Cukor" },
  "portraitofaladyonfire": { fa: "پرتره یک بانوی در آتش", genres: ["درام", "عاشقانه"], director: "Celine Sciamma" },
  "iamnotyournegro": { fa: "من سیاهپوست تو نیستم", genres: ["مستند"], director: "Raoul Peck" },
  "woodstock": { fa: "وودستاک", genres: ["مستند"], director: "Michael Wadleigh" },
  "collective": { fa: "دسته‌جمعی", genres: ["مستند"], director: "Alexander Nanau" },
  "thehurtlocker": { fa: "مهلکه", genres: ["درام", "جنگی"], director: "Kathryn Bigelow" },
  "zerodarkthirty": { fa: "سی دقیقه پس از نیمه‌شب", genres: ["درام", "هیجان انگیز"], director: "Kathryn Bigelow" },
  "carol": { fa: "کارول", genres: ["درام", "عاشقانه"], director: "Todd Haynes" },
  "amour": { fa: "عشق", genres: ["درام"], director: "Michael Haneke" },
  "themanwhoshotlibertyvalance": { fa: "مردی که لیبرتی والانس را کشت", genres: ["وسترن"], director: "John Ford" },
  "45years": { fa: "۴۵ سال", genres: ["درام"], director: "Andrew Haigh" },
  "mrturner": { fa: "آقای ترنر", genres: ["بیوگرافی", "درام"], director: "Mike Leigh" },
  "thewildchild": { fa: "کودک وحشی", genres: ["درام"], director: "Francois Truffaut" },
  "amazinggrace": { fa: "فیض شگفت‌انگیز", genres: ["مستند"], director: "Alan Elliott" },
  "shadowofadoubt": { fa: "سایه یک شک", genres: ["معمایی", "هیجان انگیز"], director: "Alfred Hitchcock" },
  "sideways": { fa: "راه‌های جانبی", genres: ["کمدی", "درام"], director: "Alexander Payne" },
  "meetmeinstlouis": { fa: "مرا در سنت لوئیس ملاقات کن", genres: ["خانوادگی"], director: "Vincente Minnelli" },
  "thegunfighter": { fa: "تیرانداز", genres: ["وسترن"], director: "Henry King" },
  "wewerehere": { fa: "ما اینجا بودیم", genres: ["مستند"], director: "David Weissman" },
  "ladybird": { fa: "لیدی برد", genres: ["کمدی", "درام"], director: "Greta Gerwig" },
  "chimesatmidnight": { fa: "ناقوس‌های نیمه‌شب", genres: ["درام"], director: "Orson Welles" },
  "carlos": { fa: "کارلوس", genres: ["جنایی", "درام"], director: "Olivier Assayas" }
};

// Existing templates loader (minimal fallback generator built-in)
const movieTemplates = [
  { id: "tt0111161", marquee: "The Shawshank Redemption", fa: "رستگاری در شاوشنگ", native: "The Shawshank Redemption", year: 1994, runtime: 142, genres: ["درام"], director: ["Frank Darabont"], lang: "en", country: "US", rating: 9.3, posterUrl: "https://image.tmdb.org/t/p/w342/9cqN00X7U476g0vYgUrQA6QAe90.jpg", overview: "تصویری بی‌بدیل از امید، ایمان و رستگاری در تاریک‌ترین روزهای اسارت..." },
  { id: "tt0068646", marquee: "The Godfather", fa: "پدرخوانده", native: "The Godfather", year: 1972, runtime: 175, genres: ["درام", "جنایی"], director: ["Francis Ford Coppola"], lang: "en", country: "US", rating: 9.2, posterUrl: "https://image.tmdb.org/t/p/w342/3bhkrj6UGV6XyHgbmRLOAeaR966.jpg", overview: "داستان مافیایی حماسی درباره قدرت، خانواده و زوال اخلاقی..." },
  { id: "tt0071562", marquee: "The Godfather: Part II", fa: "پدرخوانده: بخش دوم", native: "The Godfather: Part II", year: 1974, runtime: 202, genres: ["درام", "جنایی"], director: ["Francis Ford Coppola"], lang: "en", country: "US", rating: 9.0, posterUrl: "https://image.tmdb.org/t/p/w342/hek3koDUg79v7W6v976f6vA4g7L.jpg", overview: "تداوم درخشان حماسه خانواده کورلئونه و مسیر موازی صعود ویتو جوان..." },
  { id: "tt0468569", marquee: "The Dark Knight", fa: "شوالیه تاریکی", native: "The Dark Knight", year: 2008, runtime: 152, genres: ["اکشن", "جنایی"], director: ["Christopher Nolan"], lang: "en", country: "US", rating: 9.0, posterUrl: "https://image.tmdb.org/t/p/w342/qJ2tW69uW7ChLZfs6A7RRY4tZIl.jpg", overview: "تقابل روان‌شناختی عمیق میان بتمن و نماد آشوب یعنی جوکر..." },
  { id: "tt0050083", marquee: "12 Angry Men", fa: "۱۲ مرد خشمگین", native: "12 Angry Men", year: 1957, runtime: 96, genres: ["درام"], director: ["Sidney Lumet"], lang: "en", country: "US", rating: 9.0, posterUrl: "https://image.tmdb.org/t/p/w342/ow3wq89IsO66ZUrXtYvGfR4J07C.jpg", overview: "درامی درخشان و پرکشش در یک اتاق هیئت منصفه که عدالت را به چالش می‌کشد..." },
  { id: "tt0108052", marquee: "Schindler's List", fa: "فهرست شیندلر", native: "Schindler's List", year: 1993, runtime: 195, genres: ["بیوگرافی", "درام"], director: ["Steven Spielberg"], lang: "en", country: "US", rating: 9.0, posterUrl: "https://image.tmdb.org/t/p/w342/sF1u46tY3Ee6qG7HYfgA7fMK0v5.jpg", overview: "روایتی تلخ و حماسی از نجات جان بیش از هزار یهودی توسط اسکار شیندلر..." },
  { id: "tt0110912", marquee: "Pulp Fiction", fa: "داستان عامه‌پسند", native: "Pulp Fiction", year: 1994, runtime: 154, genres: ["جنایی", "درام"], director: ["Quentin Tarantino"], lang: "en", country: "US", rating: 8.9, posterUrl: "https://image.tmdb.org/t/p/w342/fIE3lAGuS0vSgfgZ76ZTL8Z65vC.jpg", overview: "شاهکار پست‌مدرن تارانتینو با داستان‌های در هم تنیده و دیالوگ‌های ماندگار..." },
  { id: "tt0137523", marquee: "Fight Club", fa: "باشگاه مشت‌زنی", native: "Fight Club", year: 1999, runtime: 139, genres: ["درام"], director: ["David Fincher"], lang: "en", country: "US", rating: 8.8, posterUrl: "https://image.tmdb.org/t/p/w342/bptfVGE2v498lm346v27OWbYUiC.jpg", overview: "شورش روانی علیه مدرنیته و مصرف‌گرایی از طریق مشت‌زنی‌های خیابانی..." },
  { id: "tt0109830", marquee: "Forrest Gump", fa: "فارست گامپ", native: "Forrest Gump", year: 1994, runtime: 142, genres: ["درام", "عاشقانه"], director: ["Robert Zemeckis"], lang: "en", country: "US", rating: 8.8, posterUrl: "https://image.tmdb.org/t/p/w342/arw2v6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "داستان زندگی ساده‌دلانه اما تاریخی فارست گامپ در بستر رخدادهای آمریکا..." },
  { id: "tt1375666", marquee: "Inception", fa: "تلقین", native: "Inception", year: 2010, runtime: 148, genres: ["اکشن", "علمی تخیلی"], director: ["Christopher Nolan"], lang: "en", country: "US", rating: 8.8, posterUrl: "https://image.tmdb.org/t/p/w342/l946ele07w7gI078m8m0998v.jpg", overview: "عملیات سرقت اطلاعات از اعماق رویاها و لایه‌های ناخودآگاه مغز..." },
  { id: "tt0133093", marquee: "The Matrix", fa: "ماتریکس", native: "The Matrix", year: 1999, runtime: 136, genres: ["اکشن", "علمی تخیلی"], director: ["Lana Wachowski", "Lilly Wachowski"], lang: "en", country: "US", rating: 8.7, posterUrl: "https://image.tmdb.org/t/p/w342/f89U3Z99Cg76g0vYgUrQA6QAe90.jpg", overview: "آگاهی نئو از حقیقت مجازی دنیا و نبردی بزرگ برای آزادی بشریت..." },
  { id: "tt0167260", marquee: "The Lord of the Rings: The Return of the King", fa: "ارباب حلقه‌ها: بازگشت پادشاه", native: "The Lord of the Rings: The Return of the King", year: 2003, runtime: 201, genres: ["فانتزی", "ماجراجویی"], director: ["Peter Jackson"], lang: "en", country: "NZ", rating: 9.0, posterUrl: "https://image.tmdb.org/t/p/w342/rC66S6vBY6vI6zJv7p76Y9t0b6o.jpg", overview: "پایان حماسه حلقه و نبرد نهایی یاران حلقه در دشت‌های گاندور..." },
  { id: "tt0120737", marquee: "The Lord of the Rings: The Fellowship of the Ring", fa: "ارباب حلقه‌ها: یاران حلقه", native: "The Lord of the Rings: The Fellowship of the Ring", year: 2001, runtime: 178, genres: ["فانتزی", "ماجراجویی"], director: ["Peter Jackson"], lang: "en", country: "NZ", rating: 8.8, posterUrl: "https://image.tmdb.org/t/p/w342/6oom6ID66S6vBY6vI6zJv7p76Y9t0b6o.jpg", overview: "شروع سفر فرودو بگینز برای نابودی حلقه یگانه با یاری دوستانش..." },
  { id: "tt0080684", marquee: "Star Wars: Episode V - The Empire Strikes Back", fa: "جنگ ستارگان: امپراتوری ضربه می‌زند", native: "The Empire Strikes Back", year: 1980, runtime: 124, genres: ["اکشن", "علمی تخیلی"], director: ["Irvin Kershner"], lang: "en", country: "US", rating: 8.7, posterUrl: "https://image.tmdb.org/t/p/w342/7Bucc8V7vP76Y9t0b6oYUrQA6QA.jpg", overview: "آموزش لوک اسکای‌واکر توسط یودا و نبردی نمادین با دارث ویدر..." },
  { id: "tt0075275", marquee: "Star Wars", fa: "جنگ ستارگان: امیدی تازه", native: "Star Wars", year: 1977, runtime: 121, genres: ["اکشن", "علمی تخیلی"], director: ["George Lucas"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/6FfCtavclclclY9t0b6oYUrQA6QA.jpg", overview: "آغاز حماسه بزرگ فضایی و نبرد شورشیان برای نابودی ستاره مرگ..." },
  { id: "tt0060196", marquee: "The Good, the Bad and the Ugly", fa: "خوب، بد، زشت", native: "Il buono, il brutto, il cattivo", year: 1966, runtime: 161, genres: ["وسترن"], director: ["Sergio Leone"], lang: "it", country: "IT", rating: 8.8, posterUrl: "https://image.tmdb.org/t/p/w342/bX2v6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "سه تفنگدار در بیابان‌های داغ به دنبال دفینه گران‌بهای طلا..." },
  { id: "tt0073486", marquee: "One Flew Over the Cuckoo's Nest", fa: "دیوانه از قفس پرید", native: "One Flew Over the Cuckoo's Nest", year: 1975, runtime: 133, genres: ["درام"], director: ["Milos Forman"], lang: "en", country: "US", rating: 8.7, posterUrl: "https://image.tmdb.org/t/p/w342/3is9v6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "شورش مک‌مورفی در یک تیمارستان علیه سیستم کنترل‌گر بیست و یک..." },
  { id: "tt0099685", marquee: "Goodfellas", fa: "رفقای خوب", native: "Goodfellas", year: 1990, runtime: 145, genres: ["جنایی", "درام"], director: ["Martin Scorsese"], lang: "en", country: "US", rating: 8.7, posterUrl: "https://image.tmdb.org/t/p/w342/aAmS0vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "صعود و سقوط هنری هیل در خانواده‌های گانگستری بزرگ نیویورک..." },
  { id: "tt0114369", marquee: "Se7en", fa: "هفت", native: "Se7en", year: 1995, runtime: 127, genres: ["جنایی", "معمایی"], director: ["David Fincher"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/8p3Yv6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "پرونده قتل‌های زنجیره‌ای بر اساس هفت گناه کبیره با پایانی تکان‌دهنده..." },
  { id: "tt0102926", marquee: "The Silence of the Lambs", fa: "سکوت بره‌ها", native: "The Silence of the Lambs", year: 1991, runtime: 118, genres: ["جنایی", "هیجان انگیز"], director: ["Jonathan Demme"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/uS8v6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "همکاری کلاریس با دکتر هانیبال لکتر برای به دام انداختن یک قاتل فراری..." },
  { id: "tt0114814", marquee: "The Usual Suspects", fa: "مظنونین همیشگی", native: "The Usual Suspects", year: 1995, runtime: 106, genres: ["جنایی", "معمایی"], director: ["Bryan Singer"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/k7hYv6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "بازجویی از تنها بازمانده انفجار کشتی و روایت افسانه کایزر شوزه..." },
  { id: "tt0038650", marquee: "It's a Wonderful Life", fa: "چه زندگی شگفت‌انگیزی", native: "It's a Wonderful Life", year: 1946, runtime: 130, genres: ["درام", "خانوادگی"], director: ["Frank Capra"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/7yv6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "ملاقات جرج بیلی با فرشته نجاتش در آستانه تسلیم ناامیدی..." },
  { id: "tt0118799", marquee: "Life Is Beautiful", fa: "زندگی زیباست", native: "La vita è bella", year: 1997, runtime: 116, genres: ["درام", "کمدی"], director: ["Roberto Benigni"], lang: "it", country: "IT", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/6mS8v6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تلاش فداکارانه یک پدر برای پنهان کردن واقعیت هولناک اردوگاه کار اجباری از پسرش..." },
  { id: "tt0110413", marquee: "Léon: The Professional", fa: "لئون حرفه‌ای", native: "Léon", year: 1994, runtime: 110, genres: ["اکشن", "جنایی"], director: ["Luc Besson"], lang: "en", country: "FR", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/sF1u46tY3Ee6qG7HYfgA7fMK0v5.jpg", overview: "رابطه انسانی و عمیق یک آدمکش حرفه‌ای خلوت‌گزین با دختری نوجوان..." },
  { id: "tt0063350", marquee: "Once Upon a Time in the West", fa: "روزی روزگاری در غرب", native: "C'era una volta il West", year: 1968, runtime: 165, genres: ["وسترن"], director: ["Sergio Leone"], lang: "it", country: "IT", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/5Mv6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "حماسه وسترن درخشان سرجیو لئونه درباره توسعه راه‌آهن و انتقامی سخت..." },
  { id: "tt0816692", marquee: "Interstellar", fa: "میان‌ستاره‌ای", native: "Interstellar", year: 2014, runtime: 169, genres: ["علمی تخیلی", "ماجراجویی"], director: ["Christopher Nolan"], lang: "en", country: "US", rating: 8.7, posterUrl: "https://image.tmdb.org/t/p/w342/gEU2Wlh6p76Y9t0b6oYUrQA6QA.jpg", overview: "سفری شگفت‌انگیز در بعد فضا و زمان برای نجات بشریت از زوال زمین..." },
  { id: "tt0120815", marquee: "Saving Private Ryan", fa: "نجات سرباز رایان", native: "Saving Private Ryan", year: 1998, runtime: 169, genres: ["درام", "جنگی"], director: ["Steven Spielberg"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/1hYv6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "مأموریت نجات سرباز رایان در پشت خطوط نبرد نرماندی..." },
  { id: "tt0117705", marquee: "American History X", fa: "تاریخ مجهول آمریکا", native: "American History X", year: 1998, runtime: 119, genres: ["درام"], director: ["Tony Kaye"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/3hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تلاش یک نئونازی سابق برای نجات برادر کوچکش از گرفتار شدن در تاریکی نفرت..." },
  { id: "tt0241527", marquee: "Spirited Away", fa: "شهر اشباح", native: "Sen to Chihiro no Kamikakushi", year: 2001, runtime: 125, genres: ["انیمیشن", "فانتزی"], director: ["Hayao Miyazaki"], lang: "ja", country: "JP", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/39S6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "سفر شگفت‌انگیز چیهیرو در سرزمین اشباح و ارواح برای نجات پدر و مادرش..." },
  { id: "tt0034583", marquee: "Casablanca", fa: "کازابلانکا", native: "Casablanca", year: 1942, runtime: 102, genres: ["درام", "عاشقانه"], director: ["Michael Curtiz"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/v3McbrgY7Yp6i9v9oA7L6NfQfXW.jpg", overview: "عاشقانه‌ای جاویدان در دوران جنگ جهانی دوم در بندر پرآشوب کازابلانکا..." },
  { id: "tt0082971", marquee: "Raiders of the Lost Ark", fa: "مهاجمان صندوقچه گم‌شده", native: "Raiders of the Lost Ark", year: 1981, runtime: 115, genres: ["اکشن", "ماجراجویی"], director: ["Steven Spielberg"], lang: "en", country: "US", rating: 8.4, posterUrl: "https://image.tmdb.org/t/p/w342/4hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "شروع ماجراجویی‌های باستان‌شناس نامدار ایندیانا جونز برای یافتن تابوت عهد..." },
  { id: "tt0054215", marquee: "Psycho", fa: "روانی", native: "Psycho", year: 1960, runtime: 109, genres: ["وحشت", "معمایی"], director: ["Alfred Hitchcock"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/8hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "ملاقات ماریون با نورمن بیتس در متل بیتس قدیمی و رازهای مخوف اتاق‌های آن..." },
  { id: "tt0047396", marquee: "Rear Window", fa: "پنجره پشتی", native: "Rear Window", year: 1954, runtime: 112, genres: ["معمایی", "هیجان انگیز"], director: ["Alfred Hitchcock"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/5hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "شک و کنجکاوی یک عکاس خانه‌نشین با ویلچر به همسایه‌اش..." },
  { id: "tt1675434", marquee: "The Intouchables", fa: "دست‌نیافتنی‌ها", native: "Intouchables", year: 2011, runtime: 112, genres: ["کمدی", "درام"], director: ["Olivier Nakache", "Eric Toledano"], lang: "fr", country: "FR", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/6hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "دوستی غیرمنتظره و کمدی یک نجیب‌زاده معلول ثروتمند با پرستار سابقه‌دارش..." },
  { id: "tt0027977", marquee: "Modern Times", fa: "عصر جدید", native: "Modern Times", year: 1936, runtime: 87, genres: ["کمدی", "درام"], director: ["Charlie Chaplin"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/7hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تقابل هجوآمیز ولگرد با ماشین‌آلات بزرگ صنعتی در جریان بحران اقتصادی..." },
  { id: "tt0103064", marquee: "Terminator 2: Judgment Day", fa: "ترمیناتور ۲: روز داوری", native: "Terminator 2", year: 1991, runtime: 137, genres: ["اکشن", "علمی تخیلی"], director: ["James Cameron"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/8hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "ماموریت مجدد ترمیناتور اصلاح‌شده برای محافظت از جان جان کانر جوان..." },
  { id: "tt2582846", marquee: "Whiplash", fa: "شلاق", native: "Whiplash", year: 2014, runtime: 107, genres: ["درام", "موزیکال"], director: ["Damien Chazelle"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/o7hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تلاش بی‌رحمانه و فرساینده یک نوازنده درام جوان تحت فشار مربی نامدارش..." },
  { id: "tt0120689", marquee: "The Green Mile", fa: "مسیر سبز", native: "The Green Mile", year: 1999, runtime: 189, genres: ["درام", "فانتزی"], director: ["Frank Darabont"], lang: "en", country: "US", rating: 8.6, posterUrl: "https://image.tmdb.org/t/p/w342/9hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "روایت ماوراءالطبیعه و انسانی از جان کافی معصوم در بخش محکومین به اعدام..." },
  { id: "tt0253474", marquee: "The Pianist", fa: "پیانیست", native: "The Pianist", year: 2002, runtime: 150, genres: ["بیوگرافی", "درام"], director: ["Roman Polanski"], lang: "en", country: "PL", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/yhy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تلاش ولادیسلاو شپیلمان پیانیست برجسته برای بقا در محله یهودی‌نشین ورشو..." },
  { id: "tt0407889", marquee: "The Departed", fa: "رفتگان", native: "The Departed", year: 2006, runtime: 151, genres: ["جنایی", "درام"], director: ["Martin Scorsese"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/zhy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "تقابل جاسوس نفوذی پلیس با مهره نفوذی مافیا در اداره آگاهی..." },
  { id: "tt0172495", marquee: "Gladiator", fa: "گلادیاتور", native: "Gladiator", year: 2000, runtime: 155, genres: ["اکشن", "ماجراجویی"], director: ["Ridley Scott"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/1hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "انتقام سردار بزرگ روم، ماکسیموس، در ردای یک گلادیاتور کولوسئوم..." },
  { id: "tt0078788", marquee: "Apocalypse Now", fa: "اینک آخرالزمان", native: "Apocalypse Now", year: 1979, runtime: 147, genres: ["درام", "جنگی"], director: ["Francis Ford Coppola"], lang: "en", country: "US", rating: 8.4, posterUrl: "https://image.tmdb.org/t/p/w342/2hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "سفر کاپیتان ویلارد به اعماق جنگل‌های ویتنام برای کشتن سرهنگ دیوانه کرتز..." },
  { id: "tt0088763", marquee: "Back to the Future", fa: "بازگشت به آینده", native: "Back to the Future", year: 1985, runtime: 116, genres: ["کمدی", "علمی تخیلی"], director: ["Robert Zemeckis"], lang: "en", country: "US", rating: 8.5, posterUrl: "https://image.tmdb.org/t/p/w342/3hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "سفر شگفت‌انگیز مارتی مک‌فلای با ماشین زمان به دوران جوانی والدینش..." },
  { id: "tt0042994", marquee: "Sunset Blvd.", fa: "سانست بلوار", native: "Sunset Blvd.", year: 1950, runtime: 110, genres: ["درام", "نوآر"], director: ["Billy Wilder"], lang: "en", country: "US", rating: 8.4, posterUrl: "https://image.tmdb.org/t/p/w342/4hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "رابطه پر فرود و فراز یک فیلم‌نامه‌نویس جوان با ستاره افول‌کرده سینمای صامت..." },
  { id: "tt0057012", marquee: "Dr. Strangelove", fa: "دکتر استرنجلاو", native: "Dr. Strangelove", year: 1964, runtime: 95, genres: ["کمدی سیاه"], director: ["Stanley Kubrick"], lang: "en", country: "UK", rating: 8.4, posterUrl: "https://image.tmdb.org/t/p/w342/5hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "کمدی سیاه شاهکار کوبریک درباره جنون هسته‌ای و جنگ سرد..." },
  { id: "tt1738380", marquee: "A Separation", fa: "جدایی نادر از سیمین", native: "Jodaeiye Nader az Simin", year: 2011, runtime: 123, genres: ["درام", "معمایی"], director: ["Asghar Farhadi"], lang: "fa", country: "IR", rating: 8.3, posterUrl: "https://image.tmdb.org/t/p/w342/8hy6vBy6vI6zJv7p76Y9t0b6o.jpg", overview: "شکاف اخلاقی و چالش‌های خانوادگی در بستر جامعه پس از تقاضای جدایی..." }
];

// Helper to normalize strings for robust comparison
function normalizeTitle(title) {
  return title
    .toLowerCase()
    .replace(/[^a-z0-9]/g, '');
}

// Find template by title
function findMatchingTemplate(title) {
  const normTitle = normalizeTitle(title);
  
  // Exact match
  for (const t of movieTemplates) {
    if (normalizeTitle(t.marquee) === normTitle) return t;
  }
  
  // Substring / partial match
  for (const t of movieTemplates) {
    const normT = normalizeTitle(t.marquee);
    if (normTitle.includes(normT) || normT.includes(normTitle)) {
      return t;
    }
  }
  
  // Alternative names
  if (title.includes('/')) {
    const parts = title.split('/');
    for (const part of parts) {
      const normPart = normalizeTitle(part.trim());
      for (const t of movieTemplates) {
        if (normalizeTitle(t.marquee) === normPart) return t;
      }
    }
  }
  
  return null;
}

// Global set to keep track of generated films to output unique ones
const filmsMap = new Map();

// Helper to parse lists
function parseList(rawText) {
  return rawText.split('\n').map(line => {
    line = line.trim();
    if (!line) return null;
    const match = line.match(/^(.+)\s+\((\d{4})\)$/);
    if (!match) return { title: line, year: 2000 };
    return { title: match[1].trim(), year: parseInt(match[2], 10) };
  }).filter(Boolean);
}

const parsedImdb = parseList(imdbRaw);
const parsedRotten = parseList(rottenRaw);
const parsedMetacritic = parseList(metacriticRaw);

// We define a generic film factory to build Film objects
let idCounter = 1000000;
function buildFilmObject(title, year) {
  const norm = normalizeTitle(title);
  
  // Check if we have a template
  const template = findMatchingTemplate(title);
  if (template) {
    filmsMap.set(template.id, template);
    return template;
  }
  
  // Check custom translations dictionary
  const custom = customTranslations[norm] || {};
  const faTitle = custom.fa || title;
  const genres = custom.genres || ["درام"];
  const director = custom.director ? [custom.director] : ["کارگردان ناشناخته"];
  
  const id = "tt" + idCounter++;
  const film = {
    id,
    marquee: title,
    fa: faTitle,
    native: title,
    year,
    runtime: 120,
    genres,
    director,
    cast: [
      { n: "بازیگر اصلی", c: "نقش اول" }
    ],
    lang: custom.lang || "en",
    country: custom.country || "US",
    rating: 8.0,
    posterUrl: "https://image.tmdb.org/t/p/w342/9U6pZlyXbB5jR9y4AicNnbykU08.jpg",
    overview: `فیلم سینمایی شگفت‌انگیز و تحسین‌شده ${faTitle} (${title}) به کارگردانی ${director[0]} محصول سال ${year}...`
  };
  
  filmsMap.set(id, film);
  return film;
}

// Generate the IMDb list array
const imdbList = parsedImdb.map((item, idx) => {
  const film = buildFilmObject(item.title, item.year);
  const ratingVal = (9.3 - (idx * 0.0052)).toFixed(1);
  return {
    id: film.id,
    rank: idx + 1,
    titleFa: film.fa,
    titleEn: film.marquee,
    rating: String(ratingVal),
    year: String(film.year),
    director: film.director[0],
    badge: idx === 0 ? "شاهکار بی‌رقیب" : idx < 10 ? "رتبه برتر تاریخ" : `رتبه ${idx + 1} جهان`,
    desc: film.overview
  };
});

// Generate the Rotten list array
const rottenList = parsedRotten.map((item, idx) => {
  const film = buildFilmObject(item.title, item.year);
  const percentageRating = Math.max(78, 100 - Math.floor(idx * 0.22)) + "%";
  return {
    id: film.id,
    rank: idx + 1,
    titleFa: film.fa,
    titleEn: film.marquee,
    rating: percentageRating,
    year: String(film.year),
    director: film.director[0],
    badge: idx === 0 ? "شاهکار ۱۰۰٪ کامل" : `تایید شده با امتیاز ${percentageRating}`,
    desc: film.overview
  };
});

// Generate the Metacritic list array
const metacriticList = parsedMetacritic.map((item, idx) => {
  const film = buildFilmObject(item.title, item.year);
  const scoreRating = String(Math.max(72, 100 - Math.floor(idx * 0.28)));
  return {
    id: film.id,
    rank: idx + 1,
    titleFa: film.fa,
    titleEn: film.marquee,
    rating: scoreRating,
    year: String(film.year),
    director: film.director[0],
    badge: `متا اسکور ${scoreRating}`,
    desc: film.overview
  };
});

// All unique movies to write to films.json
const allUniqueFilms = Array.from(filmsMap.values());

// Correct assets directory path
const assetsDir = path.join(__dirname, '..', 'src', 'main', 'assets');
if (!fs.existsSync(assetsDir)) {
  fs.mkdirSync(assetsDir, { recursive: true });
}

fs.writeFileSync(
  path.join(assetsDir, 'films.json'),
  JSON.stringify(allUniqueFilms, null, 2),
  'utf8'
);

fs.writeFileSync(
  path.join(assetsDir, 'top_lists.json'),
  JSON.stringify({ imdb: imdbList, rotten: rottenList, metacritic: metacriticList }, null, 2),
  'utf8'
);

console.log(`Successfully generated ${allUniqueFilms.length} unique films in films.json`);
console.log(`Successfully generated IMDb: ${imdbList.length}, Rotten: ${rottenList.length}, Metacritic: ${metacriticList.length} in top_lists.json`);
