package populousteam.savageandravage.entity.ai;

import populousteam.savageandravage.entity.illager.AbstractHouseIllagerEntity;
import net.minecraft.entity.ai.goal.Goal;

public class WakeUpGoal extends Goal {
    private final AbstractHouseIllagerEntity illager;

    public WakeUpGoal(AbstractHouseIllagerEntity houseillager) {
        this.illager = houseillager;
    }

    @Override
    public boolean shouldExecute() {
        if (this.illager.world.isDaytime()) {
            return this.illager.isSleeping();
        } else {
            return this.illager.isSleeping() && (illager.posY < (double) this.illager.getBedPosition().get().getY() + 0.4D || this.illager.isSleeping() && !this.illager.getBedPosition().get().withinDistance(illager.getPositionVec(), 1.14D) || !this.illager.getBedPosition().isPresent());
        }
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.illager.wakeUp();
    }
}