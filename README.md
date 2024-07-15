# SimpleMusicPlayer

[![Firebase Distribution](https://github.com/PitikTrondol/SimpleMusicPlayer/actions/workflows/testAndDeploy.yml/badge.svg)](https://github.com/PitikTrondol/SimpleMusicPlayer/actions/workflows/testAndDeploy.yml)
[![GitHub Downloads (specific asset, specific tag)](https://img.shields.io/github/downloads/PitikTrondol/SimpleMusicPlayer/v0.0.1-alpha/app-debug.apk)](https://github.com/PitikTrondol/SimpleMusicPlayer/releases/tag/v0.0.1-alpha)


## Developers
Afriandi Haryanto (emailafriandi@gmail.com)

## Main Feature
This project is using `MVVM` as its architecture with Kotlin `Coroutines` as asynchronous handler.
State exposed with `StateFlow` and using reactive implementation as much as possible.
Main feature of player using `ExoPlayer` and its utilities.
For some reason, playlist still handled independently from Player.

Implemented features:
 - Gist: https://gist.github.com/PitikTrondol/03c76ce55e73f088372e146055fc9d7e
 - Get playlist from github gist (unfortunately could't find a free music API)
 - Playing automatically on uninterupted
 - Select song and play it

## Unit Test
 - Scoped only for ViewModel at the moment
 - Using JUnit 5 and mockk as the main library 
 - Run `./gradlew test` to start the process

## Generate APK
 - Run `./gradlew assembleDebug` to start the process

## Deployment
 - Currently deployed to Firebase App Distribution with email invitation
 - Github tag created manually
 - Trigger on merge or push to branch `main`




``
Sorry to inform you that the player control button is messy when programmatically change its visibility.
Still unsolved.
``

# Thanks for your attention
