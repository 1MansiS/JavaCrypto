FROM openjdk
RUN yum -y install git
RUN yum -y install unzip
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-6.6-rc-6-bin.zip -o gradle-6.6-rc-6-bin.zip
RUN unzip gradle-6.6-rc-6-bin.zip
ENV GRADLE_HOME=/gradle/gradle-6.6-rc-6
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN gradle --version
RUN git --version

RUN git clone https://gitlab.com/1MansiS/JavaCrypto.git
WORKDIR JavaCrypto/JavaCryptoModule
RUN gradle clean compileJava assemble --stacktrace
WORKDIR ../SecureCryptoMicroservice
RUN gradle clean compileJava assemble --stacktrace
ENTRYPOINT ["java", "-jar" , "build/libs/SecureCryptoMicroservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080

