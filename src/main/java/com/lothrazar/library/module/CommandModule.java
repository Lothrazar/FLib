package com.lothrazar.library.module;

import java.util.Collection;
import java.util.Random;
import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.api.FlibCoreFeatures;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.library.util.AttributesUtil;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.EntityUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandModule {

  public static final Random RAND = new Random();
  private static final String FORK_RESET = "reset";
  private static final String FORK_FACTOR = "factor";
  private static final String FORK_ADD = "add";
  private static final String FORK_SET = "set";
  private static final String FORK_RANDOM = "random";
  private static final String ARG_OBJECTIVE = "objective";
  private static final String ARG_TARGETS = "targets";
  private static final String ARG_ATTR = "attribute";
  private static final String ARG_MIN = "min";
  private static final String ARG_MAX = "max";
  private static final String ARG_VALUE = "value";
  private static final String ARG_PLAYER = "player";
  public static final int PERM_EVERYONE = 0; // no restrictions
  public static final int PERM_ELEVATED = 2; // player with perms/creative OR function OR command block

  public enum SubCommands {

    TPX, HEALTH, HUNGER, HEARTS, GAMEMODE, SCOREBOARD, ATTRIBUTE, OVERRIDE;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    if (!FlibCoreFeatures.INSTANCE.get(FlibCoreFeatures.COMMANDS).get()) {
      FutureLibMod.LOGGER.info("Disabling command /flib from feature instance");
      return;
    }
    CommandDispatcher<CommandSourceStack> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSourceStack> literal(FutureLibMod.MODID)
        // cyclic gamemode @p 1
        .then(Commands.literal(SubCommands.OVERRIDE.toString())
            .requires((p) -> {
              return p.hasPermission(1);
            })
            .then(Commands.argument("corefeature", StringArgumentType.word())
                .then(Commands.argument("value", BoolArgumentType.bool())
                    .executes(x -> {
                      FutureLibMod.LOGGER.error("/flib override test command in use, mod incompatibilities may occur");
                      FutureLibMod.LOGGER.error("corefeature list: " + FlibCoreFeatures.values());
                      return FlibCoreFeatures.executeCommand(x, StringArgumentType.getString(x, "corefeature"), BoolArgumentType.getBool(x, "value"));
                    }))))
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
        // flib health add @p -1
        // flib health random @p -2 2
        // flib health factor @p 0.8
        .then(Commands.literal(SubCommands.HEALTH.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_SET)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, FloatArgumentType.floatArg(0, 100F))
                        .executes(x -> {
                          return CommandHealth.executeSet(x, EntityArgument.getPlayers(x, ARG_PLAYER), FloatArgumentType.getFloat(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                        .executes(x -> {
                          return CommandHealth.executeAdd(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_FACTOR)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
                        .executes(x -> {
                          return CommandHealth.executeFactor(x, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-20, 20))
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-20, 20))
                            .executes(x -> {
                              return CommandHealth.addRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                            }))))))
        //   /flib hunger add @p -4
        //   /flib hunger set @p -4
        //   /flib hunger random @p -4 9
        //   /flib hunger factor @p 0.5
        .then(Commands.literal(SubCommands.HUNGER.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_FACTOR)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
                        .executes(x -> {
                          return CommandHunger.executeFactor(x, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-20, 20))
                        .executes(x -> {
                          return CommandHunger.executeAdd(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_SET)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                        .executes(x -> {
                          return CommandHunger.executeSet(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-20, 20))
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-20, 20))
                            .executes(x -> {
                              return CommandHunger.executeRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                            }))))))
        // flib hearts add @p -1
        // flib hearts random @p -2 2
        // flib hearts factor @p 0.8
        .then(Commands.literal(SubCommands.HEARTS.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_SET)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                        .executes(x -> {
                          return AttributesUtil.setHearts(EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                        .executes(x -> {
                          return AttributesUtil.add(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_FACTOR)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
                        .executes(x -> {
                          return AttributesUtil.multiply(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-100, 100))
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-100, 100))
                            .executes(x -> {
                              return AttributesUtil.addRandom(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                            }))))))
        //flib scoreboard random @p 4 9 <objective>
        //flib scoreboard add @p 5 <objective>
        //flib scoreboard test @p <objective>
        .then(Commands.literal(SubCommands.SCOREBOARD.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer())
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer())
                            .then(Commands.argument(ARG_OBJECTIVE, StringArgumentType.greedyString())
                                .executes(x -> {
                                  return CommandScoreboard.scoreboardRng(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                                      ObjectiveArgument.getObjective(x, ARG_OBJECTIVE),
                                      IntegerArgumentType.getInteger(x, ARG_MIN),
                                      IntegerArgumentType.getInteger(x, ARG_MAX));
                                }))))))
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                        .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
                            .executes(x -> {
                              return CommandScoreboard.scoreboardAdd(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                                  ObjectiveArgument.getObjective(x, ARG_OBJECTIVE), IntegerArgumentType.getInteger(x, ARG_VALUE));
                            })))))
            .then(Commands.literal("test")
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
                        .executes(x -> {
                          return CommandScoreboard.scoreboardRngTest(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                              ObjectiveArgument.getObjective(x, ARG_OBJECTIVE));
                        })))))
        // cyclic gamemode @p 1
        .then(Commands.literal(SubCommands.GAMEMODE.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 3))
                    .executes(x -> {
                      return executeGamemode(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        // /cyclic attributes minecraft:reach_distance add @p 3
        // /cyclic attributes minecraft:reach_distance random @p 3 8
        // /cyclic attributes minecraft:reach_distance reset @p
        .then(Commands.literal(SubCommands.ATTRIBUTE.toString()) //same as hearts but subcommand again instead of just number
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument(ARG_ATTR, ResourceKeyArgument.key(Registry.ATTRIBUTE_REGISTRY))
                .then(Commands.literal(FORK_ADD)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-10000, 10000))
                            .executes(x -> {
                              return AttributesUtil.add(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                            }))))
                .then(Commands.literal(FORK_RANDOM)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-10000, 10000))
                            .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-10000, 10000))
                                .executes(x -> {
                                  return AttributesUtil.addRandom(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                                })))))
                .then(Commands.literal(FORK_FACTOR)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 100))
                            .executes(x -> {
                              return AttributesUtil.multiply(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                            }))))
                .then(Commands.literal(FORK_RESET)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .executes(x -> {
                          return AttributesUtil.reset(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER));
                        })))))
    //new commands here
    );
  }

  public static int executeGamemode(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer p : players) {
      switch (integer) {
        case 0:
          p.setGameMode(GameType.SURVIVAL);
        break;
        case 1:
          p.setGameMode(GameType.CREATIVE);
        break;
        case 2:
          p.setGameMode(GameType.ADVENTURE);
        break;
        case 3:
          p.setGameMode(GameType.SPECTATOR);
        break;
        default:
          ChatUtil.sendFeedback(x, integer + " = ?!");
        break;
      }
    }
    return 0;
  }

  private static void tpx(CommandContext<CommandSourceStack> x, ServerLevel dimension, BlockPos pos, Collection<ServerPlayer> players) {
    if (dimension == null || pos == null || players == null) {
      return;
    }
    for (ServerPlayer p : players) {
      EntityUtil.dimensionTeleport(p, dimension, new BlockPosDim(pos, dimension));
    }
  }

  public static class CommandHealth {

    private static void set(float newlevel, ServerPlayer player) {
      player.setHealth(newlevel);
    }

    private static float get(ServerPlayer player) {
      return player.getHealth();
    }

    public static int executeSet(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float newlevel) {
      for (ServerPlayer player : players) {
        set(newlevel, player);
      }
      return 0;
    }

    public static int executeAdd(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int more) {
      for (ServerPlayer player : players) {
        set(get(player) + more, player);
      }
      return 0;
    }

    public static int executeFactor(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, double factor) {
      for (ServerPlayer player : players) {
        set(get(player) * (float) factor, player);
      }
      return 0;
    }

    public static int addRandom(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int min, int max) {
      for (ServerPlayer player : players) {
        set(get(player) + RAND.nextInt(min, max), player);
      }
      return 0;
    }
  }

  public static class CommandScoreboard {

    public static int scoreboardRngTest(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective) {
      Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
      int i = 0;
      for (String s : scoreHolderTargets) {
        Score score = scoreboard.getOrCreatePlayerScore(s, objective);
        //        ModCyclic.LOGGER.error("[test cmd]" + score.getScore());
        i += score.getScore();
      }
      return i;
    }

    public static int scoreboardAdd(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, int integer) {
      Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
      int i = 0;
      for (String s : scoreHolderTargets) {
        Score score = scoreboard.getOrCreatePlayerScore(s, objective);
        score.add(integer);
        //        ModCyclic.LOGGER.info("objective add " + score.getScore());
        i += score.getScore();
      }
      return i;
    }

    public static int scoreboardRng(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, int min, int max) {
      Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
      int i = 0;
      for (String s : scoreHolderTargets) {
        Score score = scoreboard.getOrCreatePlayerScore(s, objective);
        if (min < max) {
          score.setScore(RAND.nextInt(min, max));
        }
        else {
          //either equal, or max is lower than min
          score.setScore(min);
        }
        //        ModCyclic.LOGGER.info("objective rng " + score.getScore());
        i += score.getScore();
      }
      return i;
    }
  }

  public static class CommandHunger {

    private static void set(int newlevel, ServerPlayer player) {
      player.getFoodData().setFoodLevel(newlevel);
    }

    private static int get(ServerPlayer player) {
      return player.getFoodData().getFoodLevel();
    }

    public static int executeSet(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int newlevel) {
      for (ServerPlayer player : players) {
        set(newlevel, player);
      }
      return 0;
    }

    public static int executeRandom(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int min, int max) {
      for (ServerPlayer player : players) {
        set(RAND.nextInt(min, max), player);
      }
      return 0;
    }

    public static int executeFactor(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, double fact) {
      for (ServerPlayer player : players) {
        set((int) (get(player) * fact), player);
      }
      return 0;
    }

    public static int executeAdd(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int more) {
      for (ServerPlayer player : players) {
        set(get(player) + more, player);
      }
      return 0;
    }
  }
}
