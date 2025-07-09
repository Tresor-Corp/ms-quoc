FROM gradle:jdk21-alpine AS build
LABEL authors="bravos"

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN openssl genrsa -out private.pem 2048 && \
    openssl rsa -in private.pem -pubout -out public.pem && \
    gradle build -x test --no-daemon

FROM azul/zulu-openjdk-alpine:21-jre-headless-latest AS runtime
WORKDIR /app
COPY --from=build /app/private.pem ./
COPY --from=build /app/public.pem ./
COPY --from=build /app/build/libs/*.jar jwt-auth.jar
ENV DB_DRIVER="org.postgresql.Driver" \
    DB_URL="postgresql://postgres:5432/jwt_auth_service?user=bravos&password=4gi5uKqCtmYRRqRyoJTmu9U"
ENTRYPOINT ["java", "-Xms1g", "-Xmx1g", "-XX:+UseContainerSupport", "-Duser.timezone=GMT+7", "-jar", "jwt-auth.jar"]