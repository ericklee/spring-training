FROM openjdk:11
ADD ./target/account-svc-0.0.1-SNAPSHOT.jar /opt/app/account-svc.jar
WORKDIR /opt/app
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/account-svc.jar"]