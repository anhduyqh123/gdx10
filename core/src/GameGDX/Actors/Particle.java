package GameGDX.Actors;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class Particle extends Actor {
    public ParticleEffect pe,pe0;
    public Runnable onRemove,onCompeted;
    private boolean autoRemove, isRunning;

    public Particle(){}
    public Particle(String name){
        SetParticleEffect(Assets.GetParticleEffect(name));
    }
    public Particle(String name,float x,float y){
        this(name);
        setPosition(x,y);
    }
    public Particle(String name,float x,float y,Group parent){
        this(name, x, y);
        parent.addActor(this);
    }
    public Particle(String name, Vector2 pos, Group parent){
        this(name, pos.x,pos.y,parent);
    }
    public void SetParticleEffect(ParticleEffect pe)
    {
        this.pe0 = pe;
        Reset();
    }
    public void Reset()
    {
        this.pe = new ParticleEffect(pe0);
        isRunning = false;
    }
    public void Start()
    {
        Start(false);
    }
    public void Start(boolean destroy) {
        if (pe==null) return;
        pe.reset();
        pe.scaleEffect(getScaleX());
        this.autoRemove = destroy;
        isRunning = true;
    }
    private void RefreshPosition()
    {
        Vector2 pos = Scene.GetPosition(this);
        pe.setPosition(pos.x+getOriginX(),pos.y+getOriginY());
    }

    @Override
    public boolean remove() {
        isRunning = false;
        if (onRemove !=null) onRemove.run();
        if (pe!=null) pe.dispose();
        return super.remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (pe==null || !isRunning) return;
        RefreshPosition();
        pe.update(delta);
        if (pe.isComplete())
        {
            if (onCompeted!=null) onCompeted.run();
            if (autoRemove) remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (pe==null) return;
        pe.draw(batch);
    }
    public void Stop()
    {
        pe.setDuration(0);
    }

    public void SetPosition(Vector2 pos)
    {
        setPosition(pos.x,pos.y);
    }

    //extend
    public void SetSize(int index, float minX,float maxX,float minY,float maxY)
    {
        ParticleEmitter emit = GetEmitter(index);
        emit.getXScale().setHigh(maxX);
        emit.getXScale().setLow(minX);
        emit.getYScale().setHigh(maxY);
        emit.getYScale().setLow(minY);
    }
    public void SetSprite(int index, TextureRegion tr)
    {
        Array<Sprite> arr = new Array<>();
        arr.add(new Sprite(new TextureRegion(tr)));
        GetEmitter(index).setSprites(arr);
    }
    public void SetSprite(int index, String txtName)
    {
        SetSprite(index,Assets.GetTexture(txtName));
    }
    public ParticleEmitter GetEmitter(int index)
    {
        return pe.getEmitters().get(index);
    }
    public ParticleEmitter GetEmitter(String name)
    {
        return pe.findEmitter(name);
    }
    public void SetSprite(String name, TextureRegion tr)
    {
        Array<Sprite> arr = new Array<>();
        arr.add(new Sprite(new TextureRegion(tr)));
        GetEmitter(name).setSprites(arr);
    }

    public void ForEmitter(GDX.Runnable<ParticleEmitter> cb)
    {
        for(ParticleEmitter e : pe.getEmitters())
            cb.Run(e);
    }

}