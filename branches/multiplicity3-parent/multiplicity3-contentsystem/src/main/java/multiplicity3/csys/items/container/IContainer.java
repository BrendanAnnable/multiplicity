package multiplicity3.csys.items.container;

import multiplicity3.csys.items.item.IItem;

public interface IContainer extends IItem {
	
	public Object[] getInformation();
	public void setInformation(Object[] info);

}
