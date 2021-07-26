package Extend.Archive;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class ArchiveFileHandleResolver implements FileHandleResolver {
    private ZipFileHandle zipFile;

    public ArchiveFileHandleResolver(ZipFileHandle zipFile)
    {
        this.zipFile = zipFile;
    }
    @Override
    public FileHandle resolve(String fileName) {
        return new ArchiveFileHandle(zipFile,fileName);
    }
}
