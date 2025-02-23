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

import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class HighlighterMain {

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      try {
        final JFrame frame = new JFrame("Asset Library GUI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new MigLayout("", "[][fill, grow]"));
        panel.add(new JLabel("Test 1:"));
        JTextField highlightedComponent1 = new JTextField("Test");
        panel.add(highlightedComponent1, "wrap");
        highlightedComponent1.addFocusListener(new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            Highlighter.error(highlightedComponent1, "Nanana!");
          }
        });
        panel.add(new JLabel("Test 2:"));
        JTextField highlightedComponent2 = new JTextField("Test");
        panel.add(highlightedComponent2);
        highlightedComponent2.addFocusListener(new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            Highlighter.error(highlightedComponent2, "Nanana! This is a long error message");
          }
        });
        frame.setContentPane(panel);
        frame.setLocation(100, 650);
        frame.setVisible(true);
        frame.pack();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

}
