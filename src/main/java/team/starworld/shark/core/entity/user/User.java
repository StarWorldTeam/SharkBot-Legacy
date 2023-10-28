package team.starworld.shark.core.entity.user;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.Interaction;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.util.DataUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class User {

    public static final Map <String, User> USERS = new HashMap <> ();

    @Getter
    private final CompoundTag tag = new CompoundTag();

    @Getter
    private final String id;

    @Getter private final Inventory inventory = new Inventory();

    private User (String id) {
        this.id = id;
        USERS.put(id, this);
        this.data = DataUtil.useData("users/" + id, UserMeta.class, UserMeta::new, DataUtil.FileType.YAML);
        load();
    }

    public User load () {
        this.tag.load(data.get().getTag());
        this.inventory.load(getTag().putIfNull("inventory", CompoundTag::new).getCompound("inventory"));
        return this;
    }

    public User modifyTag (Consumer <CompoundTag> consumer) {
        consumer.accept(getTag());
        return save();
    }

    public User modifyMeta (Consumer <UserMeta> consumer) {
        var data = getData().get();
        consumer.accept(data);
        this.data.set(data);
        return this;
    }

    public User save () {
        var data = this.data.get();
        tag.putCompound("inventory", inventory.save());
        data.setTag(getTag().saveAsMap());
        this.data.set(data);
        return this;
    }

    public static User of (String id) {
        if (USERS.containsKey(id)) return USERS.get(id).save();
        return new User(id).save();
    }

    public net.dv8tion.jda.api.entities.User toDiscord () {
        return SharkBotApplication.SHARK_CLIENT.getClient().getUserById(this.id);
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


}
