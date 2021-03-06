package com.central.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import ktx.assets.getAsset
import ktx.assets.load

enum class Tunes {
    intro,
    game_theme,
    ending;

    val path = "tunes/${name}.ogg"
    fun load() = manager.load<Music>(path)
    operator fun invoke() = manager.getAsset<Music>(path)

    companion object {
        lateinit var manager: AssetManager
    }
}