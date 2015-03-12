package de.lessvoid.nifty.processing.renderer;

import java.io.File;
import java.io.IOException;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.nifty.tools.resourceloader.FileSystemLocation;
import de.lessvoid.nifty.java2d.renderer.RenderDeviceJava2dImpl;
import de.lessvoid.nifty.processing.renderer.GraphicsWrapperProcessing;
import de.lessvoid.nifty.processing.renderer.MouseCursorProcessing;
import de.lessvoid.nifty.processing.renderer.RenderFontProcessing;
import processing.core.*;

/**
 * Implementation of Nifty's RenderDevice for Processing.
 * @author Xuanming
 */
public class RenderDeviceProcessing implements RenderDevice {
	
	private final RenderDeviceJava2dImpl renderDevice;
	private final PGraphics graphics;
	private final PApplet app;
	private NiftyResourceLoader resourceLoader;
	
	/**
	 * Instantiate RenderDeviceProcessing. 
	 * @param app PApplet instance that Processing is currently running in.
	 * @param nifty Nifty instance that is in use.
	 */
	public RenderDeviceProcessing(PApplet app) {
		GraphicsWrapperProcessing graphicsWrapper = new GraphicsWrapperProcessing(app);
		this.graphics = graphicsWrapper.getCanvas();
		this.renderDevice = new RenderDeviceJava2dImpl(graphicsWrapper);
		this.app = app;
		
		/* 
		 * All classes in Processing are inner classes of the Processing PApplet instance.
		 * Creating a helper property to assist in finding inner ScreenController classes from XML.
		 * Using this property, users just need to specify 'controller="${PROP.APP}ControllerName"' in
		 * their XML layouts, where ControllerName is the name of the actual ScreenController impl.
		 */
		System.setProperty("APP", app.getClass().getName() + "$");
	}

	@Override
	public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
		this.resourceLoader = niftyResourceLoader;
		resourceLoader.addResourceLocation(new FileSystemLocation(new File(app.dataPath(""))));
		renderDevice.setResourceLoader(resourceLoader);
	}

	@Override
	public RenderImage createImage(String filename, boolean filterLinear) {
		return renderDevice.createImage(filename,  filterLinear);
	}

	@Override
	public RenderFont createFont(String filename) {
		return new RenderFontProcessing(app, graphics, resourceLoader.getResource(filename).getPath());
	}

	@Override
	public int getWidth() {
		return renderDevice.getWidth();
	}

	@Override
	public int getHeight() {
		return renderDevice.getHeight();
	}

	@Override
	public void beginFrame() {
		renderDevice.beginFrame();
	}

	@Override
	public void endFrame() {
		renderDevice.endFrame();
	}

	@Override
	public void clear() {
		renderDevice.clear();
	}

	@Override
	public void setBlendMode(BlendMode renderMode) {
		renderDevice.setBlendMode(renderMode);
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color color) {
		renderDevice.renderQuad(x, y, width, height, color);
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color topLeft,
			Color topRight, Color bottomRight, Color bottomLeft) {
		renderDevice.renderQuad(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int width,
			int height, Color color, float imageScale) {
		renderDevice.renderImage(image, x, y, width, height, color, imageScale);
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int w, int h,
			int srcX, int srcY, int srcW, int srcH, Color color, float scale,
			int centerX, int centerY) {
		renderDevice.renderImage(image,  x,  y,  w,  h,  srcX,  srcY,  srcW,  srcH,  color,  scale,  centerX,  centerY);
	}

	@Override
	public void renderFont(RenderFont font, String text, int x, int y,
			Color fontColor, float sizeX, float sizeY) {
		if (font instanceof RenderFontProcessing){
			graphics.textFont(((RenderFontProcessing) font).getFont());
			graphics.textSize(((RenderFontProcessing) font).getSize() * sizeX);
			graphics.fill(fontColor.getRed() * 255, fontColor.getBlue() * 255, fontColor.getGreen() * 255, fontColor.getAlpha() * 255);
			graphics.text(text, x, y + font.getHeight());
		}
	}

	@Override
	public void enableClip(int x0, int y0, int x1, int y1) {
		renderDevice.enableClip(x0, y0, x1, y1);
	}

	@Override
	public void disableClip() {
		renderDevice.disableClip();
	}

	@Override
	public MouseCursor createMouseCursor(String filename, int hotspotX,
			int hotspotY) throws IOException {
		return new MouseCursorProcessing(app, app.loadImage(resourceLoader.getResource(filename).getPath()), hotspotX, hotspotY);
	}

	@Override
	public void enableMouseCursor(MouseCursor mouseCursor) {
		mouseCursor.enable();
	}

	@Override
	public void disableMouseCursor() {
		app.noCursor();
	}
	
	/**
	 * Get the Processing PGraphics object for external uses.
	 * @return PGraphics object that Nifty draws on.
	 */
	public PGraphics getCanvas() {
		return graphics;
	}
}
