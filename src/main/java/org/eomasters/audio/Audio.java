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

package org.eomasters.audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

  private final InputStream inputStream;

  public Audio(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public void play()  {
    try {
      play(inputStream);
    } catch (AudioException ignore) {
    }
  }

  /**
   * Plays the given audio input stream. Supported formats are: WAV, AU SND, and AIFF.
   *
   * @param audioInput the audio input stream
   * @throws AudioException if the audio input stream cannot be played
   * @see javax.sound.sampled.AudioFileFormat.Type
   */
  public static void play(InputStream audioInput) throws AudioException {
    try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioInput)) {
      // Executor service is used to play the audio in a separate thread. This is necessary because the audio
      // clip would be terminated immediately. LineListener is used to close the audio clip after it has been played.
      // If executor were a static field, it would prevent closing the main thread if not shutdown.
      try (ExecutorService executor = Executors.newSingleThreadExecutor()) {

        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.addLineListener(event -> {
          if (event.getType() == LineEvent.Type.STOP) {
            clip.close();
            executor.shutdown();
          }
        });

        executor.submit(clip::start);
      }
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      throw new AudioException(e);
    }
  }
}
