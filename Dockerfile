FROM eclipse-temurin:22-jdk as BUILD

COPY . /src
WORKDIR /src
RUN ./gradlew --no-daemon shadowjar

FROM eclipse-temurin:22-jre

ARG VERSION="1.1.0-alpha3"

RUN mkdir /bot
COPY --from=BUILD /src/build/libs/SCP_Tools-${VERSION}.jar /bot
WORKDIR /bot

CMD ["java","--enable-native-access=ALL-UNNAMED","-jar","SCP_Tools-1.1.0-alpha3.jar"]