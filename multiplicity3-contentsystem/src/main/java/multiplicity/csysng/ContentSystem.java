package multiplicity.csysng;

import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.display.DisplayManager;
import multiplicity.csysng.draganddrop.DragAndDropSystem;
import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.picksystem.IPickSystem;
import multiplicity.csysng.stage.IStage;

public class ContentSystem {

	private IContentFactory contentFactory;
	private DisplayManager displayManager;
	private AnimationSystem animationSystem;
	private DragAndDropSystem dragAndDropSystem;
	private IPickSystem pickSystem;
	private IStage localStage;
	private BehaviourMaker behaviourMaker;
	
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

	public void setLocalStage(IStage stage) {
		this.localStage = stage;		
	}
	
	public IStage getLocalStage() {
		return localStage;
	}
	
	public void setBehaviourMaker(BehaviourMaker bm) {
		this.behaviourMaker = bm;
	}

	public BehaviourMaker getBehaviourMaker() {
		return behaviourMaker;
	}
}
