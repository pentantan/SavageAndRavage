package illager.savageandravage.client.render;

import illager.savageandravage.SavageAndRavageCore;
import illager.savageandravage.client.model.SavagelingModel;
import illager.savageandravage.entity.SavagelingEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SavagelingRender<T extends SavagelingEntity> extends MobRenderer<T, SavagelingModel<T>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(SavageAndRavageCore.MODID, "textures/entity/savageling.png");

    public SavagelingRender(EntityRendererManager p_i50959_1_) {
        super(p_i50959_1_, new SavagelingModel<>(), 0.3F);
    }


    protected ResourceLocation getEntityTexture(SavagelingEntity entity) {
        return TEXTURES;
    }
}