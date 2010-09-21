package multiplicity.csysng;

import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysng.display.DisplayManager;
import multiplicity.csysng.draganddrop.DragAndDropSystem;
import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.picksystem.IPickSystem;

public class ContentSystem {
	private static ContentSystem instance;

	public static ContentSystem getContentSystem() {
		synchronized(ContentSystem.class) { 
			if(instance == null) instance = new ContentSystem();
			return instance;
		}
	}

	private IContentFactory contentFactory;
	private DisplayManager displayManager;
	private AnimationSystem animationSystem;
	private DragAndDropSystem dragAndDropSystem;
	private IPickSystem pickSystem;
	
	public void setContentFactory(IContentFactory contentFactory) {
		this.contentFactory = contentFactory;
	}
	
	public IContentFactory getContentFactory() {
		return this.contentFactory;
	}
	
	public void setDisplayManager(DisplayManager manager) {
		this.displayManager = manager;
	}
	
	public DisplayManager getDisplayManager() {
		return this.displayManager;
	}

	public void setAnimationSystem(AnimationSystem animationSystem) {
		this.animationSystem = animationSystem;
	}

	public AnimationSystem getAnimationSystem() {
		return animationSystem;
	}

	public void setDragAndDropSystem(DragAndDropSystem dragAndDropSystem) {
		this.dragAndDropSystem = dragAndDropSystem;
	}

	public DragAndDropSystem getDragAndDropSystem() {
		return dragAndDropSystem;
	}

	public void setPickSystem(IPickSystem pickSystem) {
		this.pickSystem = pickSystem;
	}

	public IPickSystem getPickSystem() {
		return pickSystem;
	}
}
