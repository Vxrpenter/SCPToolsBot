FROM eclipse-temurin:22-jdk AS build

WORKDIR /SCP_Tools
COPY . .
RUN ./gradlew shadowjar --no-daemon
RUN ls build/libs/
FROM eclipse-temurin:22-jre

WORKDIR /bot
COPY --from=build /SCP_Tools/build/libs/SCP_Tools-1.1.0-beta1-all.jar .

CMD ["java","--enable-native-access=ALL-UNNAMED","-jar","SCP_Tools-1.1.0-beta1-all.jar"]