package multiplicity.appgallery.stitcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.button.ButtonBehaviour;
import multiplicity.csysng.behaviours.button.IButtonBehaviourListener;
import multiplicity.csysng.behaviours.gesture.GestureLibrary;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.events.MultiTouchCursorEvent;
import no.uio.intermedia.snomobile.XWikiRestFulService;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IComment;
import no.uio.intermedia.snomobile.interfaces.IPage;
import no.uio.intermedia.snomobile.interfaces.ITag;

import com.jme.math.Vector2f;

public class StitcherApp extends AbstractStandaloneApp {
    
    private static final Logger logger = Logger.getLogger(StitcherApp.class.getName());

    private IPage wikiPage;
    private Vector<IComment> comments;
    private XWikiRestFulService wikiService;
    private Vector<IAttachment> attachments;
    private Vector<ITag> tags;
    
    //when this is filled the first one is at the top of the z index
    private ArrayList<IItem> zOrderedItems;

	public StitcherApp(IMultiTouchEventProducer mtInput) {
		super(mtInput);
	}

	@Override
	public void onAppStart() {
	    populateFromWiki();
		loadContent();
	}

	private void populateFromWiki() {
	    Properties prop = new Properties();
        
        try {
            prop.load(StitcherApp.class.getResourceAsStream("xwiki.properties"));
        } 
        catch (IOException e) {
            logger.log(Level.SEVERE, "setup:  IOException: "+e);
        }
        wikiService = new XWikiRestFulService(prop);
        wikiPage = wikiService.getWikiPage();
        comments = wikiPage.getComments();
        attachments = wikiPage.getAttachments();
        tags = wikiPage.getTags();

    }

    private void loadContent() {
        
        zOrderedItems = new ArrayList<IItem>();
        Grouper smaker = new Grouper();
        this.getMultiTouchEventProducer().registerMultiTouchEventListener(smaker);
        
		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(StitcherApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		add(bg);

		//load the comments
		for (IComment comment : comments) {
		      ILabel commentLabel = getContentFactory().createLabel("comment", UUID.randomUUID());
		        commentLabel.setText(comment.getText());
		        commentLabel.setFont(new Font("Myriad Pro", Font.BOLD, 24));
		        commentLabel.setTextColour(Color.white);
		        commentLabel.setRelativeLocation(new Vector2f(10, 10));
		        ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(commentLabel, ButtonBehaviour.class);
		        bb.addListener(new IButtonBehaviourListener() {
		            @Override
		            public void buttonClicked(IItem item) {
		                System.out.println("click");                    
		            }

		            @Override
		            public void buttonPressed(IItem item) {
	                      System.out.println("pressed");                    

		            }

		            @Override
		            public void buttonReleased(IItem item) {
                        System.out.println("released");                    

		                
		            }

		        });

		        BehaviourMaker.addBehaviour(commentLabel, RotateTranslateScaleBehaviour.class);
	            BehaviourMaker.addBehaviour(commentLabel, InertiaBehaviour.class);

//	            smaker.register(commentLabel);
	            smaker.register(commentLabel, this);
	            
	            zOrderedItems.add(commentLabel);
		        add(commentLabel);
        }
		
		//load the tags
		  for( ITag tag : tags) {
		      ILabel tagLabel = getContentFactory().createLabel("tag", UUID.randomUUID());
              tagLabel.setText(tag.getName());
              tagLabel.setFont(new Font("Myriad Pro", Font.BOLD, 18));
              tagLabel.setTextColour(Color.BLUE);
              tagLabel.setRelativeLocation(new Vector2f(10, 10));
              ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(tagLabel, ButtonBehaviour.class);
              bb.addListener(new IButtonBehaviourListener() {
                  @Override
                  public void buttonClicked(IItem item) {
                      getzOrderManager().sendToBottom(item, null);             
                  }

                  @Override
                  public void buttonPressed(IItem item) {}

                  @Override
                  public void buttonReleased(IItem item) {}

              });

              BehaviourMaker.addBehaviour(tagLabel, RotateTranslateScaleBehaviour.class);
              BehaviourMaker.addBehaviour(tagLabel, InertiaBehaviour.class);

              //smaker.register(tagLabel);
              smaker.register(tagLabel, this);

              zOrderedItems.add(tagLabel);
              add(tagLabel);
	        }
		
		
		  for (IAttachment iAttachment : attachments) {
		      
		      
		      if(iAttachment.getMimeType().equals("image/png") ||iAttachment.getMimeType().equals("image/jpeg") || iAttachment.getMimeType().equals("image/jpg") || iAttachment.getMimeType().equals("image/gif")) {
		          BufferedImage resource = (BufferedImage) iAttachment.getResource();
	                
	                File file = new File ( iAttachment.getName() );
	                try {
	                    ImageIO.write( resource,  iAttachment.getMimeType().substring(6), file);
	                    IImage img = getContentFactory().createImage("photo", UUID.randomUUID());
	                    img.setImage(file.toURI().toURL());
	                    img.setRelativeScale(0.8f);
//	                    img.addListener(new IButtonBehaviourListener() {
//	                        @Override
//	                        public void buttonClicked(IItem item) {
//	                            getzOrderManager().sendToBottom(item, null);             
//	                        }
//
//	                        @Override
//	                        public void buttonPressed(IItem item) {}
//
//	                        @Override
//	                        public void buttonReleased(IItem item) {}
//
//	                    });
	                    img.addItemListener(new ItemListenerAdapter() {
	                        @Override
	                        public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
	                            System.out.println("item pressed" + item.getBehaviours());
	                        }
	                        
	                        @Override
	                        public void itemCursorClicked(IItem item,
	                                MultiTouchCursorEvent event) {
	                            // TODO Auto-generated method stub
	                               System.out.println("item clicked" + item.getBehaviours());
	                               getzOrderManager().bumpBottom(item);
	                        }
	                    });
	                    BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
	                   // BehaviourMaker.addBehaviour(img, InertiaBehaviour.class);
	                    
	                    //smaker.register(img);
	                    smaker.register(img, this);
	                    
	                    zOrderedItems.add(img);
	                    add(img);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
		          
		      }
		      
	           
	        
		  }

		
		
		
		
		
		IColourRectangle rect = getContentFactory().createColourRectangle("cr", UUID.randomUUID(), 100, 50);
		rect.setSolidBackgroundColour(new Color(1.0f, 0f, 0f, 0.8f));
		add(rect);
		BehaviourMaker.addBehaviour(rect, RotateTranslateScaleBehaviour.class);
		


		
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

//		GestureDetectionBehaviour gdb = (GestureDetectionBehaviour) BehaviourMaker.addBehaviour(bg, GestureDetectionBehaviour.class);
//		gdb.addListener(new IGestureListener() {				
//			@Override
//			public void gestureDetected(GestureMatch match, IItem item) {				
//				if(match.libraryGesture.getName().equals("circle") && match.matchScore > 0.8) {
//				    
//				    
//				    System.out.println("Item children: " + item.getChildrenCount());
//					addRandomFrame(""+(int)(Math.random() * 10));						
//				}
//			}
//		});

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());		
		add(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
		trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		add(trails);
		

//		addNestedFrameExample();
//		addRandomFrame("aotn.jpg");
//		addRandomFrame("fabbey.jpg");
//		addRandomFrame("wreck.jpg");
		
		getzOrderManager().sendToBottom(bg, null);
		getzOrderManager().neverBringToTop(bg);

	}

    /**
     * bumps the item down one. 
     * @param item
     */
    public void bumpDownZOrder(IItem item) {
        
        int indexOf = zOrderedItems.indexOf(item);
        
        //get the one above it
        try {
            IItem swapItem = zOrderedItems.get(indexOf + 1 );
            
            int swapItemZorder = swapItem.getZOrderManager().getItemZOrder();
            int oldZOrder = item.getZOrderManager().getItemZOrder();
            
            
            
            
            item.getZOrderManager().setItemZOrder(swapItemZorder);
            swapItem.getZOrderManager().setItemZOrder(oldZOrder);
            
            //getzOrderManager().
            
            
            //zOrderedItems.remove(indexOf);
            //zOrderedItems.remove(indexOf+1);
            //zOrderedItems.add(indexOf, swapItem);
            //zOrderedItems.add(indexOf+1, item);
        } catch(IndexOutOfBoundsException iobe) {
            
        }
    }

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem.startSystem(StitcherApp.class);
	}
}

