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
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * A utility class for highlighting components. A border is drawn around the component to indicate that the component is
 * highlighted and optionally a message is displayed.
 */
public class Highlighter {

  /**
   * Highlights the component with the specified message. The highlighting is cleared after 2 seconds automatically.
   *
   * @param component the component to highlight
   * @param message   the message to display
   */
  public static void error(JComponent component, String message) {
    Highlighter highlighter = new Highlighter(component);
    highlighter.setHighlightColor(Color.red.darker());
    highlighter.highlight(message);
  }

  /**
   * Clears the highlighting of the component, if any.
   *
   * @param component the component
   */
  public static void clearHighlighting(JComponent component) {
    getActiveHighlighter(component).ifPresent(Highlighter::clear);
  }

  private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);
  private static final Map<JComponent, Highlighter> HIGHLIGHTERS = new HashMap<>();

  private final Border origBorder;
  private final JComponent component;

  private Color highlightColor;
  private int highlightMargin;
  private double duration;
  private PopupComponent popupComponent;

  /**
   * Creates a new Highlighter. The highlighting is cleared after 2 seconds automatically. The highlighting color is by
   * default blue.
   *
   * @param component the component
   */
  public Highlighter(JComponent component) {
    if (component == null) {
      throw new IllegalArgumentException("Component cannot be null");
    }
    this.component = component;
    origBorder = component.getBorder();
    highlightColor = Color.blue.darker();
    highlightMargin = 2;
    duration = 2.0;
  }

  /**
   * Sets the highlighting duration in seconds.
   *
   * @param duration in seconds
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Sets the color of the highlight border.
   *
   * @param highlightColor the color
   */
  public void setHighlightColor(Color highlightColor) {
    this.highlightColor = highlightColor;
  }

  /**
   * Sets the margin of the highlight border.
   *
   * @param highlightMargin the margin
   */
  public void setHighlightMargin(int highlightMargin) {
    this.highlightMargin = highlightMargin;
  }

  /**
   * Highlights the component with a colored border.
   *
   * @see #setHighlightColor
   */
  public void highlight() {
    highlightComponent(null);
  }

  /**
   * Highlights the component with a colored border and the specified message. If the message is null, the component is
   * only highlighted.
   *
   * @param infoMessage the message, can be null
   */
  public void highlight(String infoMessage) {
    highlightComponent(infoMessage);
    HIGHLIGHTERS.put(component, this);
    EXECUTOR_SERVICE.schedule(this::clear, (long) (duration * 1000), TimeUnit.MILLISECONDS);
  }

  /**
   * Clears the highlighting of the associated component.
   */
  public void clear() {
    if (component != null) {
      clearComponent();
      HIGHLIGHTERS.remove(component);
    }
  }

  private void highlightComponent(String infoMessage) {
    component.setBorder(createHighlightBorder(highlightMargin, highlightColor));
    if (infoMessage != null) {
      popupComponent = showPopup(infoMessage);
    }
  }

  private MatteBorder createHighlightBorder(int margin, Color color) {
    return new MatteBorder(margin, margin, margin, margin, color);
  }

  private void clearComponent() {
    component.setBorder(origBorder);
    if (popupComponent != null) {
      popupComponent.hidePopup();
    }
  }

  private PopupComponent showPopup(String message) {
    MultiLineText textField = new MultiLineText(message);
    textField.setFont(textField.getFont().deriveFont(Font.BOLD));
    textField.setEditable(false);
    textField.setBorder(new EmptyBorder(2, 2, 0, 0));
    textField.setColumns(Math.min(15, message.length()));
    textField.setRows((int) Math.floor((double) message.length() / textField.getColumns()));
    float[] rgbComponents = highlightColor.getRGBComponents(null);
    textField.setBackground(new Color(highlightColor.getColorSpace(), rgbComponents, rgbComponents[3] * 0.3f));
    PopupComponent popupComponent = new PopupComponent(textField);
    popupComponent.showPopupAt(component.getLocationOnScreen().x,
        component.getLocationOnScreen().y + component.getHeight());
    return popupComponent;
  }

  private static Optional<Highlighter> getActiveHighlighter(JComponent component) {
    return Optional.ofNullable(HIGHLIGHTERS.get(component));
  }
}

