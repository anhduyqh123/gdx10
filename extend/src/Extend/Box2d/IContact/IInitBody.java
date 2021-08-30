package Extend.Box2d.IContact;

import Extend.Box2d.IBody;

public class IInitBody extends IContact {
    @Override
    public void AfterRefresh() {
        AddListener(new IBody.IBodyListener(){
            @Override
            public void InitBody() {
                Run();
            }
        });
    }
}
