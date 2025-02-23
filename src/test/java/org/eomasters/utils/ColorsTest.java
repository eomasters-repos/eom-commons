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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import org.junit.jupiter.api.Test;

class ColorsTest {

  @Test
  void createWithValidRGBA() {
    Color color = Colors.create("#12345678");
    assertEquals(18, color.getRed());
    assertEquals(52, color.getGreen());
    assertEquals(86, color.getBlue());
    assertEquals(120, color.getAlpha());
  }

  @Test
  void createWithOnlyRGB() {
    Color color = Colors.create("#123456");
    assertEquals(18, color.getRed());
    assertEquals(52, color.getGreen());
    assertEquals(86, color.getBlue());
    assertEquals(255, color.getAlpha());
  }

  @Test
  void createWithAlpha() {
    Color color = Colors.create("#FFAA06", 0.5);
    assertEquals(255, color.getRed());
    assertEquals(170, color.getGreen());
    assertEquals(6, color.getBlue());
    assertEquals(127, color.getAlpha());
  }

  @Test
  void createWithAlphaAboveMax() {
    Color color = Colors.create("#FFAA06", 3.5);
    assertEquals(255, color.getRed());
    assertEquals(170, color.getGreen());
    assertEquals(6, color.getBlue());
    assertEquals(255, color.getAlpha());
  }

  @Test
  void createWithAlphaBelowMin() {
    Color color = Colors.create("#FFAA06", -0.5);
    assertEquals(255, color.getRed());
    assertEquals(170, color.getGreen());
    assertEquals(6, color.getBlue());
    assertEquals(0, color.getAlpha());
  }

}
