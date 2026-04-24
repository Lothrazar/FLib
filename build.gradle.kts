tasks.register("printPlayerMethods") {
    doLast {
        net.minecraft.world.entity.player.Player.class.getMethods().forEach { m ->
            if (m.name.toLowerCase().contains("respawn")) {
                println(m)
            }
        }
    }
}
