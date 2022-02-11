import java.util.ArrayList;

import javax.swing.Timer;

import org.bukkit.Sound;
import org.bukkit.block.Block;

public class GarageDoor {

	public final ArrayList<Block> topBlocks;
	public final ArrayList<Block> middleBlocks;
	public final ArrayList<Block> bottomBlocks;

	public final ArrayList<Block> allblocks;
	
	public GarageDoor(ArrayList<Block> topBlocks, ArrayList<Block> middleBlocks, ArrayList<Block> bottomBlocks)
	{
		this.topBlocks = topBlocks;
		this.middleBlocks = middleBlocks;
		this.bottomBlocks = bottomBlocks;
		this.allblocks = new ArrayList<Block>();
		allblocks.addAll(topBlocks);
		allblocks.addAll(bottomBlocks);
		allblocks.addAll(middleBlocks);
	}
	
	public void Open()
	{

		bottomBlocks.get(0).getWorld().playSound(bottomBlocks.get(0).getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);
			Main.removeQueue.addAll(topBlocks);
			Main.removeQueue.addAll(middleBlocks);
			Main.removeQueue.addAll(bottomBlocks);

		Timer timer2 = new Timer(1 * 1000, e -> {

			middleBlocks.get(0).getWorld().playSound(middleBlocks.get(0).getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);
			
		});
		
		timer2.setRepeats(false);
		timer2.start();
		
		Timer timer3 = new Timer(2 * 1000, e -> {
			topBlocks.get(0).getWorld().playSound(topBlocks.get(0).getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);
			
			
		});
		
		timer3.setRepeats(false);
		timer3.start();
	}
	
}
