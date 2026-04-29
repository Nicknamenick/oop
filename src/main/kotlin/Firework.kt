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
    var vy: Float = initialVelocity // Aufwärtsbewegung
    var gravity: Float = 0.2f // Beschleunigung nach unten
    var exploded: Boolean = false
    val particles = mutableListOf<Particles>()

    override fun update() {
        if (!exploded) {
            vy += gravity //TODO anpassen
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

    // TODO: aufstieg anpassen
    override fun draw(p: PApplet) {
        if (!exploded) {
            p.fill(200) // fixed color for the rocket asscent
            p.ellipse(x, y, 10f, 20f)
        }else{
            particles.forEach { it.draw(p) }
        }
    }

    open fun explode(){
        exploded = true
    }
}

open class ShapeRocket(
    x: Float,
    y: Float,
    initialVelocity: Float = -5f,
    val lifespan: Float,
    val shape: Shape,
    val partNum: Int = 50
    ): Rocket(x, y, initialVelocity) {
    override fun explode() {
        super.explode()
        for (i in 0 until partNum) {
            val angle = Math.random() * 2 * Math.PI
            val speed = Math.random() * 3 + 2
            val vx = (Math.cos(angle) * speed).toFloat()
            val vy = (Math.sin(angle) * speed).toFloat()
            particles.add(Particles(x, y, vx, vy, shape, 255, lifespan))
        }
    }
}