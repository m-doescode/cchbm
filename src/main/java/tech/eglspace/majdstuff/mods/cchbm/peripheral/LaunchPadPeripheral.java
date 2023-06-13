package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.blocks.bomb.LaunchPad;
import com.hbm.items.tool.ItemDesignator;
import com.hbm.items.tool.ItemDesignatorManual;
import com.hbm.items.tool.ItemDesignatorRange;
import com.hbm.tileentity.bomb.TileEntityLaunchPad;
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

public class LaunchPadPeripheral implements IPeripheral {
    private final TileEntityLaunchPad tileEntity;

    public LaunchPadPeripheral(TileEntityLaunchPad tileEntity) {

        this.tileEntity = tileEntity;
    }
    @Nonnull
    @Override
    public String getType() {
        return "launchpad";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[] { "launch", "getDesignatorPos", "setDesignatorPos", "isDesignatorPresent", "power", "maxPower", "isMissilePresent" };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException {
        switch (method) {
            // launch
            case 0: {
                LaunchPad launchPad = (LaunchPad) com.hbm.blocks.ModBlocks.launch_pad;
                launchPad.explode(tileEntity.getWorld(), tileEntity.getPos());
                return null;
            }
            // getDesignatorPos
            case 1: {
                ItemStack designatorItem = tileEntity.inventory.getStackInSlot(1);
                if (designatorItem.isEmpty() || !isDesignator(designatorItem.getItem()))
                    return null;

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
                if (designatorItem.isEmpty() || !isDesignator(designatorItem.getItem()))
                    return null;

                NBTTagCompound tag = designatorItem.getTagCompound();
                if (tag == null)
                    designatorItem.setTagCompound(tag = new NBTTagCompound());

                tag.setInteger("xCoord", xCoord);
                tag.setInteger("zCoord", zCoord);
            }
            // isDesignatorPresent
            case 3: {
                ItemStack item = tileEntity.inventory.getStackInSlot(1);
                return new Object[] { !item.isEmpty() && isDesignator(item.getItem()) };
            }
            // getPower
            case 4: {
                return new Object[] { tileEntity.power };
            }
            // getMaxPower
            case 5: {
                return new Object[] { tileEntity.maxPower };
            }
            // isMissilePresent
            case 6: {
                ItemStack item = tileEntity.inventory.getStackInSlot(0);
                return new Object[] { !item.isEmpty() && isMissile(item.getItem()) };
            }
            default:
                return null;
        }
    }

    private static boolean isDesignator(Item item) {
        return item instanceof ItemDesignator || item instanceof ItemDesignatorManual || item instanceof ItemDesignatorRange;
    }

    // Very hacky solution, but there unfortunately is no other way.
    @SuppressWarnings({"ConstantConditions"})
    private static boolean isMissile(Item item) {
        return item != com.hbm.items.ModItems.missile_assembly && item.getRegistryName().getPath().startsWith("missile_");
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }
}
