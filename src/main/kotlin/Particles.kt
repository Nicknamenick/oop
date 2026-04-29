import processing.core.PApplet

class Particles(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var shape: Shape,
    var color: Int,
    var lifespan: Float
) {
    fun update() {
        x += vx
        y += vy
        vy += 0.1f
        lifespan -= 2f
    }

    fun draw(p: PApplet) {
        p.fill(color, lifespan)
        shape.draw(p, x, y, 5f)
    }

    fun isDead(): Boolean {
        return lifespan <= 0f
    }
}