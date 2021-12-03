mvn clean package -DskipTests
docker build . -t pplflw-api -f docker/Dockerfile
cd docker/
docker-compose --env-file .env up


