#!/usr/bin/env bash
rm -rf node_modules/react-native-maps
npm install 
cd android
./gradlew app:build
