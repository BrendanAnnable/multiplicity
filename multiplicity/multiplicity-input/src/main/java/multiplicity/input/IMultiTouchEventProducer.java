package multiplicity.input;

public interface IMultiTouchEventProducer {
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener);	
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index);	
	public void registerMultiTouchExceptionListener(IDispatchedMultiTouchEventExceptionListener listener);
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener);
}
