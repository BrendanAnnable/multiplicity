package multiplicity.appgallery.stitcher;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import multiplicity.appgallery.stitcher.listeners.HotLinkBehavior;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.hotspots.HotSpotTextFrame;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;

import com.jme.math.Vector2f;

public class StitcherUtils {
    
    public static StitcherApp stitcherApp;
    public static String wikiUser = null;
    public static String wikiPass = null;
    public static int maxFileSize = 0;

    
    public static IImage createPhotoImage(URL url) {
            IImage img = ContentSystem.getContentSystem().getContentFactory().createImage(IStitcherContants.IMAGE, UUID.randomUUID());
            img.setImage(url);
            //img.setRelativeScale(0.6f);
            img.setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
            BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
            return img;
    }
    
    public static Vector2f generateRandomPosition(IFrame frame, IImage vecItem) {
        Vector2f frameSize = frame.getSize();
        Vector2f imageSize = ((JMERectangularItem) vecItem).getSize();

        float lowerBoundX = -frameSize.x / 2 + imageSize.x / 2;
        float upperBoundX = frameSize.x / 2 - imageSize.x / 2;

        float lowerBoundY = -frameSize.y / 2 + imageSize.y / 2;
        float upperBoundY = frameSize.y / 2 - imageSize.y / 2;

        float posX = (float) (lowerBoundX + (Math.random() * (upperBoundX - lowerBoundX)));
        float posY = (float) (lowerBoundY + (Math.random() * (upperBoundY - lowerBoundY)));

        return new Vector2f(posX, posY);
    }

    public static float getScale(Vector2f size) {
        float scale = 0;
        float width = size.x;
        float height = size.y;

        if ((width / height) < 1) {
            scale = IStitcherContants.MAX_THUMBNAIL_SIDE_SIZE / width;
        } else {
            scale = IStitcherContants.MAX_THUMBNAIL_SIDE_SIZE / height;
        }

        return scale;
    }
    
    public static void showKeyboard(IHotSpotText hotSpotText) {
        
        IFrame keyboard = hotSpotText.getKeyboard();
        stitcherApp.add(keyboard);
        stitcherApp.getZOrderManager().bringToTop(keyboard, null);
        hotSpotText.setKeyboardVisible(true);
        
    }
    
    public static void modScaleBehavior(List<IBehaviour> behaviours, boolean shouldScale) {
        for (Iterator iterator = behaviours.iterator(); iterator.hasNext();) {
            IBehaviour iBehaviour = (IBehaviour) iterator.next();
            if( iBehaviour instanceof RotateTranslateScaleBehaviour ) {
                ((RotateTranslateScaleBehaviour)iBehaviour).setScaleEnabled(shouldScale);
            }
        }
    }
    
    public static void hideKeyboard(IHotSpotText hotSpotText) {
        IFrame keyboard = hotSpotText.getKeyboard();
        stitcherApp.remove(keyboard);
        hotSpotText.setKeyboardVisible(false);
    }

    public static void bringToTop(IItem item) {
        stitcherApp.getZOrderManager().bringToTop(item, null);
    }

    public static void createBackgroundFrame(IImage copyImage) {
        stitcherApp.createNewFrame(copyImage, new Vector2f(0.0f,
                0.0f), IStitcherContants.BACKGROUND + "-"
                + copyImage.getUUID(),
                IStitcherContants.BACKGROUND);
    }

    public static void removeHotThing(IItem hotThing) {
        if( hotThing instanceof IHotLink ) {
            stitcherApp.remove(hotThing);
        } else if(hotThing instanceof IHotSpotFrame) {
            stitcherApp.getHotSpotFrames().remove(hotThing);
            stitcherApp.remove(hotThing);
        }
    }

    public static IHotSpotFrame createNewHotSpotFrame(String type) {
        return stitcherApp.createNewHotSpotContentFrame(type);
    }

    public static void fillHotSpotRepo(IFrame originFrame, String type) {
        stitcherApp.fillHotSpotRepo(originFrame,type);
    }

    public static void addHotSpotContentFrame(IHotSpotFrame hotSpotFrameContent) {
        stitcherApp.getHotSpotFrames().add(hotSpotFrameContent);        
    }

    public static void addHotLink(IHotLink hotLink) {
        stitcherApp.add(hotLink);
//        BehaviourMaker.addBehaviour(hotLink, HotLinkBehavior.class);
    }

    public static void updateHotShotContentFrames() {
        for (IHotSpotFrame hsFrame : stitcherApp.getHotSpotFrames()) {
            if( hsFrame.hasPalet() )
                hsFrame.bringPaletToTop();
        }
    }
    
    public static void updateAllHotLinkConnections() {
        
        HotSpotUtils.updateAllHotLinkConnections(stitcherApp.getHotSpotFrames());
    }
}
