package ca.lukegrahamlandry.eternalartifacts.leveling;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Consumer;

public class ArtifactXpCapability implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(ArtifactExperience.class)
    public static Capability<ArtifactExperience> CAP;

    private final ArtifactExperienceImpl data;

    public ArtifactXpCapability() {
        data = new ArtifactExperienceImpl();
    }

    public static void ifPresent(PlayerEntity player, Consumer<ArtifactExperience> action){
        player.getCapability(CAP).ifPresent(action::accept);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAP ? LazyOptional.of(() -> data).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return data.write();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        data.read(nbt);
    }

    public static class Storage implements Capability.IStorage<ArtifactExperience> {
        @Override
        public INBT writeNBT(Capability capability, ArtifactExperience instance, Direction side) {
            return instance.write();
        }

        @Override
        public void readNBT(Capability capability, ArtifactExperience instance, Direction side, INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            instance.read(compound);
        }
    }
}