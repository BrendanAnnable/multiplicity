package multiplicity.csysngjme.items.repository;

import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;

public class RepositoryFrame extends JMEFrame implements IRepositoryFrame {

    private final static Logger logger = Logger.getLogger(RepositoryFrame.class.getName());

	private static final long serialVersionUID = 6646308702330551200L;
	private boolean isOpen = false;
    private Vector2f openLocation;
    private Vector2f closeLocation;
    private int taps = 0;
    public RepositoryFrame(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
        
    }
    
    public RepositoryFrame(String name, UUID uuid, int width, int height, Vector2f openLocation, Vector2f closeLocation) {
        super(name, uuid, width, height);
        this.closeLocation = closeLocation;
        this.openLocation = openLocation;
    }
    
    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
        init();
    }
    
    private void init() {
        this.addItemListener(new ItemListenerAdapter(){
            
            @Override
            public void itemCursorClicked(IItem item,
                    MultiTouchCursorEvent event) {
                super.itemCursorClicked(item, event);
                IRepositoryFrame rf = (IRepositoryFrame) item;
                if( rf.tap() == 1 ) {
                    if( rf.isOpen() ) {
                        rf.close();
                        rf.resetTaps();
                    } else {
                        rf.open();
                        rf.resetTaps();
                    }
                }
                
                
            }
        });
    }
//    
//    public void OpenCloseRepo(){
//        if()
//    }

    @Override
    public void close() {
        isOpen = false;
        this.setRelativeLocation(this.getCloseLocation());
        
    }
    @Override
    public void open(){
        isOpen = true;
        this.setRelativeLocation(this.getOpenLocation());
    }

    @Override
    public void setOpenLocation(Vector2f openLocation) {
        this.openLocation = openLocation;
//        this.open();
    }

    @Override
    public Vector2f getOpenLocation() {
        return openLocation;
    }

    @Override
    public void setCloseLocation(Vector2f closeLocation) {
        this.closeLocation = closeLocation;
//        this.close();
    }

    @Override
    public Vector2f getCloseLocation() {
        return closeLocation;
    }
    
    @Override
    public boolean isOpen() {
        return this.isOpen;
    }


    @Override
    public int tap() {
        logger.info("number of taps on the repos frame: " + taps);
        return ++taps;
    }
    
    public void resetTaps() {
        taps = 0;
    }

}
