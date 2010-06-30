package multiplicity.csysngjme.items;

import java.awt.Color;
import java.awt.Font;
import java.util.UUID;

import multiplicity.csysng.items.IItem;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

public class JMELine extends JMEItem implements ILine {

    public JMELine(String name, UUID uuid) {
        super(name, uuid);
    }

    @Override
    protected void createZOrderManager() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initializeGeometry() {
        // TODO Auto-generated method stub

    }

    @Override
    public Spatial getManipulableSpatial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAnnotationEnabled(boolean isEnabled) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setArrowMode(int arrowMode) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setArrowsEnabled(boolean isEnabled) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLineColour(Color lineColour) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLineMode(int lineMode) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSourceItem(IItem sourceItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSourceLocation(Vector2f sourceLocation) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTargetItem(IItem targetItem) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTargetLocation(Vector2f targetPoint) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setText(String text) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTextColour(Color textColour) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTextFont(Font textFont) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setWidth(float lineWidth) {
        // TODO Auto-generated method stub
        
    }

}
