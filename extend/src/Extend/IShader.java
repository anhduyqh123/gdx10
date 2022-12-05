package Extend;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IShader extends Component {
    public String name = "";
    private GDX.Func<ShaderProgram> getShader;
    @Override
    public void Refresh() {
        GDX.PostRunnable(this::Init);
    }
    private void Init()
    {
        try {
            ShaderProgram.pedantic = false;
            Batch batch = GetActor().getStage().getBatch();
            String fragment = GDX.GetString(Assets.GetNode(name).url);
//            String vertex = GDX.GetStringFromName("vertex");
//            ShaderProgram shader = new ShaderProgram(vertex,fragment);
            ShaderProgram shader = new ShaderProgram(batch.getShader().getVertexShaderSource(),fragment);
            getShader = ()->shader;
        }catch (Exception e){}
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw) {
        if (getShader==null){
            onDraw.run();
            return;
        }
        ShaderProgram shader = getShader.Run();
        batch.setShader(shader);
        shader.bind();
        UpdateValue();

        onDraw.run();
        batch.setShader(null);
    }
    private void UpdateValue()
    {
        IActor iActor = GetIActor();
        ShaderProgram shader = getShader.Run();
        for (String n : iActor.GetParamMap().keySet())
        {
            if (n.startsWith("u_"))
                shader.setUniformf(n,iActor.GetParam(n,0f));
            if (n.startsWith("v2_"))
                shader.setUniformf(n,iActor.GetParam(n,new Vector2()));
            if (n.startsWith("v3_"))
                shader.setUniformf(n,iActor.GetParam(n,new Vector3()));
            if (n.startsWith("cl_"))
                shader.setUniformf(n,iActor.GetParam(n, Color.WHITE));
        }

//        shader.setUniformf("u_imageSize",113,113);
//        shader.setUniformf("u_borderColor",1,0,0,1);
//        shader.setUniformf("u_borderSize",5);
    }
}
