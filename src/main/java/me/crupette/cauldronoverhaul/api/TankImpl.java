package me.crupette.cauldronoverhaul.api;

import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.math.Fraction;

public interface TankImpl extends Tank{
    DefaultedList<FluidSlot> getVolumes();

    static TankImpl of(DefaultedList<FluidSlot> volumes){
        return () -> volumes;
    }

    @Override
    default int getSize(){
        return getVolumes().size();
    }

    @Override
    default FluidSlot getSlot(int slot){
        return getVolumes().get(slot);
    }

    @Override
    default void setSlot(int slot, FluidSlot volume){
        getVolumes().set(slot, volume);
    }
}
