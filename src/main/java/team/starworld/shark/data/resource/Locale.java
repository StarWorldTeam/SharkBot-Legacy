package team.starworld.shark.data.resource;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import team.starworld.shark.SharkBotApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Locale {

    @Getter
    private final Map <String, String> language = new HashMap <> ();

    public static Locale getByName (String name) {
        return SharkBotApplication.RESOURCE_LOADER.locales.getOrDefault(name, getDefault());
    }

    public static Locale getDefault () {
        return SharkBotApplication.RESOURCE_LOADER.locales.get(SharkBotApplication.CONFIG.getDefaultLanguage());
    }

    public static Locale fromDiscord (DiscordLocale locale) {
        for (var lang : SharkBotApplication.RESOURCE_LOADER.locales.values()) {
            if (Objects.equals(lang.get("locale.discord.locale_tag"), locale.getLocale()))
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
        return language.getOrDefault(key, key);
    }

    public String getOrDefault (String key, String defaultValue) {
        return language.getOrDefault(key, defaultValue);
    }

    public String format (String key, Object... arguments) {
        return language.get(key).formatted(arguments);
    }

}
