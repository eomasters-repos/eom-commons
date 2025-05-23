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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.eomasters.utils.ErrorHandler;

/**
 * Adapter which enables Paste Clipboard functionality on JTables.
 */
public class TablePasteAdapter extends KeyAdapter {

  private final JTable table;

  /**
   * The Excel Adapter is constructed with a JTable on which it enables Copy-Paste and acts as a Clipboard listener.
   */
  public TablePasteAdapter(JTable table) {
    this.table = table;
  }

  /**
   * This method is activated on the Keystrokes we are listening to in this implementation. Here it listens for Copy and
   * Paste ActionCommands. Selections comprising non-adjacent cells result in invalid selection and then copy action
   * cannot be performed. Paste is done by aligning the upper left corner of the selection with the 1st element in the
   * current selection of the JTable.
   */
  public void keyPressed(KeyEvent e) {
    if (InputEvent.CTRL_DOWN_MASK == e.getModifiersEx() && KeyEvent.VK_V == e.getKeyCode()) {
      int[] selectedRows = table.getSelectedRows();
      if (selectedRows.length == 0) {
        return;
      }
      int startRow = selectedRows[0];
      int[] selectedColumns = table.getSelectedColumns();
      if (selectedColumns.length == 0) {
        return;
      }
      int startCol = selectedColumns[0];
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      String[] lines = canBePasted(clipboard, startCol, startRow);
      if (lines == null) {
        return;
      }

      TableModel model = table.getModel();
      for (int y = 0; y < lines.length; y++) {
        String line = lines[y];
        String[] rowCells = line.split("\t");
        int row = startRow + y;
        for (int x = 0; x < rowCells.length; x++) {
          int column = startCol + x;
          model.setValueAt(rowCells[x], row, column);
        }
      }
    }
  }

  private String[] canBePasted(Clipboard system, int startCol, int startRow) {
    try {
      String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
      String[] lines = new BufferedReader(new StringReader(trstring)).lines().toArray(String[]::new);
      if (lines.length == 0) {
        return null;
      }
      int numCols = lines[0].split("\t").length;
      if (numCols == 0) {
        return null;
      }
      if (startCol + numCols > table.getColumnCount()) {
        ErrorHandler.handleError("Cannot paste", "Cannot paste. Not enough columns in table.");
        return null;
      }
      if (startRow + lines.length > table.getRowCount()) {
        ErrorHandler.handleError("Cannot paste", "Cannot paste. Not enough rows in table.");
        return null;
      }

      for (int y = 0; y < lines.length; y++) {
        int row = startRow + y;
        for (int x = 0; x < numCols; x++) {
          int column = startCol + x;
          if (!table.getModel().isCellEditable(row, column)) {
            ErrorHandler.handleError("Cannot paste", "Cannot paste. Cell is not editable.");
            return null;
          }
        }
      }
      return lines;
    } catch (UnsupportedFlavorException | IOException ex) {
      ErrorHandler.handleError("Cannot paste", "Invalid clipboard content.", ex);
    }
    return null;
  }


}
