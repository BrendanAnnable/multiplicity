package multiplicity.csysng.items;

import java.awt.Color;
import java.awt.Font;

import com.jme.math.Vector2f;

public interface ILine {

    public void setAnnotationEnabled(boolean isEnabled);

    public void setArrowMode(int arrowMode);

    public void setArrowsEnabled(boolean isEnabled);

    public void setLineColour(Color lineColour);

    public void setLineMode(int lineMode);

    public void setSourceItem(IItem sourceItem);

    public void setSourceLocation(Vector2f sourceLocation);

    public void setTargetItem(IItem targetItem);

    public void setTargetLocation(Vector2f targetPoint);

    public void setText(String text);

    public void setTextColour(Color textColour);

    public void setTextFont(Font textFont);

    public void setWidth(float lineWidth);

}
