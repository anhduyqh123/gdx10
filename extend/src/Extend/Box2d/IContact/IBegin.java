package Extend.Box2d.IContact;

import Extend.Box2d.IBody;

public class IBegin extends IContact{
    public String category = "";
    @Override
    public void AfterRefresh() {
        if (category.equals("")) return;
        AddListener(new IBody.IBodyListener(){
            @Override
            public void BeginContact() {
                if (iBodyB.category.equals(category))
                    Run();
            }
        });
    }
}
