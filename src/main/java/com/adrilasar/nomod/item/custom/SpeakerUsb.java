package com.adrilasar.nomod.item.custom;

import com.adrilasar.nomod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SpeakerUsb extends MusicDiscItem
{
    public SpeakerUsb(int p_i48475_1_, SoundEvent p_i48475_2_, Properties p_i48475_3_) {
        super(p_i48475_1_, p_i48475_2_, p_i48475_3_);
    }


    public SpeakerUsb(int p_i244794_1_, Supplier<SoundEvent> p_i244794_2_, Properties p_i244794_3_) {
        super(p_i244794_1_, p_i244794_2_, p_i244794_3_);
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.SPEAKER.get()) || blockstate.is(Blocks.JUKEBOX) && !blockstate.getValue(JukeboxBlock.HAS_RECORD)) {
            ItemStack itemstack = pContext.getItemInHand();
            if (!world.isClientSide) {
                ((JukeboxBlock)ModBlocks.SPEAKER.get()).setRecord(world, blockpos, blockstate, itemstack);
                world.levelEvent((PlayerEntity)null, 1010, blockpos, Item.getId(this));
                itemstack.shrink(1);
                PlayerEntity playerentity = pContext.getPlayer();
                if (playerentity != null) {
                    playerentity.awardStat(Stats.PLAY_RECORD);
                }
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }
}
