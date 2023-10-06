package team.starworld.shark.core.network;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data @Getter @Setter
public class UserMeta {

    private String locale;
    private Map <String, Object> tag = Map.of();

}
