package me.crupette.cauldronoverhaul.api;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.types.templates.List;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.math.Fraction;

public interface Tank {
    int getSize();

    FluidSlot getSlot(int slot);
    void setSlot(int slot, FluidSlot volume);

    static CompoundTag toTag(Tank tank, CompoundTag tag){
        ListTag listTag = new ListTag();
        for(int i = 0; i < tank.getSize(); i++){
            CompoundTag slotTag = new CompoundTag();
            FluidSlot slot = tank.getSlot(i);
            slotTag.putString("fluid", Registry.FLUID.getId(slot.volume.getFluid()).toString());
            slotTag.putString("volume", slot.volume.getCount().toProperString());
            slotTag.putString("capacity", slot.capacity.toProperString());

            listTag.add(i, slotTag);
        }
        CompoundTag tankTag = new CompoundTag();
        tankTag.put("contents", listTag);
        tag.put("tank", tankTag);
        return tag;
    }

    static void fromTag(Tank tank, CompoundTag tag){
        CompoundTag tankTag = tag.getCompound("tank");
        ListTag contentsTag = tankTag.getList("contents", 10);

        int i = 0;
        contentsTag.forEach(content -> {
            CompoundTag contentCompound = (CompoundTag)content;
            Fluid fluid = Registry.FLUID.get(new Identifier(contentCompound.getString("fluid")));
            Fraction volume = Fraction.getFraction(contentCompound.getString("volume"));
            Fraction capacity = Fraction.getFraction(contentCompound.getString("capacity"));

            tank.setSlot(i, new FluidSlot(new FluidVolume(fluid, volume), capacity));
        });
    }

    class FluidSlot {
        private FluidVolume volume;
        private final Fraction capacity;

        public FluidSlot(FluidVolume volume, Fraction capacity){
            this.capacity = capacity;
            this.volume = volume;
        }

        public FluidVolume getVolume() { return this.volume; }
        public Fraction getCapacity() { return this.capacity; }

        public FluidVolume insert(FluidVolume volume, boolean simulate){
            if(this.volume.getFluid() != Fluids.EMPTY && !this.volume.getCount().equals(Fraction.ZERO)) {
                if (volume.getFluid() != this.volume.getFluid()) return volume;
            }
            if(this.volume.getCount().equals(this.capacity)) return volume;
            Fraction resultFraction = this.volume.getCount().add(volume.getCount());
            if(resultFraction.compareTo(this.capacity) > 0.f){
                Fraction leftoverFraction = resultFraction.subtract(this.capacity);
                FluidVolume leftover = new FluidVolume(volume.getFluid(), volume.getCount().subtract(leftoverFraction));
                if(!simulate) this.volume.setCount(this.capacity);
                return leftover;
            }
            if(!simulate) {
                this.volume.setCount(resultFraction);
                this.volume.setFluid(volume.getFluid());
            }
            return FluidVolume.EMPTY;
        }

        public FluidVolume extract(FluidVolume volume, boolean simulate){
            if(this.volume.getCount().equals(Fraction.ZERO) || this.volume.getFluid() == Fluids.EMPTY) return  FluidVolume.EMPTY;
            if(volume.getFluid() != this.volume.getFluid()) return volume;
            Fraction resultFraction = this.volume.getCount().subtract(volume.getCount());
            if(resultFraction.floatValue() < 0.f){
                FluidVolume taken = new FluidVolume(this.volume.getFluid(), this.volume.getCount());
                if(!simulate) this.volume.setCount(Fraction.ZERO);
                return taken;
            }
            if(!simulate) this.volume.setCount(resultFraction);
            return volume;
        }
    }
}
