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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.eomasters.icons.Icon;
import org.eomasters.icons.Icons;
import org.eomasters.utils.SystemHelper;

/**
 * A collapsible panel. The component set by {@link #setContent(JComponent)} is collapsed when clicked on the title.
 */
public class CollapsiblePanel extends JPanel {

  private final JLabel toggleLabel;
  private final JComponent contentPanel;

  /**
   * Creates a new collapsible panel with the given title.
   *
   * @param title the title
   */
  public CollapsiblePanel(String title) {
    super(new BorderLayout(5, 5));
    setName("CollapsiblePanel." + title.replaceAll(" ", "_"));
    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    MouseAdapter collapser = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setCollapsed(contentPanel.isVisible());
      }
    };

    JPanel titlePanel = new JPanel(new MigLayout("flowx, insets 5 0 5 0", "[][fill, grow]"));
    titlePanel.addMouseListener(collapser);
    JLabel titleLabel = new JLabel(title);
    titleLabel.addMouseListener(collapser);
    titlePanel.add(titleLabel, "top, left");
    toggleLabel = new JLabel();
    toggleLabel.addMouseListener(collapser);
    titlePanel.add(toggleLabel, "top, left");

    add(titlePanel, BorderLayout.NORTH);

    contentPanel = new JPanel(new MigLayout("fill, insets 0, hidemode 3"));
    add(contentPanel, BorderLayout.CENTER);

    doLayout();

    setCollapsed(true);
  }

  /**
   * Creates a collapsible panel with a long text. And buttons to export the text to a file or the clipboard.
   *
   * @param title the title
   * @param text  the long text
   * @return the collapsible panel
   */
  public static CollapsiblePanel createLongTextPanel(String title, String text) {

    JTextArea textArea = new JTextArea(text);
    textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, textArea.getFont().getSize()));
    textArea.setColumns(70);
    textArea.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    textArea.setTabSize(4);
    textArea.setEditable(false);
    textArea.setRows(20);
    JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JPanel reportPreview = new JPanel(new MigLayout("fill"));
    reportPreview.add(scrollPane, "top, left, grow, pushx, wrap");
    CollapsiblePanel collapsiblePanel = new CollapsiblePanel(title);
    JButton exportBtn = createExportButton(collapsiblePanel, textArea);
    boolean headless = SystemHelper.isHeadless();
    reportPreview.add(exportBtn, "top, left" + (!headless ? ", split 2" : ""));
    if (!headless) {
      JButton clipboardBtn = new JButton("Copy to Clipboard");
      clipboardBtn.addActionListener(e -> {
        StringSelection contents = new StringSelection(textArea.getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, contents);
      });
      reportPreview.add(clipboardBtn, "top, left");
    }

    collapsiblePanel.setContent(reportPreview);
    return collapsiblePanel;
  }

  /**
   * Sets the content of this panel which can be collapsed.
   *
   * @param content the content
   */
  public void setContent(JComponent content) {
    contentPanel.removeAll();
    contentPanel.add(content, "grow, pushx");
    contentPanel.invalidate();
  }

  @Override
  public void revalidate() {
    super.revalidate();
    Window windowAncestor = SwingUtilities.getWindowAncestor(this);
    if (windowAncestor != null) {
      windowAncestor.pack();
    }
  }

  /**
   * Sets the collapsed state of this panel.
   *
   * @param collapse true to collapse, false to expand
   */
  public void setCollapsed(boolean collapse) {
    if (collapse) {
      toggleLabel.setIcon(Icons.ARROW_DOWN.getImageIcon(Icon.SIZE_16));
      contentPanel.setVisible(false);
    } else {
      toggleLabel.setIcon(Icons.ARROW_UP.getImageIcon(Icon.SIZE_16));
      contentPanel.setVisible(true);
    }
    revalidate();
  }

  // TODO: move to FileIo
  private static JButton createExportButton(JPanel contentPane, JTextArea textArea) {
    JButton exportBtn = new JButton("Export to File");
    exportBtn.addActionListener(e -> {
      FileIo exporter = new FileIo("Export Text to File");
      exporter.setParent(contentPane);
      exporter.setFileFilters(FileIo.createFileFilter("Text file", "txt"));
      exporter.save(outputStream -> outputStream.write(textArea.getText().getBytes(StandardCharsets.UTF_8)));
    });
    return exportBtn;
  }

  /**
   * Main method for testing.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame("Collapsible Panel Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CollapsiblePanel panel = new CollapsiblePanel("Title");
    panel.setContent(new JLabel("Content"));
    frame.getContentPane().add(panel);

    frame.pack();
    frame.setVisible(true);
  }
}
