package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.blocks.ModBlocks;
import com.hbm.tileentity.bomb.TileEntityCompactLauncher;
import com.hbm.tileentity.machine.TileEntityDummy;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
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

        // Exceptional handling for compact launcher
        if (Arrays.asList(ModBlocks.compact_launcher, ModBlocks.dummy_plate_compact_launcher, ModBlocks.dummy_port_compact_launcher).contains(world.getBlockState(blockPos).getBlock())) {
            TileEntityDummy tileEntityDummy = (TileEntityDummy)tileEntity;
            TileEntity targetTileEntity = world.getTileEntity(tileEntityDummy.target);
            if (targetTileEntity == null) {
                System.out.println("Launcher at position " + tileEntityDummy.target.toString() + " could not be found!");
                return null;
            }

            return new CompactLaunchPadPeripheral((TileEntityCompactLauncher) targetTileEntity);
        }

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
