package Waves;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class SpawnZombieEvent extends WaveEvent {

	@Override
	public void Call(World world) {
		
		world.spawnEntity(new Location(world, 0, 200, 0), EntityType.ZOMBIE);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute positioned 0 200 0 run spreadplayers 0 0 75 75 false @e[type=minecraft:zombie, distance=0..5]");
	}

	
	
}
