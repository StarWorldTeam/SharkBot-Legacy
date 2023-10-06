package team.starworld.shark.core.network;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.Interaction;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.util.DataUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {

    public static final Map <String, User> USERS = new HashMap <> ();

    @Getter
    private final String id;

    private User (String id) {
        this.id = id;
        this.data = DataUtil.useData("users/" + id, UserMeta.class, UserMeta::new, DataUtil.FileType.YAML);
        USERS.put(id, this);
    }

    public static User of (String id) {
        if (USERS.containsKey(id)) return USERS.get(id);
        return new User(id);
    }

    public static User of (net.dv8tion.jda.api.entities.User user) {
        return of(user.getId());
    }

    public static User of (Interaction interaction) {
        return of(interaction.getUser());
    }

    @Getter
    private DataUtil.DataHolder <UserMeta> data;

    public Locale getLocale () {
        return SharkBotApplication.RESOURCE_LOADER.locales.get(this.getData().get().getLocale());
    }

    public Locale getLocale (Locale defaultValue) {
        var data = this.getData().get();
        if (data.getLocale() == null) data.setLocale(defaultValue.getName());
        var locale = data.getLocale();
        this.getData().set(data);
        return SharkBotApplication.RESOURCE_LOADER.locales.get(locale);
    }

    public Locale getLocale (DiscordLocale defaultValue) {
        var data = this.getData().get();
        if (data.getLocale() == null) data.setLocale(Locale.fromDiscord(defaultValue).getName());
        var locale = data.getLocale();
        this.getData().set(data);
        return SharkBotApplication.RESOURCE_LOADER.locales.get(locale);
    }

    public User setLocale (DiscordLocale locale) {
        var data = this.getData().get();
        data.setLocale(Locale.fromDiscord(locale).getName());
        this.getData().set(data);
        return this;
    }

    public User setLocale (Locale locale) {
        var data = this.getData().get();
        data.setLocale(locale.getName());
        this.getData().set(data);
        return this;
    }

    public Map <String, Object> getTag () { return this.getData().get().getTag(); }
    public User setTag (Map <String, Object> tag) {
        var data = this.getData().get();
        data.setTag(tag);
        this.getData().set(data);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTag (String name) { return (T) getTag().get(name); }

    @SuppressWarnings("unchecked")
    public <T> T getTag (String name, T defaultValue) {
        var value = getTag().get(name);
        if (value == null) {
            setTag(name, defaultValue); return defaultValue;
        }
        if (defaultValue.getClass().isInstance(value)) {
            return (T) value;
        } else {
            setTag(name, defaultValue);
            return defaultValue;
        }
    }

    public <K, V> LinkedHashMap <K, V> getTagAsMap (String name) { return getTag(name); }
    public <T> User setTagAsMap (String name, Map <String, T> map) { return setTag(name, map); }

    public User setTag (String name, Object object) {
        var tag = new HashMap <> (this.getTag());
        tag.put(name, object);
        setTag(tag);
        return this;
    }

    public boolean hasTag (String name) { return getTag().containsKey(name); }


}
