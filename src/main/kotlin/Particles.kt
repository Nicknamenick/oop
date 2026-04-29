import processing.core.PApplet
import kotlin.math.sin
import kotlin.math.PI

typealias ParticleFactory = (Float, Float, Float, Float, Shape, Int, Float, Float) -> Particle

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
        x += vx
        y += vy
        vy += 0.1f
        lifespan -= 2f
    }
    abstract fun draw(p: PApplet)
    fun isDead(): Boolean{
        return lifespan <= 0f
    }
}

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
    override fun draw(p: PApplet) {
        p.fill(color, lifespan)
        shape.draw(p, x, y, size)
    }
}
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
        if (crackleTimer % 3 == 0) {
            vx += (Math.random().toFloat() - 0.5f) * 2f
            vy += (Math.random().toFloat() - 0.5f) * 2f
        }
    }

    override fun draw(p: PApplet) {
        p.fill(color, lifespan)
        shape.draw(p, x, y, size)
    }
}

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

    override fun draw(p: PApplet) {
        // Sanfter Übergang mit Sin-Welle (statt abruptem Wechsel)
        val phase = (counter % (interval * 2)).toFloat() / (interval * 2)
        val smoothWave = sin(phase * PI).toFloat()

        val modulatedAlpha = (lifespan * (1f - (1f - smoothWave) * intensity)).coerceIn(0f, lifespan)

        p.fill(color, modulatedAlpha)
        shape.draw(p, x, y, size)
    }
}

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

