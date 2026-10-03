package io.b4siliq.presentation.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.text.Font;

public final class FontLoaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(FontLoaderUtil.class);

    private FontLoaderUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final Font loadFont(Class<?> currentClass, String fontName, int size) {
        try (var is = currentClass.getResourceAsStream("/io/b4siliq/fonts/" + fontName)) {
            if (is == null) {
                logger.error("Cannot load this font...");
                return null;
            }

            var font = Font.loadFont(is, size);
            if (font == null) {
                logger.error("It seems that this font is not working");
            }

            return font;
        } catch(IOException e) {
            logger.error(
                "Something went wrong!\n{}",
                e.getMessage()
            );
            return null;
        }
    }
}
