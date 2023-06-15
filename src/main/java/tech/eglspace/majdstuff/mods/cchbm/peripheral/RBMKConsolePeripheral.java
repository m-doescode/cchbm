package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.tileentity.machine.rbmk.TileEntityRBMKConsole;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;
import net.minecraft.nbt.NBTTagCompound;
import tech.eglspace.majdstuff.mods.cchbm.LuaHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

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
        return new String[] { "setRods", "setRod", "getColumnInfo", "getAllColumnInfo" };
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
            // setRod
            case 1: {
                // 1 Arg mode: column by index
                // 2 Arg mode: column by position (x, z)

                double level = ArgumentHelper.getNumber(arguments, 0);
                int index = getIndexFromPosition(arguments);

                NBTTagCompound controlPacket = new NBTTagCompound();
                controlPacket.setDouble("level", level);
                controlPacket.setInteger("sel_" + index, index);

                tileEntity.receiveControl(controlPacket);
            }
            // getColumnInfo
            case 2: {
                // 1 Arg mode: column by index
                // 2 Arg mode: column by position (x, z)

                int index = getIndexFromPosition(arguments);
                TileEntityRBMKConsole.RBMKColumn columnObject = tileEntity.columns[index];

                if (columnObject == null)
                    return null;

                // TODO: Hideous, really hacky. Fix it next time.
                @SuppressWarnings("unchecked") Map<String, Object> infoTable = (Map<String, Object>) LuaHelper.convertNbtToCC(columnObject.data);
                //noinspection DataFlowIssue
                infoTable.put("columnType", columnObject.type.name().toLowerCase(Locale.ROOT));

                return new Object[] { infoTable };
            }
            // getAllColumnInfo
            case 3: {
                // Modes:
                // "index" (default) - [1 - 256] map by index of all columns
                // "index0" - [0 - 255] map by index of all columns
                // "pos" - (x, y) [1 - 16][1 - 16] map by position of all columns
                // "pos0" - (x, y) [0 - 15][0 - 15] map by position of all columns

                String mode = ArgumentHelper.optString(arguments, 0, "index");

                boolean startZero = false;

                switch (mode) {
                    case "index0":
                        startZero = true;
                    case "index":
                        List<Map<String, ?>> columnInfo = new ArrayList<>();

                        for (int col = 0; col < tileEntity.columns.length; col++) {
                            TileEntityRBMKConsole.RBMKColumn colObj = tileEntity.columns[col];
                            if (colObj == null)
                                continue;
                            //noinspection unchecked
                            columnInfo.add((Map<String, ?>) LuaHelper.convertNbtToCC(colObj.data));
                        }

                        return new Object[] { LuaHelper.mapify(columnInfo, startZero) };
                    case "pos0":
                        startZero = true;
                    case "pos":
                        Map<Integer, Map<Integer, Map<String, ?>>> columnMap = new HashMap<>();

                        for (int x = 0; x <= 15; x++) {
                            Map<Integer, Map<String, ?>> xMap = new HashMap<>();
                            columnMap.put(startZero ? x : x + 1, xMap);
                            for (int z = 0; z <= 15; z++) {
                                int col = x + z * 15;

                                TileEntityRBMKConsole.RBMKColumn colObj = tileEntity.columns[col];
                                if (colObj == null)
                                    continue;
                                //noinspection unchecked
                                xMap.put(startZero ? z : z + 1, (Map<String, ?>) LuaHelper.convertNbtToCC(colObj.data));
                            }
                        }
                    default:
                        throw new LuaException("Unexpected mode. Must be one of: 'index', 'index0', 'pos', 'pos0'");
                }
            }
            default:
                return null;
        }
    }

    public boolean equals(@Nullable IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }

    private static int getIndexFromPosition(Object[] arguments) throws LuaException {
        int x = ArgumentHelper.getInt(arguments, 0);
        int z = ArgumentHelper.optInt(arguments, 1, -1);

        // Argument checking
        if (z == -1) {
            if (x < 0 || x > 255)
                throw new LuaException("Index argument must be between 0 and 255");
        } else {
            if (x < 0 || x > 15)
                throw new LuaException("X argument must be between 0 and 15");
            if (z < 0 || z > 15)
                throw new LuaException("Z argument must be between 0 and 15");
        }


        if (z == -1) return x;
        else return x + z * 15;
    }
}
