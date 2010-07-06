package multiplicity.csysngjme.items.hotspots;

import java.util.ArrayList;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEColourCircle;
import multiplicity.csysngjme.items.JMELine;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class HotSpotItem extends JMEColourCircle implements IHotSpotItem {

	private static final long serialVersionUID = 3685342474539036654L;
	private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

	boolean isOpen = true;
	protected String link;
	IHotSpotItem relationHotSpot;
	private IHotSpotFrame hotSpotFrameContent;
	private JMELine hotLink;
	public int clickCount = 0;
	private ColorRGBA colorRGBA = new ColorRGBA(1f, 0f, 0f, 1f);

	public HotSpotItem(String name, UUID uuid, float radius) {
		super(name, uuid, radius);
	}

	public HotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid, radius, colorRGBA);
		this.colorRGBA = colorRGBA;
	}

	@Override
	public void initializeGeometry() {
		super.initializeGeometry();

	}

	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen) {

			// setGradientBackground(new Gradient(
			// Color.RED,
			// new Color(0f, 0f, 0f,1f), GradientDirection.VERTICAL));
			//
			// this.setSolidBackgroundColour(Color.RED);
			this.hotLink.setVisible(true);
			this.hotLink.redrawLine(getLineVertices());
			hotSpotFrameContent.setVisible(true);
		} else {
			this.hotLink.setVisible(false);

			// setGradientBackground(new Gradient(
			// Color.WHITE,
			// new Color(0f, 0f, 0f,1f), GradientDirection.VERTICAL));
			// this.setSolidBackgroundColour(Color.BLACK);
			hotSpotFrameContent.setVisible(false);
		}
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public JMELine createHotLink() {

		Vector3f[] vertices = getLineVertices();

		UUID uuid = UUID.randomUUID();
		hotLink = new JMELine("line-" + uuid, uuid, vertices, colorRGBA, 4f, this);
		hotLink.initializeGeometry();
		hotSpotFrameContent.addHotLink(hotLink);

		this.addItemListener(new ItemListenerAdapter() {

			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				HotSpotItem hs = (HotSpotItem) item;
				hs.clickCount++;
				if (hs.clickCount == 2) {
					hs.setOpen(!hs.isOpen);
					hs.clickCount = 0;
				}
			};

			public void itemMoved(IItem item) {
				Vector3f[] vertices = getLineVertices();
				((HotSpotItem) item).getHotLink().redrawLine(vertices);
			};

		});

		hotSpotFrameContent.addItemListener(new ItemListenerAdapter() {

			public void itemMoved(IItem item) {

				IHotSpotFrame frame = ((IHotSpotFrame) item);
				ArrayList<ILineItem> iLineItems = frame.getHotLinks();

				IHotSpotItem hsi = null;
				for (ILineItem iLine : iLineItems) {
					hsi = iLine.getHotSpotItem();
					Vector3f[] vertices = ((HotSpotItem) hsi).getLineVertices();
					iLine.redrawLine(vertices);
				}

				ArrayList<IHotSpotItem> hotSpots = frame.getHotSpots();
				for (IHotSpotItem iHotSpotItem : hotSpots) {
					Vector3f[] vertices = ((HotSpotItem) iHotSpotItem).getLineVertices();
					((HotSpotItem) iHotSpotItem).getHotLink().redrawLine(vertices);
				}

			};
		});

		return hotLink;
	}

	private Vector3f[] getLineVertices() {
		IHotSpotFrame parentF = (IHotSpotFrame) this.getParentItem();
		Vector2f parentFCoord = parentF.getRelativeLocation();
		// get the worldlocation of hotspot
		Vector2f xyHS1 = new Vector2f(this.getRelativeLocation().x + parentFCoord.x, this.getRelativeLocation().y + parentFCoord.y);
		// get the worldlocation of hotspot
		Vector2f xyHS2 = hotSpotFrameContent.getRelativeLocation();

		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
		vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
		return vertices;
	}

	public void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent) {
		this.hotSpotFrameContent = hotSpotFrameContent;
	}

	public IHotSpotFrame getHotSpotFrameContent() {
		return hotSpotFrameContent;
	}

	public void setHotLink(JMELine hotLink) {
		this.hotLink = hotLink;
	}

	public JMELine getHotLink() {
		return hotLink;
	}

	@Override
	public void update(Vector2f frameLocation) {
//		Vector2f HSLocation = this.getRelativeLocation();
//		Vector2f xyHS1 = new Vector2f(frameLocation.x + HSLocation.x, frameLocation.y + HSLocation.y);
//		hotLink.redrawTargetLocation(xyHS1);
		Vector3f[] vertices = this.getLineVertices();
		this.getHotLink().redrawLine(vertices);
	}

}
