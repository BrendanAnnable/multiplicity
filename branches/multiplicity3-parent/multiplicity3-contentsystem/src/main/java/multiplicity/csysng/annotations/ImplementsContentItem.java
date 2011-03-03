package multiplicity.csysng.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import multiplicity.csysng.items.item.IItem;

@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementsContentItem {
	Class<? extends IItem> target();
}
