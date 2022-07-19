package com.adrilasar.nomod.tileentity;

import com.adrilasar.nomod.block.custom.KalcenitaRefineryBlock;
import com.adrilasar.nomod.data.recipes.KalcenitaRefineryRecipe;
import com.adrilasar.nomod.data.recipes.ModRecipeTypes;
import com.adrilasar.nomod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class KalcenitaRefineryTile extends TileEntity implements ITickableTileEntity
{
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public KalcenitaRefineryTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public KalcenitaRefineryTile() {
        this(ModTileEntities.KALCENITA_REFINERY_TILE.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.put("inv", itemHandler.serializeNBT());
        return super.save(pCompound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem() == ModItems.RAW_KALCENITA.get();
                    //TODO Cambiar lava
                    case 1: return stack.getItem() == Items.LAVA_BUCKET.getItem();
                    //TODO Cambiar charm
                    case 2: return stack.getItem() == ModItems.NOMOD_CHARM.get();
                    case 3: return stack.getItem() == ModItems.KALCENITA_INGOT.get();
                    default:
                        return false;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                switch (slot) {
                    case 1:
                        return 4;
                    case 2:
                        return 1;
                    default:
                        return 64;
                }
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void craft() {
        Inventory inv = new Inventory(itemHandler.getSlots()-1);
        for (int i = 0; i < itemHandler.getSlots()-1; i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Optional<KalcenitaRefineryRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.REFINERY_RECIPE, inv, level);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();
            craftTheItem(output);
            setChanged();
        });
    }

    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 3, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.insertItem(3, output, false);
        /*if(itemHandler.extractItem(0, 3, true).getCount() == 3 && itemHandler.extractItem(1, 1, true).getCount() == 1) {
            itemHandler.extractItem(0, 3, false);
            itemHandler.extractItem(1, 1, false);

            //Don't use insertItem to only allow inserting items on the output slot to the game
            if(itemHandler.getStackInSlot(3).getCount() <=0 || itemHandler.getStackInSlot(3).getItem() != ModItems.KALCENITA_INGOT.get()) {
                itemHandler.setStackInSlot(3, output);
            }
            else if(itemHandler.getStackInSlot(3).getItem() == output.getItem()) {
                itemHandler.getStackInSlot(3).grow(output.getCount());
            }
        }*/
    }

    @Override
    public void tick() {
        if(level.isClientSide)
            return;

        craft();
        checkFuel();
    }

    private void checkFuel() {
        if(this.getBlockState().hasProperty(KalcenitaRefineryBlock.FUELED)) {
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(KalcenitaRefineryBlock.FUELED,
                    itemHandler.getStackInSlot(1).getItem() == Items.LAVA_BUCKET.getItem()), 3);
            System.out.print(itemHandler.getStackInSlot(1).getItem() == Items.LAVA_BUCKET.getItem());
            System.out.print(" -- " + level.getBlockState(this.getBlockPos()).getValue(KalcenitaRefineryBlock.FUELED) + "\n");
        }

    }
}
