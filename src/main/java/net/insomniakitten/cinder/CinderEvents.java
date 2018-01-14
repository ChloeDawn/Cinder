package net.insomniakitten.cinder;

import net.insomniakitten.cinder.api.IDecayableLight;
import net.insomniakitten.cinder.api.TileDecayableLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Cinder.ID)
public final class CinderEvents {

    private CinderEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        if (state.getBlock() instanceof IDecayableLight) {
            if (((IDecayableLight) state.getBlock()).onIgnited(
                    state, event.getWorld(), event.getPos(),
                    event.getEntityPlayer(), event.getHand()
            )) {
                if (tile == null) {
                    TileDecayableLight light = new TileDecayableLight().ignite();
                    event.getWorld().setTileEntity(event.getPos(), light);
                } else if (tile instanceof TileDecayableLight) {
                    ((TileDecayableLight) tile).ignite();
                }
            }
        }
    }

    public static void onTileUpdate(TileDecayableLight light, IBlockState state, World world, BlockPos pos) {
        IDecayableLight idl = (IDecayableLight) state.getBlock();

        if (!world.isRainingAt(pos.up()) && light.amount() > 1) {
            light.decay();
            idl.onLightDecay(state, world, pos);
        } else {
            light.extinguish();
            idl.onExtinguished(state, world, pos);
        }

        if (world.rand.nextInt(3) == 0) {
            EnumFacing side = EnumFacing.VALUES[world.rand.nextInt(5) + 1];
            if (world.isAirBlock(pos.offset(side))) {
                IBlockState fire = Blocks.FIRE.getDefaultState();
                world.setBlockState(pos.offset(side), fire, 11);
            }
        }
    }

}
