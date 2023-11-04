package team.starworld.bot.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.core.entity.fluid.FluidStack;
import team.starworld.shark.core.entity.item.ItemStack;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.util.Constants;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class User {

    public static final ResourceLocation PERMISSION_ADMIN = ResourceLocation.of("admin");

    @Command(name = "user-give")
    public static class Give {

        @Command.Action
        @SneakyThrows
        public static void run (CommandInteractionEvent event) {
            event.needPermissions(PERMISSION_ADMIN);
            event.needOptions("user", "type", "count");
            var user = team.starworld.shark.core.entity.user.User.of(Objects.requireNonNull(event.getInteraction().getOption("user")).getAsUser());
            var type = ResourceLocation.of(Objects.requireNonNull(event.getInteraction().getOption("type")).getAsString());
            var count = event.getInteraction().getOption("count");
            var id = Optional.ofNullable(event.getInteraction().getOption("id"));
            var tag = Optional.ofNullable(event.getInteraction().getOption("tag"));
            assert count != null;
            if (type == ResourceLocation.of("item") || type == ResourceLocation.of("fluid"))
                event.needOptions("id");
            if (type.equals(ResourceLocation.of("energy"))) {
                user.getInventory().getEnergyStored().insert(Long.parseLong(count.getAsString()));
                return;
            }
            if (type.equals(ResourceLocation.of("item")) && id.isPresent()) {
                var compound = new CompoundTag();
                if (tag.isPresent()) compound.load(new ObjectMapper().readValue(tag.get().getAsString(), new TypeReference <LinkedHashMap <String, ?>>() {}));
                var stack = new ItemStack(ResourceLocation.of(id.get().getAsString()), Integer.parseInt(count.getAsString()), compound);
                user.getInventory().give(stack);
            }
            if (type.equals(ResourceLocation.of("fluid")) && id.isPresent()) {
                var compound = new CompoundTag();
                if (tag.isPresent()) compound.load(new ObjectMapper().readValue(tag.get().getAsString(), new TypeReference <LinkedHashMap <String, ?>>() {}));
                var stack = new FluidStack(ResourceLocation.of(id.get().getAsString()), Integer.parseInt(count.getAsString()), compound);
                user.getInventory().give(stack);
            }
            user.save();
        }

        @Command.Setup
        public static void setup (CommandSetupEvent event) {
            event.getCommandData().addOption(OptionType.USER, "user", Constants.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "type", Constants.UNDEFINED, true, true);
            event.getCommandData().addOption(OptionType.STRING, "count", Constants.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "tag", Constants.UNDEFINED, false);
            event.getCommandData().addOption(OptionType.STRING, "id", Constants.UNDEFINED, false);
            event.autoComplete(
                autoCompleteEvent -> {
                    var locale = Locale.fromDiscord(autoCompleteEvent.getEvent().getUserLocale());
                    var locations = List.of(ResourceLocation.of("energy"), SharkRegistries.Keys.ITEM.getLocation(), SharkRegistries.Keys.FLUID.getLocation());
                    locations.forEach(i -> autoCompleteEvent.add(locale.get(i.toLanguageKey("type", "name")), i.toString()));
                },
                "type"
            );
        }

    }

}
