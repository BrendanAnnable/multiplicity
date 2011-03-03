package multiplicity.csysng.items.container;

import java.util.UUID;

import multiplicity.csysng.items.item.ItemImpl;

public class ContainerImpl extends ItemImpl implements IContainer {

	//private IContainerDelegate containerDelegate;

	public ContainerImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IContainerDelegate delegate) {
		super.setDelegate(delegate);
		//this.containerDelegate = delegate;		
	}

}


