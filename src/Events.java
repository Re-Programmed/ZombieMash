import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Events implements Listener{
	
	public final Plugin plugin;
	
	public static ArrayList<Player> players;
	public static ArrayList<PlayerData> playerdata;
	public static ArrayList<Location> bulletHoles;
	
	public static ArrayList<Location> chests;
	
	public static ArrayList<Player> playersWearingPump;
	public static ArrayList<ItemStack> originalHelm;
	
	public static ArrayList<Location> Turrets;
	public static ArrayList<Location> Charges;

	public static ArrayList<Location> AutoCharges;
	
	public static ArrayList<Location> Healing;
	
	public static ArrayList<Location> knives;
	public static ArrayList<Location> playerKnives = new ArrayList<Location>();
	
	public static ArrayList<Player> PlayerTurrets;
	
	public static ArrayList<Player> kinvesPlayer;
	
	public static HashMap<String, Location> foundSigns = new HashMap<String, Location>();
	
	public static ArrayList<Block> opentds = new ArrayList<Block>();
	
	public static boolean OneOrAll = false;
	
	public static ArrayList<Location> placedBlocks;
	
	public static ArrayList<Block> healingStations = new ArrayList<Block>();
	
	public static ArrayList<Location> resetElecBox = new ArrayList<Location>();
	
	 @EventHandler
	 public void blockInteraction(PlayerInteractEvent event)
	 {
		 if(event.getClickedBlock() != null)
		 {
			 if(event.getClickedBlock().getType() == Material.ACACIA_BUTTON)
			 {
				 if(event.getPlayer().getHealth() < 20)
				 {
					 event.getPlayer().setHealth(event.getPlayer().getHealth() + 0.2f);
				 }
			 }
		 }
	 }
	
	@EventHandler
	public void Place(BlockPlaceEvent event)
	{
		if(event.getBlock().getType() == Material.GRINDSTONE)
		{
			healingStations.add(event.getBlock());
		}
	}
	
	@EventHandler
	public void Break(BlockBreakEvent event)
	{
		
		if(event.getBlock().getType() == Material.GRINDSTONE)
		{
			if(healingStations.contains(event.getBlock()))
			{
				healingStations.remove(event.getBlock());
			}
		}
	}
	
	public Events(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void InteractEvent(PlayerInteractEvent event)
	{
		if(event.getItem() != null)
		{
			if(event.getItem().getType() == Material.SHEARS)
			{
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if(event.getClickedBlock() != null)
					{
						if(event.getClickedBlock().getType() == Material.CRIMSON_PRESSURE_PLATE || event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
						{
							event.getClickedBlock().setType(Material.AIR);
							event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
						}
						
						if(event.getClickedBlock().getType() == Material.JUNGLE_BUTTON)
						{
							resetElecBox.add(event.getClickedBlock().getLocation());
			    			event.setCancelled(true);
			    			
							Material mat = event.getClickedBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
			    			
			    			Missions.GenLoot(event.getClickedBlock().getLocation());
			    			
			    			event.getClickedBlock().setType(Material.WARPED_NYLIUM);
			    			event.getClickedBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).setType(mat);
			    			
			    			Random rand = new Random();
			    			
			    			for(Entity e : event.getClickedBlock().getWorld().getNearbyEntities(event.getClickedBlock().getLocation(), 2f, 2f, 2f))
			    			{
			    				if(e instanceof Item)
			    				{
			    					if(rand.nextInt(101) < 74)
			    					{
			    						e.remove();
			    					}
			    					
			    					if(((Item)e).getItemStack().getType() == Material.CHEST)
			    					{
			    						e.remove();
			    					}
			    				}
			    			}
			    			
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent event)
	{
		for(Block b : healingStations)
		{
			if(b.getLocation().distance(event.getPlayer().getLocation()) < 10)
			{
				Missions.CreateParticleCircle(10, 1, 0, 0, b.getLocation());
				
				event.getPlayer().setHealth(event.getPlayer().getHealth() + 0.04f);
			}
		}
		
		for(int x = -2; x < 2; x++)
		{
			for(int z = -2; z < 2; z++)
			{
				for(int y = -1; y < 1; y++)
				{
					Location loc = event.getPlayer().getLocation().clone();
					loc.setX(loc.getX() + x);
					loc.setY(loc.getY() + y);
					loc.setZ(loc.getZ() + z);
					
					if(loc.getBlock().getType() == Material.WARPED_TRAPDOOR)
					{
						TrapDoor td = (TrapDoor)loc.getBlock().getBlockData();
						
						td.setOpen(true);
						
						loc.getBlock().setBlockData(td);
						
						opentds.add(loc.getBlock());
					}
				}
			}
		}
		
		for(Block tdb : opentds)
		{
			if(tdb.getLocation().distance(event.getPlayer().getLocation()) > 3)
			{
				TrapDoor td = (TrapDoor)tdb.getBlockData();
				
				td.setOpen(false);
				
				tdb.setBlockData(td);
				
				opentds.remove(tdb);
				break;
			}
		}
		
	}
	
	@EventHandler
	public void death(EntityDeathEvent event)
	{
		if(event.getEntity().getType() == EntityType.ZOMBIE)
		{
			if(ZombieSpawner.mine.contains(event.getEntity()))
			{
				for(Block b : ZombieSpawner.mineBlocks)
				{
					b.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, b.getLocation(), 10);
					
					b.getWorld().playSound(b.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					
					b.setType(Material.AIR);
					
					for(Entity e : b.getWorld().getNearbyEntities(b.getLocation(), 5f, 5f, 5f))
					{
						if(e.getType() == EntityType.PLAYER)
						{
							((LivingEntity)e).damage(5);
						}
						
						
						if(e instanceof ArmorStand)
						{
							if(e.getCustomName().contains("Zombie Charge"))
							{
								e.remove();
							}
						}
					}
				}
				
				ZombieSpawner.mineBlocks.clear();
			}
		}
	}
	
	@EventHandler
	public void RClick(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock() != null)
			{
				if(event.getClickedBlock().getType() == Material.WARPED_BUTTON)
				{
					boolean found = false;
					for(Map.Entry<String, Location> entry : foundSigns.entrySet())
					{
						if(entry.getKey().contains(event.getPlayer().getName()))
						{
							if(event.getClickedBlock().getLocation().distance(entry.getValue()) < 0.5f)
							{
								found = true;
							}
						}
					}
					
					if(!found)
					{
						Random rand = new Random();
						foundSigns.put(rand.nextInt(99999) + event.getPlayer().getName() + rand.nextInt(99999), event.getClickedBlock().getLocation());
						
						int foundv = 0;
						for(Map.Entry<String, Location> entry : foundSigns.entrySet())
						{
							if(entry.getKey().contains(event.getPlayer().getName()))
							{
								foundv++;
							}
						}
						
						if(foundv == 1)
						{
							event.getPlayer().sendMessage(ChatColor.GREEN + "You have found " + foundv + " Zombie Sign.");
						}else {
							event.getPlayer().sendMessage(ChatColor.GREEN + "You have found " + foundv + " Zombie Signs.");
						}
						
						switch(foundv)
						{
						case 5:
							Main.GrantAdvance(event.getPlayer(), "sign5");
							break;
						case 10:
							Main.GrantAdvance(event.getPlayer(), "sign10");
							break;
						case 15:
							Main.GrantAdvance(event.getPlayer(), "sign15");
							break;
						case 25:
							Main.GrantAdvance(event.getPlayer(), "signs25");
							break;
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event)
	{
		if(event.getEntity().getType() == EntityType.ZOMBIE)
		{
			((LivingEntity)event.getEntity()).setMaxHealth(100);
		}
	}

	
	@EventHandler
	public void PlayerInteraction(PlayerInteractEvent event)
	{
		Missions.FixMission(event);
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event)
	{
		if(event.getBlock().getType() == Material.CRIMSON_FENCE)
		{
			for(Entity e : event.getBlock().getWorld().getNearbyEntities(new Location(event.getBlock().getLocation().getWorld(), event.getBlock().getLocation().getX(), event.getBlock().getLocation().getY() + 1, event.getBlock().getLocation().getZ()), 1f, 1f, 1f))
			{
				if(e instanceof ArmorStand)
				{
					if(e.getCustomName().contains(ChatColor.BLUE + "Battery"))
					{
						e.remove();
					}
				}
			}
			
			event.setCancelled(true);
			
			ItemStack battery = new ItemStack(Material.CRIMSON_FENCE);
			ItemMeta meta = battery.getItemMeta();
			
			meta.setDisplayName(ChatColor.GOLD + "Battery");
			
			battery.setItemMeta(meta);
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), battery);
			event.getBlock().setType(Material.AIR);
			
			return;
		}
		
		if(!placedBlocks.contains(event.getBlock().getLocation()))
		{
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "You can only break blocks placed by players!");
				event.setCancelled(true);
			}
		}
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			if(event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
			{
				event.setCancelled(true);
			}
			if(event.getBlock().getType() == Material.END_ROD)
			{
				event.setCancelled(true);
				
			}
			if(event.getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
			{
				event.setCancelled(true);
			}
			if(event.getBlock().getType() == Material.LIME_CARPET || event.getBlock().getType() == Material.BLACK_CARPET)
			{
				event.setCancelled(true);
			}
			if(event.getBlock().getType() == Material.ACACIA_LEAVES)
			{
				event.setCancelled(true);
			}
			if(event.getBlock().getType() == Material.WARPED_PRESSURE_PLATE)
			{
				event.setCancelled(true);
			}
		}
		
		
			for(GarageDoor door : Main.garageDoors)
			{
				for(Block block : door.allblocks)
				{
					if(block.getLocation().distance(event.getBlock().getLocation()) < 1)
					{
						door.Open();
					}
				}
			}
		
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event)
	{
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			placedBlocks.add(event.getBlock().getLocation());
		}else {
			if(event.getBlock().getType() == Material.WARPED_PRESSURE_PLATE)
			{				
				ArmorStand stand = (ArmorStand)event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.ARMOR_STAND);
				
				stand.setCustomName(ChatColor.GREEN + "Grave " + ChatColor.WHITE + "-" + ChatColor.RED + " 100 HP");
				stand.setCustomNameVisible(true);
				stand.setInvisible(true);
				stand.setGravity(false);
				stand.setInvulnerable(true);
				stand.setSmall(true);
				
				ZombieSpawner.graveBlocks.add(new Grave(stand, event.getBlock()));
			}
		}
		
		if(event.getBlock().getType() == Material.OAK_SAPLING)
		{
			for (int i = 0; i < 150; i++) {
				event.getBlock().applyBoneMeal(BlockFace.DOWN);
			}
		}
		
		if(event.getBlock().getType() == Material.GRAY_TERRACOTTA)
		{
			event.getBlock().setType(Material.CRIMSON_PRESSURE_PLATE);
			knives.add(event.getBlock().getLocation());
			kinvesPlayer.add(event.getPlayer());
			
			if(GetScoreboardVal(event.getPlayer(), "knivesplace") < 20)
			{
				AddScoreboardVal(event.getPlayer(), "knivesplace", 1);
				
				if(GetScoreboardVal(event.getPlayer(), "knivesplace") >= 20)
				{
					Main.GrantAdvance(event.getPlayer(), "knivesplace20");
				}
			}
		}
		if(event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
		{
			AutoCharges.add(event.getBlock().getLocation());
		}
		if(event.getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
		{
			Charges.add(event.getBlock().getLocation());
		}
		if(event.getBlock().getType() == Material.END_ROD)
		{
			Turrets.add(event.getBlock().getLocation());
			PlayerTurrets.add(event.getPlayer());
			if(Turrets.size() > 3)
			{
				Turrets.remove(3);
			}
		}
		if(event.getBlock().getType() == Material.BLACK_CARPET)
		{
			Healing.add(event.getBlock().getLocation());
		}
		if(event.getBlock().getType() == Material.TRAPPED_CHEST)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Location> loots = (ArrayList<Location>) Main.config.getList("Loots");
			loots.add(event.getBlock().getLocation());
			Main.config.set("Loots", loots);
			event.getPlayer().sendMessage(loots.toString());
		}
	}
	
		@EventHandler
		public void onEntityExplode(EntityExplodeEvent e){
			
			for(Block block : e.blockList())
			{
				if(block.getType() == Material.GRASS_BLOCK)
				{
					Random rand = new Random();
					if(rand.nextBoolean())
					{
						if(rand.nextBoolean())
						{
							block.setType(Material.COARSE_DIRT);
						}else {
							//Nothing
						}
					}else {
						if(rand.nextBoolean())
						{
							block.setType(Material.GRASS_PATH);
						}else {
							block.setType(Material.PODZOL);
						}
					}
				}
				
				if(block.getType() == Material.BLACK_CONCRETE)
				{
					Random rand = new Random();
					if(rand.nextBoolean())
					{
						if(rand.nextBoolean())
						{
							block.setType(Material.GRAY_CONCRETE_POWDER);
						}else {
							//Nothing
						}
					}else {
						if(rand.nextBoolean())
						{
							block.setType(Material.GRAVEL);
						}else {
							block.setType(Material.CYAN_TERRACOTTA);
						}
					}
				}
				
				if(block.getType() == Material.OAK_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
				{
					block.breakNaturally();
				}
				
				if(block.getType() == Material.PINK_STAINED_GLASS_PANE || block.getType() == Material.ORANGE_STAINED_GLASS_PANE || block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE ||  block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE || block.getType() == Material.BLUE_STAINED_GLASS_PANE ||  block.getType() == Material.BLACK_STAINED_GLASS_PANE || block.getType() == Material.WHITE_STAINED_GLASS_PANE || block.getType() == Material.LIGHT_GRAY_STAINED_GLASS)
				{
	                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
	                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
					block.breakNaturally();	
					for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 5, 5, 5))
					{
						if(entity instanceof Player)
						{
							if(GetScoreboardVal((Player)entity, "glassdie") < 30)
							{
								AddScoreboardVal((Player)entity, "glassdie", 1);
								
								if(GetScoreboardVal((Player)entity, "glassdie") >= 30)
								{
									Main.GrantAdvance((Player)entity, "breakglassgrenade30");
								}
							}
						}
					}
				}
				
				if(block.getType() == Material.REDSTONE_LAMP || block.getType() == Material.GLOWSTONE || block.getType() == Material.SEA_LANTERN || block.getType() == Material.LANTERN || block.getType() == Material.SOUL_LANTERN)
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle flame " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.5 0.5 0.5 1 100 normal @a");
	                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
	                block.getWorld().playSound(block.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, 1);
					block.breakNaturally();	
										
					for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 5, 5, 5))
					{
						if(entity instanceof Player)
						{
							if(GetScoreboardVal((Player)entity, "glowdie") < 5)
							{
								AddScoreboardVal((Player)entity, "glowdie", 1);
								
								if(GetScoreboardVal((Player)entity, "glowdie") >= 5)
								{
									Main.GrantAdvance((Player)entity, "5glowstonegrande");
								}
							}
						}
						
						if(entity instanceof LivingEntity)
						{
							((LivingEntity) entity).damage(8);
						}
						entity.setFireTicks(100);
					}
					
				}
				
				if(block.getType() == Material.OAK_LOG)
				{
					Random rand = new Random();
					if(rand.nextInt(101) < 33)
					{
						block.setType(Material.DARK_OAK_LOG);
					}
				}
				
				
			}
			
			if(e.getEntity() instanceof TNTPrimed)
			{
				TNTPrimed tnt = (TNTPrimed)e.getEntity();
				if(tnt.isGlowing())
				{
					if(tnt.isSilent())
					{
						tnt.getWorld().createExplosion(tnt.getLocation(), 4f, false, false);
					}else {
						tnt.getWorld().createExplosion(tnt.getLocation(), 4f, true, false);
					}
					
					int count = 0;
					
					for(Entity en : tnt.getNearbyEntities(4f, 4f, 4f))
					{
						if(en instanceof Zombie)
						{
							if(((Zombie) en).getHealth() <= 20)
							{
								count++;
								for(Entity p : tnt.getNearbyEntities(10f, 10f, 10f))
								{
									if(p instanceof Player)
									{
										if(count >= 10)
										{
											Main.GrantAdvance((Player)p, "combogrenade10");
										}
										
										if(GetScoreboardVal((Player)p, "zombiegren") < 25)
										{
											AddScoreboardVal((Player)p, "zombiegren", 1);
											
											if(GetScoreboardVal((Player)p, "zombiegren") >= 25)
											{
												Main.GrantAdvance((Player)p, "grenadekill25");
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
		    e.blockList().clear();
		    if(e.getLocation().getBlock().getType() == Material.AIR)
		    {
		    	if(e.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
		    	{
		    		e.getLocation().getBlock().setType(Material.ACTIVATOR_RAIL);
		    	}
		    }
		   
		}
	
		@EventHandler
		public void onPlayerDie(PlayerDeathEvent event)
		{
			if(Main.customDeath)
			{
				event.setDeathMessage(ChatColor.RED + Main.customDeathMessage);
				Main.customDeath = false;
			}else {
				event.setDeathMessage(ChatColor.RED + event.getDeathMessage());
			}
        	event.getEntity().getEquipment().setHelmet(new ItemStack(Material.AIR));
		}
		
	@EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() != null){
            if (player.getInventory().getItemInMainHand().getType() == Material.STONE_HOE || player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL || player.getInventory().getItemInMainHand().getType() == Material.IRON_SHOVEL){
            	if(!player.isSneaking())
            	{
            		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 187, true));
            		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000000, 100, true));
            		playersWearingPump.add(player);
            		originalHelm.add(player.getEquipment().getHelmet());
            		player.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
            	}
            }
         }
        
        if(player.hasPotionEffect(PotionEffectType.SLOW))
        {
        	if(player.getPotionEffect(PotionEffectType.SLOW).getAmplifier() == 100)
        	{
        		if(player.isSneaking())
            	{
	        		player.removePotionEffect(PotionEffectType.SLOW);
	        		player.removePotionEffect(PotionEffectType.JUMP);
	            	player.getEquipment().setHelmet(new ItemStack(Material.AIR));
            		originalHelm.remove(playersWearingPump.indexOf(player));
            		playersWearingPump.remove(player);
            	}
        	}
        }
	}
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent event)
	{
		if(event.getEntity().getType() == EntityType.PLAYER)
		{
			if(event.getDamager().getType() == EntityType.ZOMBIE)
			{
				if(event.getDamager().getCustomName() == ChatColor.GREEN + "Poison Zombie")
				{
					if(event.getEntity() instanceof LivingEntity)
					{
						((LivingEntity)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4000, 1));
					}
				}
			}
		}
	
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block detector_rail " + event.getEntity().getLocation().getX() + " " + (event.getEntity().getLocation().getY() + 1) + " " + event.getEntity().getLocation().getZ() + " .1 .1 .1 1 " + Math.toIntExact(Math.round(50 * event.getDamage())) + " normal");
		Block block = event.getEntity().getLocation().getBlock();
		if(block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_DIORITE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_GRANITE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_BLACKSTONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.STONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.SMOOTH_STONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.STONE_BRICK_SLAB)
		{
			Random rand = new Random();
			if(rand.nextInt(101) < 30) {return;}
			if(rand.nextBoolean())
			{
				block.setType(Material.DETECTOR_RAIL);
				if(rand.nextInt(101) < 30)
				{
					if(rand.nextBoolean())
					{
						block.setType(Material.RAIL);
					}else if(block.getRelative(BlockFace.DOWN).getType() == Material.BLACK_CONCRETE){
						block.setType(Material.POWERED_RAIL);
						block.getRelative(BlockFace.DOWN).setType(Material.REDSTONE_BLOCK);
					}
				}
			}else {
				block.setType(Material.POWERED_RAIL);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		//event.getPlayer().setResourcePack("https://www.dropbox.com/s/l71wkdika1otbus/ZombiePackv2.zip?dl=1");
		event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + " is ready to face the challenge!");
	}
	
	@EventHandler
	public void EntityInteractEvent(org.bukkit.event.entity.EntityInteractEvent event)
	{
		if(event.getEntityType() == EntityType.ARMOR_STAND)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.getBlock().getType() == Material.ACACIA_LEAVES)
		{
			Material mat = event.getBlock().getRelative(BlockFace.DOWN).getType();
			if(mat == Material.SMOOTH_STONE_SLAB || mat == Material.STONE_BRICK_SLAB || mat == Material.STONE_SLAB || mat == Material.QUARTZ_SLAB || mat == Material.POLISHED_BLACKSTONE_SLAB || mat == Material.POLISHED_GRANITE_SLAB || mat == Material.POLISHED_DIORITE_SLAB)
			{
				event.getBlock().setType(Material.JUNGLE_LEAVES);
			}
		}
		
		if(event.getBlock().getType() == Material.JIGSAW)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Location> mines = (ArrayList<Location>) Main.config.getList("StartMines");
			mines.add(event.getBlock().getLocation());
			Main.config.set("StartMines", mines);
			event.getPlayer().sendMessage(mines.toString());
		}
		
		if(event.getBlock().getType() == Material.BROWN_GLAZED_TERRACOTTA)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Location> mines = (ArrayList<Location>) Main.config.getList("StartKnives");
			mines.add(event.getBlock().getLocation());
			Main.config.set("StartKnives", mines);
			event.getPlayer().sendMessage(mines.toString());
		}
	}
	
	public static void RemoveCheck(ArrayList<Entity> list)
	{
		for(Entity e : list)
		{
			if(e.isDead() || e == null)
			{
				list.remove(e);
				break;
			}
		}
	}
	
	@EventHandler
	public void dmg(EntityDamageByEntityEvent event)
	{
		RemoveCheck(ZombieSpawner.nodmg);
		RemoveCheck(ZombieSpawner.poison);
		
		if(ZombieSpawner.nodmg.contains(event.getDamager()))
		{
			event.setDamage(0);
		}
		
		if(ZombieSpawner.poison.contains(event.getDamager()))
		{
			if(event.getEntity() instanceof Player)
			{
				((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
			}
		}
	}
	
	@EventHandler
	public void entityDeath(EntityDeathEvent event)
	{
		if(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
		    EntityDamageByEntityEvent edet = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
		    if(edet.getDamager().getType() == EntityType.PLAYER)
		    {
		    	Player player = (Player)edet.getDamager();
		    	if(event.getEntity().getType() == EntityType.ZOMBIE)
		    	{
		    		if(event.getEntity().getCustomName().contains("Loot"))
		    		{
		    			Material mat = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		    			
		    			Missions.GenLoot(event.getEntity().getLocation());
		    			
		    			event.getEntity().getLocation().getBlock().breakNaturally();
		    			event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).setType(mat);
		    			
		    			Random rand = new Random();
		    			
		    			for(Entity e : event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 2f, 2f, 2f))
		    			{
		    				if(e instanceof Item)
		    				{
		    					if(rand.nextInt(101) < 74)
		    					{
		    						e.remove();
		    					}
		    					
		    					if(((Item)e).getItemStack().getType() == Material.CHEST)
		    					{
		    						e.remove();
		    					}
		    				}
		    			}
		    		}
		    	}
		    	
		    	if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE)
		    	{
		    		if(GetScoreboardVal(player, "crowbarkill") < 30)
		    		{
		    			AddScoreboardVal(player, "crowbarkill", 1);
		    			
		    			if(GetScoreboardVal(player, "crowbarkill") >= 30)
		    			{
		    				if(GetScoreboardVal(player, "crowbarglass") >= 80)
		    				{
		    					Main.GrantAdvance(player, "gordon");
		    				}
		    			}
		    		}else {
		    			if(GetScoreboardVal(player, "crowbarglass") >= 80)
	    				{
	    					Main.GrantAdvance(player, "gordon");
	    				}
		    		}
		    		
		    		for(Player playert : Bukkit.getOnlinePlayers())
		    		{
		    			playert.sendMessage(ChatColor.RED + player.getName() + " totally destroyed " + event.getEntity().getName());
		    		}
		    	}
		    }
		    
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() != null)
		{
			for(Map.Entry<Block, Timer> data : MenuManager.signCheck.entrySet())
			{
				if(event.getClickedBlock().getLocation().distance(data.getKey().getLocation()) < 0.5f)
				{
					data.getValue().setRepeats(false);
					data.getValue().start();
				}
			}
		}
		
		try
		{
		if(event.getItem().getType() == Material.EMERALD)
		{
			if(event.getItem().getAmount() > 1)
			{
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "METAL POWERS ACTIVATED");
				player.setMaxHealth(40);
				player.setHealth(40);
				player.playSound(player.getLocation(), Sound.MUSIC_DISC_PIGSTEP, 0.5f, 1);
				
				Timer timer = new Timer(60 * 1000, e -> {
					player.setMaxHealth(20);
				});
				
				
				timer.setRepeats(false);
				timer.start();
				
				event.getItem().setAmount(event.getItem().getAmount() - 1);
			}
		}
		if(event.getItem().getType() == Material.SCUTE)
		{
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				OneOrAll = !OneOrAll;
				if(OneOrAll)
				{
					event.getPlayer().sendMessage(ChatColor.YELLOW + "Set to explode one at a time.");
				}else {
					event.getPlayer().sendMessage(ChatColor.YELLOW + "Set to explode all at once.");
				}
			}else {
				if(OneOrAll)
				{
					for(Location mine : Charges)
					{
						mine.getBlock().setType(Material.ACTIVATOR_RAIL);
						mine.getWorld().createExplosion(mine, 3f, false, false);
						Charges.remove(mine);
					}
				}else {
					for(Location mine : Charges)
					{
						mine.getBlock().setType(Material.ACTIVATOR_RAIL);
						mine.getWorld().createExplosion(mine, 3f, false, false);
					}
					Charges.clear();
				}
			}

		}
		if(event.getItem().getType() == Material.YELLOW_DYE)
		{
			if(event.getItem().getAmount() > 1)
			{
				Player player = event.getPlayer();
				TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(100);
				tnt.setVelocity(player.getEyeLocation().getDirection());
				tnt.setSource(player);
				event.getItem().setAmount(event.getItem().getAmount() - 1);
				if(GetScoreboardVal(player, "grenadethrow") < 10)
				{
					AddScoreboardVal(player, "grenadethrow", 1);
					
					if(GetScoreboardVal(player, "grenadethrow") >= 10)
					{
						Main.GrantAdvance(player, "throw10gren");
					}
				}
			}
		}
		
		if(event.getItem().getType() == Material.ORANGE_DYE)
		{
			if(event.getItem().getAmount() > 1)
			{
				Player player = event.getPlayer();
				TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(100);
				tnt.setVelocity(player.getEyeLocation().getDirection());
				tnt.setSource(player);
				tnt.setGlowing(true);
				tnt.setSilent(false);
				event.getItem().setAmount(event.getItem().getAmount() - 1);
				if(GetScoreboardVal(player, "grenadethrow") < 10)
				{
					AddScoreboardVal(player, "grenadethrow", 1);
					
					if(GetScoreboardVal(player, "grenadethrow") >= 10)
					{
						Main.GrantAdvance(player, "throw10gren");
					}
				}
			}
		}
		
		if(event.getItem().getType() == Material.BLUE_DYE)
		{
			if(event.getItem().getAmount() > 1)
			{
				Player player = event.getPlayer();
				TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(100);
				tnt.setVelocity(player.getEyeLocation().getDirection());
				tnt.setSource(player);
				tnt.setGlowing(true);
				tnt.setSilent(true);
				event.getItem().setAmount(event.getItem().getAmount() - 1);
				if(GetScoreboardVal(player, "grenadethrow") < 10)
				{
					AddScoreboardVal(player, "grenadethrow", 1);
					
					if(GetScoreboardVal(player, "grenadethrow") >= 10)
					{
						Main.GrantAdvance(player, "throw10gren");
					}
				}
			}
		}
		
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getItem().getType() == Material.GOLDEN_AXE)
			{
				if(event.getClickedBlock().getType() == Material.CHEST)
				{
					event.getClickedBlock().breakNaturally();
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);
				}
				
					Block block = event.getClickedBlock();
					 if(block.getType() == Material.PINK_STAINED_GLASS_PANE || block.getType() == Material.ORANGE_STAINED_GLASS_PANE || block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE ||  block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE || block.getType() == Material.BLUE_STAINED_GLASS_PANE ||  block.getType() == Material.BLACK_STAINED_GLASS_PANE || block.getType() == Material.WHITE_STAINED_GLASS_PANE || block.getType() == Material.LIGHT_GRAY_STAINED_GLASS)
		             {
		                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
		                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
						block.breakNaturally();	
						
						if(GetScoreboardVal(event.getPlayer(), "crowbarglass") < 80)
						{
							AddScoreboardVal(event.getPlayer(), "crowbarglass", 1);
							
							if(GetScoreboardVal(event.getPlayer(), "crowbarglass") == 20)
							{
								Main.GrantAdvance(event.getPlayer(), "crowbarsmash");
							}
							
							if(GetScoreboardVal(event.getPlayer(), "crowbarglass") == 80)
							{
								Main.GrantAdvance(event.getPlayer(), "crowbarsmash2");
							}
						}
						
		             }
					 if(block.getType() == Material.OAK_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
		             {
		                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle block oak_leaves " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
		                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, 1, 1);
						block.breakNaturally();	
		             }
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getItem().getType() == Material.STONE_SHOVEL || event.getItem().getType() == Material.WOODEN_HOE || event.getItem().getType() == Material.GOLDEN_HOE || event.getItem().getType() == Material.STONE_HOE || event.getItem().getType() == Material.IRON_HOE || event.getItem().getType() == Material.DIAMOND_HOE || event.getItem().getType() == Material.NETHERITE_HOE))
		{
			if(event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR)
			{
				event.setCancelled(true);
			} 
		}
		
		//if(event.getAction() == Action.LEFT_CLICK_AIR)
		//{
		 final Player player = event.getPlayer();
         Random rand = new Random();
		 if(playerdata.get(players.indexOf(player)).AkReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD){
	            	 
	            	 if(player.getInventory().contains(Material.GOLD_NUGGET))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
		            		removeItem(player, Material.GOLD_NUGGET);
			 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
			 				data.AkShots++;
			 				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
		            		ItemMeta meta = shell.getItemMeta();
		            		meta.setDisplayName(ChatColor.WHITE + "Shell");
		            		shell.setItemMeta(meta);
		            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
		            		item.setPickupDelay(200);
			 				if(data.AkShots >= 3)
			 				{
			 					data.AkReload = 5;
			 					data.AkShots = 0;
			 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
				 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 5 levels");
				                
			 				}
			 				playerdata.set(players.indexOf(player), data);
			                Shoot(player, 9, 20, Color.GRAY, 0.9f, 1.6f, 1.4f);    
			                
			                if(GetScoreboardVal(player, "akshot") < 1)
			                {
				                SetScoreboardVal(player, "akshot", 1);
				                Main.GrantAdvance(player, "ak47");
			                }
			                
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
			             }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
	            	 }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).ARReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE){
	            	 if(player.getInventory().contains(Material.GOLD_NUGGET))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
			            removeItem(player, Material.GOLD_NUGGET);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.ARShots++;
		 				data.ARReload = 2;
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		 				ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				if(data.ARShots >= 15)
		 				{
		 					data.ARReload = 7;
		 					data.ARShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 7 levels");
		            		
		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 13, 25, Color.GRAY, 0.69f, 2f, 1.3f, false, false, "arpewshot", "ar");    
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).AkReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL){
	            	 
	            	 if(player.getInventory().contains(Material.GOLD_NUGGET))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            			
		            		removeItem(player, Material.GOLD_NUGGET);
			 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
			 				data.AkShots++;
			 				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
		            		ItemMeta meta = shell.getItemMeta();
		            		meta.setDisplayName(ChatColor.WHITE + "Shell");
		            		shell.setItemMeta(meta);
		            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
		            		item.setPickupDelay(200);
			 				if(data.AkShots >= 3)
			 				{
			 					data.AkReload = 5;
			 					data.AkShots = 0;
			 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
				 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 5 levels");
				                
			 				}
			 				playerdata.set(players.indexOf(player), data);
			                Shoot(player, 9, 55, Color.GRAY, 0.9f, 1.6f, 5f, false, false, "augshot", "aug", 5);    
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
			                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
			             }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
	            	 }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).RPGReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_PICKAXE){
	            	 if(player.getInventory().contains(Material.LAPIS_LAZULI))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 50)
	            		 {
			            removeItem(player, Material.LAPIS_LAZULI);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.RPGReload = 10;
		 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 10 levels");
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
	            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 30, 50, Color.GRAY, 0f, 1.3f, 0f, false, true);
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 100, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 100, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).SMGReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE){
	            	 if(player.getInventory().contains(Material.NETHERITE_SCRAP))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            removeItem(player, Material.NETHERITE_SCRAP);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SMGShots++;
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());

		 				ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				if(data.SMGShots >= 25)
		 				{
		 					data.SMGReload = 10;
		 					data.SMGShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 10 levels");
		            		
		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 5, 9, Color.GRAY, 0.75f, 1.6f, 0.8f);    
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).SMGReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.STONE_SHOVEL){
	            	 if(player.getInventory().contains(Material.NETHERITE_SCRAP))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {

	            			 if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
	            			 {
	            				 if(playerdata.get(players.indexOf(player)).GRENADE < 1)
	            				 {
	 	            				TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
		            				tnt.setFuseTicks(100);
		            				tnt.setVelocity(player.getEyeLocation().getDirection());
				 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 5));
				 					tnt.setSource(player);
				 					PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
					 				data.GRENADE = 15;
					 				playerdata.set(players.indexOf(player), data);
					 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 15 levels");
	            				 }

	            			 }
			            removeItem(player, Material.NETHERITE_SCRAP);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SMGShots++;
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());

		 				ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				if(data.SMGShots >= 25)
		 				{
		 					data.SMGReload = 10;
		 					data.SMGShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 10 levels");
		            		
		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 5, 9, Color.GRAY, 0.75f, 1.6f, 0.8f);    
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).SMGReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE){
	            	 if(player.getInventory().contains(Material.NETHERITE_SCRAP))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 50)
	            		 {
	            			 if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
	            			 {
	            				 if(playerdata.get(players.indexOf(player)).GRENADE < 1)
	            				 {
	 	            				TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
		            				tnt.setFuseTicks(100);
		            				tnt.setVelocity(player.getEyeLocation().getDirection());
				 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 5));
				 					tnt.setSource(player);
				 					PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
					 				data.GRENADE = 15;
					 				playerdata.set(players.indexOf(player), data);
					 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 15 levels");
	            				 }

	            			 }
			            removeItem(player, Material.NETHERITE_SCRAP);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SMGShots++;
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		 				ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				if(data.SMGShots >= 25)
		 				{
		 					data.SMGReload = 10;
		 					data.SMGShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 10 levels");
		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 10, 9, Color.GRAY, 0.75f, 1.6f, 1.5f, false, false, "carbshot", "carbine", 25);    
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).SPAZReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.STONE_PICKAXE){
	            	 if(player.getInventory().contains(Material.IRON_NUGGET))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 40)
	            		 {
	            	 
			            removeItem(player, Material.IRON_NUGGET);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SPAZShots++;
		 				data.SPAZReload = 5;
		 				ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				if(data.SPAZShots >= 7)
		 				{
		 					data.SPAZReload = 13;
		 					data.SPAZShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 13 levels");
			                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		            		
		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 10, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 10, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 10, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 10, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 10, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50); 
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).MiniReload < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.NETHERITE_HOE){
	            	 if(player.getInventory().contains(Material.DIAMOND))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            removeItem(player, Material.DIAMOND);
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.MiniShots++;
		 				 player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
		            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
		            		ItemMeta meta = shell.getItemMeta();
		            		meta.setDisplayName(ChatColor.WHITE + "Shell");
		            		shell.setItemMeta(meta);
		            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
		            		item.setPickupDelay(200);

		 				if(data.MiniShots >= 50)
		 				{
		 					data.MiniReload = 15;
		 					data.MiniShots = 0;
		 					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 15 levels");
			               		 				}
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 5, 15, Color.GRAY, 0.55f, 2.3f, 0.5f, false, false, "minishot", "minigun50zomb", 50);    
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
		         }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).PistolShots < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD){
	            	 if(player.getInventory().contains(Material.NETHERITE_SCRAP))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            removeItem(player, Material.NETHERITE_SCRAP);		
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.PistolShots = 5;
		 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 5 levels");
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
	            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 11, 10, Color.GRAY, 0.95f, 1.6f, 2f, false, false, "pistshot", "pistol");
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
	             }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).ShotgunShots < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.IRON_HOE){
	            	 if(player.getInventory().contains(Material.IRON_NUGGET))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            removeItem(player, Material.IRON_NUGGET);		
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.ShotgunShots = 7;
		 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 7 levels");
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
	            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				playerdata.set(players.indexOf(player), data);
		                Shoot(player, 20, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 20, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 20, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 16, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                Shoot(player, 16, 6, Color.GRAY, 0.2f, 2.5f, 5f, false, false, "shotgunkills", "kill50shotgun", 50);
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
	            		 
	             }
	         }
    	 }
		 if(playerdata.get(players.indexOf(player)).SniperShots < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.STONE_HOE){
	            	 if(player.getInventory().contains(Material.IRON_INGOT))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            		removeItem(player, Material.IRON_INGOT);		
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SniperShots = 10;
		 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 10 levels");
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
	            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				playerdata.set(players.indexOf(player), data);
		 				if(player.isSneaking())
		 				{
			                Shoot(player, 30, 110, Color.GRAY, 0, 2.5f, 8f, false, false, "snipshot", "sniper");
		 				}else {
			                Shoot(player, 25, 110, Color.GRAY, 0.6f, 2.5f, 8f, false, false, "snipshot", "sniper");
		 				}
		 				
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
	             }
	         }
    	 }
		 
		 if(playerdata.get(players.indexOf(player)).SniperShots < 1)
		 {
	         if (player.getInventory().getItemInMainHand() != null){
	             if (player.getInventory().getItemInMainHand().getType() == Material.IRON_SHOVEL){
	            	 if(player.getInventory().contains(Material.IRON_INGOT))
	            	 {
	            		 if(player.getInventory().getItemInMainHand().getDurability() <= 27)
	            		 {
	            	 
			            		removeItem(player, Material.IRON_INGOT);		
		 				PlayerData data = new PlayerData(playerdata.get(players.indexOf(player)));
		 				data.SniperShots = 5;
		 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
			 			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + player.getName() + " 5 levels");
		                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, rand.nextFloat() + rand.nextFloat());
	            		ItemStack shell = new ItemStack(Material.QUARTZ, 1);
	            		ItemMeta meta = shell.getItemMeta();
	            		meta.setDisplayName(ChatColor.WHITE + "Shell");
	            		shell.setItemMeta(meta);
	            		Item item = player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), shell);
	            		item.setPickupDelay(200);
		 				playerdata.set(players.indexOf(player), data);
		 				if(player.isSneaking())
		 				{
			                Shoot(player, 15, 110, Color.GRAY, 0, 2.5f, 8f, false, false, "semishot", "sniper50zombies", 50);
		 				}else {
			                Shoot(player, 13, 110, Color.GRAY, 0.6f, 2.5f, 8f, false, false, "semishot", "sniper50zombies", 50);
		 				}
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
		                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, -5000));
	            		 }else {
				             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
				             player.sendMessage(ChatColor.RED + "That gun is broken!");
			             }
		             }else {
			             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             }
	             }
	         }
    	 }
		 
         if (player.getInventory().getItemInMainHand() != null){
             if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE){	
        		 if(player.getInventory().getItemInMainHand().getDurability() <= 67)
        		 {
             
	 				player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 1));
	                Shoot(player, 10, 10, Color.GRAY, 0f, 1.6f, 0f, true, false, "flameshot", "flamethrower", 40);
	                event.getPlayer().getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 5, rand.nextFloat() + rand.nextFloat());
        		 }else {
		             event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1, rand.nextFloat() + rand.nextFloat());
		             player.sendMessage(ChatColor.RED + "That gun is broken!");
	             }
             }
         }
		}catch(Exception e)
		{
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void SetScoreboardVal(Player p, String objective, int scoreval)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective(objective);
    	
    	if(obj == null)
    	{
    		obj = board.registerNewObjective(objective, "dummy");
    	}
    	
    	Score score = obj.getScore(p);
    	score.setScore(scoreval);
	}
	
	@SuppressWarnings("deprecation")
	public static void AddScoreboardVal(Player p, String objective, int scoreval)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective(objective);
    	
    	if(obj == null)
    	{
    		obj = board.registerNewObjective(objective, "dummy");
    	}
    	
    	Score score = obj.getScore(p);
    	score.setScore(score.getScore() + scoreval);
	}
	
	public static int GetScoreboardVal(Player p, String objective)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective(objective);
    	
    	if(obj == null)
    	{
    		return 0;
    	}
    	
    	@SuppressWarnings("deprecation")
		Score score = obj.getScore(p);
    	return score.getScore();
	}
	
	public static Location activeloc;
	
	@SuppressWarnings("deprecation")
	public void AddKillAdv(Player player)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective("z_kill");
    	
    	if(obj == null)
    	{
    		obj = board.registerNewObjective("z_kill", "dummy");
    	}
    	
    	Score score = obj.getScore(player);
    	score.setScore(score.getScore() + 1);
    	
    	if(score.getScore() == 25)
    	{
    		Main.GrantAdvance(player, "kill25zombies");
    	}
    	
    	if(score.getScore() == 75)
    	{
    		Main.GrantAdvance(player, "kill75zombies");
    	}
    	
    	if(score.getScore() == 150)
    	{
    		Main.GrantAdvance(player, "kill150zombies");
    	}
    	
    	if(score.getScore() == 300)
    	{
    		Main.GrantAdvance(player, "kill300zombies");
    	}
    	
    	if(score.getScore() == 500)
    	{
    		Main.GrantAdvance(player, "kill500zombies");
    	}
    	
    	if(score.getScore() == 1000)
    	{
    		Main.GrantAdvance(player, "kill1000zombies");
    	}
    	
    	if(score.getScore() == 3000)
    	{
    		Main.GrantAdvance(player, "kill3000zombies");
    	}
	}
	
	public void Shoot(final Player player, final double damage, final int MaxLife, final Color particleColor, final float off, final float dist, final float recoil)
	{
		Shoot(player, damage, MaxLife, particleColor, off, dist, recoil, false, false, "", "");
	}
	 
	 public void Shoot(final Player player, final double damage, final int MaxLife, final Color particleColor, final float off, final float dist, final float recoil, final boolean flame, final boolean rpg)
	 {
			Shoot(player, damage, MaxLife, particleColor, off, dist, recoil, flame, rpg, "", "");
	 }
	 
	 public void Shoot(final Player player, final double damage, final int MaxLife, final Color particleColor, final float off, final float dist, final float recoil, final boolean flame, final boolean rpg, String adv, String advtitl)
	 {
			Shoot(player, damage, MaxLife, particleColor, off, dist, recoil, flame, rpg, adv, advtitl, 1);
	 }
	 
	public void Shoot(final Player player, final double damage, final int MaxLife, final Color particleColor, final float off, final float dist, final float recoil, final boolean flame, final boolean rpg, String adv, String advtitl, int advcount)
	{
		new BukkitRunnable(){
        	Random rand = new Random();
            double time = 0;
            Location loc = player.getLocation();
            Vector direction = loc.getDirection().normalize();
			@Override
            public void run() {
            	if(time == 0)
            	{
            		if(!flame)
            		{
	            		Location newloc = player.getLocation();
	            		newloc.setPitch(-recoil + newloc.getPitch());
	            		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " at @s run tp @s ~ ~ ~ ~ " + newloc.getPitch());
            		}
            	
            		if(off != 0)
            		{
                		direction.setX(direction.getX() + ((0.5 - rand.nextFloat())/6/off));
                		direction.setY(direction.getY() + ((0.5 - rand.nextFloat())/6/off));
                		direction.setZ(direction.getZ() + ((0.5 - rand.nextFloat())/6/off));
            		}
            	}
                time += 1;
               
                if(flame || rpg)
                {
                    loc.add(new Vector(direction.getX()/1.5, direction.getY()/1.5, direction.getZ()/1.5));
                }else {
                    loc.add(new Vector(direction.getX()*2, direction.getY()*2, direction.getZ()*2));
                }
                loc.add(0, 1.5, 0);
               
                if(flame)
                {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:flame " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " 0.2 0.2 0.2 0 50 normal");
                }else{
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " 0 0 0 0 15 normal");
                }

                for(Grave b : ZombieSpawner.graveBlocks)
                {
                	if(loc.distance(b.block.getLocation()) < 1.6f)
                	{
                		b.Damage(7);
                	}
                }
                
                for (Entity e : loc.getWorld().getEntities()){
                    if (e.getLocation().distance(loc) < dist){
                        if (e != player && e.getType() != EntityType.ARMOR_STAND){
                            if (e.getType().isAlive()){
                                Damageable d = (Damageable) e;
                                
                                if(d.getHealth() - (damage - (e.getLocation().distance(loc)/1.1) - time/8) <= 0)
                                {
                                	AddKillAdv(player);
                                	
                                	if(adv != "")
                                	{
                                		if(GetScoreboardVal(player, adv) < advcount && GetScoreboardVal(player, adv) != -1)
                                	    {
                                			AddScoreboardVal(player, adv, 1);
                                			
                                			if(GetScoreboardVal(player, adv) == advcount)
                                			{
                                				if(GetScoreboardVal(player, adv) != -1)
                                    	    	{
                                        	        Main.GrantAdvance(player, advtitl);
                                        	        SetScoreboardVal(player, adv, -1);
                                    	    	}
                                			}
                                	    }else {
                                	    	if(GetScoreboardVal(player, adv) != -1)
                                	    	{
                                    	        Main.GrantAdvance(player, advtitl);
                                    	        SetScoreboardVal(player, adv, -1);
                                	    	}
                                	    }
                                	}
                                }
                                
                                d.damage(damage - (e.getLocation().distance(loc)/1.1) - time/8, player);
                                
                                for (int x = -2; x <= 2; x++) {
                                    for (int z = -2; z <= 2; z++) {
                                        for (int y = -2; y <= 2; y++) {
                                            final Material material = e.getLocation().add(x, y, z).getBlock().getType();
                                            final Block block = e.getLocation().add(x, y, z).getBlock();
                                            if (material == Material.ACACIA_LEAVES ) {
                                            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + block.getX() + " " + block.getY() + " " + block.getZ() + " 2.4 2.4 2.4 .01 800 normal @a");
                                            	block.getWorld().createExplosion(block.getLocation(), 4f, false, false);
                                            	block.setType(Material.ACTIVATOR_RAIL);
                                            	if(GetScoreboardVal(player, "barrelexp") < 15)
                                    			{
                                    				AddScoreboardVal(player, "barrelexp", 1);
                                    				
                                    				if(GetScoreboardVal(player, "barrelexp") >= 15)
                                    				{
                                    					Main.GrantAdvance(player, "blow15barrel");
                                    				}
                                    			}
                                            }
                                            if(material == Material.JUNGLE_LEAVES)
                                            {
                                            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + block.getX() + " " + block.getY() + " " + block.getZ() + " 2.4 2.4 2.4 .01 800 normal @a");
                                            	block.getWorld().createExplosion(block.getLocation(), 4f, false, false);
                                            	block.setType(Material.AIR);
                                            	if(GetScoreboardVal(player, "barrelexp") < 15)
                                    			{
                                    				AddScoreboardVal(player, "barrelexp", 1);
                                    				
                                    				if(GetScoreboardVal(player, "barrelexp") >= 15)
                                    				{
                                    					Main.GrantAdvance(player, "blow15barrel");
                                    				}
                                    			}
                                            }
                                        }
                                    }
                                }
                                if(flame)
                                {
                                	d.setFireTicks(100);
                                }
                                if(rpg)
                                {
    								d.getWorld().createExplosion(d.getLocation(), 3f, false, false);
                                }
                                 this.cancel();
                            }
                        }
                    }
                }
        		Block block = loc.getBlock();

                CheckBlocksBullet(block, player);
                CheckBlocksBullet(block.getRelative(BlockFace.UP), player);
                CheckBlocksBullet(block.getRelative(BlockFace.DOWN), player);
                CheckBlocksBullet(block.getRelative(BlockFace.EAST), player);
                CheckBlocksBullet(block.getRelative(BlockFace.SOUTH), player);
                CheckBlocksBullet(block.getRelative(BlockFace.NORTH), player);
                CheckBlocksBullet(block.getRelative(BlockFace.WEST), player);

				if(rand.nextInt(101) > 40)
				{
                //if(block.getLocation().distance(loc) < 0.5f)
                //{
					if(block.getType() == Material.AIR)
					{
						if(block.getRelative(BlockFace.DOWN).getType() != Material.AIR)
						{
							if(flame)
							{
								block.setType(Material.FIRE);
							}
							
							if(rpg)
							{
								block.getWorld().createExplosion(block.getLocation(), 3f, false, false);
							}
						}
					}
                    if(block.getType() == Material.RED_GLAZED_TERRACOTTA)
                    {
						block.setType(Material.YELLOW_GLAZED_TERRACOTTA);	
                    }
					if(block.getType() == Material.BLACK_GLAZED_TERRACOTTA)
                    {
						block.setType(Material.RED_GLAZED_TERRACOTTA);	
                    }
                    if(block.getType() == Material.BLACK_CONCRETE)
                    {
						block.setType(Material.BLACK_GLAZED_TERRACOTTA);	
                    } 
                    
                    if(block.getType() == Material.POLISHED_BLACKSTONE_SLAB)
                    {
						block.setType(Material.POLISHED_GRANITE_SLAB);	
                    }
                    if(block.getType() == Material.POLISHED_DIORITE_SLAB)
                    {
						block.setType(Material.POLISHED_BLACKSTONE_SLAB);	
                    }
                    if(block.getType() == Material.SMOOTH_STONE_SLAB)
                    {
						block.setType(Material.POLISHED_DIORITE_SLAB);	
                    }
                    
                    if(block.getType() == Material.OAK_LEAVES)
                    {
                    	block.setType(Material.AIR);
                    	ItemStack item = new ItemStack(Material.SPIDER_EYE, 3);
                    	ItemMeta meta = item.getItemMeta();
                    	meta.setDisplayName(ChatColor.WHITE + "Leaves");
                    	item.setItemMeta(meta);
                    	block.getWorld().dropItemNaturally(block.getLocation(), item);
                    }
				}
                //}

                
                loc.subtract(0, 1.5, 0);
               
                if (time == MaxLife){
                    this.cancel();
                }
               
            }
           
        }.runTaskTimer(plugin, 0, 1);

	}
	
	public void removeItem(Player p, Material mat)
	{
		for(int i = 0; i < p.getInventory().getSize(); i++){
			  ItemStack itm = p.getInventory().getItem(i);
			  if(itm != null && itm.getType().equals(mat)){
			    int amt = itm.getAmount() - 1;
			    itm.setAmount(amt);
			    p.getInventory().setItem(i, amt > 0 ? itm : null);
			    p.updateInventory();
			    break;
			  }
			}

	}
	
	public void CheckBlocksBullet(Block block, Player p)
	{
		if(block.getType() == Material.CHEST)
		{
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block chest " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
        	block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
			block.breakNaturally();
		}
		
		if(block.getType() == Material.ACACIA_LEAVES)
		{
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + block.getX() + " " + block.getY() + " " + block.getZ() + " 2.4 2.4 2.4 .01 800 normal @a");
        	block.getWorld().createExplosion(block.getLocation(), 4f, false, false);
        	block.setType(Material.ACTIVATOR_RAIL);
		}
		
		if(block.getType() == Material.JUNGLE_LEAVES)
		{
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + block.getX() + " " + block.getY() + " " + block.getZ() + " 2.4 2.4 2.4 .01 800 normal @a");
        	block.getWorld().createExplosion(block.getLocation(), 4f, false, false);
        	block.setType(Material.AIR);
		}
		
		if(block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.JUNGLE_LEAVES)
		{
			if(GetScoreboardVal(p, "barrelexp") < 15)
			{
				AddScoreboardVal(p, "barrelexp", 1);
				
				if(GetScoreboardVal(p, "barrelexp") >= 15)
				{
					Main.GrantAdvance(p, "blow15barrel");
				}
			}
		}

        if(block.getType() == Material.PINK_STAINED_GLASS_PANE || block.getType() == Material.ORANGE_STAINED_GLASS_PANE || block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE ||  block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE || block.getType() == Material.BLUE_STAINED_GLASS_PANE ||  block.getType() == Material.BLACK_STAINED_GLASS_PANE || block.getType() == Material.WHITE_STAINED_GLASS_PANE || block.getType() == Material.LIGHT_GRAY_STAINED_GLASS)
        {
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
        	block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
			block.breakNaturally();	
        }
	}
	
}
