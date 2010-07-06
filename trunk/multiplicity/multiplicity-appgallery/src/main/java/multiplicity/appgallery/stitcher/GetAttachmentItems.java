package multiplicity.appgallery.stitcher;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.imageio.ImageIO;

import multiplicity.app.utils.LocalStorageUtility;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.csysngjme.picking.AccuratePickingUtility;
import multiplicity.csysngjme.picking.PickedSpatial;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.jmeutils.UnitConversion;
import no.uio.intermedia.snomobile.WikiUtility;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Node;

public class GetAttachmentItems extends Thread {

	private final static Logger logger = Logger.getLogger(GetAttachmentItems.class.getName());

	private Vector<IAttachment> attachments;

	private List<IItem> items = null;
	private Vector<Object> itemsToReturn = null;

	private IPage iPage = null;

	private StitcherApp stitcher;

	private String parentContainerName;

	private ArrayList<HotSpotFrame> highlightedFrames = new ArrayList<HotSpotFrame>();

	public GetAttachmentItems(StitcherApp stitcherApp, IPage iPage, Vector<IAttachment> attachments, List<IItem> items, String parentContainerName) {
		this.stitcher = stitcherApp;
		this.attachments = attachments;
		this.iPage = iPage;
		this.items = items;
		this.parentContainerName = parentContainerName;
	}

	/**
	 * Enables to write to a local directory, will which be created if
	 * non-existant
	 * 
	 * @param id
	 * @param org
	 *            .dom4j.Document
	 * @param Directory
	 *            name
	 * @param Version
	 */
	public File writeFileToLocalStorageDir(String filename, String dirName) {
		String targetDirectory = LocalStorageUtility.getLocalWorkingDirectory(stitcher.MULTIPLICITY_SPACE, "").getAbsolutePath() + File.separatorChar + dirName;
		boolean canUpload = false;
		File savedFile = null;
		if (new File(targetDirectory).exists() == false) {
			canUpload = (new File(targetDirectory)).mkdirs();
		} else {
			canUpload = true;
		}

		if (canUpload) {
			stitcher.output_document_path = targetDirectory + File.separatorChar + filename;
			savedFile = new File(stitcher.output_document_path);
		}
		return savedFile;
	}

	public List<IItem> getItems() {
		return items;
	}

	public IImage createPhotoImage(URL url) {

		IImage img;
		img = stitcher.getContentFactory().createImage("photo", UUID.randomUUID());
		img.setImage(url);
		BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
		img.setRelativeScale(0.6f);
		img.setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
		img.addItemListener(new ItemListenerAdapter() {

			@Override
			public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
				String message = "item moved over: ";
				boolean firstFrameFound = false;
				boolean offParent = true;
				Node s = (Node) stitcher.getOrthoNode();
				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);

				if (item.getParentItem().getTreeRootSpatial() instanceof JMEFrame) {
					JMEFrame frame = (JMEFrame) item.getParentItem().getTreeRootSpatial();
					for (PickedSpatial pickedSpatial : spatialsList) {
						if (pickedSpatial.getSpatial().equals(frame.getMaskGeometry())) {
							offParent = false;
							message = message + "it's parent frame SCANS.";
						}
					}
				}

				if (parentContainerName.equals(stitcher.SCAN_NAME) && offParent) {
					for (PickedSpatial pickedSpatial : spatialsList) {
						if ((pickedSpatial.getSpatial().toString()).equals("maskGeometry") && firstFrameFound == false) {
							try {
								Geometry geometry = (Geometry) pickedSpatial.getSpatial();

								if (geometry.getParent() instanceof HotSpotFrame) {
									HotSpotFrame targetFrame = (HotSpotFrame) geometry.getParent();

									if (targetFrame.getName().contains("hotspotf-") && !highlightedFrames.contains(targetFrame)) {
										message = message + targetFrame.getName() + ": let's turn it red";
										firstFrameFound = true;
										IBorder border = targetFrame.getBorder();
										border.setColor(new ColorRGBA(1f, 0f, 0f, 0.6f));
										// targetFrame.setBorder(getHighlightedFrame());
										highlightedFrames.add(targetFrame);
									} else if (highlightedFrames.contains(targetFrame)) {
										message = message + "a hotspot frameprobably already red";
										for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
											if (!hotSpotFrame.equals(targetFrame)) {
												IBorder border = hotSpotFrame.getBorder();
												border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
												// hotSpotFrame.setBorder(getNormalFrame());
												highlightedFrames.remove(hotSpotFrame);
											}
										}
									}
								}
							} catch (Exception e) {
								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
							}
						}
					}
				}

				if (!firstFrameFound && offParent) {
					firstFrameFound = false;
					message = message + "something else than a hotspotframe or the parent (i.e. the mist)";

					for (PickedSpatial pickedSpatial : spatialsList) {
						if ((pickedSpatial.getSpatial().toString()).equals("maskGeometry") && firstFrameFound == false) {
							try {
								Geometry geometry = (Geometry) pickedSpatial.getSpatial();

								if (geometry.getParent() instanceof HotSpotFrame) {
									IHotSpotFrame targetFrame = (IHotSpotFrame) geometry.getParent();
									if (highlightedFrames.contains(targetFrame)) {
										firstFrameFound = true;
										IBorder border = targetFrame.getBorder();
										border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
										// targetFrame.setBorder(getNormalFrame());
										highlightedFrames.remove(targetFrame);
									}
								}
							} catch (Exception e) {
								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
							}
						}
					}

					if (!firstFrameFound) {
						clearAllHighlightedHotSpotFrames();
					}
				}

				logger.info(message);
				stitcher.bumpHotSpotConnections();
			}

			private void clearAllHighlightedHotSpotFrames() {
				for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
					IBorder border = hotSpotFrame.getBorder();
					border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
				}
				highlightedFrames = new ArrayList<HotSpotFrame>();
			}

			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
				logger.info("item pressed" + item.getBehaviours() + "parent: " + item.getParentItem());
			}

			@Override
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				logger.info("item clicked" + item.getBehaviours());
			}

			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				boolean offParent = true;

				Node s = (Node) stitcher.getOrthoNode();

				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);

				if (item.getParentItem().getTreeRootSpatial() instanceof JMEFrame) {
					JMEFrame frame = (JMEFrame) item.getParentItem().getTreeRootSpatial();
					for (PickedSpatial pickedSpatial : spatialsList) {
						if (pickedSpatial.getSpatial().equals(frame.getMaskGeometry())) {
							offParent = false;
						}
					}
				}

				if (parentContainerName.equals(stitcher.BACKGROUND_NAME) && offParent) {
					IFrame frame = (IFrame) item.getParentItem();
					item.removeItemListener(this);

					if (item instanceof IImage) {
						IImage imageItem = (IImage) item;
						// create copy
						IImage copy = createPhotoImage(imageItem.getImageUrl());
						frame.addItem(copy);
					}
					stitcher.moveItemToNewFrame(item, new Vector2f(0.0f, 0.0f), "back-" + item.getUUID());
					frame.removeItem(item);
				} else {
					// check if we are dropping on a "background"
					// frame
					boolean firstFrameFound = false;
					for (PickedSpatial pickedSpatial : spatialsList) {
						if ((pickedSpatial.getSpatial().toString()).equals("maskGeometry") && firstFrameFound == false) {
							try {
								Geometry geometry = (Geometry) pickedSpatial.getSpatial();

								if (geometry.getParent() instanceof HotSpotFrame) {
									HotSpotFrame targetFrame = (HotSpotFrame) geometry.getParent();

									if (!targetFrame.isLocked() && targetFrame.getName().contains("back-") && (parentContainerName.equals(stitcher.SCAN_NAME) || parentContainerName.equals(stitcher.STENCIL_NAME))) {
										firstFrameFound = true;
										IFrame frame = (IFrame) item.getParentItem();
										frame.removeItem(item);

										Vector2f itemWorldPos = item.getWorldLocation();
										item.setRelativeScale(stitcher.INITIAL_DROP_SCALE);
										targetFrame.addItem(item);
										item.setWorldLocation(itemWorldPos);
										targetFrame.getZOrderManager().bringToTop(item, null);

										targetFrame.bringHotSpotsToTop();
										targetFrame.bringPaletToTop();
									} else if (!targetFrame.isLocked() && targetFrame.getName().contains("hotspotf-") && parentContainerName.equals(stitcher.SCAN_NAME)) {
										firstFrameFound = true;
										IFrame frame = (IFrame) item.getParentItem();
										frame.removeItem(item);

										targetFrame.addItem(item);
										item.setRelativeScale(1.0f);
										((JMERectangularItem) item).setSize(stitcher.HOTSPOT_FRAME_DIMENSION, stitcher.HOTSPOT_FRAME_DIMENSION);
										item.centerItem();
										clearAllHighlightedHotSpotFrames();
										targetFrame.getZOrderManager().bringToTop(item, null);

										targetFrame.bringHotSpotsToTop();
										// targetFrame.bringPaletToTop();
									}
								}

							} catch (Exception e) {
								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
							}
						}

					}
					// image was dragged off of the frame DELETE
					if (!firstFrameFound && offParent) {
						logger.info("delete image moved off the frame");

						IFrame frame = (IFrame) item.getParentItem();
						frame.removeItem(item);
					}
				}

				logger.info("cursor released caught event: " + item.getParentItem().getClass());
				stitcher.bumpHotSpotConnections();
			}
		});

		return img;
	}

	public void run() {
		itemsToReturn = new Vector<Object>();

		for (IAttachment iAttachment : attachments) {

			if (iAttachment.isOfImageType()) {
				File file = writeFileToLocalStorageDir(iAttachment.getName(), iPage.getPageName());

				logger.info("File exists: " + file.exists() + " - " + iAttachment.getName() + " - " + iAttachment.getSize() + " - " + file.length());
				if (file.exists() == false) {
					WikiUtility wikiUtility = new WikiUtility(stitcher.wikiUser, stitcher.wikiPass, stitcher.maxFileSize, iAttachment.getMimeType(), iAttachment.getAbsoluteUrl());
					try {
						wikiUtility.start();
						wikiUtility.join();
					} catch (InterruptedException e) {
						logger.debug("getComments:  InterruptedException: " + e);
					}
					Object resourceToDownload = wikiUtility.getResource();

					if (resourceToDownload != null) {
						iAttachment.setIsValid(true);
						try {
							ImageIO.write((RenderedImage) resourceToDownload, iAttachment.getMimeType().substring(6), file);
						} catch (IOException e) {
							logger.debug("IOException: " + e);
						}
					} else {
						iAttachment.setIsValid(false);
					}
				} else {
					iAttachment.setIsValid(true);
					// TODO: check that the file on the disk is at least the
					// same size as the "expected one", if not, download new.
					// udpate: 31/05/2010 - Already attempted comparing
					// iAttachment.getSize() and file.length(), but they appear
					// not to be the same
					// even though the files on disk are valid
				}

				if (iAttachment.getIsValid()) {
					float calculatedScale = 0;
					Vector<Object> entryVector = new Vector<Object>();

					if (iAttachment.getIsValid()) {
						IImage i = null;
						try {
							i = createPhotoImage(file.toURI().toURL());
							Vector2f size = ((JMERectangularItem) i).getSize();
							calculatedScale = getScale(size);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						entryVector.addElement(calculatedScale);
						entryVector.addElement(i);

						itemsToReturn.addElement(entryVector);
					}
				}
			}
		}
	}

	private float getScale(Vector2f size) {
		float scale = 0;
		float width = size.x;
		float height = size.y;

		if ((width / height) < 1) {
			scale = stitcher.MAX_THUMBNAIL_SIDE_SIZE / width;
		} else {
			scale = stitcher.MAX_THUMBNAIL_SIDE_SIZE / height;
		}

		return scale;
	}

	public Vector<Object> getItemsToReturn() {
		return itemsToReturn;
	}

}
