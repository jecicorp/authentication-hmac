## JECI - Authentication HMac

## Lancement de la stack locale

Vous pouvez démarrer l'application en local avec `docker-compose`

```
mvn package
mvn resources:resources
pip install docker-compose
docker-compose -f ./target/classes/docker-compose.yml up --build -d
```

ou `podman-compose`

```
mvn package
mvn resources:resources
pip install podman-compose
podman-compose -f ./target/classes/docker-compose.yml build
```

Vous pouvez ensuite accéder aux applications avec les urls suivantes :

* [ACS](http://localhost:8080/alfresco) : http://localhost:8080/alfresco
* [Share](http://localhost:8080/share) : http://localhost:8080/share
