FROM openjdk:10-jdk
RUN git clone https://gitlab.com/1MansiS/JavaCrypto.git
WORKDIR JavaCrypto/JavaCryptoModule
RUN ./gradlew clean compileJava assemble --stacktrace
WORKDIR ../SecureCryptoMicroservice
RUN ./gradlew clean compileJava assemble --stacktrace
ENTRYPOINT ["java", "-jar" , "build/libs/SecureCryptoMicroservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080

