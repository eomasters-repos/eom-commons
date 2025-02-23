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

import com.google.gson.GsonBuilder;

// TODO: move to eom-commons
/**
 * A functional interface for configuring the {@link GsonStore}, more specifically the underlying {@link GsonBuilder}
 */
@FunctionalInterface
public interface GsonStoreConfig {
    void configBuilder(final GsonBuilder builder);
}
