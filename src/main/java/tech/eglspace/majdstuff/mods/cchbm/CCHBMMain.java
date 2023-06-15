package tech.eglspace.majdstuff.mods.cchbm;

import com.hbm.tileentity.bomb.TileEntityLaunchPad;
import com.hbm.tileentity.machine.TileEntityReactorControl;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKConsole;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tech.eglspace.majdstuff.mods.cchbm.peripheral.HBMPeripheralProvider;
import tech.eglspace.majdstuff.mods.cchbm.peripheral.LaunchPadPeripheral;
import tech.eglspace.majdstuff.mods.cchbm.peripheral.RBMKConsolePeripheral;
import tech.eglspace.majdstuff.mods.cchbm.peripheral.ReactorControlPeripheral;

@Mod(
        modid = CCHBMMain.MOD_ID,
        name = CCHBMMain.MOD_NAME,
        version = CCHBMMain.VERSION,
        dependencies = "required-after:computercraft@[1.80pr1];required-after:hbm@[hbm-1.8.1A-Gv3,)"
)
public class CCHBMMain {

    public static final String MOD_ID = "cchbm";
    public static final String MOD_NAME = "cchbm";
    public static final String VERSION = "1.0-SNAPSHOT";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @SuppressWarnings("unused")
    @Mod.Instance(MOD_ID)
    public static CCHBMMain INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        HBMPeripheralProvider peripheralProvider = new HBMPeripheralProvider();
        peripheralProvider.register(TileEntityReactorControl.class, ReactorControlPeripheral::new);
        peripheralProvider.register(TileEntityLaunchPad.class, LaunchPadPeripheral::new);
        peripheralProvider.register(TileEntityRBMKConsole.class, RBMKConsolePeripheral::new);
        ComputerCraftAPI.registerPeripheralProvider(peripheralProvider);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }
}
