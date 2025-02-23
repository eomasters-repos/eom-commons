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

package org.eomasters.icons;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.eomasters.utils.ImageUtils;

public class SvgIcon extends Icon {
  private static final Logger LOG = Logger.getLogger("org.eomasters");


  public SvgIcon(String path, Class<?> loadingClass) {
    super(path, loadingClass);
  }

  @Override
  protected ImageIcon createIcon(int size) {
    String iconPath = getPath() + ".svg";
    try {
      InputStream resource = getLoadingClass().getResourceAsStream(iconPath);
      return new ImageIcon(ImageUtils.loadSvgImage(resource, size, size));
    } catch (Throwable e) {
      LOG.warning("Not able to load image icon: " + iconPath);
      return new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB));
    }
  }

}
