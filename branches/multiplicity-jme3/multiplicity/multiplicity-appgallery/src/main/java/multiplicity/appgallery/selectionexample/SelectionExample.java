package multiplicity.appgallery.selectionexample;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;


public class SelectionExample extends AbstractMultiplicityApp {

	public SelectionExample(AbstractSurfaceSystem ass, IMultiTouchEventProducer producer) {
		super(ass, producer);
	}

	@Override
	public void onAppStart() {
	    IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
        bg.setImage(SelectionExample.class.getResource("yellowflowers_1680x1050.png"));
        bg.centerItem();
        addItem(bg);
	    
	   
		
		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
        cursors.respondToMultiTouchInput(getMultiTouchEventProducer());     
        addItem(cursors);

        
        ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
        trails.respondToItem(bg);
        trails.setFadingColour(Color.white);
        addItem(trails);
        
        SelectionMaker smaker = new SelectionMaker(this);
        this.getMultiTouchEventProducer().registerMultiTouchEventListener(smaker);
        addDefaultSelectableLabel("Abc", 200, 200, smaker);
        addDefaultSelectableLabel("Def", 260, 200, smaker);
        
        getZOrderManager().sendToBottom(bg, null);
        getZOrderManager().neverBringToTop(bg);
	}
	
	private void addDefaultSelectableLabel(String content, float x, float y, SelectionMaker sm) {
		final ILabel lbl = getContentFactory().createLabel(content, UUID.randomUUID());
		lbl.setText(content);
		lbl.setRelativeLocation(new Vector2f(x, y));
		lbl.setFont(new Font("Arial", Font.PLAIN, 24));
		lbl.setSize(200, 100);
		lbl.setTextColour(Color.red);
		BehaviourMaker.addBehaviour(lbl, RotateTranslateScaleBehaviour.class);
		
		// lets allow removing from a frame... check on release whether still over parent frame or not
		lbl.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {	
				if(lbl.getParentItem() == null) return; // not in a frame
				
				Vector2f screenPos = new Vector2f();
				ContentSystem.getContentSystem().getDisplayManager().tableToScreen(event.getPosition(), screenPos);
				List<IItem> itemsAt = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(screenPos);
				boolean insideframe = false; // the test flag... are we in the frame or not?
				for(IItem x : itemsAt) {
					if(x == lbl.getParentItem()) {
						insideframe = true;
					}
				}
				if(!insideframe) { // we aren't in the frame, so lets remove ourselves and put us back in the app
					Vector2f releasePos = lbl.getWorldLocation();					
					lbl.getParentItem().removeItem(lbl);
					SelectionExample.this.addItem(lbl);
					lbl.setWorldLocation(releasePos);
					SelectionExample.this.getZOrderManager().bringToTop(lbl, null);
				}
			}
		});
		
		addItem(lbl);
		sm.register(lbl);
		
		getZOrderManager().bringToTop(lbl, null);
	}
	
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(SelectionExample.class);
	}

}
