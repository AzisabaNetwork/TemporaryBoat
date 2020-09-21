package amata1219.temporary.boat

import amata1219.temporary.boat.BukkitBoat._

import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class Main extends JavaPlugin() {

  Main.instance = this

  override def onEnable(): Unit = {
    getServer.getPluginManager.registerEvents(BoatControlListener, this)
  }

  override def onDisable(): Unit = {
    HandlerList.unregisterAll(this)
    BukkitBoat.temporaryBoats.foreach(_.vanish())
  }

}

object Main {
  var instance: Main = _
}
