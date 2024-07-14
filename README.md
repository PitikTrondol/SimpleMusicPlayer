# SimpleMusicPlayer

[![Firebase Distribution](https://github.com/PitikTrondol/SimpleMusicPlayer/actions/workflows/testAndDeploy.yml/badge.svg)](https://github.com/PitikTrondol/SimpleMusicPlayer/actions/workflows/testAndDeploy.yml)
![GitHub Downloads (all assets, specific tag)](https://img.shields.io/github/downloads/PitikTrondol/SimpleMusicPlayer/v0.0.1-alpha/total)



## Developers
Afriandi Haryanto 
emailafriandi@gmail.com

## Main Feature
This project is using `MVVM` as its architecture with Kotlin `Coroutines` as asynchronous handler.
State exposed with `StateFlow` and using reactive implementation as much as possible.
Main feature of player using `ExoPlayer` and its utilities.
For some reason, playlist still handled independently from Player.

Implemented features:
 - Get playlist from github gist (unfortunately could't find a free music API)
 - Playing automatically on uninterupted
 - Select song and play it

## Unit Test
 - Scoped only for ViewModel at the moment
 - Using JUnit 5 and mockk as the main library 
 - Run `./gradlew test` to start the process

## Deployment
 - Currently deployed to Firebase App Distribution with email invitation
 - Github tag created manually


# Thanks for your attention