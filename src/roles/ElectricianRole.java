package roles;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ElectricianRole extends Roles {

	@Override
	public String GetStartMessage() {
		return ChatColor.GREEN + "Electrician: \n"
		+ "  - Open electricity boxes with wire cutters.\n"
		+ "  - Disarm mines and knives with wire cutters.\n"
		+ "  - Access utility tunnels.";
	}


	@Override
	public void GameStart() {
		for(Player p : players)
		{
			ItemStack i = new ItemStack(Material.SHEARS, 1);
			
			ItemMeta m = i.getItemMeta();
			
			m.setDisplayName(ChatColor.GREEN + "Wire Cutters");
			
			p.getInventory().addItem(i);
		}
	}

}
