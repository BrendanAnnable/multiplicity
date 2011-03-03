package multiplicity3.jme3csys.items.shapes;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.shapes.ColourRectangleImpl;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.jme3csys.items.IInitable;

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
