
public class PlayerData {

	public int AkReload = 0;
	public int AkShots = 0;
	
	public int PistolShots = 0;
	
	public int SMGShots = 0;
	public int SMGReload = 0;
	
	public int SniperShots = 0;
	
	public int ARReload = 0;
	public int ARShots = 0;
	
	public int ShotgunShots = 0;
	
	public int MiniReload = 0;
	public int MiniShots = 0;
	
	public int RPGReload = 0;
	
	public int SPAZReload = 0;
	public int SPAZShots = 0;
	public int GRENADE = 0;

	public PlayerData()
	{
		
	}
	
	public PlayerData(PlayerData data)
	{
		AkReload = data.AkReload;
		AkShots = data.AkShots;
		PistolShots = data.PistolShots;
		SMGShots = data.SMGShots;
		SMGReload = data.SMGReload;
		SniperShots = data.SniperShots;
		ARReload = data.ARReload;
		ARShots = data.ARShots;
		ShotgunShots = data.ShotgunShots;
		MiniShots = data.MiniShots;
		MiniReload = data.MiniReload;
		RPGReload = data.RPGReload;
		SPAZReload = data.SPAZReload;
		SPAZShots = data.SPAZShots;
		GRENADE = data.GRENADE;
	}
	
}
