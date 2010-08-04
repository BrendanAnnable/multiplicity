package multiplicity.app;

import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysng.display.DisplayManager;
import multiplicity.csysng.draganddrop.DragAndDropSystem;
import multiplicity.csysngjme.factory.JMEContentItemFactory;
import multiplicity.csysngjme.picking.ContentSystemPicker;
import multiplicity.input.IMultiTouchEventProducer;

public abstract class AbstractMultiplicitySurfaceSystem extends AbstractSurfaceSystem {
	
	/**
	 * This method must be called by subclasses that override it
	 */
	@Override
	protected void initSurfaceSystem(IMultiTouchEventProducer producer) {
		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		ContentSystem csys = ContentSystem.getContentSystem(); 
		csys.setContentFactory(new JMEContentItemFactory());
		csys.setDragAndDropSystem(DragAndDropSystem.getInstance());
		csys.setPickSystem(new ContentSystemPicker(getSurfaceSystemOrthoNode()));
		csys.setAnimationSystem(AnimationSystem.getInstance());
		csys.setDisplayManager(new DisplayManager());		
		csys.getDragAndDropSystem().setPickSystemForApp(csys.getPickSystem());
		csys.getDisplayManager().setDisplayDimensions(r.getWidth(), r.getHeight());
	}
}
