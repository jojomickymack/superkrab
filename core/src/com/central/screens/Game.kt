package com.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.central.App
import com.central.actors.Krab
import com.central.managers.MapManager
import com.central.assets.Tunes.*
import com.central.assets.Sounds.*
import ktx.actors.plusAssign
import ktx.app.KtxScreen


/** Super Mario Brothers-like very basic platformer, using a tile map built using [Tiled](http://www.mapeditor.org/)
 *
 * Shows simple platformer collision detection - based on SuperKoalio
 * https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/superkoalio/SuperKoalio.java
 * @author mzechner
 */
class Game(val app: App) : KtxScreen {

    // load the map, set the unit scale to 1/16 (1 unit == 32 pixels)

    private var mapManager = MapManager()
    private var renderer = OrthogonalTiledMapRenderer(mapManager.map, 1 / 32f)

    // create the Koala we want to move around the world
    private var krab = Krab()
    private val gravity = -2.5f

    private var debugRenderer = ShapeRenderer()

    override fun show() {
        app.stg += krab

        // create an orthographic camera, shows us 24x18 units of the world
        app.cam.setToOrtho(false, 24f, 18f)
        app.cam.update()

        krab.x = 20f
        krab.y = 20f

        game_theme().play()
        game_theme().isLooping = true
    }

    override fun render(delta: Float) {
        // clear the screen
        Gdx.gl.glClearColor(0.8f, 0.6f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // set the TiledMapRenderer view based on what the
        // camera sees, and render the map
        renderer.setView(app.cam)
        renderer.render()

        // apply gravity if we are falling
        krab.velocity.y += gravity

        krab.handleInput(app)

        // update the krab (process input, position update)
        app.stg.act()
        app.hudStg.act()

        // let the camera follow the krab, x-axis only
        app.cam.position.x = krab.x
        app.cam.update()

        mapManager.collideWithKoala(krab)

        // render the krab (and anything else that's been added to the stage)
        app.stg.draw()
        // this 'layer' has the on screen gamepad
        app.hudStg.draw()

        // render debug rectangles
        if (app.debug) renderDebug()

        // if the krab falls off the screen, the game ends
        if (krab.y < 0) {
            die().play()
            app.ic.aPressed = false
            app.ic.bPressed = false
            app.ic.lPressed = false
            app.ic.rPressed = false
            app.setScreen<End>()
        }
    }

    private fun renderDebug() {
        debugRenderer.projectionMatrix = app.cam.combined
        debugRenderer.begin(ShapeType.Line)

        debugRenderer.color = Color.RED
        debugRenderer.rect(krab.x, krab.y, krab.width, krab.height)

        mapManager.renderDebug(debugRenderer, app.cam)
    }

    override fun hide() {
        app.stg.clear()
        game_theme().stop()
        app.cam.position.x = app.cam.viewportWidth / 2
        app.cam.position.y = app.cam.viewportHeight / 2
    }

    override fun dispose() {
        renderer.dispose()
        debugRenderer.dispose()
        super.dispose()
    }
}