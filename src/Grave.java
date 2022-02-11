import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.FaceAttachable.AttachedFace;
import org.bukkit.entity.ArmorStand;

import net.md_5.bungee.api.ChatColor;

public class Grave {

	final Block block;
	final ArmorStand stand;
	int damage = 100;
	
	public Grave(ArmorStand stand, Block block)
	{
		this.block = block;
		this.stand = stand;
	}
	
	public void Damage(int amount)
	{
		damage -= amount;
		
		if(damage < 1)
		{
			ZombieSpawner.graveBlocks.remove(this);
			
			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle block cobblestone " + block.getLocation().getX() + " " + block.getLocation().getY() + " " + block.getLocation().getZ() + " 0 0 0 1 90 normal");
			
			block.setType(Material.CRIMSON_BUTTON);
			
			FaceAttachable b = (FaceAttachable)block.getBlockData();
			
			b.setAttachedFace(AttachedFace.FLOOR);
			
			block.setBlockData(b);
			
			stand.remove();
			return;
		}
		
		stand.setCustomName(ChatColor.GREEN + "Grave " + ChatColor.WHITE + "-" + ChatColor.RED + " " + damage + " HP");
		stand.setCustomNameVisible(true);
	}
	
}
