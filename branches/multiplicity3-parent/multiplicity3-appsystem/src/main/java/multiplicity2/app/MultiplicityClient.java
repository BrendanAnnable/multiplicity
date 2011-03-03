package multiplicity2.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity.csysjme3.factory.JME3ContentSystemFactory;
import multiplicity.csysjme3.items.stage.JMEStage;
import multiplicity.csysjme3.items.stage.JMEStageDelegate;
import multiplicity.csysjme3.picking.ContentSystemPicker;
import multiplicity.csysjme3.picking.PickedItemDispatcher;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.IUpdateable;
import multiplicity.csysng.MultiplicityEnvironment;
import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.display.DisplayManager;
import multiplicity.csysng.draganddrop.DragAndDropSystem;
import multiplicity.csysng.factory.ContentTypeAlreadyBoundException;
import multiplicity.csysng.factory.ContentTypeInvalidException;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.MultiTouchInputComponent;
import multiplicity.input.exceptions.MultiTouchInputException;
import multiplicity2.app.jme.JMEAppRoot;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MultiplicityClient extends JMEAppRoot implements IQueueOwner {
	
	private static final Logger log = Logger.getLogger(MultiplicityClient.class.getName());
	
//	public static void main(String[] args) {
//		MultiplicityClient m = new MultiplicityClient();
//		m.start();
//	}

	private MultiTouchInputComponent mtInput;
	//private NetworkedInputClient nic;
	private IMultiTouchInputSource source;
	private List<IUpdateable> updateList = new ArrayList<IUpdateable>();
	private IMultiplicityApp app;

	public static AssetManager assetManager;

	public static AssetManager getSharedAssetManager() {
		return assetManager;
	}
	
	public MultiplicityClient(IMultiplicityApp app) {
		super();
		this.app = app;
	}

	public void handleError(String errMsg, Throwable t) {
		log.severe(errMsg);
		log.severe("Exception was: " + t);
		for(StackTraceElement ste : t.getStackTrace()) {
			log.severe(ste.toString());
		}
	}

	@Override
	public void simpleInitApp() {		
		assetManager = this.getAssetManager();
		guiNode.detachAllChildren();
		flyCam.setEnabled(false);
		
		Camera camera = this.getCamera();

		// establish 0,0 in center
		//TODO: manage this through the stage?
		guiNode.setLocalTranslation(getCamera().getWidth()/2, getCamera().getHeight()/2, 0);
//		ItemImpl.transformDelegateParent.setLocalTranslation(guiNode.getLocalTranslation());
		
		
		
		ContentSystem csys = new ContentSystem();
		JMEStage stage = new JMEStage("localstage", UUID.randomUUID(), csys);
		JMEStageDelegate delegate = new JMEStageDelegate(stage);
		stage.setDelegate(delegate);
		//stage.getTransformDelegate().setLocalTranslation(guiNode.getLocalTranslation());
		stage.setZOrder(0);
		
		guiNode.attachChild(delegate.getManipulableSpatial());
		guiNode.attachChild(stage.getTransformDelegate());
		try {
			csys.setContentFactory(new JME3ContentSystemFactory(renderer, audioRenderer, assetManager, updateList));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ContentTypeAlreadyBoundException e1) {
			e1.printStackTrace();
		} catch (ContentTypeInvalidException e1) {
			e1.printStackTrace();
		}

		
		csys.setPickSystem(new ContentSystemPicker(guiNode, camera.getWidth(), camera.getHeight()));
		csys.setAnimationSystem(AnimationSystem.getInstance());
		csys.setDisplayManager(new DisplayManager());				
		csys.getDisplayManager().setDisplayDimensions(camera.getWidth(), camera.getHeight());
		
		DragAndDropSystem dads = new DragAndDropSystem(stage);
		csys.setDragAndDropSystem(dads);
		csys.setBehaviourMaker(new BehaviourMaker(stage));
		csys.getDragAndDropSystem().setPickSystemForApp(csys.getPickSystem());
		
		MultiplicityEnvironment.get().addStage("local", stage);
		
		initMultiTouchInput();

		int displayWidth = getCamera().getWidth();
		int displayHeight = getCamera().getHeight();
		
		source = MultiTouchInputUtility.getInputSource(inputManager, displayWidth, displayHeight);
		mtInput = new MultiTouchInputComponent(source);
		
		mtInput.registerMultiTouchEventListener(new PickedItemDispatcher(rootNode, stage));
		
		app.init(mtInput, this);
		
		//printNode(guiNode);
	}
	
	protected void printNode(Node n) {
		printNode(n, 0);
	}
	
	private void printNode(Node n, int depth) {
		printSpaces(depth);
		System.out.println(n.getName() + " " + n.getClass());
		if(n.getChildren().size() > 0) {
			for(Spatial t : n.getChildren()) {
				if(t instanceof Node) {					
					printNode((Node)t, depth + 3);
				}else{
					printSpaces(depth + 3);
					System.out.println(t.getName() + " " + t.getClass());
				}
			}
		}
	}
	
	private void printSpaces(int n) {
		for(int i = 0; i < n; i++) {
			System.out.print(' ');
		}
	}


	private void initMultiTouchInput() {
		getContext().getMouseInput().setCursorVisible(true);		
	}

	public void simpleUpdate(float tpf){
		try {
			source.update(tpf);
			AnimationSystem.getInstance().update(tpf);
			for(IUpdateable item : updateList) {
				item.update(tpf);
			}
		} catch (MultiTouchInputException e) {
			e.printStackTrace();
		}
	}
}
