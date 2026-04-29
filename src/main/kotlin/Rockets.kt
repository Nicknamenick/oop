class CircleExplosionRocket(
    x: Float,
    y: Float,
    initialVelocity: Float = -5f,
    lifespan: Float = 255f,
    partNum: Int = 50,
    val baseColor: Int,
    val cRange: Int,
    particleShape: Shape = Circle(),
    val explosionShape: ExplosionShape = CircleExplosionShape(),
) : ShapeRocket(x, y, initialVelocity, lifespan, particleShape, partNum) {
    override fun explode() {
        exploded = true
        for (i in 0 until partNum) {
            val speed = Math.random().toFloat() * 4 + 1 // zufällige Geschwindigkeit
            val (dx, dy) = explosionShape.direction(i, partNum)
            val vx = dx * speed
            val vy = dy * speed

            val randomOffset = (Math.random() * cRange).toInt()
            val r = ((baseColor shr 16) and 0xFF) + ((randomOffset shr 16) and 0xFF)
            val g = ((baseColor shr 8) and 0xFF) + ((randomOffset shr 8) and 0xFF)
            val b = (baseColor and 0xFF) + (randomOffset and 0xFF)

            // Komponenten auf 0-255 begrenzen
            val color = (0xFF shl 24) or
                        ((r.coerceIn(0, 255) and 0xFF) shl 16) or
                        ((g.coerceIn(0, 255) and 0xFF) shl 8) or
                        (b.coerceIn(0, 255) and 0xFF)

            particles.add(Particles(x, y, vx, vy, shape, color, lifespan))
        }
    }
}