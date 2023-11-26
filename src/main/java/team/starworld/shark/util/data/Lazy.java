package team.starworld.shark.util.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface Lazy <T> {

    static <T> Lazy <T> of (Supplier <T> supplier) {
       return new Lazy <> () {

           @Override
           public void set (@Nullable T value) {}

           @Override
           public T get () {
               return supplier.get();
           }

       };
    }

    static <T> Lazy <T> ofFinal () {
        return new Lazy <> () {

            private T value = null;

            @Override
            @Nullable
            public T get () {
                return this.value;
            }

            @Override
            public void set (T value) {
                if (value == this.value) return;
                if (this.value == null) this.value = value;
                else throw new RuntimeException("Final value cannot be set multiple times!");
            }

        };
    }

    void set (@Nullable T value);

    T get ();

}
