package me.crupette.cauldronoverhaul.api;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.math.Fraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FluidVolume {
    public static final FluidVolume EMPTY = new FluidVolume(Fluids.EMPTY, Fraction.ZERO);

    private static final Logger LOGGER = LogManager.getLogger();

    private Fraction count;
    private Fluid fluid;

    public FluidVolume(Fluid fluid, Fraction count){
        this.fluid = fluid;
        this.count = count;
    }

    public FluidVolume(Fluid fluid){
        this(fluid, Fraction.ONE);
    }

    public FluidVolume(CompoundTag tag){
        this.fluid = Registry.FLUID.get(new Identifier(tag.getString("id")));
        this.count = Fraction.getFraction(tag.getString("count"));
    }

    public static FluidVolume fromTag(CompoundTag tag) {
        try {
            return new FluidVolume(tag);
        } catch (RuntimeException var2) {
            LOGGER.debug("Tried to load invalid item: {}", tag, var2);
            return FluidVolume.EMPTY;
        }
    }

    public CompoundTag toTag(CompoundTag tag){
        Identifier fluidId = Registry.FLUID.getId(this.fluid);
        tag.putString("id", fluidId.toString());
        tag.putString("count", this.count.toProperString());
        return tag;
    }

    public Fraction getCount() {
        return count;
    }
    public void setCount(Fraction count) { this.count = count; }

    public Fluid getFluid() {
        return this.fluid;
    }
    public void setFluid(Fluid fluid) { this.fluid = fluid; }

    public boolean isEmpty() {
        return this.fluid == Fluids.EMPTY || this.count.floatValue() == 0.f;
    }

}
