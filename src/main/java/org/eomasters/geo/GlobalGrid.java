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

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines a global grid of cells.
 * <p>
 * Each grid cell is identified by the upper left corner of the cell. The cell identifier is a Point object with the x
 * coordinate representing the longitude and the y coordinate representing the latitude.
 * </p>
 * <p>
 * The grid starts in the upper left corner and proceeds to the right and then down. The first cell is at -180 degrees
 * of longitude and 90 degrees (default) of latitude. The second cell is at -177 degrees of longitude and 90 degrees of
 * latitude.
 * </p>
 */
public class GlobalGrid {

  private final int cellWidth;
  private final int cellHeight;
  private final double pixelSize;
  private int northBound;
  private int southBound;
  private final static int NORTH_BOUND = 90;
  private final static int SOUTH_BOUND = -90;
  private final static int WEST_BOUND = -180;
  private final static int EAST_BOUND = 180;

  /**
   * Creates a new global grid with the specified cell size. The grid spans 360 degrees of longitude and 180 degrees of
   * latitude. The upper left corner of the grid is -180 degrees of longitude and 90 degrees of latitude. The lower
   * right corner of the grid is 180 degrees of longitude and -90 degrees of latitude. The longitude value 180 is
   * considered to be equivalent to -180.
   *
   * @param cellWidth  the width of each cell in degrees of longitude
   * @param cellHeight the height of each cell in degrees of latitude
   * @param pixelSize  the size of each pixel in degrees of longitude
   */
  public GlobalGrid(int cellWidth, int cellHeight, double pixelSize) {
    this.cellWidth = cellWidth;
    this.cellHeight = cellHeight;
    this.pixelSize = pixelSize;
    setGridBounds(NORTH_BOUND, SOUTH_BOUND);
  }

  /**
   * Sets the bounds of the grid. The bounds are specified as the northern and southern latitude limits of the grid.
   *
   * @param north the northern latitude limit
   * @param south the southern latitude limit
   */
  public void setGridBounds(int north, int south) {
    this.northBound = north;
    this.southBound = south;
  }

  /**
   * Returns the cell identifier for the cell which contains the provided latitude and longitude. The cell identifier is
   * a Point object with the x coordinate representing the longitude and the y coordinate representing the latitude. If
   * the provided latitude and longitude are outside the grid bounds, null is returned.
   *
   * @param grid the grid
   * @param lon  the longitude
   * @param lat  the latitude
   * @return a Point object representing the cell identifier
   */
  public static Point getCellID(GlobalGrid grid, int lon, int lat) {
    if (grid.isInGridBounds(lon, lat)) {
      return grid.getCellId(lon, lat);
    } else {
      return null;
    }
  }

  /**
   * Parses a cell identifier string and returns a Point object representing the cell identifier. The cell identifier is
   * a Point object with the x coordinate representing the longitude and the y coordinate representing the latitude.
   * <p>
   * The cell identifier is formatted as follows:
   * <ul>
   *   <li>the latitude is prefixed with "N" or "S" depending on whether it is positive or negative</li>
   *   <li>the latitude is formatted as a two digit number with leading zeros</li>
   *   <li>the longitude is prefixed with "E" or "W" depending on whether it is positive or negative</li>
   *   <li>the longitude is formatted as a three digit number with leading zeros</li>
   * </ul>
   *
   * @param cellString the cell identifier to be parsed
   * @return a Point object representing the cell identifier
   * @throws IllegalArgumentException if the cell identifier is not 7 characters long
   */
  public static Point parseCellId(String cellString) {
    if (cellString.length() != 7) {
      throw new IllegalArgumentException("Cell identifier must be 7 characters long");
    }
    int lat = Integer.parseInt(cellString.substring(1, 3));
    if (cellString.charAt(0) == 'S') {
      lat *= -1;
    }
    int lon = Integer.parseInt(cellString.substring(4, 7));
    if (cellString.charAt(3) == 'W') {
      lon *= -1;
    }
    return new Point(lon, lat);
  }

  /**
   * Returns the width of the grid in longitude degrees.
   *
   * @return the width of the grid in longitude degrees
   */
  public int getGridWidth() {
    return 360;
  }

  /**
   * Returns the height of the grid in latitude degrees.
   *
   * @return the height of the grid in latitude degrees
   */
  public int getGridHeight() {
    return (northBound + 90) - (southBound + 90);
  }

  /**
   * Returns the width of a cell in degrees.
   *
   * @return the width of a cell in degrees
   */
  public double getCellWidth() {
    return cellWidth;
  }

  /**
   * Returns the height of a cell in degrees.
   *
   * @return the height of a cell in degrees
   */
  public double getCellHeight() {
    return cellHeight;
  }

  /**
   * Returns the size of a pixel in degrees.
   *
   * @return the size of a pixel in degrees
   */
  public double getPixelSize() {
    return pixelSize;
  }

  public boolean isInGridBounds(double lon, double lat) {
    return lon >= WEST_BOUND && lon <= EAST_BOUND && lat >= southBound && lat <= northBound;
  }

  /**
   * Formats the cell identifier as a string. The cell identifier is a Point object with the x coordinate representing
   * the longitude and the y coordinate representing the latitude.
   * <p>
   * The cell identifier is formatted as follows:
   * <ul>
   *   <li>the latitude is prefixed with "N" or "S" depending on whether it is positive or negative</li>
   *   <li>the latitude is formatted as a two digit number with leading zeros</li>
   *   <li>the longitude is prefixed with "E" or "W" depending on whether it is positive or negative</li>
   *   <li>the longitude is formatted as a three digit number with leading zeros</li>
   * </ul>
   *
   * @param cellId the cell identifier to be formatted
   * @return a string representing the cell identifier
   */
  public static String formatCellId(Point cellId) {
    StringBuilder sb = new StringBuilder();
    if (cellId.y >= 0) {
      sb.append("N");
    } else {
      sb.append("S");
    }
    sb.append(String.format("%02d", Math.abs(cellId.y)));
    if (cellId.x < 0) {
      sb.append("W");
    } else {
      sb.append("E");
    }
    return sb.append(String.format("%03d", Math.abs(cellId.x))).toString();
  }

  /**
   * Returns a list of cell identifiers for the cells intersected by the provided bounding box. The cell identifiers are
   * ordered from the upper left corner to the lower right corner.
   *
   * @param minX the minimum longitude of the bounding box
   * @param minY the minimum latitude of the bounding box
   * @param maxX the maximum longitude of the bounding box
   * @param maxY the maximum latitude of the bounding box
   * @return a list of cell identifiers for the cells intersected by the bounding box
   */
  public List<Point> getIntersectedCells(double minX, double minY, double maxX, double maxY) {
    List<Point> cellIds = new ArrayList<>();
    Point ulCellId = getCellId(Math.max(minX, WEST_BOUND), Math.min(maxY, northBound));
    Point lrCellId = getCellId(Math.min(maxX, EAST_BOUND - cellWidth / 2.0), Math.max(minY, southBound));
    minX = ulCellId.x;
    maxY = ulCellId.y;
    maxX = lrCellId.x;
    minY = lrCellId.y;
    for (int lat = (int) Math.ceil(maxY); lat >= (int) Math.floor(minY); lat -= cellHeight) {
      for (int lon = (int) Math.floor(minX); lon <= (int) Math.ceil(maxX); lon += cellWidth) {
        cellIds.add(new Point(lon, lat));
      }
    }
    return cellIds;
  }

  /**
   * Returns a list of cell identifiers for the cells intersected by the bounding box defined by the provided longitude
   * start and width. In latitude the bounding box spans from the northern to the southern bound of the grid.
   *
   * @param lonStart the start longitude
   * @param lonWidth the width of the bounding box
   * @return a list of cell identifiers for the cells intersected by the bounding box
   */
  public List<Point> getGlobalCellIdStripe(int lonStart, int lonWidth) {
    return getGlobalCellIdStripe(lonStart, lonWidth, northBound);
  }

  /**
   * Returns a list of cell identifiers for the cells intersected by the bounding box defined by the provided longitude
   * start and width and the latitude start. The latitude width spans from <code>startLat</code> to the southern bound
   * of the grid.
   *
   * @param lonStart the start longitude
   * @param lonWidth the width of the bounding box
   * @param startLat the start latitude
   * @return a list of cell identifiers for the cells intersected by the bounding box
   */
  public List<Point> getGlobalCellIdStripe(int lonStart, int lonWidth, int startLat) {
    List<Point> cellIds = new ArrayList<>();
    startLat = Math.min(startLat, northBound);
    for (int lat = startLat; lat > southBound; lat -= cellHeight) {
      for (int lon = lonStart; lon < lonStart + lonWidth; lon += cellWidth) {
        cellIds.add(new Point(lon, lat));
      }
    }
    return cellIds;
  }

  /**
   * Calculates the upper left corner of the cell which contains the provided latitude and longitude. The calculation is
   * done by dividing the coordinates by the tile width and height, flooring the result, multiplying it by the tile
   * width and height, and subtracting the offset to get the tile's upper left corner.
   * <p>
   * The longitude is converted to the range -180 to 180, so that it wraps around the globe. The latitude is clipped at
   * the poles.
   *
   * @param lon the longitude for which the tile position is to be calculated
   * @param lat the latitude for which the tile position is to be calculated
   * @return a Point object representing the upper left corner of the tile
   * @throws IllegalArgumentException if the longitude or latitude is outside the grid bounds
   * @see #isInGridBounds(double, double)
   */
  public Point getCellId(double lon, double lat) {
    // consider the half-pixel offset which is already in the adjacent cell
    double latDistance = getDistanceToBorder(lat, cellHeight);
    if (latDistance > 0 && latDistance < (pixelSize / 2)) {
      lat = lat - (pixelSize / 2);
    }
    double lonDistance = getDistanceToBorder(lon, cellWidth);
    if (lonDistance > 0 && lonDistance < (pixelSize / 2)) {
      lon = lon + (pixelSize / 2);
    }
    // convert longitude to range -180 to 180, so that it wraps around the globe
    lon = normalizeLon(lon);
    // convert latitude to range -90 to 90, so that it is clipped at the poles
    lat = clipLat(lat);
    if (!isInGridBounds(lon, lat)) {
      throw new IllegalArgumentException(
          String.format("Cell-X must be between %d and %d, Cell-Y must be between %d and %d",
              WEST_BOUND, EAST_BOUND, southBound, northBound));
    }
    int x = (int) Math.floor((lon + 180) % 360 / cellWidth) * cellWidth - 180;
    int y;
    if (lat < 0) {
      y = ((int) Math.floor((lat * -1) / cellHeight) * cellHeight) * -1;
      if (y == southBound) {
        // Special case for the south pole
        y = southBound + cellHeight;
      }
    } else {
      y = ((int) Math.ceil(lat / cellHeight) * cellHeight);
    }
    return new Point(x, y);
  }

  private double getDistanceToBorder(double ordinate, int cellSpan) {
    double remaining = Math.abs(ordinate) % cellSpan;
    if(remaining <= pixelSize) {
      return remaining;
    }else {
      return cellSpan - remaining;
    }
  }

  /**
   * Returns an array of all the cell positions in the grid.
   *
   * @return an array of Point objects representing the upper left corner of each cell in the grid
   */
  public Point[] getAllIds() {
    Point[] cellPositions = new Point[(getGridWidth() / cellWidth) * (getGridHeight() / cellHeight)];
    int i = 0;
    for (int lat = northBound; lat > southBound; lat -= cellHeight) {
      for (int lon = WEST_BOUND; lon < EAST_BOUND; lon += cellWidth) {
        cellPositions[i++] = new Point(lon, lat);
      }
    }
    return cellPositions;
  }

  /**
   * Returns an array of the cell positions surrounding the given longitude and latitude.
   *
   * @param lon the longitude
   * @param lat the latitude
   * @return an array of cell ids
   */
  public Point[] getSurroundingCellIds(double lon, double lat) {
    if (!isInGridBounds(lon, lat)) {
      throw new IllegalArgumentException(
          String.format("Cell-X must be between %d and %d, Cell-Y must be between %d and %d",
              WEST_BOUND, EAST_BOUND, southBound, northBound));
    }
    return getSurroundingCellIds(getCellId(lon, lat));
  }

  /**
   * Returns an array of the cell positions surrounding the given cell position. The array is ordered from the upper
   * left corner to the lower right corner.
   *
   * @param cellId the cell position for which the surrounding cell positions are to be calculated
   * @return an array of Point objects representing the upper left corner of each cell in the grid
   */
  public Point[] getSurroundingCellIds(Point cellId) {
    if (cellId.y < southBound + cellHeight || cellId.y > northBound) {
      throw new IllegalArgumentException(String.format("Cell-Y must be between %d and %d", southBound + cellHeight,
          northBound));
    }
    if (cellId.x < WEST_BOUND || cellId.x > EAST_BOUND) {
      throw new IllegalArgumentException(String.format("Cell-X must be between %d and %d", WEST_BOUND, EAST_BOUND));
    }
    Set<Point> cellIds = new LinkedHashSet<>();
    int cell0x = cellId.x - cellWidth;
    int cell0y = cellId.y + cellHeight;
    for (int i = 0; i < 9; i++) {
      int cellX = cell0x + (i % 3) * cellWidth;
      int cellY = cell0y - (i / 3) * cellHeight;
      // convert longitude to range -180 to 180, so that it wraps around the globe
      cellX = (int) normalizeLon(cellX);
      // clip to latitude bounds
      cellY = (int) clipCellY(cellY);
      cellIds.add(new Point(cellX, cellY));
    }
    return cellIds.toArray(new Point[0]);
  }


  private static double clipLat(double lat) {
    return Math.min(Math.max(lat, -90), 90);
  }

  private double clipCellY(int cellY) {
    return Math.min(Math.max(cellY, southBound + cellHeight), northBound);
  }

  private static double normalizeLon(double lon) {
    lon %= 360.0;
    if (lon < -180.0) {
      lon += 360.0;
    } else if (lon >= 180.0) {
      lon -= 360.0;
    }
    return lon;
  }
}
