package Extend.Util;

import GameGDX.Actors.Particle;
import GameGDX.GDX;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

public class Pools {
    private static Map<String, Pool> map = new HashMap<>();

    public static Pool Get(String key) {
        return map.get(key);
    }
    public static void Put(String key,Pool pool)
    {
        map.put(key, pool);
    }
    public static void Put(String key,Pool pool,int size)
    {
        pool.fill(size);
        Put(key, pool);
    }
    public static void Clear()
    {
        map.clear();
    }
    public static void Clear(String key)
    {
        map.get(key).clear();
    }

    public static <T> T Obtain (String key, GDX.Func<Object> newObject)
    {
        if (!Contains(key))
            Put(key, new Pool() {
                @Override
                protected Object newObject() {
                    return newObject.Run();
                }
            });
        return Obtain(key);
    }
    public static <T> T Obtain (String key, int size, GDX.Func<Object> newObject)
    {
        if (!Contains(key))
            {
                Put(key, new Pool() {
                    @Override
                    protected Object newObject() {
                        return newObject.Run();
                    }
                });
                map.get(key).fill(size);
            }
        return Obtain(key);
    }
    public static <T> T Obtain (String key) {
        return (T)map.get(key).obtain();
    }
    public static void Free(String key,Object object)
    {
        map.get(key).free(object);
    }
    public static boolean Contains(String key)
    {
        return map.containsKey(key);
    }

    //Use
    public static Particle GetParticle(String name, int size, float x, float y, Group parent)
    {
        Particle par = Pools.Obtain(name,size,()-> new Particle(name));
        par.onRemove = ()->Pools.Free(name,par);
        par.setPosition(x,y);
        parent.addActor(par);
        return par;
    }
}
