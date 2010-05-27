package multiplicity.app.singleappsystem;

import java.util.ArrayList;
import java.util.List;

import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.factory.JMEContentItemFactory;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.utils.JMEUtils;
import multiplicity.csysngjme.zordering.NestedZOrderManager;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;

public abstract class AbstractStandaloneApp {

	public static final int IMAGE_TYPE = 0;

	protected Node orthoNode;
	protected List<IItem> items = new ArrayList<IItem>();
	protected IContentFactory contentFactory;
	protected IZOrderManager zOrderManager;
	private IMultiTouchEventProducer mtInput;

	public AbstractStandaloneApp(IMultiTouchEventProducer producer) {
		this.mtInput = producer;
		orthoNode = new Node(this.getClass().getName() + "_orthonode");
		// by convention, put 0,0 in the middle of the display
		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		orthoNode.setLocalTranslation(r.getWidth() / 2, r.getHeight() / 2, 0);
		contentFactory = new JMEContentItemFactory();
		zOrderManager = new NestedZOrderManager(null, 500);
		getzOrderManager().setItemZOrder(0);
	}

	public IMultiTouchEventProducer getMultiTouchEventProducer() {
		return this.mtInput;
	}

	public void registerForMultiTouch(IMultiTouchEventListener listener) {
		mtInput.registerMultiTouchEventListener(listener);
	}

	public Spatial getOrthoNode() {
		return orthoNode;
	}

	public void add(IItem item, int type) {
		orthoNode.attachChild(item.getTreeRootSpatial());
		items.add(item);
		getzOrderManager().registerForZOrdering(item);
		orthoNode.updateGeometricState(0f, true);
		JMEUtils.dumpItemToConsole(item, this.getClass());

		switch (type) {
		case IMAGE_TYPE:
			JMERectangularItem ji = (JMERectangularItem) item;
			BlendState blend = DisplaySystem.getDisplaySystem().getRenderer()
					.createBlendState();
			blend.setBlendEnabled(true);
			ji.getChild(0).setRenderState(blend);
			break;
		default:
			break;
		}
	}

	public abstract void onAppStart();

	public IContentFactory getContentFactory() {
		return contentFactory;
	}

	public IZOrderManager getzOrderManager() {
		return zOrderManager;
	}
}
