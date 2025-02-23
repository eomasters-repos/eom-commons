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

/**
 * An interface for uploading files to a file sharing service.
 */
public interface FileSharingService {

  /**
   * Returns the name of the file sharing service.
   *
   * @return the name of the file sharing service
   */
  String getName();

  /**
   * Returns the website url of the file sharing service.
   *
   * @return the website url of the file sharing service
   */
  String getWebpage();

  /**
   * Returns the url of the terms of service of the file sharing service.
   *
   * @return the url of the terms of service
   */
  String getTosUrl();

  /**
   * Returns the url of the privacy policy of the file sharing service.
   *
   * @return the url of the privacy policy
   */
  String getPrivacyUrl();

  /**
   * Uploads a file the file sharing service.
   *
   * @param file The file to upload
   * @return The response from the API
   * @throws IOException if an I/O error occurs
   */
  default UploadResponse uploadFile(Path file) throws IOException {
    return this.uploadFile(file.getFileName().toString(), Files.newInputStream(file));
  }

  /**
   * Uploads data from the provided input stream to the file sharing service and puts it into a file using the provided
   * name.
   *
   * @param filename    The name of the file to upload
   * @param inputStream The input stream of the file to upload
   * @return The response from the API
   * @throws IOException if an I/O error occurs
   */
  UploadResponse uploadFile(String filename, InputStream inputStream) throws IOException;

  /**
   * A class that represents the response from the file sharing service.
   */
  class UploadResponse {

    // The properties of the response
    private final int status;
    private final String statusMessage;
    private final String url;

    /**
     * Creates a new UploadResponse with the given status and url.
     *
     * @param status  the status
     * @param message the status message
     * @param url     the url
     */
    public UploadResponse(int status, String message, String url) {
      this.status = status;
      this.statusMessage = message;
      this.url = url;
    }

    /**
     * Returns the status of the response.
     *
     * @return the status
     */
    public int getStatus() {
      return status;
    }

    public String getStatusMessage() {
      return statusMessage;
    }

    /**
     * Returns the url of the response.
     *
     * @return the url under which the uploaded file can be accessed
     */
    public String getUrl() {
      return url;
    }

    @Override
    public String toString() {
      return "UploadResponse{"
          + "status=" + status
          + ", url='" + url + '\''
          + '}';
    }
  }
}
