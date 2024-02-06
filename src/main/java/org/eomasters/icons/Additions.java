/*-
 * ========================LICENSE_START=================================
 * EOM Commons - Library of common utilities for Java
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2024 Marco Peters
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

package org.eomasters.icons;

/**
 * Eases access to additions. These can be used with other Icons.
 */
public final class Additions {

  /**
   * The eye addition for icons.
   */
  public static final Icon EYE = new RasterIcon("/icons/" + "additions/Eye_addition");
  /**
   * The Minus addition for icons.
   */
  public static final Icon MINUS = new RasterIcon("/icons/" + "additions/Minus_addition");
  /**
   * The Plus addition for icons.
   */
  public static final Icon PLUS = new RasterIcon("/icons/" + "additions/Plus_addition");

  private Additions() {
  }

}
