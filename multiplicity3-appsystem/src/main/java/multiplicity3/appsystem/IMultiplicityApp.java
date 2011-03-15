package multiplicity3.appsystem;

import multiplicity3.input.MultiTouchInputComponent;

public interface IMultiplicityApp {
	void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo);
	void shouldStop();
}
