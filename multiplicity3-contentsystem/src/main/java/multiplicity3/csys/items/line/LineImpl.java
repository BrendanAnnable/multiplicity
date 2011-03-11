package multiplicity3.csys.items.line;

import java.awt.Color;
import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.item.ItemImpl;

public class LineImpl extends ItemImpl implements ILine {

	private IItem source;
	private ILineDelegate lineDelegate;
	private IItem destination;
	private LineMode mode;
	private Vector2f startPosition;
	private Vector2f endPosition;

	public LineImpl(String name, UUID uuid) {
		super(name, uuid);
		this.mode = LineMode.UNLINKED;
	}
	
	public void setDelegate(ILineDelegate delegate) {
		super.setDelegate(delegate);
		this.lineDelegate = delegate;
	}

	@Override
	public void setSourceItem(IItem item) {
		this.source = item;		
		lineDelegate.setSourceItem(item);
		if(this.source != null && this.destination != null) {
			this.mode = LineMode.LINKED;
		}
	}

	@Override
	public IItem getSourceItem() {
		return this.source;
	}

	@Override
	public void setDestinationItem(IItem item) {
		this.destination = item;	
		this.lineDelegate.setDestinationItem(item);
		if(this.source != null && this.destination != null) {
			this.mode = LineMode.LINKED;
		}
	}

	@Override
	public IItem getDestinationItem() {
		return this.destination;
	}

	@Override
	public void setLineColour(Color c) {
		//lineDelegate.setLineColour(c);
	}

	@Override
	public void setLineWidth(float width) {
		lineDelegate.setLineWidth(width);
	}

	@Override
	public float getLength() {
		return destination.getWorldLocation().subtract(source.getWorldLocation()).length();
	}

	@Override
	public LineMode getMode() {
		return mode;
	}

	@Override
	public void setStartPosition(Vector2f v) {
		this.startPosition = v.clone();
		lineDelegate.setStartPosition(v);
	}

	@Override
	public void setEndPosition(Vector2f v) {
		this.endPosition = v.clone();
		lineDelegate.setEndPosition(v);
	}

	@Override
	public Vector2f getStartPosition() {
		return startPosition;
	}

	@Override
	public Vector2f getEndPosition() {
		return endPosition;
	}

}
