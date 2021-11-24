package com.game.desktop;

import Extend.Box2d.IAction.IForce;
import Extend.Box2d.IAction.IGravity;
import Extend.Box2d.IBody;
import Extend.Box2d.IRayCast;
import Extend.Box2d.IShape;
import Extend.GShape.IMask;
import Extend.GShape.Shape;
import Extend.ILineRenderer;
import GameGDX.Assets;
import GameGDX.GUIData.GUIData;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IChild.ISize;
import GameGDX.GUIData.IGroup;
import GameGDX.GUIData.IImage;
import GameGDX.Util;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.List;

public class XmlLevel {

    private JsonValue jsData;
    public XmlLevel(String name,IGroup iGroup)
    {
        FileHandle file = new FileHandle("levels/level_61.xml");
//        Read(file);
        XmlReader.Element eData = new XmlReader().parse(file.reader());
        //jsData = Read(eData.getChildByName(name));
        jsData = Read2(eData.getChildByName(name));

        //InitFlash(iGroup);
        //InitLaser(iGroup);
        //InitObject(name,iGroup);
        //InitSpeedUp(iGroup);
        //InitGravity(iGroup);

        //object5
//        InitObject2(name,iGroup);
//        InitBody(iGroup);
        InitMask(iGroup);
    }
    private void Read(FileHandle file)
    {
        XmlReader.Element eData = new XmlReader().parse(file.reader());
        Read(eData);
    }
    private JsonValue Read(XmlReader.Element eData)
    {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        for (int i=0;i<eData.getChildCount();i++)
        {
            XmlReader.Element e = eData.getChild(i);
            if (e.get("x","").equals("")) continue;
            JsonValue js = new JsonValue(JsonValue.ValueType.object);
            js.addChild("x",new JsonValue(e.get("x")));
            js.addChild("y",new JsonValue(e.get("y")));
            js.addChild("dx",new JsonValue(e.get("dx","0")));
            js.addChild("dy",new JsonValue(e.get("dy","0")));
            js.addChild("sx",new JsonValue(e.get("sx","1")));
            js.addChild("sy",new JsonValue(e.get("sy","1")));
            js.addChild("rot",new JsonValue(e.get("rot","0")));
            js.addChild("image",new JsonValue(e.get("image","")));
            json.addChild(e.getName(),js);
        }
        return json;
    }
    private JsonValue Read2(XmlReader.Element eData)
    {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        for (int i=0;i<eData.getChildCount();i++)
        {
            XmlReader.Element e = eData.getChild(i);
            json.addChild(e.getName(),Read(e));
        }
        return json;
    }

    private void AddE(XmlReader.Element e,JsonValue js,String name)
    {
        if (e.get(name,"").equals("")) return;
        js.addChild(name,new JsonValue(e.get(name)));
    }

    private void InitFlash(IGroup iGroup)
    {
        for (JsonValue i : jsData)
        {
            if (!i.name.contains("splash")) continue;
            IActor child = iGroup.GetIChild(0).Clone();
            child.iPos.Set(new Vector2(i.getFloat("x")*1.6f,-i.getFloat("y")*1.6f));
            iGroup.AddChildAndConnect(i.name,child);
        }
    }
    private void InitLaser(IGroup iGroup)
    {
        int size = iGroup.GetChildren().size();
        IGroup grLine = iGroup.GetIChild("line");
        for (int i=1;i<size-1;i++)
            AddLaser(iGroup,grLine,i,i+1);
        AddLaser(iGroup,grLine,size-1,1);
    }
    private void AddLaser(IGroup iGroup,IGroup grLine,int i,int j)
    {
        IActor p1 = iGroup.GetIChild(i);
        IActor p2 = iGroup.GetIChild(j);
        ILineRenderer line = new ILineRenderer();
        line.p1 = p1.GetName();
        line.p2 = p2.GetName();
        line.line = grLine.GetIChild(i-1).GetName();
        iGroup.AddComponent("line"+i,line);

        IRayCast iRayCast = new IRayCast();
        iRayCast.p1 = p1.GetName();
        iRayCast.p2 = p2.GetName();
        iRayCast.name = "laser";
        iGroup.AddComponent("raycast"+i,iRayCast);
    }
    private void InitObject(String name,IGroup iGroup)
    {
        for (JsonValue i : jsData)
        {
            if (!i.name.contains(name)) continue;
            if (i.getString("image").equals("")) continue;
            IActor iActor = NewIActor(i);
            InitIActor(iActor,iGroup,i);
        }
    }
    private void InitObject2(String name,IGroup iGroup)
    {
        if (name.contains("thorn")){
            InitThorn(iGroup);
            return;
        }
        for (JsonValue i : jsData)
        {
            if (!i.name.contains(name)) continue;
            if (i.getString("image").equals("")) continue;
            IActor iActor = NewIActor2(i);
            InitIActor(iActor,iGroup,i);
        }
    }
    private void InitThorn(IGroup iGroup)
    {
        for (JsonValue i : jsData)
        {
            if (!i.name.contains("thorn")) continue;
            IActor iActor = GUIData.i.Get("othorn").Clone();
            iActor.prefab = "objects5/othorn";
            SetIActor(iActor,i);
            InitIActor(iActor,iGroup,i);
        }
    }
    private void InitStairs(IGroup iGroup)
    {
        System.out.println(jsData);
        IActor iActor = GUIData.i.Get("oStairs").Clone();
        iActor.prefab = "objects5/oStairs";
        IBody iBody = iActor.GetComponent("body");

        List<Vector2> points = new ArrayList<>();
        for (JsonValue i : jsData)
            points.add(new Vector2(i.getFloat("x")*1.6f,-i.getFloat("y")*1.6f));

        IShape.IEdge shape = (IShape.IEdge)iBody.fixtures.get(0).iShape;
        shape.points = points;

        iGroup.AddChildAndConnect("stairs",iActor);
    }
    private void InitSpeedUp(IGroup iGroup)
    {
        for (JsonValue i : jsData) {
            if (!i.name.contains("speedUp")) continue;
            IActor iActor = NewIActor("ospeedUp",i);
            iActor.prefab = "objects4/ospeedUp";

            float rotate = iActor.iSize.rotate;
            IForce iForce = iActor.acList.GetIMulti("init").FindIAction("force");
            Vector2 dir = new Vector2(8,0).setAngleDeg(rotate);
            iForce.force.set(dir);
            InitIActor(iActor,iGroup,i);
        }
    }
    private void InitGravity(IGroup iGroup)
    {
        for (JsonValue i : jsData) {
            if (!i.name.contains("grav")) continue;
            IActor iActor = NewIActor("ogravity",i);
            iActor.prefab = "objects4/ogravity";

            iActor.iSize.origin = IAlign.bottom;
            float rotate = iActor.iSize.rotate;
            IGravity iGravity = iActor.acList.GetIMulti("init").FindIAction("gravity");
            Vector2 dir = new Vector2(0,10).rotateDeg(rotate);
            Util.Round(dir);
            iGravity.value.set(dir);
            InitIActor(iActor,iGroup,i);
        }
    }
    private void InitBody(IGroup iGroup)
    {
        IActor iActor = GUIData.i.Get("oBody").Clone();
        IBody iBody = iActor.GetComponent("body");
        IMask iMask = iActor.GetComponent("main");

        List<Vector2> points = new ArrayList<>();
        for (JsonValue i : jsData)
            points.add(new Vector2(i.getFloat("x")*1.6f,-i.getFloat("y")*1.6f));
        points.add(new Vector2(points.get(0)));

        IShape.IEdge shape = (IShape.IEdge)iBody.fixtures.get(0).iShape;
        shape.points = points;

        Shape.Polygon polygon = (Shape.Polygon)iMask.shape;
        polygon.points = points;

        iGroup.AddChildAndConnect("body",iActor);
    }
    private void InitMask(IGroup iGroup)
    {

        for (JsonValue i : jsData)
        {
            List<Vector2> points = new ArrayList<>();
            for (JsonValue j : i)
                points.add(new Vector2(j.getFloat("x")*1.6f,-j.getFloat("y")*1.6f));

            IActor iActor = GUIData.i.Get("oBody").Clone();
            IBody iBody = iActor.GetComponent("body");

            IShape.IEdge shape = (IShape.IEdge)iBody.fixtures.get(0).iShape;
            shape.points = points;

            iGroup.AddChildAndConnect(i.name,iActor);
        }
    }

    private void InitIActor(IActor iActor,IGroup iGroup,JsonValue i)
    {
        iGroup.AddChildAndConnect(i.name,iActor);
        ISize iSize = iActor.iSize;
        float dx = -iActor.iSize.originX*iSize.GetWidth();
        float dy = -iActor.iSize.originY*iSize.GetHeight();
        iActor.iPos.delX = dx;
        iActor.iPos.delY = dy;
    }
    private IActor NewIActor(JsonValue js)
    {
        String name = "o"+js.getString("image");
        return NewIActor(name,js);
    }
    private IActor NewIActor(String name,JsonValue js)
    {
        IActor iActor = GUIData.i.Get(name).Clone();
        SetIActor(iActor,js);
        //iActor.iSize.origin = IAlign.center;
        return iActor;
    }
    private void SetIActor(IActor iActor,JsonValue js)
    {
        iActor.iPos.Set(new Vector2(js.getFloat("x")*1.6f,-js.getFloat("y")*1.6f));
        iActor.iSize.rotate = -js.getFloat("rot");
        iActor.iSize.scaleX = js.getFloat("sx");
        iActor.iSize.scaleY = js.getFloat("sy");
        iActor.iSize.originX = js.getFloat("dx");
        iActor.iSize.originY = js.getFloat("dy");
    }

    private IActor NewIActor2(JsonValue js)
    {
        String name = js.getString("image");
        IImage iImage = GUIData.i.Get("odecor").Clone();
        iImage.iTexture.name = name;
        SetIActor(iImage,js);
        if (name.contains("woodShadow")) iImage.iSize.originY = 0;
        if (name.contains("woodShadowD")) iImage.iSize.originX = 0.6f;
        if (name.contains("groundLeft")) iImage.iSize.originX = 1;
        if (name.contains("groundRight")) iImage.iSize.originX = 0;
        //iActor.iSize.origin = IAlign.center;
        return iImage;
    }
}
