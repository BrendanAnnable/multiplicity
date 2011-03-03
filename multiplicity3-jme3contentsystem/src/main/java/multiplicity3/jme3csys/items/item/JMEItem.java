package multiplicity3.jme3csys.items.item;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.item.ItemImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IItem.class)
public abstract class JMEItem extends ItemImpl implements IInitable {

	public JMEItem(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEItemDelegate) getDelegate()).initializeGeometry(assetManager);
	}

}
