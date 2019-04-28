package com.central.managers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.central.actors.GameObject


class MapManager {

    private val rectPool = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }

    }
    private val tiles = Array<Rectangle>()

    var map = TmxMapLoader().load("maps/level1.tmx")
    private val walls = map.layers.get("solid") as TiledMapTileLayer

    fun collideWithKoala(gameObject: GameObject) {
        // perform collision detection & response, on each axis, separately
        // if the koala is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        // perform collision detection & response, on each axis, separately
        // if the koala is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left

        val testRect = Rectangle()

        testRect.set(gameObject.x, gameObject.y, gameObject.width, gameObject.height)

        var myTiles = getVertNeighbourTiles(gameObject.velocity, testRect, walls)

        myTiles.forEach {
            if (testRect.overlaps(it)) {
                with(gameObject) {
                    if (velocity.y > 0) {
                        y = it.y - height
                    } else if (velocity.y < 0) {
                        y = it.y + it.height
                        grounded = true
                    }
                    velocity.y = 0f
                }
            }
        }

        testRect.set(gameObject.x, gameObject.y, gameObject.width, gameObject.height)

        myTiles = getHorizNeighbourTiles(gameObject.velocity, testRect, walls)

        myTiles.forEach {
            if (testRect.overlaps(it)) {
                with(gameObject) {
                    if (velocity.x > 0) {
                        x = it.x - width
                    } else if (velocity.x < 0) {
                        x = it.x + it.width
                    }
                    velocity.x = 0f
                }
            }
        }
    }

    fun getTiles(startX: Int, startY: Int, endX: Int, endY: Int, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        rectPool.freeAll(tiles)

        tiles.clear()

        for (y in startY..endY) {
            for (x in startX..endX) {
                val cell = tileLayer.getCell(x, y)
                if (cell != null) {
                    val rect = rectPool.obtain()
                    rect.set(x.toFloat(), y.toFloat(), 1f, 1f)
                    tiles.add(rect)
                }
            }
        }
        return tiles
    }

    fun getHorizNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startY = rect.y.toInt()
        val endY = (rect.y + rect.height).toInt()
        // if the sprite is moving right, get the tiles to its right side
        // if the sprite is moving left, get the tiles to its left side
        val startX = if (velocity.x > 0) (rect.x + rect.width).toInt() else rect.x.toInt()
        val endX = startX

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    fun getVertNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startX = rect.x.toInt()
        val endX = (rect.x + rect.width).toInt()
        // if sprite is moving up, get the tiles above it
        // if sprite is moving down, get the tiles below it
        val startY = if (velocity.y > 0) (rect.y + rect.height).toInt() else rect.y.toInt()
        val endY = startY

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    fun renderDebug(debugRenderer: ShapeRenderer, cam: OrthographicCamera) {
        debugRenderer.color = Color.YELLOW
        val layer = map.layers.get("solid") as TiledMapTileLayer
        for (y in 0..layer.height) {
            for (x in 0..layer.width) {
                val cell = layer.getCell(x, y)
                if (cell != null) {
                    if (cam.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0f, 1f, 1f, 0f))
                        debugRenderer.rect(x.toFloat(), y.toFloat(), 1f, 1f)
                }
            }
        }
        debugRenderer.end()
    }
}