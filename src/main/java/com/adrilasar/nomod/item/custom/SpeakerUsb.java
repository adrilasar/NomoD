package com.adrilasar.nomod.item.custom;

import com.adrilasar.nomod.block.ModBlocks;
import com.adrilasar.nomod.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class SpeakerUsb extends MusicDiscItem
{
    public SpeakerUsb(int p_i48475_1_, SoundEvent p_i48475_2_, Properties p_i48475_3_) {
        super(p_i48475_1_, p_i48475_2_, p_i48475_3_);
    }

    public SpeakerUsb(int p_i244794_1_, Supplier<SoundEvent> p_i244794_2_, Properties p_i244794_3_) {
        super(p_i244794_1_, p_i244794_2_, p_i244794_3_);
    }

    private int getDuration(SoundEvent sound) {
        String fileName = sound.getLocation().getPath();
        File file = new File(this.getClass().getClassLoader().getResource("assets/nomod/sounds/" + fileName + ".ogg").getFile());
        AudioFile af = null;
        try {
            af = AudioFileIO.read(file);
        } catch (CannotReadException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TagException e) {
            throw new RuntimeException(e);
        } catch (ReadOnlyFileException e) {
            throw new RuntimeException(e);
        } catch (InvalidAudioFrameException e) {
            throw new RuntimeException(e);
        }
        AudioHeader ah = af.getAudioHeader();
        return ah.getTrackLength();
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        if(!pContext.getItemInHand().hasTag()){
            int duration = getDuration(this.getSound());
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("duration", duration);
            nbt.putString("sound", this.getSound().getLocation().getPath());
            pContext.getItemInHand().setTag(nbt);
        }
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.SPEAKER.get()) || blockstate.is(Blocks.JUKEBOX) && !blockstate.getValue(JukeboxBlock.HAS_RECORD)) {
            ItemStack itemstack = pContext.getItemInHand();
            if (!world.isClientSide) {
                ((JukeboxBlock)ModBlocks.SPEAKER.get()).setRecord(world, blockpos, blockstate, itemstack);
                world.levelEvent((PlayerEntity)null, 1010, blockpos, Item.getId(this));
                world.playSound(pContext.getPlayer(), blockpos, this.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                //TODO
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
