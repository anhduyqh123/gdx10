package Extend.PagedScroll;

import GameGDX.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedScroll extends ScrollPane {

    private boolean wasPanDragFling = false;

    private Table content;
    private GDX.Runnable<Actor> onScroll;

    public PagedScroll () {
        super(null);
        setup();
    }

    public void setOnScroll(GDX.Runnable<Actor> onScroll)
    {
        this.onScroll = onScroll;
    }

    private void setup() {
        this.onScroll = null;

        content = new Table();
        super.setWidget(content);
    }

    public void addPages (Actor... pages) {
        for (Actor page : pages) {
            content.add(page).expandY().fillY();
        }
    }

    public Cell addPage (Actor page) {
        return content.add(page).expandY().fillY().width(page.getWidth());
    }

    @Override
    public void addActor(Actor actor) {
        content.add(actor);
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
            scrollToPage();
        } else {
            if (isPanning() || isDragging() || isFlinging()) {
                wasPanDragFling = true;
            }
        }
    }

    @Override
    public void setWidget (Actor widget)
    {
        //
    }

    public void setPageSpacing (float pageSpacing) {
        if (content != null) {
            content.defaults().space(pageSpacing);
            for (@SuppressWarnings("rawtypes") Cell cell : content.getCells()) {
                cell.space(pageSpacing);
            }
            content.invalidate();
        }
    }

    private void scrollToPage () {
        final float width = getWidth();
        final float scrollX = getScrollX() + getWidth() / 2f;

        Array<Actor> pages = content.getChildren();
        if (pages.size > 0) {
            for (Actor a : pages) {
                float pageX = a.getX();
                float pageWidth = a.getWidth();

                if (scrollX >= pageX - pageWidth && scrollX < pageX + pageWidth)
                {
                    setScrollX(pageX - (width - pageWidth) / 2f);
                    if (onScroll != null) onScroll.Run(a);
                    break;
                }
            }
        }
    }

    public void scrollTo(Actor listenerActor)
    {
        float pageX = listenerActor.getX();
        float pageWidth = listenerActor.getWidth();
        final float width = getWidth();

        setScrollX(pageX - (width - pageWidth) / 2f);
        if (onScroll != null) onScroll.Run(listenerActor);
    }

    public void addEmptyPage(float offset)
    {
        content.add().minWidth(offset);
    }

    @Override
    public void clearChildren() {
        content.clearChildren();
    }

    @Override
    public void clear() {
        clearActions();
        //clearListeners();
        clearChildren();
    }

    @Override
    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
        content.setDebug(enabled);
    }
}
