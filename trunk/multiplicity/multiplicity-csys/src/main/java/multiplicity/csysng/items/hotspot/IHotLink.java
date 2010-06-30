package multiplicity.csysng.items.hotspot;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public interface IHotLink {

    public abstract void redrawLine(Vector3f[] vertices);

    public void redrawSourceLocation(Vector2f vertex);

    public void redrawTargetLocation(Vector2f vertex);

    public abstract boolean isVisable();

    public abstract void setVisable(boolean isVisable);

}