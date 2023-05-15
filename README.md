# Software Engineering Unternehnem B

[Aufgabenteilung](https://docs.google.com/document/d/1td_Cq1KWGvkMBdMc_GPDeqOcdD4ZtIBVWQttYoDQt-c/edit?usp=sharing)
[Google Drive Ordner](https://drive.google.com/drive/folders/1qd48g0qEr9dvK1HfBh9PEq5oQJ_m3qTh?usp=sharing)

## Installation for development

### Requirements
- Angular CLI (`npm install -g @angular/cli`)
- Java w/ JDK Version >= 17

### Frontend
1. Switch into the `Frontend/` Directory
2. Generate the necessary environment-files using `ng generate environments`
3. Copy the pinned content found in the Discord-Channel _#Frontend_ into the `src/environments/environment.ts` and `src/environments/environment.development.ts`
4. Run `npm install` and `ng serve`
5. Open http://localhost:4200

### Backend
1. Switch to the `Backend/ssp` Directory
2. Create the File `src/main/resources/serviceAccountKey.json`
3. Copy the pinned content found in the Discord-Channel _#Backend_ into this file
4. Replace `${MONGO_URI}` in `src/main/resources/application.properties` with the pinned Mongo URI found in the _#Backend_ Discord Channel
5. Run `gradlew.bat build` and `java -jar build\libs\ssp\ssp-0.0.1-SNAPSHOT.jar`