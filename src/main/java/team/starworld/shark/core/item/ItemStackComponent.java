package team.starworld.shark.core.item;

import j2html.tags.DomContent;
import lombok.Getter;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.network.chat.Component;

public class ItemStackComponent extends Component {

    @Getter
    private final ItemStack stack;

    @Getter
    private final Component baseComponent;

    public ItemStackComponent (ItemStack stack, Component baseComponent) {
        this.stack = stack;
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

}
