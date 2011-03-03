package multiplicity.csysng.items.image;

import multiplicity.csysng.items.shapes.IRectangularItem;

public interface IImage extends IRectangularItem {
	public void setImage(String imageResoure);
	public void setSize(float width, float height);
}
