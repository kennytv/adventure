/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.transformation.inbuild.TemplateTransformation;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class TransformationRegistry {

  public static TransformationRegistry EMPTY = new TransformationRegistry();

  static {
    EMPTY.clear();
  }

  private final List<TransformationType<? extends Transformation>> types = new ArrayList<>();

  public TransformationRegistry() {
    this.register(TransformationType.COLOR);
    this.register(TransformationType.DECORATION);
    this.register(TransformationType.HOVER_EVENT);
    this.register(TransformationType.CLICK_EVENT);
    this.register(TransformationType.KEYBIND);
    this.register(TransformationType.TRANSLATABLE);
    this.register(TransformationType.INSERTION);
    this.register(TransformationType.FONT);
    this.register(TransformationType.GRADIENT);
    this.register(TransformationType.RAINBOW);
    this.register(TransformationType.RESET);
    this.register(TransformationType.PRE);
  }

  public TransformationRegistry(TransformationType<? extends Transformation>... types) {
    for (TransformationType<? extends Transformation> type : types) {
      this.register(type);
    }
  }

  public void clear() {
    this.types.clear();
  }

  public <T extends Transformation> void register(final TransformationType<T> type) {
    this.types.add(type);
  }

  public @Nullable Transformation get(final String name, final List<Token> inners, final Map<String, Template.ComponentTemplate> templates, final Function<String, ComponentLike> placeholderResolver) {
    for (final TransformationType<? extends Transformation> type : this.types) {
      if (type.canParse.test(name)) {
        final Transformation transformation = type.parser.parse();
        transformation.load(name, inners);
        return transformation;
      } else if (templates.containsKey(name)) {
        final TemplateTransformation transformation = new TemplateTransformation(templates.get(name));
        transformation.load(name, inners);
        return transformation;
      } else {
        ComponentLike potentialTemplate = placeholderResolver.apply(name);
        if (potentialTemplate != null) {
          final TemplateTransformation transformation = new TemplateTransformation(new Template.ComponentTemplate(name, potentialTemplate.asComponent()));
          transformation.load(name, inners);
          return transformation;
        }
      }
    }

    return null;
  }

  public boolean exists(final String name) {
    for (final TransformationType<? extends Transformation> type : this.types) {
      if (type.canParse.test(name)) {
        return true;
      }
    }
    return false;
  }
}
