package team.starworld.shark.core.registries;

public interface RegistryEntry <T extends Registrable> {

    ResourceLocation getLocation ();
    T get ();

}
