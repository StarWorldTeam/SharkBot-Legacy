package team.starworld.shark.core.network;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Data @Getter @Setter
public class UserMeta {

    private String locale;
    private LinkedHashMap <String, Object> tag = new LinkedHashMap <> ();

}
