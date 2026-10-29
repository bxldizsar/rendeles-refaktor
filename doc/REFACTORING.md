# Refaktorálás: a RendelesKezelo God class lebontása

## Kiindulás

Az alapállapot a **`f61add1`** commit: az eredeti `RendelesKezelo` változatlanul, valamint 12 jellemző teszt (`RendelesKezeloJellemzoTest`), amelyek a mostani kimenetet rögzítik. A tesztek a refaktor egyetlen lépésében sem változtak, csak a `kezelo()` és a `bevetel()` segédmetódusuk, amely a kezelőt összerakja.

Diagramok: [`docs/elotte.png`](docs/elotte.png) és [`docs/utana.png`](docs/utana.png).

## Code smellek és hogy melyik lépés szüntette meg

| # | Code smell | Hol volt | Megszüntette |
|---|---|---|---|
| 1 | **Hiányzó kapcsos zárójelek, wildcard import** (`import java.util.*`) | egysoros `if`/`for` törzsek | formázás commit |
| 2 | **Long Method** | a `feldolgoz()` mindent egy metódusban csinált | Extract Method (a validator commitban) |
| 3 | **God Class / SRP-sértés**: 6 felelősség egy osztályban | validálás, árazás, kedvezmény, mentés, értesítés, számla | validator, kedvezmény, tár, értesítő és számlaformázó commitok |
| 4 | **Típuskódra épülő `if`/`else` lánc** (`tipus.equals("VIP")`) | kedvezmények | kedvezményszabályok commit (`KedvezmenySzabaly` lista, OCP) |
| 5 | **Globális, módosítható `static` állapot** (`osszesBevetel`) | bevétel | tárolás commit (`KonzolosTar` példánymezője) |
| 6 | **Bedrótozott függőség** (`System.out` közvetlenül) | DB INSERT és EMAIL | tárolás és értesítő commit (`PrintStream` a konstruktorban, DIP) |
| 7 | **Long Parameter List** és **párhuzamos listák** (`termekek` + `arak`) | `feldolgoz(...)` 5–6 paramétere | részben: a `Rendeles` record (Introduce Parameter Object). A nyilvános `feldolgoz` szignatúra a jellemző tesztek miatt marad. |

## Lépések

1. **Eredeti kód + jellemző tesztek** (`f61add1`): 12 teszt, köztük az ismert hibáké is.
2. **Formázás**: Allman-zárójelek, explicit import, a viselkedés nem változott.
3. **Extract Method + Validator**: a `feldolgoz()` beszédes privát metódusokra bomlott, majd a validálás a `RendelesValidator` / `AlapValidator` mögé került, konstruktor-injektálással. (Az Extract Method nem kapott külön commitot, a validatorral együtt került be.)
4. **Kedvezményszabályok**: `KedvezmenySzabaly` interfész, `VipKedvezmeny`, `DiakKedvezmeny`, `NagyRendelesKedvezmeny`. A kezelő egy listán megy végig, és a lista sorrendje számít.
5. **Tárolás `static` nélkül**: `RendelesTar` / `KonzolosTar(PrintStream)`, a bevétel példányszintű.
6. **Értesítő**: `Ertesito` / `EmailErtesito` / `SmsErtesito`. Az e-mail → SMS csere egyetlen konstruktorparaméter. Mockito-teszt (`ErtesitoTest`). A 6. commitban a jellemző teszt fájlja tévedésből csonka lett, ez a commit állította vissza.
7. **Számlaformázó**: `SzamlaFormazo` / `SzovegesSzamla`.
8. **Kuponkód (új funkció, OCP)**: új `KuponKedvezmeny` osztály. A meglévő szabályokhoz nem kellett hozzányúlni, csak egy új elem került a listába. A `Rendeles` kapott egy `kupon` mezőt, plusz egy kupon nélküli konstruktort, így a régi hívások változatlanok.

## Eredmény

- A `RendelesKezelo` csak összehangol: 5 függőséget kap a konstruktorban (validator, szabálylista, tár, értesítő, számlaformázó), és **nem hoz létre** egyiket sem `new`-val.
- Nincs `static` állapot.
- 27 JUnit teszt: 12 jellemző + 15 egységteszt, köztük egy Mockitóval mockolt értesítővel.

## Ismert hibák (szándékosan nem javítva)

A 4. tipp szerint refaktor közben nem javítunk hibát. Ezeket a jellemző tesztek rögzítik, a javítás külön commit lehetne.

- `null` e-mail → `NullPointerException` (a validator `email.contains`-t hív null-ellenőrzés nélkül).
- `null` típus → `NullPointerException` a kedvezményszabályokban.
- Ha az `arak` lista rövidebb, mint a `termekek`, `IndexOutOfBoundsException` jön.
- A `%.2f` a rendszer locale-jától függ: magyar Windowson `150,00` jelenik meg, angolon `150.00`.
- Pénzösszegek `double`-ben (kerekítési hibák lehetnek, `BigDecimal` lenne a helyes).
