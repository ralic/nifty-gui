/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyMinSizeCallback;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.annotation.NiftyStyleProperty;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterHAlign;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyColor;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyFont;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterVAlign;
import de.lessvoid.nifty.internal.render.TextRenderer;

public class Label extends NiftyAbstractControl {
  private NiftyColor textColor = NiftyColor.white();
  private String text = "";
  private NiftyFont font;
  private HAlign textHAlign = HAlign.center;
  private VAlign textVAlign = VAlign.center;

  @Override
  public void init(final NiftyNode niftyNode) {
    super.init(niftyNode);
    niftyNode.addCanvasPainter(new LabelCanvasPainter());
    niftyNode.enableMinSize(new NiftyMinSizeCallback() {
      @Override
      public Size calculateMinSize(final NiftyNode niftyNode) {
        assertFont();
        Size result = new Size();
        result.width = font.getWidth(text);
        result.height = font.getHeight();
        return result;
      }
    });
    niftyNode.setStyleClass("label");
  }

  /**
   * Change the Label text.
   *
   * @param text new text
   */
  @NiftyStyleProperty(name = "text")
  public void setText(final String text) {
    this.text = text;
    niftyNode.requestLayout();
  }

  /**
   * Get the Label text.
   *
   * @return label text
   */
  public String getText() {
    return text;
  }

  /**
   * Set the Label color.
   *
   * @param color the color
   */
  @NiftyStyleProperty(name = "text-color", converter = NiftyStyleStringConverterNiftyColor.class)
  public void setTextColor(final NiftyColor color) {
    this.textColor = color;
    niftyNode.requestRedraw();
  }

  /**
   * Get the current Label color.
   *
   * @return the current color of the label
   */
  public NiftyColor getTextColor() {
    return textColor;
  }

  /**
   * Set the NiftyFont to use for this label.
   *
   * @param font the font to use
   */
  @NiftyStyleProperty(name = "font", converter = NiftyStyleStringConverterNiftyFont.class)
  public void setFont(final NiftyFont font) {
    this.font = font;
    niftyNode.requestRedraw();
  }

  /**
   * Get the NiftyFont this label uses.
   *
   * @return the NiftyFont
   */
  public NiftyFont getFont() {
    return font;
  }

  /**
   * Set the horizontal alignment.
   * @param halign horizontal alignment
   */
  @NiftyStyleProperty(name = "text-halign", converter = NiftyStyleStringConverterHAlign.class)
  public void setTextHAlign(final HAlign halign) {
    textHAlign = halign;
  }

  /**
   * Get the horizontal alignment.
   * @return the horizontal alignment
   */
  public HAlign getTextHAlign() {
    return textHAlign;
  }

  /**
   * Set the vertical alignment.
   * @param valign vertical alignment
   */
  @NiftyStyleProperty(name = "text-valign", converter = NiftyStyleStringConverterVAlign.class)
  public void setTextVAlign(final VAlign valign) {
    textVAlign = valign;
  }

  /**
   * Get the vertical alignment.
   * @return the vertical alignment
   */
  public VAlign getTextVAlign() {
    return textVAlign;
  }

  /**
   * Get the width of the current text in px.
   * @return the width of the current text in px
   */
  public int getTextWidth() {
    return font.getWidth(text);
  }

  /**
   * Get the height of the current text in px.
   * @return the height of the current text in px
   */
  public int getTextHeight() {
    return font.getHeight();
  }

  private void assertFont() {
    if (font == null) {
      throw new RuntimeException("Label requires a font but none available (null)");
    }
  }

  private class LabelCanvasPainter implements NiftyCanvasPainter {
    private TextRenderer textRenderer = new TextRenderer();

    @Override
    public void paint(final NiftyNode node, final NiftyCanvas canvas) {
      canvas.setTextColor(textColor);
      assertFont();
      textRenderer.initialize(font, text);
      textRenderer.setTextHAlign(textHAlign);
      textRenderer.setTextVAlign(textVAlign);
      textRenderer.renderText(node, canvas);
    }
  }
}