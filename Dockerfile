FROM oraclelinux:8

RUN dnf install -y oraclelinux-release-el8 && \
    dnf install -y java-21-openjdk

WORKDIR /app

CMD ["mvn","package"]

COPY target/bravosshop-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9898

CMD ["java","-jar","app.jar"]