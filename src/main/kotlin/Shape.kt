import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

interface Shape {
    fun draw(p: PApplet, x: Float, y: Float, size: Float)
}

abstract class ExplosionShape {
    abstract fun direction(index: Int, total: Int): Pair<Float, Float>
}

class CircleExplosionShape : ExplosionShape() {
    override fun direction(index: Int, total: Int): Pair<Float, Float> {
        val safeTotal = total.coerceAtLeast(1)
        val angle = (index.toDouble() / safeTotal.toDouble()) * 2.0 * PI
        return Pair(cos(angle).toFloat(), sin(angle).toFloat())
    }
}

class SquareExplosionShape : ExplosionShape() {
    override fun direction(index: Int, total: Int): Pair<Float, Float> {
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
        return Pair(rawX, rawY)
    }
}

class Circle : Shape {
    override fun draw(p: PApplet, x: Float, y: Float, size: Float) {
        p.noStroke()
        p.ellipse(x, y, size, size)
    }
}
class Rectangle : Shape {
    override fun draw(p: PApplet, x: Float, y: Float, size: Float) {
        p.noStroke()
        p.rect(x - size / 2, y - size / 2, size, size)
    }
}