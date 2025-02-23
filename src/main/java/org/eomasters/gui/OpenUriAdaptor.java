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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import org.eomasters.utils.ErrorHandler;

/**
 * Implements an action listener that opens a URI in the default browser.
 */
public class OpenUriAdaptor implements ActionListener {

  private final URI uri;

  /**
   * Creates a new instance of {@link OpenUriAdaptor}.
   *
   * @param uri the URI to open
   */
  public OpenUriAdaptor(String uri) {
    this.uri = URI.create(uri);
  }

  /**
   * Creates a new instance of {@link OpenUriAdaptor}.
   *
   * @param uri the URI to open
   */
  public OpenUriAdaptor(URI uri) {
    this.uri = uri;
  }

  @Override
  public void actionPerformed(ActionEvent e1) {
    try {
      Desktop.getDesktop().browse(uri);
    } catch (IOException ex) {
      ErrorHandler.handleError("Error opening browser", ex.getMessage(), ex);
    }
  }
}
