import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class Main extends JavaPlugin {
	
	public static FileConfiguration config;
	
	public static int advance = 0;
	
	public static int resetSound = 0;
	
	public static ArrayList<Location> minesStart;
	public static ArrayList<Location> knivesStart;
	
	public static ArrayList<GarageDoor> garageDoors;
	
	public static ArrayList<Block> removeQueue;
	
	public static int shake = 0;
	
	public static ArrayList<LivingEntity> grenadeZombies;
	
	public static boolean customDeath = false;
	
	public static boolean planeShake = false;
	
	public static String customDeathMessage = "";
	
	public static boolean removeplane = false;
	
	public static int queuedMis = 0;
	
	public static boolean randomShot = false;
	
	public static Main INSTANCE;
	
	int grenadeTick = 0;

	int soundTimer = 60 * 20;
	
	public static boolean queueMissionMenu = false;
	
	public static int LoadRoles = -1;
	
	public static MissionBreif loadMission;
	public static String giveCommandBook;
	
	public static void CustomDeath(int damage, LivingEntity entity, String message)
	{
		if(entity.getHealth() - damage <= 0)
		{
			Main.customDeath = true;
			Main.customDeathMessage = message;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable()
	{
		INSTANCE = this;
		removeQueue = new ArrayList<Block>();
		
		config = this.getConfig();
		
		config.addDefault("Loots", new ArrayList<Location>());
		config.addDefault("StartMines", new ArrayList<Location>());
		config.addDefault("StartKnives", new ArrayList<Location>());
		
		config.options().copyDefaults(true);
		saveConfig();
		
		Events.chests = (ArrayList<Location>) config.getList("Loots");
		
		minesStart = (ArrayList<Location>) config.getList("StartMines");
		
		knivesStart = (ArrayList<Location>) config.getList("StartKnives");
		
		this.saveDefaultConfig();
		
		Events.players = new ArrayList<Player>();
		Events.playerdata = new ArrayList<PlayerData>();
		Events.bulletHoles = new ArrayList<Location>();
		Events.playersWearingPump = new ArrayList<Player>();
		Events.originalHelm = new ArrayList<ItemStack>();
		Events.placedBlocks = new ArrayList<Location>();
		Events.Turrets = new ArrayList<Location>();
		Events.Charges = new ArrayList<Location>();
		Events.AutoCharges = new ArrayList<Location>();
		Events.Healing = new ArrayList<Location>();
		Events.kinvesPlayer = new ArrayList<Player>();
		Events.knives = new ArrayList<Location>();
		grenadeZombies = new ArrayList<LivingEntity>();
		Events.PlayerTurrets = new ArrayList<Player>();
		
		garageDoors = new ArrayList<GarageDoor>();
		ArrayList<Block> TopBlocks = new ArrayList<Block>();
		ArrayList<Block> MiddleBlocks = new ArrayList<Block>();
		ArrayList<Block> BottomBlocks = new ArrayList<Block>();
		
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 81).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 80).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 79).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 78).getBlock());

		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 81).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 80).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 79).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 78).getBlock());

		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 81).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 80).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 79).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 78).getBlock());
		
		garageDoors.add(new GarageDoor(TopBlocks, MiddleBlocks, BottomBlocks));
		
		TopBlocks = new ArrayList<Block>();
		MiddleBlocks = new ArrayList<Block>();
		BottomBlocks = new ArrayList<Block>();
		
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 92).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 91).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 90).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 89).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 6, 88).getBlock());

		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 92).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 91).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 90).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 89).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 5, 88).getBlock());

		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 92).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 91).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 90).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 89).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -111, 4, 88).getBlock());

		garageDoors.add(new GarageDoor(TopBlocks, MiddleBlocks, BottomBlocks));
		
		TopBlocks = new ArrayList<Block>();
		MiddleBlocks = new ArrayList<Block>();
		BottomBlocks = new ArrayList<Block>();
		
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -90, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -89, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -88, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -87, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -86, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -85, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -84, 6, 112).getBlock());
		TopBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -83, 6, 112).getBlock());

		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -90, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -89, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -88, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -87, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -86, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -85, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -84, 5, 112).getBlock());
		MiddleBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -83, 5, 112).getBlock());

		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -90, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -89, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -88, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -87, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -86, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -85, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -84, 4, 112).getBlock());
		BottomBlocks.add(new Location(Bukkit.getServer().getWorlds().get(0), -83, 4, 112).getBlock());

		garageDoors.add(new GarageDoor(TopBlocks, MiddleBlocks, BottomBlocks));
		
		Events events = new Events(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    @SuppressWarnings("deprecation")
			public void run() { 
		    	
		    	if(giveCommandBook != null && giveCommandBook != "")
		    	{
		    		for(Player p : Bukkit.getOnlinePlayers())
		    		{
		    			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		    			BookMeta meta = (BookMeta)book.getItemMeta();
		    			meta.setDisplayName(ChatColor.GOLD + "Mission Brief");
		    			meta.setAuthor("For Agent 00" + (new Random()).nextInt(10));
		    			meta.setGeneration(Generation.ORIGINAL);
		    			meta.setTitle(ChatColor.GOLD + "Mission Brief");
		    			
		    			List<String> str = new ArrayList<String>();
		    			
		    			for(String s : giveCommandBook.split("@"))
		    			{
		    				meta.addPage(s);
		    			}
		    					    					    			
		    			book.setItemMeta(meta);
		    			
		    			p.getInventory().addItem(book);
		    		}
		    		
		    		giveCommandBook = null;
		    	
		    	}
		    	
		    	if(LoadRoles != -1)
		    	{
		    		MenuManager.LoadRoleMenu(LoadRoles);
		    		LoadRoles = -1;
		    	}
		    	
		    	if(queuedMis != 0)
		    	{
		    		ZombieManager.LoadByInt(queuedMis);
		    		queuedMis = 0;
		    	}
		    	
		    	if(loadMission != null)
		    	{
		    		MenuManager.LoadMissionBreif(loadMission.difficulty, loadMission.deathLim, loadMission.duration, loadMission.command, loadMission.mis);
		    		loadMission = null;
		    	}
		    	
		    	if(queueMissionMenu)
		    	{
		    		MenuManager.LoadMissionMenu();
		    		queueMissionMenu = false;
		    	}
		    	
		    	if(removeplane)
		    	{
					Location loc = new Location(Bukkit.getWorlds().get(0), -115, 5, -139);

					loc.getBlock().setType(Material.AIR);
					loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
					loc.getBlock().getRelative(BlockFace.SOUTH).setType(Material.AIR);
					loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).setType(Material.AIR);
					loc.getBlock().getRelative(BlockFace.NORTH).setType(Material.AIR);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 50);
					
					removeplane = false;
		    	}
		    	
		    	if(randomShot == true)
		    	{
		    		if((new Random()).nextInt(101) < 35)
		    		{
		    			for(Player p2 : Bukkit.getOnlinePlayers())
						{
							Random rand = new Random();
							Location loc = new Location(p2.getLocation().getWorld(), p2.getLocation().getBlockX() + (rand.nextInt(11) - 5), p2.getLocation().getBlockY(), p2.getLocation().getBlockZ() + (rand.nextInt(11) - 5));
							p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						}
		    		}
		    	}
		    	
		    	if(planeShake == true)
		    	{
		    		shake++;
		    		for(Player p : Bukkit.getOnlinePlayers())
	    			{
		    			p.playSound(p.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 1f, 1f);
			    		if(shake % 2 == 0)
			    		{
			    			
		            		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "execute as " + p.getName() + " at @s run tp @s ~ ~ " + (p.getLocation().getZ() + 0.1f) + " ~ ~");
			    		}
			    		
			    		if(shake + 1 % 2 == 0)
			    		{
		            		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "execute as " + p.getName() + " at @s run tp @s ~ ~ " + (p.getLocation().getZ() - 0.1f) + " ~ ~");
			    		}
	    			}
		    		
		    		if(shake > 15)
		    		{
		    			shake = 0;
		    			planeShake = false;
		    		}
		    	}
		    	
		    	ZombieSpawner.ZombieTick();
		    	
		    	Missions.MissionTick();
		    	
		    	Random rand = new Random();
		    	
		    	soundTimer++;
		    	
	    		for(Player player : Bukkit.getOnlinePlayers())
	    		{
		    	if(soundTimer > ((60 * 20)/4))
		    	{
		    		player.stopSound(Sound.AMBIENT_BASALT_DELTAS_LOOP);
		    		player.stopSound(Sound.AMBIENT_NETHER_WASTES_LOOP);
		    		player.stopSound(Sound.AMBIENT_CRIMSON_FOREST_LOOP);
		    		player.stopSound(Sound.AMBIENT_WARPED_FOREST_LOOP);
		    		soundTimer = 0;
		    		if(rand.nextBoolean())
		    		{
		    			if(rand.nextBoolean())
		    			{
			    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_WARPED_FOREST_LOOP, 0.75f, 1);
		    			}else {
			    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_CRIMSON_FOREST_LOOP, 0.75f, 1);
		    			}
		    		}else{
		    			if(rand.nextBoolean())
		    			{
			    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_NETHER_WASTES_LOOP, 0.75f, 1);
		    			}else {
			    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_BASALT_DELTAS_LOOP, 0.75f, 1);
		    			}
		    		}
		    	}
		    	
		    	if(rand.nextInt(101) < 5)
		    	{
		    			
		    			int random = rand.nextInt(30);
		    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.85f, 1);

		    			if(player.getPlayerListName().contains("In: Wilderness") || player.getPlayerListName().contains("In: National Park"))
		    			{
			    			if(random < 5)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_WARPED_FOREST_ADDITIONS, 0.85f, 1);
			    			}else if(random < 10)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 0.85f, 1);
			    			}else if(random < 15)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_WARPED_FOREST_MOOD, 0.85f, 1);
			    			}else if(random < 20)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_NETHER_WASTES_ADDITIONS, 0.85f, 1);
			    			}else if(random < 25)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.85f, 1);
			    			}else if(random < 30)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_CRIMSON_FOREST_MOOD, 0.85f, 1);
			    			}
		    			}else {
			    			if(random < 5)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.85f, 1);
			    			}else if(random < 10)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 0.85f, 1);
			    			}else if(random < 15)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_WARPED_FOREST_ADDITIONS, 0.85f, 1);
			    			}else if(random < 20)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_NETHER_WASTES_ADDITIONS, 0.85f, 1);
			    			}else if(random < 25)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.85f, 1);
			    			}else if(random < 30)
			    			{
				    			player.playSound(player.getLocation().add(rand.nextInt(15) - 7.5f, 0, rand.nextInt(15) - 7.5f), Sound.AMBIENT_CRIMSON_FOREST_MOOD, 0.85f, 1);
			    			}
		    			}
		    		}
	    		
		    	}
		    	
		    	int ZombieCount = 0;
		    	for(LivingEntity entity : Bukkit.getWorlds().get(0).getLivingEntities())
		    	{
		    		if(entity.getType() == EntityType.ZOMBIE)
		    		{
		    			if(ZombieCount > 4000)
		    			{
		    				entity.remove();
		    			}else {
		    				ZombieCount++;
		    			}
		    		}
		    	}
		    	
		    	if(removeQueue.size() > 0)
		    	{
		    		removeQueue.get(0).setType(Material.AIR);
		    		removeQueue.remove(0);
		    	}
		    	
		    	grenadeTick++;
		    	if(grenadeTick == 10)
		    	{
		    		grenadeTick = 0;
		    		ArrayList<Integer> remove = new ArrayList<Integer>();
			    	for(LivingEntity zombie : grenadeZombies)
			    	{
			    			if(zombie == null || zombie.isDead())
			    			{
			    				remove.add(grenadeZombies.indexOf(zombie));
			    			}else {
			    				for(Entity entity : zombie.getNearbyEntities(13, 13, 13))
			    				{
			    					if(entity.getType() == EntityType.PLAYER)
			    					{
			    						TNTPrimed tnt = (TNTPrimed)zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.PRIMED_TNT);
			    						tnt.setFuseTicks(100);
			    						tnt.setVelocity(((LivingEntity)zombie).getEyeLocation().getDirection());
			    						tnt.setSource(zombie);
			    					}
			    				}
			    			}
			    	}
			    	for(Integer num : remove)
			    	{
			    		grenadeZombies.remove(num.intValue());
			    	}
		    	}
		    	
		    	for(Location heal : Events.Healing)
		    	{
		    		for(Entity entity : heal.getWorld().getNearbyEntities(heal, 2, 10, 2))
		    		{
		    			if(entity instanceof LivingEntity)
		    			{
		    				LivingEntity le = (LivingEntity)entity;
		    				le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 0, false, false, false));
		    				if(le.getHealth() < 20 && le.getHealth() > 1)
		    				{
		    						if(le.getType() == EntityType.PLAYER)
			    					{
					    				le.setHealth(le.getHealth() + 1);
			    					}else {
					    				le.setHealth(le.getHealth() + 4.6);
			    					}
		    				}
		    			}
		    		}
		    	}
		    	
		    	for(Location knife : Events.knives)
		    	{
		    		for(Entity entity : knife.getWorld().getNearbyEntities(knife, 0.4, 0.4, 0.4))
		    		{
		    			if(entity.getType() != EntityType.ARMOR_STAND)
		    			{
		    				if(entity instanceof LivingEntity)
		    				{
		    					LivingEntity le = (LivingEntity)entity;
		    					le.setVelocity(new Vector(0, 1, 0));
		    					if(entity.getType() == EntityType.PLAYER)
		    					{
		    						if(le.getHealth() - 8 <= 0)
		    						{
				    					if(Events.GetScoreboardVal((Player)le, "knifedeaths") < 6)
				    					{
				    						Events.AddScoreboardVal((Player)le, "knifedeaths", 1);
				    						
				    						if(Events.GetScoreboardVal((Player)le, "knifedeaths") >= 6)
				    						{
				    							GrantAdvance((Player)le, "dieknife6");
				    						}
				    					}
		    							
		    							if(Events.kinvesPlayer.get(Events.knives.indexOf(knife)) == null)
		    							{
			    							Main.customDeath = true;
			    							Main.customDeathMessage = le.getName() + " got knifed";
		    							}else {
			    							Main.customDeath = true;
			    							Main.customDeathMessage = le.getName() + " got knifed by " + Events.kinvesPlayer.get(Events.knives.indexOf(knife)).getName();
		    							}

		    						}
		    					}
		    					
		    					
		    					le.damage(8, Events.kinvesPlayer.get(Events.knives.indexOf(knife)));

		    					le.getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
		    					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block detector_rail " + entity.getLocation().getX() + " " + (entity.getLocation().getY() + 1) + " " + entity.getLocation().getZ() + " .1 .1 .1 1 " + Math.toIntExact(Math.round(25 * 200)) + " normal");
		    				}
		    			}
		    		}
		    	}
		    	
		    	for(Location knife : Events.playerKnives)
		    	{
		    		for(Entity entity : knife.getWorld().getNearbyEntities(knife, 0.4, 0.4, 0.4))
		    		{
		    			if(entity.getType() == EntityType.PLAYER)
		    			{
		    				if(entity instanceof LivingEntity)
		    				{
		    					LivingEntity le = (LivingEntity)entity;
		    					le.setVelocity(new Vector(0, 1, 0));
		    					
		    					le.damage(8);

		    					le.getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
		    					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block detector_rail " + entity.getLocation().getX() + " " + (entity.getLocation().getY() + 1) + " " + entity.getLocation().getZ() + " .1 .1 .1 1 " + Math.toIntExact(Math.round(25 * 200)) + " normal");
		    				}
		    			}
		    		}
		    	}
		    	
		    	for(Location autocharge : Events.AutoCharges)
		    	{
		    		for(Entity entity : autocharge.getWorld().getNearbyEntities(autocharge, 1, 1, 1))
		    		{
		    			if(entity.getType() != EntityType.ARMOR_STAND)
		    			{
		    				if(entity.getType() == EntityType.PLAYER)
		    				{
		    					if(((Player)entity).getGameMode() == GameMode.SURVIVAL)
		    					{
			    					CustomDeath(20, (LivingEntity)entity, entity.getName() + " stepped on a mine!");
		    					}
		    				}
		    				autocharge.getBlock().setType(Material.ACTIVATOR_RAIL);
		    				autocharge.getWorld().createExplosion(autocharge, 3.5f, false, false);
		    				
		    				for(int x = -4; x < 4; x++)
		    				{
			    				for(int y = -4; y < 4; y++)
			    				{
				    				for(int z = -4; z < 4; z++)
				    				{
				    					if(((Player)entity).getGameMode() == GameMode.SURVIVAL)
				    					{
				    						Block block = new Location(autocharge.getWorld(), autocharge.getBlockX() + x, autocharge.getBlockY() + y, autocharge.getBlockZ() + z).getBlock();
					    					
					    					if(block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.JUNGLE_LEAVES)
					    					{
					    						if(entity.getType() == EntityType.PLAYER)
					    						{
					    							if(Events.GetScoreboardVal((Player)entity, "barrelexpm") < 15)
						    						{
					    								Events.AddScoreboardVal((Player)entity, "barrelexpm", 1);
						    							
						    							if(Events.GetScoreboardVal((Player)entity, "barrelexpm") >= 15)
						    							{
						    								GrantAdvance((Player)entity, "barrel15landmine");
						    							}
						    						}
					    						}
					    						
					    						
				                            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:smoke " + block.getX() + " " + block.getY() + " " + block.getZ() + " 2.4 2.4 2.4 .01 800 normal @a");
				                            	block.getWorld().createExplosion(block.getLocation(), 4f, false, false);
				                            	if(block.getType() == Material.ACACIA_LEAVES)
				                            	{
				                            		block.setType(Material.ACTIVATOR_RAIL);
				                            	}else {
				                            		block.setType(Material.AIR);
				                            	}
					    					}
				    					}
				    				}
			    				}
		    				}
		    				
		    				Events.AutoCharges.remove(autocharge);
		    			}
		    		}
		    	}
		    	
		    	for(Location turret : Events.Turrets)
		    	{
		    		for(Entity entity : turret.getWorld().getNearbyEntities(turret, 10, 50, 10))
		    		{
		    			if(entity instanceof LivingEntity)
		    			{
		    				if(entity.getType() != EntityType.ARMOR_STAND)
		    				{
		    					Player owner = Events.PlayerTurrets.get(Events.Turrets.indexOf(turret));
		    					if(owner != null)
		    					{			    					
			    					if(entity.getType() == EntityType.PLAYER)
			    					{
			    						CustomDeath(1, (LivingEntity)entity, entity.getName() + " was killed by " + owner.getName() + "'s turret");
			    					}
			    					
			    					((LivingEntity) entity).damage(1, owner);
			    					
		    					}else {
			    					if(entity.getType() == EntityType.PLAYER)
			    					{
			    						CustomDeath(1, (LivingEntity)entity, entity.getName() + " was killed by a turret.");
			    					}
			    					((LivingEntity) entity).damage(1);
		    					}
		    					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block redstone_block " + entity.getLocation().getX() + " " + (entity.getLocation().getY() + 1) + " " + entity.getLocation().getZ() + " .1 .1 .1 1 " + "100" + " normal");
		    					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block detector_rail " + entity.getLocation().getX() + " " + (entity.getLocation().getY() + 1) + " " + entity.getLocation().getZ() + " .1 .1 .1 1 " + "100" + " normal");
		    					Block block = entity.getLocation().getBlock();
		    					if(block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_DIORITE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_GRANITE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.POLISHED_BLACKSTONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.STONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.SMOOTH_STONE_SLAB && block.getRelative(BlockFace.DOWN).getType() != Material.STONE_BRICK_SLAB)
		    					{
		    						if(rand.nextInt(101) < 30) {return;}
		    						if(rand.nextBoolean())
		    						{
		    							block.setType(Material.DETECTOR_RAIL);
		    						}else {
		    							block.setType(Material.POWERED_RAIL);
		    						}
		    					}
		    				}
		    			}
		    		}
		    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:ash " + turret.getX() + " " + (turret.getY() + 1) + " " + turret.getZ() + " 5 0 5 .01 1000 normal");
		    	}
		    	
		    	if(advance == 1)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
					}

		    	}
		    	
		    	if(advance == 2)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
	
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.RED + "Durable Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 2);
			    			le.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	if(advance == 3)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {

			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.RED + "Durable Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 2);
			    			le.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	
		    	if(advance == 4)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
	
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.RED + "Strong Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
			    			le.getEquipment().setHelmet(new ItemStack(Material.YELLOW_BANNER));
			    			le.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	
		    	if(advance == 5)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
	
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Metal Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
			    			le.setHealth(le.getHealth() * 2f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	
		    	if(advance == 6 || advance == 7)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
	
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Metal Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
			    			le.setHealth(le.getHealth() * 2.5f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	if(advance == 6 || advance == 7)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
	
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Scientist Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 2.5f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 3));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	
		    	if(advance == 7)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
		    			
			    		Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GREEN + "Poison Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 2.5f);
			    			le.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
			    		}
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
		    		}
		    	}
		    	
		    	if(advance == 8)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
					}

		    	}
		    	
		    	if(advance == 9)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon minecraft:zombie 0 200 0"); 
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
					}

		    	}
		    	
		    	if(advance == 10)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
		    			Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Scientist Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100f);
			    			le.setHealth(le.getHealth() * 2.5f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 3));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
			    		}
			    		e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Metal Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
			    			le.setHealth(le.getHealth() * 2f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
			    		}
			    		
			    		e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GOLD + "Grenade Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			grenadeZombies.add(le);
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
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
					}

		    	}
		    	
		    	if(advance == 11 || advance == 12)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
		    			Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GOLD + "Super Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 3f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 5));
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000 * 20, 1));
			    		}
			    		
			    		e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GOLD + "Grenade Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			grenadeZombies.add(le);
			    			le.setMaxHealth(100);
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
			    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
					}

		    	}
		    	
		    	if(advance == 12)
		    	{
		    		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
		    			Entity e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Scientist Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			
			    			le.setMaxHealth(100);
			    			le.setHealth(le.getHealth() * 2.5f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 3));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
			    		}
			    		e = Bukkit.getWorld("map").spawnEntity(new Location(Bukkit.getWorld("map"), 0, 200, 0), EntityType.ZOMBIE);
			    		e.setCustomName(ChatColor.GRAY + "Metal Zombie");
			    		e.setCustomNameVisible(true);
			    		if(e instanceof LivingEntity)
			    		{
			    			LivingEntity le = (LivingEntity)e;
			    			le.setMaxHealth(100);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000 * 20, 1));
			    			le.setHealth(le.getHealth() * 2f);
			    			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 1));
			    			le.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			    			le.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			    			le.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			    			le.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
			    		}
		    		}
		    	}
		    	
		    	saveConfig();
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(!Events.players.contains(player))
					{
						Events.players.add(player);
						Events.playerdata.add(new PlayerData());
					}
					if(Events.playerdata.get(Events.players.indexOf(player)).GRENADE > 0 || Events.playerdata.get(Events.players.indexOf(player)).SPAZReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).RPGReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).MiniReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).ShotgunShots > 0 || Events.playerdata.get(Events.players.indexOf(player)).ARReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).SniperShots > 0 || Events.playerdata.get(Events.players.indexOf(player)).SMGReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).AkReload > 0 || Events.playerdata.get(Events.players.indexOf(player)).PistolShots > 0)
					{
						if(Events.playerdata.get(Events.players.indexOf(player)).SniperShots > 0)
						{
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 2));
						}else if(Events.playerdata.get(Events.players.indexOf(player)).PistolShots > 0)
						{

						}else if(Events.playerdata.get(Events.players.indexOf(player)).SMGReload > 0)
						{
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 2));
						}else if(Events.playerdata.get(Events.players.indexOf(player)).ShotgunShots > 0)
						{
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 2));
						}else {
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 1));
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp add " + player.getName() + " -1 levels");
						PlayerData data = new PlayerData(Events.playerdata.get(Events.players.indexOf(player)));
						data.AkReload--;
						data.PistolShots--;
						data.SMGReload--;
						data.ARReload--;
						data.SniperShots--;
						data.ShotgunShots--;
						data.MiniReload--;
						data.RPGReload--;
						data.SPAZReload--;
						data.GRENADE--;
		                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.1f, 1);
						Events.playerdata.set(Events.players.indexOf(player), data);
					}

				}
				
				for(Entity e : Bukkit.getServer().getWorlds().get(0).getEntities())
				{
					if(e.getType() == EntityType.PRIMED_TNT)
					{
						TNTPrimed tnt = (TNTPrimed)e;
						
						
						
						if(tnt.isGlowing())
						{
	                                	if(tnt.isOnGround())
	                                	{
	        								tnt.setFuseTicks(0);
	                                	}
	                                
						}
						
					}
					
					if(e.getType() == EntityType.ARMOR_STAND)
					{
						ArmorStand as = (ArmorStand)e;
						if(as.isInvisible())
						{
							if(as.getCustomName() != "" && as.getCustomName() != null)
							{
								for(Entity entity : as.getNearbyEntities(5, 255, 5))
								{
									if(entity.getType() == EntityType.PLAYER)
									{
										Player player = ((Player)entity);
										player.setCustomName(player.getName() + ChatColor.GRAY + " [In: " + as.getCustomName() + "]");
										player.setDisplayName(player.getName() + ChatColor.GRAY + " [In: " + as.getCustomName() + "]");
										player.setPlayerListName(player.getName() + ChatColor.GRAY + " [In: " + as.getCustomName() + "]");
									}
								}
							}
						}
					}
				}
				
				for(LivingEntity e : Bukkit.getServer().getWorlds().get(0).getLivingEntities())
				{
					if(e.getType() == EntityType.PLAYER)
					{
						if(e.getLocation().getBlock().getType() == Material.WATER)
						{
							e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2, false, false, false));
						}
					}
					
					if(e.getType() == EntityType.ZOMBIE)
					{
						for(Entity entity : e.getNearbyEntities(6, 6, 6))
						{
							if(entity.getType() == EntityType.PLAYER)
							{
								if(entity instanceof LivingEntity)
								{
									Zombie z = ((Zombie) e);
									z.setTarget((LivingEntity)entity);
								}
							}
						}
						
						Events.RemoveCheck(ZombieSpawner.follow);
						
						if(ZombieSpawner.follow.contains(e))
						{
							for(Entity entity : e.getNearbyEntities(100, 100, 100))
							{
								if(entity.getType() == EntityType.PLAYER)
								{
									if(entity instanceof LivingEntity)
									{
										Zombie z = ((Zombie) e);
										z.setTarget((LivingEntity)entity);
										break;
									}
								}
							}
						}
						
						Events.RemoveCheck(ZombieSpawner.grave);
						
						if(rand.nextInt(251) < 3)
						{
							for(Grave g : ZombieSpawner.graveBlocks)
							{
								if(g == null || g.block.getType() != Material.WARPED_PRESSURE_PLATE)
								{
									ZombieSpawner.graveBlocks.remove(g);
									break;
								}else {
									g.block.getWorld().spawnEntity(g.block.getLocation(), EntityType.ZOMBIE);
								}
							}
						}
						
						if(ZombieSpawner.grave.contains(e))
						{
							if(rand.nextInt(101) < 2)
							{
								if(ZombieSpawner.graveBlocks.size() < 40)
								{
									Location loc = new Location(e.getLocation().getWorld(), e.getLocation().getX() + (rand.nextInt(3) - 1), 1, e.getLocation().getZ() + (rand.nextInt(3) - 1));
									
									loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
									
									if(loc.getBlock().getType() == Material.AIR)
									{
										loc.getBlock().setType(Material.WARPED_PRESSURE_PLATE);
										
										ArmorStand stand = (ArmorStand)e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
										
										stand.setCustomName(ChatColor.GREEN + "Grave " + ChatColor.WHITE + "-" + ChatColor.RED + " 100 HP");
										stand.setCustomNameVisible(true);
										stand.setInvisible(true);
										stand.setGravity(false);
										stand.setInvulnerable(true);
										stand.setSmall(true);
										
										ZombieSpawner.graveBlocks.add(new Grave(stand, loc.getBlock()));
									}
									
									ZombieSpawner.Graves++;
								}
							}
						}
						
						Events.RemoveCheck(ZombieSpawner.knife);
						
						if(ZombieSpawner.knife.contains(e))
						{
							if(rand.nextInt(101) < 2)
							{
								if(ZombieSpawner.knifeBlocks.size() < 40)
								{
									Location loc = new Location(e.getLocation().getWorld(), e.getLocation().getX() + (rand.nextInt(3) - 1), 1, e.getLocation().getZ() + (rand.nextInt(3) - 1));
									
									loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
									
									if(loc.getBlock().getType() == Material.AIR)
									{
										loc.getBlock().setType(Material.CRIMSON_PRESSURE_PLATE);
										
										ArmorStand stand = (ArmorStand)e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
										
										stand.setCustomName(ChatColor.GREEN + "Zombie Knife " + ChatColor.WHITE + "-" + ChatColor.RED + " 20s");
										stand.setCustomNameVisible(true);
										stand.setInvisible(true);
										stand.setGravity(false);
										stand.setInvulnerable(true);
										stand.setSmall(true);
										
										stand.teleport(stand.getLocation().clone().add(0, 0.1, 0));
										
										ZombieSpawner.knifeBlocks.add(loc.getBlock());
										Events.playerKnives.add(loc);
									}
								}
							}
						}
						
						Events.RemoveCheck(ZombieSpawner.mine);
						
						if(ZombieSpawner.mine.contains(e))
						{
							if(rand.nextInt(101) < 2)
							{
								if(ZombieSpawner.mineBlocks.size() < 40)
								{
									Location loc = new Location(e.getLocation().getWorld(), e.getLocation().getX() + (rand.nextInt(3) - 1), 1, e.getLocation().getZ() + (rand.nextInt(3) - 1));
									
									loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
									
									if(loc.getBlock().getType() == Material.AIR)
									{
										loc.getBlock().setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
										
										ArmorStand stand = (ArmorStand)e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
										
										stand.setCustomName(ChatColor.GREEN + "Zombie Charge");
										stand.setCustomNameVisible(true);
										stand.setInvisible(true);
										stand.setGravity(false);
										stand.setInvulnerable(true);
										stand.setSmall(true);
										
										stand.teleport(stand.getLocation().clone().add(0, 0.1, 0));
										
										ZombieSpawner.mineBlocks.add(loc.getBlock());
									}
									
								}
							}
						}
						
						for(Block b : ZombieSpawner.knifeBlocks)
						{
							if(b == null || b.getType() == Material.AIR)
							{
								ZombieSpawner.knifeBlocks.remove(b);
								break;
							}
							if(rand.nextInt(301) < 3)
							{
								b.setType(Material.AIR);
	
								b.getWorld().playSound(b.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
								
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle block cobblestone " + b.getLocation().getX() + " " + b.getLocation().getY() + " " + b.getLocation().getZ() + " 0 0 0 1 90 normal");
							
								for(Entity en : b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 1f, 1f, 1f))
								{
									if(en instanceof ArmorStand)
									{
										if(en.getCustomName().contains("Zombie Knife"))
										{
											en.remove();
										}
									}
								}
								
								ZombieSpawner.knifeBlocks.remove(b);
								Events.playerKnives.remove(b.getLocation());
								break;
							}
						}
						
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                for (int y = -1; y <= 1; y++) {
                                    final Material material = e.getLocation().add(x, y, z).getBlock().getType();
                                    final Block block = e.getLocation().add(x, y, z).getBlock();
                                    if(rand.nextInt(101) < 10)
                                    {
                                    	if(material == Material.OAK_FENCE_GATE || material == Material.OAK_PLANKS || material == Material.OAK_DOOR || material == Material.BIRCH_DOOR || material == Material.DARK_OAK_DOOR || material == Material.JUNGLE_DOOR || material == Material.ACACIA_DOOR  || material == Material.SPRUCE_DOOR)
                                    	{
                        					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block oak_planks " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 1000 normal @a");
                                    		block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);
                        					block.breakNaturally();
                                    	}
                                    	
                                    	if(block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE ||  block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE || block.getType() == Material.BLUE_STAINED_GLASS_PANE ||  block.getType() == Material.BLACK_STAINED_GLASS_PANE || block.getType() == Material.WHITE_STAINED_GLASS_PANE || block.getType() == Material.LIGHT_GRAY_STAINED_GLASS)
                                    	{
                        					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 1000 normal @a");
                                    		block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                        					block.breakNaturally();
                                    	}
                                    }else {
                                    	if(material == Material.OAK_PLANKS || material == Material.OAK_DOOR || material == Material.BIRCH_DOOR || material == Material.DARK_OAK_DOOR || material == Material.JUNGLE_DOOR || material == Material.ACACIA_DOOR  || material == Material.SPRUCE_DOOR)
                                    	{
                        					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block oak_planks " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
                                    		block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
                                    	}
                                    	if(block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE ||  block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE || block.getType() == Material.BLUE_STAINED_GLASS_PANE ||  block.getType() == Material.BLACK_STAINED_GLASS_PANE || block.getType() == Material.WHITE_STAINED_GLASS_PANE || block.getType() == Material.LIGHT_GRAY_STAINED_GLASS)
                                    	{
                                    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:block glass_pane " + block.getX() + " " + block.getY() + " " + block.getZ() + " 0.1 0.1 0.1 1 100 normal @a");
                                    		block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_HIT, 1, 1);
                                    	}
                                    }
                                }
                            }
                        }
					}
				}
				
				/*
				for(Location loc : Events.bulletHoles)
				{
	                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:angry_villager " + loc.getX() + " " + (loc.getY() + 1) + " " + loc.getZ() + " 0 0 0 0 100 normal");
				}*/
		    }
		}, 4, 4);
		
		getServer().getPluginManager().registerEvents(events, this);
		Bukkit.getServer().getWorld("map").setAutoSave(false);
		getCommand("start").setExecutor(new Commands());
		
		getCommand("setrole").setExecutor(new Commands());
		getCommand("resetloots").setExecutor(new Commands());
		getCommand("cutscene").setExecutor(new Commands());
		getCommand("loadloots").setExecutor(new Commands());
		getCommand("loadmines").setExecutor(new Commands());
		getCommand("loadknives").setExecutor(new Commands());
		getCommand("credits").setExecutor(new Commands());
		getCommand("summonzombie").setExecutor(new Commands());
	}
	
    public static void ActionBar(String message)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }
	
	@SuppressWarnings("deprecation")
	public static void SetDisplayMission(String... mission)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective("mission");
    	
    	if(obj != null)
    	{
    		obj.unregister();
    	}
    	
		obj = board.registerNewObjective("mission", "dummy");
    	
    	obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Current Objective");
    	for(String m : mission)
    	{
    		obj.getScore(ChatColor.BLUE + m).setScore(0);
    	}
    	
    	obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public static void HideDisplayMission()
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
    	Scoreboard board = manager.getMainScoreboard();
    	Objective obj = board.getObjective("mission");
    	//obj.setDisplaySlot(null);
    	
    	if(obj != null)
    	{
    		obj.unregister();
    	}
	}
	
	public static void GrantAdvance(Player p, String a)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only zombiegame:" + a);
	}
	
	@Override
	public void onDisable()
	{
		for(Location loc : Events.resetElecBox)
		{
			loc.getBlock().setType(Material.JUNGLE_BUTTON);
			Directional d = (Directional)loc.getBlock().getBlockData();
			d.setFacing(BlockFace.SOUTH);
			
			loc.getBlock().setBlockData(d);
		}
		
		if(Missions.guideplayer != null)
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				removeNPCPacket(Missions.guideplayer.getBukkitEntity().getPlayer(), p);
			}
		}
	}
	public static void removeNPCPacket(Entity npc, Player player) {
	    PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
	    connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getEntityId()));
	}
}
