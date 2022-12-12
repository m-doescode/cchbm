package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.machine.TileEntityReactorControl;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReactorControlPeripheral implements IPeripheral {

    private final TileEntityReactorControl tileEntity;

    public ReactorControlPeripheral(TileEntityReactorControl tileEntity) {

        this.tileEntity = tileEntity;
    }

    @Nonnull
    @Override
    public String getType() {
        return "reactor_control";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[] {"isOn","setOn","isAuto","setAuto","getCompression","setCompression",
                "isLinked","getHullHeat","getCoreHeat","getFuel","getWater","getCool", "getSteam",
                "getMaxWater","getMaxCool","getMaxSteam",
                "getRods","getMaxRods"};
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
        switch (method) {
            // isOn
            case 0: {
                return context.executeMainThreadTask(() ->
                        new Object[]{tileEntity.isOn}
                );
            }
            // setOn
            case 1: {
                boolean on = ArgumentHelper.getBoolean(arguments, 0);
                context.issueMainThreadTask(() -> {
                    sendButtonAction(on ? 1 : 0, 0);
                    return null;
                });
                return null;
            }
            // isAuto
            case 2: {
                return context.executeMainThreadTask(() ->
                        new Object[]{tileEntity.auto}
                );
            }
            // setAuto
            case 3: {
                boolean auto = ArgumentHelper.getBoolean(arguments, 0);
                context.issueMainThreadTask(() -> {
                    sendButtonAction(auto ? 1 : 0, 1);
                    return null;
                });
                return null;
            }
            // getCompression
            case 4: {
                return context.executeMainThreadTask(() ->
                        new Object[]{tileEntity.compression}
                );
            }
            // setCompression
            case 5: {
                int compression = ArgumentHelper.getInt(arguments, 0);
                if (compression < 0 || compression > 2)
                    throw new LuaException("Compression must be between 0 and 2");
                context.issueMainThreadTask(() -> {
                    sendButtonAction(compression, 1);
                    return null;
                });
                return null;
            }
            // isLinked
            case 6: {
                return context.executeMainThreadTask(() ->
                        new Object[]{tileEntity.isLinked}
                );
            }
            default: {
                if (method >= getMethodNames().length)
                    return null;
                int value;
                switch (method) {
                    // getHullHeat
                    case 7:
                        value = tileEntity.hullHeat;
                        break;
                    // getCoreHeat
                    case 8:
                        value = tileEntity.coreHeat;
                        break;
                    // getFuel
                    case 9:
                        value = tileEntity.fuel;
                        break;
                    // getWater
                    case 10:
                        value = tileEntity.water;
                        break;
                    // getCool
                    case 11:
                        value = tileEntity.cool;
                        break;
                    // getSteam
                    case 12:
                        value = tileEntity.steam;
                        break;
                    // getMaxWater
                    case 13:
                        value = tileEntity.maxWater;
                        break;
                    // getMaxCool
                    case 14:
                        value = tileEntity.maxCool;
                        break;
                    // getMaxSteam
                    case 15:
                        value = tileEntity.maxSteam;
                        break;
                    // getRods
                    case 16:
                        value = tileEntity.rods;
                        break;
                    // getMaxRods
                    case 17:
                        value = tileEntity.maxRods;
                        break;
                    default:
                        return null;
                }

                return context.executeMainThreadTask(() ->
                        new Object[]{value}
                );
            }
        }
    }

    private void sendButtonAction(int value, int btnId) {
        BlockPos pos = tileEntity.getPos();
        PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(pos.getX(), pos.getY(), pos.getZ(), value, btnId));
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }
}
