package multiplicity.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import multiplicity.csysng.items.IItem;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Helper class which performs various xml/file related tasks
 * 
 * @author jt151077
 */
public class XMLOperations {

	private final static Logger logger = Logger.getLogger(XMLOperations.class.getName());	
	private static Document xmlDoc = null;
	private static final String ENCODING_FORMAT = "ISO-8859-1";
	private static String output_document_path = "";
	private static String input_file_path = "";

	public XMLOperations() {
	}

	
	/**
	 * Constructor that takes 2 params, an ID and a list of items
	 * @param uUID
	 * @param items
	 */
	public XMLOperations(UUID uUID, List<IItem> items) {
		//TODO: check if there is already an xml document existing for this group, if so fetch it
		Document newDoc = createDocument();
		Element root = newDoc.getRootElement();
		Element node;
		for (IItem iItem : items) {
			node = root.addElement("item");
			node.addElement("id").addCDATA(iItem.getUUID().toString());
			node.addElement("location").addCDATA(iItem.getRelativeLocation().toString());
		}
		outputDocument(newDoc);
		writeToLocalStorageDir(uUID, newDoc, "multiplicity", "xmlOuts");
	}


	/**
	 * Enables to write to a local directory, will which be created if non-existant
	 * @param id
	 * @param org.dom4j.Document
	 * @param Directory name
	 * @param Version
	 */
	private void writeToLocalStorageDir(UUID uUID, Document newDoc, String multiplicitySpace, String dirName) {
		String targetDirectory = LocalStorageUtility.getLocalWorkingDirectory(multiplicitySpace, "").getAbsolutePath() + File.separatorChar + dirName;
		boolean canUpload = false;
		if(new File(targetDirectory).exists() == false) {
			canUpload = (new File(targetDirectory)).mkdir();
		}
		else {
			canUpload = true;
		}
		
		if(canUpload) {
			output_document_path = targetDirectory + File.separatorChar + "Group_"+uUID+".xml";
			writeToFile(newDoc);			
		}
	}
	
	/**
	 * Method to create an empty xml document
	 * @return org.dom4j.document
	 */
	private Document createDocument() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement( "root" );

        return document;
    }

	
    /**
     * Get the dom4j document which contains the items
     * @return org.dom4j.document
     */
	protected static Document getXmlDoc() {
		if (xmlDoc == null) {
			SAXReader xmlReader = new SAXReader();
			xmlReader.setEncoding(ENCODING_FORMAT);
			try {
				xmlDoc = xmlReader.read(new File(input_file_path));
			} catch (DocumentException e) {
				logger.error("DocumentException: " + e);
			}
			if (xmlDoc == null) {
			    logger.error("Reading xml returned null! Encoding trouble?");
			}
		}
		return xmlDoc;
	}
	
	   
    /**
     * Get a dom4j document corresponding to a file name
     * @param fileName
     * @return org.dom4j.document
     */
    protected static Document getXmlDocument(String fileName) {
        Document doc = null;
        File xmlFile = new File(fileName);
        if (!xmlFile.exists()) {
            logger.error("No file: " + xmlFile.getAbsolutePath());
        } else {
            logger.debug("reading file at " + xmlFile.getAbsolutePath());
            SAXReader reader = new SAXReader();
            try {
                doc = reader.read(xmlFile);
            } catch (DocumentException e) {
                logger.error("Trouble while reading file: " + xmlFile.getAbsolutePath() + e);
            }
        }
        return doc;
    }

    /**
     * Removes a node from a document and writes
     * @param node to be deleted
     * @param xmlDoc which used to contain the node
     */
	protected static void deleteNode(Node node, Document xmlDoc) {
		node.detach();
		writeToFile(xmlDoc);
	}
	
	/**
	 * @param document
	 * @return a byteArray formatted document
	 */
	private static byte[] convertToByteArray(Document document) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputFormat of = OutputFormat.createPrettyPrint();
		of.setEncoding(ENCODING_FORMAT);
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(out, of);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e);
		}
		try {
			writer.write(document);
			writer.flush();
		} catch (IOException e) {
			logger.error("IOException: " + e);
		}

		return out.toByteArray();
	}
	
	private void outputDocument(Document document) {
		// Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer;
		try {
			writer = new XMLWriter( System.out, format );
			writer.write( document );
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e);
		} catch (IOException e) {
			logger.error("IOException: " + e);
		}

	}


	/**
	 * Write the xml document to a file
	 * @param document
	 */
	private static boolean writeToFile(Document document) {
		System.setProperty("file.encoding", ENCODING_FORMAT);
		try {
			FileOutputStream fos = new FileOutputStream(output_document_path);
			fos.write(convertToByteArray(document));
			fos.close();
			logger.info("File successfully written here: "+output_document_path);
			return true;
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException: " + e);
			return false;
		} catch (IOException e) {
			logger.error("IOException: " + e);
			return false;
		}
	}
}
