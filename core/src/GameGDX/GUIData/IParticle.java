package GameGDX.GUIData;

import GameGDX.Actors.Particle;
import GameGDX.Assets;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Json;
import GameGDX.Util;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class IParticle extends IActor {

    public boolean start = true;
    public String parName = "";

    @Override
    protected Actor NewActor() {
        return new Particle(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
            }
            @Override
            public boolean remove() {
                ForComponent((n,p)->p.Remove());
                return super.remove();
            }
        };
    }

    @Override
    public void Refresh() {
        InitActor();
        RefreshEffect();
        BaseRefresh();
        if (start)
            Continuous();

        AfterRefresh();
    }

    private void RefreshEffect()
    {
        try {
            Particle par = GetActor();
            ParticleEffect pe = Assets.GetParticleEffect(parName);
            par.SetParticleEffect(pe);
            RefreshEmitter();
        }
        catch (Exception e){}
    }
    private void Continuous()
    {
        try {
            Particle par = GetActor();
            if (par.GetEmitter(0).isContinuous())
                Start();
        }
        catch (Exception e){}
    }
    public void Start()
    {
        Particle par = GetActor();
        par.Start(false);
    }
    public void Stop()
    {
        Particle par = GetActor();
        par.Stop();
        par.Reset();
    }
    private void RefreshEmitter()//edit emitter in param
    {
        for (String key : mapParam.keySet())
            if (key.contains("emit")) //emit_0
            {
                int index = Integer.parseInt(key.split("_")[1]);
                new IEmitter(index,mapParam.get(key));
            }
    }

    public class IEmitter
    {
        private JsonValue js;
        private ParticleEmitter emit;

        public IEmitter(int index,String data)
        {
            js = Json.DataToJson(data);
            Particle par = GetActor();
            emit = par.GetEmitter(index);
            Util.Try(this::SetSprites);
        }
        private void SetSprites()
        {
            if (!js.hasChild("sprites")) return;
            Array<Sprite> arr = new Array<>();
            for (String name : js.get("sprites").asStringArray())
                arr.add(new Sprite(new TextureRegion(Assets.GetTexture(name))));
            emit.setSprites(arr);
        }
    }
}
