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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Utility class for working with JSON data.
 */
public class JsonUtils {

  /**
   * Reads a JSON formatted input stream and converts it into a Map object with the specified key and value types.
   *
   * @param in        the input stream containing the JSON data
   * @param typetoken the TypeToken representing the map type
   * @return a Map object containing the JSON data
   * @throws IOException if there is an error reading or parsing the JSON data
   */
  public static <V, T> Map<V, T> readMap(InputStream in, TypeToken<Map<V, T>> typetoken) throws IOException {
    try (Reader reader = new InputStreamReader(in)) {
      return new Gson().fromJson(reader, typetoken.getType());
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a Map object to an OutputStream in JSON format.
   *
   * @param map the Map object to write
   * @param out the OutputStream to write to
   * @throws IOException if there is an error writing the JSON data
   */
  public static <V, T> void writeMap(Map<V, T> map, OutputStream out) throws IOException {
    try (Writer writer = new OutputStreamWriter(out)) {
      new Gson().toJson(map, writer);
    } catch (JsonIOException e) {
      throw new IOException(e);
    }
  }

  /**
   * Reads a JSON formatted input stream and converts it into a List object of the specified type.
   *
   * @param in        the input stream containing the JSON data
   * @param typeToken the TypeToken representing the list type
   * @return a List object containing the JSON data
   * @throws IOException if there is an error reading or parsing the JSON data
   */
  public static <T> List<T> readList(InputStream in, TypeToken<List<T>> typeToken) throws IOException {
    try (Reader reader = new InputStreamReader(in)) {
      return new Gson().fromJson(reader, typeToken.getType());
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a List object to an OutputStream in JSON format.
   *
   * @param list the List object to write
   * @param out  the OutputStream to write to
   * @throws IOException if there is an error writing the JSON data
   */
  public static <T> void writeList(List<T> list, OutputStream out) throws IOException {
    try (Writer writer = new OutputStreamWriter(out)) {
      new Gson().toJson(list, writer);
    } catch (JsonIOException e) {
      throw new IOException(e);
    }
  }
}
