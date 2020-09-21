package amata1219.temporary.boat

import scala.jdk.CollectionConverters._
import com.google.common.collect.HashBiMap
import org.bukkit.{Location, Material, TreeSpecies}
import org.bukkit.Material.{ACACIA_BOAT, BIRCH_BOAT, DARK_OAK_BOAT, JUNGLE_BOAT, OAK_BOAT, SPRUCE_BOAT}
import org.bukkit.TreeSpecies._
import org.bukkit.entity.Boat
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object BukkitBoat {

  val temporaryBoats: mutable.Set[Boat] = mutable.Set()

  val woodenTypeMap: HashBiMap[Material, TreeSpecies] = HashBiMap.create(Map(
    ACACIA_BOAT -> ACACIA,
    BIRCH_BOAT -> BIRCH,
    DARK_OAK_BOAT -> DARK_OAK,
    JUNGLE_BOAT -> JUNGLE,
    OAK_BOAT -> GENERIC,
    SPRUCE_BOAT -> REDWOOD,
  ).asJava)

  implicit class XBoat(boat: Boat) {
    def vanish(): Unit = {
      if (temporaryBoats.contains(boat)) {
        val location: Location = boat.getPassengers
          .asScala
          .headOption
          .getOrElse(boat)
          .getLocation

        val woodenType: Material = woodenTypeMap.inverse()
          .getOrDefault(boat.getWoodType, OAK_BOAT)

        val item: ItemStack = new ItemStack(woodenType)
        location.getWorld.dropItem(location, item)

        temporaryBoats.remove(boat)
      }

      boat.remove()
    }

    override def hashCode(): Int = boat.getUniqueId.hashCode()
  }

}
