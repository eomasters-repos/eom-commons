/*-
 * ========================LICENSE_START=================================
 * EOM Commons - Library of common utilities for Java
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2025 Marco Peters
 * ======================================================================
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * =========================LICENSE_END==================================
 */

package org.eomasters.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A multi-line text component based on a JTextArea.
 *
 * <p>If used in a MigLayout the "wmin" property should be set to a small value.
 */
public class MultiLineText extends JTextArea {

  /**
   * Creates a new MultiLineText with the given text.
   *
   * @param text the text
   */
  public MultiLineText(String text) {
    super(text);
    setEditable(false);
    setLineWrap(true);
    setWrapStyleWord(true);
    setFont(UIManager.getFont("Label.font"));
    setBackground((Color) UIManager.get("Label.background"));
    setBorder((Border) UIManager.get("TextField.border"));
  }

  /**
   * Sets the preferred width of the component while keeping the preferred height.
   *
   * @param preferredWidth the preferred width
   */
  public void setPreferredWidth(int preferredWidth) {
    setPreferredSize(new Dimension(preferredWidth, getPreferredSize().height));
  }

  private int getTextWidth(String text) {
    AffineTransform identity = new AffineTransform();
    FontRenderContext frc = new FontRenderContext(identity, true, true);
    return (int) (getFont().getStringBounds(text, frc).getWidth());
  }
}
