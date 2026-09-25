package de.florianmichael.viamcp.fixes;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FixedSoundEngine {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public FixedSoundEngine() {
    }

    public static boolean destroyBlock(World world, BlockPos pos, boolean var2) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
        if (block.getMaterial() == Material.air) {
            return false;
        }

        if (var2) {
            block.dropBlockAsItem(world, pos, iblockstate, 0);
        }

        return world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }

    public static boolean onItemUse(
        ItemBlock itemBlock, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float var6, float var7, float var8
    ) {
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        if (stack.stackSize == 0) {
            return false;
        }

        if (!player.canPlayerEdit(pos, facing, stack)) {
            return false;
        }

        if (world.canBlockBePlaced(itemBlock.getBlock(), pos, false, facing, (Entity)null, stack)) {
            int i = itemBlock.getMetadata(stack.getMetadata());
            IBlockState iblockstate = itemBlock.getBlock().onBlockPlaced(world, pos, facing, var6, var7, var8, i, player);
            if (world.setBlockState(pos, iblockstate, 3)) {
                iblockstate = world.getBlockState(pos);
                if (iblockstate.getBlock() == itemBlock.getBlock()) {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    itemBlock.getBlock().onBlockPlacedBy(world, pos, iblockstate, player, stack);
                }

                if (ViaLoadingBase.getInstance().getTargetVersion().getOriginalVersion() != 47) {
                    mc.theWorld
                        .playSoundAtPos(
                            pos.add(0.5, 0.5, 0.5),
                            itemBlock.getBlock().stepSound.getPlaceSound(),
                            (itemBlock.getBlock().stepSound.getVolume() + 1.0F) / 2.0F,
                            itemBlock.getBlock().stepSound.getFrequency() * 0.8F,
                            false
                        );
                } else {
                    world.playSoundEffect(
                        pos.getX() + 0.5F,
                        pos.getY() + 0.5F,
                        pos.getZ() + 0.5F,
                        itemBlock.getBlock().stepSound.getPlaceSound(),
                        (itemBlock.getBlock().stepSound.getVolume() + 1.0F) / 2.0F,
                        itemBlock.getBlock().stepSound.getFrequency() * 0.8F
                    );
                }

                stack.stackSize--;
            }

            return true;
        }
        return false;
    }
}
