FROM openjdk:18-alpine
RUN apk update && apk add git zip unzip curl
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-7.4.2-bin.zip -o gradle-7.4.2-bin.zip 
RUN unzip gradle-7.4.2-bin.zip
ENV GRADLE_HOME=/gradle/gradle-7.4.2
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN git clone https://github.com/1MansiS/JavaCrypto.git
WORKDIR JavaCrypto/JavaCryptoModule
RUN gradle clean compileJava assemble --stacktrace
WORKDIR ../SecureCryptoMicroservice
RUN gradle clean compileJava assemble --stacktrace
ENTRYPOINT ["java", "-jar" , "build/libs/SecureCryptoMicroservice-0.0.2-SNAPSHOT.jar"]
EXPOSE 8080

