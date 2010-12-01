package multiplicity.csysng.items;

public interface IPalet extends IItem {

	public void lockPalet(boolean locked);
	
	public int tap();
	
	public void resetTaps();

}
