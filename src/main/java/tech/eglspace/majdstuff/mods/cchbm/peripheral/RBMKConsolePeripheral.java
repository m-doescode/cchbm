package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.tileentity.machine.rbmk.TileEntityRBMKConsole;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RBMKConsolePeripheral implements IPeripheral {

    private final TileEntityRBMKConsole tileEntity;

    public RBMKConsolePeripheral(TileEntityRBMKConsole tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Nonnull
    @Override
    public String getType() {
        return "rbmk_console";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[] { "setRods" };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException {
        switch (method) {
            // setRods
            case 0: {
                double level = ArgumentHelper.getNumber(arguments, 0);
                int group = ArgumentHelper.optInt(arguments, 1, -1);

                // Argument checking
                if (level < 0.0D || level > 1.0D)
                    throw new LuaException("Level must be between 0.00 and 1.00");
                if (group != -1 && (group < 0 || group > 4))
                    throw new LuaException("Group must be between 0 and 4 or nil");

                NBTTagCompound controlPacket = new NBTTagCompound();
                controlPacket.setDouble("level", level);

                // Get columns matching group
                for (int col = 0; col < tileEntity.columns.length; col++) {
                    TileEntityRBMKConsole.RBMKColumn colObj = tileEntity.columns[col];
                    if (!(colObj != null && colObj.type == TileEntityRBMKConsole.ColumnType.CONTROL && (group == -1 || colObj.data.getShort("color") == group)))
                        continue;
                    controlPacket.setInteger("sel_" + col, col);
                }

                // Because we are already on the server, no networking is necessary.
                tileEntity.receiveControl(controlPacket);

                return null;
            }
            default:
                return null;
        }
    }

    public boolean equals(@Nullable IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }
}
