package com.adrilasar.nomod.block.custom;

import com.adrilasar.nomod.container.KalcenitaRefineryContainer;
import com.adrilasar.nomod.tileentity.KalcenitaRefineryTile;
import com.adrilasar.nomod.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class KalcenitaRefineryBlock extends Block
{
    public static final BooleanProperty FUELED = BooleanProperty.create("fueled");
    public KalcenitaRefineryBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(this.stateDefinition.any().setValue(FUELED, false));
    }


    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof KalcenitaRefineryTile) {
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN,
                        SoundCategory.BLOCKS, 0.5F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
                NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {

            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.nomod.kalcenita_refinery");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new KalcenitaRefineryContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.KALCENITA_REFINERY_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof KalcenitaRefineryTile) {

            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FUELED);
    }
}
