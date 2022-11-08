package Editor.ParticleViewer;

import GameGDX.Actors.Particle;
import GameGDX.GDX;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import java.util.HashSet;
import java.util.Set;

public class ParModel {
    public String name;
    public ParticleEffect pe = new ParticleEffect();
    public Set<String> imagePath;
    private FileHandle file;

    public ParModel(FileHandle file)
    {
        this.file = file;
        name = file.nameWithoutExtension();
        pe.load(file, file.parent());
        imagePath = GetImages(pe);
    }
    private Set<String> GetImages(ParticleEffect pe)
    {
        Set<String> list = new HashSet<>();
        for (ParticleEmitter i : pe.getEmitters())
        {
            for (String s : i.getImagePaths())
                list.add(GetImageName(s));
        }
        return list;
    }
    private String GetImageName(String path)
    {
        String[] arr = path.split("\\\\");
        return arr[arr.length-1];
    }
    public String ToString()
    {
        String st = "";
        for (String s : imagePath)
            st += s+"\n";
        return st;
    }
    public void Export(FileHandle dir)
    {
        Clone(dir.path()+"/"+file.name(),file);
        for (String s : imagePath)
        {
            String path = file.parent().path()+"/"+s;
            Clone(dir.path()+"/"+s,new FileHandle(path));
        }
    }
    private void Clone(String path,FileHandle fileHandle)
    {
        FileHandle clone = new FileHandle(path);
        fileHandle.copyTo(clone);
    }
    public void Delete()
    {
        new FileHandle(file.path()).delete();
    }
}
