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

### Test d'authentification pour l'admin

```
export USER="admin"
export SECRET="secretkey"
export MESSAGE="${USER}|$(date +"%s%3N")"
export TOKEN=$(echo -n $MESSAGE | openssl sha256 -hmac $SECRET | awk '{print $2}')
export PASSWORD="${MESSAGE}|${TOKEN}"
curl -v --user ${USER}:${PASSWORD} http://localhost:8080/alfresco/service/index
```

Use https://www.jokecamp.com/blog/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#shell
