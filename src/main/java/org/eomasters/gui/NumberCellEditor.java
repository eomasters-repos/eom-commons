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
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * A cell editor for numbers. If the input is not of type number the cell border is set to red.
 */
public class NumberCellEditor extends DefaultCellEditor {

  private final Class<? extends Number> numberClass;

  /**
   * Creates a new cell editor for numbers.
   *
   * @param numberClass the number class the input text needs to be compatible with
   */
  public NumberCellEditor(Class<? extends Number> numberClass) {
    super(new JTextField());
    this.numberClass = numberClass;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    final JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
    textField.selectAll();
    textField.setHorizontalAlignment(JTextField.RIGHT);
    return textField;
  }

  @Override
  public boolean stopCellEditing() {
    JTextField textField = (JTextField) getComponent();
    try {
      switch (numberClass.getSimpleName()) {
        case "Integer":
          Integer.parseInt(textField.getText());
          break;
        case "Long":
          Long.parseLong(textField.getText());
          break;
        case "Float":
          Float.parseFloat(textField.getText());
          break;
        case "Double":
          Double.parseDouble(textField.getText());
          break;
        default:
          throw new IllegalArgumentException("Unsupported number class: " + numberClass);
      }
    } catch (NumberFormatException ignored) {
      ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
      return false;
    }

    if (!super.stopCellEditing()) {
      ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
      return false;
    }

    return true;
  }
}
