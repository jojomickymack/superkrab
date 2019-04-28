package com.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.central.App
import com.central.assets.Images.*
import com.central.assets.Tunes.*
import ktx.actors.*
import ktx.actors.plusAssign
import ktx.app.KtxScreen


class End(val app: App) : KtxScreen {
    private val background = Image(end())
    private val logo = Image(game_over())

    override fun show() {
        ending().volume = 0.5f
        ending().play()

        with(background) {
            setSize(app.cam.viewportWidth, app.cam.viewportHeight)
            alpha = 0f
            this += sequence(fadeIn(2f), delay(3f))
        }

        with(logo) {
            setSize(app.cam.viewportWidth / 1.2f, app.cam.viewportHeight / 1.2f)
            setPosition(app.cam.viewportWidth / 2 - logo.width / 2, app.cam.viewportHeight)
            this += sequence(
                    moveTo(app.cam.viewportWidth / 2 - logo.width / 2, app.cam.viewportHeight / 2 - logo.height / 2, 3f),
                    delay(2f)
            )
        }

        app.stg += background
        app.stg += logo
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        with(app) {
            stg.act(delta)
            stg.draw()

            hudStg.act(delta)
            hudStg.draw()
        }

        checkInput()
    }

    override fun hide() {
        background.clearActions()
        logo.clearActions()
        app.stg.clear()
        ending().stop()
    }

    fun checkInput() {
        if (app.ic.aPressed || app.ic.bPressed || app.ic.lPressed || app.ic.rPressed) {
            app.setScreen<Game>()
        }
    }
}