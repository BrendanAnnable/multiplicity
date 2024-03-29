package multiplicity.config.display;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity.config.PreferencesItem;

public class DisplayPreferences implements PreferencesItem {
	
	private static Preferences prefs = Preferences.userNodeForPackage(DisplayPreferences.class);

	private static final String DISPLAY_WIDTH = "DISPLAY_WIDTH";
	private static final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";
	private static final String DISPLAY_FREQ = "DISPLAY_FREQ";
	private static final String DISPLAY_BITS_PER_PIXEL = "DISPLAY_BITS_PER_PIXEL";
	private static final String DISPLAY_FULLSCREEN = "DISPLAY_FULLSCREEN";
	private static final String DISPLAY_MIN_AA_SAMPLES = "DISPLAY_MIN_AA_SAMPLES";
	private static final String DISPLAY_STENCIL_BITS = "DISPLAY_STENCIL_BITS";
	private static final String DISPLAY_ALPHA_BITS = "DISPLAY_ALPHA_BITS";	
	private static final String DISPLAY_DEPTH_BITS = "DISPLAY_DEPTH";
	private static final String DISPLAY_RENDERER = "DISPLAY_RENDERER";
	private static final String DISPLAY_SHAPE = "DISPLAY_SHAPE";
	private static final String DISPLAY_DEFAULT_SHAPE = "DISPLAY_DEFAULT_SHAPE";
	private static final String DISPLAY_STEREO_MODE = "DISPLAY_STEREO_MODE";
	
	public enum Stereo3DMode {
		NONE,
		ANAGLYPH,
		STEREO_BUFFER
	}
	
	
	public DisplayPreferences() {}
	
	@Override
	public JPanel getConfigurationPanel() {
		DisplayPreferencesPanel dp2 = new DisplayPreferencesPanel(this);
		return dp2;
	}

	@Override
	public String getConfigurationPanelName() {
		return "Display";
	}
	
	public void setWidth(int w) {
		prefs.putInt(DISPLAY_WIDTH, w);
	}

	public int getWidth() {
		return prefs.getInt(DISPLAY_WIDTH, 1024);
	}

	public int getHeight() {
		return prefs.getInt(DISPLAY_HEIGHT, 768);
	}

	public void setHeight(int h) {
		prefs.putInt(DISPLAY_HEIGHT, h);
	}

	public int getDepthBits() {
		return prefs.getInt(DISPLAY_DEPTH_BITS, 8);
	}

	public void setDepthBits(int b) {
		prefs.putInt(DISPLAY_DEPTH_BITS, b);
	}
	
	public void setBitsPerPixel(int bpp) {
		prefs.putInt(DISPLAY_BITS_PER_PIXEL, bpp);
	}
	
	public int getBitsPerPixel() {
		return prefs.getInt(DISPLAY_BITS_PER_PIXEL, 16);
	}
	
	public int getStencilBits() {
		return prefs.getInt(DISPLAY_STENCIL_BITS, 8);
	}
	
	public void setStencilBits(int bits) {
		prefs.putInt(DISPLAY_STENCIL_BITS, bits);
	}

	public int getAlphaBits() {
		return prefs.getInt(DISPLAY_ALPHA_BITS, 0);
	}
	
	public void setAlphaBits(int bits) {
		prefs.putInt(DISPLAY_ALPHA_BITS, bits);
	}
	
	public int getFrequency() {
		return prefs.getInt(DISPLAY_FREQ, -1);
	}

	public void setFrequency(int f) {
		prefs.putInt(DISPLAY_FREQ, f);
	}

	public boolean getFullScreen() {
		return prefs.getBoolean(DISPLAY_FULLSCREEN, false);
	}

	public void setFullScreen(boolean fs) {
		prefs.putBoolean(DISPLAY_FULLSCREEN, fs);
	}

	public String getDisplayRenderer() {
		return prefs.get(DISPLAY_RENDERER, "LWJGL");
	}
	
	public int getMinimumAntiAliasSamples() {
		return prefs.getInt(DISPLAY_MIN_AA_SAMPLES, 0);
	}
	
	public void setMinimumAntiAliasSamples(int samples) {
		prefs.putInt(DISPLAY_MIN_AA_SAMPLES, samples);
	}

	public void setDisplayRenderer(String renderer) {
		prefs.put(DISPLAY_RENDERER, renderer);
	}

	public void setDisplayShape(String s) {
		prefs.put(DISPLAY_SHAPE, s);
	}

	public String getDisplayShape() {
		return prefs.get(DISPLAY_SHAPE, "");
	}

	public void setUseDefaultShapeFlag(boolean fs) {
		prefs.putBoolean(DISPLAY_DEFAULT_SHAPE, fs);
	}

	public boolean getUseDefaultShapeFlag() {
		return prefs.getBoolean(DISPLAY_DEFAULT_SHAPE, true);
	}

	public void setStereo3DMode(Stereo3DMode mode) {
		prefs.put(DISPLAY_STEREO_MODE, mode.toString());
	}
	
	public Stereo3DMode getStereo3DMode() {
		return Stereo3DMode.valueOf(prefs.get(DISPLAY_STEREO_MODE, Stereo3DMode.NONE.toString()));
	}
}
