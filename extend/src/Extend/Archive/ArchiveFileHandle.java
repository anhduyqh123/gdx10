package Extend.Archive;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ArchiveFileHandle extends FileHandle {
    private final String url;
    private ZipFileHandle zipFile;

    public ArchiveFileHandle(ZipFileHandle zipFile, String url)
    {
        super(url.replace('\\', '/'), Files.FileType.Classpath);
        this.url = url;
        this.zipFile = zipFile;
    }
    public ArchiveFileHandle(ZipFileHandle zipFile, File file)
    {
        super(file, Files.FileType.Classpath);
        this.url = file.getPath();
        this.zipFile = zipFile;
    }

    @Override
    public InputStream read() {
        return zipFile.GetInputStream(url);
    }
    @Override
    public FileHandle child(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) return new ArchiveFileHandle(zipFile, new File(name));
        return new ArchiveFileHandle(zipFile, new File(file, name));
    }

    @Override
    public FileHandle sibling(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root.");
        return new ArchiveFileHandle(zipFile, new File(file.getParent(), name));
    }

    @Override
    public FileHandle parent() {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == Files.FileType.Absolute)
                parent = new File("/");
            else
                parent = new File("");
        }
        return new ArchiveFileHandle(zipFile, parent);
    }

    @Override
    public boolean exists() {
        return zipFile.GetInputStream(url)!=null;
    }

    @Override
    public long length () {
        try {
            return zipFile.GetInputStream(url).available();
        }catch (IOException e){}
        return 0;
    }
}
