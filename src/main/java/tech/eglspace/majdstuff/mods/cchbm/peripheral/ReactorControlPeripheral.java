package tech.eglspace.majdstuff.mods.cchbm.peripheral;

import com.hbm.tileentity.machine.TileEntityReactorControl;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;

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
        return new String[] {"isOn","setOn","isAuto","setAuto"};
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
                    //PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(this.control.getPos().getX(), this.control.getPos().getY(), this.control.getPos().getZ(), this.control.isOn ? 0 : 1, 0));
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
                    //PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(this.control.getPos().getX(), this.control.getPos().getY(), this.control.getPos().getZ(), this.control.isOn ? 0 : 1, 0));
                    return null;
                });
                return null;
            }
            default:
                return null;
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
