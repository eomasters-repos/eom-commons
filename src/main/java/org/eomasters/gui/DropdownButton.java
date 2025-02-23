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

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * A custom button with a dropdown menu.
 */
public class DropdownButton extends JButton {

  private final JPopupMenu popupMenu;

  /**
   * Constructs a DropdownButton with the specified icon.
   *
   * @param icon The icon to display on the button.
   */
  public DropdownButton(ImageIcon icon) {
    this(null, icon);
  }

  /**
   * Constructs a DropdownButton with the specified label.
   *
   * @param label The label to display on the button, or null if the button should display a default arrow icon.
   */
  public DropdownButton(String label) {
    this(label, null);
  }

  /**
   * Constructs a DropdownButton with the specified label and icon.
   *
   * @param label The label to display on the button, or null if the button should display a default arrow icon.
   * @param icon  The icon to display on the button.
   */
  public DropdownButton(String label, ImageIcon icon) {
    this(label, icon, new JPopupMenu());
  }

  /**
   * Constructs a DropdownButton with the specified label, icon, and popup menu.
   *
   * @param label     The label to display on the button.
   * @param icon      The icon to display on the button.
   * @param popupMenu The popup menu to display when the button is clicked.
   */
  public DropdownButton(String label, ImageIcon icon, JPopupMenu popupMenu) {
    setIcon(icon);
    setText(label);
    this.popupMenu = popupMenu;
    this.popupMenu.setVisible(false);
    addActionListener(this::actionPerformed);
  }

  /**
   * Sets the text of the button. If the text is null, the button will display a default arrow icon.
   *
   * @param text The text to display on the button.
   */
  @Override
  public void setText(String text) {
    if (text == null) {
      super.setText("▼");
    } else {
      super.setText(text + " ▼");
    }
  }

  /**
   * Sets the component to display by the popup menu.
   *
   * @param component The component to display by the popup menu.
   */
  public void setPopupComponent(JComponent component) {
    popupMenu.removeAll();
    popupMenu.add(component);
  }

  /**
   * Shows or hides the popup menu.
   *
   * @param show Whether to show or hide the popup menu.
   */
  public void showPopup(boolean show) {
    if (show) {
      popupMenu.show(DropdownButton.this, 0, DropdownButton.this.getHeight());
    } else {
      popupMenu.setVisible(false);
    }
  }

  private void actionPerformed(ActionEvent e) {
    showPopup(!popupMenu.isVisible());
  }

}
