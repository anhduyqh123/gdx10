package Editor.ParticleViewer;

import GameGDX.GDXGame;
import GameGDX.Scene;

public class MyGame extends GDXGame {
    public Runnable done;

    public MyGame(Runnable done)
    {
        this.done = done;
    }
    @Override
    protected Scene NewScene() {
        return new Scene(720,720);
    }

    @Override
    public void DoneLoading() {
        done.run();
    }
}
