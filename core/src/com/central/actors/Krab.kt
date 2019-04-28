package com.central.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.*
import com.central.assets.Images.*
import com.central.assets.Sounds.*
import com.central.App


/** The player character, has state and state time,  */
class Krab : GameObject() {

    // load the koala frames, split them, and assign them to Animations
    private var regions: Array<TextureRegion>
    private var stand: Animation<TextureRegion>
    private var walk: Animation<TextureRegion>
    private var jump: Animation<TextureRegion>

    // figure out the width and height of the koala for collision
    // detection and rendering by converting a koala frames pixel
    // size into world units (1 unit == 16 pixels)

    var stateTime = 0f

    init {
        this.tex = crab_sheet()

        val regionWidth = 200
        val regionHeight = 200
        regions = TextureRegion.split(this.tex, regionWidth, regionHeight)[0]
        stand = Animation(0f, regions[0])
        walk = Animation(0.15f, regions[1], regions[2], regions[3])
        jump = Animation(0f, regions[4])

        this.width = 1 / 32f * 60f
        this.height = 1 / 32f * 60f

        walk.playMode = Animation.PlayMode.LOOP_PINGPONG

        this.maxVelocity = 10f
        this.jumpVelocity = 40f
        this.damping = 0.87f

        this.state = State.Walking

        this.facesRight = true
        this.grounded = false
    }

    fun handleInput(app: App) {
        // check input and apply to velocity & state
        if (app.ic.aPressed && this.grounded) {
            this.velocity.y += this.jumpVelocity
            this.state = State.Jumping
            jump().play()
            this.grounded = false
        }

        if (app.ic.lPressed) {
            this.velocity.x = -this.maxVelocity
            if (this.grounded) this.state = State.Walking
            this.facesRight = false
        }

        if (app.ic.rPressed) {
            this.velocity.x = this.maxVelocity
            if (this.grounded) this.state = State.Walking
            this.facesRight = true
        }

        if (app.ic.bPressed) {
            app.debug = !app.debug
            app.ic.bPressed = false
        }
    }

    override fun act(delta: Float) {
        var deltaTime = delta
        if (deltaTime == 0f) return

        if (deltaTime > 0.1f)
            deltaTime = 0.1f

        this.stateTime += deltaTime

        // clamp the velocity to the maximum, x-axis only
        //this.velocity.x = MathUtils.clamp(this.velocity.x, -this.maxVelocity, this.maxVelocity)

        // If the velocity is < 1, set it to 0 and set state to Standing
        if (Math.abs(this.velocity.x) < 1) {
            this.velocity.x = 0f
            if (this.grounded) this.state = State.Standing
        }

        // multiply by delta time so we know how far we go
        // in this frame
        this.velocity.scl(deltaTime)

        // unscale the velocity by the inverse delta time and set
        // the latest position
        this.x += this.velocity.x
        this.y += this.velocity.y

        this.velocity.scl(1 / deltaTime)

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        this.velocity.x *= this.damping
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        // based on the koala state, get the animation frame
        val frame = when(this.state) {
            State.Standing -> stand.getKeyFrame(this.stateTime)
            State.Walking -> walk.getKeyFrame(this.stateTime)
            State.Jumping -> jump.getKeyFrame(this.stateTime)
            else -> stand.getKeyFrame(this.stateTime)
        }

        // draw the koala, depending on the current velocity
        // on the x-axis, draw the koala facing either right
        // or left

        if (this.facesRight) {
            batch.draw(frame, this.x, this.y, this.width, this.height)
        } else {
            batch.draw(frame, this.x + this.width, this.y, -this.width, this.height)
        }
    }

    private fun isTouched(startX: Float, endX: Float): Boolean {
        // Check for touch inputs between startX and endX
        // startX/endX are given between 0 (left edge of the screen) and 1 (right edge of the screen)
        for (i in 0..1) {
            val x = Gdx.input.getX(i) / Gdx.graphics.width.toFloat()
            if (Gdx.input.isTouched(i) && x >= startX && x <= endX) {
                return true
            }
        }
        return false
    }
}
