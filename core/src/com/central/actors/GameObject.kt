package com.central.actors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.central.assets.Images.*


open class GameObject : Actor() {

    enum class State {
        Standing, Walking, Jumping, None
    }

    val velocity = Vector2()

    var maxVelocity = 0f
    var jumpVelocity = 0f
    var damping = 0f

    // this is supposed to be a default texture that gets overridden
    var tex = default()

    var state = State.None

    var facesRight = true
    var grounded = false
}