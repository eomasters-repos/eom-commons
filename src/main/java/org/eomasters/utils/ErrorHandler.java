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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eomasters.gui.Dialogs;

/**
 * Handles errors.
 */
public class ErrorHandler {

  protected static final Logger LOG = Logger.getLogger("org.eomasters");

  /**
   * Shows a simple error dialog.
   *
   * @param title   the title
   * @param message the error message
   */
  public static void handleError(String title, String message) {
    LOG.log(Level.WARNING, String.format("%s: %s", title, message));
    if (SystemHelper.isHeadless()) {
      return;
    }
    Dialogs.error(title, message, null);
  }

  /**
   * Shows an error dialog extended by the cause exception.
   *
   * @param title     the title
   * @param message   the error message
   * @param throwable the cause throwable
   */
  public static void handleError(String title, String message, Throwable throwable) {
    LOG.log(Level.WARNING, message, throwable);
    if (SystemHelper.isHeadless()) {
      return;
    }
    Dialogs.error(title, message, throwable);
  }


}
