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
        // ground: lines use stroke (not fill). set stroke color and weight, and remove trailing comma
        stroke(255f, 255f, 255f)
        strokeWeight(2f)
        line(80f, height.toFloat() -10f, width.toFloat() - 80f, height.toFloat() - 10f)
        noStroke()
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
        if(isMouseLeftPressed()){
            fireworks.add(
                when (random(3f).toInt()) {
                    0 -> rocket1(mouseX.toFloat(), mouseY.toFloat(), random(-16f, -12f))
                    1 -> rocket2(mouseX.toFloat(), mouseY.toFloat(), random(-16f, -12f))
                    else -> rocket3(mouseX.toFloat(), mouseY.toFloat(), random(-16f, -12f))
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

    fun isMouseLeftPressed(): Boolean {
        return mousePressed && mouseButton == LEFT
    }
}