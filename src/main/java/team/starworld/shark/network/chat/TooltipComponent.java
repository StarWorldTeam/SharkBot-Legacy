package team.starworld.shark.network.chat;

import j2html.tags.DomContent;
import lombok.Getter;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.data.resource.Locale;

public abstract class TooltipComponent extends Component {

    @Getter
    private final Component baseComponent;

    public TooltipComponent (Component baseComponent) {
        this.baseComponent = baseComponent;
    }

    @Override
    public String getString () {
        return baseComponent.getString();
    }

    @Override
    public DomContent getDomContent () {
        return baseComponent.getDomContent();
    }

    @Override
    public String getString (Locale locale) {
        return baseComponent.getString(locale);
    }

    @Override
    public DomContent getDomContent (Locale locale) {
        return baseComponent.getDomContent(locale);
    }

    @Override
    public String toString () {
        return baseComponent.toString();
    }

    public abstract Component getTitle ();
    public abstract Component getDescription ();

    public Component getTitle (User user) { return getTitle(); }
    public Component getDescription (User user) { return getDescription(); }

}
