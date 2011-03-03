package multiplicity3.jme3csys.items.image;


import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.image.ImageImpl;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItemDelegate;

@ImplementsContentItem(target = IImage.class)
public class JMEImage extends ImageImpl implements IInitable {

	public JMEImage(String name, UUID uuid) {
		super(name, uuid);
		JMEImageDelegate delegate = new JMEImageDelegate(this);
		setDelegate(delegate);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEItemDelegate) getDelegate()).initializeGeometry(assetManager);
	}
}
