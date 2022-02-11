package roles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MercenaryRole extends Roles {

	@Override
	public String GetStartMessage() {
		return  ChatColor.DARK_AQUA + "Mercenary: \n"
				+ "  - Place turrets to shoot players and zombies.\n"
				+ "  - Start with an APK.\n"
				+ "  - Starts with 64 ammo.";
	}

	@Override
	public void GameStart() {
		for(Player p : players)
		{
			ItemStack turret = new ItemStack(Material.END_ROD, 1);
			ItemMeta meta = turret.getItemMeta();
			
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Mercenary Turret");
			
			List<String> lore = new ArrayList<String>();
			
			lore.add(ChatColor.DARK_PURPLE + "Shoots nearby players & zombies!");
			
			meta.setLore(lore);
			
			turret.setItemMeta(meta);
			
			Item i = p.getWorld().dropItemNaturally(p.getLocation(), turret);
			
			i.setPickupDelay(0);
			
			ItemStack AK = new ItemStack(Material.WOODEN_SWORD, 1);
			ItemMeta meta2 = AK.getItemMeta();
		
			meta2.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "APK");
			
			lore = new ArrayList<String>();
			
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Assult Rifle");
			
			meta2.setLore(lore);
			
			AK.setItemMeta(meta2);
			
			Item AKi = p.getWorld().dropItemNaturally(p.getLocation(), AK);
			
			AKi.setPickupDelay(0);
			
			ItemStack medammo = new ItemStack(Material.GOLD_NUGGET, 32);
			ItemMeta meta3 = medammo.getItemMeta();
			meta3.setDisplayName(ChatColor.RED + "APK Ammo");
			medammo.setItemMeta(meta3);
			
			Item Ammoi = p.getWorld().dropItemNaturally(p.getLocation(),medammo);
			
			Ammoi.setPickupDelay(0);
		}
	}

}
