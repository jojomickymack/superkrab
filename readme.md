# SuperKrab

![superkrab.gif](.github/superkrab.gif?raw=true)

This is a refactor of 
[SuperKoalio](https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/superkoalio/SuperKoalio.java) - a well known libgdx 'hello world' single file platformer game.

The original game is a 'test' that demonstates loading a map and performing collision on it. Since I've used the same collision methods in a lot of other games, I wanted to put it into practice here (it's just been refactored a little and broken out into a 'MapManager' class).

The original game is kind of a poor starting point for a project, because it's easy to break and in order to be extended it really needs to be spread out into different files. This project is a lot more 'tweakable' - I hope if you're trying to make a platformer you can 'get there quicker' by starting from here.

This game also utilizes [libktx](https://libktx.github.io/) - specifically the following features

- [KtxGame and KtxScreen](https://github.com/libktx/ktx/tree/master/app) - handy entrypoint to launch the game and manage different screens

- [KtxScene2d Actors](https://github.com/libktx/ktx/tree/master/actors) - adds += syntax to add actors to a stage and add actions to actors, 
like fadeIn and moveTo.

- [KtxAssets](https://github.com/libktx/ktx/tree/master/assets) - enhances AssetManager usage with enum classes representing sounds, images, and music

There is a [precompiled jar](.github/superkrab.jar?raw=true) you can run. The idea is this project could make a good jumping off point to extend the tiled map and add your own assets.

Music and sounds were taken from freesound at the links below.

intro queue
https://freesound.org/people/Joykey/sounds/393384/
Joykey

game theme
https://freesound.org/people/LoopPacks/sounds/200939/
LoopPacks

game over noise
https://freesound.org/people/ProjectsU012/sounds/333782/
ProjectsU012

game over queue
https://freesound.org/people/michorvath/sounds/412343/
michorvath

