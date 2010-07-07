package multiplicity.csysng.items;

public interface IPalet extends IItem {

	public void updatePalet(boolean locked);
	
	public int tap();
	
	public void resetTaps();

}
