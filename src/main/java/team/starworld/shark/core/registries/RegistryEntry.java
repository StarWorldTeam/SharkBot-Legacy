package team.starworld.shark.core.registries;

public interface RegistryEntry <T> {

    ResourceLocation getLocation ();
    T get ();

}
