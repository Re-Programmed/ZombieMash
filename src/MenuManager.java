import java.util.HashMap;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class MenuManager {

	public static HashMap<Block, Timer> signCheck = new HashMap<Block, Timer>();
	
	public static void LoadMainMenu()
	{
		for(Block b : signCheck.keySet())
		{
			b.setType(Material.AIR);
		}
		signCheck.clear();
		
		SetSign(ChatColor.BLUE + "" + ChatColor.BOLD + "Missions", "", -46, 37, -32, new Timer(0, e -> {
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.sendMessage(ChatColor.GREEN + "Loading missions...");
			}
			Main.queueMissionMenu = true;
		}));
		
		TpPlayers();
	}
	
	public static void LoadMissionMenu()
	{
		for(Block b : signCheck.keySet())
		{
			b.setType(Material.AIR);
		}
		signCheck.clear();
		
		SetSign(ChatColor.GREEN + "1. Protect The", ChatColor.GREEN + "Citizens!", -46, 38, -34, new Timer(0, e -> {
			Main.loadMission = new MissionBreif(ChatColor.GREEN + "1 - 3 EASY", ChatColor.RED + "50", ChatColor.BLUE + "30 - 45 min.", "Good morning agents, although your plane has crash landed in a town that isn't even listed on any maps, we can still continue your mission.@ Your objective today is to guide a few residents of the city to safe locations. To view your mission plan go to the next page ->@1. Guide a citizen from the brick building to the green house. 2. Protect the green house -- this is the safe house. 3. Repair the safe house. 4. Survive 2 min. 5. Guide someone from the Church to the Farm House. 6. Protect the market for an incoming Air Drop.@7. Fix the bounce pad at the bottom of the gray building. 8. Guide someone off of the roof of the large gray building to the church. 9. Guide someone from the back of the bowling alley to the back of the yellow building. 10. Protect the park for an airdrop.@11. Survive 5 min. 12. Return to the plane to end the mission.", 1);
		}));
	}
	
	public static void LoadMissionBreif(String difficulty, String deathLim, String duration, String giveBookCommand, int mis)
	{
		for(Block b : signCheck.keySet())
		{
			b.setType(Material.AIR);
		}
		signCheck.clear();
		
		SetSign(ChatColor.RED + "" + ChatColor.BOLD + "Mission Brief", "", -46, 38, -32, new Timer(0, e -> {
			
		}));
		
		SetSign(ChatColor.RED + "Difficulty: ", difficulty, -46, 37, -33, new Timer(0, e -> {
			
		}));
		
		SetSign(ChatColor.GOLD + "Death Limit:", deathLim, -46, 37, -32, new Timer(0, e -> {
			
		}));
		
		SetSign(ChatColor.BLUE + "Average Duration:", duration, -46, 37, -31, new Timer(0, e-> {
			
		}));
		
		SetSign(ChatColor.GREEN + "" + ChatColor.BOLD + "Read Brief", "", -46, 36, -32, new Timer(0, e -> {
			Main.giveCommandBook = giveBookCommand;
		}));
		
		SetSign(ChatColor.GREEN + "" + ChatColor.BOLD + "Start Game", "", -46, 36, -31, new Timer(0, e -> {
			Main.queuedMis = mis;
			//Main.LoadRoles = mis;
		}));
	}
	
	public static void LoadRoleMenu(int mis)
	{
		for(Block b : signCheck.keySet())
		{
			b.setType(Material.AIR);
		}
		signCheck.clear();
		
		int z = -35;
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Location loc = new Location(Bukkit.getWorlds().get(0), -46, 38, z);
			
			loc.getBlock().setType(Material.PLAYER_HEAD);
			
			Skull skull = (Skull)loc.getBlock().getState();
						
			skull.setOwningPlayer(p);
			
	        skull.update();
			
	        Rotatable dir = (Rotatable)loc.getBlock().getBlockData();
	        
	        dir.setRotation(BlockFace.WEST);
	        
	        loc.getBlock().setBlockData(dir);
	        
			z++;
		}
	}
	
	public static void TpPlayers()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.teleport(new Location(Bukkit.getWorlds().get(0), -48.5, 36, -31.5, -90, 0));
			p.setInvisible(true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000 * 20, 255));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000 * 20, 187));
		}
	}
	
	public static void SetSign(String text, String text2, int x, int y, int z, Timer t)
	{
		World world = Bukkit.getWorlds().get(0);
		
		Block b = new Location(world, x, y, z).getBlock();
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock " + x + " " + y + " " + z + " minecraft:oak_wall_sign[facing=west]");
		
		Sign s = (Sign)b.getState();
				
		s.setLine(0, text);
		s.setLine(1, text2);
		
		s.update(true);
		
		signCheck.put(b, t);
	}
}
