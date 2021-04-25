#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY / /home/app/
WORKDIR /home/app/
RUN ls -la
RUN mvn clean install -e -DskipTests=true

FROM scratch
COPY --from=build /home/app/application/data/target/data-0.0.1-SNAPSHOT.jar /
COPY --from=build /home/app/application/nearest/target/nearest-0.0.1-SNAPSHOT.jar /