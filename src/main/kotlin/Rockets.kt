/**
 * Example rockets with different explosion patterns and particle effects.
 */

fun rocket1(x: Float, y: Float, initialVelocity: Float): AbstractFirework {
    return RocketSpawner(
        x,
        y,
        initialVelocity,
        lifespan = Global.papp.random(200f, 300f),
        rocketColor = 0xFFFFFF,
        payloads = listOf(
            ExplosionPayload(
                partNum = 300,
                explosionShape = StarExplosionShape(),
                particleFactories = listOf(::StandardParticle),
                particleShape = Circle(),
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
                power = 0.6f
            ),
            ExplosionPayload(
                partNum = 200,
                size = 2.5f,
                explosionShape = SquareExplosionShape(),
                particleFactories = listOf(
                    crackleFactory(
                        intensity = 0.6f,
                        interval = 5,
                        baseFactory = ::SpreadParticle
                    )
                ),
                particleShape = Square(),
                baseColor = 0xFFFF00,
                cRange = 0x0000FF,
                power = 0.5f
            )
        )
    )
}

fun rocket2(x: Float, y: Float, initialVelocity: Float): AbstractFirework {
    return RocketSpawner(
        x,
        y,
        initialVelocity,
        lifespan = Global.papp.random(180f, 260f),
        rocketColor = 0x00FFFF,
        payloads = listOf(
            ExplosionPayload(
                partNum = 260,
                explosionShape = CircleExplosionShape(),
                particleFactories = listOf(
                    ::SpreadParticle,
                    crackleFactory(
                        intensity = 0.6f,
                        interval = 4,
                        baseFactory = ::StandardParticle
                    )
                ),
                particleShape = Circle(),
                baseColor = 0x00FFFF,
                cRange = 0xFF00FF,
                power = 0.85f
            ),
            ExplosionPayload(
                partNum = 100,
                explosionShape = SquareExplosionShape(),
                particleFactories = listOf(::StandardParticle),
                particleShape = Square(),
                baseColor = 0xFF00FF,
                cRange = 0x00FFFF,
                power = 0.4f
            )
        )
    )
}

fun rocket3(x: Float, y: Float, initialVelocity: Float): AbstractFirework {
    return RocketSpawner(
        x,
        y,
        initialVelocity,
        lifespan = Global.papp.random(220f, 320f),
        rocketColor = 0xFF8800,
        payloads = listOf(
            ExplosionPayload(
                partNum = 350,
                explosionShape = TriangleExplosionShape(),
                particleFactories = listOf(::StandardParticle),
                particleShape = Circle(),
                baseColor = 0xAA00FF,
                cRange = 0x00FF88,
                power = 0.75f
            ),
            ExplosionPayload(
                partNum = 50,
                explosionShape = CircleExplosionShape(),
                particleFactories = listOf(
                    crackleFactory(
                        intensity = 0.8f,
                        interval = 3,
                        baseFactory = ::SpreadParticle
                    )
                ),
                particleShape = Circle(),
                baseColor = 0xFF0088,
                cRange = 0x88FF00,
                power = 2f
            )
        )
    )
}
