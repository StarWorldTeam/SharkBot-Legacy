package team.starworld.shark.core.data.material;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import team.starworld.shark.core.registries.Registrable;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;

import java.util.Arrays;

@Accessors(chain = true)
public class Element implements Registrable {

    // 质子量
    @Getter @Setter
    private long protons;

    // 中子量
    @Getter @Setter
    private long neutrons;

    // 半衰期（秒）
    @Getter @Setter
    private long halfLife;

    // 此元素衰变到的元素，用“&”字符分隔
    @Getter @Setter
    private String decayTo;

    public Element[] getDecayToElements () {
        return Arrays.stream(decayTo.split("&")).map(ResourceLocation::of).map(SharkRegistries.ELEMENTS::get).toList().toArray(new Element[] {});
    }

    // 符号
    @Getter @Setter
    private String symbol;

    // 同位素
    @Getter @Setter
    private boolean isotope;

    public ResourceLocation getLocation () {
        return SharkRegistries.ELEMENTS.getKey(this);
    }

    @Override
    public String toString () {
        return "Element [%s] (%s)".formatted(getSymbol(), getLocation());
    }

}