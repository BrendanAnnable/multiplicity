package multiplicity.csysng.items;

import java.awt.Color;
import java.awt.Font;

public interface ILabel extends IRectangularItem {
	public void setFont(Font f);
	public void setText(String text);
	public void setTextColour(Color c);
}
