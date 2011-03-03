package multiplicity3.jme3csys.items.container;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.container.ContainerImpl;
import multiplicity.csysng.items.container.IContainer;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IContainer.class)
public class JMEContainer extends ContainerImpl implements IInitable {

	public JMEContainer(String name, UUID uuid) {
		super(name, uuid);
		setDelegate(new JMEContainerDelegate(this));
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEContainerDelegate) getDelegate()).initializeGeometry(assetManager);
	}

}
