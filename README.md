## JECI - Authentication HMac

## Description

Ajout d'un subsystem pour l'authentification avec hMac

## Compilation

```
mvn clean package -DskipTests
```

### Installation

Ajout dans le fichier `alfresco-global.properties`

```
authentication.chaine=hMac_name:hMac,...
hMac.authentication.secretKey=secretkey
hMac.authentication.validateTimeToken=00:05:00
```

## Test

```
mvn resources:resources
pip install docker-compose
docker-compose -f ./target/classes/docker-compose.yml up --build -d
```
