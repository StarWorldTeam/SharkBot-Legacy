package team.starworld.shark.network.chat;

import j2html.TagCreator;
import j2html.tags.DomContent;
import j2html.tags.Renderable;
import lombok.Getter;
import team.starworld.shark.data.resource.Locale;

import java.util.Arrays;

public class MultiComponent extends Component {

    @Getter
    private final Component[] components;

    public MultiComponent (Component... components) {
        this.components = components;
    }

    @Override
    public String getString () {
        return String.join("", Arrays.stream(components).map(Component::toString).toList());
    }

    @Override
    public DomContent getDomContent () {
        return TagCreator.rawHtml(
            String.join("", Arrays.stream(components).map(Component::getDomContent).map(Renderable::render).toList())
        );
    }

    @Override
    public String getString (Locale locale) {
        return String.join("", Arrays.stream(components).map(i -> i.getString(locale)).toList());
    }

    @Override
    public DomContent getDomContent (Locale locale) {
        return TagCreator.rawHtml(
            String.join("", Arrays.stream(components).map(i -> i.getDomContent(locale)).map(Renderable::render).toList())
        );
    }

}
