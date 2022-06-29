## Csak a MySQL fut konténerben, az alkalmazást IDEA-ban futtatjátok (fejlesztés közben)

##### docker hálózat létrehozása
```docker network create filmdatabasenetwork```

##### docker mysql konténer létrehozása
```docker run --name filmdatabasedb --network filmdatabasenetwork -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=filmdatabase -d -p 3307:3306 mysql:latest```

Ha van felrakva mysql a gépetekre és fut is, akkor a portot meg kell változtatni, pl ```-p 3307:3306```-ra. Fontos, ilyenkor javítsátok az ```application.yaml```-ben is.

## Az alkalmazás és az adatbázis is konténerben fut

A docker hálózatot és a mysql konténert ugyanúgy kell létrehozni (vagy ha már létre lett hozva korábban, akkor nincs vele további teendő).

##### alkalmazás buildelése
1. a ```pom.xml```-ben fel kell venni a ```spring-boot-maven-plugin```-t a build pluginek közé, hogy futtatható jar file-t hozzon létre a maven
2. az ```application.yaml```-ben javítani kell az adatbázis url-jét
3. konzolból le kell futtatni a ```mvn clean package``` utasítást a ```jpa-with-mysql``` mappában állva    
   (ha a tesztek futtatása sokáig tart, akkor lehet a ```mvn clean package -DskipTests``` utasítást helyette)

##### docker image létrehozása
1. létre kell hozni a Dockerfile-t és megírni a tartalmát (ebben a projektben kész van)
2. ```docker build -t videogameapp .``` (a ponttal együtt!)

##### docker konténer létrehozása és indítása
```docker run --name videogameapp --network filmdatabasenetwork -p 8080:8080 -d videogameapp```

## Alkalmazás újrabuildelelése (pl új funckió fejlesztése után)
1. ```mvn clean package```
2. ```docker build -t videogameapp .``` (ponttal együtt)
3. le kell állítani a már futó konténert  
   ```docker stop videogameapp```  
   (le tudjátok ellenőrizni ```docker ps -a```-val, hogy leállt)
4. ki kell törölni a leállított konténert  
   ```docker rm videogameapp```
5. ```docker run --name videogameapp --network filmdatabasenetwork -p 8080:8080 -d videogameapp```