# https://spring.io/guides/topicals/spring-boot-docker/
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/build/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV COBBY_AUTH_TIMEZONE="Asia/Seoul"

### REQUIRED
# ENV COBBY_AUTH_PASSWORD
###

ENTRYPOINT ["java","-Djasypt.encryptor.password=${COBBY_AUTH_PASSWORD}","-Duser.timezone=${COBBY_AUTH_TIMEZONE}}","-cp","app:app/lib/*","com.cobby.auth.AuthApplication"]