package Editor.ParticleViewer;

import Editor.JFameUI;
import Editor.Util.UIList;
import GameGDX.Actors.Particle;
import GameGDX.GDX;
import GameGDX.Scene;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ViewerForm {
    public JPanel panel1;
    private JList list1;
    private JButton deleteButton;
    private JPanel pnInfo;
    private JCheckBox cbLoop;
    private JTextArea textInfo;
    private JButton btExport;
    private JButton btClear;

    private JFameUI ui = new JFameUI();
    private ParModel parModel;
    private List<ParModel> parModels = new ArrayList<>();
    private List<String> totalImg = new ArrayList<>();
    private FileHandle direction = GDX.GetFile("particles");

    public ViewerForm()
    {
        try {
            for (FileHandle f : direction.list())
            {
                if (f.extension().equals("p")) parModels.add(new ParModel(f));
                if (f.extension().equals("png")) totalImg.add(f.name());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        UIList<ParModel> uiList = new UIList();
        uiList.onSelect  = this::OnSelect;
        uiList.onDelete = ParModel::Delete;
        uiList.getName = p->p.name;
        uiList.Init(list1, parModels);

        ui.Button(deleteButton,uiList::Delete);
        ui.Button(btExport,this::Export);
        ui.Button(btClear,this::ClearTrash);
    }
    private void Export()
    {
        try {
            parModel.Export(GDX.GetFile("export"));
            ui.NewDialog("success!",panel1);
        }catch (Exception e){
            ui.NewDialog(e.getLocalizedMessage(),panel1);
        }
    }
    private void OnSelect(ParModel model)
    {
        this.parModel = model;
        textInfo.setText(model.ToString());
        SetView(model);
    }
    private void SetView(ParModel model)
    {
        Scene.ui.setSize(Scene.width,Scene.height);
        Scene.ui.clear();
        Vector2 pos = new Vector2(Scene.width/2,Scene.height/2);
        Particle par = new Particle();
        par.SetParticleEffect(model.pe);
        par.SetPosition(pos);
        par.Start();
        Scene.ui.addActor(par);

        if (cbLoop.isSelected())
            par.onCompeted = par::Start;

        Scene.ui.addListener(new ClickListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                par.setPosition(x,y);
            }
        });
    }
    private void ClearTrash()
    {
        HashSet<String> set = new HashSet<>();
        for (ParModel p : parModels) set.addAll(p.imagePath);
        List<String> trash = new ArrayList<>();
        for (String s : totalImg)
            if (!set.contains(s)){
                trash.add(s);
                new FileHandle(direction.path()+"/"+s).delete();
            }
        totalImg.removeAll(trash);
        ui.NewDialog("Deleted "+trash.size()+" files",panel1);
    }
}
