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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ProgressManager {

  public static final int UNDEFINED_PROGRESS = -1;
  private static final String GENERAL_LISTENER_ID = "";
  private static final ProgressManager instance = new ProgressManager();
  private final TreeMap<String, ProgressTask> tasks = new TreeMap<>();
  private final HashMap<String, List<ProgressListener>> listenerMap = new HashMap<>();

  private ProgressManager() {
    // prevent instantiation
  }

  public static ProgressTask registerTask(String taskID, int amount) {
    ProgressTask task = new ProgressTask(taskID, amount);
    instance.tasks.put(taskID, task);
    return task;
  }

  public static void worked(String taskID, int steps) {
    ProgressTask task = instance.tasks.get(taskID);
    task.worked(steps);
  }

  public static void worked(String taskID, String title, int steps) {
    ProgressTask task = instance.tasks.get(taskID);
    task.setTitle(title);
    task.worked(steps);
  }

  public static int getProgress(String taskID) {
    ProgressTask task = instance.tasks.get(taskID);
    if (task != null) {
      return task.getProgress();
    } else {
      return UNDEFINED_PROGRESS;
    }
  }

  public static void done(String taskID) {
    try (ProgressTask remove = instance.tasks.remove(taskID)) {
      if (remove != null) {
        instance.listenerMap.remove(taskID);
        remove.getSubTasks().forEach(instance.listenerMap::remove);
        remove.getSubTasks().forEach(ProgressManager::done);
      }
    }
  }

  public static void addProgressListener(ProgressListener progressListener) {
    addProgressListener(GENERAL_LISTENER_ID, progressListener);
  }

  public static void addProgressListener(String taskID, ProgressListener progressListener) {
    List<ProgressListener> list = instance.listenerMap.computeIfAbsent(taskID, k -> new ArrayList<>());
    list.add(progressListener);
  }

  public static void removeProgressListener(ProgressListener progressListener) {
    for (List<ProgressListener> list : instance.listenerMap.values()) {
      list.remove(progressListener);
    }
  }

  static void fireProgressChanged(ProgressTask task) {
    List<ProgressListener> progressListeners = instance.listenerMap.get(task.getTaskID());
    if (progressListeners != null) {
      progressListeners.forEach(l -> l.onProgressChanged(task));
    }
    progressListeners = instance.listenerMap.get(GENERAL_LISTENER_ID);
    if (progressListeners != null) {
      progressListeners.forEach(l -> l.onProgressChanged(task));
    }
  }

  static void fireTaskDone(ProgressTask task) {
    List<ProgressListener> progressListeners = instance.listenerMap.get(task.getTaskID());
    if (progressListeners != null) {
      progressListeners.forEach(l -> l.onTaskDone(task));
    }
    progressListeners = instance.listenerMap.get(GENERAL_LISTENER_ID);
    if (progressListeners != null) {
      progressListeners.forEach(l -> l.onTaskDone(task));
    }
  }


}


