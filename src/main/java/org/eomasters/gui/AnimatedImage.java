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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedImage extends JPanel {

  private final Image[] frames;
  private final float fWidth;
  private final float fHeight;
  private int frameIdx;
  private double scaling;
  private int delay;
  private final Timer timer;

  public AnimatedImage(Image[] frames) {
    this(frames, 1.0f);
  }

  /**
   * Creates a new animated image. The animation is centered in the component.
   *
   * @param frames  The frames of the animation
   * @param scaling the scaling applied to the frames. A value of <=0 means the images are scaled to the size of the
   *                component, all other values are multiplied with the original size
   */
  public AnimatedImage(Image[] frames, double scaling) {
    if (frames.length == 0) {
      throw new IllegalArgumentException("frames must not be empty");
    }
    this.frames = frames;
    fWidth = frames[0].getWidth(null);
    fHeight = frames[0].getHeight(null);
    frameIdx = 0;
    this.scaling = scaling;
    delay = 1000 / 10;

    for (Image frame : frames) {
      if (frame.getWidth(null) != fWidth || frame.getHeight(null) != fHeight) {
        throw new IllegalArgumentException("frames must all have the same size");
      }
    }

    timer = new Timer(delay, e -> {
      repaint();
    });

    Dimension size = new Dimension(Math.round(fWidth), Math.round(fHeight));
    setPreferredSize(size);
    setSize(size);
  }

  protected int getFrameIdx() {
    int index = frameIdx % getNumFrames();
    frameIdx++;
    return index;
  }

  protected int getNumFrames() {
    return frames.length;
  }

  public void setScaling(float scaling) {
    this.scaling = scaling;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public void start() {
    timer.setDelay(delay);
    timer.start();
  }

  public void stop() {
    timer.stop();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create(); // it is better to work on the copy
    clearBackground(g2d);
    paintImage(g2d);
    g2d.dispose();
  }

  /**
   * Clears the background with the background color defined for the component
   *
   * @param g the graphics context
   */
  private void clearBackground(Graphics g) {
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
  }

  private void paintImage(Graphics2D g) {
    g.drawImage(frames[getFrameIdx()], createTransform(), null);
  }

  private AffineTransform createTransform() {
    float cWidth = getWidth();
    float cHeight = getHeight();
    double effectiveScaling;
    if (scaling <= 0) {
      effectiveScaling = Math.min(cWidth / fWidth, cHeight / fHeight);
    } else {
      effectiveScaling = scaling;
    }
    double x = (cWidth - (fWidth * effectiveScaling)) / 2;
    double y = (cHeight - (fHeight * effectiveScaling)) / 2;
    AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
    transform.scale(effectiveScaling, effectiveScaling);
    return transform;
  }

}
