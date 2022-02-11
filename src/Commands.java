import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import roles.MedicRole;
import roles.MercenaryRole;

public class Commands implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg2.equalsIgnoreCase("setrole"))
		{
			if(arg3[0].equalsIgnoreCase("medic"))
			{
				MedicRole mr = new MedicRole();
				if(arg0 instanceof Player)
				{
					mr.AddRole((Player)arg0);
					mr.GameStart();
					arg0.sendMessage(arg2);
				}
			}else if(arg3[0].equalsIgnoreCase("merc"))
			{
				MercenaryRole mr = new MercenaryRole();
				if(arg0 instanceof Player)
				{
					mr.AddRole((Player)arg0);
					mr.GameStart();
					arg0.sendMessage(arg2);
				}
			}
		}
		
		if(arg2.equalsIgnoreCase("cutscene"))
		{
			MenuManager.LoadMainMenu();
			return true;
		}
		
		if(arg2.equalsIgnoreCase("credits"))
		{
			arg0.sendMessage(ChatColor.GOLD + "Textures:\n" + ChatColor.WHITE
					+ "    Zombie Skins - ForemostTrack87\n"
					+ "    Guns, Grenades, and Ammo - ForemostTrack87\n"
					+ "    Barrels, Mines, Knives, Health Pads, Roads, Bullet Holes, Logo, Blood, Explosion Marks, Zombie Parts, Chests, GUI - Re_Programmed\n"
					+ ChatColor.GOLD + "Models:\n" + ChatColor.WHITE
					+ "    Turret - ForemostTrack87\n"
					+ "    Barrels, Mines, Knives, Health Pads, Blood, Zombie Parts - Re_Programmed\n"
					+ ChatColor.GOLD + "Sounds:\n" + ChatColor.WHITE
					+ "    HD Audio - AudioCraftV1.5 & Enhanced+Audio+r3\n"
					+ ChatColor.GOLD + "All Coding:\n" + ChatColor.WHITE
					+ "    Re_Programmed\n"
					+ ChatColor.GOLD + "Building & Map:\n" + ChatColor.WHITE
					+ "    Re_Programmed, ForemostTrack87, CodeNameChewy, PancakesRGood4U, TheEndChicken, N_N_I_F\n"
					+ "\n");
		}
		
		if (arg2.equalsIgnoreCase("summonzombie")) {
			
			if(arg3[0].equalsIgnoreCase("mine"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnMineZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("knife"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnKnifeZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("grave"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnGraveZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("res"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnResurrectedZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("poison"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnPoisonZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("speedy"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnSpeedyZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			
			if(arg3[0].equalsIgnoreCase("metal"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnMetalZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			if(arg3[0].equalsIgnoreCase("farmer"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnFarmerZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			if(arg3[0].equalsIgnoreCase("loot"))
			{
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					ZombieSpawner.SpawnLootZombie(sender.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Spawned");
				}
			}
			if (arg3[0].equalsIgnoreCase("grenade")) {
				if(arg0 instanceof Player)
				{
					Player sender = (Player)arg0;
					Entity e = Bukkit.getWorld("map").spawnEntity(sender.getLocation(), EntityType.ZOMBIE);
		    		e.setCustomName(ChatColor.GOLD + "Grenade Zombie");
		    		e.setCustomNameVisible(true);
		    		if(e instanceof LivingEntity)
		    		{
		    			LivingEntity le = (LivingEntity)e;
		    			Main.grenadeZombies.add(le);
		    			le.setMaxHealth(le.getHealth() * 2f);
		    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
		    			le.setHealth(le.getHealth() * 2f);
		    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1));
		    			le.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
		    			le.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		    			le.getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
		    			le.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		    			le.getEquipment().setItemInMainHand(new ItemStack(Material.YELLOW_DYE));
		    			le.getEquipment().setItemInMainHandDropChance(0f);
		    		}
				}
			}
		}
		if(arg2.equalsIgnoreCase("start"))
		{
		
			
			/*
			Location loc = new Location(Bukkit.getServer().getWorld("world"), Bukkit.getServer().getWorld("world").getSpawnLocation().getX(), Bukkit.getServer().getWorld("world").getSpawnLocation().getY(), Bukkit.getServer().getWorld("world").getSpawnLocation().getZ());
	        for(Player player : Bukkit.getOnlinePlayers())
	        {
	        	player.teleport(loc);
	        }
			rollback("map");
            loc = new Location(Bukkit.getServer().getWorld("map"), Bukkit.getServer().getWorld("map").getSpawnLocation().getX(), Bukkit.getServer().getWorld("map").getSpawnLocation().getY(), Bukkit.getServer().getWorld("map").getSpawnLocation().getZ());
	        for(Player player : Bukkit.getOnlinePlayers())
	        {
	        	player.teleport(loc);
	        }
	        */
			LootManager.GenerateLoots();
			
			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage(ChatColor.GREEN + "Started");
			}
									
			Events.knives = new ArrayList<Location>();
			Events.kinvesPlayer = new ArrayList<Player>();
			for(Location mine : Main.knivesStart)
			{
				Events.knives.add(mine);
				Events.kinvesPlayer.add(null);
				mine.getBlock().setType(Material.CRIMSON_PRESSURE_PLATE);
				arg0.sendMessage(mine.toString());
			}
			
			Events.AutoCharges = new ArrayList<Location>();
			for(Location mine : Main.minesStart)
			{
				Events.AutoCharges.add(mine);
				mine.getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
				arg0.sendMessage(mine.toString());
			}
			
            return true;
		}
		
		if(arg2.equalsIgnoreCase("resetloots"))
		{
			Events.chests = new ArrayList<Location>();
			Main.config.set("Loots", new ArrayList<Location>());
			arg0.sendMessage("Reset");
		}
		
		if(arg2.equalsIgnoreCase("loadmines"))
		{
			Events.AutoCharges = new ArrayList<Location>();
			for(Location mine : Main.minesStart)
			{
				Events.AutoCharges.add(mine);
				mine.getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
				arg0.sendMessage(mine.toString());
			}
		}
		
		if(arg2.equalsIgnoreCase("loadknives"))
		{
			Events.knives = new ArrayList<Location>();
			Events.kinvesPlayer = new ArrayList<Player>();
			for(Location mine : Main.knivesStart)
			{
				Events.knives.add(mine);
				Events.kinvesPlayer.add(null);
				mine.getBlock().setType(Material.CRIMSON_PRESSURE_PLATE);
				arg0.sendMessage(mine.toString());
			}
		}
		
		if(arg2.equalsIgnoreCase("loadloots"))
		{
			LootManager.GenerateLoots();
		}
		
		return false;
	}

	 public static void unloadMap(String mapname){
	        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
	            Bukkit.getLogger().info("Successfully unloaded " + mapname);
	        }else{
	            Bukkit.getLogger().severe("COULD NOT UNLOAD " + mapname);
	        }
	    }
	    //Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
	    public static void loadMap(String mapname){
	        Bukkit.getServer().createWorld(new WorldCreator(mapname));
	    }
	 
	    //Maprollback method, because were too lazy to type 2 lines
	    public static void rollback(String mapname){
	        unloadMap(mapname);
	        loadMap(mapname);
	    }
	
}
