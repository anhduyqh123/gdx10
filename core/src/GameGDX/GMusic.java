package GameGDX;

import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMusic {
    public static GDX.Runnable<Integer> doVibrate;
    private static boolean music,sound,vibrate;
    private static String musicName;

    private static Map<String, GDX.Runnable<Boolean>> cbSound,cbMusic,cbVibrate;
    private static List<String> singles;

    public GMusic()
    {
        sound = GDX.GetPrefBoolean("sound",true);
        music = GDX.GetPrefBoolean("music",true);
        vibrate = GDX.GetPrefBoolean("vibrate",true);
        cbSound = new HashMap<>();
        cbMusic = new HashMap<>();
        cbVibrate = new HashMap<>();
        singles = new ArrayList<>();
    }
    private static void Refresh(Map<String, GDX.Runnable<Boolean>> map,boolean active)
    {
        for(GDX.Runnable cb : map.values()) cb.Run(active);
    }
    private static void RefreshSound()
    {
        Refresh(cbSound,sound);
    }
    private static void RefreshMusic()
    {
        Refresh(cbMusic,music);
    }
    private static void RefreshVibrate()
    {
        Refresh(cbVibrate,vibrate);
    }

    public static void AddSoundCallback(String st, GDX.Runnable<Boolean> cb)
    {
        cbSound.put(st,cb);
        cb.Run(sound);
    }
    public static void AddMusicCallback(String st, GDX.Runnable<Boolean> cb)
    {
        cbMusic.put(st,cb);
        cb.Run(music);
    }
    public static void AddVibrateCallback(String st, GDX.Runnable<Boolean> cb)
    {
        cbVibrate.put(st,cb);
        cb.Run(vibrate);
    }
    public static void DoVibrate()
    {
        DoVibrate(30);
    }
    public static void DoVibrate(int num)
    {
        if (!vibrate) return;
        if (doVibrate!=null) doVibrate.Run(num);
    }
    public static void DoSingleVibrate(float delay,int num)
    {
        if (singles.contains("vibrate")) return;
        singles.add("vibrate");
        DoVibrate(num);
        GDX.DelayRunnable(()->singles.remove("vibrate"),delay);
    }
    public static void DoSingleVibrate(float delay)
    {
        DoSingleVibrate(delay,30);
    }
    public static void DoSingleVibrate()
    {
        DoSingleVibrate(0.1f,30);
    }

    public static void SwitchVibrate()
    {
        vibrate = !vibrate;
        GDX.SetPrefBoolean("vibrate",vibrate);
        if (vibrate && doVibrate!=null) doVibrate.Run(100);
        RefreshVibrate();
    }

    public static void PlaySingleSound(String name)
    {
        PlaySingleSound(name,0.1f);
    }
    public static void PlaySingleSound(String name,float delay)
    {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name);
        GDX.DelayRunnable(()->singles.remove(name),delay);
    }
    public static void PlaySound(String name)
    {
        if (!sound) return;
        Assets.GetSound(name).play();
    }
    public static void SwitchSound()
    {
        SetSound(!sound);
        RefreshSound();
    }
    public static void SetSound(boolean value)
    {
        sound = value;
        GDX.SetPrefBoolean("sound",value);
    }
    public static void SwitchMusic()
    {
        SetMusic(!music);
    }
    public static void SetMusic(boolean value)
    {
        music = value;
        GDX.SetPrefBoolean("music",value);
        RefreshMusic();
        OnMusic(value);
    }
    private static void OnMusic(boolean active)
    {
        if (musicName==null) return;
        if (active) StartMusic(musicName);
        else StopMusic(musicName);
    }
    public static void PlayMusic(String name)
    {
        if (musicName!=null){
            if (musicName.equals(name)) return;
            StopMusic(musicName);
        }
        StartMusic(name);
    }
    public static void StopMusic(String name)
    {
        Assets.GetMusic(name).stop();
    }
    private static void StartMusic(String name)
    {
        musicName = name;
        if (!music) return;
        Music music = Assets.GetMusic(name);
        music.setLooping(true);
        music.play();
    }

    public static void OnPause()
    {
        if (!music) return;
        OnMusic(false);
    }
    public static void OnResume()
    {
        if (!music) return;
        OnMusic(true);
    }
    //Get Value
    public static boolean IsSound()
    {
        return sound;
    }
    public static boolean IsMusic()
    {
        return music;
    }
    public static boolean IsVibrate()
    {
        return vibrate;
    }
}
