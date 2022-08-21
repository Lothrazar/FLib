package com.lothrazar.library.item;

import java.util.List;
import com.lothrazar.library.util.ItemStackUtil;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFlib extends Item {

  public static final int COLOUR_RF_BAR = 0xBA0909;
  public static final float INACCURACY_DEFAULT = 1.0F;
  public static final float VELOCITY_MAX = 1.5F;

  public static class Settings {

    boolean tooltip = false;
    int burnTime = 0;

    public Settings tooltip() {
      this.tooltip = true;
      return this;
    }

    public Settings noTooltip() {
      this.tooltip = false;
      return this;
    }

    public Settings burnTime(int burnTicks) {
      burnTime = Math.max(0, burnTicks);
      return this;
    }

    public void tooltipApply(Item block, List<Component> tooltipList) {
      tooltipList.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }

  private Settings me;

  public ItemFlib(Properties prop, Settings custom) {
    super(prop);
    this.me = custom;
  }

  public ItemFlib(Properties prop) {
    this(prop, new Settings());
  }

  @Override
  public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
    return me.burnTime;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  public void tryRepairWith(ItemStack stackToRepair, Player player, Item target) {
    if (stackToRepair.isDamaged()) {
      ItemStack torches = this.findAmmo(player, target);
      if (!torches.isEmpty()) {
        torches.shrink(1);
        ItemStackUtil.repairItem(stackToRepair);
      }
    }
  }
  //*********************** Projectile stuff

  protected void shootMe(Level world, Player shooter, Projectile ball, float pitch, float velocityFactor) {
    if (world.isClientSide) {
      return;
    }
    Vec3 vector3d1 = shooter.getUpVector(1.0F);
    // pitch is degrees so can be -10, +10, etc
    Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), pitch, true);
    Vec3 vector3d = shooter.getViewVector(1.0F);
    Vector3f vector3f = new Vector3f(vector3d);
    vector3f.transform(quaternion);
    ball.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocityFactor * VELOCITY_MAX, INACCURACY_DEFAULT);
    //    worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
    //        SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    world.addFreshEntity(ball);
  }

  protected ItemStack findAmmo(Player player, Item item) {
    for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
      ItemStack itemstack = player.getInventory().getItem(i);
      if (itemstack.getItem() == item) {
        return itemstack;
      }
    }
    return ItemStack.EMPTY;
  }

  public float getChargedPercent(ItemStack stack, int chargeTimer) {
    return BowItem.getPowerForTime(this.getUseDuration(stack) - chargeTimer);
  }
}
