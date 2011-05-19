package multiplicity3.appsystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import multiplicity3.appsystem.jme.JMEAppRoot;
import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.IUpdateable;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.display.DisplayManager;
import multiplicity3.csys.draganddrop.DragAndDropSystem;
import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.exceptions.MultiTouchInputException;
import multiplicity3.jme3csys.factory.JME3ContentSystemFactory;
import multiplicity3.jme3csys.items.stage.JMEStage;
import multiplicity3.jme3csys.items.stage.JMEStageDelegate;
import multiplicity3.jme3csys.picking.ContentSystemPicker;
import multiplicity3.jme3csys.picking.PickedItemDispatcher;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MultiplicityClient extends JMEAppRoot implements IQueueOwner {

	private static final Logger log = Logger.getLogger(MultiplicityClient.class.getName());

	private MultiTouchInputComponent mtInput;
	private IMultiTouchInputSource source;
	private List<IUpdateable> updateList = new ArrayList<IUpdateable>();
	private IMultiplicityApp currentApp;

	public static AssetManager assetManager;

	private static MultiplicityClient instance;

	public static AssetManager getSharedAssetManager() {
		return assetManager;
	}

	public static MultiplicityClient get() {
		synchronized(MultiplicityClient.class) {
			if(instance == null) instance = new MultiplicityClient();
			return instance;
		}
	}

	private MultiplicityClient() {
		super();		
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
		multiplicityRootNode.detachAllChildren();

		Camera camera = this.getCamera();

		// establish 0,0 in center
		//TODO: manage this through the stage?
		multiplicityRootNode.setLocalTranslation(getCamera().getWidth()/2, getCamera().getHeight()/2, 0);		

		ContentSystem csys = new ContentSystem();
		JMEStage stage = new JMEStage("localstage", UUID.randomUUID(), csys);
		JMEStageDelegate delegate = new JMEStageDelegate(stage);
		stage.setDelegate(delegate);
		stage.setZOrder(0);

		multiplicityRootNode.attachChild(delegate.getManipulableSpatial());
		multiplicityRootNode.attachChild(stage.getTransformDelegate());
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


		csys.setPickSystem(new ContentSystemPicker(multiplicityRootNode, camera.getWidth(), camera.getHeight()));
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
		mtInput.registerMultiTouchEventListener(new PickedItemDispatcher(multiplicityRootNode, stage));

		getInputManager().setCursorVisible(source.requiresMouseDisplay());

		//printNode(guiNode);
	}

	public void setCurrentApp(final IMultiplicityApp app) {
		this.enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if(currentApp == app) return null;				
				if(currentApp != null) {
					currentApp.shouldStop();
					for(int i = 0; i < MultiplicityEnvironment.get().getLocalStages().size(); i++) {
						IStage stage = MultiplicityEnvironment.get().getLocalStages().get(i);
						stage.removeAllItems(true);						
					}
				}
				log.info("Starting application " + app.getFriendlyAppName());
				app.shouldStart(mtInput, MultiplicityClient.this);

				currentApp = app;

				return null;
			}

		});

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
	
	public File getHomeDirectoryForApp(IMultiplicityApp app, boolean shouldCreateIfDoesNotExist) {
		File appHomeDirectory = new File(getMultiplicityAppsDirectory(), app.getFriendlyAppName());
		if(shouldCreateIfDoesNotExist)
			appHomeDirectory.mkdirs();
		return appHomeDirectory;
	}

	private File getMultiplicityAppsDirectory() {
		return new File(getMultiplicityHomeDirectory(), "applications");
	}

	private File getMultiplicityHomeDirectory() {
		return new File(System.getProperty("user.home"), ".multiplicity3");
	}
	
    public void destroy() {
    	if(currentApp != null) {
    		currentApp.shouldStop();
    	}
    	super.destroy();
    }
	
}
