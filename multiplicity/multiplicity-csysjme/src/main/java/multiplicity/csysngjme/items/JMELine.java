package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

public class JMELine extends JMELineItem {

    private static final long serialVersionUID = -8078610021819270289L;

    protected Line l;

    protected BlendState diskBlend;

    protected Vector3f[] vertices;

    protected ColorRGBA lineColour = new ColorRGBA(0f, 0f, 0f, .9f);

    protected float lineWidth = 8f;

    protected boolean visible = true;

    public JMELine(String name, UUID uuid, Vector3f[] vertices,
            ColorRGBA lineColour, float lineWidth) {
        super(name, uuid);
        this.vertices = vertices;
        this.lineColour = lineColour;
        this.lineWidth = lineWidth;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            l.setSolidColor(lineColour);
        } else {
            l.setSolidColor(new ColorRGBA(0f, 0f, 0f, 0f));
        }
    }

    @Override
    protected IZOrderManager createZOrderManager() {
        return new SimpleZOrderManager(this);
    }

    @Override
    public Spatial getManipulableSpatial() {
        return l;
    }

    public void changeBackgroundColor(ColorRGBA colorRGBA) {
        l.setSolidColor(colorRGBA);
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void initializeGeometry() {
        l = new Line("Link", vertices, null, null, null);
        l.setSolidColor(lineColour);
        l.setLineWidth(lineWidth);
        l.setAntialiased(true);
        ItemMap.register(l, this);
        l.setModelBound(new OrthogonalBoundingBox());
        l.updateModelBound();
        diskBlend = DisplaySystem.getDisplaySystem().getRenderer()
                .createBlendState();
        diskBlend.setSourceFunction(SourceFunction.SourceAlpha);
        diskBlend
                .setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
        diskBlend.setBlendEnabled(true);
        l.setRenderState(diskBlend);
        attachChild(l);
        updateModelBound();
    }

}
