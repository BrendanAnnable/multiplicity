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
import multiplicity.appgallery.stitcher.listeners.ImageMultiTouchListener;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
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
import com.jme.scene.Spatial;

public class AttachmentFetchThread extends Thread {

	private final static Logger logger = Logger.getLogger(AttachmentFetchThread.class.getName());

	private Vector<IAttachment> attachments;

	private List<IItem> items = null;
	private Vector<Object> itemsToReturn = null;

	private IPage iPage = null;

	private StitcherApp stitcherApp;
	private String parentContainerName;

	private ArrayList<HotSpotFrame> highlightedFrames = new ArrayList<HotSpotFrame>();

	public AttachmentFetchThread(StitcherApp stitcherApp, IPage iPage, Vector<IAttachment> attachments, List<IItem> items, String parentContainerName) {
		this.stitcherApp = stitcherApp;
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
		String targetDirectory = LocalStorageUtility.getLocalWorkingDirectory(stitcherApp.MULTIPLICITY_SPACE, "").getAbsolutePath() + File.separatorChar + dirName;
		boolean canUpload = false;
		File savedFile = null;
		if (new File(targetDirectory).exists() == false) {
			canUpload = (new File(targetDirectory)).mkdirs();
		} else {
			canUpload = true;
		}

		if (canUpload) {
			stitcherApp.output_document_path = targetDirectory + File.separatorChar + filename;
			savedFile = new File(stitcherApp.output_document_path);
		}
		return savedFile;
	}

	public List<IItem> getItems() {
		return items;
	}

	public boolean isOnRepository(Spatial spatial) {
	    
	    if( spatial instanceof IRepositoryFrame ) {
	        return true;
	    }
	    
	    if(spatial.getParent() != null){
	        isOnRepository(spatial.getParent());
	    }
	    
        return false;
	    
	}
	public boolean isOnRepository(IItem item) {
	        
	        if( item instanceof IRepositoryFrame ) {
	            return true;
	        }
	        
	        if(item.getParentItem() != null){
	            isOnRepository(item.getParentItem());
	        }
	        
	        return false;
	        
	}
	   
//	public IImage createPhotoImage(URL url) {
//
//	    IHotSpotContentFactory hotSpotContentFactory = new HotSpotContentFactory();
//		IImage img;
//		img = stitcher.getContentFactory().createImage("photo", UUID.randomUUID());
//		img.setImage(url);
//		//img.setRelativeScale(0.6f);
//		img.setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
//	    BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
//
//		img.addItemListener(new ItemListenerAdapter() {
//
//			@Override
//			public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
//			    super.itemCursorChanged(item, event);
//				String message = "item moved over: ";
//				boolean firstFrameFound = false;
//				boolean offParent = true;
//				Node s = (Node) stitcher.getOrthoNode();
//				Vector2f locStore = new Vector2f();
//				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);
//
//				List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);
//				
//				if (item.getParentItem().getTreeRootSpatial() instanceof JMEFrame) {
//					JMEFrame frame = (JMEFrame) item.getParentItem().getTreeRootSpatial();
//					for (IItem pickedItem : findItemsOnTableAtPosition) {
//					    
//					    if( pickedItem.equals(frame)) {
//					        offParent = false;
//                            message = message + "it's parent frame SCANS.";
//					    }
//					    
////						if (pickedSpatial.getSpatial().equals(frame.getMaskGeometry())) {
////							offParent = false;
////							message = message + "it's parent frame SCANS.";
////						}
//					}
//				}
//
//				if (parentContainerName.equals(stitcher.SCAN_NAME) && offParent || parentContainerName.equals(stitcher.STENCIL_NAME) && offParent) {
//					for (IItem foundItem : findItemsOnTableAtPosition) {
//						if (foundItem instanceof JMEFrame && firstFrameFound == false) {
//							try {
//								Geometry geometry = (Geometry) foundItem.getManipulableSpatial();
//
//								if (foundItem instanceof HotSpotFrame) {
//									HotSpotFrame targetFrame = (HotSpotFrame)foundItem;
//
//									if (targetFrame.getName().contains("hotspotf-") && !highlightedFrames.contains(targetFrame)) {
//										message = message + targetFrame.getName() + ": let's turn it red";
//										firstFrameFound = true;
//										IBorder border = targetFrame.getBorder();
//										border.setColor(new ColorRGBA(1f, 0f, 0f, 0.6f));
//										// targetFrame.setBorder(getHighlightedFrame());
//										highlightedFrames.add(targetFrame);
//									} else if (highlightedFrames.contains(targetFrame)) {
//										message = message + "a hotspot frameprobably already red";
//										for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
//											if (!hotSpotFrame.equals(targetFrame)) {
//												IBorder border = hotSpotFrame.getBorder();
//												border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
//												// hotSpotFrame.setBorder(getNormalFrame());
//												highlightedFrames.remove(hotSpotFrame);
//											}
//										}
//									}
//								}
//							} catch (Exception e) {
//								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
//							}
//						}
//					}
//				}
//
//				if (!firstFrameFound && offParent) {
//					firstFrameFound = false;
//					message = message + "something else than a hotspotframe or the parent (i.e. the mist)";
//
//					for (IItem foundItem : findItemsOnTableAtPosition) {
//						if (foundItem instanceof JMEFrame && firstFrameFound == false) {
//							try {
//
//								if (foundItem instanceof HotSpotFrame) {
//									IHotSpotFrame targetFrame = (IHotSpotFrame)foundItem;
//									if (highlightedFrames.contains(targetFrame)) {
//										firstFrameFound = true;
//										IBorder border = targetFrame.getBorder();
//										border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
//										// targetFrame.setBorder(getNormalFrame());
//										highlightedFrames.remove(targetFrame);
//									}
//								}
//							} catch (Exception e) {
//								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
//							}
//						}
//					}
//
//					if (!firstFrameFound) {
//						clearAllHighlightedHotSpotFrames();
//					}
//				}
//
//				logger.info(message);
//			}
//
//			@Override
//			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
//			    super.itemCursorPressed(item, event);
//				logger.info("item pressed" + item.getBehaviours() + "parent: " + item.getParentItem());
//			}
//
//			@Override
//			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
//			    super.itemCursorClicked(item, event);
//				logger.info("item clicked" + item.getBehaviours());
//			}
//
//			@Override
//			public void itemCursorReleased(IItem releasedItem, MultiTouchCursorEvent event) {
//			    super.itemCursorReleased(releasedItem, event);
//				boolean offParent = true;
//
//				Vector2f locStore = new Vector2f();
//				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);
//
//                List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);
//                
//				if (releasedItem.getParentItem() instanceof IRepositoryFrame) {
//				    IRepositoryFrame repositoryFrame = (IRepositoryFrame) releasedItem.getParentItem();
//					for (IItem foundItem : findItemsOnTableAtPosition) {
//					    
//					    //did we move it around the repos
//					    if( isOnRepository(foundItem)) {
//                            offParent = false;
//                            return;
//                        }
//					}
//				}
//
//				if (parentContainerName.equals(stitcher.BACKGROUND_NAME) && (offParent == true)) {
//				    logger.debug("Creating Background Frame...");
//					IFrame frame = (IFrame) releasedItem.getParentItem();
//					releasedItem.removeItemListener(this);
//
//					if (releasedItem instanceof IImage) {
//						IImage imageItem = (IImage) releasedItem;
//						// create copy
//						IImage copy = createPhotoImage(imageItem.getImageUrl());
//						
//						float scale = (Float) getScale(((JMERectangularItem)copy).getSize());
//						copy.setRelativeScale(scale);
//						Vector2f position = stitcher.generateRandomPosition((JMEFrame) frame, copy);
//						frame.addItem(copy);
    //						copy.setRelativeLocation(position);
//					}
//					
//					frame.removeItem(releasedItem);
//					stitcher.createNewFrame(releasedItem, new Vector2f(0.0f, 0.0f), "back-" + releasedItem.getUUID(), IStitcherContants.BACKGROUND);
//					
//				} else {
//					// check if we are dropping on a "background"
//					// frame
//					boolean firstFrameFound = false;
//					for (IItem foundItem : findItemsOnTableAtPosition) {
//						if (foundItem instanceof JMEFrame && firstFrameFound == false) {
//							try {
//								if (foundItem instanceof HotSpotFrame) {
//									IHotSpotFrame targetFrame = (IHotSpotFrame)foundItem;
//
//									if (!targetFrame.isLocked() && targetFrame.getName().contains("back-") && (parentContainerName.equals(stitcher.SCAN_NAME) || parentContainerName.equals(stitcher.STENCIL_NAME))) {
//									    logger.debug("Dropping on background frame.....");
//										firstFrameFound = true;
//										IFrame frame = (IFrame) releasedItem.getParentItem();
//										frame.removeItem(releasedItem);
//
//										Vector2f itemWorldPos = releasedItem.getWorldLocation();
//										releasedItem.setRelativeScale(stitcher.INITIAL_DROP_SCALE);
//										targetFrame.addItem(releasedItem);
//										releasedItem.setWorldLocation(itemWorldPos);
//										targetFrame.getZOrderManager().bringToTop(releasedItem, null);
//
////										stitcher.bumpHotSpotConnections();
//										targetFrame.bringHotSpotsToTop();
//										targetFrame.bringPaletToTop();
//									} else if (!targetFrame.isLocked() && targetFrame.getName().contains("hotspotf-") && (parentContainerName.equals(stitcher.SCAN_NAME) || parentContainerName.equals(stitcher.STENCIL_NAME) || parentContainerName.equals(stitcher.BACKGROUND_NAME) )) {
//									    logger.debug("Dropping on hotspot frame.....");
//	                                    closeRepos(releasedItem.getParentItem());
//										firstFrameFound = true;
//										dropOnHotSpotFrame(releasedItem, targetFrame);
//									}// if
//								}// if
//
//							} catch (Exception e) {
//								logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
//							}// try
//						}// if
//
//					}// for
//					// image was dragged off of the frame DELETE
//					if (!firstFrameFound && (offParent == true )) {
//						logger.info("delete image moved off the frame");
//
//						IFrame frame = (IFrame) releasedItem.getParentItem();
//						frame.removeItem(releasedItem);
//					}// if
//				}// if
//
//				logger.info("cursor released caught event: " + releasedItem.getParentItem().getClass());
//				//stitcher.bumpHotSpotConnections();
//			}
//
//            private void closeRepos(IItem parentItem) {
//               if( parentItem instanceof IRepositoryFrame ){
//                   IRepositoryFrame repos = (IRepositoryFrame) parentItem;
//                   repos.close();
//               }
//                
//            }
//		});


//		return img;
//	}
	
    private void clearAllHighlightedHotSpotFrames() {
        for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
            IBorder border = hotSpotFrame.getBorder();
            border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
        }
        highlightedFrames = new ArrayList<HotSpotFrame>();
    }

	
	public void run() {
		itemsToReturn = new Vector<Object>();

		for (IAttachment iAttachment : attachments) {

			if (iAttachment.isOfImageType()) {
				File file = writeFileToLocalStorageDir(iAttachment.getName(), iPage.getPageName());

				logger.info("File exists: " + file.exists() + " - " + iAttachment.getName() + " - " + iAttachment.getSize() + " - " + file.length());
				if (file.exists() == false) {
					WikiUtility wikiUtility = new WikiUtility(stitcherApp.wikiUser, stitcherApp.wikiPass, stitcherApp.maxFileSize, iAttachment.getMimeType(), iAttachment.getAbsoluteUrl());
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
							i = StitcherUtils.createPhotoImage(file.toURI().toURL());
//							i.addItemListener(new ImageItemListener(stitcherApp));
							new ImageMultiTouchListener(i, stitcherApp);
							Vector2f size = ((JMERectangularItem) i).getSize();
							calculatedScale = StitcherUtils.getScale(size);
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



	public Vector<Object> getItemsToReturn() {
		return itemsToReturn;
	}

}
