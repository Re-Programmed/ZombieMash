

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import Loot.LootBoxes;

public class LootManager {

	public static void GenerateLoots()
	{
		for(Location loot : Events.chests)
		{
			LootBoxes spawnloot = randomLoot();

			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage(loot.toString());
			}
			try {
						
			loot.getBlock().setType(Material.AIR);
			loot.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);	
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock " + loot.getBlockX() + " " + loot.getBlockY() + " " + loot.getBlockZ() + " structure_block[mode=load]{name:\"" + spawnloot.name + "\",rotation:\"NONE\",mirror:\"NONE\",mode:\"LOAD\"} replace");
			loot.getBlock().getRelative(BlockFace.DOWN).setType(Material.REDSTONE_BLOCK);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=item]");
	}
	
	public static LootBoxes randomLoot()
	{
		Random rand = new Random();
		return LootBoxes.values()[rand.nextInt(LootBoxes.values().length)];
	}
}
