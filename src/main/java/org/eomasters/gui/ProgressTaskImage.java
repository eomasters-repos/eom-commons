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

import java.awt.Image;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.eomasters.utils.ProgressManager;
import org.eomasters.utils.ProgressTask;

public class ProgressTaskImage extends AnimatedImage {

  private final ProgressTask task;
  private int currentFrameIdx;
  private int targetFrameIdx;

  public ProgressTaskImage(ProgressTask task, Image[] frames) {
    this(task, frames, 1.0);
  }

  public ProgressTaskImage(ProgressTask task, Image[] frames, double scaling) {
    super(frames, scaling);
    this.task = task;
    if (task.getProgress() != ProgressManager.UNDEFINED_PROGRESS) {
      ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
      scheduler.scheduleAtFixedRate(() -> {
        if (task.isDone()) {
          scheduler.shutdown();
        }
        int progress = task.getProgress();
        targetFrameIdx = (int) Math.floor((getNumFrames() - 1) * progress / 100f);
        if(targetFrameIdx > currentFrameIdx) {
           currentFrameIdx++;
        }
        repaint();
      }, 0, 50, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
  }

  @Override
  protected int getFrameIdx() {
    int progress = task.getProgress();
    if (progress == ProgressManager.UNDEFINED_PROGRESS) {
      return super.getFrameIdx();
    }
    return currentFrameIdx;
  }

}
