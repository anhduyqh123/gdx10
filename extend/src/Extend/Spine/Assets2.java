package Extend.Spine;

import GameGDX.AssetLoading.AssetNode;
import GameGDX.Assets;
import com.esotericsoftware.spine.SkeletonData;

public class Assets2 extends Assets {

    public Assets2()
    {
        super();
        manager.setLoader(SkeletonData.class,new SkeletonDataLoader(manager.getFileHandleResolver()));
    }
    @Override
    protected void Load(AssetNode as) {
        if (manager.isLoaded(as.url)) return;
        if (as.kind== AssetNode.Kind.Spine)
        {
            String atlas = as.url.replace("json","atlas");
            SkeletonDataLoader.SkeletonDataLoaderParameter pep =
                    new SkeletonDataLoader.SkeletonDataLoaderParameter(atlas);
            manager.load(as.url,SkeletonData.class,pep);
            return;
        }
        super.Load(as);
    }
}
