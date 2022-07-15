package com.adrilasar.nomod.block.custom;

import com.adrilasar.nomod.item.custom.SpeakerUsb;
import com.adrilasar.nomod.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.impl.data.BlockDataAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ITickList;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class Speaker extends Block
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape S_SHAPE = Block.box(0.0D, 0.0D, 3.0D, 16.0D, 26.0D, 16.0D);
    protected static final VoxelShape E_SHAPE = Block.box(3.0D, 0.0D, 0.0D, 16.0D, 26.0D, 16.0D);
    protected static final VoxelShape W_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 13.0D, 26.0D, 16.0D);
    protected static final VoxelShape N_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 26.0D, 13.0D);

    public static final BooleanProperty PLAYING = BooleanProperty.create("playing");

    public Speaker(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        switch(pState.getValue(FACING)) {
            case SOUTH:
                return S_SHAPE;
            case EAST:
                return E_SHAPE;
            case WEST:
                return W_SHAPE;
            default:
                return N_SHAPE;
        }
    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        Item item = pPlayer.getItemInHand(pHand).getItem();
        if (item instanceof SpeakerUsb) {
            pLevel.playLocalSound(pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            pLevel.getBlockEntity(pPos).getUpdateTag().putString("sound", pPlayer.getItemInHand(pHand).getTag().getString("sound"));
            pLevel.getBlockEntity(pPos).getUpdateTag().putString("duration", pPlayer.getItemInHand(pHand).getTag().getString("duration"));
            return ActionResultType.sidedSuccess(pLevel.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        if (pRand.nextInt(20) == 0 /*&& pLevel.getBlockEntity(pPos).getUpdateTag().contains("sound")*/) {
            if(!pState.getValue(PLAYING)) {
                pState.setValue(PLAYING, true);
                //TODO Cambiar por sonido para todos (no local)
//                pLevel.playLocalSound(pPos.getX(), pPos.getY(), pPos.getZ(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }
// TODO Clase que con la duracion (NBT) ponga isPlaying a false cuando se acabe la cancion

//FACING

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
        pBuilder.add(PLAYING);
    }

    /*@Override
    public void playSound(@Nullable PlayerEntity pPlayer, BlockPos pPos, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {

    }*/
}
