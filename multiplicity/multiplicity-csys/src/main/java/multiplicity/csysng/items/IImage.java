package multiplicity.csysng.items;

import java.net.URL;

public interface IImage extends IRectangularItem {
	public void setImage(URL imageResoure);
	public void setImage(URL resource, float scale);
	public URL getImageUrl();
	public void setAlphaBlending(AlphaStyle blending);
	public void setSize(float width, float height);
	
	public enum AlphaStyle {
		NO_TRANSPARENCY,
		USE_TRANSPARENCY,
		BLEND
	}
}
