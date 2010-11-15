package multiplicity.csysng.items;

import java.awt.Color;
import java.awt.Font;

public interface ILabel extends IRectangularItem {
	public void setFont(Font f);
	public void setText(String text);
	public String getText();
	public void setTextColour(Color c);
	public void setUnderlineChars(char... underlineChars);
	public void setAlpha(float alpha);
}
