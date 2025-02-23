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
 * A class that uploads files to (<a href="https://gofile.io">gofile.io</a>). See the <a
 * href="https://gofile.io/api">API</a>
 */
public class GoFileService implements FileSharingService {

  private static final String WEB_URL = "https://gofile.io";

  @Override
  public String getName() {
    return "GoFile";
  }

  @Override
  public String getWebpage() {
    return WEB_URL;
  }

  @Override
  public String getTosUrl() {
    return WEB_URL + "/terms";
  }

  @Override
  public String getPrivacyUrl() {
    return WEB_URL + "/privacy";
  }

  @Override
  public UploadResponse uploadFile(String filename, InputStream inputStream) throws IOException {
    Connection.Response response;
    // get the best server first
    response = Jsoup.connect("https://api.gofile.io/getServer")
        .method(Connection.Method.GET)
        .ignoreContentType(true)
        .execute();

    JsonObject serverJson = new Gson().fromJson(response.body(), JsonObject.class);
    if (!serverJson.get("status").getAsString().equalsIgnoreCase("ok")) {
      throw new IOException("Could not get server");
    }
    JsonObject serverData = serverJson.getAsJsonObject("data");
    String serverName = serverData.get("server").getAsString();

    response = Jsoup.connect("https://" + serverName + ".gofile.io/uploadFile")
        .data("file", filename, inputStream)
        .method(org.jsoup.Connection.Method.POST)
        .ignoreContentType(true)
        .execute();

    JsonObject uploadJson = new Gson().fromJson(response.body(), JsonObject.class);
    if (!uploadJson.get("status").getAsString().equalsIgnoreCase("ok")) {
      throw new IOException("Could not upload file");
    }
    JsonObject uploadData = uploadJson.getAsJsonObject("data");
    String downloadPage = uploadData.get("downloadPage").getAsString();
    return new UploadResponse(200, response.statusMessage(), downloadPage);

  }
}
