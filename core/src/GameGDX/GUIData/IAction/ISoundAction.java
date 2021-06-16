package GameGDX.GUIData.IAction;

import GameGDX.GMusic;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ISoundAction extends IAction{
    public String soundName = "";
    public ISoundAction()
    {
        name = "sound";
    }
    @Override
    public Action Get() {
        return Actions.run(()-> GMusic.PlaySound(soundName));
    }

    @Override
    public Action Get(IActor iActor) {
        return Get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISoundAction)) return false;
        if (!super.equals(o)) return false;
        ISoundAction that = (ISoundAction) o;
        return soundName.equals(that.soundName);
    }
}
