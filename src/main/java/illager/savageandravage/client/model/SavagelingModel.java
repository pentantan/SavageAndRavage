package illager.savageandravage.client.model;//Made with Blockbench
//Paste this code into your mod.

import illager.savageandravage.entity.SavagelingEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SavagelingModel<T extends SavagelingEntity> extends EntityModel<T> {
    private final RendererModel head;
    private final RendererModel body;
    private final RendererModel leg_right;
    private final RendererModel leg_left;
    private final RendererModel jaw;

    public SavagelingModel() {
        textureWidth = 40;
        textureHeight = 40;

        head = new RendererModel(this);
        head.setRotationPoint(0.0F, 15.0F, -2.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -3.0F, -6.0F, -6.0F, 6, 8, 6, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 24, 0, -1.0F, -1.0F, -8.0F, 2, 4, 2, 0.0F, false));

        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 24.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 0, 26, -4.0F, -10.0F, -3.0F, 8, 6, 8, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 18, 8, -5.0F, -10.0F, -2.0F, 1, 5, 6, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 18, 8, 4.0F, -10.0F, -2.0F, 1, 5, 6, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 13, 19, -3.0F, -8.0F, 5.0F, 6, 0, 5, 0.0F, false));

        leg_right = new RendererModel(this);
        leg_right.setRotationPoint(-2.0F, 20.0F, 2.0F);
        leg_right.cubeList.add(new ModelBox(leg_right, 28, 8, -1.0F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));
        leg_right.cubeList.add(new ModelBox(leg_right, 22, 6, -2.0F, 4.0F, -2.0F, 3, 0, 2, 0.0F, false));

        leg_left = new RendererModel(this);
        leg_left.setRotationPoint(2.0F, 20.0F, 2.0F);
        leg_left.cubeList.add(new ModelBox(leg_left, 28, 8, 0.0F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));
        leg_left.cubeList.add(new ModelBox(leg_left, 22, 6, -1.0F, 4.0F, -2.0F, 3, 0, 2, 0.0F, false));

        jaw = new RendererModel(this);
        jaw.setRotationPoint(0.0F, 17.0F, -2.0F);
        setRotationAngle(jaw, 0.0873F, 0.0F, 0.0F);
        jaw.cubeList.add(new ModelBox(jaw, 0, 19, -3.0F, -1.0F, -6.0F, 6, 2, 5, 0.0F, false));
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        body.render(f5);
        leg_right.render(f5);
        leg_left.render(f5);
        jaw.render(f5);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);

        this.jaw.copyModelAngles(this.head);

        this.leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}