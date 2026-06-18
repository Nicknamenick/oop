package postProcessing

import processing.core.PApplet
import processing.core.PGraphics

/**
 * PostProcessingPipeline manages a sequence of post-processing effects. It takes the raw rendered canvas as input,
 * applies each enabled effect in order, and outputs the final processed image to the screen.
 * The pipeline uses two off-screen buffers to alternate between effects without needing to create a new buffer for each effect.
 */
class PostProcessingPipeline(private val app: PApplet) {

    private val effects = mutableListOf<PostProcessEffect>()

    private var buffer1: PGraphics = app.createGraphics(app.width, app.height)
    private var buffer2: PGraphics = app.createGraphics(app.width, app.height)


    fun addEffect(effect: PostProcessEffect) {
        effects.add(effect)
    }

    fun processAndDraw(rawCanvas: PGraphics) {
        val activeEffects = effects.filter { it.isEnabled }

        if (activeEffects.isEmpty()) {
            app.image(rawCanvas, 0f, 0f)
            return
        }

        buffer1.beginDraw()
        buffer1.image(rawCanvas, 0f, 0f)
        buffer1.endDraw()

        var currentSource = buffer1
        var currentDest = buffer2

        for (effect in effects) {
            if (effect.isEnabled) {
                effect.apply(currentSource, currentDest)

                val temp = currentSource
                currentSource = currentDest
                currentDest = temp
            }
        }

        app.image(currentSource, 0f, 0f)
    }
}