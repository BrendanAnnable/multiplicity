package multiplicity.csysng.items;

import java.util.List;

public interface INode {
	public void addItem(IItem item);
	public void removeItem(IItem item);
	public boolean hasChildren();
	public int getChildrenCount();
	
	public void registerChildrenChangedListener(IChildrenChangedListener listener);
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener);
	
	public static interface IChildrenChangedListener {
		public void childrenChanged(INode node, List<IItem> list);
	}
}
