package populousteam.savageandravage.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class UseItemOnLeftHandGoal<T extends MobEntity> extends Goal {
    private final T field_220766_a;
    private final ItemStack field_220767_b;
    private final Predicate<? super T> field_220768_c;
    private final SoundEvent field_220769_d;
    private boolean canHold;
    private int holdTick;

    public UseItemOnLeftHandGoal(T p_i50319_1_, ItemStack p_i50319_2_, @Nullable SoundEvent p_i50319_3_, Predicate<? super T> p_i50319_4_, boolean hold) {
        this.field_220766_a = p_i50319_1_;
        this.field_220767_b = p_i50319_2_;
        this.field_220769_d = p_i50319_3_;
        this.field_220768_c = p_i50319_4_;
        this.canHold = hold;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.field_220768_c.test(this.field_220766_a);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.field_220766_a.isHandActive() || this.canHold && this.holdTick <= 40;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.field_220766_a.setItemStackToSlot(EquipmentSlotType.OFFHAND, this.field_220767_b.copy());
        this.field_220766_a.setActiveHand(Hand.OFF_HAND);
        this.holdTick = 0;
        if (this.field_220769_d != null) {
            this.field_220766_a.playSound(this.field_220769_d, 1.0F, this.field_220766_a.getRNG().nextFloat() * 0.2F + 0.9F);
        }
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.field_220766_a.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        this.holdTick = 0;
        this.canHold = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.canHold && holdTick++ <= 40) {
            this.field_220766_a.setActiveHand(Hand.OFF_HAND);
        }
    }
}