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

package org.eomasters.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.List;
import org.junit.jupiter.api.Test;

class GlobalGridTest {

  @Test
  void testGetTilePosition() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3 / 36000.0);
    assertEquals(new Point(-180, 90), grid.getCellId(-180, 90));
    assertEquals(new Point(-180, 90), grid.getCellId(-178, 90));
    assertEquals(new Point(-180, -81), grid.getCellId(180, -82));
    assertEquals(new Point(-180, -87), grid.getCellId(180, -90));
    assertEquals(new Point(-177, 81), grid.getCellId(-176, 80));
    assertEquals(new Point(-15, 39), grid.getCellId(-15, 39));
  }

  @Test
  void testGetAllIds_global() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3 / 36000.0);
    Point[] cells = grid.getAllIds();
    assertEquals(360 / 3 * 180 / 3, cells.length);
    assertEquals(new Point(-180, 90), cells[0]);
    assertEquals(new Point(177, -87), cells[cells.length - 1]);
  }

  @Test
  void testGetAllIds_withBounds() {
    GlobalGrid grid = new GlobalGrid(20, 20,1);
    grid.setGridBounds(60, -60);
    Point[] cells = grid.getAllIds();
    assertEquals(108, cells.length);
    assertEquals(new Point(-180, 60), cells[0]);
    assertEquals(new Point(160, -40), cells[cells.length - 1]);
  }

  @Test
  void createSurroundingCellIds_byId() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3 / 36000.0);
    Point[] cells = grid.getSurroundingCellIds(new Point(0, 0));
    assertEquals(9, cells.length);
    assertEquals(new Point(-3, 3), cells[0]);
    assertEquals(new Point(3, -3), cells[cells.length - 1]);
    cells = grid.getSurroundingCellIds(new Point(15, 39));
    assertEquals(9, cells.length);
    assertEquals(new Point(12, 42), cells[0]);
    assertEquals(new Point(18, 36), cells[cells.length - 1]);
    cells = grid.getSurroundingCellIds(new Point(-177, -30));
    assertEquals(9, cells.length);
    assertEquals(new Point(-180, -27), cells[0]);
    assertEquals(new Point(-174, -33), cells[cells.length - 1]);

    // wrap around the anti-meridian
    cells = grid.getSurroundingCellIds(new Point(-180, -30));
    assertEquals(9, cells.length);
    assertEquals(new Point(177, -27), cells[0]);
    assertEquals(new Point(-177, -33), cells[cells.length - 1]);

    cells = grid.getSurroundingCellIds(new Point(177, -30));
    assertEquals(9, cells.length);
    assertEquals(new Point(174, -27), cells[0]);
    assertEquals(new Point(-180, -33), cells[cells.length - 1]);

    // clipped at the poles
    cells = grid.getSurroundingCellIds(new Point(177, 90));
    assertEquals(6, cells.length);
    assertEquals(new Point(174, 90), cells[0]);
    assertEquals(new Point(-180, 87), cells[cells.length - 1]);

    cells = grid.getSurroundingCellIds(new Point(3, -87));
    assertEquals(6, cells.length);
    assertEquals(new Point(0, -84), cells[0]);
    assertEquals(new Point(6, -87), cells[cells.length - 1]);

  }

  @Test
  void createSurroundingCellIds_byGeo() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3 / 36000.0);
    Point[] cells = grid.getSurroundingCellIds(15, 39);
    assertEquals(9, cells.length);
    assertEquals(new Point(12, 42), cells[0]);
    assertEquals(new Point(18, 36), cells[cells.length - 1]);

    cells = grid.getSurroundingCellIds(-180, -30);
    assertEquals(9, cells.length);
    assertEquals(new Point(177, -27), cells[0]);
    assertEquals(new Point(-177, -33), cells[cells.length - 1]);

    cells = grid.getSurroundingCellIds(3, -90);
    assertEquals(6, cells.length);
    assertEquals(new Point(0, -84), cells[0]);
    assertEquals(new Point(6, -87), cells[cells.length - 1]);
  }

  @Test
  void testGetCellIdAtTheEdgesWithPixelSize() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3/36000.0);
    Point cellId;
    cellId = grid.getCellId(-120, 57.000018);
    assertEquals(new Point(-120, 57), cellId);
    cellId = grid.getCellId(-117.000018, 57.000018);
    assertEquals(new Point(-117, 57), cellId);
    cellId = grid.getCellId(2.999967, 52.284484);
    assertEquals(new Point(3, 54), cellId);
    cellId = grid.getCellId(3.99967, -2.9999967);
    assertEquals(new Point(3, -3), cellId);
    cellId = grid.getCellId(5.999978, 52.022732);
    assertEquals(new Point(6, 54), cellId);
  }

  @Test
  void getIntersectedCells_3degree() {
    GlobalGrid grid = new GlobalGrid(3, 3, 3 / 36000.0);
    List<Point> cells = grid.getIntersectedCells(-180, -90, 180, 90);
    assertEquals(360 / 3 * 180 / 3, cells.size());
    assertEquals(new Point(-180, 90), cells.get(0));
    assertEquals(new Point(177, -87), cells.get(cells.size() - 1));

    cells = grid.getIntersectedCells(-4, -35, 2, -30);
    assertEquals(6, cells.size());
    assertEquals(new Point(-6, -30), cells.get(0));
    assertEquals(new Point(0, -33), cells.get(cells.size() - 1));
  }

  @Test
  void getIntersectedCells_20degree() {
    GlobalGrid grid = new GlobalGrid(20, 20, 1);
    grid.setGridBounds(60, -60);
    List<Point> cells = grid.getIntersectedCells(-180, -90, 180, 90);
    assertEquals(108, cells.size());
    assertEquals(new Point(-180, 60), cells.get(0));
    assertEquals(new Point(160, -40), cells.get(cells.size() - 1));
  }

  @Test
  void cellIdShouldBeParsedCorrectly() {
    assertEquals(new Point(-180, 90), GlobalGrid.parseCellId("N90W180"));
    assertEquals(new Point(-180, -90), GlobalGrid.parseCellId("S90W180"));
    assertEquals(new Point(180, -90), GlobalGrid.parseCellId("S90E180"));
    assertEquals(new Point(180, 90), GlobalGrid.parseCellId("N90E180"));
  }
}

