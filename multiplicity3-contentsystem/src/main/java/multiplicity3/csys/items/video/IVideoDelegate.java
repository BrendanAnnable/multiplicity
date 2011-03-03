package multiplicity3.csys.items.video;

import multiplicity3.csys.items.item.IItemDelegate;
import multiplicity3.csys.items.video.exceptions.CouldNotPlayVideoException;

public interface IVideoDelegate extends IItemDelegate {
	void setSize(float width, float height);
	void setResource(String resource);
	void startPlaying() throws CouldNotPlayVideoException;
	void stopPlaying();

}
