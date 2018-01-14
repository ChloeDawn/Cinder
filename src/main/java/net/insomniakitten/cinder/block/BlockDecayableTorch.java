package net.insomniakitten.cinder.block;

import net.insomniakitten.cinder.api.IDecayableLight;
import net.insomniakitten.cinder.api.TileDecayableLight;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public final class BlockDecayableTorch extends BlockTorch implements IDecayableLight {

    {
        setHardness(0.0F);
        setSoundType(SoundType.WOOD);
        setUnlocalizedName("torch");
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDecayableLight) {
            return ((TileDecayableLight) tile).amount();
        }
        return 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDecayableLight();
    }

}
