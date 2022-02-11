package roles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MedicRole extends Roles {

	@Override
	public String GetStartMessage() {
		return ChatColor.DARK_PURPLE + "Medic: \n"
		+ "  - Heal other players by right clicking a player with the Med Pack.\n"
		+ "  - Place healing stations that heal players in a radius.\n"
		+ "  - Starts with Health Pads.";
	}

	@Override
	public void GameStart() {
		for(Player p : players)
		{
			ItemStack HPPad = new ItemStack(Material.BLACK_CARPET, 1);
			
			ItemMeta meta = HPPad.getItemMeta();
			
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Healing Pad");
			
			List<String> lore = new ArrayList<String>();
			
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Place down to heal players");
			lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Also heals zombies");

			meta.setLore(lore);
			
			HPPad.setItemMeta(meta);
			
			p.getInventory().addItem(HPPad);
			
			ItemStack HPStation = new ItemStack(Material.GRINDSTONE, 1);
			
			meta = HPStation.getItemMeta();
			
			meta.setDisplayName(ChatColor.RED + "HP Station");
			
			HPStation.setItemMeta(meta);
			
			p.getInventory().addItem(HPStation);
		}
	}

}
