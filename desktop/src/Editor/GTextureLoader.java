package com.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;

import GameGDX.GDX;

public class GTextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {

    static public class TextureLoaderInfo {
        String filename;
        TextureData data;
        Texture texture;
    };

    TextureLoaderInfo info = new TextureLoaderInfo();

    public GTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        info.filename = fileName;
        if (parameter == null || parameter.textureData == null) {
            byte[] bytes = GDX.Decode(file);
            Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
            Pixmap.Format format = null;
            boolean genMipMaps = false;
            info.texture = null;

            if (parameter != null) {
                format = parameter.format;
                genMipMaps = parameter.genMipMaps;
                info.texture = parameter.texture;
            }

            info.data = new FileTextureData(file, pixmap, format, true);
        } else {
            info.data = parameter.textureData;
            info.texture = parameter.texture;
        }
        if (!info.data.isPrepared()) info.data.prepare();
    }

    @Override
    public Texture loadSync (AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        if (info == null) return null;
        Texture texture = info.texture;
        if (texture != null) {
            texture.load(info.data);
        } else {
            texture = new Texture(info.data);
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        return null;
    }
}
