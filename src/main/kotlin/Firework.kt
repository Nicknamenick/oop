import processing.core.PGraphics

/**
 * AbstractFirework is the base class for all fireworks.
 * It defines the common properties and methods that all fireworks must implement.
 */
abstract class AbstractFirework(
    var x: Float,
    var y: Float
){
    var isDead: Boolean = false
    abstract fun update()
    abstract fun draw(p: PGraphics)
}

/**
 * Rocket represents the initial ascent of a firework. It moves upwards until it reaches its peak, at which point it explodes into particles.
 * The Rocket class can be extended to create different types of rockets with unique explosion patterns and particles
 */
open class Rocket(
    x: Float,
    y: Float,
    val initialVelocity: Float = -5f
): AbstractFirework(x, y) {
    var vy: Float = initialVelocity
    var gravity: Float = config.gravity
    var exploded: Boolean = false
    val particles = mutableListOf<Particle>()

    override fun update() {
        if (!exploded) {
            vy += gravity
            y += vy
            x += (Math.random().toFloat() - 0.5f) * 2f
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

    override fun draw(p: PGraphics) {
        if (!exploded) {

            p.fill(255) // fixed color for the rocket asscent
            p.ellipse(x, y, 3f, 10f)
        }else{
            vy = 0f
            particles.forEach { it.draw(p) }
        }
    }

    open fun explode(){
        exploded = true
    }
}