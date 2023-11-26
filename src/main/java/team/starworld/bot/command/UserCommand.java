package team.starworld.bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.core.entity.fluid.FluidStack;
import team.starworld.shark.core.entity.item.ItemStack;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.network.command.SharkOptionMapping;
import team.starworld.shark.util.ConstantUtil;
import team.starworld.shark.util.PlayWrightUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static j2html.TagCreator.*;
public class UserCommand {

    public static final ResourceLocation PERMISSION_ADMIN = ResourceLocation.of("admin");

    @Command(name = "user", subName = "inventory")
    public static class Inventory {

        @Command.Action
        public static void run (CommandInteractionEvent event) {
            event.getOption("user").ifPresent(i -> event.needPermissions(PERMISSION_ADMIN));
            var user = event.getOption("user").map(SharkOptionMapping::getAsUser).orElse(event.getUser());
            var document = html().attr("min-width", "100").with(
                head().with(
                    link().withRel("stylesheet").withHref(PlayWrightUtil.CDN_CONFIG.getUrlStyleMDB()),
                    link().withRel("stylesheet").withHref(PlayWrightUtil.CDN_CONFIG.getUrlStyleFontAwesome())
                ),
                body().with(
                    div().withClass("container").withStyle("padding-top: calc(var(--mdb-gutter-x) * 0.5) !important").with(
                        div().withClass("progress rounded-pill").withStyle("height: 5rem").attr("role", "progressbar").with(
                            div().withClass("progress-bar bg-success")
                                .with(
                                    span().with(
                                        i().withClass("fas fa-bolt-lightning").withStyle("margin-right: 1em;"),
                                        text(Component.translatable("type.shark.energy.name").getString(user.getLocale()) + " - " + BigInteger.valueOf(user.getInventory().getEnergyStored().getEnergy()))
                                    )
                                )
                                .withStyle("width: 100%; font-size: 1.8rem")
                        )
                    )
                )
            );
            event.replyAsync(
                () -> new MessageEditBuilder().setContent("").setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(document))).build()
            );
        }

        @Command.Setup
        public static void setup (CommandSetupEvent event) {
            event.getCommandData().addOption(OptionType.USER, "user", ConstantUtil.UNDEFINED, false, false);
        }

    }

    @Command(name = "user", subName = "give")
    public static class Give {

        @Command.Action
        public static void run (CommandInteractionEvent event) {
            event.needPermissions(PERMISSION_ADMIN);
            event.needOptions("user", "type", "amount");
            var user = User.of(Objects.requireNonNull(event.getInteraction().getOption("user")).getAsUser());
            var type = ResourceLocation.of(Objects.requireNonNull(event.getInteraction().getOption("type")).getAsString());
            var amount = event.getInteraction().getOption("amount");
            var id = Optional.ofNullable(event.getInteraction().getOption("id"));
            var tag = Optional.ofNullable(event.getInteraction().getOption("tag"));
            assert amount != null;
            if (type.equals(ResourceLocation.of("item")) || type.equals(ResourceLocation.of("fluid")))
                event.needOptions("id");
            if (type.equals(ResourceLocation.of("energy"))) {
                user.getInventory().getEnergyStored().insert(Long.parseLong(amount.getAsString()));
                return;
            }
            if (type.equals(ResourceLocation.of("item")) && id.isPresent()) {
                var compound = new CompoundTag();
                tag.ifPresent(option -> compound.parse(option.getAsString()));
                var stack = new ItemStack(ResourceLocation.of(id.get().getAsString()), Integer.parseInt(amount.getAsString()), compound);
                user.getInventory().give(stack);
            }
            if (type.equals(ResourceLocation.of("fluid")) && id.isPresent()) {
                var compound = new CompoundTag();
                tag.ifPresent(option -> compound.parse(option.getAsString()));
                var stack = new FluidStack(ResourceLocation.of(id.get().getAsString()), Integer.parseInt(amount.getAsString()), compound);
                user.getInventory().give(stack);
            }
            user.save();
        }

        @Command.Setup
        public static void setup (CommandSetupEvent event) {
            event.getCommandData().addOption(OptionType.USER, "user", ConstantUtil.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "type", ConstantUtil.UNDEFINED, true, true);
            event.getCommandData().addOption(OptionType.STRING, "amount", ConstantUtil.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "tag", ConstantUtil.UNDEFINED, false);
            event.getCommandData().addOption(OptionType.STRING, "id", ConstantUtil.UNDEFINED, false);
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
