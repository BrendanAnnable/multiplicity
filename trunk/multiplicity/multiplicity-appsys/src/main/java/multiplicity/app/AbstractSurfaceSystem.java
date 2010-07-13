package multiplicity.app;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity.app.utils.CameraUtility;
import multiplicity.app.utils.KeyboardInputUtility;
import multiplicity.app.utils.MultiTouchInputUtility;
import multiplicity.app.utils.StatsUtility;
import multiplicity.config.developer.DeveloperPreferences;
import multiplicity.config.display.DisplayPreferences;
import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysngjme.picking.PickedItemDispatcher;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.MultiTouchInputComponent;
import multiplicity.input.exceptions.MultiTouchInputException;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.BaseGame;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.Debug;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;
import com.jme.util.stat.StatCollector;
import com.jmex.audio.AudioSystem;

public abstract class AbstractSurfaceSystem extends BaseGame {

	private static final Logger logger = Logger.getLogger(AbstractSurfaceSystem.class.getName());
	protected Camera cam;
	protected Node rootNode;
	protected InputHandler input;
	protected Timer timer;
	protected Node statNode;
	protected Node graphNode;
	protected float tpf;
	protected boolean showDepth = false;
	protected boolean showBounds = false;
	protected boolean showNormals = false;
	protected boolean showGraphs = false;
	protected WireframeState wireState;
	protected LightState lightState;
	protected boolean pause;
	protected boolean updateSceneMonitor;
	protected KeyboardInputUtility keyInput;
	protected StatsUtility stats;
	protected MultiTouchInputComponent mtInput;
	protected PickedItemDispatcher pickedItemDispatcher;
	protected IMultiTouchInputSource inputSource;

	public AbstractSurfaceSystem() {
		keyInput = new KeyboardInputUtility(this);		
		System.setProperty("jme.stats", "set");
	}

	protected void initSystem() throws JmeException {
		logger.info(getVersion());
		keyInput.setupBindings();		
		try {
			DisplayPreferences dp = new DisplayPreferences();
			display = DisplaySystem.getDisplaySystem(dp.getDisplayRenderer() );			
			display.setMinDepthBits( dp.getDepthBits() );
			display.setMinStencilBits( dp.getStencilBits() );
			display.setMinAlphaBits( dp.getAlphaBits() );
			display.setMinSamples( dp.getMinimumAntiAliasSamples() );
			display.createWindow(
					dp.getWidth(), dp.getHeight(), 
					dp.getBitsPerPixel(), display.getFrequency(), dp.getFullScreen());
			logger.info("Running on: " + display.getAdapter()
					+ "\nDriver version: " + display.getDriverVersion() + "\n"
					+ display.getDisplayVendor() + " - "
					+ display.getDisplayRenderer() + " - "
					+ display.getDisplayAPIVersion());

			cam = display.getRenderer().createCamera( display.getWidth(), display.getHeight() );
		} catch ( JmeException e ) {
			logger.log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit( 1 );
		}

		display.getRenderer().setBackgroundColor( ColorRGBA.black.clone() );
		CameraUtility.cameraPerspective(cam);
		Vector3f loc = new Vector3f( 0.0f, 0.0f, 25.0f );
		Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
		Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
		Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
		cam.setFrame( loc, left, up, dir );
		cam.update();
		display.getRenderer().setCamera( cam );
		FirstPersonHandler firstPersonHandler = new FirstPersonHandler( cam, 50, 1 );
		input = firstPersonHandler;

		timer = Timer.getTimer();
		String className = getClass().getName();
		if ( className.lastIndexOf( '.' ) > 0 ) className = className.substring( className.lastIndexOf( '.' )+1 );
		display.setTitle( className );
	}

	
	
	private void setupMultiTouchInput() {		
		inputSource = MultiTouchInputUtility.getInputSource();
		mtInput = new MultiTouchInputComponent(inputSource);
		pickedItemDispatcher = new PickedItemDispatcher(rootNode);
		mtInput.registerMultiTouchEventListener(pickedItemDispatcher);
	}

	protected void initGame() {
		rootNode = new Node( "rootNode" );
		wireState = display.getRenderer().createWireframeState();
		wireState.setEnabled( false );
		rootNode.setRenderState( wireState );

		/**
		 * Create a ZBuffer to display pixels closest to the camera above
		 * farther ones.
		 */
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled( true );
		buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo );
		rootNode.setRenderState( buf );

		statNode = new Node( "Stats node" );
		statNode.setCullHint( Spatial.CullHint.Never );
		statNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		if (Debug.stats) {
			graphNode = new Node( "Graph node" );
			graphNode.setCullHint( Spatial.CullHint.Never );
			statNode.attachChild(graphNode);
			stats = new StatsUtility(graphNode);
			stats.setupStatGraphs();
			stats.setupStats();
		}

		PointLight light = new PointLight();
		light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
		light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
		light.setLocation( new Vector3f( 100, 100, 100 ) );
		light.setEnabled( true );

		lightState = display.getRenderer().createLightState();
		lightState.setEnabled( true );
		lightState.attach( light );
		rootNode.setRenderState( lightState );

		setupMultiTouchInput();
		initSurfaceSystem(mtInput);

		timer.reset();

		rootNode.updateGeometricState( 0.0f, true );
		rootNode.updateRenderState();
		statNode.updateGeometricState( 0.0f, true );
		statNode.updateRenderState();

		if(new DeveloperPreferences().getShowSceneMonitor()) {
			SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
			SceneMonitor.getMonitor().showViewer(true);
			updateSceneMonitor = true;
		}

		
		
		timer.reset();
	}

	protected void update( float interpolation ) {
		/** Recalculate the framerate. */
		timer.update();
		/** Update tpf to time per frame according to the Timer. */
		tpf = timer.getTimePerFrame();

		if(inputSource != null) {			
			try {
				inputSource.update(tpf);
			} catch (MultiTouchInputException e) {
				notifyMultiTouchInputException(e);
			}
		}
		input.update( tpf );
		
		AnimationSystem.getInstance().update(tpf);
		
		if (Debug.stats) {
			StatCollector.update();
		}
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

		keyInput.processKeyboard(interpolation);

		if ( !pause ) {
			simpleUpdate();
			rootNode.updateGeometricState(tpf, true);
			statNode.updateGeometricState(tpf, true);
		}

		if(updateSceneMonitor) {
			SceneMonitor.getMonitor().updateViewer(tpf);
		}
	}
	

	public void addToUpdateCycle(Callable<?> callable) {
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(callable);
	}

	protected void render( float interpolation ) {
		Renderer r = display.getRenderer();
		r.clearBuffers();
		r.clearStencilBuffer();
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();
		r.draw(rootNode);
		r.draw(statNode);
		doDebug(r);
		simpleRender();
	}

	protected void doDebug(Renderer r) {
		if(showBounds) {
			Debugger.drawBounds( rootNode, r, true );
		}

		if(showNormals) {
			Debugger.drawNormals( rootNode, r );
			Debugger.drawTangents( rootNode, r );
		}

		if (showDepth) {
			r.renderQueue();
			Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, r);
		}
	}

	public void simpleUpdate() {}
	protected void simpleRender() {}
	protected void reinit() {}

	protected void cleanup() {
		logger.info( "Cleaning up resources." );

		TextureManager.doTextureCleanup();
		if (display != null && display.getRenderer() != null)
			display.getRenderer().cleanup();
		KeyInput.destroyIfInitalized();
		MouseInput.destroyIfInitalized();
		JoystickInput.destroyIfInitalized();
		if (AudioSystem.isCreated()) {
			AudioSystem.getSystem().cleanup();
		}
	}

	protected void quit() {
		super.quit();
		System.exit( 0 );
	}





	protected abstract void initSurfaceSystem(IMultiTouchEventProducer producer);
	protected abstract void notifyMultiTouchInputException(MultiTouchInputException e);


	public Node 	getRootNode() 					{ return rootNode; }

	public boolean 	isPaused() 						{ return pause; }
	public void 	setPaused(boolean b) 			{ this.pause = b; }

	public boolean 	isDrawingWireframe() 			{ return wireState.isEnabled(); }
	public void 	setDrawingWireframe(boolean b) 	{ wireState.setEnabled(b); rootNode.updateRenderState(); }

	public boolean 	isLightingEnabled()				{ return lightState.isEnabled(); }
	public void 	setLightingEnabled(boolean b) 	{ lightState.setEnabled(b); rootNode.updateRenderState(); }

	public boolean 	isDrawingBounding() 			{ return showBounds; }
	public void 	setDrawingBounding(boolean b) 	{ showBounds = b; }

	public boolean 	isShowingDepthBuffer() 			{ return showDepth; }
	public void 	setShowDepthBuffer(boolean b) 	{ showDepth = b; }

	public boolean 	isDrawingNormals() 				{ return showNormals; }
	public void 	setDrawingNormals(boolean b) 	{ showNormals = b; }

	public Camera 	getCamera() 					{ return cam; }

	public boolean 	isDrawingStats() 				{ return showGraphs; }
	public void 	setDrawingStats(boolean b) 		{ showGraphs = !showGraphs; Debug.updateGraphs = showGraphs; stats.clearControllers(); stats.newControllers(showGraphs); }

}
