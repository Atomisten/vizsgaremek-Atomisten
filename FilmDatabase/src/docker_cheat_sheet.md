##### docker hálózat létrehozása
```docker network create filmdatabasenetwork```

##### docker mysql konténer létrehozása
```docker run --name filmdatabasedb --network filmdatabasenetwork -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=filmdatabase -d -p 3307:3306 mysql:latest```