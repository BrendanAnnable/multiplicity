package multiplicity.csysng.items.video;

import multiplicity.csysng.items.item.IItemDelegate;
import multiplicity.csysng.items.video.exceptions.CouldNotPlayVideoException;

public interface IVideoDelegate extends IItemDelegate {
	void setSize(float width, float height);
	void setResource(String resource);
	void startPlaying() throws CouldNotPlayVideoException;
	void stopPlaying();

}
