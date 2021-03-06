package com.central.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.assets.getAsset
import ktx.assets.load

enum class Images {
    title_background,
    crab_logo,
    end,
    game_over,
    crab_sheet,
    default;

    val path = "images/${name}.png"
    fun load() = manager.load<Texture>(path)
    operator fun invoke() = manager.getAsset<Texture>(path)
    companion object {
        lateinit var manager: AssetManager
    }
}