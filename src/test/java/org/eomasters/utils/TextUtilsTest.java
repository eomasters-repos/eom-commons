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

package org.eomasters.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TextUtilsTest {

  @Test
  void testInvalidMailAddresses() {
    assertFalse(TextUtils.isValidEmailAddress("illegal@@email.com"));
    assertFalse(TextUtils.isValidEmailAddress("illegal@email"));
  }

  @Test
  void testValidMailAddresses() {
    assertTrue(TextUtils.isValidEmailAddress("marco@eomasters.org"));
  }

  @Test
  void escapeHtml() {
    assertEquals("test", TextUtils.escapeHtml("test"));
    assertEquals("test &amp; test", TextUtils.escapeHtml("test & test"));
    assertEquals("test &lt; test", TextUtils.escapeHtml("test < test"));
    assertEquals("test &gt; test", TextUtils.escapeHtml("test > test"));
    assertEquals("test &quot; test", TextUtils.escapeHtml("test \" test"));
    assertEquals("test &apos; test", TextUtils.escapeHtml("test ' test"));
    assertEquals("test &lt; test &gt; test", TextUtils.escapeHtml("test < test > test"));
  }

  @Test
  void asFormattedTable() {
    String[][] table = {{"Header_a", "Name_b"}, {"ab", "defghjklmnopqrstuvwxyz"}, {"", "1053"}};
    assertEquals(""
        + "| Header_a | Name_b                 |\n"
        + "| ab       | defghjklmnopqrstuvwxyz |\n"
        + "|          | 1053                   |\n", TextUtils.asFormattedTable(table));
  }
}
