package multiplicity.csysng.items;

import java.util.List;

import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.zorder.IZOrderManager;

public interface INestable {
	//TODO: change to return true/false
	public void addItem(IItem item);
	//TODO: change to return true/false
	public void removeItem(IItem item);
	public boolean hasChildren();
	public int getChildrenCount();
	public IZOrderManager getZOrderManager();
	
	//TODO: change to return true/false
	public void registerChildrenChangedListener(IChildrenChangedListener listener);
	//TODO: change to return true/false
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener);
	
	public static interface IChildrenChangedListener {
		public void childrenChanged(INestable node, List<IItem> list);
	}
}
