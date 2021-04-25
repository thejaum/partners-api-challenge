# partners-api-challenge

docker container run -it --name mvn001 -v F:\desenv\challenges\partners-api-challenge:/usr/src/mymaven -w /usr/src/mymaven maven:3.6.0-jdk-11-slim mvn clean package -DskipTests