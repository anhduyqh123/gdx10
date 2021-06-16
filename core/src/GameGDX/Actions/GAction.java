package GameGDX.Actions;

import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class GAction {
    public static void FollowTarget(Actor actor, Actor targetActor)
    {
        Vector2 pos1 = Scene.GetStagePosition(actor);
        Vector2 pos2 = Scene.GetStagePosition(targetActor);
        Vector2 dir = Util.GetDirect(pos2,pos1);

        Action ac = new Action() {
            @Override
            public boolean act(float delta) {
                Vector2 pos = Scene.GetStagePosition(targetActor).add(dir);
                actor.setPosition(pos.x,pos.y);
                return false;
            }
        };
        actor.addAction(ac);
    }
    //Delay
    public static void Delay(Actor actor, Runnable run,float delay)
    {
        Action a1 = Actions.delay(delay);
        Action a2 = Actions.run(run);
        actor.addAction(Actions.sequence(a1,a2));
    }
    //Loop
    public static void Loop(Actor actor,Runnable run,float duration)
    {
        Action a1 = Actions.run(run);
        Action a2 = Actions.delay(duration);
        Action a12 = Actions.sequence(a1,a2);
        actor.addAction(Actions.forever(a12));
    }

    public static Runnable Fade(Actor actor, float in, float out, float delay)
    {
        return Fade(actor,in,out,delay,()->{});
    }
    public static Runnable Fade(Actor actor,float in,float out,float delay,Runnable done)
    {
        Action ac1 = Actions.fadeIn(in);
        Action ac2 = Actions.delay(delay);
        Action ac3 = Actions.fadeOut(out);
        Action ac4 = Actions.run(done);
        return ()->actor.addAction(Actions.sequence(ac1,ac2,ac3,ac4));
    }

    public static Runnable Recolor(Actor actor,float duration,Runnable onStart,Runnable onFinish)
    {
        Color cl = new Color(actor.getColor());
        return ()->{
            onStart.run();
            Action ac1 = Actions.color(cl,duration);
            Action ac2 = Actions.run(onFinish);
            actor.addAction(Actions.sequence(ac1,ac2));
        };
    }
    public static Runnable Recolor(Actor actor,float duration,Runnable onStart)
    {
        return Recolor(actor,duration,onStart,()->{});
    }
    public static Runnable Recolor(Actor actor,float duration)
    {
        return Recolor(actor,duration,()->{});
    }

    public static Runnable Relocated(Actor actor, float duration, Interpolation interpolation, Runnable onStart, Runnable onFinish)
    {
        float y = actor.getY();
        float x = actor.getX();
        return ()->{
            onStart.run();
            Action ac1 = Actions.moveTo(x,y,duration,interpolation);
            Action ac2 = Actions.run(onFinish);
            actor.addAction(Actions.sequence(ac1,ac2));
        };
    }
    public static Runnable Relocated(Actor actor, float duration, Interpolation interpolation, Runnable onStart)
    {
        return Relocated(actor,duration,interpolation,onStart,()->{});
    }
    public static Runnable Relocated(Actor actor, float duration, Interpolation interpolation)
    {
        return Relocated(actor,duration,interpolation,()->{});
    }
    public static Runnable Rescaled(Actor actor, float duration, Interpolation interpolation)
    {
        return Rescaled(actor, duration, interpolation, null, null);
    }
    public static Runnable Rescaled(Actor actor, float duration, Interpolation interpolation, Runnable onStart)
    {
        return Rescaled(actor, duration, interpolation, onStart, null);
    }
    public static Runnable Rescaled(Actor actor, float duration, Interpolation interpolation, Runnable onStart,Runnable onDone)
    {
        float scaleX = actor.getScaleX();
        float scaleY = actor.getScaleY();
        return ()->{
            if (onStart!=null) onStart.run();
            Action ac1 = Actions.scaleTo(scaleX,scaleY,duration,interpolation);
            Action ac2 = Actions.run(()->{
                if (onDone!=null) onDone.run();
            });
            actor.addAction(Actions.sequence(ac1,ac2));
        };
    }

    public static void DoScale(Actor actor,float from,float to,float duration,Interpolation interpolation)
    {
        Action a1 = Actions.scaleTo(to,to,duration,interpolation);
        Action a2 = Actions.scaleTo(from,from,duration,interpolation);
        Action a12 = Actions.sequence(a1,a2);
        actor.addAction(Actions.forever(a12));
    }

    //Suggest
    public static Action Scale(float from,float to,float duration)
    {
        Action a1 = Actions.scaleTo(to,to,duration,Interpolation.fade);
        Action a2 = Actions.scaleTo(from,from,duration,Interpolation.fade);
        return Actions.sequence(a1,a2);
    }
    public static Action Scale(int num,float from,float to,float duration)
    {
        Action a1 = Scale(from, to, duration);
        return Actions.repeat(num,a1);
    }

    public static Action Shake(int num,float rotate,float duration)
    {
        Action a1 = Actions.rotateTo(-rotate,duration/2);
        Action a2 = Actions.rotateTo(rotate,duration);
        Action a3 = Actions.rotateTo(-rotate,duration);
        Action a23 = Actions.repeat(num,Actions.sequence(a2,a3));
        Action a4 = Actions.rotateTo(0,duration);
        return Actions.sequence(a1,a23,a4);
    }
    public static void Shake(Actor actor)
    {
        actor.setOrigin(Align.center);
        Action a1 = Shake(4,30,0.2f);
        Action a2 = Actions.delay(1f);
        Action a3 = Actions.forever(Actions.sequence(a1,a2));
        actor.addAction(a3);
    }
    public static void DoFade(Actor actor, float duration)
    {
        Action a1 = Actions.fadeOut(duration);
        Action a2 = Actions.fadeIn(duration);
        Action a12 = Actions.sequence(a1,a2);
        actor.addAction(Actions.forever(a12));
    }
    public static void DoFade(Actor actor,float from,float to,float duration)
    {
        Color cl1 = new Color(actor.getColor());
        Color cl2 = new Color(actor.getColor());
        cl1.a = from;
        cl2.a = to;

        actor.setColor(cl1);
        Action a1 = Actions.color(cl2,duration);
        Action a2 = Actions.color(cl1,duration);
        Action a12 = Actions.sequence(a1,a2);
        actor.addAction(Actions.forever(a12));
    }
}
