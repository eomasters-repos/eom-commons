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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

class IconWithAddition extends Icon {

  private final Icon source;
  private final Icon addition;

  public IconWithAddition(Icon source, Icon addition) {
    super(source.getPath() + "_" + addition.getPath());
    this.source = source;
    this.addition = addition;
  }

  @Override
  protected ImageIcon createIcon(int size) {
    ImageIcon srcImage = source.getImageIcon(size);
    ImageIcon addImage = addition.getImageIcon(size);

    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D canvas = image.createGraphics();
    int quarter = size / 4;
    canvas.drawImage(srcImage.getImage(), quarter, 0, size - quarter, size - quarter, null);
    canvas.drawImage(addImage.getImage(), 0, 0, null);
    return new ImageIcon(image);
  }
}
