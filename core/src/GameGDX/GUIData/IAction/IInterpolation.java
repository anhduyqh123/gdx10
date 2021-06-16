package GameGDX.GUIData.IAction;

import com.badlogic.gdx.math.Interpolation;

public enum IInterpolation {
    linear(Interpolation.linear),
    fade(Interpolation.fade),
    smooth(Interpolation.smooth),
    swing(Interpolation.swing),
    swingIn(Interpolation.swingIn),
    swingOut(Interpolation.swingOut),
    bounce(Interpolation.bounce),
    bounceIn(Interpolation.bounceIn),
    bounceOut(Interpolation.bounceOut),
    circle(Interpolation.circle),
    circleIn(Interpolation.circleIn),
    circleOut(Interpolation.circleOut),
    fastSlow(Interpolation.fastSlow),
    sine(Interpolation.sine),
    sineIn(Interpolation.sineIn),
    sineOut(Interpolation.sineOut);

    public Interpolation value;
    IInterpolation(Interpolation value)
    {
        this.value = value;
    }
}
