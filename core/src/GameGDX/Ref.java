package GameGDX;

public class Ref<T> {
    private T object;
    public Ref()
    {
        Set(null);
    }
    public Ref(T object)
    {
        Set(object);
    }
    public T Get()
    {
        return object;
    }
    public void Set(T object)
    {
        this.object = object;
    }
}
