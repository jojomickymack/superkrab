package com.central

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.app.KtxGame
import com.central.input.GamepadCtl
import com.central.input.InputCtl
import com.central.input.OnScreenGamepad
import com.central.screens.*
import com.central.assets.Images
import com.central.assets.Sounds
import com.central.assets.Tunes
import ktx.actors.plusAssign
import ktx.scene2d.Scene2DSkin


class App : KtxGame<Screen>() {

    private var width = 0f
    private var height = 0f

    private lateinit var sb: SpriteBatch
    private lateinit var view: StretchViewport

    lateinit var cam: OrthographicCamera
    lateinit var stg: Stage

    lateinit var hudSb: SpriteBatch
    lateinit var hudCam: OrthographicCamera
    lateinit var hudView: StretchViewport
    lateinit var hudStg: Stage

    lateinit var ic: InputCtl
    lateinit var gpc: GamepadCtl
    lateinit var osc: OnScreenGamepad

    val textureManager = AssetManager()
    val soundsManager = AssetManager()
    val tunesManager = AssetManager()

    var debug = false

    override fun create() {

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("skin/my_skin.json"))

        Images.manager = this.textureManager
        Sounds.manager = this.soundsManager
        Tunes.manager = this.tunesManager

        this.width = Gdx.graphics.height.toFloat()
        this.height = Gdx.graphics.width.toFloat()

        this.sb = SpriteBatch()

        this.cam = OrthographicCamera(this.width, this.height)
        this.view = StretchViewport(480f, 360f, this.cam)
        this.stg = Stage(this.view, this.sb)

        this.hudSb = SpriteBatch()
        this.hudCam = OrthographicCamera(this.width, this.height)
        this.hudView = StretchViewport(480f, 360f, hudCam)
        this.hudStg = Stage(hudView , hudSb)

        this.ic = InputCtl(this)
        this.gpc = GamepadCtl(this)
        this.osc = OnScreenGamepad(this)

        this.hudStg += this.osc

        Gdx.input.inputProcessor = this.hudStg

        Images.values().forEach { it.load() }
        Sounds.values().forEach { it.load() }
        Tunes.values().forEach { it.load() }

        while (!this.tunesManager.update() || !this.soundsManager.update() || !this.textureManager.update()) {
            println("still loading")
            this.textureManager.update()
            this.soundsManager.update()
            this.tunesManager.update()
        }

        addScreen(Title(this))
        addScreen(Game(this))
        addScreen(End(this))

        setScreen<Title>()
    }

    override fun dispose() {
        this.textureManager.dispose()
        this.soundsManager.dispose()
        this.tunesManager.dispose()

        this.sb.dispose()
        this.stg.dispose()
        this.hudSb.dispose()
        this.hudStg.dispose()
        super.dispose()
    }
}
