import processing.core.PApplet

object Global {
    lateinit var papp: PApplet
}

class ProcessingMain : PApplet() {

    private val fireworks = mutableListOf<AbstractFirework>()
    private var paused = false

    override fun settings() {
        size(1200, 800)
    }

    override fun setup() {
        Global.papp = this
        background(255)
    }

    override fun draw() {
        background(0f, 0f, 0f, 25f)
        if (random(0.75f) < 0.02f && fireworks.size <= 3) {
            val initialVelocity = random(-16f, -12f)
            val x = random(width.toFloat())
            val y = height.toFloat()
            fireworks.add(
                when (random(3f).toInt()) {
                    0 -> rocket1(x, y, initialVelocity)
                    1 -> rocket2(x, y, initialVelocity)
                    else -> rocket3(x, y, initialVelocity)
                }
            )
        }
        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(this) }
        fireworks.removeAll { it.isDead }

    }

    override fun keyPressed() {
        if (key == ' ') {
            paused = !paused
            if (paused) {
                noLoop()
            } else {
                loop()
            }
        }
    }
}