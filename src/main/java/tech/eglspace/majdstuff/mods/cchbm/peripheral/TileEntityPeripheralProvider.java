package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityPeripheralProvider<P extends IPeripheral,T extends TileEntity> implements IPeripheralProvider {
    private final Class<T> tileEntityClass;
    private final PeripheralProviderGenerator<P,T> generator;

    public TileEntityPeripheralProvider(Class<T> tileEntityClass, PeripheralProviderGenerator<P,T> generator) {
        this.tileEntityClass = tileEntityClass;
        this.generator = generator;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity genericTileEntity = world.getTileEntity(blockPos);
        if (tileEntityClass.isInstance(genericTileEntity))
            return generator.generate((T) genericTileEntity);
        else
            return null;
    }

    public interface PeripheralProviderGenerator<P extends IPeripheral,T extends TileEntity> {
        P generate(T tileEntity);
    }
}
