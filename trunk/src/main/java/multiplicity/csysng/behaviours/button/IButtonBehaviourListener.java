package multiplicity.csysng.behaviours.button;

import multiplicity.csysng.items.IItem;

public interface IButtonBehaviourListener {
	public void buttonPressed(IItem item);
	public void buttonClicked(IItem item);
	public void buttonReleased(IItem item);
}
