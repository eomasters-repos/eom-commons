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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eomasters.utils.FileSharingService.UploadResponse;

/**
 * A class that uploads files to a file sharing service.
 */
public final class FileSharing {

  private static FileSharingService service = new GoFileService();

  // this one is currently not used. Files are deleted after one download which is usually done by the user himself.
  // private static FileSharingService service = new FileIoService();

  private FileSharing() {
  }

  /**
   * Returns the currently used file sharing service.
   *
   * @return the currently used file sharing service
   */
  public static FileSharingService getService() {
    return service;
  }

  /**
   * Sets the file sharing service to use.
   *
   * @param service the file sharing service to use
   */
  public static void setService(FileSharingService service) {
    FileSharing.service = service;
  }

  /**
   * Uploads a file the file sharing service.
   *
   * @param file The file to upload
   * @return The response from the API
   * @throws IOException if an I/O error occurs
   */
  public static UploadResponse uploadFile(Path file) throws IOException {
    return FileSharing.uploadFile(file.getFileName().toString(), Files.newInputStream(file));
  }

  /**
   * Uploads a data from the provided input stream to the used file sharing service and puts it into a file using the
   * provided name.
   *
   * @param filename    The name of the file to upload
   * @param inputStream The input stream of the file to upload
   * @return The response from the API
   * @throws IOException if an I/O error occurs
   */
  public static UploadResponse uploadFile(String filename, InputStream inputStream) throws IOException {
    return service.uploadFile(filename, inputStream);
  }


  /**
   * A main method for testing.
   */
  public static void main(String[] args) throws IOException {
    // Call the uploadFile method and get the response as a string
    Path path = Path.of("src/main/resources/org/eomasters/eomtbx/docs/toc.xml").toAbsolutePath();
    UploadResponse response = FileSharing.uploadFile(path);
    // Print the response
    System.out.println(response);
  }
}
