package GameGDX.GUIData;

import GameGDX.Assets;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Actors.Particle;
import GameGDX.Reflect;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IParticle extends IActor {

    public boolean start = true;
    public String parName = "";
    public List<IEmitter> iEmitters = new ArrayList<>();

    @Override
    protected Actor NewActor() {
        return new Particle(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
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
    public void RefreshEmitter()
    {
        try {
            for (IEmitter i : iEmitters) SetIEmitter(i);
        }catch (Exception e){e.printStackTrace();}
    }
    private void SetIEmitter(IEmitter e)
    {
        Particle par = GetActor();
        ParticleEmitter emit = par.GetEmitter(e.index);
        if (e.IsSprite()) par.SetSprite(e.index,Assets.GetTexture(e.sprite));
        e.SetData(emit);
    }

    public static class IEmitter
    {
        public int index;
        public String sprite = "";
        public Value size,offset;

        public boolean IsSprite()
        {
            return !sprite.equals("");
        }
        public void SetData(ParticleEmitter e)
        {
            if (size!=null) size.SetScale(e.getXScale(),e.getYScale());
            if (offset!=null) offset.SetRanged(e.getXOffsetValue(),e.getYOffsetValue());
        }
        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
    public static class Value
    {
        public int minX,maxX,minY,maxY;
        private boolean IsX()
        {
            return minX!=0 && maxX!=0;
        }
        private boolean IsY()
        {
            return minY!=0 && maxY!=0;
        }
        public void SetRanged(ParticleEmitter.RangedNumericValue vlX,ParticleEmitter.RangedNumericValue vlY)
        {
            if (IsX()) vlX.setLow(minX,maxY);
            if (IsY()) vlY.setLow(minY,maxY);
        }
        public void SetScale(ParticleEmitter.ScaledNumericValue vlX,ParticleEmitter.ScaledNumericValue vlY)
        {
            if (IsX()){
                vlX.setLow(minX);
                vlX.setHigh(maxX);
            }
            if (IsY()){
                vlY.setLow(minY);
                vlY.setHigh(maxY);
            }
        }

        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
}
