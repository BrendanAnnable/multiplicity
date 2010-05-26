package multiplicity.csysng.items;

import java.net.URL;

public interface IImage extends IItem {
	public void setImage(URL imageResoure);
	public void setImage(URL resource, float scale);
	public URL getImageUrl();
	public void setAlphaBlending(boolean blendingOn);
	public void setSize(float width, float height);
}
