package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BaseGame extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private Texture masked, original;
    private final int size = 256;

    @Override
    public void create () {
        /* Needed to render our textures, a ShapeRenderer won't work with this technique. */
        spriteBatch = new SpriteBatch();

        /* The path to the image to mask. */
        FileHandle imagePath = new FileHandle("first/textures/mask1.png");

        /* Load the pixels of our image into a Pixmap. */
        Pixmap pixmap = new Pixmap(imagePath);

        /* Have an unaltered version for comparison. */
        original = new Texture(imagePath);

        /* Apply the mask to our Pixmap. */
        pixmap = applyMask(pixmap);

        /* Load the pixel information of the Pixmap into a Texture for drawing. */
        masked = new Texture(pixmap);
    }
    private Pixmap applyMask(Pixmap source) {
        /* Create a Pixmap to store the mask information, at the end it will
         * contain the result. */
        Pixmap result = new Pixmap(source.getWidth(), source.getHeight(), Pixmap.Format.RGBA8888);

        /* This setting lets us overwrite the pixels' transparency. */
        result.setBlending(Pixmap.Blending.None);

        /* Ignore RGB values unless you want funky results, alpha is for the mask. */
        result.setColor(new Color(1f, 1f, 1f, 1f));

        /* Draw a circle to our mask, any shape is possible since
         * you can draw individual pixels to the Pixmap. */
        result.fillCircle(size / 2, size / 2, size / 2);

        /* Draw a rectangle with little alpha to our mask, this will turn
         * a corner of the original image transparent. */
        result.setColor(1f, 1f, 1f, 0.25f);
        result.fillRectangle(size / 2, size / 2, size / 2, size / 2);

        /* We can also define the mask by loading an image:
         * result = new Pixmap(new FileHandle("image.png")); */

        /* Decide the color of each pixel using the AND bitwise operator. */
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.drawPixel(x, y, source.getPixel(x, y) & result.getPixel(x, y));
            }
        }

        return result;
    }
    private void drawImages() {
        /* Draw the original image for comparison. */
//        spriteBatch.setColor(Color.WHITE);
//        spriteBatch.draw(original, 0, size, size, size);

        /* Draw the masked image in red. */
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(masked, 20, 20, size, size);
    }

    @Override
    public void render () {
        ScreenUtils.clear(Color.BLACK);

        spriteBatch.begin();
        drawImages();
        spriteBatch.end();
    }
}
