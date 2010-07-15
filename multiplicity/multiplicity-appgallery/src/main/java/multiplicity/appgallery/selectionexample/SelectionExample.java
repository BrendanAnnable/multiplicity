package multiplicity.appgallery.selectionexample;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.input.IMultiTouchEventProducer;

import com.jme.math.Vector2f;


public class SelectionExample extends AbstractStandaloneApp {

	public SelectionExample(IMultiTouchEventProducer producer) {
		super(producer);
	}

	@Override
	public void onAppStart() {
	    IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
        bg.setImage(SelectionExample.class.getResource("yellowflowers_1680x1050.png"));
        bg.centerItem();
        add(bg);
	    
	   
		
		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
        cursors.respondToMultiTouchInput(getMultiTouchEventProducer());     
        add(cursors);

        
        ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
        trails.respondToItem(bg);
        trails.setFadingColour(Color.white);
        add(trails);
        
        SelectionMaker smaker = new SelectionMaker(contentFactory);
        this.getMultiTouchEventProducer().registerMultiTouchEventListener(smaker);
        addDefaultSelectableLabel("Abc", 200, 200, smaker);
        addDefaultSelectableLabel("Def", 260, 200, smaker);
        
        getzOrderManager().sendToBottom(bg, null);
        getzOrderManager().neverBringToTop(bg);
	}
	
	private void addDefaultSelectableLabel(String content, float x, float y, SelectionMaker sm) {
		ILabel lbl = getContentFactory().createLabel(content, UUID.randomUUID());
		lbl.setText(content);
		lbl.setRelativeLocation(new Vector2f(x, y));
		lbl.setFont(new Font("Arial", Font.PLAIN, 24));
		lbl.setSize(200, 100);
		lbl.setTextColour(Color.red);
		
		add(lbl);
		sm.register(lbl);
		
		getzOrderManager().bringToTop(lbl, null);
	}
	
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem.startSystem(SelectionExample.class);
	}

}
