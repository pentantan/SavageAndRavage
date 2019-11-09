package populousteam.savageandravage.client.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ISavageParticle extends IParticleFactory<IParticleData> {
    Particle makeParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int[] params);

    @Nullable
    @Override
    default Particle makeParticle(IParticleData type, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return null;
    }
}