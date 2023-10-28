package team.starworld.shark.network.chat;

import j2html.TagCreator;
import j2html.tags.DomContent;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import team.starworld.shark.data.resource.Locale;

public abstract class Component {


    @NotNull
    public static Component literal (String literalString) {
        return new Component () {
            @Override
            public String getString () {
                var content = getDomContent().render();
                var doc = Jsoup.parse(content);
                return doc.text();
            }

            @Override
            public DomContent getDomContent () {
                return TagCreator.rawHtml(literalString);
            }

        };
    }

    @NotNull
    public static Component newLine () {
        return new Component () {
            @Override
            public String getString () {
                return "\n";
            }

            @Override
            public DomContent getDomContent () {
                return TagCreator.br();
            }

        };
    }

    @NotNull
    public static Component translatable (String key, Object... parameters) {
        return new Component () {

            @NotNull
            @Override
            public String getString () { return getString(Locale.getDefault()); }

            @NotNull
            @Override
            public String getString (Locale locale) {
                return Component.literal(locale.format(key, parameters)).getString();
            }

            @Override
            public DomContent getDomContent () {
                return getDomContent(Locale.getDefault());
            }

            @Override
            public DomContent getDomContent (Locale locale) {
                return Component.literal(locale.format(key, parameters)).getDomContent();
            }

        };
    }


    public abstract String getString ();

    public String getString (Locale locale) { return getString(); }

    public abstract DomContent getDomContent ();

    public DomContent getDomContent (Locale locale) { return getDomContent(); }

    @Override
    public String toString () {
        return getString();
    }

}
