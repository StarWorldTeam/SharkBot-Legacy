package team.starworld.shark.core.entity.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.starworld.shark.data.resource.Locale;

import java.util.HashMap;

@Data @Getter @Setter
public class UserMeta {

    private String locale = Locale.getDefault().getName();
    private HashMap <String, Object> tag = new HashMap <> ();

}
