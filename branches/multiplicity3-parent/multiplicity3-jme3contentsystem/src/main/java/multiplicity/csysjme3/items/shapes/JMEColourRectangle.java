package multiplicity.csysjme3.items.shapes;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.shapes.ColourRectangleImpl;
import multiplicity.csysng.items.shapes.IColourRectangle;

@ImplementsContentItem(target = IColourRectangle.class)
public class JMEColourRectangle extends ColourRectangleImpl implements IInitable {
	public JMEColourRectangle(String name, UUID uuid) {
		super(name, uuid);
		JMEColourRectangleDelegate delegate = new JMEColourRectangleDelegate(this);
		setDelegate(delegate);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEColourRectangleDelegate) getDelegate()).initializeGeometry(assetManager);
	}
}
