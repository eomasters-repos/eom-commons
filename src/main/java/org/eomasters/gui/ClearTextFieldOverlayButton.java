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

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.eomasters.icons.Icons;

/**
 * Shows a clickable icon on the right of a text field allowing clear the content. The icon is only shown if the text
 * field is not empty.
 */
public class ClearTextFieldOverlayButton {

  private final JTextField textField;
  private final JButton clearButton;
  JPopupMenu hoverPopup;
  private final AtomicBoolean isShowing;

  public static void install(JTextField textField) {
    textField.getDocument().addDocumentListener(new TextFieldListener(textField));
  }

  private ClearTextFieldOverlayButton(JTextField textField) {
    this.textField = textField;
    clearButton = new JButton();
    clearButton.setContentAreaFilled(false);
    clearButton.setOpaque(false);
    clearButton.setBorder(BorderFactory.createEmptyBorder());
    clearButton.setFocusPainted(false);
    clearButton.addActionListener(e -> textField.setText(null));
    hoverPopup = new JPopupMenu(){
      @Override
      public void setVisible(boolean b) {
        // Only hide if isShowing allows it
        // This prevents that the popup gets invisible if the focus is moved to another component.
        super.setVisible(b ? b : isShowing.get());
      }
    };
    hoverPopup.setOpaque(false);
    hoverPopup.setBorder(BorderFactory.createEmptyBorder());
    hoverPopup.setBorderPainted(false);
    hoverPopup.add(clearButton);
    isShowing = new AtomicBoolean(false);
  }

  public void show() {
    if (!isShowing.compareAndExchange(false, true)) {
      int iconSize = textField.getHeight() - 10;
      clearButton.setIcon(Icons.CANCEL.getImageIcon(iconSize));
      clearButton.doLayout();
      clearButton.setPreferredSize(new Dimension(iconSize, iconSize));
      int xPos = textField.getWidth() - clearButton.getPreferredSize().width - 5;
      int yPos = textField.getHeight() / 2 - clearButton.getPreferredSize().height / 2;
      hoverPopup.show(textField, xPos, yPos);

      // Use SwingUtilities.invokeLater to delay focus grab
      SwingUtilities.invokeLater(textField::grabFocus);
    }
  }

  public void hide() {
    if (isShowing.compareAndExchange(true, false)) {
      hoverPopup.setVisible(false);
      // Use SwingUtilities.invokeLater to delay focus grab
      SwingUtilities.invokeLater(textField::grabFocus);
    }
  }

  private static class TextFieldListener implements DocumentListener {

    private final JTextField textField;
    private final ClearTextFieldOverlayButton overlayButton;

    public TextFieldListener(JTextField textField) {
      this.textField = textField;
      overlayButton = new ClearTextFieldOverlayButton(textField);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      if (!textField.getText().isEmpty()) {
        overlayButton.show();
      }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      if (textField.getText().isEmpty()) {
        overlayButton.hide();
      }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      // nothing to do here
    }
  }
}
