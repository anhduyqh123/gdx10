package com.game.desktop;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.XmlReader;

public class XmlLevel {

    public XmlLevel()
    {

        FileHandle file = new FileHandle("level_01.xml");
        Read(file);
    }
    private void Read(FileHandle file)
    {
        XmlReader.Element eData = new XmlReader().parse(file.reader());
        JsonValue jsData = new JsonValue(JsonValue.ValueType.object);
        for (int i=0;i<eData.getChildCount();i++)
        {
            XmlReader.Element e = eData.getChild(i);
            if (e.get("x","").equals("")) continue;
            JsonValue js = new JsonValue(JsonValue.ValueType.object);
            js.addChild("x",new JsonValue(e.get("x")));
            js.addChild("y",new JsonValue(e.get("y")));
            jsData.addChild(e.getName(),js);
        }
        System.out.println(jsData);
    }
    private void AddE(XmlReader.Element e,JsonValue js,String name)
    {
        if (e.get(name,"").equals("")) return;
        js.addChild(name,new JsonValue(e.get(name)));
    }
}
