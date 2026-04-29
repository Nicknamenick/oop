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
        background(255)
    }

    override fun draw() {
        background(0f, 0f, 0f, 25f)
        if (random(0.75f) < 0.02f && fireworks.size <= 3) {
            fireworks.add(
                RocketSpawner(
                    x = random(width.toFloat()),
                    y = height.toFloat(),
                    initialVelocity = random(-15f, -10f),
                    lifespan = random(200f, 300f),
                    rocketColor = 0xFFFFFF,
                    payloads = listOf(
                        ExplosionPayload(
                            partNum = 300,
                            explosionShape = StarExplosionShape(),
                            particleFactories = listOf(::StandardParticle),
                            particleShape = Square(),
                            baseColor = 0xFF0000,
                            cRange = 0x00FF00,
                            power = 0.7f
                        ),
                        ExplosionPayload(
                            partNum = 300,
                            explosionShape = CircleExplosionShape(),
                            particleFactories = listOf(::StandardParticle),
                            particleShape = Circle(),
                            baseColor = 0x0000FF,
                            cRange = 0xFF00FF,
                            power = 0.2f
                        ),
                        ExplosionPayload(
                            partNum = 200,
                            size = 2.5f,
                            explosionShape = SquareExplosionShape(),
                            particleFactories = listOf(crackleFactory(
                                intensity = 0.6f,
                                interval = 5,
                                baseFactory = ::SpreadParticle,
                            )),
                            particleShape = Square(),
                            baseColor = 0xFFFF00,
                            cRange = 0x0000FF,
                            power = 0.2f
                        )
                    )
                )
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