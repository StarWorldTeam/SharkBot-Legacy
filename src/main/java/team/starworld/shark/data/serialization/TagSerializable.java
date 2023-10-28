package team.starworld.shark.data.serialization;

public interface TagSerializable <T extends TagSerializable <T>> {

    // Return "this"
    T load (CompoundTag tag);

    CompoundTag save ();


}
