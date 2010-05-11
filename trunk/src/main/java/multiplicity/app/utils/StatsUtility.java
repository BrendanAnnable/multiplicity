package multiplicity.app.utils;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jme.util.stat.StatCollector;
import com.jme.util.stat.StatType;
import com.jme.util.stat.graph.DefColorFadeController;
import com.jme.util.stat.graph.GraphFactory;
import com.jme.util.stat.graph.LineGrapher;
import com.jme.util.stat.graph.TabledLabelGrapher;

public class StatsUtility {
	private LineGrapher lgrapher;
	private TabledLabelGrapher tgrapher;
	private Node graphNode;
	private Quad lineGraph;
	private Quad labGraph;

	public StatsUtility(Node graphNode) {
		this.graphNode = graphNode;
	}
	
	public void setupStats() {
		lgrapher.addConfig(StatType.STAT_FRAMES, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.green);
		lgrapher.addConfig(StatType.STAT_FRAMES, LineGrapher.ConfigKeys.Stipple.name(), 0XFF0F);
		lgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.cyan);
		lgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lgrapher.addConfig(StatType.STAT_QUAD_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.lightGray);
		lgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lgrapher.addConfig(StatType.STAT_LINE_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.red);
		lgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lgrapher.addConfig(StatType.STAT_GEOM_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.gray);
		lgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.orange);
		lgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);

		tgrapher.addConfig(StatType.STAT_FRAMES, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_FRAMES, TabledLabelGrapher.ConfigKeys.Name.name(), "Frames/s:");
		tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tris:");
		tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Quads:");
		tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Lines:");
		tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Objs:");
		tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tex binds:");
		tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
	}
	
	public void setupStatGraphs() {
		StatCollector.setSampleRate(1000L);
		StatCollector.setMaxSamples(40);
		
		lineGraph = new Quad("lineGraph", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight()*.75f) {
			private static final long serialVersionUID = 1L;
			@Override
			public void draw(Renderer r) {
				StatCollector.pause();
				super.draw(r);
				StatCollector.resume();
			}
		};
		lgrapher = GraphFactory.makeLineGraph((int)(lineGraph.getWidth()+.5f), (int)(lineGraph.getHeight()+.5f), lineGraph);
		//	      lgrapher = GraphFactory.makeTimedGraph((int)(lineGraph.getWidth()+.5f), (int)(lineGraph.getHeight()+.5f), lineGraph);
		lineGraph.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth()*.5f), (DisplaySystem.getDisplaySystem().getHeight()*.625f),0);
		lineGraph.setCullHint(CullHint.Always);
		lineGraph.getDefaultColor().a = 0;
		graphNode.attachChild(lineGraph);

		Text f4Hint = new Text("f4", "F4 - toggle stats") {
			private static final long serialVersionUID = 1L;
			@Override
			public void draw(Renderer r) {
				StatCollector.pause();
				super.draw(r);
				StatCollector.resume();
			}
		};
		f4Hint.setCullHint( Spatial.CullHint.Never );
		f4Hint.setRenderState( Text.getDefaultFontTextureState() );
		f4Hint.setRenderState( Text.getFontBlend() );
		f4Hint.setLocalScale(.8f);
		f4Hint.setTextColor(ColorRGBA.gray);
		f4Hint.setLocalTranslation(DisplaySystem.getDisplaySystem().getRenderer().getWidth() - f4Hint.getWidth() - 15, DisplaySystem.getDisplaySystem().getRenderer().getHeight() - f4Hint.getHeight() - 10, 0);
		graphNode.attachChild(f4Hint);
		
		labGraph = new Quad("labelGraph", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight()*.25f) {
			private static final long serialVersionUID = 1L;
			@Override
			public void draw(Renderer r) {
				StatCollector.pause();
				super.draw(r);
				StatCollector.resume();
			}
		};
		tgrapher = GraphFactory.makeTabledLabelGraph((int)(labGraph.getWidth()+.5f), (int)(labGraph.getHeight()+.5f), labGraph);
		tgrapher.setColumns(2);
		tgrapher.setMinimalBackground(false);
		tgrapher.linkTo(lgrapher);
		labGraph.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth()*.5f), (DisplaySystem.getDisplaySystem().getHeight()*.125f),0);
		labGraph.setCullHint(CullHint.Always);
		labGraph.getDefaultColor().a = 0;
		graphNode.attachChild(labGraph);

	}

	public void clearControllers() {
		labGraph.clearControllers();
		lineGraph.clearControllers();		
	}

	public void newControllers(boolean showGraphs) {
		labGraph.addController(new DefColorFadeController(labGraph, showGraphs ? .6f : 0f, showGraphs ? .5f : -.5f));
		lineGraph.addController(new DefColorFadeController(lineGraph, showGraphs ? .6f : 0f, showGraphs ? .5f : -.5f));
	}
}
