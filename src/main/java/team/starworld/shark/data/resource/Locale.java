package team.starworld.shark.data.resource;

import j2html.tags.DomContent;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import team.starworld.shark.SharkBotApplication;

import java.util.*;

public class Locale {
    @Getter
    private final Map <String, String> language = new HashMap <> ();

    public static Locale getByName (String name) {
        return SharkBotApplication.RESOURCE_LOADER.locales.getOrDefault(name, getDefault());
    }

    public static Locale getDefault () {
        return SharkBotApplication.RESOURCE_LOADER.locales.get(SharkBotApplication.CONFIG.getDefaultLanguage());
    }

    public String getLocalizedName () {
        return get("locale.name");
    }

    public String getName () {
        return SharkBotApplication.RESOURCE_LOADER.locales
            .entrySet()
            .stream()
            .filter(i -> i.getValue() == this)
            .map(Map.Entry::getKey)
            .toList().get(0);
    }

    public static Locale fromDiscord (DiscordLocale locale) {
        for (var lang : SharkBotApplication.RESOURCE_LOADER.locales.values()) {
            if (Objects.equals(lang.getOrDefault("locale.discord.locale_tag", "unknown"), locale.getLocale()))
                return lang;
        }
        return getDefault();
    }

    public DiscordLocale toDiscord () {
        return DiscordLocale.from(this.get("locale.discord.locale_tag"));
    }

    public void put (String key, String value) {
        language.put(key, value);
    }

    public String get (String key) {
        return language.getOrDefault(key, getDefault().getOrDefault(key, key));
    }

    public String getOrDefault (String key, String defaultValue) {
        return language.getOrDefault(key, defaultValue);
    }

    public String format (String key, Object... parameters) {
        return get(key).formatted(
            Arrays.stream(parameters).map(
                i -> i instanceof DomContent content ? content.render() : i.toString()
            ).toArray()
        );
    }

}
