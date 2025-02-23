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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class MultiStateButton extends JPanel {

  private final DefaultListModel<State> model;
  private final JButton button;
  private int currentStateIndex;
  private final EventListenerList stateListenerList;

  public MultiStateButton(State initial, State... other) {
    this(createModel(initial, other));
  }

  private static DefaultListModel<State> createModel(State initial, State[] other) {
    DefaultListModel<State> listModel = new DefaultListModel<>();
    listModel.addElement(initial);
    listModel.addAll(List.of(other));
    return listModel;
  }


  public MultiStateButton(DefaultListModel<State> model) {
    this.model = model;
    if (model.isEmpty()) {
      throw new IllegalStateException("Model should not be empty");
    }
    stateListenerList = new EventListenerList();
    setLayout(new BorderLayout());
    button = new JButton();
    add(button, BorderLayout.CENTER);
    currentStateIndex = 0;
    updateButtonState(model.get(currentStateIndex));
    button.addActionListener(new ButtonClickedHandler(model));
    model.addListDataListener(new ModelChangeHandler(model));
  }

  public State getCurrentState() {
    return model.get(currentStateIndex);
  }

  private void updateButtonState(State state) {
    button.setText(state.getText());
    button.setIcon(state.getIcon());
    button.setToolTipText(state.getToolTipText());
  }

  public void addStateListener(StateListener listener) {
    stateListenerList.add(StateListener.class, listener);
  }

  public void removeStateListener(StateListener listener) {
    stateListenerList.remove(StateListener.class, listener);
  }

  public DefaultListModel<State> getMultiStateModel() {
    return model;
  }

  private class ButtonClickedHandler implements ActionListener {

    private final DefaultListModel<State> model;

    public ButtonClickedHandler(DefaultListModel<State> model) {
      this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      StateListener[] listeners = stateListenerList.getListeners(StateListener.class);
      State oldState = model.get(currentStateIndex);
      currentStateIndex = (currentStateIndex + 1) % model.size();
      State newState = model.get(currentStateIndex);
      updateButtonState(newState);
      for (StateListener listener : listeners) {
        listener.stateChanged(oldState, newState);
      }
    }
  }

  private class ModelChangeHandler implements ListDataListener {

    private final DefaultListModel<State> model;

    public ModelChangeHandler(DefaultListModel<State> model) {
      this.model = model;
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
      updateButtonState(model.get(currentStateIndex));
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
      updateButtonState(model.get(currentStateIndex));
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
      updateButtonState(model.get(currentStateIndex));
    }
  }
}
