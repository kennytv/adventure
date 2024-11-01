package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ShadowColorTagResolver implements TagResolver, SerializableResolver.Single {
  private static final String SHADOW_COLOR = "shadow";

  static final TagResolver INSTANCE = new ShadowColorTagResolver();
  private static final StyleClaim<ShadowColor> STYLE = StyleClaim.claim(SHADOW_COLOR, Style::shadowColor, (color, emitter) -> {
      emitter.tag(SHADOW_COLOR).argument(color.asHexString());
  });

  ShadowColorTagResolver() {
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException {
    if (!this.has(name)) {
      return null;
    }

    final String colorString = args.popOr("Expected to find a color parameter: #RRGGBBAA").lowerValue();
    final ShadowColor color = ShadowColor.fromHexString(colorString);
    if (color == null) {
      throw ctx.newException(String.format("Unable to parse a shadow color from '%s'. Please use #RRGGBBAA formatting.", colorString));
    }
    return Tag.styling(color);
  }

  @Override
  public boolean has(final @NotNull String name) {
    return name.equals(SHADOW_COLOR);
  }

  @Override
  public @Nullable StyleClaim<?> claimStyle() {
    return STYLE;
  }
}
