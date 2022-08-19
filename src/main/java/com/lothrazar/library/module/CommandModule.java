package com.lothrazar.library.module;

import java.util.Random;
import com.lothrazar.library.FutureLibMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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

  public enum CyclicCommands {

    TELEPORT, DEV;
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
    CommandDispatcher<CommandSourceStack> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSourceStack> literal(FutureLibMod.MODID)
        // cyclic home teleport @p
        // cyclic home reset @p
        // cyclic home save @p
        // cyclic home set @p x y z
        .then(Commands.literal(CyclicCommands.DEV.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            //            .then(Commands.literal(FORK_TP)
            //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
            .executes(x -> {
              System.out.println("flib test " + x.getSource());
              return 0; // CommandHome.executeTp(x, EntityArgument.getPlayers(x, ARG_PLAYER));
            })
        //                ) // end of fork
        //
        )
    //            .then(Commands.literal(FORK_RESET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandHome.executeReset(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    })))
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument("x", IntegerArgumentType.integer())
    //                        .then(Commands.argument("y", IntegerArgumentType.integer())
    //                            .then(Commands.argument("z", IntegerArgumentType.integer())
    //                                .executes(x -> {
    //                                  return CommandHome.executeSetHome(x, EntityArgument.getPlayers(x, ARG_PLAYER), new BlockPos(IntegerArgumentType.getInteger(x, "x"), IntegerArgumentType.getInteger(x, "y"), IntegerArgumentType.getInteger(x, "z")));
    //                                }))))))
    //            .then(Commands.literal(FORK_SAVE)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandHome.executeSaveHome(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    }))))
    //        //cyclic gethome   !! this is player only, not command block. TODO:? evaluate @Deprecated in 1.19 
    //        .then(Commands.literal(CyclicCommands.GETHOME.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDGETHOME.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .executes(x -> {
    //              return CommandGetHome.execute(x);
    //            }))
    //        // cyclic health add @p -1
    //        // cyclic health random @p -2 2
    //        // cyclic health factor @p 0.8
    //        .then(Commands.literal(CyclicCommands.HEALTH.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDHEALTH.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, FloatArgumentType.floatArg(0, 100F))
    //                        .executes(x -> {
    //                          return CommandHealth.executeSet(x, EntityArgument.getPlayers(x, ARG_PLAYER), FloatArgumentType.getFloat(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_ADD)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
    //                        .executes(x -> {
    //                          return CommandHealth.executeAdd(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_FACTOR)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
    //                        .executes(x -> {
    //                          return CommandHealth.executeFactor(x, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-20, 20))
    //                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-20, 20))
    //                            .executes(x -> {
    //                              return CommandHealth.addRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
    //                            }))))))
    //        // cyclic hearts add @p -1
    //        // cyclic hearts random @p -2 2
    //        // cyclic hearts factor @p 0.8
    //        .then(Commands.literal(CyclicCommands.HEARTS.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDHEALTH.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
    //                        .executes(x -> { // TODO: heartsCommand; attributesCommand
    //                          return AttributesUtil.setHearts(EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_ADD)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
    //                        .executes(x -> {
    //                          return AttributesUtil.add(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_FACTOR)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
    //                        .executes(x -> {
    //                          return AttributesUtil.multiply(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-100, 100))
    //                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-100, 100))
    //                            .executes(x -> {
    //                              return AttributesUtil.addRandom(Attributes.MAX_HEALTH, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
    //                            }))))))
    //        //cyclic scoreboard random @p 4 9 <objective>
    //        //cyclic scoreboard add @p 5 <objective>
    //        //cyclic scoreboard test @p <objective>
    //        .then(Commands.literal(CyclicCommands.SCOREBOARD.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_ELEVATED);
    //            })
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
    //                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer())
    //                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer())
    //                            .then(Commands.argument(ARG_OBJECTIVE, StringArgumentType.greedyString())
    //                                .executes(x -> {
    //                                  return CommandScoreboard.scoreboardRng(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
    //                                      ObjectiveArgument.getObjective(x, ARG_OBJECTIVE),
    //                                      IntegerArgumentType.getInteger(x, ARG_MIN),
    //                                      IntegerArgumentType.getInteger(x, ARG_MAX));
    //                                }))))))
    //            .then(Commands.literal(FORK_ADD)
    //                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
    //                        .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
    //                            .executes(x -> {
    //                              return CommandScoreboard.scoreboardAdd(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
    //                                  ObjectiveArgument.getObjective(x, ARG_OBJECTIVE), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                            })))))
    //            .then(Commands.literal("test")
    //                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
    //                    .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
    //                        .executes(x -> {
    //                          return CommandScoreboard.scoreboardRngTest(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
    //                              ObjectiveArgument.getObjective(x, ARG_OBJECTIVE));
    //                        })))))
    //        // /cyclic attributes minecraft:reach_distance add @p 3
    //        // /cyclic attributes minecraft:reach_distance random @p 3 8
    //        // /cyclic attributes minecraft:reach_distance reset @p
    //        .then(Commands.literal(CyclicCommands.ATTRIBUTE.toString()) //same as hearts but subcommand again instead of just number
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_ELEVATED);
    //            })
    //            .then(Commands.argument(ARG_ATTR, ResourceKeyArgument.key(Registry.ATTRIBUTE_REGISTRY))
    //                .then(Commands.literal(FORK_ADD)
    //                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                        .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-10000, 10000))
    //                            .executes(x -> {
    //                              return AttributesUtil.add(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                            }))))
    //                .then(Commands.literal(FORK_RANDOM)
    //                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                        .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-10000, 10000))
    //                            .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-10000, 10000))
    //                                .executes(x -> {
    //                                  return AttributesUtil.addRandom(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
    //                                })))))
    //                .then(Commands.literal(FORK_FACTOR)
    //                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                        .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 100))
    //                            .executes(x -> {
    //                              return AttributesUtil.multiply(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
    //                            }))))
    //                .then(Commands.literal(FORK_RESET)
    //                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                        .executes(x -> {
    //                          return AttributesUtil.reset(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER));
    //                        })))))
    //        // cyclic gamemode @p 1
    //        .then(Commands.literal(CyclicCommands.GAMEMODE.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_ELEVATED);
    //            })
    //            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 3))
    //                    .executes(x -> {
    //                      return CommandGamemode.executeGamemode(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                    }))))
    //        //  cyclic gravity set @p true
    //        //  cyclic gravity random @p
    //        //  cyclic gravity toggle @p
    //        .then(Commands.literal(CyclicCommands.GRAVITY.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_ELEVATED);
    //            })
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
    //                        .executes(x -> {
    //                          return CommandGravity.executeGravity(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandGravity.executeRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    })))
    //            .then(Commands.literal(FORK_TOGGLE)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandGravity.executeToggle(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    }))))
    //        //       /cyclic glowing random @p
    //        //       /cyclic glowing set @p false
    //        //       /cyclic glowing toggle @p
    //        .then(Commands.literal(CyclicCommands.GLOWING.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_ELEVATED);
    //            })
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
    //                        .executes(x -> {
    //                          return CommandGlowing.executeGlowing(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandGlowing.executeRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    })))
    //            .then(Commands.literal(FORK_TOGGLE)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .executes(x -> {
    //                      return CommandGlowing.executeToggle(x, EntityArgument.getPlayers(x, ARG_PLAYER));
    //                    }))))
    //        //   /cyclic hunger add @p -4
    //        //   /cyclic hunger set @p -4
    //        //   /cyclic hunger random @p -4 9
    //        //   /cyclic hunger factor @p 0.5
    //        .then(Commands.literal(CyclicCommands.HUNGER.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDHUNGER.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .then(Commands.literal(FORK_FACTOR)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
    //                        .executes(x -> {
    //                          return CommandHunger.executeFactor(x, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_ADD)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-20, 20))
    //                        .executes(x -> {
    //                          return CommandHunger.executeAdd(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_SET)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
    //                        .executes(x -> {
    //                          return CommandHunger.executeSet(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                        }))))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
    //                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-20, 20))
    //                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-20, 20))
    //                            .executes(x -> {
    //                              return CommandHunger.executeRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
    //                            }))))))
    //        .then(Commands.literal(CyclicCommands.DEV.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDDEV.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .then(Commands.literal("nbt")
    //                .executes(x -> {
    //                  return CommandNbt.executePrintNbt(x);
    //                }))
    //            .then(Commands.literal("tags")
    //                .executes(x -> {
    //                  return CommandNbt.executePrintTags(x);
    //                }))
    //            .then(Commands.literal(FORK_RANDOM)
    //                .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer())
    //                    .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer())
    //                        .executes(x -> {
    //                          return CommandRegistry.returnIntRng(
    //                              IntegerArgumentType.getInteger(x, ARG_MIN),
    //                              IntegerArgumentType.getInteger(x, ARG_MAX));
    //                        })))))
    //        .then(Commands.literal(CyclicCommands.PING.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(COMMANDPING.get() ? PERM_ELEVATED : PERM_EVERYONE);
    //            })
    //            .then(Commands.literal("nether")
    //                .executes(x -> {
    //                  return CommandNetherping.exeNether(x);
    //                }))
    //            .then(Commands.literal("here")
    //                .executes(x -> {
    //                  return CommandNetherping.execute(x);
    //                })))
    //        .then(Commands.literal(CyclicCommands.TODO.toString())
    //            .requires((p) -> {
    //              return p.hasPermission(PERM_EVERYONE);
    //            })
    //            .then(Commands.literal(FORK_ADD)
    //                .then(Commands.argument("arguments", StringArgumentType.greedyString())
    //                    .executes(x -> {
    //                      return CommandTask.add(x, StringArgumentType.getString(x, "arguments"));
    //                    })))
    //            .then(Commands.literal("remove")
    //                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
    //                    .executes(x -> {
    //                      return CommandTask.remove(x, IntegerArgumentType.getInteger(x, ARG_VALUE));
    //                    })))
    //            .then(Commands.literal("list")
    //                .executes(x -> {
    //                  return CommandTask.list(x);
    //                })))
    //new commands here
    );
  }

  private static int returnIntRng(int min, int max) {
    return RAND.nextInt(min, max);
  }
}
