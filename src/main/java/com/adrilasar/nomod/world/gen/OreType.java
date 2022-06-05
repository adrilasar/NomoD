package com.adrilasar.nomod.world.gen;

import com.adrilasar.nomod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum OreType
{
    KALCENITA(Lazy.of(ModBlocks.KALCENITA_ORE), 0,14,8);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;

    OreType(Lazy<Block> block, int minHeight, int maxHeight, int maxVeinSize) {
        this.block = block;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.maxVeinSize = maxVeinSize;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public static OreType getOreType(Block block) {
        for (OreType oreType: values()) {
            if (oreType.getBlock().get() == block) {
                return oreType;
            }
        }
        return null;
    }
}
