FROM openjdk:22
MAINTAINER vxrpenter

COPY /build/libs/SCP_Tools-1.0.0-all.jar bot.jar

RUN java -jar bot.jar