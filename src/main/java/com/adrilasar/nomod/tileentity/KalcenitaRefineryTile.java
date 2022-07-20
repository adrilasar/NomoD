package com.adrilasar.nomod.tileentity;

import com.adrilasar.nomod.block.custom.KalcenitaRefineryBlock;
import com.adrilasar.nomod.data.recipes.IKalcenitaRefineryRecipe;
import com.adrilasar.nomod.data.recipes.KalcenitaRefineryRecipe;
import com.adrilasar.nomod.data.recipes.ModRecipeTypes;
import com.adrilasar.nomod.item.ModItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;

public class KalcenitaRefineryTile extends TileEntity implements ITickableTileEntity
{
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    /**
     * The number of burn ticks left
     */
    private int litTime = 0;
    /**
     * Total burn time in ticks of the burned item
     */
    private int litDuration = 216;
    /**
     * Item's craft progress
     */
    private int cookingProgress = 0;
    /**
     * Item's total craft duration
     */
    private int cookingTotalTime = 72;
    protected final IIntArray dataAccess = new IIntArray() {
        public int get(int pIndex) {
            switch(pIndex) {
                case 0:
                    return KalcenitaRefineryTile.this.litTime;
                case 1:
                    return KalcenitaRefineryTile.this.litDuration;
                case 2:
                    return KalcenitaRefineryTile.this.cookingProgress;
                case 3:
                    return KalcenitaRefineryTile.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int pIndex, int pValue) {
            switch(pIndex) {
                case 0:
                    KalcenitaRefineryTile.this.litTime = pValue;
                    break;
                case 1:
                    KalcenitaRefineryTile.this.litDuration = pValue;
                    break;
                case 2:
                    KalcenitaRefineryTile.this.cookingProgress = pValue;
                    break;
                case 3:
                    KalcenitaRefineryTile.this.cookingTotalTime = pValue;
            }

        }

        public int getCount() {
            return 4;
        }
    };

private static final ImmutableMap<Item, Integer> burnTimes = ImmutableMap.of(
        Items.LAVA_BUCKET, 1800
);

    public KalcenitaRefineryTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public KalcenitaRefineryTile() {
        this(ModTileEntities.KALCENITA_REFINERY_TILE.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        cookingProgress = nbt.getInt("kalcenita_refinery.progress");
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.put("inv", itemHandler.serializeNBT());
        pCompound.putInt("kalcenita_refinery.progress", cookingProgress);
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

    public IIntArray getDataAccess() {
        return dataAccess;
    }

    @Override
    public void tick() {
        boolean flag = this.isLit();
        boolean flag1 = false;
        if (this.isLit()) {
            --this.litTime;
        }

        if (!this.level.isClientSide) {
            ItemStack itemstack = this.itemHandler.getStackInSlot(1);
            if (this.isLit() || !this.isAnySlotEmpty()) {
                Inventory currentIngredients = new Inventory(itemHandler.getSlots()-1);
                for (int i = 0; i < itemHandler.getSlots()-1; i++) {
                    currentIngredients.setItem(i, itemHandler.getStackInSlot(i));
                }
                Optional<KalcenitaRefineryRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.REFINERY_RECIPE, currentIngredients, level);

                if (!this.isLit() && this.canBurn(recipe.orElse(null), currentIngredients)) {
                    this.litTime = this.getBurnDuration(itemstack);
                    this.litDuration = this.litTime;
                    if (this.isLit()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.itemHandler.setStackInSlot(1, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty()) {
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                this.itemHandler.setStackInSlot(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isLit() && this.canBurn(recipe.orElse(null), currentIngredients)) {
                    ++this.cookingProgress;
                    if (this.cookingProgress == this.cookingTotalTime) {
                        this.cookingProgress = 0;
                        this.cookingTotalTime = this.getTotalCookTime(currentIngredients);
                        this.burn(recipe.orElse(null), currentIngredients);
                        flag1 = true;
                    }
                } else {
                    this.cookingProgress = 0;
                }
            } else if (!this.isLit() && this.cookingProgress > 0) {
                this.cookingProgress = MathHelper.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
            }

            if (flag != this.isLit()) {
                flag1 = true;
                this.setFueledProp(this.isLit());
            }
        }

        if (flag1) {
            this.setChanged();
        }
    }

    /**
     * Checks if any slot of the itemHandler is empty
     * @return True if any slot is empty
     */
    private boolean isAnySlotEmpty() {
        return isStackEmpty(0) || isStackEmpty(1) || isStackEmpty(2);
    }

    private boolean isStackEmpty(int slot) {
        return this.itemHandler.getStackInSlot(slot).isEmpty();
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    /**
     * Checks if it can output items to the result slot
     * @return True if it can
     */
    protected boolean canBurn(@Nullable IRecipe<?> pRecipe, Inventory inv) {
        if (!isStackEmpty(0) && !isStackEmpty(2) && pRecipe != null) {
            ItemStack itemstack = ((KalcenitaRefineryRecipe) pRecipe).assemble(inv);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.itemHandler.getStackInSlot(3);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= inv.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack pFuel) {
        return pFuel.isEmpty() ? 0 : burnTimes.getOrDefault(pFuel.getItem(), 0);
    }

    protected int getTotalCookTime(Inventory pCurrentIngredients) {
        //TODO Comprobar si coge el valor o si usa el orElse
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.REFINERY_RECIPE, pCurrentIngredients, this.level).map(KalcenitaRefineryRecipe::getCookingTime).orElse(200);
    }

    private void burn(@Nullable IRecipe<?> pRecipe, Inventory pCurrentIngredients) {
        if (pRecipe != null && this.canBurn(pRecipe, pCurrentIngredients)) {
            ItemStack itemstack = this.itemHandler.getStackInSlot(0);
            ItemStack itemstack1 = ((KalcenitaRefineryRecipe) pRecipe).assemble(pCurrentIngredients);
            ItemStack itemstack2 = this.itemHandler.getStackInSlot(2);
            if (itemstack2.isEmpty()) {
                this.itemHandler.setStackInSlot(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(getRawDecrement(pCurrentIngredients));
        }
    }

    private int getRawDecrement(Inventory pCurrentIngredients) {
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.REFINERY_RECIPE, pCurrentIngredients, this.level).map(KalcenitaRefineryRecipe::getDecrement).orElse(200);
    }

    /**
     * If the block has the property FUELED, set the block's FUELED property to the value of pValue
     *
     * @param pValue The value to set the property to.
     */
    private void setFueledProp(boolean pValue) {
        if(getBlockState().hasProperty(KalcenitaRefineryBlock.FUELED)) {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(KalcenitaRefineryBlock.FUELED, pValue), 3);
        }
    }
}
