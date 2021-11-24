package GameGDX;

import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GAudio {
    public static GDX.Runnable<Integer> doVibrate;

    public static GAudio i;
    private Map<String, GDX.Runnable<Float>> cbSound,cbMusic,cbVibrate;
    private List<String> singles;

    private String musicName;
    public float soundVolume = 1,musicVolume = 1,vibrateVolume = 1;

    public GAudio()
    {
        i = this;
        soundVolume = GDX.GetPrefFloat("soundVolume", soundVolume);
        musicVolume = GDX.GetPrefFloat("musicVolume", musicVolume);
        vibrateVolume = GDX.GetPrefFloat("vibrateVolume", vibrateVolume);

        cbSound = new HashMap<>();
        cbMusic = new HashMap<>();
        cbVibrate = new HashMap<>();
        singles = new ArrayList<>();
    }
    private void Refresh(Map<String, GDX.Runnable<Float>> map,float volume)
    {
        for(GDX.Runnable cb : map.values()) cb.Run(volume);
    }
    private void RefreshSound()
    {
        Refresh(cbSound,soundVolume);
    }
    private void RefreshMusic()
    {
        Refresh(cbMusic,musicVolume);
    }
    private void RefreshVibrate()
    {
        Refresh(cbVibrate,vibrateVolume);
    }

    public void AddSoundCallback(String st, GDX.Runnable<Float> cb)
    {
        cbSound.put(st,cb);
        cb.Run(soundVolume);
    }
    public void AddMusicCallback(String st, GDX.Runnable<Float> cb)
    {
        cbMusic.put(st,cb);
        cb.Run(musicVolume);
    }
    public void AddVibrateCallback(String st, GDX.Runnable<Float> cb)
    {
        cbVibrate.put(st,cb);
        cb.Run(vibrateVolume);
    }
    public void DoVibrate()
    {
        DoVibrate(30);
    }
    public void DoVibrate(int num)
    {
        if (doVibrate!=null) doVibrate.Run((int)(num*vibrateVolume));
    }
    public void DoSingleVibrate(float delay,int num)
    {
        if (singles.contains("vibrate")) return;
        singles.add("vibrate");
        DoVibrate(num);
        GDX.Delay(()->singles.remove("vibrate"),delay);
    }
    public void DoSingleVibrate(float delay)
    {
        DoSingleVibrate(delay,30);
    }
    public void DoSingleVibrate()
    {
        DoSingleVibrate(0.1f,30);
    }

    private float Switch(float volume)
    {
        return volume==0?1:0;
    }
    public void SwitchVibrate()
    {
        vibrateVolume = Switch(vibrateVolume);
        GDX.SetPrefFloat("vibrateVolume", vibrateVolume);
        if (doVibrate!=null) doVibrate.Run((int)(100*vibrateVolume));
        RefreshVibrate();
    }

    public void PlaySingleSound(String name)
    {
        PlaySingleSound(name,0.1f);
    }
    public void PlaySingleSound(String name,float delay)
    {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name);
        GDX.Delay(()->singles.remove(name),delay);
    }
    public void PlaySound(String name)
    {
        Assets.GetSound(name).play(soundVolume);
    }
    public void StopSound(String name)
    {
        Assets.GetSound(name).stop();
    }

    public void SwitchSound()
    {
        SetSoundVolume(Switch(soundVolume));
    }
    public void SetSoundVolume(float volume)
    {
        soundVolume = volume;
        GDX.SetPrefFloat("soundVolume", soundVolume);
        RefreshSound();
    }
    public void SwitchMusic()
    {
        SetMusicVolume(Switch(musicVolume));
    }
    public void SetMusicVolume(float volume)
    {
        musicVolume = volume;
        GDX.SetPrefFloat("musicVolume", musicVolume);
        RefreshMusic();
        if (musicName==null) return;
        Assets.GetMusic(musicName).setVolume(musicVolume);
    }
    public void SetVibrateVolume(float volume)
    {
        vibrateVolume = volume;
        GDX.SetPrefFloat("vibrateVolume", vibrateVolume);
        RefreshVibrate();
    }

    public void StopMusic(String name)
    {
        Assets.GetMusic(name).stop();
    }
    public void StartMusic(String name)
    {
        musicName = name;
        Music music = Assets.GetMusic(name);
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();
    }

    public void PauseMusic()
    {
        if (musicName==null) return;
        Assets.GetMusic(musicName).pause();
    }
    public void ResumeMusic()
    {
        if (musicName==null) return;
        Assets.GetMusic(musicName).play();
    }
    public void StopMusic()
    {
        if (musicName==null) return;
        StopMusic(musicName);
        musicName = null;
    }

    //Sound for Game
    public void PlaySound(String name,float vol,float pan)
    {
        Assets.GetSound(name).play(vol* soundVolume,1,pan);
    }

    public void PlayLoop(String name, float vol, float pan, GDX.Runnable<Long> cb)
    {
        GDXSdk.i.SoundLoop(name,vol*soundVolume,pan,cb);
    }
    public void SetPan(String name,long id,float vol,float pan)
    {
        Assets.GetSound(name).setPan(id,pan,vol*soundVolume);
    }
    public void StopSound(String name,long id)
    {
        Assets.GetSound(name).stop(id);
    }
}
