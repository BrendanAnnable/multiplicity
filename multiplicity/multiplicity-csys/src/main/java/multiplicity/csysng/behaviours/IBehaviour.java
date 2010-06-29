package multiplicity.csysng.behaviours;

import multiplicity.csysng.items.IItem;

public interface IBehaviour {
	public void setItemActingOn(final IItem item);

    public void removeItemActingOn();
}
