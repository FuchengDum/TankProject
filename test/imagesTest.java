import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class imagesTest {
    @Test
    public void testLoadImage(){
        try{
            BufferedImage image = ImageIO.read(new File("D:\\IdeaProject\\mashiping\\tankProject\\src\\images\\tank1.jpeg"));
            assertNotNull(image);
            BufferedImage image2 = ImageIO.read(imagesTest.class.getClassLoader().getResourceAsStream("images/tank2.jpeg"));
            assertNotNull(image2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRotateImage(){
        try {
            BufferedImage tank1 = ImageIO.read(imagesTest.class.getClassLoader().getResourceAsStream("images/tank2.jpeg"));
            tank1 = rotateImage(tank1,90);
            assertNotNull(tank1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage rotateImage( final BufferedImage bufferedImage, final int degree) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage image;
        Graphics2D graphics2D;
        (graphics2D = (image = new BufferedImage(w,h,type))
                .createGraphics()).setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        graphics2D.rotate(Math.toRadians(degree),w/2,h/2);
        graphics2D.drawImage(bufferedImage,0,0,null);
        graphics2D.dispose();
        return image;


    }
}
