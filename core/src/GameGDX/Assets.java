package GameGDX;

import GameGDX.AssetLoading.AssetNode;
import GameGDX.AssetLoading.AssetPackage;
import GameGDX.AssetLoading.GameData;
import GameGDX.GUIData.GUIData;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets extends Actor {
    public static Assets i;

    private List<String> packLoaded = new ArrayList<>(); // loaded
    private Map<String, AssetNode> mapAssets = new HashMap<>(); //loaded node
    private GameData gameData;
    protected AssetManager manager = NewAssetManager();

    private Runnable doneLoading;
    private GDX.Runnable<Float> cbAssetsUpdate;

    public Assets()
    {
        i = this;
    }
    public void SetData(GameData gameData)
    {
        this.gameData = gameData;
        gameData.Install();
    }
    protected AssetManager NewAssetManager()
    {
        return new AssetManager();
    }

    @Override
    public void act(float delta) {
        UpdateAssetLoading();
        super.act(delta);
    }

    private void UpdateAssetLoading()
    {
        if (doneLoading==null) return;
        if (manager.update()) {
            Runnable done = doneLoading;
            doneLoading = null;
            done.run();
        }
        if (cbAssetsUpdate!=null) cbAssetsUpdate.Run(manager.getProgress());
    }

    //LoadAsset
    private static void LoadAssets(List<AssetNode> list)
    {
        if (list==null) return;
        for (AssetNode as : list)
            i.Load(as);
    }
    public void Load(String url,Class type)
    {
        manager.load(url,type);
    }
    protected void Load(AssetNode as)
    {
        if (manager.isLoaded(as.url)) return;
        switch (as.kind)
        {
            case TextureAtlas:
                manager.load(as.url, TextureAtlas.class);
                break;
            case Texture:
                TextureLoader.TextureParameter pepTexture = new TextureLoader.TextureParameter();
                pepTexture.minFilter = Texture.TextureFilter.Linear;
                pepTexture.magFilter = Texture.TextureFilter.Linear;
                manager.load(as.url, Texture.class,pepTexture);
                break;
            case Sound:
                manager.load(as.url, Sound.class);
                break;
            case Music:
                manager.load(as.url, Music.class);
                break;
            case BitmapFont:
                BitmapFontLoader.BitmapFontParameter pepBitmap = new BitmapFontLoader.BitmapFontParameter();
                pepBitmap.minFilter = Texture.TextureFilter.Linear;
                pepBitmap.magFilter = Texture.TextureFilter.Linear;
                pepBitmap.loadedCallback = (assetManager, fileName, type) -> {
                    manager.get(as.url, BitmapFont.class).getData().markupEnabled = true;
                };
                manager.load(as.url, BitmapFont.class,pepBitmap);
                break;
            case Particle:
                ParticleEffectLoader.ParticleEffectParameter pepParticle=  new ParticleEffectLoader.ParticleEffectParameter();
                AssetPackage pack = GetAssetPackage(as.pack);
                if (pack.Contain("particles"))
                    pepParticle.atlasFile = pack.Get("particles").url;
                manager.load(as.url, ParticleEffect.class,pepParticle);
                break;
            default:
        }
    }

    public void LoadPackage(String pack)
    {
        if (!gameData.Contains(pack)) return;
        if (packLoaded.contains(pack)) return;
        packLoaded.add(pack);
        for(AssetNode n : GetAssetPackage(pack).assetNodes)
            mapAssets.put(n.name,n);
        LoadAssets(GetAssetPackage(pack).loadableNode);
    }
    public static void LoadPackages(Runnable done, String... packs){
        for(String pack : packs) i.LoadPackage(pack);
        i.manager.finishLoading();
        if(done!=null) done.run();
    }
    public static void LoadPackagesSync(GDX.Runnable<Float> cbProgress, Runnable onLoaded, String... packs)
    {
        for(String pack : packs) i.LoadPackage(pack);
        i.doneLoading = onLoaded;
        i.cbAssetsUpdate = cbProgress;
    }

    public static void UnloadPackage(String pack)
    {
        if (!i.gameData.Contains(pack)) return;
        RemovePackage(pack);
        for (AssetNode n : GetAssetPackage(pack).loadableNode)
        {
            GUIData.i.Remove(n.name);
            if (i.manager.contains(n.url))
                i.manager.unload(n.url);
        }
    }
    public static void UnloadPackages(String... packs)
    {
        for (String pack : packs) UnloadPackage(pack);
    }
    public static void RemovePackage(String pack)
    {
        i.packLoaded.remove(pack);
    }

    //get value
    public static TextureRegion GetTexture(String name)
    {
        AssetNode node = GetNode(name);
        AssetPackage pack = GetAssetPackage(node.pack);
        if (pack.Contain(node.atlas))
        {
            AssetNode al = pack.Get(node.atlas);
            return i.manager.get(al.url, TextureAtlas.class).findRegion(name);
        }
        return new TextureRegion(Get(name, Texture.class));
    }

    public static BitmapFont GetFont(String name)
    {
        return Get(name, BitmapFont.class);
    }
    public static Sound GetSound(String name)
    {
        return Get(name, Sound.class);
    }
    public static Music GetMusic(String name)
    {
        return Get(name, Music.class);
    }
    public static ParticleEffect GetParticleEffect(String name)
    {
        return Get(name, ParticleEffect.class);
    }
    public static <T> T Get(String name,Class<T> type){
        AssetNode as = GetNode(name);
        return i.manager.get(as.url,type);
    }
    public static AssetNode GetNode(String name)
    {
        return i.mapAssets.get(name);
    }
    public static AssetPackage GetAssetPackage(String name)
    {
        return i.gameData.Get(name);
    }
    public static List<AssetNode> Get(AssetNode.Kind kind)
    {
        List<AssetNode> list = new ArrayList<>();
        for(AssetPackage p : i.gameData.GetAll())
            for(AssetNode n : p.loadableNode)
                if (n.kind==kind) list.add(n);
        return list;
    }
    public static List<AssetNode> GetLoaded(AssetNode.Kind kind)
    {
        List<AssetNode> list = new ArrayList<>();
        for(AssetNode n : i.mapAssets.values())
                if (n.kind==kind) list.add(n);
        return list;
    }
    public static AssetManager GetManager()
    {
        return i.manager;
    }
    public static GameData GetData()
    {
        return i.gameData;
    }
}
