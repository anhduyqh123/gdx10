package GameGDX.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GGroup extends Group {
    public boolean clip;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (clip) ClipDraw(batch, parentAlpha);
        else super.draw(batch, parentAlpha);
    }
    private void ClipDraw(Batch batch, float parentAlpha)
    {
        batch.flush();
        if (clipBegin())
        {
            super.draw(batch, parentAlpha);
            clipEnd();
        }
    }
}
