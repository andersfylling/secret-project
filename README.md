# secret-project
previous repository: https://github.com/andersfylling/ntnu-imt3673-project

## Health
| Branch       | Build status  |
| ------------ |:-------------:|
| Master       | [![CircleCI](https://circleci.com/gh/andersfylling/secret-project/tree/master.svg?style=svg)](https://circleci.com/gh/andersfylling/secret-project/tree/master)    |
| Develop      | [![CircleCI](https://circleci.com/gh/andersfylling/secret-project/tree/develop.svg?style=svg)](https://circleci.com/gh/andersfylling/secret-project/tree/develop)   |



## About
Game developed for android course at NTNU. Builds upon the previous project (lab 3). The game is developed by Anders Fylling and Cim Stordal.

The game functionality works something like this: https://www.youtube.com/watch?v=gOtb5YnYrYk
The objective is to survive for as long as possible and if you touch the bottom of your
mobile screen you die.

## Project Code Organisation
The project code is organised as "Package by feature". For more information about this, please refer to Wiki/Architecture

## Features

- [x] Auto generation of level using a seed
- [x] Collision detection
- [x] Gravity
- [x] Jumping
- [x] In game buffs

## Changes needed before you using this project
There is two changes needed when setting this project up. One is to start the game server, and change the server URL using the settings menu in the game application. Please refer to the Wiki for more information about this. 
The second is to change the res/values/ids.xml to point to your own Google play project. Such that you are allowed to sign it. If not, the Google Play integration would not work for you.

## Pictures

![Just implemented floors/levels](https://i.imgur.com/ElGWEom.jpg)
