package multiplicity.csysng.items;

import multiplicity.csysng.items.item.IItem;

public interface IPalet extends IItem {

	public void lockPalet(boolean locked);
	
	public int tap();
	
	public void resetTaps();

}
