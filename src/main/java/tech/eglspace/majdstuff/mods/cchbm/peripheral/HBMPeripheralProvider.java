package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class HBMPeripheralProvider implements IPeripheralProvider {
    private static final Map<Class<? extends TileEntity>, PeripheralGenerator<?, ?>> generatorMap = new HashMap<>();

    public <T extends TileEntity, P extends IPeripheral> void register(Class<T> tileEntityClass, PeripheralGenerator<P, T> peripheralGenerator) {
        generatorMap.put(tileEntityClass, peripheralGenerator);
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity == null)
            return null;
        Class<? extends TileEntity> tileEntityClass = tileEntity.getClass();
        PeripheralGenerator<?, ?> generator = generatorMap.get(tileEntityClass);
        if (generator == null)
            return null;
        //noinspection unchecked
        return ((PeripheralGenerator<?, TileEntity>)generator).generate(tileEntity);
    }

    public interface PeripheralGenerator<P extends IPeripheral, T extends TileEntity> {
        P generate(T tileEntity);
    }
}
