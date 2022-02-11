package roles;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public abstract class Roles {
	
	protected ArrayList<Player> players = new ArrayList<Player>();

	public void AddRole(Player player)
	{
		players.add(player);
		
		player.sendMessage(GetStartMessage());
		
		GameStart();
	}
	
	public abstract String GetStartMessage();
	
	public abstract void GameStart();
	
	public boolean CheckPlayer(Player p)
	{
		return players.contains(p);
	}
	
	public void RevokeRole()
	{
		players.clear();
	}
}
