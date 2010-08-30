package multiplicity.appgallery.stitcher;

import java.net.URL;
import java.util.UUID;

import com.jme.math.Vector2f;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysngjme.items.JMERectangularItem;

public class StitcherUtils {
    
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
}
