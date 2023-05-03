FROM node:latest

# Install Java
RUN apt-get update && apt-get install openjdk-17-jre-headless openjdk-17-jdk -y

# Install Gradle
ENV GRADLE_HOME=/gradle-8.1.1
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN curl -L https://services.gradle.org/distributions/gradle-8.1.1-bin.zip -o gradle-8.1.1-bin.zip &&\
    unzip gradle-8.1.1-bin.zip

# Add & Install frontend
RUN mkdir Frontend
ADD Frontend /Frontend

# Add Backend files
RUN mkdir -p Backend/ssp
ADD Backend/ssp /Backend/ssp

# Install Backend & Copy Angular to frontend
WORKDIR /Backend/ssp
RUN gradle installAngular
RUN gradle build


# Run
EXPOSE 8080
CMD java -jar build/libs/ssp-0.0.1-SNAPSHOT.jar
# CMD ng serve --host 0.0.0.0