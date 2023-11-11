package team.starworld.shark.core.entity.item;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.data.material.Material;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.Registrable;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.network.chat.Component;

import java.util.List;

public class Item implements ItemSupplier, Registrable {

    @Override
    public Item asItem () {
        return this;
    }

    public static class Properties {

        @Getter @Setter
        @Accessors(fluent = true, chain = true)
        private Material material;

    }

    private Properties properties;

    public Properties getProperties () {
        if (properties == null) properties = new Properties();
        return properties;
    }

    public Component getName (ItemStack stack, @Nullable User user) {
        return Component.translatable(this.getTranslationId("name"));
    }

    public void appendToolTip (ItemStack stack, List <Component> tooltip, @Nullable User user) {}


    public String getTranslationId (String attribute) {
        return SharkRegistries.ITEMS.getKey(this).toLanguageKey("item", attribute);
    }


}
