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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import org.eomasters.icons.Icon;
import org.eomasters.icons.Icons;
import org.eomasters.utils.ImageUtils;
import org.eomasters.utils.ProgressManager;
import org.eomasters.utils.ProgressTask;

public class OverlayProgressSwingWorker extends SwingWorker<Void, Void> {

  private static final int MAX_NOT_SHOWING_PROGRESS = 200;
  private static final int SHOW_DELAY = 50;
  private SyncComponentSizeListener sizeListener;

  public static void main(String[] args) {
    final JFrame frame = new JFrame("OverlayProgressSwingWorker");
    JButton progressBtn = new JButton(Icons.EYE.getImageIcon(Icon.SIZE_24));
    JPanel overlayed = new JPanel(new BorderLayout());
    overlayed.add(progressBtn, BorderLayout.NORTH);
    overlayed.add(new JList<>(new String[]{"1", "2", "3", "4", "5"}), BorderLayout.CENTER);

    Container contentPane = frame.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(overlayed, BorderLayout.CENTER);
    contentPane.add(new JButton("Visible"), BorderLayout.SOUTH);
    progressBtn.addActionListener(e -> {
      ProgressTask testWorker = ProgressManager.registerTask("test", 2500);
      testWorker.setRunnable(() -> {
        try {
          int time = 1000;
          Thread.sleep(time);
          testWorker.worked(time);
          System.out.println("Progress: " + testWorker.getProgress());
          Thread.sleep(time);
          testWorker.worked(time);
          System.out.println("Progress: " + testWorker.getProgress());
          Thread.sleep(time);
          testWorker.worked(time);
          System.out.println("Progress: " + testWorker.getProgress());
        } catch (InterruptedException ignored) {
        }
      });
      new OverlayProgressSwingWorker(overlayed, testWorker).execute();
    });

    frame.setSize(400, 600);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    SwingUtilities.invokeLater(() -> frame.setVisible(true));
  }

  private static JPopupMenu popupComponent;
  private final JComponent component;
  private final ProgressTask task;


  /**
   * Creates a new swing worker that shows a progress indicator on a glass pane while it is running.
   *
   * @param component the component which is overlaid with a progress indicator
   * @param task      the task to run
   */
  public OverlayProgressSwingWorker(JComponent component, ProgressTask task) {
    this.component = component;
    this.task = task;
    initPopup(component);
  }

  private void initPopup(JComponent component) {
    Rectangle bounds = component.getBounds();
    if (component.isVisible() && bounds.width != 0 && bounds.height != 0) {
      URL resource;
      if (task.getProgress() == ProgressManager.UNDEFINED_PROGRESS) {
        resource = OverlayProgressSwingWorker.class.getResource("/icons/progress/indefinite.gif");
      } else {
        resource = OverlayProgressSwingWorker.class.getResource("/icons/progress/progress.gif");
      }
      AnimatedImage image;
      try {
        Image[] frames = ImageUtils.loadFramesFromGif(
            ImageIO.createImageInputStream(Objects.requireNonNull(resource).openStream()));
        image = new ProgressTaskImage(task, frames, 0.2);
        image.setBackground(new Color(0, 0, 0, 89));
        image.setOpaque(false);
        image.start();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      JPanel overplayPanel = new JPanel(new BorderLayout());
      overplayPanel.setPreferredSize(component.getSize());
      overplayPanel.setBorder(component.getBorder());
      overplayPanel.add(image, BorderLayout.CENTER);
      overplayPanel.setBounds(bounds);
      overplayPanel.setBackground(new Color(0, 0, 0, 0));
      overplayPanel.setOpaque(false);
      popupComponent = new JPopupMenu() {
        @Override
        public void menuSelectionChanged(boolean isIncluded) {
          // override to prevent the popup from closing when the mouse is clicked outside the popup
        }
      };
      popupComponent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
      popupComponent.setBorder(BorderFactory.createEmptyBorder());
      popupComponent.add(overplayPanel);
      popupComponent.pack();
      popupComponent.setOpaque(false);
    }
  }

  public void waitFor() {
    JDialog dialog = new JDialog((Dialog) null);
    dialog.setUndecorated(true);
    dialog.setModalityType(ModalityType.APPLICATION_MODAL);
    dialog.setFocusableWindowState(false);
    dialog.setBounds(0, 0, 0, 0);
    addPropertyChangeListener(new SwingWorkerCompletionWaiter(dialog));
    execute();
    //the dialog will be visible until the SwingWorker is done
    dialog.setVisible(true);
  }

  @Override
  protected Void doInBackground() {
    if (component.isShowing()) {
      sizeListener = new SyncComponentSizeListener();
      component.addComponentListener(sizeListener);
      // start delayed show
      ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
      scheduler.schedule(this::showPopup, SHOW_DELAY, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
    task.getRunnable().run();
    return null;
  }

  private void showPopup() {
    if (task.getProgress() < (SHOW_DELAY * 100f) / MAX_NOT_SHOWING_PROGRESS) {
      SwingUtilities.invokeLater(() -> {
        if (!isDone()) {
          popupComponent.show(component, 0, 0);
        }
      });
    }
  }

  @Override
  protected void done() {
    component.removeComponentListener(sizeListener);
    if (popupComponent != null) {
      popupComponent.setVisible(false);
    }
  }

  private static class SwingWorkerCompletionWaiter implements PropertyChangeListener {
    private JDialog dialog;

    public SwingWorkerCompletionWaiter(JDialog dialog) {
      this.dialog = dialog;
    }

    public void propertyChange(PropertyChangeEvent event) {
      if ("state".equals(event.getPropertyName())
          && SwingWorker.StateValue.DONE == event.getNewValue()) {
        dialog.setVisible(false);
        dialog.dispose();
      }
    }
  }

  private class SyncComponentSizeListener implements ComponentListener {

    @Override
    public void componentResized(ComponentEvent e) {
      popupComponent.setPopupSize(e.getComponent().getBounds().getSize());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
      popupComponent.setLocation(e.getComponent().getLocationOnScreen());
    }

    @Override
    public void componentShown(ComponentEvent e) {
      if (!isDone()) {
        popupComponent.setVisible(true);
      }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
      popupComponent.setVisible(false);
    }
  }
}
