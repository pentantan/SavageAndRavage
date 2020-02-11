package populousteam.savageandravage.client.render;

import populousteam.savageandravage.SavageAndRavageCore;
import populousteam.savageandravage.client.model.SkeletonVillagerModel;
import populousteam.savageandravage.entity.SkeletonVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.ZombieVillagerModel;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkeletonVillagerRender<T extends SkeletonVillagerEntity> extends MobRenderer<T, SkeletonVillagerModel<T>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(SavageAndRavageCore.MODID, "textures/entity/skeletonvillager.png");

    public SkeletonVillagerRender(EntityRendererManager p_i50959_1_) {
        super(p_i50959_1_, new SkeletonVillagerModel<>(), 0.5F);
        this.addLayer(new HeldItemLayer<T, SkeletonVillagerModel<T>>(this) {
            public void render(T entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
                if (entityIn.isAggressive() || entityIn.isCharging() || entityIn.isHolding(Items.CROSSBOW)) {
                    super.render(entityIn, p_212842_2_, p_212842_3_, p_212842_4_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
                }
            }
        });
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new BipedArmorLayer(this, new ZombieVillagerModel(0.5F, true), new ZombieVillagerModel(1.0F, true)));
    }

    protected ResourceLocation getEntityTexture(SkeletonVillagerEntity entity) {
        return TEXTURES;
    }
}