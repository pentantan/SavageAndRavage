package populousteam.savageandravage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import populousteam.savageandravage.client.IllagerEntityRender;

@Mod(SavageAndRavage.MOD_ID)
public class SavageAndRavage {

    public static final String MOD_ID = "savageandravage";
    public SavageAndRavage instance;

    public SavageAndRavage(){

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        IllagerEntityRender.entityRender();
    }
}
