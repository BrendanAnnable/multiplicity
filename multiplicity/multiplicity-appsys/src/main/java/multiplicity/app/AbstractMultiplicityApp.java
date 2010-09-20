package multiplicity.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.IUpdateable;
import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.threedee.IThreeDeeContent;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.zordering.NestedZOrderManager;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.exceptions.MultiTouchInputException;

import com.jme.scene.Node;

public abstract class AbstractMultiplicityApp {
	private final static Logger log = Logger.getLogger(AbstractMultiplicityApp.class.getName());	
	
	protected Node orthoNode;
	protected Node threeDNode;
	protected List<IItem> items = new ArrayList<IItem>();
	protected IZOrderManager zOrderManager;
	private IMultiTouchEventProducer mtInput;
	private AbstractSurfaceSystem surfaceSystem;

	public AbstractMultiplicityApp(AbstractSurfaceSystem surfaceSystem, IMultiTouchEventProducer producer) {
		this.surfaceSystem = surfaceSystem;
		this.mtInput = producer;
		orthoNode = new Node(this.getClass().getName() + "_orthonode");
		threeDNode = new Node(this.getClass().getName() + "_3dnode");
		
		zOrderManager = new NestedZOrderManager(null, 1000);
		getZOrderManager().setItemZOrder(0);	
	}

	public IMultiTouchEventProducer getMultiTouchEventProducer() {
		return this.mtInput;
	}

	public void registerForMultiTouch(IMultiTouchEventListener listener) {
		mtInput.registerMultiTouchEventListener(listener);
	}
	
	public void registerForUpdating(IUpdateable updateable) {
		surfaceSystem.registerForUpdating(updateable);
	}
	
	public void unregisterForUpdating(IUpdateable updateable) {
		surfaceSystem.unregisterForUpdating(updateable);
	}

	public Node getOrthoNode() {
		return orthoNode;
	}
	
	public Node getThreeDNode() {
		return threeDNode;
	}

	public void add(IItem item) {
		orthoNode.attachChild(item.getTreeRootSpatial());
		items.add(item);
		getZOrderManager().registerForZOrdering(item);
		getZOrderManager().updateZOrdering();
		orthoNode.updateGeometricState(0f, true);
	}
	
	public void addThreeDeeItem(IThreeDeeContent item) {
		getThreeDNode().attachChild(item.getSpatial());
	}
	
	public void remove(List<IItem> items) {
		for(IItem i : items) {
			remove(i);
		}
	}
	
	public void remove(IItem... someItems) {
		for(IItem i : someItems) {
			remove(i);
		}
	}
	
	public void remove(IItem i) {
		orthoNode.detachChild(i.getTreeRootSpatial());
		items.remove(i);
		getZOrderManager().unregisterForZOrdering(i);
		orthoNode.updateGeometricState(0f, true);
	}

	public abstract void onAppStart();
	
	public void notifyMultiTouchInputException(MultiTouchInputException e) {
		log.error("Error with multitouch input.", e);
	}

	public IContentFactory getContentFactory() {
		return ContentSystem.getContentSystem().getContentFactory();
	}

	public IZOrderManager getZOrderManager() {
		return zOrderManager;
	}

	public AbstractSurfaceSystem getSurfaceSystem() {
		return surfaceSystem;
	}

}
