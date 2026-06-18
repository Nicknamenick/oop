import processing.core.PGraphics
import kotlin.math.sin
import kotlin.math.PI

typealias ParticleFactory = (Float, Float, Float, Float, Shape, Int, Float, Float) -> Particle

/**
 * Particle represents a single particle in the explosion. It has properties like position, velocity, shape, color and lifespan.
 */
abstract class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var shape: Shape,
    var color: Int,
    var lifespan: Float,
    val size: Float = 5f
){
    open fun update(){
        val resistance = 0.98f
        vx *= resistance
        vy *= resistance

        x += vx
        y += vy
        vy += config.gravityParticle
        lifespan -= 2f
    }
    abstract fun draw(p: PGraphics)
    fun isDead(): Boolean{
        return lifespan <= 0f
    }
}

/**
 * StandardParticle is a basic implementation of a particle that simply moves according to its velocity and fades out over time.
 */
class StandardParticle(
    x: Float,
    y: Float,
    vx: Float,
    vy: Float,
    shape: Shape,
    color: Int,
    lifespan: Float,
    size: Float = 5f
) : Particle(x, y, vx, vy, shape, color, lifespan, size) {
    override fun draw(p: PGraphics) {
        p.fill(color, lifespan)
        shape.draw(p, x, y, size)
    }
}

/**
 * SpreadParticle is a particle that changes its velocity randomly every few frames
 * by adding a small random value to its velocity.
 */
class SpreadParticle(
    x: Float,
    y: Float,
    vx: Float,
    vy: Float,
    shape: Shape,
    color: Int,
    lifespan: Float,
    size: Float = 5f
) : Particle(x, y, vx, vy, shape, color, lifespan, size) {
    private var crackleTimer = 0

    override fun update() {
        super.update()
        crackleTimer++
        // every 3 frames
        if (crackleTimer % 3 == 0) {
            vx += (Math.random().toFloat() - 0.5f) * 2f
            vy += (Math.random().toFloat() - 0.5f) * 2f
        }
    }

    override fun draw(p: PGraphics) {
        p.fill(color, lifespan)
        shape.draw(p, x, y, size)
    }
}

/**
 * CrackleParticle is a particle that creates a crackling effect by modulating its alpha value with a sine wave.
 * its created by wrapping another particle and modulating its alpha value based on a sine wave to create a crackling effect.
 */
class CrackleParticle(
    private val inner: Particle,
    private val intensity: Float = 1f,
    private val interval: Int = 5
) : Particle(inner.x, inner.y, inner.vx, inner.vy, inner.shape, inner.color, inner.lifespan, inner.size) {
    private var counter = 0

    override fun update() {
        inner.update()

        x = inner.x
        y = inner.y
        vx = inner.vx
        vy = inner.vy
        lifespan = inner.lifespan

        counter++
    }

    override fun draw(p: PGraphics) {
        // Sanfter Übergang mit Sin-Welle (statt abruptem Wechsel)
        val phase = (counter % (interval * 2)).toFloat() / (interval * 2)
        val smoothWave = sin(phase * PI).toFloat()

        val modulatedAlpha = (lifespan * (1f - (1f - smoothWave) * intensity)).coerceIn(0f, lifespan)

        p.fill(color, modulatedAlpha)
        shape.draw(p, x, y, size)
    }
}

/**
 * crackleFactory is a higher-order function that creates a ParticleFactory which produces CrackleParticles.
 * It takes an optional intensity and interval parameter to control the crackling effect, and a particle to create the inner particle.
 */
fun crackleFactory(
    intensity: Float = 1f,
    interval: Int = 5,
    baseFactory: ParticleFactory = { x, y, vx, vy, shape, color, lifespan, size -> StandardParticle(x, y, vx, vy, shape, color, lifespan, size) }
): ParticleFactory {
    return { x, y, vx, vy, shape, color, lifespan, size ->
        val base = baseFactory(x, y, vx, vy, shape, color, lifespan, size)
        CrackleParticle(base, intensity, interval)
    }
}

