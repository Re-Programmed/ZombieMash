import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import roles.MedicRole;
import roles.Roles;

public class ZombieManager {

	public static ArrayList<Roles> roles = new ArrayList<Roles>();
	
	public static void LoadByInt(int val)
	{
		ZombieManager.world = Bukkit.getWorlds().get(0);

		roles.clear();
		roles.add(new MedicRole());
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Random random = new Random();
			Roles rand = roles.get(random.nextInt(roles.size()));
			
			rand.AddRole(p);
		}
		
		switch(val)
		{
		case 1:
			WaveOne();
			break;
		}
	}
	
	static World world;
	public static void WaveOne()
	{		
		Random rand = new Random();
		
		/*Protect Point
		Location loc = new Location(world, rand.nextInt(50) - 25, 0, rand.nextInt(50) - 25);
		
		loc.setY(world.getHighestBlockYAt(loc));
		
		Missions.CreateGaurdPoint(loc);
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			player.teleport(loc);
		}
		*/
		
		/*Guide
		Missions.CreateGuideMission(new Location(world, 94, 4, 34), "Timmy Tyler III Jr.", new Location(world, -11, 4, 34));
		*/
		
		//Missions.CreateFixMission(new Location(world, -84, 4, -40), 5, "Fix Gas Pump");
		
		for(Location loc : Events.resetElecBox)
		{
			loc.getBlock().setType(Material.JUNGLE_BUTTON);
			Directional d = (Directional)loc.getBlock().getBlockData();
			d.setFacing(BlockFace.SOUTH);
		}
		//Main.SetDisplayMission("Guide TheForemostDerp", "to the safe house.");
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.teleport(new Location(Bukkit.getWorlds().get(0), -1, 4, 5));
			p.setInvisible(false);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		
		Main.SetDisplayMission("20 second grace period.");
		Timer t = new Timer(1000 * 20, eva -> {
		Main.HideDisplayMission();
		ZombieSpawner.SetDiff(1);
		
		Timer a = new Timer(0, e -> {
			//Main.SetDisplayMission("Protect the safe house.");
			Missions.QueuedProtections.put(new Location(Bukkit.getWorlds().get(0), -69, 4, 102), "Gaurd The House");
			Missions.QueuedProtectionsData.put(new Timer(0, ev -> {
				Missions.fixmissionqueue.put(new Location(Bukkit.getWorlds().get(0), -57, 4, 110), 5);
				Missions.fixmissiondataqueue.put(ChatColor.GREEN + "Fix The Balcony Supports", new Timer(0, eu -> {
					
				}));
			}), false);
		});
		
		Missions.presetguide.add(new GuidePreset(a, new Location(world, -69, 4, -50), new Location(world, -69, 4, 102), "TheForemostDerp"));
		});
		
		t.setRepeats(false);
		t.start();
	}
	
	
	public static void WaveEnd()
	{
		Main.advance = 0;
	}
}
