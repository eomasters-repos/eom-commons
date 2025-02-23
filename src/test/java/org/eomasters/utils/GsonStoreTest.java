/*-
 * ========================LICENSE_START=================================
 * Toolbox Module - EOMasters Toolbox PRO for SNAP
 * -> https://www.eomasters.org/eomtbx/modules/toolbox
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GsonStoreTest {

  @SuppressWarnings("unused")
  private static class DummyObject {

    private String name;
    private int[] numbers;
    private Color color;

  }

  private static class ColorAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      int colorInt = jsonObject.getAsJsonPrimitive("coloring").getAsInt();
      return new Color(colorInt, true);
    }

    @Override
    public JsonElement serialize(Color value, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject colorObject = new JsonObject();
      colorObject.addProperty("coloring", value.getRGB());
      return colorObject;
    }
  }

  private static GsonStore<DummyObject> store;

  @BeforeAll
  public static void beforeClass() {
    store = new GsonStore<>(DummyObject.class,
        builder -> builder.registerTypeAdapter(Color.class, new ColorAdapter()));
  }

  @Test
  public void nullJson() {
    String json = null;
    DummyObject dummyObject = store.fromJson(json);
    assertNull(dummyObject);
  }

  @Test
  public void emptyJson() {
    String json = "";
    DummyObject dummyObject = store.fromJson(json);
    assertNull(dummyObject);
  }

  @Test
  public void invalidJson() {
    String json = "{\"name\":\"test\",\"numbers\":1,2,3,\"color\":{\"coloring\":16777215}}";
    Exception exception = assertThrows(JsonSyntaxException.class, () -> store.fromJson(json));
    assertTrue(exception.getMessage().contains("Expected BEGIN_ARRAY but was NUMBER"));
  }

  @Test
  public void loadSyntaxError() throws URISyntaxException {
    try {
      final Path location = Paths.get(
          Objects.requireNonNull(GsonStoreTest.class.getResource("SyntaxError.jsn")).toURI());
      store.load(location);
      fail("Expected exception");
    } catch (IOException e) {
      assertInstanceOf(JsonSyntaxException.class, e.getCause());
    }
  }

  @Test
  public void load() throws URISyntaxException, IOException {
    final Path location = Paths.get(Objects.requireNonNull(GsonStoreTest.class.getResource("DummyObject.jsn")).toURI());
    DummyObject loaded = store.load(location);
    assertEquals("Winnie", loaded.name);
    assertArrayEquals(new int[]{8, 8, 888}, loaded.numbers);
    assertEquals(895613501, loaded.color.getRGB());
  }

  @Test
  public void save() throws IOException {
    try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
      final Path storeDir = fs.getPath("store");
      Files.createDirectory(storeDir);
      final Path target = storeDir.resolve("target");
      DummyObject dummyObject = new DummyObject();
      dummyObject.name = "test";
      dummyObject.numbers = new int[]{1, 2, 3};
      dummyObject.color = new Color(255, 255, 255, 127);
      store.save(dummyObject, target);
      Files.exists(target);
    }
  }

  @Test
  public void toJson() {
    DummyObject dummyObject = new DummyObject();
    dummyObject.name = "test";
    dummyObject.numbers = new int[]{1, 2, 3};
    dummyObject.color = new Color(255, 255, 255, 127);
    final String text = store.toJson(dummyObject);
    assertEquals("{\n"
        + "  \"name\": \"test\",\n"
        + "  \"numbers\": [\n"
        + "    1,\n"
        + "    2,\n"
        + "    3\n"
        + "  ],\n"
        + "  \"color\": {\n"
        + "    \"coloring\": 2147483647\n"
        + "  }\n"
        + "}", text);
  }

  @Test
  public void fromJson() {
    String json = "{\n"
        + "  \"name\": \"test\",\n"
        + "  \"numbers\": [\n"
        + "    1,\n"
        + "    2,\n"
        + "    3\n"
        + "  ],\n"
        + "  \"color\": {\n"
        + "    \"coloring\": 2147483647\n"
        + "  }\n"
        + "}";
    DummyObject dummyObject = store.fromJson(json);
    assertEquals("test", dummyObject.name);
    assertArrayEquals(new int[]{1, 2, 3}, dummyObject.numbers);
    assertEquals(new Color(255, 255, 255, 127), dummyObject.color);
  }

}
