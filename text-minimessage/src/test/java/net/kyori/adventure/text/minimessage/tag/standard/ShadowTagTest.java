package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

class ShadowTagTest extends AbstractTest {

  @Test
  void testSerializeShadow() {
    final String expected = "<shadow:#054D79FF>This is a test";

    final Component builder = Component.text("This is a test").shadowColor(ShadowColor.shadowColor(-16429703));

    this.assertSerializedEquals(expected, builder);
    this.assertParsedEquals(builder, expected);
  }

  @Test
  void testSerializeShadowClosing() {
    final String expected = "<shadow:#054D79FF>This is a</shadow> test";

    final Component builder = Component.text()
      .append(Component.text("This is a").shadowColor(ShadowColor.shadowColor(-16429703)))
      .append(Component.text(" test")).asComponent();

    this.assertSerializedEquals(expected, builder);
    this.assertParsedEquals(builder, expected);
  }
}
