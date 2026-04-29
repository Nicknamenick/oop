data class ExplosionPayload(
    val partNum: Int,
    val explosionShape: ExplosionShape,
    val particleFactories: List<ParticleFactory>,
    val particleShape: Shape,
    val baseColor: Int,
    val cRange: Int,
    val size: Float = 5f,
    val power: Float = 1f
)

class RocketSpawner(
    x: Float,
    y: Float,
    initialVelocity: Float,
    val lifespan: Float,
    val payloads: List<ExplosionPayload>,
    val rocketColor: Int = 0xFFFFFF,
    ) : Rocket(x, y, initialVelocity) {
    override fun explode() {
        super.explode()

        for (payload in payloads) {
            val factories = if (payload.particleFactories.isEmpty()) listOf(::StandardParticle) else payload.particleFactories

            for (i in 0 until payload.partNum) {
                val speed = Math.random().toFloat() * 4 + 1
                val (dx, dy) = payload.explosionShape.direction(i, payload.partNum, power=payload.power)
                val vx = dx * speed
                val vy = dy * speed

                val randomOffset = (Math.random() * payload.cRange).toInt()
                val r = ((payload.baseColor shr 16) and 0xFF) + ((randomOffset shr 16) and 0xFF)
                val g = ((payload.baseColor shr 8) and 0xFF) + ((randomOffset shr 8) and 0xFF)
                val b = (payload.baseColor and 0xFF) + (randomOffset and 0xFF)

                // Komponenten auf 0-255 begrenzen
                val color = (0xFF shl 24) or
                            ((r.coerceIn(0, 255) and 0xFF) shl 16) or
                            ((g.coerceIn(0, 255) and 0xFF) shl 8) or
                            (b.coerceIn(0, 255) and 0xFF)

                val factory = factories.random()
                particles.add(factory(x, y, vx, vy, payload.particleShape, color, lifespan, payload.size))
            }
        }
    }
}