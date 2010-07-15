package multiplicity.appgallery.selectionexample;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.data.CursorPositionRecord;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

import com.jme.math.Vector2f;

public class SelectionMaker implements IMultiTouchEventListener {

	private List<IItem> registeredItems = new ArrayList<IItem>();
    private AbstractStandaloneApp app;

	public SelectionMaker() {
	}
	
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		List<CursorPositionRecord> history = event.getPositionHistory();
		
		   List<IItem> selectedItems = new ArrayList<IItem>();

		
		for(IItem item : registeredItems) {
			boolean selected = contains(history, new Vector2f(item.getWorldLocation()), 50, 50, 100);
			if(selected) { 
			    selectedItems.add(item);
			}
		}
		
		if( !selectedItems.isEmpty() && selectedItems.size() > 1 ) {

		  this.addItemsToFrame(selectedItems); 
		  
		}
		
		
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void register(IItem item) {
		if(!registeredItems.contains(item)) registeredItems.add(item);		
	}
	
	public boolean contains(List<CursorPositionRecord> history, Vector2f p0, float minWidth, float minHeight, float startEndTolerance) {
		Vector2f ti, ti1;
        int crossings = 0;
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        for (int i = 0; i < history.size()-1; i++) {
        	ti = history.get(i).getPosition();
        	// min values
        	if(ti.x < minX) minX = ti.x; if(ti.y < minY) minY = ti.y;
        	// max values
        	if(ti.x > maxX) maxX = ti.x; if(ti.y > maxY) maxY = ti.y;
        	
        	ti1 = history.get(i+1).getPosition();
            double slope = (ti1.x - ti.x)  / (ti1.y - ti.y);
            boolean cond1 = (ti.y <= p0.y) && (p0.y < ti1.y);
            boolean cond2 = (ti1.y <= p0.y) && (p0.y < ti.y);
            boolean cond3 = p0.x <  slope * (p0.y - ti.y) + ti.x;
            if ((cond1 || cond2) && cond3) crossings++;
        }        
        float width = maxX - minX;
        float height = maxY - minY;
        float startEndDistance = history.get(0).getPosition().distance(history.get(history.size()-1).getPosition());
        return (crossings % 2 != 0) && (width > minWidth) && (height > minHeight) && (startEndDistance < startEndTolerance);
    }

    public void register(IItem item, AbstractStandaloneApp app) {
        if(!registeredItems.contains(item)) registeredItems.add(item);
        this.app = app;
    }

    private void addItemsToFrame(List<IItem> items) {     
        IFrame frame = app.getContentFactory().createFrame("randomframe", UUID.randomUUID(), 800, 600);      
        frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1f, 8));
        frame.setGradientBackground(new Gradient(
                new Color(0.5f, 0.5f, 0.5f, 0.8f), 
                new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
        frame.maintainBorderSizeDuringScale();
        app.add(frame);
        BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

       for (IItem item : items) {
           item.setRelativeLocation(new Vector2f(10, 10));
           frame.addItem(item);
          // item.centerItem();
       }
       
       ILabel label2 = app.getContentFactory().createLabel("label", UUID.randomUUID());
       label2.setText("MultiTouch");
       label2.setFont(new Font("Myriad Pro", Font.BOLD, 24));
       label2.setTextColour(Color.white);
       label2.setRelativeLocation(new Vector2f(10, 10));
       
       frame.addItem(label2);
        app.getzOrderManager().bringToTop(frame, null);
    }


}
