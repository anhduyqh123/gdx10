package GameGDX;

import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GAudio {
    public static Plat plat = new Plat();
    public static GAudio i;
    private Map<String, GDX.Runnable<Float>> cbSound = new HashMap<>(),
            cbMusic = new HashMap<>(),
            cbVibrate = new HashMap<>();
    private List<String> singles = new ArrayList<>();
    private String musicName;
    public float soundVolume = GDX.GetPrefFloat("soundVolume", 1),
            musicVolume = GDX.GetPrefFloat("musicVolume", 1),
            vibrateVolume = GDX.GetPrefFloat("vibrateVolume", 1);

    public GAudio()
    {
        i = this;
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

    public void AddSoundEvent(String st, GDX.Runnable<Float> cb)
    {
        cbSound.put(st,cb);
        cb.Run(soundVolume);
    }
    public void AddMusicEvent(String st, GDX.Runnable<Float> cb)
    {
        cbMusic.put(st,cb);
        cb.Run(musicVolume);
    }
    public void AddVibrateEvent(String st, GDX.Runnable<Float> cb)
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
        plat.Vibrate((int)(num*i.vibrateVolume));
    }
    private void DoSingleVibrate(float delay,int num)
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
        DoVibrate(100);
        RefreshVibrate();
    }

    public void PlaySingleSound(String name)
    {
        i.PlaySingleSound(name,0.1f);
    }
    private void PlaySingleSound(String name,float delay)
    {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name);
        GDX.Delay(()->singles.remove(name),delay);
    }
    public void PlaySound(String name)
    {
        Assets.GetSound(name).play(i.soundVolume);
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
        plat.SoundLoop(name,vol*soundVolume,pan,cb);
    }
    public void SetPan(String name,long id,float vol,float pan)
    {
        Assets.GetSound(name).setPan(id,pan,vol*soundVolume);
    }
    public void StopSound(String name,long id)
    {
        Assets.GetSound(name).stop(id);
    }
    public static class Plat
    {
        public void Vibrate(int num)
        {
            GDX.Vibrate(num);
        }
        public void SoundLoop(String name,float volume,float pan,GDX.Runnable<Long> cb)
        {
            cb.Run(Assets.GetSound(name).loop(volume,1,pan));
        }
    }
}
