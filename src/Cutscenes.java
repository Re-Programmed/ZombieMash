import java.util.Random;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cutscenes {

	public static void IntroCutscene()
	{
		Location loca = new Location(Bukkit.getWorlds().get(0), -115, 5, -139);
		loca.getBlock().setType(Material.WHITE_CONCRETE);
		loca.getBlock().getRelative(BlockFace.UP).setType(Material.WHITE_CONCRETE);
		loca.getBlock().getRelative(BlockFace.SOUTH).setType(Material.WHITE_CONCRETE);
		loca.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).setType(Material.WHITE_CONCRETE);
		loca.getBlock().getRelative(BlockFace.NORTH).setType(Material.WHITE_CONCRETE);
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 20));
			p.playSound(p.getLocation(), Sound.MUSIC_DISC_STAL, 1, 1);
			p.setInvisible(true);
			p.teleport(new Location(Bukkit.getWorlds().get(0), -103, 5, -142));
		}
		
		Timer t = new Timer(20 * 1000, e -> {
			Main.planeShake = true;
			
			Timer t2 = new Timer(2 * 1000, e2 -> {
				for(Player p2 : Bukkit.getOnlinePlayers())
				{
					p2.sendMessage("<" + ((Player)Bukkit.getOnlinePlayers().toArray()[(new Random()).nextInt(Bukkit.getOnlinePlayers().size())]).getName() + "> What is going on?");
				}
				
				Timer t3 = new Timer(4 * 1000, e3 -> {
					for(Player p2 : Bukkit.getOnlinePlayers())
					{
						p2.sendMessage("<Pilot> Just a bit of turbulence.");
					}
					
					Timer t4 = new Timer(5 * 1000, e4 -> {
						for(Player p2 : Bukkit.getOnlinePlayers())
						{
							p2.sendMessage("<" + ((Player)Bukkit.getOnlinePlayers().toArray()[(new Random()).nextInt(Bukkit.getOnlinePlayers().size())]).getName() + "> That was a bit more than just turbulence.");
						}
						
						Timer t5 = new Timer(8 * 1000, e5 -> {
							for(Player p2 : Bukkit.getOnlinePlayers())
							{
								Random rand = new Random();
								Location loc = new Location(p2.getLocation().getWorld(), p2.getLocation().getBlockX() + (rand.nextInt(11) - 5), p2.getLocation().getBlockY(), p2.getLocation().getBlockZ() + (rand.nextInt(11) - 5));
								p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
							}
							
							Timer t6 = new Timer(1 * 1000, e6 -> {
								for(Player p2 : Bukkit.getOnlinePlayers())
								{
									p2.sendMessage("<Pilot> We've lost an engine!");
								}
								
								Timer t7 = new Timer(5 * 1000, e7 -> {
									for(Player p2 : Bukkit.getOnlinePlayers())
									{
										p2.sendMessage("<" + ((Player)Bukkit.getOnlinePlayers().toArray()[(new Random()).nextInt(Bukkit.getOnlinePlayers().size())]).getName() + "> Wait what is happening, are we getting shot at?");
									}
									
									Timer t8 = new Timer(8 * 1000, e8 -> {
										for(Player p2 : Bukkit.getOnlinePlayers())
										{
											Random rand = new Random();
											Location loc = new Location(p2.getLocation().getWorld(), p2.getLocation().getBlockX() + (rand.nextInt(11) - 5), p2.getLocation().getBlockY(), p2.getLocation().getBlockZ() + (rand.nextInt(11) - 5));
											p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
										}
										
										Timer t9 = new Timer(1 * 1000, e9 -> {
											for(Player p2 : Bukkit.getOnlinePlayers())
											{
												p2.sendMessage("<Pilot> UH OH! All our engines are out!");
											}
											
											Timer t10 = new Timer(10 * 1000, e10 -> {
												for(Player p2 : Bukkit.getOnlinePlayers())
												{
													p2.sendMessage("<Pilot> No STOP! AHHHHH!!...");
												}
												
												Timer t11 = new Timer(2 * 1000, e11 -> {
													for(Player p2 : Bukkit.getOnlinePlayers())
													{
														Random rand = new Random();
														Location loc = new Location(p2.getWorld(), -95, 5, -139);
																
														p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
														p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
														p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
														p2.playSound(loc, Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
														p2.playSound(loc, Sound.BLOCK_BAMBOO_BREAK, 5, rand.nextFloat() + rand.nextFloat());
													}
													
													Main.SetDisplayMission("Evacuate the plane!");
													Main.planeShake = true;
													Main.randomShot = true;
													
													Timer t12 = new Timer(10 * 1000, e12 -> {
														for(Player p2 : Bukkit.getOnlinePlayers())
														{
															Random rand = new Random();
															Location loc = new Location(p2.getWorld(), -115, 5, -139);
																	
															p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
															p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
															p2.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, rand.nextFloat() + rand.nextFloat());
															
															Main.removeplane = true;

														}
													});
													
													t12.setRepeats(false);
													t12.start();
												});
												
												t11.setRepeats(false);
												t11.start();
											});
											
											t10.setRepeats(false);
											t10.start();
										});
										
										t9.setRepeats(false);
										t9.start();
									});
									
									t8.setRepeats(false);
									t8.start();
								});
								
								t7.setRepeats(false);
								t7.start();
							});
							
							t6.setRepeats(false);
							t6.start();
						});
						
						t5.setRepeats(false);
						t5.start();
					});
					
					t4.setRepeats(false);
					t4.start();
				});
				t3.setRepeats(false);
				t3.start();
			});
			
			t2.setRepeats(false);
			t2.start();
		});
		
		Timer t2 = new Timer(24 * 1000, e -> {
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.setInvisible(false);
			}
		});
		
		t.setRepeats(false);
		t.start();
		
		t2.setRepeats(false);
		t2.start();
	}	
}
