import processing.core.PApplet

class ProcessingMain : PApplet()
{
    override fun settings()
    {
        size(400, 400)
    }

    override fun setup()
    {
        background(255)
    }

    override fun draw()
    {
        fill(255f, 0f, 0f)
        ellipse(mouseX.toFloat(), mouseY.toFloat(), 50f, 50f)
    }
}