package com.lothrazar.library.module;

import java.util.Collection;
import java.util.Random;
import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.library.util.EntityUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandModule {

  public static final Random RAND = new Random();
  //  private static final String FORK_RESET = "reset";
  //  private static final String FORK_FACTOR = "factor";
  //  private static final String FORK_ADD = "add";
  //  private static final String FORK_TP = "teleport"; //so far only home
  //  private static final String FORK_SAVE = "save"; //new SAVE: like set but its save-current. ie players current position
  //  private static final String FORK_SET = "set";
  //  private static final String FORK_TOGGLE = "toggle";
  //  private static final String FORK_RANDOM = "random";
  //  private static final String ARG_OBJECTIVE = "objective";
  //  private static final String ARG_TARGETS = "targets";
  //  private static final String ARG_ATTR = "attribute";
  //  private static final String ARG_MIN = "min";
  //  private static final String ARG_MAX = "max";
  //  private static final String ARG_VALUE = "value";
  //  private static final String ARG_PLAYER = "player";
  private static final int PERM_EVERYONE = 0; // no restrictions
  private static final int PERM_ELEVATED = 2; // player with perms/creative OR function OR command block

  public enum SubCommands {

    TPX, DEV;
    //step height
    //does the file format thing, same as DatFile

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public static BooleanValue COMMANDDEV;
  public static BooleanValue COMMANDGETHOME;
  public static BooleanValue COMMANDHEALTH;
  public static BooleanValue COMMANDHOME;
  public static BooleanValue COMMANDHUNGER;
  public static BooleanValue COMMANDPING;

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    if (!ConfigModule.ENABLE_COMMANDS.get()) {
      FutureLibMod.LOGGER.info("Disabling command /flib");
      return;
    }
    CommandDispatcher<CommandSourceStack> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSourceStack> literal(FutureLibMod.MODID)
        //                /flib tpx minecraft:the_end 0 99 0 @p
        .then(Commands.literal(SubCommands.TPX.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument("dim", DimensionArgument.dimension())
                .then(Commands.argument("x", IntegerArgumentType.integer())
                    .then(Commands.argument("y", IntegerArgumentType.integer())
                        .then(Commands.argument("z", IntegerArgumentType.integer())
                            .then(Commands.argument("p", EntityArgument.players())
                                .executes(x -> {
                                  FutureLibMod.LOGGER.info("flib test " + x.getSource());
                                  CommandModule.tpx(x, DimensionArgument.getDimension(x, "dim"),
                                      new BlockPos(IntegerArgumentType.getInteger(x, "x"), IntegerArgumentType.getInteger(x, "y"), IntegerArgumentType.getInteger(x, "z")), EntityArgument.getPlayers(x, "p"));
                                  return 0; // CommandHome.executeTp(x, EntityArgument.getPlayers(x, ARG_PLAYER));
                                })))))))
    //new commands here
    );
  }

  private static void tpx(CommandContext<CommandSourceStack> x, ServerLevel dimension, BlockPos pos, Collection<ServerPlayer> players) {
    if (dimension == null || pos == null || players == null) {
      return;
    }
    for (ServerPlayer p : players) {
      EntityUtil.dimensionTeleport(p, dimension, new BlockPosDim(pos, dimension));
    }
  }

  private static int returnIntRng(int min, int max) {
    return RAND.nextInt(min, max);
  }
}
