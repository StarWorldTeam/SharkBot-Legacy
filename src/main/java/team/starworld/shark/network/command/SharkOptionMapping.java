package team.starworld.shark.network.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.Registrable;
import team.starworld.shark.core.registries.Registry;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.data.serialization.CompoundTag;

import java.util.Objects;

public class SharkOptionMapping {

    @Getter
    private OptionMapping source;

    public SharkOptionMapping (OptionMapping source) {
        this.source = source;
    }

    public String getName () {
        return getSource().getName();
    }

    public long getAsLong () {
        return getSource().getAsLong();
    }

    public double getAsDouble () {
        return getSource().getAsLong();
    }

    public long getAsInteger () {
        return getSource().getAsInt();
    }

    public float getAsFloat () {
        return (float) getSource().getAsDouble();
    }

    public String getAsString () {
        return getSource().getAsString();
    }

    public User getAsUser () {
        try {
            return User.of(getSource().getAsUser());
        } catch (Throwable ignored) {}
        try {
            return User.of(Objects.requireNonNull(getSource().getAsMember()).getUser());
        } catch (Throwable ignored) {}
        try {
            return User.of(getSource().getAsString());
        } catch (Throwable ignored) {}
        throw new IllegalStateException("Cannot resolve User for option " + getName());
    }

    public CompoundTag getAsCompoundTag () {
        return new CompoundTag().parse(getAsString());
    }

    public <T extends Registrable> T getAsRegistryValue (Registry <T> registry) {
        return registry.get(getAsResourceLocation());
    }

    public ResourceLocation getAsResourceLocation () {
        return ResourceLocation.of(getAsString());
    }

}
