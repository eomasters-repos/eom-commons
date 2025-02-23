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

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

public class PopupComponent {

  private int delay = 500;
  private final JComponent component;
  protected final JPopupMenu popup;
  private JComponent invoker;

  public PopupComponent(JComponent component) {
    popup = new JPopupMenu();
    popup.setBorder(new BevelBorder(BevelBorder.RAISED));
    this.component = component;
    if (this.component != null) {
      popup.add(component);
    }
    popup.pack();
  }

  public void setPopupDelay(int delay) {
    this.delay = delay;
  }

  public void showPopupAt(int x, int y) {
    popup.show(invoker, x, y);
  }

  public void hidePopup() {
    popup.setVisible(false);
  }

  public void installTo(JComponent component) {
    invoker = component;
    invoker.addMouseListener(new ShowPopupPanelListener());
  }

  // Makes the bounds independent of parent
  private Rectangle getScreenLocationBounds(Component component) {
    Rectangle bounds = component.getBounds();
    synchronized (component.getTreeLock()) {
      if (component.isShowing()) {
        bounds.setLocation(component.getLocationOnScreen());
      }
    }
    return bounds;
  }

  private class HidePopupPanelListener extends MouseAdapter {

    @Override
    public void mouseExited(MouseEvent e) {
      if (!component.isVisible()) {
        return;
      }
      Rectangle bounds = getScreenLocationBounds(component);
      if (!bounds.contains(e.getLocationOnScreen())) {
        hidePopup();
      }
    }
  }

  private class ShowPopupPanelListener extends MouseAdapter {

    @Override
    public void mouseEntered(MouseEvent e) {
      // popup should be made visible only if the mouse is still over the invoker after a short delay
      ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
      executor.schedule(() -> {
        Point mousePosition = invoker.getMousePosition(true);
        if (mousePosition != null) {
          showPopupAt(mousePosition.x - 10, mousePosition.y - 10);
          component.addMouseListener(new HidePopupPanelListener());
        }
      }, delay, TimeUnit.MILLISECONDS);
    }

  }

}
