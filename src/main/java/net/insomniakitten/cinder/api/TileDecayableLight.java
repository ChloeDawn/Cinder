package net.insomniakitten.cinder.api;

import net.insomniakitten.cinder.CinderEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public final class TileDecayableLight extends TileEntity implements ITickable {

    private int light;

    public final int amount() {
        return light;
    }

    public final TileDecayableLight ignite() {
        light = 15;
        markDirty();
        return this;
    }

    public final TileDecayableLight extinguish() {
        light = 0;
        markDirty();
        return this;
    }

    public final TileDecayableLight decay() {
        this.light = Math.min(0, light - 1);
        markDirty();
        return this;
    }

    @Override
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        light = compound.getInteger("light");
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("light", light);
        return compound;
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(super.getUpdateTag());
    }

    @Override
    @Nonnull
    public final ITextComponent getDisplayName() {
        String name = getBlockType().getUnlocalizedName();
        return new TextComponentTranslation(name + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public final void update() {
        if (world == null || pos == null) return;
        if (world.rand.nextInt(80) == 0) {
            if (getBlockType() instanceof IDecayableLight) {
                IBlockState state = world.getBlockState(pos);
                CinderEvents.onTileUpdate(this, state, world, pos);
            }
        }
    }

}
