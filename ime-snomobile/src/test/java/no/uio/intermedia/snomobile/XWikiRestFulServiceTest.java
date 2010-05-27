package no.uio.intermedia.snomobile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IComment;
import no.uio.intermedia.snomobile.interfaces.IPage;
import no.uio.intermedia.snomobile.interfaces.ISpace;
import no.uio.intermedia.snomobile.interfaces.ITag;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jeremy Toussaint
 *
 */
public class XWikiRestFulServiceTest {
	
	private static final Logger logger = Logger.getLogger(XWikiRestFulServiceTest.class.getName());
	private Properties prop = null;
	
	@Before
	public void setup() {
		prop = new Properties();
		 
    	try {
            prop.load(getClass().getResourceAsStream("/xwiki.properties"));
    	} 
    	catch (IOException e) {
    		logger.debug("setup:  IOException: "+e);
        }
	}
	
	@Test
	public void propertyFileNullExceptionTest() {
		WikiService wikiService = null;
		try {
			wikiService = new XWikiRestFulService(null);
		}
		catch (NullPointerException e) {
			assertNull(wikiService);
		}
	}
	
	
	@Test
	public void test_returnPage() {
		//we go for the default test page "snomobile.fuzz", and use the default constructor
		WikiService wikiService = new XWikiRestFulService(prop);
		IPage wikiPage = wikiService.getWikiPage();
		//test that page is not null
		assertNotNull(wikiPage);
		//test that the page content contains an expected string
		assertTrue((wikiPage.getContent()).contains("text"));
		
		logger.info("content: "+wikiPage.getContent());
		logger.info("creator: "+wikiPage.getCreator());
		logger.info("version: "+wikiPage.getVersion());
		logger.info("version: "+wikiPage.getComments());
		logger.info("version: "+wikiPage.getAttachments());
		logger.info("version: "+wikiPage.getTags());
	}
	
	@Test
	public void test_returnSpaces() {
		//we go for the default test page "snomobile.fuzz", and use the default constructor
		WikiService wikiService = new XWikiRestFulService(prop);
		Vector<ISpace> spaces = wikiService.getWikiSpaces();
		assertNotNull(spaces);
		if(spaces != null) {
			logger.info("1st space: "+spaces.elementAt(0).getName());			
		}
	}
	
	@Test
	public void test_returnCommentsForPage() {
		//we go for the default test page "snomobile.fuzz", and use the default constructor
		WikiService wikiService = new XWikiRestFulService(prop);
		Vector<IComment> comments = wikiService.getComments();
		assertNotNull(comments);
		if(comments != null) {
			logger.info("comment: "+comments.elementAt(0).getText());			
		}
	}
	
	@Test
	public void test_returnAttachmentsForPage() {
		//we go for the default test page "snomobile.fuzz", and use the default constructor
		WikiService wikiService = new XWikiRestFulService(prop);
		Vector<IAttachment> attachments = wikiService.getAttachments();
		assertNotNull(attachments);
		if(attachments != null) {
			for(int i=0; i<attachments.size(); i++) {
				logger.info("################################attachment: "+attachments.elementAt(i).getName()+" "+attachments.elementAt(i).getMimeType());		
				if(attachments.elementAt(i).getMimeType().equals("image/png") || attachments.elementAt(i).getMimeType().equals("image/jpeg") || attachments.elementAt(i).getMimeType().equals("image/jpg") || attachments.elementAt(i).getMimeType().equals("image/gif")) {
					assertNotNull(attachments.elementAt(i).getResource());
				}
				else {
					assertNotNull((attachments.elementAt(i).getResource()));
				}				
			}
		}
	}
	

	@Test
	public void test_returnTagsForPage() {
		//we go for the default test page "snomobile.fuzz", and use the default constructor
		WikiService wikiService = new XWikiRestFulService(prop);
		Vector<ITag> tags = wikiService.getTags();
		assertNotNull(tags);
		if(tags != null) {
			logger.info("tag: "+tags.elementAt(0).getName());			
		}
	}
}