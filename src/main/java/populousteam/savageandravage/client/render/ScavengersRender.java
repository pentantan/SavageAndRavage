package populousteam.savageandravage.client.render;

import populousteam.savageandravage.SavageAndRavageCore;
import populousteam.savageandravage.client.model.ScavengersModel;
import populousteam.savageandravage.client.render.layer.HeldCrossOffHandItemLayer;
import populousteam.savageandravage.entity.illager.ScavengersEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;

public class ScavengersRender<T extends ScavengersEntity> extends MobRenderer<T, ScavengersModel<T>> {
    private static final ResourceLocation ILLAGER_TEXTURE = new ResourceLocation(SavageAndRavageCore.MODID, "textures/entity/illager/scavenger.png");

    public ScavengersRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ScavengersModel<>(), 0.5F);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new HeldCrossOffHandItemLayer<>(this));
    }

    protected ResourceLocation getEntityTexture(ScavengersEntity entity) {
        return ILLAGER_TEXTURE;
    }

}