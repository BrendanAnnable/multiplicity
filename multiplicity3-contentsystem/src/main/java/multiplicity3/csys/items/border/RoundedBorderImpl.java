package multiplicity3.csys.items.border;

import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.ItemImpl;

public class RoundedBorderImpl extends ItemImpl implements IRoundedBorder {

	protected float borderSize;
	private IRoundedBorderDelegate borderDelegate;
	private Vector2f size = new Vector2f();

	public RoundedBorderImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IRoundedBorderDelegate delegate) {
		super.setDelegate(delegate);
		this.borderDelegate = delegate;
	}

	@Override
	public Vector2f getSize() {
		return size;
	}

	@Override
	public void setSize(Vector2f size) {
		setSize(size.x, size.y);
	}

	@Override
	public void setSize(float width, float height) {
		size.set(width, height);
		borderDelegate.setSize(width, height);
	}

	@Override
	public void setBorderWidth(float borderSize) {
		this.borderSize = borderSize;
		borderDelegate.setBorderWidth(borderSize);
	}

	@Override
	public void setColor(ColorRGBA color) {
		this.borderDelegate.setColor(color);
	}

	@Override
	public float getBorderWidth() {
		return this.borderSize;
	}

}
