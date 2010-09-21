package multiplicity.csysngjme.zordering;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.items.JMEFrame;

public class FrameZOrderManager extends NestedZOrderManager implements IZOrderManager {

	private JMEFrame frameBeingManaged;

	public FrameZOrderManager(JMEFrame frame, int initialCapacity) {
		super(frame, initialCapacity);
		frameBeingManaged = frame;
	}

	@Override
	public void updateZOrdering() {		
		int borderZOrder = startZOrder;
		int drawableContentZOrderStart = startZOrder;
		frameBeingManaged.getTreeRootSpatial().setZOrder(startZOrder);
		if(frameBeingManaged.hasBorder()) {
			frameBeingManaged.getBorder().getZOrderManager().setItemZOrder(borderZOrder);		
			drawableContentZOrderStart += frameBeingManaged.getBorder().getZOrderManager().getZSpaceRequirement();
		}

		// nested content
		int z = drawableContentZOrderStart;		
		for(IItem i : registeredItems) {
			i.getZOrderManager().setItemZOrder(z);
			z += i.getZOrderManager().getZSpaceRequirement();
		}
		
		int maskGeometryZOrder = z;		
		frameBeingManaged.getMaskGeometry().setZOrder(maskGeometryZOrder);	 // this is very important! Draw mask first
	}
}
