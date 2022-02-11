import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class ZombieSpawner {

	private static int difficulty = 0;
	
	public static ArrayList<Entity> nodmg = new ArrayList<Entity>();
	
	public static ArrayList<Entity> poison = new ArrayList<Entity>();
	
	public static ArrayList<Entity> grave = new ArrayList<Entity>();

	public static ArrayList<Entity> knife = new ArrayList<Entity>();
	public static ArrayList<Entity> mine = new ArrayList<Entity>();

	public static ArrayList<Entity> follow = new ArrayList<Entity>();
	
	public static ArrayList<Grave> graveBlocks = new ArrayList<Grave>();
	public static ArrayList<Block> mineBlocks = new ArrayList<Block>();
	public static ArrayList<Block> knifeBlocks = new ArrayList<Block>();
	
	public static int Graves;
	
	public static void SetDiff(int dif)
	{
		difficulty = dif;
	}
	
	public static void ZombieTick()
	{
		switch(difficulty)
		{
		case 1:
			Diff1();
			break;
		}
	}
	
	protected static void Diff1()
	{
		Random rand = new Random();
		if(rand.nextInt(101) < 30)
		{
			Zombie zombie = (Zombie)Bukkit.getWorlds().get(0).spawnEntity(GetSpawnLocation(), EntityType.ZOMBIE);
			
			zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		}
	}

	protected static Location GetSpawnLocation()
	{
		Random rand = new Random();
		Location loc = new Location(Bukkit.getWorlds().get(0), rand.nextInt(201) - 100, 1, rand.nextInt(201) - 100);
		loc.setY(loc.getWorld().getHighestBlockYAt(loc));
		
		return loc;
	}
	
	public static void SpawnLootZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GREEN + "Loot Zombie");
		zombie.setCustomNameVisible(true);
		
		nodmg.add(zombie);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	}
	
	public static void SpawnFarmerZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.YELLOW + "Farmer Zombie");
		zombie.setCustomNameVisible(true);
		
		ItemStack item = new ItemStack(Material.IRON_SWORD);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(10001);
		item.setItemMeta(meta);
		
		zombie.getEquipment().setItemInMainHand(item);
		
		zombie.getEquipment().setItemInMainHandDropChance(0);
	}
	
	@SuppressWarnings("deprecation")
	public static void SpawnMetalZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GRAY + "Metal Zombie");
		zombie.setCustomNameVisible(true);
		
		zombie.setMaxHealth(40);
		zombie.setHealth(40);
		
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000 * 20, 1, false, false));
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	}
	
	@SuppressWarnings("deprecation")
	public static void SpawnResurrectedZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GOLD + "Resurrected Zombie");
		zombie.setCustomNameVisible(true);
		
		zombie.setMaxHealth(30);
		zombie.setHealth(30);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		
		follow.add(zombie);
	}
	
	@SuppressWarnings("deprecation")
	public static void SpawnSpeedyZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.AQUA + "Speedy Zombie");
		zombie.setCustomNameVisible(true);
		
		zombie.setMaxHealth(15);
		zombie.setHealth(15);
		
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1, false, false));
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	}
	
	public static void SpawnPoisonZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GREEN + "Poison Zombie");
		zombie.setCustomNameVisible(true);
		
		poison.add(zombie);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	}
	
	public static void SpawnGraveZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GOLD + "Grave Zombie");
		zombie.setCustomNameVisible(true);
		
		grave.add(zombie);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.WARPED_PRESSURE_PLATE));
	}
	
	public static void SpawnKnifeZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GOLD + "Knife Zombie");
		zombie.setCustomNameVisible(true);
		
		knife.add(zombie);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	}
	
	public static void SpawnMineZombie(Location loc)
	{
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.setCustomName(ChatColor.GOLD + "Mine Zombie");
		zombie.setCustomNameVisible(true);
		
		mine.add(zombie);
		
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));
	}
}
