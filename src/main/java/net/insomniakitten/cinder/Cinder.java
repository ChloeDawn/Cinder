package net.insomniakitten.cinder;

import net.insomniakitten.cinder.api.TileDecayableLight;
import net.insomniakitten.cinder.block.BlockDecayableTorch;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Cinder.ID, name = Cinder.NAME, version = Cinder.VERSION)
@Mod.EventBusSubscriber(modid = Cinder.ID)
public final class Cinder {

    public static final String ID = "cinder";
    public static final String NAME = "Cinder";
    public static final String VERSION = "%VERSION%";

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        GameRegistry.registerTileEntity(TileDecayableLight.class, ID + ":decayable_light");
        event.getRegistry().register(new BlockDecayableTorch().setRegistryName("minecraft", "torch"));
    }

}
