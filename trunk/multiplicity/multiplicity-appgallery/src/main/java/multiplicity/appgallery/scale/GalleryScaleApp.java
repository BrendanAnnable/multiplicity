package multiplicity.appgallery.scale;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.behaviours.button.ButtonBehaviour;
import multiplicity.csysng.behaviours.button.IButtonBehaviourListener;
import multiplicity.csysng.behaviours.gesture.GestureDetectionBehaviour;
import multiplicity.csysng.behaviours.gesture.GestureLibrary;
import multiplicity.csysng.behaviours.gesture.GestureMatch;
import multiplicity.csysng.behaviours.gesture.IGestureListener;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.factory.PaletItemFactory;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.items.hotspots.listeners.OverlayMultiTouchListener;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

public class GalleryScaleApp extends AbstractMultiplicityApp {

    public final static int HOTSPOT_DIMENSION = 80;

    public final static int HOTSPOT_FRAME_DIMENSION = 200;

    public final static int PALET_DIMENSION = 35;
    
    public final static Float BORDER_THICKNESS = 40f;

    private HotSpotContentItemFactory hotSpotContentItemFactory;


    private IPaletFactory paletFactory;

    
	public GalleryScaleApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		loadContent();
	}

	private void loadContent() {
		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(GalleryScaleApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		add(bg);

		IColourRectangle rect = getContentFactory().createColourRectangle("cr", UUID.randomUUID(), 100, 50);
		rect.setSolidBackgroundColour(new Color(1.0f, 0f, 0f, 0.8f));
		add(rect);
		BehaviourMaker.addBehaviour(rect, RotateTranslateScaleBehaviour.class);
		


		
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

		GestureDetectionBehaviour gdb = (GestureDetectionBehaviour) BehaviourMaker.addBehaviour(bg, GestureDetectionBehaviour.class);
		gdb.addListener(new IGestureListener() {				
			@Override
			public void gestureDetected(GestureMatch match, IItem item) {				
				if(match.libraryGesture.getName().equals("circle") && match.matchScore > 0.8) {
					addRandomFrame(""+(int)(Math.random() * 10));						
				}
			}
		});

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());		
		add(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
		trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		add(trails);
		

//		addNestedFrameExample();
		
		addHotSpotFrame("wreck.jpg");
		addHotSpotText();
//		addRandomFrame("aotn.jpg");
//		addRandomFrame("fabbey.jpg");
//		addRandomFrame("wreck.jpg");
		
		
		zOrderManager.sendToBottom(bg, null);
		zOrderManager.neverBringToTop(bg);

	}

	private void addHotSpotText() {
        final IHotSpotText hotspotLabel = getHotSpotContentFactory().createEditableHotSpotText("HotSpotLabel", UUID.randomUUID());
        hotspotLabel.setText("Label");
        hotspotLabel.setFont(new Font("Myriad Pro", Font.BOLD, 48*2));
        hotspotLabel.setTextColour(Color.white);
        hotspotLabel.setRelativeLocation(new Vector2f(0, 200));
        //hotspotLabel.setCursorAt(3);
        BehaviourMaker.addBehaviour(hotspotLabel, RotateTranslateScaleBehaviour.class);       
        add(hotspotLabel);
        
        hotspotLabel.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter(){
            
            @Override
            public void cursorPressed(MultiTouchCursorEvent event) {
                super.cursorPressed(event);
                if (hotspotLabel.tap() > 4) {

                    if (hotspotLabel.isKeyboardVisible() == false) {
                        showKeyboard(hotspotLabel);
                    } else {
                        hideKeyboard(hotspotLabel);
                    }
                    
                    hotspotLabel.resetTaps();

                }
            }
            
        });
    }
    
    private void showKeyboard(IHotSpotText hotSpotText) {
        
        IFrame keyboard = hotSpotText.getKeyboard();
        this.add(keyboard);
        BehaviourMaker.addBehaviour(keyboard, RotateTranslateScaleBehaviour.class);
        this.getZOrderManager().bringToTop(keyboard, null);
        hotSpotText.setKeyboardVisible(true);
        
    }
    
    private void hideKeyboard(IHotSpotText hotSpotText) {
        IFrame keyboard = hotSpotText.getKeyboard();
        this.remove(keyboard);
        hotSpotText.setKeyboardVisible(false);
    }
    

    private void addHotSpotFrame(String picture) {
        UUID randomUUID = UUID.randomUUID();
        setHotSpotContentFactory(new HotSpotContentItemFactory());
        setPaletFactory(new PaletItemFactory());

        float xPos = Integer.valueOf(-DisplaySystem.getDisplaySystem().getWidth() / 2 + (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();
        float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();

        IHotSpotFrame newHotSpotFrame = (IHotSpotFrame) this.getHotSpotContentFactory().createHotSpotFrame("hotspotframe" + randomUUID, randomUUID, HOTSPOT_FRAME_DIMENSION, HOTSPOT_FRAME_DIMENSION);

        newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1, 15, new ColorRGBA(0f, 0f, 0f, 0f)));
        newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
        newHotSpotFrame.maintainBorderSizeDuringScale();
        newHotSpotFrame.setRelativeLocation(new Vector2f(xPos, yPos));

        BehaviourMaker.addBehaviour(newHotSpotFrame, RotateTranslateScaleBehaviour.class);
        this.add(newHotSpotFrame);

//        new OverlayMultiTouchListener(newHotSpotFrame.getFrameOverlay());
        
        //set up the palet
        IPalet palet = this.getPaletFactory().createPaletItem("palet",  UUID.randomUUID(), PALET_DIMENSION, new ColorRGBA(0f, 1f, 0f, 1f));
        newHotSpotFrame.addPalet(palet);
        palet.centerItem();
        BehaviourMaker.addBehaviour(palet, RotateTranslateScaleBehaviour.class);
        //add the listener
       
        IImage img = getContentFactory().createImage("photo", UUID.randomUUID());
        img.setImage(GalleryScaleApp.class.getResource(picture));
        img.setRelativeScale(0.8f);
        BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);

        new OverlayMultiTouchListener(newHotSpotFrame.getFrameOverlay());

        newHotSpotFrame.addItem(img);
        newHotSpotFrame.setLocked(true);
        newHotSpotFrame.toggleLock();
        newHotSpotFrame.getPalet().lockPalet(newHotSpotFrame.isLocked());

        newHotSpotFrame.bringPaletToTop();
           
    }
        
        public IPaletFactory getPaletFactory() {
            return paletFactory;
        }

        public void setPaletFactory(IPaletFactory paletFactory) {
            this.paletFactory = paletFactory;
        }

    private HotSpotContentItemFactory getHotSpotContentFactory() {
        return hotSpotContentItemFactory;
    }

    private void setHotSpotContentFactory(
            HotSpotContentItemFactory hotSpotContentItemFactory) {
        this.hotSpotContentItemFactory = hotSpotContentItemFactory;
    }

    private void addNestedFrameExample() {
		IFrame framewrap = getContentFactory().createFrame("superframe", UUID.randomUUID(), 800, 500);		
		framewrap.setBorder(getContentFactory().createRoundedRectangleBorder("superframeborder", UUID.randomUUID(), 20f, 8));
		framewrap.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		framewrap.maintainBorderSizeDuringScale();
		add(framewrap);
		BehaviourMaker.addBehaviour(framewrap, RotateTranslateScaleBehaviour.class);

		IFrame f = getContentFactory().createFrame("innerframe", UUID.randomUUID(), 200, 100);		
		f.setBorder(getContentFactory().createRoundedRectangleBorder("innerframeborder", UUID.randomUUID(), 20f, 8));
		f.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		f.maintainBorderSizeDuringScale();
		framewrap.addItem(f);
		BehaviourMaker.addBehaviour(f, RotateTranslateScaleBehaviour.class);
		

	}

	private void addRandomFrame(String photoResource) {		
		IFrame frame = getContentFactory().createFrame("randomframe", UUID.randomUUID(), 400, 200);		
		frame.setBorder(getContentFactory().createRoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 20f, 8));
		frame.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		add(frame);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		ILabel label2 = getContentFactory().createLabel("label", UUID.randomUUID());
		label2.setText("MultiTouch");
		label2.setFont(new Font("Myriad Pro", Font.BOLD, 24));
		label2.setTextColour(Color.white);
		label2.setRelativeLocation(new Vector2f(10, 10));
		ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(label2, ButtonBehaviour.class);
		bb.addListener(new IButtonBehaviourListener() {
			@Override
			public void buttonClicked(IItem item) {}

			@Override
			public void buttonPressed(IItem item) {}

			@Override
			public void buttonReleased(IItem item) {}

		});


		frame.addItem(label2);

		IImage img = getContentFactory().createImage("photo", UUID.randomUUID());
		img.setImage(GalleryScaleApp.class.getResource(photoResource));
		img.setRelativeScale(0.8f);
		BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
		BehaviourMaker.addBehaviour(img, InertiaBehaviour.class);
		frame.addItem(img);

		zOrderManager.bringToTop(frame, null);
	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(GalleryScaleApp.class);
	}
}

