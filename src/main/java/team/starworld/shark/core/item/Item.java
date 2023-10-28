package team.starworld.shark.core.item;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.network.chat.Component;

import java.util.List;

public class Item implements ItemSupplier {

    @Override
    public Item asItem () {
        return this;
    }

    public static class Properties {}

    @Getter
    private final Properties properties;

    public Item (Properties properties) {
        this.properties = properties;
    }

    public Component getName (ItemStack stack, @Nullable User user) {
        return Component.translatable(this.getTranslationId("name"));
    }
    public void appendToolTip (ItemStack stack, List <Component> tooltip, @Nullable User user) {}


    public String getTranslationId (String attribute) {
        return SharkRegistries.ITEMS.getKey(this).toLanguageKey("item", attribute);
    }


}
