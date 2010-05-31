package multiplicity.appgallery.stitcher;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import multiplicity.app.utils.LocalStorageUtility;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.input.events.MultiTouchCursorEvent;
import no.uio.intermedia.snomobile.WikiUtility;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

public class GetAttachmentItems extends Thread {
	
	private final static Logger logger = Logger.getLogger(GetAttachmentItems.class.getName());	
	private Vector<IAttachment> attachments;
	private List<IItem> items = null;
	private IPage iPage = null;
	private StitcherApp stitcher;
	
	public GetAttachmentItems(StitcherApp stitcherApp, IPage iPage, Vector<IAttachment> attachments, List<IItem> items) {
		this.stitcher = stitcherApp;
		this.attachments = attachments;
		this.iPage = iPage;
		this.items = items;
	}
	
	/**
	 * Enables to write to a local directory, will which be created if non-existant
	 * @param id
	 * @param org.dom4j.Document
	 * @param Directory name
	 * @param Version
	 */
	public File writeFileToLocalStorageDir(String filename, String dirName) {
		String targetDirectory = LocalStorageUtility.getLocalWorkingDirectory(stitcher.MULTIPLICITY_SPACE, "").getAbsolutePath() + File.separatorChar + dirName;
		boolean canUpload = false;
		File savedFile = null;
		if(new File(targetDirectory).exists() == false) {
			canUpload = (new File(targetDirectory)).mkdirs();
		}
		else {
			canUpload = true;
		}
		
		if(canUpload) {
			stitcher.output_document_path = targetDirectory + File.separatorChar + filename;	
			savedFile = new File(stitcher.output_document_path);
		}
		return savedFile;
	}
	
	public List<IItem> getItems() {
		return items;
	}
	
	public void run() {
		for (IAttachment iAttachment : attachments) {
			
			
			if(iAttachment.getMimeType().equals("image/png") ||iAttachment.getMimeType().equals("image/jpeg") || iAttachment.getMimeType().equals("image/jpg") || iAttachment.getMimeType().equals("image/gif")) {
				IImage img = null;
				File file = writeFileToLocalStorageDir(iAttachment.getName(), iPage.getPageName());
				
				logger.info("File exists: "+file.exists()+" - "+iAttachment.getName()+" - "+iAttachment.getSize()+ " - "+file.length());
				if(file.exists() == false) {
					WikiUtility wikiUtility = new WikiUtility(stitcher.wikiUser, stitcher.wikiPass, stitcher.maxFileSize, iAttachment.getMimeType(),iAttachment.getAbsoluteUrl());
					try {
						wikiUtility.start();
						wikiUtility.join();
					} catch (InterruptedException e) {
						logger.debug("getComments:  InterruptedException: " + e);
					}
					Object resourceToDownload = wikiUtility.getResource();
					
					if(resourceToDownload != null) {
						iAttachment.setIsValid(true);
						try {
							ImageIO.write( (RenderedImage) resourceToDownload,  iAttachment.getMimeType().substring(6), file);
						} catch (IOException e) {
							logger.debug("IOException: "+e);
						} 
					}
					else {
						iAttachment.setIsValid(false);
					}
				}
				else {
					iAttachment.setIsValid(true);
					//TODO: check that the file on the disk is at least the same size as the "expected one", if not, download new.
					// udpate: 31/05/2010 - Already attempted comparing iAttachment.getSize() and file.length(), but they appear not to be the same
					// even though the files on disk are valid
				}
				
				if(iAttachment.getIsValid()) {
					try {
						img = stitcher.getContentFactory().createImage("photo", UUID.randomUUID());
						img.setImage(file.toURI().toURL());
					} catch (MalformedURLException e) {
						logger.debug("MalformedURLException: "+e);
					}
					img.setRelativeScale(0.8f);
					img.setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
					
					img.addItemListener(new ItemListenerAdapter() {
						@Override
						public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
							System.out.println("item pressed" + item.getBehaviours());
						}
						
						@Override
						public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
							System.out.println("item clicked" + item.getBehaviours());
						}
					});
					BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
					
					//stitcher.smaker.register(img, this);
					
					stitcher.zOrderedItems.add(img);
					items.add(img);
				}
			}
		}
	}

}
