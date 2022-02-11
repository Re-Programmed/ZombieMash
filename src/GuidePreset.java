import javax.swing.Timer;

import org.bukkit.Location;

public class GuidePreset {

	public final Location loc;
	public final String Title;
	public final Timer timer;
	public final Location dest;
	
	public GuidePreset(Timer timer, Location loc, Location dest, String Title)
	{
		this.loc = loc;
		this.timer = timer;
		this.Title = Title;
		this.dest = dest;
	}
	
}
