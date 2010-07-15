package multiplicity.app.singleappsystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.exceptions.MultiTouchInputException;

import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.RenderState.StateType;

public class SingleAppTableSystem extends AbstractSurfaceSystem {
	private final static Logger log = Logger.getLogger(SingleAppTableSystem.class.getName());	
	private Node tableSystemOrtho;
	private Class<? extends AbstractStandaloneApp> appClass;
	private AbstractStandaloneApp app;

	public SingleAppTableSystem(Class<? extends AbstractStandaloneApp> appClass) {
		super();
		this.appClass = appClass;
	}

	@Override
	protected void notifyMultiTouchInputException(MultiTouchInputException e) {
		if(app != null) {
			app.notifyMultiTouchInputException(e);
		}else{
			log.error("Error with multitouch input system", e);
		}
	}


	protected void initSurfaceSystem(IMultiTouchEventProducer producer) {
		rootNode.setCullHint(CullHint.Never);

		tableSystemOrtho = new Node("singleapptablesystem[ortho]"); // node holds all ORTHO/2D content
		tableSystemOrtho.setCullHint(CullHint.Never);
		tableSystemOrtho.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		tableSystemOrtho.clearRenderState(StateType.Light);
		orthoNode.attachChild(tableSystemOrtho);

		input = new InputHandler();
		MouseInput.get().setCursorVisible(true);

		try {
			Constructor<? extends AbstractStandaloneApp> con = appClass.getConstructor(IMultiTouchEventProducer.class);
			app = con.newInstance(producer);
			app.setSurfaceSystem(this);
			rootNode.attachChild(app.getThreeDNode());
			tableSystemOrtho.attachChild(app.getOrthoNode());			
			app.onAppStart();						
		} catch (SecurityException e) {
			// TODO propagate?
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO propagate?
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO propagate?
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO propagate?
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO propagate?
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO propagate?
			e.printStackTrace();
		}
	}

	public static void startSystem(Class<? extends AbstractStandaloneApp> appClass) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem tg = new SingleAppTableSystem(appClass);
		tg.setConfigShowMode(ConfigShowMode.NeverShow);
		tg.start();

	}
}
