/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.format;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ShadowColor extends StyleBuilderApplicable {
  @Contract(pure = true)
  static ShadowColor shadowColor(final int value) {
    return new ShadowColorImpl(value);
  }

  @Contract(pure = true)
  static ShadowColor fromHexString(String value) {
    if (value.startsWith("#")) {
      value = value.substring(1);
    }
    if (value.length() != 8) {
      return null;
    }
    try {
      int r = Integer.parseInt(value.substring(0, 2), 16);
      int g = Integer.parseInt(value.substring(2, 4), 16);
      int b = Integer.parseInt(value.substring(4, 6), 16);
      int a = Integer.parseInt(value.substring(6, 8), 16);
      return new ShadowColorImpl((a << 24) | (r << 16) | (g << 8) | b);
    } catch (NumberFormatException ignored) {
      return null;
    }
  }

  @NotNull
  default String asHexString() {
    int argb = value();
    int a = (argb >> 24) & 0xFF;
    int r = (argb >> 16) & 0xFF;
    int g = (argb >> 8) & 0xFF;
    int b = argb & 0xFF;
    return String.format("#%02X%02X%02X%02X", r, g, b, a);
  }

  int value();

  @Override
  default void styleApply(final Style.@NotNull Builder style) {
    style.shadowColor(this);
  }
}
