package es.edwardbelt.hycraft.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public final class ServerIconUtil {
    private static String cachedServerIconBase64 = "";

    public static void loadServerIcon(File iconFile) throws IOException {
        BufferedImage image = ImageIO.read(iconFile);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);

        cachedServerIconBase64 = Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String getServerIconBase64() {
        return cachedServerIconBase64;
    }
}
