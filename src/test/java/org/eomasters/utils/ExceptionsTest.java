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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Class ExceptionsTest tests the method throwIf of class Exceptions.
 * The method is utilized to throw a specified throwable if the passed condition is true.
 */
public class ExceptionsTest {

  /**
   * Method that tests if the method throwIf throws the specified exception when the condition is True.
   */
  @Test
  public void throwIf_conditionTrue_throwsException() throws Exception {
    // Arrange
    IOException exception = new IOException("Expected exception");

    // Act/Assert      
    Assertions.assertThrows(IOException.class, () -> Exceptions.throwIf(true, exception));
  }

  /**
   * Method that tests if the method throwIf does not throw any exception when the condition is False.
   */
  @Test
  public void throwIf_conditonFalse_noException() throws Exception {
    // Arrange
    IOException exception = new IOException("Unexpected exception");

    // Act/Assert
    Assertions.assertDoesNotThrow(() -> Exceptions.throwIf(false, exception));
  }
}
