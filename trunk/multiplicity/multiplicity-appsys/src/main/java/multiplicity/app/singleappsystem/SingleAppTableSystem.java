package multiplicity.app.singleappsystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.input.IMultiTouchEventProducer;

import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.RenderState.StateType;

public class SingleAppTableSystem extends AbstractSurfaceSystem {

	private Node tableSystemOrtho;
	private Class<? extends AbstractStandaloneApp> appClass;

	public SingleAppTableSystem(Class<? extends AbstractStandaloneApp> appClass) {
		super();
		this.appClass = appClass;
	}

	protected void initSurfaceSystem(IMultiTouchEventProducer producer) {
		rootNode.setCullHint(CullHint.Never);
		rootNode.clearRenderState(StateType.Light);
				
		tableSystemOrtho = new Node("singleapptablesystem[ortho]"); // node holds all ORTHO/2D content
		tableSystemOrtho.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		rootNode.attachChild(tableSystemOrtho);
		
		input = new InputHandler();
		MouseInput.get().setCursorVisible(true);
		
		try {
			Constructor<? extends AbstractStandaloneApp> con = appClass.getConstructor(IMultiTouchEventProducer.class);
			AbstractStandaloneApp app = con.newInstance(producer);
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
