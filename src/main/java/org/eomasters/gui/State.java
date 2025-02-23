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

import javax.swing.Icon;

public class State {

  private final String id;
  private Icon icon;
  private String text;
  private String toolTip;

  public State(String id) {
    this.id = id;
  }

  public State(String id, Icon icon) {
    this.id = id;
    this.icon = icon;
  }

  public State(String id, String text) {
    this.id = id;
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public Icon getIcon() {
    return icon;
  }

  public String getText() {
    return text;
  }

  public String getToolTipText() {
    return toolTip;
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setToolTip(String toolTip) {
    this.toolTip = toolTip;
  }
}
