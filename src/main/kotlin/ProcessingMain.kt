import processing.core.PApplet
import com.google.gson.Gson

object Global {
    lateinit var papp: PApplet
}

class ProcessingMain : PApplet() {

    private val fireworks = mutableListOf<AbstractFirework>()
    private var paused = false

    override fun settings() {
        size(config.windowSizeX, config.windowSizeY)
    }

    override fun setup() {
        Global.papp = this
        background(255)
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
    }

    override fun draw() {
        // setup background color using config values
        background(config.backgroundColor[0],
            config.backgroundColor[1],
            config.backgroundColor[2],
            config.backgroundColor[3]
        )
        // ground: lines use stroke (not fill). set stroke color and weight, and remove trailing comma
        stroke(255f, 255f, 255f)
        strokeWeight(2f)
        line(80f, height.toFloat() -10f, width.toFloat() - 80f, height.toFloat() - 10f)
        noStroke()

        if(config.mode == "normal"){
            modeNormal()
        }
        if(config.mode == "show"){
            modeShow()
        }
    }



    fun modeNormal() {
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

        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(this) }
        fireworks.removeAll { it.isDead }
    }

    var currentQueueIndex: Int = 0
    var showStartTime: Float = -1f
    fun modeShow() {
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
                    println("Rakete ${entry.rocketKey} bei ${elapsedSeconds}s gespawnt.")
                }
                currentQueueIndex++
            }
        }

        fireworks.forEach { it.update() }
        fireworks.forEach { it.draw(this) }
        fireworks.removeAll { it.isDead }
    }

    // help functions
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
    fun isMouseLeftPressed(): Boolean {
        return mousePressed && mouseButton == LEFT
    }
}