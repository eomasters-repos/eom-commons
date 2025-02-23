/*-
 * ========================LICENSE_START=================================
 * EOMTBX PRO - EOMasters Toolbox PRO for SNAP
 * -> https://www.eomasters.org/sw/EOMTBX
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  @Test
  void readMap() throws IOException {
    TypeToken<Map<String, Instance>> typetoken = new TypeToken<>() {
    };
    Map<String, Instance> map = JsonUtils.readMap(getClass().getResourceAsStream("testMap.json"), typetoken);
    assertEquals(3, map.size());
    Set<String> strings = map.keySet();
    assertTrue(strings.containsAll(List.of("A", "B", "Z")));

    Instance a = map.get("A");
    assertEquals("A", a.name);
    assertEquals("Value A", a.description);
    assertEquals(6.0, a.value);

    Instance b = map.get("B");
    assertEquals("B", b.name);
    assertEquals("Value B", b.description);
    assertEquals(7.5, b.value);

    Instance z = map.get("Z");
    assertEquals("OMEGA", z.name);
    assertEquals("Value Z", z.description);
    assertEquals(1.06589, z.value);
  }

  @Test
  void writeMap() throws IOException {
    Map<String, Instance> map = new HashMap<>();
    map.put("A", new Instance("A", "Value A", 6.0));
    map.put("B", new Instance("B", "Value B", 7.5));
    map.put("Z", new Instance("OMEGA", "Value Z", 1.06589));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonUtils.writeMap(map, outputStream);

    String json = outputStream.toString();
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    assertEquals(3, jsonObject.size());
    assertTrue(jsonObject.has("A"));
    assertTrue(jsonObject.has("B"));
    assertTrue(jsonObject.has("Z"));

    JsonObject a = jsonObject.getAsJsonObject("A");
    assertEquals("A", a.get("name").getAsString());
    assertEquals("Value A", a.get("description").getAsString());
    assertEquals(6.0, a.get("value").getAsDouble());

    JsonObject b = jsonObject.getAsJsonObject("B");
    assertEquals("B", b.get("name").getAsString());
    assertEquals("Value B", b.get("description").getAsString());
    assertEquals(7.5, b.get("value").getAsDouble());

    JsonObject z = jsonObject.getAsJsonObject("Z");
    assertEquals("OMEGA", z.get("name").getAsString());
    assertEquals("Value Z", z.get("description").getAsString());
    assertEquals(1.06589, z.get("value").getAsDouble());
  }

  @Test
  void readList() throws IOException {
    TypeToken<List<Instance>> typetoken = new TypeToken<>() {
    };
    List<Instance> list = JsonUtils.readList(getClass().getResourceAsStream("testList.json"), typetoken);
    assertEquals(3, list.size());

    Instance a = list.get(0);
    assertEquals("A", a.name);
    assertEquals("Value A", a.description);
    assertEquals(6.0, a.value);

    Instance b = list.get(1);
    assertEquals("B", b.name);
    assertEquals("Value B", b.description);
    assertEquals(7.5, b.value);

    Instance z = list.get(2);
    assertEquals("OMEGA", z.name);
    assertEquals("Value Z", z.description);
    assertEquals(1.06589, z.value);
  }

  @Test
  void writeList() throws IOException {
    List<Instance> list = Arrays.asList(
        new Instance("A", "Value A", 6.0),
        new Instance("B", "Value B", 7.5),
        new Instance("OMEGA", "Value Z", 1.06589)
    );
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonUtils.writeList(list, outputStream);
    String json = outputStream.toString();
    JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
    assertEquals(3, jsonArray.size());

    JsonObject aJson = jsonArray.get(0).getAsJsonObject();
    assertEquals("A", aJson.get("name").getAsString());
    assertEquals("Value A", aJson.get("description").getAsString());
    assertEquals(6.0, aJson.get("value").getAsDouble());

    JsonObject bJson = jsonArray.get(1).getAsJsonObject();
    assertEquals("B", bJson.get("name").getAsString());
    assertEquals("Value B", bJson.get("description").getAsString());
    assertEquals(7.5, bJson.get("value").getAsDouble());

    JsonObject zJson = jsonArray.get(2).getAsJsonObject();
    assertEquals("OMEGA", zJson.get("name").getAsString());
    assertEquals("Value Z", zJson.get("description").getAsString());
    assertEquals(1.06589, zJson.get("value").getAsDouble());
  }

  private static class Instance {

    public String name;
    public String description;
    public double value;

    public Instance(String name, String description, double value) {
      this.name = name;
      this.description = description;
      this.value = value;
    }
  }
}
