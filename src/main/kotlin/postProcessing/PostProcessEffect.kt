package postProcessing

import processing.core.PApplet
import processing.core.PGraphics

/**
 * Base class for post-processing effects
 */
abstract class PostProcessEffect(protected val app: PApplet) {
    var isEnabled = true

    fun toggle() {
        isEnabled = !isEnabled
    }

    abstract fun apply(input: PGraphics, output: PGraphics)
}