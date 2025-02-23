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
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * A focus traversal policy that traverses components in the order specified in
 * the constructor.
 */
public class OrderedFocusTraversalPolicy extends FocusTraversalPolicy {

  List<Component> order;

  public OrderedFocusTraversalPolicy(List<Component> order) {
    this.order = new ArrayList<>(order.size());
    this.order.addAll(order);
  }

  public Component getComponentAfter(Container focusCycleRoot,
      Component aComponent) {
    int idx = (order.indexOf(aComponent) + 1) % order.size();
    return order.get(idx);
  }

  public Component getComponentBefore(Container focusCycleRoot,
      Component aComponent) {
    int idx = order.indexOf(aComponent) - 1;
    if (idx < 0) {
      idx = order.size() - 1;
    }
    return order.get(idx);
  }

  public Component getDefaultComponent(Container focusCycleRoot) {
    return order.get(0);
  }

  public Component getLastComponent(Container focusCycleRoot) {
    return order.get(order.size() - 1);
  }

  public Component getFirstComponent(Container focusCycleRoot) {
    return order.get(0);
  }
}
