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

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClearTextFieldOverlayButtonMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClearTextFieldOverlayButtonMain::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Create and set up the main frame
        JFrame frame = new JFrame("ClearTextFieldOverlayButton Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setPreferredSize(new Dimension(400, 300));

        // Create a panel with text fields
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create text fields with labels
        panel.add(createLabelAndTextField("Name:"));
        panel.add(createLabelAndTextField("Email:"));
        panel.add(createLabelAndTextField("Phone:"));
        
        // Add a button to clear all fields
        JButton clearAllButton = new JButton("Clear All Fields");
        clearAllButton.addActionListener(e -> {
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    Component[] subComponents = ((JPanel) component).getComponents();
                    for (Component subComponent : subComponents) {
                        if (subComponent instanceof JTextField) {
                            ((JTextField) subComponent).setText("");
                        }
                    }
                }
            }
        });
        
        // Add the panel and button to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(clearAllButton, BorderLayout.SOUTH);

        // Display the window
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Add a window listener for cleanup
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private static JPanel createLabelAndTextField(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(20);
        
        // Install the ClearTextFieldOverlayButton on this text field
        ClearTextFieldOverlayButton.install(textField);
        
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }
}
