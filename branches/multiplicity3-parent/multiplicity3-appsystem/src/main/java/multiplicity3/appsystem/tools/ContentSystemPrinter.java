package multiplicity3.appsystem.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;

public class ContentSystemPrinter {
	public static void logContentSystemByZOrderFromRoot(Logger log, Level level, IItem root) {
		int indent = 0;
		printContentSystemFromItemWithIndent(log, level, root, indent);
	}

	private static void printContentSystemFromItemWithIndent(Logger log,
			Level level, IItem item, int indent) {		
		StringBuffer sb = new StringBuffer();
		sb.append('\n');
		String indentSpaces = getNSpaces(indent * 3);
		sb.append(indentSpaces + getItemString(item));
		sb.append(itemsChildren(item, indent+1));
		
		log.log(level, sb.toString());
	}
	

	private static String itemsChildren(IItem item, int indent) {
		StringBuffer sb = new StringBuffer();
		String indentSpaces = getNSpaces(indent * 3);
		for(IItem i : item.getChildItems()) {
			sb.append(indentSpaces + getItemString(i));			
			sb.append(itemsChildren(i, indent + 1));
		}
		return sb.toString();
	}
	
	private static String getItemString(IItem item) {
		return item.getZOrder() + ": " + item.toString() + '\n';
	}

	private static String getNSpaces(int indent) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < indent; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
}
