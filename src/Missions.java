import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import Loot.LootBoxes;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class Missions {

	public static float GaurdAmount = 0;
	
	public static ArrayList<Location> GaurdPoints = new ArrayList<Location>();
	public static ArrayList<ArmorStand> GaurdStands = new ArrayList<ArmorStand>();
	
	public static ArmorStand Guide;
	public static Location destination;
	
	public static float GuideHP = 100f;
	public static String Name = "";
	
	static boolean legfor = true;
	
	public static float GuideResp = 10f;
	public static ArmorStand GuideTemp;
	
	public static EntityPlayer guideplayer;
	public static GameProfile guideprof;
	
	public static Timer runafter;
	
	public static HashMap<Location, String> QueuedProtections = new HashMap<Location, String>();
	public static HashMap<Timer, Boolean> QueuedProtectionsData = new HashMap<Timer, Boolean>();

	public static ArrayList<GuidePreset> presetguide = new ArrayList<GuidePreset>();
	
	public static void GuideMission(Location loc)
	{	
		int nearbyPlayers = 0;
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 5, 5, 5))
		{
			if(e instanceof Player)
			{
				nearbyPlayers ++;
			}
		}
		
		if(nearbyPlayers > 0)
		{
			CreateParticleCircle(5, 0, 0, 1, loc);
			
			Vector pos = Guide.getLocation().toVector();
			Vector target = destination.toVector();
			Vector velocity = target.subtract(pos);
			
			Guide.setVelocity(velocity.normalize().multiply(0.1));
			
			Events.activeloc = Guide.getLocation();
			
			/*for(Player p : Bukkit.getOnlinePlayers())
			{
				velocity = velocity.multiply(2.3);
				sendMoveEntityPacket(guideplayer.getBukkitEntity().getPlayer(), p, velocity.getX(), velocity.getY(), velocity.getZ());
			}*/
			
			if(legfor)
			{
				Guide.setLeftLegPose(new EulerAngle(Guide.getLeftLegPose().getX() + 0.3, 0, 0));
				Guide.setRightLegPose(new EulerAngle(Guide.getRightLegPose().getX() - 0.3, 0, 0));

				if(Guide.getLeftLegPose().getX() > 0.45)
				{
					legfor = false;
				}
			}else {
				Guide.setLeftLegPose(new EulerAngle(Guide.getLeftLegPose().getX() - 0.3, 0, 0));
				Guide.setRightLegPose(new EulerAngle(Guide.getRightLegPose().getX() + 0.3, 0, 0));

				if(Guide.getLeftLegPose().getX() < -0.45)
				{
					legfor = true;
				}
			}
			
		}else {
			CreateParticleCircle(5, 1, 0, 0, loc);
		}
		
		if(Guide.getLocation().distance(destination) < 2)
		{
			runafter.setRepeats(false);
			runafter.start();
			
			Guide.remove();
			
			Guide = null;
			
			/*
			GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()));
			GenLoot(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()));
			GenLoot(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() + 1, loc.getZ()));
			GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() + 1));

			GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ()), false);
			*/
		}
		
		if(Guide.getLocation().getBlock().getType() == Material.CRIMSON_PRESSURE_PLATE)
		{
			GuideHP -= 1;
		}
		
		
		for(Location c : Events.AutoCharges)
		{
			if(c.distance(Guide.getLocation()) < 1)
			{
				Location autocharge = c;
				autocharge.getBlock().setType(Material.ACTIVATOR_RAIL);
				autocharge.getWorld().createExplosion(autocharge, 3.5f, false, false);
				
				Events.AutoCharges.remove(c);
				
				GuideHP -= 45;
			}
		}
		
		Guide.setCustomName(Name + ChatColor.WHITE + " - " + ChatColor.RED + GuideHP + " HP");
		Guide.setCustomNameVisible(true);
		
		if(GuideHP < 1)
		{
			GuideTemp = (ArmorStand)Guide.getLocation().getWorld().spawnEntity(new Location(Guide.getLocation().getWorld(), Guide.getLocation().getX(), Guide.getLocation().getY() + 2, Guide.getLocation().getZ()), EntityType.ARMOR_STAND);
			
			GuideTemp.setInvisible(true);
			GuideTemp.setInvulnerable(true);
			GuideTemp.setGravity(false);
			GuideTemp.setCustomName(ChatColor.GOLD + "Respawning in 10s");
			GuideResp = 10f;
			GuideTemp.setCustomNameVisible(true);
			
			Guide.remove();
			Guide = null;
			
			GuideHP = 100f;
		}
	}
	
	public static void FixMission(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getItem() != null)
			{
				if(event.getItem().getType() == Material.CRIMSON_FENCE)
				{
					if(event.getClickedBlock().getLocation().distance(fixmission) < 2.5f)
					{
						event.setCancelled(true);
						storedbatteries++;
						int v = fixbatteries - storedbatteries;
						
						if(v == 1)
						{
							fixstand.setCustomName(ChatColor.GOLD + fixtitle + " - " + v + " battery");
						}else {
							fixstand.setCustomName(ChatColor.GOLD + fixtitle + " - " + v + " batteries");
						}
						
						if(event.getItem().getAmount() > 1)
						{
							event.getItem().setAmount(event.getItem().getAmount() - 1);
						}else {
							event.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						}
						
						if(v <= 0)
						{
							for(Player p : Bukkit.getOnlinePlayers())
							{
								p.playSound(fixmission, Sound.BLOCK_ANVIL_DESTROY, 5f, 1f);
							}
							
							fixmission.getBlock().setType(Material.AIR);
							fixmission = null;
							fixstand.remove();
							runafter.setRepeats(false);
							runafter.start();
							fixstand = null;
						}
					}
				}
			}
		}
	}
	
	public static void GaurdPointMission(Location loc)
	{
		int nearbyPlayers = 0;
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 5, 5, 5))
		{
			if(e instanceof Player)
			{
				nearbyPlayers ++;
			}
		}
		
		GaurdAmount += nearbyPlayers/Bukkit.getOnlinePlayers().size();
		
		if(nearbyPlayers > 0)
		{
			CreateParticleCircle(5, 0, 1, 0, new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()));
		}
		else 
		{
			CreateParticleCircle(5, 0, 0, 1, new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()));
			
			if(GaurdAmount > 250)
			{
				GaurdAmount -= 0.1f;
			}
		}
		
		GaurdStands.get(0).setCustomName(ChatColor.BLUE + prottitl + ChatColor.WHITE + " - " + ChatColor.GOLD + (GaurdAmount/5) + "%");
		
		if(GaurdAmount > 500)
		{
			GaurdPoints.remove(loc);
			
			runafter.setRepeats(false);
			runafter.start();
			
			GaurdStands.get(0).remove();
			GaurdStands.clear();
			
			if(loot)
			{
				GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()));
				GenLoot(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()));
				GenLoot(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() + 1, loc.getZ()));
				GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() + 1));

				GenLoot(new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ()), false);
			}
		}
		
	}
	
	protected static boolean loot = false;
	
	public static void GenLoot(Location locn) {GenLoot(locn, true);}
	
	public static void GenLoot(Location locn, boolean block)
	{		
		LootBoxes spawnloot = LootManager.randomLoot();
	
		try {
					
		locn.getBlock().setType(Material.AIR);
		locn.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);	
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock " + locn.getBlockX() + " " + locn.getBlockY() + " " + locn.getBlockZ() + " structure_block[mode=load]{name:\"" + spawnloot.name + "\",rotation:\"NONE\",mirror:\"NONE\",mode:\"LOAD\"} replace");
		locn.getBlock().getRelative(BlockFace.DOWN).setType(Material.REDSTONE_BLOCK);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		if(block)
		{
			new Location(locn.getWorld(), locn.getX(), locn.getY() - 1, locn.getZ()).getBlock().setType(Material.BLACK_CONCRETE);
		}
	}

	public static String prottitl;
	
	public static void CreateGaurdPoint(Location loc, String Title, Timer timer, boolean loot)
	{
		Events.activeloc = loc;
		
		ArmorStand stand = (ArmorStand)loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), EntityType.ARMOR_STAND);
		
		prottitl = Title;
		
		runafter = timer;
		
		Missions.loot = loot;
		
		stand.setInvisible(true);
		stand.setCustomName(ChatColor.BLUE + Title);
		stand.setCustomNameVisible(true);
		stand.setSilent(true);
		stand.setGravity(false);
		stand.setInvulnerable(true);
		
		GaurdPoints.add(loc);
		GaurdStands.add(stand);
	}
	
	static Location fixmission;
	static int fixbatteries;
	static ArmorStand fixstand;
	
	static String fixtitle;
	
	static int storedbatteries;
	
	public static HashMap<Location, Integer> fixmissionqueue = new HashMap<Location, Integer>();
	public static HashMap<String, Timer> fixmissiondataqueue = new HashMap<String, Timer>();
	
	public static void CreateFixMission(Location loc, int batteries, String title, Timer timer)
	{
		Events.activeloc = loc;
		
		fixmission = loc;
		fixbatteries = batteries;
		
		loc.getBlock().setType(Material.ORANGE_TERRACOTTA);
		storedbatteries = 0;
		
		ArmorStand stand = (ArmorStand)loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), EntityType.ARMOR_STAND);
		
		stand.setInvisible(true);
		stand.setInvulnerable(true);
		stand.setGravity(false);
		stand.setCustomNameVisible(true);
		stand.setCustomName(ChatColor.GOLD + title + " - " + batteries + " batteries");
		
		fixstand = stand;
		
		runafter = timer;
		
		fixtitle = title;
		
		for(int i = 0; i < batteries; i++)
		{
			SpawnBattery(loc, 38);
		}
	}
	
	public static void SpawnBattery(Location source, int radius)
	{
		Random rand = new Random();
		Location newloc = new Location(source.getWorld(), source.getX() + (rand.nextInt(radius) - radius/2), 1, source.getZ() + (rand.nextInt(radius) - radius/2));
		
		newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc));
		
		newloc.add(0, 1, 0);
		
		if(newloc.getBlock().getType() == Material.AIR && newloc.distance(source) > 2)
		{
			if(newloc.getY() < 10)
			{
				newloc.getBlock().setType(Material.CRIMSON_FENCE);
				
				ArmorStand stand = (ArmorStand)newloc.getWorld().spawnEntity(new Location(newloc.getWorld(), newloc.getX() + 0.5, newloc.getY() + 1, newloc.getZ() + 0.5), EntityType.ARMOR_STAND);
				
				stand.setSmall(true);
				stand.setInvisible(true);
				stand.setMarker(true);
				stand.setInvulnerable(true);
				stand.getEquipment().setHelmet(new ItemStack(Material.CRIMSON_FENCE));
				stand.setGlowing(true);
				stand.setGravity(false);
				stand.setCustomName(ChatColor.BLUE + "Battery");
				stand.setCustomNameVisible(true);
				
				return;
			}
		}
		
		SpawnBattery(source, radius);
	}
	
	public static void CreateGuidMission()
	{
		
	}
	
	public static void sendMoveEntityPacket(Entity entity, Player player, double x, double y, double z) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(entity.getEntityId(), (short)(x * 4096), (short)(y * 4096), (short)(z * 4096), true));
	}
	
	public static void CreateGuideMission(Location loc, String name, Location destinationi, Timer runafterval)
	{
		Events.activeloc = loc;
		
		Guide = (ArmorStand)loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), EntityType.ARMOR_STAND);
		
		Guide.setCustomNameVisible(false);
		Guide.setCustomName(ChatColor.DARK_PURPLE + name);
		Guide.setBasePlate(false);
		
		Guide.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		Guide.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
		Guide.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		Guide.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));

		Guide.setInvulnerable(true);
		
		runafter = runafterval;
		
		Vector dirBetweenLocations = destinationi.toVector().subtract(Guide.getLocation().toVector());
		Location loca = Guide.getLocation();
		loca.setDirection(dirBetweenLocations);
		Guide.teleport(loca);

		Name = name;
		
		destination = destinationi;
		/*
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)loc.getWorld()).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "TheForemostDerp"); // Change "playername" to the name the NPC should have, max 16 characters.
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld)); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loca.getYaw(), loc.getPitch());
        
        guideprof = gameProfile;
        guideplayer = npc;
        
        for(Player p : Bukkit.getOnlinePlayers())
        {
        	addNPCPacket(npc, p);
        }
        */
	}
	
	public static void addNPCPacket(EntityPlayer npc, Player player) {
	    PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
	    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
	    connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)); // Spawns the NPC for the player client.
	    connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360))); // Correct head rotation when spawned in player look direction.
	}
	
	public static void MissionTick() 
	{
		int i = 0;
		for(Map.Entry<Location, String> entry : QueuedProtections.entrySet())
		{
			CreateGaurdPoint(entry.getKey(), entry.getValue(), (Timer)QueuedProtectionsData.keySet().toArray()[i], (Boolean)QueuedProtectionsData.values().toArray()[i]);
			i++;
		}
		i=0;
		for(Map.Entry<Location, Integer> entry : fixmissionqueue.entrySet()) 
		{
			CreateFixMission(entry.getKey(), entry.getValue(), (String)fixmissiondataqueue.keySet().toArray()[i], (Timer)fixmissiondataqueue.values().toArray()[i]);
			i++;
		}
		
		for(GuidePreset preset : presetguide)
		{
			CreateGuideMission(preset.loc, preset.Title, preset.dest, preset.timer);
		}
		
		presetguide.clear();
		
		QueuedProtectionsData.clear();
		QueuedProtections.clear();
		fixmissiondataqueue.clear();
		fixmissionqueue.clear();
		
		for(Location loc : GaurdPoints)
		{
			GaurdPointMission(loc);
		}
		
		if(Guide != null)
		{
			GuideMission(Guide.getLocation());
		}else {
			if(GuideTemp != null)
			{
				GuideResp -= 0.2f;
				
				GuideTemp.setCustomName(ChatColor.GOLD + "Respawning in " + Math.round(GuideResp) + "s");
				
				if(GuideResp <= 0)
				{
					CreateGuideMission(new Location(GuideTemp.getLocation().getWorld(), GuideTemp.getLocation().getX(), GuideTemp.getLocation().getY() - 2, GuideTemp.getLocation().getZ()), Name, destination, runafter);
					
					GuideTemp.remove();
					
					GuideTemp = null;
				}
			}
		}
	}
	
	public static void CreateParticleCircle(double radius, double r, double g, double b, Location myloc)
	{
		double angle = Math.PI;
		for (int i = 0; i < 360; i++) {
			if(angle < -Math.PI/8) angle = Math.PI;
		       
	        double x = (radius * Math.sin(angle));
	        double z = (radius * Math.cos(angle));
	        angle += 15;
	       
	       // myloc.getWorld().spawnParticle(Particle.REDSTONE, myloc.getX() + x, myloc.getY(), myloc.getZ() + z, 0, r, g, b, 1);
	        

                Particle.DustOptions dust = new Particle.DustOptions(
                        Color.fromRGB((int) r * 255, (int) g * 255, (int) b * 255), 1);
                myloc.getWorld().spawnParticle(Particle.REDSTONE, myloc.getX() + x, myloc.getY(), myloc.getZ() + z, 0, 0, 0, 0, dust);
            
		}
	}
}
