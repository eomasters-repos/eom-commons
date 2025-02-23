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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ProgressTask implements AutoCloseable{

  private final String taskID;
  private String title;
  private final float totalWork;
  private float worked;
  private final Map<String, Integer> subTaskIDs = new TreeMap<>();
  private final ProgressListener subProgressListener = new ProgressListener() {
    @Override
    public void onProgressChanged(ProgressTask task) {
      ProgressManager.fireProgressChanged(ProgressTask.this);
    }

    @Override
    public void onTaskDone(ProgressTask task) {
      Integer subWorked = subTaskIDs.remove(task.getTaskID());
      worked += subWorked;
    }
  };
  private boolean done;
  private Runnable runnable;

  ProgressTask(String taskID, int totalWork) {
    if (taskID == null || taskID.isEmpty()) {
      throw new IllegalArgumentException("taskID must not be null or empty");
    }

    this.taskID = taskID;
    if (totalWork == 0) {
      throw new IllegalArgumentException("totalWork must be " + ProgressManager.UNDEFINED_PROGRESS + "(UNDEFINED)) or greater than 0");
    }
    this.totalWork = totalWork;
  }

  public ProgressTask with(String title) {
    setTitle(title);
    return this;
  }

  public ProgressTask with(String subTaskID, int parentWorkSteps) {
    if (parentWorkSteps > totalWork) {
      throw new IllegalArgumentException("parentWorkSteps must be less than or equal to work of parent task");
    }
    subTaskIDs.put(subTaskID, parentWorkSteps);
    ProgressManager.addProgressListener(subTaskID, subProgressListener);
    return this;
  }

  public String getTaskID() {
    return taskID;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public Set<String> getSubTasks() {
    return subTaskIDs.keySet();
  }

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  public Runnable getRunnable() {
    return runnable;
  }

  public void worked(int stepsWorked) {
    this.worked += stepsWorked;
    ProgressManager.fireProgressChanged(this);
    if (totalWork != ProgressManager.UNDEFINED_PROGRESS && worked >= totalWork) {
      done();
    }
  }

  public int getProgress() {
    if (totalWork == ProgressManager.UNDEFINED_PROGRESS) {
      return ProgressManager.UNDEFINED_PROGRESS;
    }
    float subprogress = 0;
    if (!subTaskIDs.isEmpty()) {
      for (Entry<String, Integer> entry : subTaskIDs.entrySet()) {
        int subTaskProgress = ProgressManager.getProgress(entry.getKey());
        if (subTaskProgress != ProgressManager.UNDEFINED_PROGRESS) {
          int parentAmount = entry.getValue();
          subprogress += (subTaskProgress / 100f) * parentAmount;
        }
      }
    }
    return Math.min((int) ((worked + subprogress) / totalWork * 100), 100);
  }

  public boolean isDone() {
    return done;
  }

  public void done() {
    worked = totalWork;
    this.done = true;
    ProgressManager.fireTaskDone(this);
    ProgressManager.done(taskID);
  }

  @Override
  public void close() {
    done();
  }
}
