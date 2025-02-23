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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Icon {
  
  public static final int SIZE_16 = 16;
  public static final int SIZE_24 = 16;
  public static final int SIZE_32 = 32;
  public static final int SIZE_48 = 48;

  private final String path;
  private final Class<?> loadingClass;

  protected Icon(String path) {
    this(path, Icon.class);
  }

  public Icon(String path, Class<?> loadingClass) {
    this.path = path;
    this.loadingClass = loadingClass;
  }

  public String getPath() {
    return path;
  }

  protected Class<?> getLoadingClass() {
    return loadingClass;
  }

  public ImageIcon getImageIcon(int size) {
    return createIcon(size);
  }

  public List<? extends Image> getImages(int[] sizes) {
    ArrayList<Image> images = new ArrayList<>();
    for (int size : sizes) {
      images.add(getImageIcon(size).getImage());
    }
    return images;
  }

  protected abstract ImageIcon createIcon(int size);

  public Icon withAddition(Icon addition) {
    return new IconWithAddition(this, addition);
  }

}
