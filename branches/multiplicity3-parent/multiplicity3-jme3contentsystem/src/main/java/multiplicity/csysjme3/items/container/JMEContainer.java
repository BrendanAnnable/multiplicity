package multiplicity.csysjme3.items.container;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.container.ContainerImpl;
import multiplicity.csysng.items.container.IContainer;

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
