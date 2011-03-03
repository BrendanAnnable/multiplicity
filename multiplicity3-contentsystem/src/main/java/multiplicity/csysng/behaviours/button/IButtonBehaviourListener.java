package multiplicity.csysng.behaviours.button;

import multiplicity.csysng.items.item.IItem;

public interface IButtonBehaviourListener {
	public void buttonPressed(IItem item);
	public void buttonClicked(IItem item);
	public void buttonReleased(IItem item);
}
