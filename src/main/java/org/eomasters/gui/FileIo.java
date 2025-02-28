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

package org.eomasters.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.eomasters.utils.ErrorHandler;


/**
 * Utility class for file input/output.
 */
public class FileIo {

  private final String title;
  private Component parent;
  private boolean allFileFilterUsed;
  private FileFilter[] fileFilters;
  private String fileName;

  /**
   * Creates a new FileIO with the given title used by the file chooser dialog.
   *
   * @param title the title
   */
  public FileIo(String title) {
    this.title = title;
  }

  public static FileNameExtensionFilter createFileFilter(String description, String... extensions) {
    String text = description + " " + Arrays.stream(extensions).map(s -> "*." + s).collect(Collectors.toList());
    return new FileNameExtensionFilter(text, extensions);
  }

  private static Path ensureFileExtension(Path path, FileFilter fileFilter) {
    if (fileFilter instanceof FileNameExtensionFilter) {
      String[] extensions = ((FileNameExtensionFilter) fileFilter).getExtensions();
      if (extensions.length > 0) {
        String extension = extensions[0];
        if (!path.toString().endsWith(extension)) {
          path = path.resolveSibling(path.getFileName() + "." + extension);
        }
      }
    }
    return path;
  }

  /**
   * Sets the parent component of the file chooser dialog.
   *
   * @param parent the parent component
   */
  public void setParent(Component parent) {
    this.parent = parent;
  }

  /**
   * Sets the file name used by the file chooser dialog.
   *
   * @param fileName the file name
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Sets whether the all file filter should be used.
   *
   * @param allFileFilterUsed whether the all file filter should be used
   */
  public void setAllFileFilterUsed(boolean allFileFilterUsed) {
    this.allFileFilterUsed = allFileFilterUsed;
  }

  /**
   * Sets the file filters used by the file chooser. The first file filter is used as default.
   *
   * @param fileFilters the file filters
   */
  public void setFileFilters(FileFilter... fileFilters) {
    this.fileFilters = fileFilters;
  }

  /**
   * Loads a file using the given read callback.
   *
   * @param read the read callback
   */
  public void load(Read read) {
    JFileChooser fileChooser = createFileChooser();

    int returnVal = fileChooser.showOpenDialog(parent);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      Path path = fileChooser.getSelectedFile().toPath();
      if (!Files.isReadable(path)) {
        ErrorHandler.handleError(title, "The file with the path " + path + " is not readable.");
        return;
      }
      try {
        read.read(Files.newInputStream(path));
      } catch (IOException ex) {
        ErrorHandler.handleError(title, "Could not import file", ex);
      }
    }
  }

  /**
   * Saves a file using the given write callback.
   *
   * @param write the write callback
   */
  public void save(Write write) {
    JFileChooser fileChooser = createFileChooser();

    int returnVal = fileChooser.showSaveDialog(parent);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      Path path = ensureFileExtension(fileChooser.getSelectedFile().toPath(), fileChooser.getFileFilter());
      if (!validatePath(path, parent)) {
        return;
      }
      try {
        write.write(Files.newOutputStream(path));
      } catch (IOException ex) {
        ErrorHandler.handleError(title, "Could not export file", ex);
      }
    }
  }

  private JFileChooser createFileChooser() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(title);
    fileChooser.setAcceptAllFileFilterUsed(allFileFilterUsed);
    if (fileFilters != null) {
      for (FileFilter filter : fileFilters) {
        fileChooser.addChoosableFileFilter(filter);
      }
      fileChooser.setFileFilter(fileFilters[0]);
    }
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fileName != null) {
      fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), fileName));
    }
    return fileChooser;
  }

  private boolean validatePath(Path path, Component parent) {
    if (Files.exists(path) && !Dialogs.confirmation("File already exists",
        "Do you want to overwrite the file?\n" + path.toAbsolutePath(), parent)) {
      return false;
    }
    if (!Files.exists(path.getParent())) {
      try {
        Files.createDirectories(path.getParent());
      } catch (IOException e) {
        ErrorHandler.handleError(title, "Could not create directory", e);
        return false;
      }
    }
    if (!Files.exists(path)) {
      try {
        Files.createFile(path);
      } catch (IOException e) {
        ErrorHandler.handleError(title, "Could not create file", e);
        return false;
      }
    }
    if (!Files.isWritable(path)) {
      ErrorHandler.handleError(title, "Cannot write to " + path + ".");
      return false;
    }
    return true;
  }

  /**
   * Callback interface for writing a file.
   */
  public interface Write {

    /**
     * Writes the file to the given output stream.
     *
     * @param outputStream the output stream
     * @throws IOException if an I/O error occurs
     */
    void write(OutputStream outputStream) throws IOException;
  }

  /**
   * Callback interface for reading a file.
   */
  public interface Read {

    /**
     * Reads the file from the given input stream.
     *
     * @param inputStream the input stream
     * @throws IOException if an I/O error occurs
     */
    void read(InputStream inputStream) throws IOException;
  }
}
