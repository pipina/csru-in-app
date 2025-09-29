FROM ubuntu:20.04 

ENV TZ Europe/Bratislava
ENV PATH=$PATH:/opt/java/jdk-17.0.2/bin
ENV JAVA_HOME=/opt/java/jdk-17.0.2

COPY openjdk-17.0.2.tar.gz /opt/java/
COPY urso-in-app-1.0-SNAPSHOT.jar /usr/app/
COPY application.properties /usr/app/

WORKDIR /opt/java

RUN tar -xzf openjdk-17.0.2.tar.gz && \
    rm -rf openjdk-17.0.2.tar.gz

RUN apt-get update -y && \
	apt-get install curl -y && \
	apt-get install wget -y && \
	apt-get install openssh-client -y;

WORKDIR "/usr"
RUN mkdir -p app

WORKDIR /usr/app

EXPOSE 8080
LABEL version="0.1-dev"

ENTRYPOINT ["java", "-jar", "urso-in-app-1.0-SNAPSHOT.jar","--spring.config.location=file:./application.properties"]
