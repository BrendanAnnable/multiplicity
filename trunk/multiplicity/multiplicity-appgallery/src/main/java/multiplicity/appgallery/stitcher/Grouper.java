package multiplicity.appgallery.stitcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.Display;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.utils.JMEUtils;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.data.CursorPositionRecord;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

import com.jme.math.Vector2f;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;

public class Grouper implements IMultiTouchEventListener {
	
	private static Logger logger = Logger.getLogger(Grouper.class.getName());
	private List<IItem> registeredItems = new ArrayList<IItem>();
	private AbstractStandaloneApp app;

	public Grouper() {
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		List<CursorPositionRecord> history = event.getPositionHistory();

		List<IItem> selectedItems = new ArrayList<IItem>();

		for (IItem item : registeredItems) {
			boolean selected = contains(history, new Vector2f(item
					.getWorldLocation()), 50, 50, 100);
			if (selected) {
				selectedItems.add(item);
				System.out.println(item.getManipulableSpatial().getName()
						+ " is selected!");
			}
		}

		if (!selectedItems.isEmpty() /* && selectedItems.size() > 1 */) {

			this.addItemsToFrame(selectedItems);

		}

	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub

	}

	public void register(IItem item) {
		if (!registeredItems.contains(item))
			registeredItems.add(item);
	}

	public boolean contains(List<CursorPositionRecord> history, Vector2f p0,
			float minWidth, float minHeight, float startEndTolerance) {
		Vector2f ti, ti1;
		int crossings = 0;
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		for (int i = 0; i < history.size() - 1; i++) {
			ti = history.get(i).getPosition();
			// min values
			if (ti.x < minX)
				minX = ti.x;
			if (ti.y < minY)
				minY = ti.y;
			// max values
			if (ti.x > maxX)
				maxX = ti.x;
			if (ti.y > maxY)
				maxY = ti.y;

			ti1 = history.get(i + 1).getPosition();
			double slope = (ti1.x - ti.x) / (ti1.y - ti.y);
			boolean cond1 = (ti.y <= p0.y) && (p0.y < ti1.y);
			boolean cond2 = (ti1.y <= p0.y) && (p0.y < ti.y);
			boolean cond3 = p0.x < slope * (p0.y - ti.y) + ti.x;
			if ((cond1 || cond2) && cond3)
				crossings++;
		}
		float width = maxX - minX;
		float height = maxY - minY;
		float startEndDistance = history.get(0).getPosition().distance(
				history.get(history.size() - 1).getPosition());
		return (crossings % 2 != 0) && (width > minWidth)
				&& (height > minHeight)
				&& (startEndDistance < startEndTolerance);
	}

	public void register(IItem item, AbstractStandaloneApp app) {
		if (!registeredItems.contains(item))
			registeredItems.add(item);
		this.app = app;
	}

	private void addItemsToFrame(List<IItem> items) {
		IFrame frame = app.getContentFactory().createFrame("group",
				UUID.randomUUID(), 600, 600);
		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID
				.randomUUID(), 1f, 15));
		frame
				.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f,
						0.8f), new Color(0f, 0f, 0f, 0.8f),
						GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		app.add(frame,-1);
		for (IItem item : items) {

			JMERectangularItem ji = (JMERectangularItem) item;
			//put the object right in the center of the new frame
			ji.setLocalTranslation(0.0f, 0.0f, 0.0f);

			JMEUtils.dumpItemToConsole(ji, this.getClass());
			frame.add(ji);
			logger.info(ji.getChildIndex(ji.getManipulableSpatial()));
			ji.getChild(0).setZOrder(2, true);

			logger.info(ji.getChild(0).getRenderState(RenderState.StateType.Blend));
			logger.info("JI Z: "+ji.getZOrder()+" - "+ji.getName());
			JMEUtils.dumpItemToConsole(ji, this.getClass());
		}

		// ILabel label2 = app.getContentFactory().createLabel("label",
		// UUID.randomUUID());
		// label2.setText("MultiTouch");
		// label2.setFont(new Font("Myriad Pro", Font.BOLD, 24));
		// label2.setTextColour(Color.white);
		// label2.setRelativeLocation(new Vector2f(10, 10));
		// BehaviourMaker.addBehaviour(label2,
		// RotateTranslateScaleBehaviour.class);
		// frame.add(label2);
		app.getzOrderManager().bringToTop(frame, null);

	}

}