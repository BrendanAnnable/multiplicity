package multiplicity3.csys.items.shapes;

import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.items.item.ItemImpl;

public class ColourRectangleImpl extends ItemImpl implements IColourRectangle {

	private IColourRectangleDelegate rectangleDelegate;
	private Vector2f size = new Vector2f();

	public ColourRectangleImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IColourRectangleDelegate delegate) {
		super.setDelegate(delegate);
		this.rectangleDelegate = delegate;
	}

	@Override
	public void setSize(float width, float height) {
		this.size.set(width, height);
		rectangleDelegate.setSize(width, height);
	}

	@Override
	public void setSize(Vector2f size) {
		this.size = size;
		rectangleDelegate.setSize(size.x, size.y);
	}

	@Override
	public Vector2f getSize() {
		return this.size;
	}

	@Override
	public float getWidth() {
		return size.x;
	}

	@Override
	public float getHeight() {
		return size.y;
	}

	@Override
	public void setGradientBackground(Gradient g) {
		rectangleDelegate.setGradientBackground(g);
	}

	@Override
	public void setSolidBackgroundColour(ColorRGBA colorRGBA) {
		rectangleDelegate.setSolidBackgroundColour(colorRGBA);
	}

}
