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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * A class that uploads files to (<a href="https://file.io">file.io</a>). Files are deleted after one download
 */
public class FileIoService implements FileSharingService {

  // The API endpoint for uploading files
  private static final String WEB_URL = "https://file.io";
  private static final String TOS_URL = WEB_URL + "/tos";
  private static final String PRIVACY_URL = WEB_URL + "/privacy";

  @Override
  public String getName() {
    return "file.io";
  }

  @Override
  public String getWebpage() {
    return WEB_URL;
  }

  @Override
  public String getTosUrl() {
    return TOS_URL;
  }

  @Override
  public String getPrivacyUrl() {
    return PRIVACY_URL;
  }

  @Override
  public UploadResponse uploadFile(String filename, InputStream inputStream) throws IOException {
    Connection.Response response = Jsoup.connect(WEB_URL)
        .header("Accept", "application/json")
        .header("Content-Type", "multipart/form-data")
        .data("file", filename, inputStream)
        .method(Connection.Method.POST)
        .ignoreContentType(true)
        .execute();

    String body = response.body();
    JsonObject jsonObject = new Gson().fromJson(body, JsonObject.class);
    return new UploadResponse(response.statusCode(), response.statusMessage(), jsonObject.get("link").getAsString());
  }

}
