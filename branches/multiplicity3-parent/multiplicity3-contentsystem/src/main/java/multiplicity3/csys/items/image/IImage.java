package multiplicity3.csys.items.image;

import multiplicity3.csys.items.shapes.IRectangularItem;

public interface IImage extends IRectangularItem {
	public void setImage(String imageResoure);
	public void setSize(float width, float height);
}
