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

import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import net.miginfocom.swing.MigLayout;


public class UriField extends JPanel {

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel(new MigLayout("top, left, gap 10, insets 0"));
    frame.add(panel);
    frame.setLocationRelativeTo(null);

    UriField uriField1 = new UriField();
    uriField1.setEditable(true);
    UriField uriField2 = new UriField("https://www.eomasters.org");
    uriField2.setEditable(true);
    UriField uriField3 = new UriField("https://www.eomasters.org", "EOMasters");
    uriField3.setEditable(true);

    Checkbox editable1 = new Checkbox("Editable", true);
    editable1.addItemListener(e -> {
      uriField1.setEditable(e.getStateChange() == ItemEvent.SELECTED);
    });
    Checkbox editable2 = new Checkbox("Editable", true);
    editable2.addItemListener(e -> {
      uriField2.setEditable(e.getStateChange() == ItemEvent.SELECTED);
    });
    Checkbox editable3 = new Checkbox("Editable", true);
    editable3.addItemListener(e -> {
      uriField3.setEditable(e.getStateChange() == ItemEvent.SELECTED);
    });
    panel.add(editable1);
    panel.add(uriField1, "grow, pushx, wrap");
    panel.add(editable2);
    panel.add(uriField2, "grow, wrap");
    panel.add(editable3);
    panel.add(uriField3, "grow, wrap");

    frame.pack();
    SwingUtilities.invokeLater(() -> frame.setVisible(true));
  }


  private static final String VIEW_CARD = "view";
  private static final String EDIT_CARD = "edit";
  private static final boolean IS_BROWSING_SUPPORTED =
      Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
  private final CardLayout cards;
  private final JTextField viewField;
  private final JTextField labelField;
  private final JTextField uriField;
  private final TextEditListener editListener = new TextEditListener();
  private boolean editable;
  private Cursor previousCursor;
  private String label;
  private String uri;


  public UriField() {
    this(null, null);
  }

  public UriField(String uri) {
    this(uri, uri);
  }

  public UriField(String uri, String label) {
    this.uri = uri;
    this.label = label;
    cards = new CardLayout();
    setLayout(cards);

    JPanel viewCard = new JPanel(new MigLayout("top, left, fillx, insets 0"));
    viewField = new JTextField();
    viewField.setForeground(Color.BLUE.darker());
    viewField.setEditable(false);
    viewField.setToolTipText("Click to open link");
    viewField.setFont(
        UIManager.getFont("TextField.font").deriveFont(Map.of(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
    MouseHandler mouseHandler = new MouseHandler(this);
    viewField.addMouseListener(mouseHandler);
    viewField.addMouseMotionListener(mouseHandler);
    viewCard.add(viewField, "top, left, growx, pushx, wrap");

    JPanel editCard = new JPanel(new MigLayout("top, left, fillx, gapx 2, insets 0"));
    labelField = new PromptTextField("Enter label");
    Dimension labelFieldPreferredSize = labelField.getPreferredSize();
    labelFieldPreferredSize.width = 100;
    labelField.setPreferredSize(labelFieldPreferredSize);
    labelField.setToolTipText("Specify the label of the link");
    labelField.getDocument().addDocumentListener(editListener);
    uriField = new PromptTextField("Enter URL");
    Dimension uriFieldPreferredSize = uriField.getPreferredSize();
    uriFieldPreferredSize.width = 100;
    uriField.setPreferredSize(uriFieldPreferredSize);
    uriField.setToolTipText("Specify the URL of the link");
    uriField.getDocument().addDocumentListener(editListener);
    editCard.add(labelField, "top, left, growx 40, pushx");
    editCard.add(uriField, "top, left, growx 60, pushx, wrap");

    add(viewCard, VIEW_CARD);
    add(editCard, EDIT_CARD);

    updateState();
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
    updateState();
  }

  public void setUri(String uri) {
    setUri(uri, uri);
  }

  public void setUri(String uri, String label) {
    this.uri = uri;
    if (label != null) {
      this.label = label;
    } else {
      this.label = uri;
    }
    updateState();
  }

  public String getUri() {
    return uri;
  }

  public String getLabel() {
    return label;
  }

  public void updateState() {
    if (editable) {
      cards.show(this, EDIT_CARD);
      setTextWithoutEvent(labelField, label);
      labelField.setCaretPosition(0);
      setTextWithoutEvent(uriField, uri);
      uriField.setCaretPosition(0);
    } else {
      cards.show(this, VIEW_CARD);
      viewField.setText(label);
      viewField.setCaretPosition(0);
    }
  }

  private void setTextWithoutEvent(JTextField field, String text) {
    field.getDocument().removeDocumentListener(editListener);
    field.setText(text);
    field.getDocument().addDocumentListener(editListener);
  }

  private boolean isClickable() {
    if (IS_BROWSING_SUPPORTED) {
      return !editable && uri != null && !uri.isEmpty();
    }
    return false;
  }

  private void setHandCursor() {
    previousCursor = getCursor();
    setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  private void resetCursor() {
    setCursor(previousCursor != null ? previousCursor : new Cursor(Cursor.DEFAULT_CURSOR));
  }

  private static class MouseHandler extends MouseInputAdapter {

    private final UriField uriField;

    public MouseHandler(UriField uriField) {
      this.uriField = uriField;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (uriField.isClickable()) {
        try {
          Desktop.getDesktop().browse(URI.create(uriField.uri));
        } catch (Throwable t) {
          JOptionPane.showMessageDialog(uriField,
              String.format("Cannot open URL: %s", uriField.uri), "Cannot open URL", JOptionPane.ERROR_MESSAGE);
        }
        super.mouseClicked(e);
      }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      if (uriField.isClickable()) {
        uriField.setHandCursor();
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      if (uriField.isClickable()) {
        uriField.setHandCursor();
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      uriField.resetCursor();
    }

  }

  private class TextEditListener implements DocumentListener {

    public TextEditListener() {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      update();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      update();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    private void update() {
      UriField.this.label = labelField.getText();
      UriField.this.uri = uriField.getText();

    }
  }

}
