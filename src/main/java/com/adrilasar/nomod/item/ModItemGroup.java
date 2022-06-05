package com.adrilasar.nomod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup NOMOD_TAB = new ItemGroup("nomodtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.NOMOD_CHARM.get());
        }
    };
}
