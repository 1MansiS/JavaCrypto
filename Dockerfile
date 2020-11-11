FROM adoptopenjdk/openjdk15
RUN java --version
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-6.7-bin.zip -o  gradle-6.7-bin.zip
RUN apt-get update
RUN apt-get install zip unzip
RUN unzip gradle-6.7-bin.zip
ENV GRADLE_HOME=/gradle/gradle-6.7
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN gradle --version

RUN apt-get install -q -y git 
RUN git --version

RUN git clone https://github.com/1MansiS/JavaCrypto.git
WORKDIR JavaCrypto/JavaCryptoModule
RUN gradle clean compileJava assemble --stacktrace
WORKDIR ../SecureCryptoMicroservice
RUN gradle clean compileJava assemble --stacktrace
ENTRYPOINT ["java", "-jar" , "build/libs/SecureCryptoMicroservice-0.0.1-SNAPSHOT.jar"]
#EXPOSE 8080

