package Editor.LanguageTool;

import Editor.JFameUI;
import Editor.WrapLayout;
import GameGDX.Language;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;
import java.util.*;

public class MainForm {
    public JPanel panel1;
    private JList keyList;
    private JButton btNew;
    private JButton btDelete;
    private JButton btSave;
    private JButton btTranslate;
    private JList codeList;
    private JButton btNew2;
    private JComboBox cbCode;
    private JButton btDelete2;
    private JPanel pnNode;
    private JButton btGlyphs;

    private Language language;
    private JFameUI ui = new JFameUI();
    public MainForm(Language language,Runnable onSave)
    {
        pnNode.setLayout(new WrapLayout());

        this.language = language;
        InitCode(language.GetCodes());
        Init();
        Click(btSave,()->{
            onSave.run();
            ui.NewDialog("Save Success!",panel1);
        });
    }

    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->run.run());
    }
    private void Init()
    {
        Click(btNew,this::New);
        Click(btDelete,this::Delete);
        keyList.addListSelectionListener(e->{
            String key = (String) keyList.getSelectedValue();
            if (key==null) return;
            InitNode(key);
        });
        RefreshKeyList0();
    }
    private void New()
    {
        String key = GetKeyName();
        language.AddKey(key);
        RefreshKeyList(key);
    }
    private void Delete()
    {
        String key = (String) keyList.getSelectedValue();
        language.RemoveKey(key);
        RefreshKeyList0();
    }
    private void RefreshKeyList0()
    {
        String[] arr = language.GetKeys().toArray(new String[0]);
        if (arr.length<=0) return;
        RefreshKeyList(arr[0]);
    }
    private void RefreshKeyList()
    {
        String key = (String) keyList.getSelectedValue();
        if (key==null) return;
        RefreshKeyList(key);
    }
    private void RefreshKeyList(String key)
    {
        List<String> list = new ArrayList<>(language.GetKeys());
        Collections.sort(list);
        ui.ListSetModel(keyList,new ArrayList<>(list));
        keyList.setSelectedValue(key,true);
    }
    private String GetKeyName()
    {
        String key = "newKey";
        int i=1;
        while (true)
        {
            if (!language.ContainsKey(key)) return key;
            key = "newKey"+i++;
        }
    }
    private void InitCode(List<String> codes)
    {
        JLocale jLocale = new JLocale();
        ui.ComboBox(cbCode, jLocale.GetDisplayCodes());
        ui.ListSetModel(codeList, Arrays.asList(jLocale.GetDisplayCodes(codes)));
        Click(btNew2,()->{
            String display = (String) cbCode.getSelectedItem();
            String code = jLocale.GetCode(display);
            language.AddCode(code);
            ui.ListSetModel(codeList, Arrays.asList(jLocale.GetDisplayCodes(codes)));
            codeList.setSelectedIndex(codes.size()-1);
            RefreshKeyList();
        });
        Click(btDelete2,()->{
            String display = (String) codeList.getSelectedValue();
            String code = jLocale.GetCode(display);
            language.RemoveCode(code);
            ui.ListSetModel(codeList, Arrays.asList(jLocale.GetDisplayCodes(codes)));
            if (codes.size()>0) codeList.setSelectedIndex(0);
            RefreshKeyList();
        });
        Click(btGlyphs,()->ui.Try(this::Glyphs,panel1));
    }
    private LineForm NewLine(String name)
    {
        LineForm line = new LineForm(name);
        pnNode.add(line.panel1);
        return line;
    }
    private void InitNode(String key)
    {
        pnNode.removeAll();
        Language.Node node = language.GetNode(key);
        String mainCode = language.GetCodes().get(0);

        LineForm keyLine = NewLine("Key");
        keyLine.SetButton("Translate",()->{
            for(String code : language.GetCodes())
            {
                if (code.equals(mainCode)) continue;
                if (!node.GetContent(code).equals("")) continue;
                String st = Translate(mainCode,code,node.GetContent(mainCode));
                node.Put(code,st);
            }
            ui.NewDialog("Translate Success!",pnNode);
            InitNode(key);
        });
        keyLine.SetKey(key,vl->{
            language.RenameKey(key,vl);
            RefreshKeyList(vl);
        });
        for(String code : language.GetCodes())
        {
            LineForm line = NewLine(code);
            line.SetButton("Refresh",()->{
                if (code.equals(mainCode)) return;
                String st = Translate(mainCode,code,node.GetContent(mainCode));
                line.SetText(st);
                node.Put(code,st);
            });
            line.SetText(node.GetContent(code),vl->node.Put(code,vl));
        }
        pnNode.repaint();
    }
    private String Translate(String fromCode,String toCode,String text)
    {
        try {
            return Translator.Translate(fromCode,toCode,text);
        }catch (Exception e){e.printStackTrace();}
        return "fail";
    }
    private void Glyphs()
    {
        List<String> codes = new ArrayList<>();
        for (Object st : codeList.getSelectedValuesList())
        {
            String locale = (String) st;
            codes.add(locale.split("-")[0]);
        }
        HashSet<Character> hashSet = new HashSet<>();
        language.Foreach(n->{
            for (String code : codes)
            {
                String st = n.GetContent(code);
                for (char c : st.toCharArray())
                    hashSet.add(c);
            }
        });
        String s0 = "";
        for (Character c : hashSet)
            s0+=c;
        new GlyphsForm(s0);
    }
}
