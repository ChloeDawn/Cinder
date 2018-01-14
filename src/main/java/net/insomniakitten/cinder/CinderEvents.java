package net.insomniakitten.cinder;

import net.insomniakitten.cinder.api.IDecayableLight;
import net.insomniakitten.cinder.api.TileDecayableLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Cinder.ID)
public final class CinderEvents {

    private CinderEvents() {}

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
        FMLClientHandler handler = FMLClientHandler.instance();
        GameSettings settings = handler.getClient().gameSettings;
        if (settings.showDebugInfo && !settings.reducedDebugInfo) {
            EntityPlayer player = handler.getClientPlayerEntity();
            if (player != null && player.world != null) {
                RayTraceResult result = handler.getClient().objectMouseOver;
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    TileEntity tile = player.world.getTileEntity(result.getBlockPos());
                    if (tile instanceof TileDecayableLight) {
                        int light = ((TileDecayableLight) tile).amount();
                        event.getRight().add("light: " + light);
                    }
                }
            }
        }
    }

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
                event.getWorld().notifyBlockUpdate(event.getPos(), state, state, 3);
                event.getWorld().checkLight(event.getPos());
                event.setUseItem(Event.Result.DENY);
            }
        }
    }

    public static void onTileUpdate(TileDecayableLight light, IBlockState state, World world, BlockPos pos) {
        IDecayableLight idl = (IDecayableLight) state.getBlock();
        if (world.getTotalWorldTime() % (CinderConfig.burnTime / 15) == 0) {
            if (light.amount() > 1) {
                light.decay();
                idl.onLightDecay(state, world, pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                world.checkLight(pos);
            } else if (light.amount() == 1) {
                light.extinguish();
                idl.onExtinguished(state, world, pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                world.checkLight(pos);
            }
            if (light.amount() > 0 && world.rand.nextInt(10) == 0) {
                /*
                EnumFacing side = EnumFacing.VALUES[world.rand.nextInt(5) + 1];
                if (world.isAirBlock(pos.offset(side))) {
                    IBlockState fire = Blocks.FIRE.getDefaultState();
                    world.setBlockState(pos.offset(side), fire, 11);
                }
                */
            }
        } else if (light.amount() > 1 && world.isRainingAt(pos.up()) && world.rand.nextInt(80) == 0) {
            light.extinguish();
            idl.onExtinguished(state, world, pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            world.checkLight(pos);
        }
    }

}
