import postProcessing.GlowEffect
import processing.core.PApplet
import postProcessing.PostProcessingPipeline
import processing.core.PGraphics

object Global {
    lateinit var papp: PApplet
}

class ProcessingMain : PApplet() {

    private val fireworks = mutableListOf<AbstractFirework>()
    private val eventListener = EventListener(fireworks)
    private var paused = false

    private lateinit var postProcessingPipeline: PostProcessingPipeline
    private lateinit var mainCanvas: PGraphics
    private lateinit var glowEffect: GlowEffect

    override fun settings() {
        size(config.windowSizeX, config.windowSizeY)
    }

    override fun setup() {
        Global.papp = this

        mainCanvas = createGraphics(width, height)
        glowEffect = GlowEffect(this)
        postProcessingPipeline = PostProcessingPipeline(this)
        postProcessingPipeline.addEffect(glowEffect)

        background(10)
        mainCanvas.beginDraw()
        mainCanvas.background(10)
        mainCanvas.endDraw()
    }

    override fun mousePressed() {
        super.mousePressed()
        if(mouseButton == LEFT){
            val initialVelocity = random(-config.maxInitialVelocity, -config.minInitialVelocity)
            val x = mouseX.toFloat()
            val y = mouseY.toFloat()

            val randomIndex = random(3f).toInt() + 1
            val rocketKey = String.format("firework_%02d", randomIndex)

            val rocketData = rocketRegistry[rocketKey]

            val newFirework: AbstractFirework? = rocketData?.let { data ->
                Config.createRocketFromConfig(x, y, initialVelocity, data)
            } ?: run {
                println("Warnung: $rocketKey wurde nicht in der Registry gefunden!")
                null
            }

            if (newFirework != null) {
                fireworks.add(newFirework)
            }
        }
        if(mouseButton == RIGHT){
            glowEffect.toggle()
        }
    }

    override fun draw() {
        if (paused) return

        mainCanvas.beginDraw()
        val bg = config.backgroundColor

        mainCanvas.noStroke()
        mainCanvas.fill(bg[0], bg[1], bg[2], bg[3])
        mainCanvas.rect(0f, 0f, width.toFloat(), height.toFloat())

        mainCanvas.fill(255)
        mainCanvas.text("FPS: ${frameRate.toInt()}", 10f, 20f)
        mainCanvas.text("press space to pause", 10f, 40f)
        mainCanvas.text("press right mouse button to toggle glow effect", 10f, 60f)

        eventListener.checkParticleCollision()

        if(config.mode == "normal"){
            modeNormal(mainCanvas)
        }else if(config.mode == "show") {
            modeShow(mainCanvas)
        }

        mainCanvas.endDraw()
        postProcessingPipeline.processAndDraw(mainCanvas)
    }


    /**
     * Rockets are spawned randomly based on the spawn chance and current number of fireworks, or if there are no fireworks at all.
     */
    fun modeNormal(canvas: PGraphics) {
        if ((random(0.75f) < 0.02f && fireworks.size <= 3) || fireworks.isEmpty()) {
            val initialVelocity = random(-config.maxInitialVelocity, -config.minInitialVelocity)
            val x = random(width.toFloat())
            val y = height.toFloat()

            val randomIndex = random(3f).toInt() + 1
            val rocketKey = String.format("firework_%02d", randomIndex)

            val rocketData = rocketRegistry[rocketKey]

            val newFirework: AbstractFirework? = rocketData?.let { data ->
                Config.createRocketFromConfig(x, y, initialVelocity, data)
            } ?: run {
                println("Warnung: $rocketKey wurde nicht in der Registry gefunden!")
                null
            }

            if (newFirework != null) {
                fireworks.add(newFirework)
            }
        }

        eventListener.checkParticleCollision()
        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(canvas) }
        fireworks.removeAll { it.isDead }
    }

    var currentQueueIndex: Int = 0
    var showStartTime: Float = -1f

    /**
     * Rockets are spawned based on a predefined queue in the configuration, allowing for precise timing and choreography of fireworks displays.
     */
    fun modeShow(canvas: PGraphics) {
        if (showStartTime == -1f) {
            showStartTime = millis() / 1000f
        }

        val currentShowKey = "show_01" // TODO : load shows dynamically
        val showData = showRegistry[currentShowKey] ?: return

        val elapsedSeconds = (millis() / 1000f) - showStartTime

        if (currentQueueIndex < showData.queue.size) {
            val entry = showData.queue[currentQueueIndex]

            if (elapsedSeconds >= entry.time) {
                val rocketData = rocketRegistry[entry.rocketKey]
                if (rocketData != null) {
                    val newFirework = Config.createRocketFromConfig(entry.x,height.toFloat(), -entry.initialVelocity, rocketData)
                    fireworks.add(newFirework)
                    println("Rakete ${entry.rocketKey} bei ${elapsedSeconds}s gespawned.")
                }
                currentQueueIndex++
            }
        }

        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(canvas) }
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