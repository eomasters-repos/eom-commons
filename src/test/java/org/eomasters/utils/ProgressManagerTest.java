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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ProgressManagerTest {

  @Test
  void testProgressReporting() {
    ProgressTask task1 = ProgressManager.registerTask("task1", 4);

    ProgressManager.worked("task1", 1);
    assertEquals(25, ProgressManager.getProgress("task1"));

    ProgressManager.worked("task1", 2);
    assertEquals(75, ProgressManager.getProgress("task1"));

    ProgressManager.worked("task1", 1);
    assertEquals(-1, ProgressManager.getProgress("task1"));
    assertEquals(100, task1.getProgress());

    ProgressManager.done("task1");
  }

  @Test
  void testProgressDoesNotExceed100() {
    int total = 4;
    ProgressTask task1 = ProgressManager.registerTask("task1", total);

    ProgressManager.worked("task1", total + 1);
    assertEquals(100, task1.getProgress());
    assertTrue(task1.isDone());
    ProgressManager.done("task1");
  }

  @Test
  void testTaskIsDeregisteredAfterDone() {
    ProgressManager.registerTask("task1", 4);

    ProgressManager.worked("task1", 1);
    assertEquals(25, ProgressManager.getProgress("task1"));

    ProgressManager.done("task1");
    assertEquals(-1, ProgressManager.getProgress("task1"));
  }

  @Test
  void testProgressReportingForTaskWithUndefinedAmount() {
    ProgressTask task1 = ProgressManager.registerTask("task1", -1);

    assertEquals(-1, ProgressManager.getProgress("task1"));

    ProgressManager.worked("task1", 1);
    assertEquals(-1, ProgressManager.getProgress("task1"));

    ProgressManager.worked("task1", 2);
    assertEquals(-1, ProgressManager.getProgress("task1"));

    ProgressManager.worked("task1", 2);
    assertEquals(-1, ProgressManager.getProgress("task1"));

    ProgressManager.done("task1");
    assertTrue(task1.isDone());
  }

  @Test
  void testProgressReportingWithSubTasks() {
    ProgressTask task1 = ProgressManager.registerTask("task", 10)
                                        .with("subtask1", 5)
                                        .with("subtask2", 2);

    ProgressTask subtask1 = ProgressManager.registerTask("subtask1", 2);
    subtask1.worked(1);

    ProgressTask subtask2 = ProgressManager.registerTask("subtask2", 10);
    subtask2.worked(5);

    assertEquals(50, subtask1.getProgress());
    assertEquals(50, subtask2.getProgress());

    assertEquals(35, task1.getProgress());

    task1.worked(1);
    assertEquals(45, task1.getProgress());

    task1.done();
    subtask1.done();
    subtask2.done();
  }

  @Test
  void testProgressReportingWithSubSubTasks() {
    ProgressTask task1 = ProgressManager.registerTask("task", 10)
                                        .with("subtask1", 5);
    ProgressTask subtask1 = ProgressManager.registerTask("subtask1", 5)
                                           .with("subsubtask1", 2);
    ProgressTask subsubtask1 = ProgressManager.registerTask("subsubtask1", 10);

    subsubtask1.worked(5);
    assertEquals(50, subsubtask1.getProgress());
    assertEquals(20, subtask1.getProgress());
    assertEquals(10, task1.getProgress());

    subtask1.worked(1);
    assertEquals(50, subsubtask1.getProgress());
    assertEquals(40, subtask1.getProgress());
    assertEquals(20, task1.getProgress());

    task1.done();
    subtask1.done();
    subsubtask1.done();
  }

  @Test
  void testTaskSpecificListenerNotification() {
    ProgressTask task = ProgressManager.registerTask("task", 10)
                                       .with("subtask1", 5);
    ProgressTask subtask1 = ProgressManager.registerTask("subtask1", 5)
                                           .with("subsubtask1", 2);
    ProgressTask subsubtask1 = ProgressManager.registerTask("subsubtask1", 10);

    final AtomicInteger generalProgressCounter = new AtomicInteger();
    final AtomicInteger generalIsDones = new AtomicInteger();
    ProgressListener generalListener = new ProgressListener() {
      @Override
      public void onProgressChanged(ProgressTask task) {
        generalProgressCounter.getAndIncrement();
      }

      @Override
      public void onTaskDone(ProgressTask task) {
        generalIsDones.getAndIncrement();
      }
    };
    ProgressManager.addProgressListener(generalListener);
    final AtomicInteger taskProgressCounter = new AtomicInteger();
    final AtomicBoolean taskIsDone = new AtomicBoolean(false);

    ProgressListener taskListener = new ProgressListener() {
      @Override
      public void onProgressChanged(ProgressTask task) {
        taskProgressCounter.getAndIncrement();
      }

      @Override
      public void onTaskDone(ProgressTask task) {
        taskIsDone.set(true);
      }
    };
    ProgressManager.addProgressListener("task", taskListener);
    final AtomicInteger subsubtaskProgressCounter = new AtomicInteger();
    final AtomicBoolean subsubtaskIsDone = new AtomicBoolean(false);

    ProgressListener subsubtaskListener = new ProgressListener() {
      @Override
      public void onProgressChanged(ProgressTask task) {
        subsubtaskProgressCounter.getAndIncrement();
      }

      @Override
      public void onTaskDone(ProgressTask task) {
        subsubtaskIsDone.set(true);
      }
    };
    ProgressManager.addProgressListener("subsubtask1", subsubtaskListener);

    subsubtask1.worked(5);
    assertEquals(3, generalProgressCounter.get()); // task, subtask1 and subsubtask1 change
    assertEquals(1, taskProgressCounter.get());
    assertEquals(1, subsubtaskProgressCounter.get());

    subtask1.worked(1);
    assertEquals(5, generalProgressCounter.get()); // task, subtask1 change
    assertEquals(2, taskProgressCounter.get());
    assertEquals(1, subsubtaskProgressCounter.get());

    subsubtask1.worked(5);
    assertEquals(8, generalProgressCounter.get()); // task, subtask1 and subsubtask1 change
    assertEquals(3, taskProgressCounter.get());
    assertEquals(2, subsubtaskProgressCounter.get());
    assertEquals(100, subsubtask1.getProgress());
    assertEquals(30, task.getProgress());
    subsubtask1.done();
    assertTrue(subsubtaskIsDone.get());

    task.worked(1);
    assertEquals(40, task.getProgress());
    assertEquals(9, generalProgressCounter.get()); // only task changes
    assertEquals(4, taskProgressCounter.get());
    assertEquals(2, subsubtaskProgressCounter.get());

    task.done();
    assertTrue(taskIsDone.get());
  }

}
