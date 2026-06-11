import processing.core.PApplet
import com.google.gson.Gson
import java.io.File

val gson = Gson()
const val configFilePath = "src/main/resources/config.json"
const val fireworkParentPath = "src/main/resources/fireworks"
const val showsParentPath = "src/main/resources/shows"

lateinit var config: Config.GlobalConfig
var rocketRegistry = mutableMapOf<String, Config.RocketConfigData>()
var showRegistry = mutableMapOf<String, Config.ShowConfig>()

fun main()
{
   //load JSON data
   val configFileText = File(configFilePath).readText()
   println("Loaded config: $configFileText")
   config = gson.fromJson(configFileText, Config.GlobalConfig::class.java)
   println("Parsed config: $config")
   //load rockets from JSON data
    val rocketConfigFiles = File(fireworkParentPath).listFiles { file -> file.extension == "json" } ?: arrayOf()
    for (file in rocketConfigFiles) {
        val rocketConfigText = file.readText()
        println("Loaded rocket config from ${file.name}: $rocketConfigText")
        val rocketConfig = gson.fromJson(rocketConfigText, Config.RocketConfigData::class.java)
        println("Parsed rocket config from ${file.name}: $rocketConfig")
        rocketRegistry[file.nameWithoutExtension] = rocketConfig
    }

    //load shows if mode is show
    if(config.mode == "show"){
        val showConfigFiles = File(showsParentPath).listFiles { file -> file.extension == "json" } ?: arrayOf()
        for (file in showConfigFiles) {
            val showConfigText = file.readText()
            println("Loaded show config from ${file.name}: $showConfigText")
            var show = gson.fromJson(showConfigText, Config.ShowConfig::class.java)
            show = show.copy(queue = show.queue.sortedBy { it.time })
            println("Parsed show config from ${file.name}: $show")
            showRegistry[file.nameWithoutExtension] = show
        }
    }

   PApplet.main("ProcessingMain")
}