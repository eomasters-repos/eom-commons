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

package org.eomasters.utils;

import java.awt.Color;

public class Colors {

  /**
   * Creates a color from a rgba string. The string should start with "#" followed with a 6 digit hex value.
   *
   * @param rgb   the rgba string in the form '#000000'
   * @param alpha the alpha value
   * @return the created color
   */
  public static Color create(String rgb, double alpha) {
    if (rgb.length() > 7) {
      rgb = rgb.substring(0, 7);
    }
    int clippedAlpha = (int) (255 * Math.max(0, Math.min(1, alpha)));
    String alphaHex = Integer.toHexString(clippedAlpha);
    if (alphaHex.length() < 2) {
      alphaHex = "0" + alphaHex;
    }
    String rgba = rgb + alphaHex;
    return parseRGBA(rgba);
  }

  /**
   * Creates a color from a rgba string. The string should start with "#" followed with a 6 or 8 digit hex value.
   * If only 6 digits (RGB) are provided, the alpha channel is set to be fully opaque.
   *
   * @param rgba the rgba string in the form '#000000(00)'
   * @return the created color
   */
  public static Color create(String rgba) {
    return parseRGBA(rgba);
  }

  private static Color parseRGBA(String rgba) {
    // consider alpha only if the rgba string is long enough

    int r = Integer.parseInt(rgba.substring(1, 3), 16);
    int g = Integer.parseInt(rgba.substring(3, 5), 16);
    int b = Integer.parseInt(rgba.substring(5, 7), 16);
    if (rgba.length() > 7) {
      return new Color(r, g, b, Integer.parseInt(rgba.substring(7, 9), 16));
    } else {
      return new Color(r, g, b);
    }
  }

}
