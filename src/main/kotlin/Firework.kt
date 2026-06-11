import processing.core.PApplet

abstract class AbstractFirework(
    var x: Float,
    var y: Float
){
    var isDead: Boolean = false
    abstract fun update()
    abstract fun draw(p: PApplet)
}

open class Rocket(
    x: Float,
    y: Float,
    val initialVelocity: Float = -5f
): AbstractFirework(x, y) {
    var vy: Float = initialVelocity
    var gravity: Float = config.gravity ?: 0.2f
    var exploded: Boolean = false
    val particles = mutableListOf<Particle>()

    override fun update() {
        if (!exploded) {
            vy += gravity
            y += vy
            x += (Math.random().toFloat() - 0.5f) * 2f // leichte horizontale Bewegung TODO(anpassen)
            if (vy >= 0) {
                explode()
            }
        } else {
            particles.forEach { it.update() }
            particles.removeAll { it.isDead() }
            if (particles.isEmpty()) {
                isDead = true
            }
        }
    }

    override fun draw(p: PApplet) {
        if (!exploded) {
            p.fill(200) // fixed color for the rocket asscent
            p.ellipse(x, y, 10f, 20f)
        }else{
            vy = 0f
            particles.forEach { it.draw(p) }
        }
    }

    open fun explode(){
        exploded = true
    }
}