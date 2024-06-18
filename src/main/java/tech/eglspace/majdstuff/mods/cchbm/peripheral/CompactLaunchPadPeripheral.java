package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.CompactLauncher;
import com.hbm.items.tool.ItemDesignator;
import com.hbm.items.tool.ItemDesignatorManual;
import com.hbm.items.tool.ItemDesignatorRange;
import com.hbm.tileentity.bomb.TileEntityCompactLauncher;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompactLaunchPadPeripheral implements IPeripheral {
    private final TileEntityCompactLauncher tileEntity;

    public CompactLaunchPadPeripheral(TileEntityCompactLauncher tileEntity) {

        this.tileEntity = tileEntity;
    }
    @Nonnull
    @Override
    public String getType() {
        return "compact_launchpad";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[] { "launch", "getDesignatorPos", "setDesignatorPos", "isDesignatorPresent", "isManualDesignator", "power", "maxPower", "isMissilePresent", "solidFuel", "maxSolidFuel", "tankType", "tankCapacity", "tankContent" };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException {
        switch (method) {
            // launch
            case 0: {
                if (!isMissilePresent())
                    throw new LuaException("No missile in launch pad");
                if (!isDesignatorPresent())
                    throw new LuaException("No designator in launch pad");
                CompactLauncher launchPad = (CompactLauncher) ModBlocks.compact_launcher;
                launchPad.explode(tileEntity.getWorld(), tileEntity.getPos());
                return null;
            }
            // getDesignatorPos
            case 1: {
                ItemStack designatorItem = tileEntity.inventory.getStackInSlot(1);
                if (!isDesignatorPresent())
                    throw new LuaException("No designator in launch pad");

                NBTTagCompound tag = designatorItem.getTagCompound();
                if (tag == null)
                    return null;

                return new Object[]{tag.getInteger("xCoord"), tag.getInteger("zCoord")};
            }
            // setDesignatorPos
            case 2: {
                int xCoord = ArgumentHelper.getInt(arguments, 0);
                int zCoord = ArgumentHelper.getInt(arguments, 1);

                ItemStack designatorItem = tileEntity.inventory.getStackInSlot(1);
                if (!isManualDesignatorPresent())
                    throw new LuaException("No manual designator in launch pad");

                NBTTagCompound tag = designatorItem.getTagCompound();
                if (tag == null)
                    designatorItem.setTagCompound(tag = new NBTTagCompound());

                tag.setInteger("xCoord", xCoord);
                tag.setInteger("zCoord", zCoord);

                return null;
            }
            // isDesignatorPresent
            case 3: {
                return new Object[] { isDesignatorPresent() };
            }
            // isManualDesignator
            case 4: {
                return new Object[] { isManualDesignatorPresent() };
            }
            // getPower
            case 5: {
                return new Object[] { tileEntity.power };
            }
            // getMaxPower
            case 6: {
                return new Object[] { TileEntityCompactLauncher.maxPower };
            }
            // isMissilePresent
            case 7: {
                return new Object[] { isMissilePresent() };
            }
            // getSolidFuel
            case 8: {
                return new Object[] { tileEntity.solid };
            }
            // getMaxSolidFuel
            case 9: {
                return new Object[] { TileEntityCompactLauncher.maxSolid };
            }
            //"getTankType", "getTankCapacity", "getTankContent"
            // getTankType
            case 10: {
                int idx = ArgumentHelper.getInt(arguments, 0);
                if (idx < 0 || idx > 1)
                    throw new LuaException("Tank index is out of range. Expected between 0 and 1, got " + idx);

                if (tileEntity.tankTypes[idx] == null)
                    return new Object[] { null };

                return new Object[] { tileEntity.tankTypes[idx].getName() };
            }
            // getTankCapacity
            case 11: {
                int idx = ArgumentHelper.getInt(arguments, 0);
                if (idx < 0 || idx > 1)
                    throw new LuaException("Tank index is out of range. Expected between 0 and 1, got " + idx);

                if (tileEntity.tanks[idx] == null)
                    return new Object[] { null };

                return new Object[] { tileEntity.tanks[idx].getCapacity() };
            }
            // getTankContent
            case 12: {
                int idx = ArgumentHelper.getInt(arguments, 0);
                if (idx < 0 || idx > 1)
                    throw new LuaException("Tank index is out of range. Expected between 0 and 1, got " + idx);

                if (tileEntity.tanks[idx] == null || tileEntity.tanks[idx].getFluid() == null)
                    return new Object[] { null };

                // Stupid intellij....
                //noinspection DataFlowIssue
                return new Object[] { tileEntity.tanks[idx].getFluid().amount };
            }
            default:
                return null;
        }
    }

    private static boolean isDesignator(Item item) {
        return item instanceof ItemDesignator || item instanceof ItemDesignatorManual || item instanceof ItemDesignatorRange;
    }

    private boolean isDesignatorPresent() {
        ItemStack item = tileEntity.inventory.getStackInSlot(1);
        return !item.isEmpty() && isDesignator(item.getItem());
    }

    private boolean isManualDesignatorPresent() {
        ItemStack item = tileEntity.inventory.getStackInSlot(1);
        return !item.isEmpty() && item.getItem() instanceof ItemDesignatorManual;
    }

    // Very hacky solution, but there unfortunately is no other way.
    @SuppressWarnings({"ConstantConditions"})
    private static boolean isMissile(Item item) {
        return item != com.hbm.items.ModItems.missile_assembly && item.getRegistryName().getPath().startsWith("missile_");
    }

    private boolean isMissilePresent() {
        ItemStack item = tileEntity.inventory.getStackInSlot(0);
        return !item.isEmpty() && isMissile(item.getItem());
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }
}
