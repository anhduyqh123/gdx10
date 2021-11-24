package Extend.Spine;

import GameGDX.Assets;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.esotericsoftware.spine.utils.SkeletonActorPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GSpine extends Group {
    private static Map<SkeletonData,SkeletonActorPool> poolMap = new HashMap<>();
    private static SkeletonRenderer renderer;

    private static void NewRenderer()
    {
        if (renderer!=null) return;
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
    }
    private static void NewPool(SkeletonData data,int initial)
    {
        if (poolMap.containsKey(data)) return;
        new Skeleton(data);
        AnimationStateData stateData = new AnimationStateData(data);
        new AnimationState(stateData);
        poolMap.put(data,new SkeletonActorPool(renderer,data,stateData,initial,Integer.MAX_VALUE));
    }
    private static void Free(SkeletonData data,SkeletonActor actor)
    {
        if (!poolMap.containsKey(data)) return;
        poolMap.get(data).free(actor);
    }
    private static SkeletonActor GetSpine(SkeletonData data)
    {
        NewRenderer();
        if (!poolMap.containsKey(data)) NewPool(data,1);
        return poolMap.get(data).obtain();
    }

    private SkeletonActor actor;
    private SkeletonData data;

    public GSpine(){}
    public GSpine(String name)
    {
        this(Assets.Get(name,SkeletonData.class));
    }
    public GSpine(SkeletonData data)
    {
        SetData(data);
        setSize(data.getWidth(),data.getHeight());
    }
    public void SetSpinePositionBy(float delX, float delY)
    {
        actor.setPosition(data.getWidth()/2+delX,data.getHeight()/2+delY);
    }
    public void SetData(SkeletonData data)
    {
        this.data = data;
        actor = GetSpine(data);
        actor.setPosition(data.getWidth()/2,data.getHeight()/2);
        clearChildren();
        this.addActor(actor);
    }
    public void SetSkin(String name)
    {
        Skin skin = data.findSkin(name);
        if (skin==null) skin = data.getSkins().get(0);
        actor.getSkeleton().setSkin(skin);
    }

    public AnimationState.TrackEntry SetAnimation(String name)
    {
        return SetAnimation(name,"idle");
    }
    public AnimationState.TrackEntry SetAnimation(String name, String idle)
    {
        return SetAnimation(name,()->SetAnimation(idle,true));
    }
    public AnimationState.TrackEntry SetAnimation(String name, boolean loop)
    {
        return actor.getAnimationState().setAnimation(1,name,loop);
    }
    public AnimationState.TrackEntry SetAnimation(String name, Runnable done)
    {
        AnimationState.TrackEntry track = SetAnimation(name,false);
        track.setListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                done.run();
            }
        });
        return track;
    }

    @Override
    public boolean remove() {
        //Free(data,actor);
        return super.remove();
    }

    public List<String> GetAnimationNames()
    {
        List<String> list = new ArrayList<>();
        if (data==null) return list;
        for(Animation a : data.getAnimations())
            list.add(a.getName());
        return list;
    }
    public String[] GetSkinNames()
    {
        if (data==null) return null;
        List<String> list = new ArrayList<>();
        for (Skin s : data.getSkins())
            list.add(s.getName());
        return list.toArray(new String[list.size()]);
    }
}
