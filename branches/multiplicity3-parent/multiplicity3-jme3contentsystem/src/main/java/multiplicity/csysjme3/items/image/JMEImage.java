package multiplicity.csysjme3.items.image;


import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysjme3.items.item.JMEItemDelegate;
import multiplicity.csysng.items.image.IImage;
import multiplicity.csysng.items.image.ImageImpl;
import multiplicity.csysng.annotations.ImplementsContentItem;

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
