package azisaba.life.temporary.boat

import BukkitBoat._
import org.bukkit.entity.{Boat, EntityType, Player}
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.{PlayerInteractEvent, PlayerQuitEvent}
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.{Location, Material}

object BoatControlListener extends Listener {

  @EventHandler(ignoreCancelled = true)
  def on(event: EntitySpawnEvent): Unit = {
    if (event.getEntityType == EntityType.BOAT) event.setCancelled(true)
  }

  @EventHandler(ignoreCancelled = true)
  def on(event: PlayerInteractEvent): Unit = {
    if (event.getAction != Action.RIGHT_CLICK_BLOCK || !event.hasItem) return

    val item: ItemStack = event.getItem
    val material: Material = item.getType
    if (!woodenTypeMap.containsKey(material)) return

    val spawnLocation: Location = event.getClickedBlock
      .getRelative(event.getBlockFace)
      .getLocation
      .add(0.5, 0.0, 0.5)

    val player: Player = event.getPlayer
    spawnLocation.setYaw(player.getLocation.getYaw)

    val boat: Boat = spawnLocation.getWorld
      .spawnEntity(spawnLocation, EntityType.BOAT)
      .asInstanceOf[Boat]

    boat.setWoodType(woodenTypeMap.get(material))
    temporaryBoats.add(boat)

    item.setAmount(item.getAmount - 1)
    boat.addPassenger(player)

    event.setCancelled(true)
  }

  @EventHandler(ignoreCancelled = true)
  def on(event: VehicleExitEvent): Unit = event.getVehicle match {
    case boat: Boat => boat.vanish()
    case _ =>
  }

  @EventHandler
  def on(event: PlayerQuitEvent): Unit = event.getPlayer.getVehicle match {
    case boat: Boat => boat.vanish()
    case _ =>
  }

}
