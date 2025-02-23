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

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;

public class ImageUtils {

  public static Image[] loadFramesFromGif(ImageInputStream imageInputStream) throws IOException {
    ArrayList<BufferedImage> frames = new ArrayList<>();
    ImageReader gifReader = ImageIO.getImageReadersByFormatName("gif").next();
    gifReader.setInput(imageInputStream);
    int width = gifReader.getWidth(0);
    int height = gifReader.getHeight(0);
    for (int i = 0; i < gifReader.getNumImages(true); i++) {
      BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      frames.add(frame);
      frame.getGraphics().drawImage(gifReader.read(i), 0, 0, null);
    }
    return frames.toArray(new Image[0]);
  }

  public static Image[] loadNumberedImages(Class<?> aClass, String resourceStr, int start, int end) {
    ArrayList<Image> list = new ArrayList<>();
    for (int i = start; i <= end; i++) {
      try {
        try (InputStream resource = aClass.getResourceAsStream(
            resourceStr.replace("#", String.valueOf(i)))) {
          list.add(ImageIO.read(Objects.requireNonNull(resource)));
        }
      } catch (IOException ignored) {
      }
    }
    return list.toArray(new Image[0]);
  }

  public static Image loadSvgImage(InputStream inputStream, int width, int height) throws IOException {
    SvgTranscoder transcoder = new SvgTranscoder();
    TranscodingHints hints = new TranscodingHints();
    hints.put(ImageTranscoder.KEY_WIDTH, (float) width);
    hints.put(ImageTranscoder.KEY_HEIGHT, (float) height);
    hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
    hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
    hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
    hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
    transcoder.setTranscodingHints(hints);
    try {
      transcoder.transcode(new TranscoderInput(inputStream), null);
      return transcoder.getImage();
    } catch (TranscoderException e) {
      throw new IOException("SVG image can not be decoded", e);
    }
  }

  private static class SvgTranscoder extends ImageTranscoder {

    private BufferedImage image = null;

    public BufferedImage createImage(int w, int h) {
      image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      return image;
    }

    @Override
    protected ImageRenderer createRenderer() {
      ImageRenderer renderer = super.createRenderer();
      RenderingHints hints = renderer.getRenderingHints();
      hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Not a big difference
      hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      renderer.setRenderingHints(hints);
      return renderer;
    }

    public void writeImage(BufferedImage img, TranscoderOutput out) {
      // empty
    }

    public BufferedImage getImage() {
      return image;
    }
  }
}
