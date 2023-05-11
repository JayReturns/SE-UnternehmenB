#!/bin/sh

mkdir -p Backend/ssp/src/main/resources/
echo "${SERVICE_ACCOUNT_KEY}" > Backend/ssp/src/main/resources/serviceAccountKey.json

mkdir -p Frontend/src/environments
echo "${ANGULAR_ENVIRONMENT}" > Frontend/src/environments/environment.ts
