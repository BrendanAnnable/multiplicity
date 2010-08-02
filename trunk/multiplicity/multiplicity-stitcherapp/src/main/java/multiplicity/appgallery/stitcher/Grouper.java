package multiplicity.appgallery.stitcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.utils.XMLOperations;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.data.CursorPositionRecord;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;

public class Grouper implements IMultiTouchEventListener {
	
	private static final Logger logger = Logger.getLogger(Grouper.class.getName());
	
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
			
			this.addItemsToFrame(selectedItems, event.getPosition());

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

	public void addItemsToFrame(List<IItem> items, Vector2f atPosition) {
		int frameWidth = 600;
		int frameHeight = 600;
		UUID uUID = UUID.randomUUID();
		IFrame frame = app.getContentFactory().createFrame("group", uUID, frameWidth, frameHeight);
		logger.info("Group UUID: "+uUID.toString());
		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 10f, 15));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		frame.setRelativeLocation(atPosition);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		app.add(frame);
		for (IItem item : items) {
			Vector2f itemWorldPos = item.getWorldLocation();
			app.remove(item);			
			frame.addItem(item);
			item.setWorldLocation(itemWorldPos);
		}
		app.getzOrderManager().bringToTop(frame, null);
		
		createXMLRepresentationForGroup(uUID, items);
	}

	private void createXMLRepresentationForGroup(UUID uUID, List<IItem> items) {
		XMLOperations xmlOperations = new XMLOperations(uUID, items);
	}

}
