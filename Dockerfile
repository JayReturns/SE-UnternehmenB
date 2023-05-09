FROM node:latest

# Add Evnironment Variables
ARG MONGO_URI
ARG SERVICE_ACCOUNT_KEY

ENV MONGO_URI=$MONGO_URI
ENV SERVICE_ACCOUNT_KEY=$SERVICE_ACCOUNT_KEY

# Install Java
RUN apt-get update && apt-get install openjdk-17-jdk -y

# Install Gradle
ENV GRADLE_HOME=/gradle-8.1.1
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN curl -L https://services.gradle.org/distributions/gradle-8.1.1-bin.zip -o gradle-8.1.1-bin.zip &&\
    unzip gradle-8.1.1-bin.zip


COPY container-setup.sh .

# Add & Install frontend
RUN mkdir Frontend
ADD Frontend /Frontend

# Add Backend files
RUN mkdir -p Backend/ssp
ADD Backend/ssp /Backend/ssp
RUN chmod +x container-setup.sh
RUN /container-setup.sh


# Install Backend & Copy Angular to frontend
WORKDIR /Backend/ssp
RUN gradle installAngular
RUN gradle build


# Run
EXPOSE 8080
CMD java -jar build/libs/ssp-0.0.1-SNAPSHOT.jar