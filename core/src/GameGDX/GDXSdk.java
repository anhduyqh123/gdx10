package GameGDX;

public class GDXSdk {
    public static GDXSdk i = new GDXSdk();

    public void SoundLoop(String name,float volume,float pan,GDX.Runnable<Long> cb)
    {
        cb.Run(Assets.GetSound(name).loop(volume,1,pan));
    }
}
