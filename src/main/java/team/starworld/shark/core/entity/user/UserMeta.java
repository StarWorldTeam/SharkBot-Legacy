package team.starworld.shark.core.entity.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.starworld.shark.data.resource.Locale;

import java.util.LinkedHashMap;

@Data @Getter @Setter
public class UserMeta {

    private String locale = Locale.getDefault().getName();
    private LinkedHashMap <String, Object> tag = new LinkedHashMap <> ();

}
