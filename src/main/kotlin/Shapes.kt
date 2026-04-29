import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

interface Shape {
    fun draw(p: PApplet, x: Float, y: Float, size: Float)
}

abstract class ExplosionShape {
    abstract fun direction(index: Int, total: Int, power: Float=1f): Pair<Float, Float>
}

class CircleExplosionShape : ExplosionShape() {
    override fun direction(index: Int, total: Int, power: Float): Pair<Float, Float> {
        val safeTotal = total.coerceAtLeast(1)
        val angle = (index.toDouble() / safeTotal.toDouble()) * 2.0 * PI
        return Pair(cos(angle).toFloat()*power, sin(angle).toFloat()*power)
    }
}

class SquareExplosionShape : ExplosionShape() {
    override fun direction(index: Int, total: Int, power: Float): Pair<Float, Float> {
        val safeTotal = total.coerceAtLeast(1)
        val t = (index.toDouble() / safeTotal.toDouble()) * 4.0
        val side = t.toInt().coerceIn(0, 3)
        val u = (t - side).toFloat() * 2f - 1f

        val rawX: Float
        val rawY: Float
        when (side) {
            0 -> {
                rawX = u
                rawY = -1f
            }
            1 -> {
                rawX = 1f
                rawY = u
            }
            2 -> {
                rawX = -u
                rawY = 1f
            }
            else -> {
                rawX = -1f
                rawY = -u
            }
        }

        // Nicht normieren: Normierung macht aus der Quadratkontur wieder einen Kreis.
        return Pair(rawX*power, rawY*power)
    }
}

class StarExplosionShape : ExplosionShape() {
    private val spikes = 5
    private val outerRadius = 1f
    private val innerRadius = 0.45f
    private val jitterAmount = 0.25f

    override fun direction(index: Int, total: Int, power: Float): Pair<Float, Float> {
        val safeTotal = total.coerceAtLeast(1)
        val starPoints = spikes * 2
        val pointIndex = index % starPoints
        val spikeIndex = pointIndex / 2
        val isOuterPoint = pointIndex % 2 == 0

        val angle = (spikeIndex.toDouble() / spikes.toDouble()) * 2.0 * PI - PI / 2.0
        val radius = if (isOuterPoint) outerRadius else innerRadius

        val jitter = ((index % safeTotal).toFloat() / safeTotal.toFloat() - 0.5f) * jitterAmount
        val dx = (cos(angle).toFloat() * radius) + jitter
        val dy = (sin(angle).toFloat() * radius) + jitter

        return Pair(dx*power, dy*power)
    }
}

class Circle : Shape {
    override fun draw(p: PApplet, x: Float, y: Float, size: Float) {
        p.noStroke()
        p.ellipse(x, y, size, size)
    }
}
class Square : Shape {
    override fun draw(p: PApplet, x: Float, y: Float, size: Float) {
        p.noStroke()
        p.rect(x - size / 2, y - size / 2, size, size)
    }
}