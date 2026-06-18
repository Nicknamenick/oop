class Config {

    data class RocketConfigData(
        val x: Float,
        val y: Float,
        val initialVelocity: Float,
        val rocketColor: String,
        val payloads: List<ExplosionPayloadData>
    )

    data class ExplosionPayloadData(
        val partNum: Int,
        val explosionShape: String,
        val particleFactories: List<String>,
        val particleShape: String,
        val baseColor: String,
        val cRange: String,
        val size: Float = 5f,
        val power: Float = 1f
    )

    data class GlobalConfig(
        val windowSizeX: Int,
        val windowSizeY: Int,
        val gravity: Float,
        val gravityParticle: Float,
        val mode: String,
        val backgroundColor: FloatArray = floatArrayOf(0f, 0f, 0f, 25f ),
        val minRockets: Int,
        val maxRockets: Int,
        val rocketSpawnChance: Float,
        val minInitialVelocity: Float,
        val maxInitialVelocity: Float,
        val isChainEnabled: Boolean,
        val collisionThreshold: Float,
    )

    data class QueueEntry(
        val time: Float,
        val rocketKey: String,
        val x: Float,
        val initialVelocity: Float
    )
    data class ShowConfig(
        val queue: List<QueueEntry>
    )

    companion object {
        val parseHexColor = { colorString: String ->
            colorString
                .replace("0x", "")
                .trim()
                .toLong(16) // überlauf fehler möglich
                .toInt()
        }

        /**
         * Creates a RocketSpawner instance from the given configuration data
         */
        fun createRocketFromConfig(x: Float, y: Float, initialVelocity: Float, data: RocketConfigData): RocketSpawner {
            val realPayloads = data.payloads.map { payloadData ->

                val shape = when (payloadData.explosionShape) {
                    "STAR" -> StarExplosionShape()
                    "CIRCLE" -> CircleExplosionShape()
                    "SQUARE" -> SquareExplosionShape()
                    "TRIANGLE" -> TriangleExplosionShape()
                    else -> CircleExplosionShape() // Fallback
                }

                val pShape = when (payloadData.particleShape) {
                    "CIRCLE" -> Circle()
                    "SQUARE" -> Square()
                    else -> {
                        println("Unknown particle shape '${payloadData.particleShape}', defaulting to Circle")
                        Circle()
                    }
                }

                val factories = payloadData.particleFactories.map { factoryType ->
                    when (factoryType) {
                        "SPREAD" -> ::SpreadParticle
                        "STANDARD" -> ::StandardParticle
                        "CRACKLE" -> crackleFactory(intensity = 0.6f, interval = 5, baseFactory = ::SpreadParticle)
                        else -> {
                            println("Unknown particle factory '$factoryType', defaulting to StandardParticle")
                            ::StandardParticle
                        }
                    }
                }

                ExplosionPayload(
                    partNum = payloadData.partNum,
                    explosionShape = shape,
                    particleFactories = factories,
                    particleShape = pShape,

                    baseColor = parseHexColor(payloadData.baseColor),
                    cRange = parseHexColor(payloadData.cRange),

                    size = payloadData.size,
                    power = payloadData.power
                )
            }

            return RocketSpawner(
                x = x,
                y = y,
                initialVelocity = initialVelocity,
                lifespan = Global.papp.random(200f, 300f),
                rocketColor = parseHexColor(data.rocketColor),
                payloads = realPayloads
            )
        }
    }
}