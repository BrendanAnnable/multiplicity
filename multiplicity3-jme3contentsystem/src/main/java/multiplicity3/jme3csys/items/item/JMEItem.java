package multiplicity3.jme3csys.items.item;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.items.item.ItemImpl;
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
