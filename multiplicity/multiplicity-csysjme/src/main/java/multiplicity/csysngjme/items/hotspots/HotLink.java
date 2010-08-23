package multiplicity.csysngjme.items.hotspots;

import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.items.JMELine;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

public class HotLink extends JMELine implements IHotLink {

    private static final long serialVersionUID = 1677415896709510948L;

    private IHotSpotItem hotSpotItem = null;

    public HotLink(String name, UUID uuid, Vector3f[] vertices,
            ColorRGBA lineColour, float lineWidth, IHotSpotItem hotSpotItem) {
        super(name, uuid, vertices, lineColour, lineWidth);
        this.hotSpotItem = hotSpotItem;
    }

    @Override
    public IHotSpotItem getHotSpotItem() {
        return hotSpotItem;
    }

    @Override
    public void redrawLine(Vector3f[] vertices) {
        if (visible) {
            FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);
            l.reconstruct(fBuffer, null, null, null);
            l.setSolidColor(lineColour);
        }
    }

    @Override
    public void redrawTargetLocation(Vector2f relativeLocation) {
        FloatBuffer fBuffer = BufferUtils.createFloatBuffer(relativeLocation);
        l.reconstruct(fBuffer, null, null, null);
        l.setSolidColor(lineColour);
    }

    @Override
    public void initializeGeometry() {
       super.initializeGeometry();
    }
}
