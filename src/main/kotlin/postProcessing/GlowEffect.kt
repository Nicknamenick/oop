package postProcessing

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics

/**
 * Bloom-style glow post processing effect.
 *
 * Extracts bright areas from the input image and
 * adds them back using additive blending to create a glow-like effect.
 */
class GlowEffect(app: PApplet) : PostProcessEffect(app) {

    private val scale = 1
    private val smallW = app.width / scale
    private val smallH = app.height / scale
    private val glowBuffer = app.createGraphics(smallW, smallH)


    /** Brightness threshold for glow extraction */
    private var threshold: Int = 150
        set(value) {
            field = value.coerceIn(0, 255)
        }

    override fun apply(input: PGraphics, output: PGraphics) {
        if (!isEnabled) {
            output.beginDraw()
            output.image(input, 0f, 0f)
            output.endDraw()
            return
        }

        // Extract bright areas into a separate buffer
        glowBuffer.beginDraw()
        glowBuffer.background(0f, 0f, 0f, 0f)

        glowBuffer.image(input, 0f, 0f, smallW.toFloat(), smallH.toFloat())

        glowBuffer.loadPixels()
        for (i in glowBuffer.pixels.indices) {
            val c = glowBuffer.pixels[i]

            val r = (c shr 16) and 0xFF
            val g = (c shr 8) and 0xFF
            val b = c and 0xFF

            // Luminance approximation (simple average)
            val brightness = (r + g + b) / 3f

            if (brightness < threshold) {
                // Fully discard dark pixels to avoid color bleeding
                glowBuffer.pixels[i] = 0x00000000
            } else {
                // Preserve original color, modulate only alpha by intensity
                val alpha = ((brightness - threshold) / (255f - threshold)).coerceIn(1f, 1f)
                val a = (alpha * 255).toInt()

                glowBuffer.pixels[i] =
                    (a shl 24) or (r shl 16) or (g shl 8) or b
            }
        }
        glowBuffer.updatePixels()

        glowBuffer.endDraw()

        // Composite result
        output.beginDraw()
        output.background(0)

        output.image(input, 0f, 0f)

        // Additive blending ensures light accumulation without darkening
        output.blendMode(PConstants.ADD)
        output.image(glowBuffer, 0f, 0f, output.width.toFloat(), output.height.toFloat())

        output.blendMode(PConstants.BLEND)
        output.endDraw()
    }
}