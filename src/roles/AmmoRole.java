package roles;

import net.md_5.bungee.api.ChatColor;

public class AmmoRole extends Roles {

	@Override
	public String GetStartMessage() {
		return ChatColor.RED + "Ammo Agent: \n"
		+ "  - Access sewer locker rooms for ammo.\n"
		+ "  - Access utility tunnels.";
	}


	@Override
	public void GameStart() {
		
	}

}
