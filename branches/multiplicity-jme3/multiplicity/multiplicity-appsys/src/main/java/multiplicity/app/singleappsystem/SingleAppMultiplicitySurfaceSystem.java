package multiplicity.app.singleappsystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractMultiplicitySurfaceSystem;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.exceptions.MultiTouchInputException;

import com.jme.input.InputHandler;
import com.jme.input.MouseInput;

public class SingleAppMultiplicitySurfaceSystem extends AbstractMultiplicitySurfaceSystem {
	private final static Logger log = Logger.getLogger(SingleAppMultiplicitySurfaceSystem.class.getName());	
	private Class<? extends AbstractMultiplicityApp> appClass;
	private AbstractMultiplicityApp app;

	public SingleAppMultiplicitySurfaceSystem(Class<? extends AbstractMultiplicityApp> appClass) {
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

	@Override
	protected void initSurfaceSystem(IMultiTouchEventProducer producer) {
		super.initSurfaceSystem(producer);

		input = new InputHandler();
		MouseInput.get().setCursorVisible(true);

		try {
			Constructor<? extends AbstractMultiplicityApp> con = appClass.getConstructor(AbstractSurfaceSystem.class, IMultiTouchEventProducer.class);
			app = con.newInstance(this, producer);
			getSurfaceSystem3DNode().attachChild(app.getThreeDNode());
			getSurfaceSystemOrthoNode().attachChild(app.getOrthoNode());			
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

	public static void startSystem(Class<? extends AbstractMultiplicityApp> appClass) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem tg = new SingleAppMultiplicitySurfaceSystem(appClass);
		tg.setConfigShowMode(ConfigShowMode.NeverShow);
		tg.start();

	}
}
