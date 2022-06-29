
# FILMES ADATBÁZIS

* menthetünk filmeket és sorozatokat.
* ezeket ha töröljük akkor archiválva lesznek, így könnyen újra elérhetőek ha 
meg akarnánk őket később nézni. 
* akár egyéni felhasználók is használhatják mint filmes adatbázis, hogy mit láttak, vagy 
szeretnének kibérelni, de akár weboldalak is mint pl imdb vagy netflix. 
* sok funkció nincs benne az egyszerűség kedvéért pl User/Rent/Rating/watchedCounter


-Világos Szilárd

-----------------------------
eredeti readme leírás:




### Vizsgaremek

A feladatod egy backend API projekt elkészítése, általad választott témában.  
A témákhoz összeszedtünk néhány ötletet, kérlek írd be magad ahhoz a témához, amit te választanál. Érdemes mindenkinek egyedi alkalmazást készíteni, próbáljatok meg osztozkodni a témákon.  
Nem csak ezek közül a témák közül lehet választani, ha saját ötleted van, akkor nyugodtan írd hozzá a listához.

[témaötletek](https://docs.google.com/document/d/1ct21ZzbqeV0_Zvw_0k_dwjtEQVKa7aLqE49pB1Uq1eI/edit?usp=sharing)

#### Követelmények

* Maven projekt
* Spring Boot alkalmazás
* REST API, Swagger, OpenAPI dokumentáció
* SQL backend (pl. MySQL, MariaDB)
* Flyway sémamigráció, SQL táblalétrehozás, adatbetöltés
* Hibakezelés
* Spring Data JPA repository
* Integrációs tesztek
* Konténerizált alkalmazás

#### Feladat nagysága

* Legalább két 1-n kapcsolatban lévő tábla
* Legalább két SQL migráció
* Legalább két entitás
* Legalább két controller
* Minden bemenő paraméter validálása
* Legalább egy property beolvasása
* Minden HTTP metódusra legalább egy végpont (`GET`, `POST`, `PUT`, `DELETE`)
* Legalább 60%-os tesztlefedettség, amely tartalmaz egység és integrációs teszteket is
* Egy `Dockerfile`
