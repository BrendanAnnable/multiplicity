package multiplicity.csysngjme.factory;

import java.util.UUID;

import multiplicity.csysng.factory.IContentFactory;

public class ContentItemFactoryUtil {

    public static String validateName(String name) {
    	if(name == null) return "<empty>";
    	return name;
    }

    public static UUID validateUUID(UUID uuid) {
    	if(uuid == null) return UUID.randomUUID();
    	return uuid;
    }

}