import processing.core.PApplet

object Global {
    lateinit var papp: PApplet
}

class ProcessingMain : PApplet() {

    private val fireworks = mutableListOf<AbstractFirework>()

    override fun settings() {
        size(1200, 800)
    }

    override fun setup() {
        background(255)
    }

    override fun draw() {
        background(0f, 0f, 0f, 25f) // leicht transparentes Schwarz für Nachzieheffekt
        if (random(0.75f) < 0.02f) { // zufällige Raketenstartchance
            fireworks.add(
                CircleExplosionRocket(random(width.toFloat()),
                    height.toFloat(),
                    initialVelocity = -15f,
                    lifespan = 255f,
                    partNum = 500,
                    baseColor = 0x000000, // rot in hex
                    cRange = 0xFFFFFF    // Farbvariationsbereich in hex
                )
            )
        }
        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(this) }
        fireworks.removeAll { it.isDead }

    }
}