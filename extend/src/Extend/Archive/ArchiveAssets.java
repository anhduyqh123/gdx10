package Extend.Archive;

import GameGDX.Assets;
import com.badlogic.gdx.assets.AssetManager;

public class ArchiveAssets extends Assets {
    @Override
    protected AssetManager NewAssetManager() {
        ZipFileHandle zip = new ZipFileHandle("assets.zip");
        ArchiveFileHandleResolver resolver = new ArchiveFileHandleResolver(zip);
        return new AssetManager(resolver);
    }
}
