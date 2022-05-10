package ca.lukegrahamlandry.eternalartifacts.tile;

import ca.lukegrahamlandry.eternalartifacts.registry.TileTypeInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ShellTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {
    public ShellTileEntity() {
        super(TileTypeInit.SHELL.get());
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationEvent<ShellTileEntity> event) {
        if (transitionTick > 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation(open ? "open" : "close", false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(open ? "idle" : "closed", true));
        }

        return PlayState.CONTINUE;
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private static final int range = 5;
    private boolean open = false;
    private int transitionTick = 0;

    @Override
    public void tick() {
        if (this.level != null && this.level.isClientSide()){
            if (transitionTick > 0){
                transitionTick--;
            }

            AxisAlignedBB aabb = new AxisAlignedBB(this.worldPosition).inflate(range);
            boolean playerNear = !this.level.getEntitiesOfClass(PlayerEntity.class, aabb).isEmpty();
            if (this.open != playerNear){
                this.open = playerNear;
                this.transitionTick = this.open ? 30 : 18;
            }
        }
    }
}
