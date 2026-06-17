import kotlin.math.sqrt

class EventListener (
    private val fireworks: MutableList<AbstractFirework>
){
    fun register(firework: AbstractFirework) {
        fireworks.add(firework)
    }

     fun checkParticleCollision(){
         if(!config.isChainEnabled) return
         val activeRockets = fireworks.filterIsInstance<Rocket>().filter { !it.exploded }
         val explodedRockets = fireworks.filterIsInstance<Rocket>().filter { it.exploded }

         for(activeRocket in activeRockets){
             for(explodedRocket in explodedRockets){
                 for(particle in explodedRocket.particles){
                    val dx = activeRocket.x - particle.x
                    val dy = activeRocket.y - particle.y
                    val dist = sqrt(dx * dx + dy * dy)
                    val collisionThreshold = config.collisionThreshold + (particle.size / 2f)

                    if(dist <= collisionThreshold){
                        activeRocket.vy = 0f
                    }
                }
            }
        }
     }
}