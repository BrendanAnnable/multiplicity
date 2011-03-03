package multiplicity3.csys.items.image;

import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.ItemImpl;

public class ImageImpl extends ItemImpl implements IImage {
	private IImageDelegate imageDelegate;

	private Vector2f size = new Vector2f();
	
	public ImageImpl(String name, UUID uuid) {
		super(name, uuid);		
	}
	
	public void setDelegate(IImageDelegate delegate) {
		super.setDelegate(delegate);
		this.imageDelegate = delegate;
	}

	@Override
	public void setSize(Vector2f size) {
		this.size = size;
		imageDelegate.setSize(size);
	}

	@Override
	public Vector2f getSize() {
		return size;
	}

	@Override
	public float getWidth() {
		return size.getX();
	}

	@Override
	public float getHeight() {
		return size.getY();
	}

	@Override
	public void setImage(String imageResource) {
		imageDelegate.setImage(imageResource);
	}

	@Override
	public void setSize(float width, float height) {
		this.size.set(width, height);
		imageDelegate.setSize(size);
	}

}
