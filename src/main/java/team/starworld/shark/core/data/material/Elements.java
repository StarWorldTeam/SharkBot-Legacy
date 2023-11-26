package team.starworld.shark.core.data.material;

import team.starworld.shark.core.registries.RegistryEntry;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.util.NumberUtil;

public class Elements {

    public static final RegistryEntry <Element> HYDROGEN = register("hydrogen", 1, 0, -1, null, "H", false);
    public static final RegistryEntry <Element> DEUTERIUM = register("deuterium", 1, 1, -1, "hydrogen", "D", true);
    public static final RegistryEntry <Element> TRITIUM = register("tritium", 1, 2, -1, "deuterium", "T", true);
    public static final RegistryEntry <Element> HELIUM = register("helium", 2, 2, -1, null, "He", false);
    public static final RegistryEntry <Element> HELIUM_3 = register("helium_3", 2, 1, -1, "hydrogen&deuterium", NumberUtil.numberToSuper(3) + "He", true);
    public static final RegistryEntry <Element> LITHIUM = register("lithium", 3, 4, -1, null, "Li", false);
    public static final RegistryEntry <Element> BERYLLIUM = register("beryllium", 4, 5, -1, null, "Be", false);
    public static final RegistryEntry <Element> BORON = register("boron", 5, 5, -1, null, "B", false);
    public static final RegistryEntry <Element> CARBON = register("carbon", 6, 6, -1, null, "C", false);
    public static final RegistryEntry <Element> NITROGEN = register("nitrogen", 7, 7, -1, null, "N", false);
    public static final RegistryEntry <Element> OXYGEN = register("oxygen", 8, 8, -1, null, "O", false);
    public static final RegistryEntry <Element> FLUORINE = register("fluorine", 9, 9, -1, null, "F", false);
    public static final RegistryEntry <Element> NEON = register("neon", 10, 10, -1, null, "Ne", false);
    public static final RegistryEntry <Element> SODIUM = register("sodium", 11, 11, -1, null, "Na", false);
    public static final RegistryEntry <Element> MAGNESIUM = register("magnesium", 12, 12, -1, null, "Mg", false);
    public static final RegistryEntry <Element> ALUMINIUM = register("aluminium", 13, 13, -1, null, "Al", false);
    public static final RegistryEntry <Element> SILICON = register("silicon", 14, 14, -1, null, "Si", false);
    public static final RegistryEntry <Element> PHOSPHORUS = register("phosphorus", 15, 15, -1, null, "P", false);
    public static final RegistryEntry <Element> SULFUR = register("sulfur", 16, 16, -1, null, "S", false);
    public static final RegistryEntry <Element> CHLORINE = register("chlorine", 17, 18, -1, null, "Cl", false);
    public static final RegistryEntry <Element> ARGON = register("argon", 18, 22, -1, null, "Ar", false);
    public static final RegistryEntry <Element> POTASSIUM = register("potassium", 19, 20, -1, null, "K", false);
    public static final RegistryEntry <Element> CALCIUM = register("calcium", 20, 20, -1, null, "Ca", false);
    public static final RegistryEntry <Element> SCANDIUM = register("scandium", 21, 24, -1, null, "Sc", false);
    public static final RegistryEntry <Element> TITANIUM = register("titanium", 22, 26, -1, null, "Ti", false);
    public static final RegistryEntry <Element> VANADIUM = register("vanadium", 23, 28, -1, null, "V", false);
    public static final RegistryEntry <Element> CHROME = register("chrome", 24, 28, -1, null, "Cr", false);
    public static final RegistryEntry <Element> MANGANESE = register("manganese", 25, 30, -1, null, "Mn", false);
    public static final RegistryEntry <Element> IRON = register("iron", 26, 30, -1, null, "Fe", false);
    public static final RegistryEntry <Element> COBALT = register("cobalt", 27, 32, -1, null, "Co", false);
    public static final RegistryEntry <Element> NICKEL = register("nickel", 28, 30, -1, null, "Ni", false);
    public static final RegistryEntry <Element> COPPER = register("copper", 29, 34, -1, null, "Cu", false);
    public static final RegistryEntry <Element> ZINC = register("zinc", 30, 35, -1, null, "Zn", false);
    public static final RegistryEntry <Element> GALLIUM = register("gallium", 31, 39, -1, null, "Ga", false);
    public static final RegistryEntry <Element> GERMANIUM = register("germanium", 32, 40, -1, null, "Ge", false);
    public static final RegistryEntry <Element> ARSENIC = register("arsenic", 33, 42, -1, null, "As", false);
    public static final RegistryEntry <Element> SELENIUM = register("selenium", 34, 45, -1, null, "Se", false);
    public static final RegistryEntry <Element> BROMINE = register("bromine", 35, 45, -1, null, "Br", false);
    public static final RegistryEntry <Element> KRYPTON = register("krypton", 36, 48, -1, null, "Kr", false);
    public static final RegistryEntry <Element> RUBIDIUM = register("rubidium", 37, 48, -1, null, "Rb", false);
    public static final RegistryEntry <Element> STRONTIUM = register("strontium", 38, 49, -1, null, "Sr", false);
    public static final RegistryEntry <Element> YTTRIUM = register("yttrium", 39, 50, -1, null, "Y", false);
    public static final RegistryEntry <Element> ZIRCONIUM = register("zirconium", 40, 51, -1, null, "Zr", false);
    public static final RegistryEntry <Element> NIOBIUM = register("niobium", 41, 53, -1, null, "Nb", false);
    public static final RegistryEntry <Element> MOLYBDENUM = register("molybdenum", 42, 53, -1, null, "Mo", false);
    public static final RegistryEntry <Element> TECHNETIUM = register("technetium", 43, 55, -1, null, "Tc", false);
    public static final RegistryEntry <Element> RUTHENIUM = register("ruthenium", 44, 57, -1, null, "Ru", false);
    public static final RegistryEntry <Element> RHODIUM = register("rhodium", 45, 58, -1, null, "Rh", false);
    public static final RegistryEntry <Element> PALLADIUM = register("palladium", 46, 60, -1, null, "Pd", false);
    public static final RegistryEntry <Element> SILVER = register("silver", 47, 60, -1, null, "Ag", false);
    public static final RegistryEntry <Element> CADMIUM = register("cadmium", 48, 64, -1, null, "Cd", false);
    public static final RegistryEntry <Element> INDIUM = register("indium", 49, 65, -1, null, "In", false);
    public static final RegistryEntry <Element> TIN = register("tin", 50, 68, -1, null, "Sn", false);
    public static final RegistryEntry <Element> ANTIMONY = register("antimony", 51, 70, -1, null, "Sb", false);
    public static final RegistryEntry <Element> TELLURIUM = register("tellurium", 52, 75, -1, null, "Te", false);
    public static final RegistryEntry <Element> IODINE = register("iodine", 53, 74, -1, null, "I", false);
    public static final RegistryEntry <Element> XENON = register("xenon", 54, 77, -1, null, "Xe", false);
    public static final RegistryEntry <Element> CAESIUM = register("caesium", 55, 77, -1, null, "Cs", false);
    public static final RegistryEntry <Element> BARIUM = register("barium", 56, 81, -1, null, "Ba", false);
    public static final RegistryEntry <Element> LANTHANUM = register("lanthanum", 57, 81, -1, null, "La", false);
    public static final RegistryEntry <Element> CERIUM = register("cerium", 58, 82, -1, null, "Ce", false);
    public static final RegistryEntry <Element> PRASEODYMIUM = register("praseodymium", 59, 81, -1, null, "Pr", false);
    public static final RegistryEntry <Element> NEODYMIUM = register("neodymium", 60, 84, -1, null, "Nd", false);
    public static final RegistryEntry <Element> PROMETHIUM = register("promethium", 61, 83, -1, null, "Pm", false);
    public static final RegistryEntry <Element> SAMARIUM = register("samarium", 62, 88, -1, null, "Sm", false);
    public static final RegistryEntry <Element> EUROPIUM = register("europium", 63, 88, -1, null, "Eu", false);
    public static final RegistryEntry <Element> GADOLINIUM = register("gadolinium", 64, 93, -1, null, "Gd", false);
    public static final RegistryEntry <Element> TERBIUM = register("terbium", 65, 93, -1, null, "Tb", false);
    public static final RegistryEntry <Element> DYSPROSIUM = register("dysprosium", 66, 96, -1, null, "Dy", false);
    public static final RegistryEntry <Element> HOLMIUM = register("holmium", 67, 97, -1, null, "Ho", false);
    public static final RegistryEntry <Element> ERBIUM = register("erbium", 68, 99, -1, null, "Er", false);
    public static final RegistryEntry <Element> THULIUM = register("thulium", 69, 99, -1, null, "Tm", false);
    public static final RegistryEntry <Element> YTTERBIUM = register("ytterbium", 70, 103, -1, null, "Yb", false);
    public static final RegistryEntry <Element> LUTETIUM = register("lutetium", 71, 103, -1, null, "Lu", false);
    public static final RegistryEntry <Element> HAFNIUM = register("hafnium", 72, 106, -1, null, "Hf", false);
    public static final RegistryEntry <Element> TANTALUM = register("tantalum", 73, 107, -1, null, "Ta", false);
    public static final RegistryEntry <Element> TUNGSTEN = register("tungsten", 74, 109, -1, null, "W", false);
    public static final RegistryEntry <Element> RHENIUM = register("rhenium", 75, 111, -1, null, "Re", false);
    public static final RegistryEntry <Element> OSMIUM = register("osmium", 76, 114, -1, null, "Os", false);
    public static final RegistryEntry <Element> IRIDIUM = register("iridium", 77, 115, -1, null, "Ir", false);
    public static final RegistryEntry <Element> PLATINUM = register("platinum", 78, 117, -1, null, "Pt", false);
    public static final RegistryEntry <Element> GOLD = register("gold", 79, 117, -1, null, "Au", false);
    public static final RegistryEntry <Element> MERCURY = register("mercury", 80, 120, -1, null, "Hg", false);
    public static final RegistryEntry <Element> THALLIUM = register("thallium", 81, 123, -1, null, "Tl", false);
    public static final RegistryEntry <Element> LEAD = register("lead", 82, 125, -1, null, "Pb", false);
    public static final RegistryEntry <Element> BISMUTH = register("bismuth", 83, 125, -1, null, "Bi", false);
    public static final RegistryEntry <Element> POLONIUM = register("polonium", 84, 124, -1, null, "Po", false);
    public static final RegistryEntry <Element> ASTATINE = register("astatine", 85, 124, -1, null, "At", false);
    public static final RegistryEntry <Element> RADON = register("radon", 86, 134, -1, null, "Rn", false);
    public static final RegistryEntry <Element> FRANCIUM = register("francium", 87, 134, -1, null, "Fr", false);
    public static final RegistryEntry <Element> RADIUM = register("radium", 88, 136, -1, null, "Ra", false);
    public static final RegistryEntry <Element> ACTINIUM = register("actinium", 89, 136, -1, null, "Ac", false);
    public static final RegistryEntry <Element> THORIUM = register("thorium", 90, 140, -1, null, "Th", false);
    public static final RegistryEntry <Element> PROTACTINIUM = register("protactinium", 91, 138, -1, null, "Pa", false);
    public static final RegistryEntry <Element> URANIUM = register("uranium", 92, 146, -1, null, "U", false);
    public static final RegistryEntry <Element> URANIUM_238 = register("uranium_238", 92, 146, -1, null, NumberUtil.numberToSuper(238) + "U", false);
    public static final RegistryEntry <Element> URANIUM_235 = register("uranium_235", 92, 143, -1, null, NumberUtil.numberToSuper(235) + "U", true);
    public static final RegistryEntry <Element> NEPTUNIUM = register("neptunium", 93, 144, -1, null, "Np", false);
    public static final RegistryEntry <Element> PLUTONIUM = register("plutonium", 94, 152, -1, null, "Pu", false);
    public static final RegistryEntry <Element> PLUTONIUM_239 = register("plutonium_239", 94, 145, -1, null, NumberUtil.numberToSuper(239) + "Pu", false);
    public static final RegistryEntry <Element> PLUTONIUM_241 = register("plutonium_241", 94, 149, -1, null, NumberUtil.numberToSuper(241) + "Pu", true);
    public static final RegistryEntry <Element> AMERICIUM = register("americium", 95, 150, -1, null, "Am", false);
    public static final RegistryEntry <Element> CURIUM = register("curium", 96, 153, -1, null, "Cm", false);
    public static final RegistryEntry <Element> BERKELIUM = register("berkelium", 97, 152, -1, null, "Bk", false);
    public static final RegistryEntry <Element> CALIFORNIUM = register("californium", 98, 153, -1, null, "Cf", false);
    public static final RegistryEntry <Element> EINSTEINIUM = register("einsteinium", 99, 153, -1, null, "Es", false);
    public static final RegistryEntry <Element> FERMIUM = register("fermium", 100, 157, -1, null, "Fm", false);
    public static final RegistryEntry <Element> MENDELEVIUM = register("mendelevium", 101, 157, -1, null, "Md", false);
    public static final RegistryEntry <Element> NOBELIUM = register("nobelium", 102, 157, -1, null, "No", false);
    public static final RegistryEntry <Element> LAWRENCIUM = register("lawrencium", 103, 159, -1, null, "Lr", false);
    public static final RegistryEntry <Element> RUTHERFORDIUM = register("rutherfordium", 104, 161, -1, null, "Rf", false);
    public static final RegistryEntry <Element> DUBNIUM = register("dubnium", 105, 163, -1, null, "Db", false);
    public static final RegistryEntry <Element> SEABORGIUM = register("seaborgium", 106, 165, -1, null, "Sg", false);
    public static final RegistryEntry <Element> BOHRIUM = register("bohrium", 107, 163, -1, null, "Bh", false);
    public static final RegistryEntry <Element> HASSIUM = register("hassium", 108, 169, -1, null, "Hs", false);
    public static final RegistryEntry <Element> MEITNERIUM = register("meitnerium", 109, 167, -1, null, "Mt", false);
    public static final RegistryEntry <Element> DARMSTADTIUM = register("darmstadtium", 110, 171, -1, null, "Ds", false);
    public static final RegistryEntry <Element> ROENTGENIUM = register("roentgenium", 111, 169, -1, null, "Rg", false);
    public static final RegistryEntry <Element> COPERNICIUM = register("copernicium", 112, 173, -1, null, "Cn", false);
    public static final RegistryEntry <Element> NIHONIUM = register("nihonium", 113, 171, -1, null, "Nh", false);
    public static final RegistryEntry <Element> FLEROVIUM = register("flerovium", 114, 175, -1, null, "Fl", false);
    public static final RegistryEntry <Element> MOSCOVIUM = register("moscovium", 115, 173, -1, null, "Mc", false);
    public static final RegistryEntry <Element> LIVERMORIUM = register("livermorium", 116, 177, -1, null, "Lv", false);
    public static final RegistryEntry <Element> TENNESSINE = register("tennessine", 117, 177, -1, null, "Ts", false);
    public static final RegistryEntry <Element> OGANESSON = register("oganesson", 118, 176, -1, null, "Og", false);

    @SuppressWarnings("SameParameterValue")
    private static RegistryEntry <Element> register (String name, long protons, long neutrons, long halfLife, String decayTo, String symbol, boolean isotope) {
        var element = new Element()
            .setProtons(protons)
            .setNeutrons(neutrons)
            .setHalfLife(halfLife)
            .setDecayTo(decayTo)
            .setSymbol(symbol)
            .setIsotope(isotope);
        return SharkRegistries.ELEMENTS.register(ResourceLocation.of(name), () -> element);
    }

    public static void bootstrap () {}

}
