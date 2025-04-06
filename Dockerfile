ARG VERSION=1.0.0

FROM openjdk:22

RUN mkdir /bot
ADD build/libs/SCP_Tools-1.0.0-all.jar /bot
WORKDIR /bot

CMD ["java","-jar","SCP_Tools-1.0.0-all.jar"]