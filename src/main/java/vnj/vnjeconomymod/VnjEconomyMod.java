package vnj.vnjeconomymod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import vnj.vnjeconomymod.common.CommonProxy;
import vnj.vnjeconomymod.common.VnjEvents;
import vnj.vnjeconomymod.common.commands.VnjCommand;
import vnj.vnjeconomymod.common.config.VnjConfig;

import javax.annotation.Nonnull;

@Mod(
        modid = VnjEconomyMod.MOD_ID,
        name = VnjEconomyMod.MOD_NAME,
        version = VnjEconomyMod.VERSION,
        acceptedMinecraftVersions = VnjEconomyMod.ACCEPTED_VERSION
)
public class VnjEconomyMod {
    public static final String MOD_ID = "vnjeconomymod";
    public static final String MOD_NAME = "VnjEconomyMod";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSION = "[1.12.2]";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static @Nonnull VnjEconomyMod INSTANCE;

    @SidedProxy(
        clientSide = "vnj.vnjeconomymod.client.ClientProxy",
        serverSide = "vnj.vnjeconomymod.common.CommonProxy"
    )
    public static CommonProxy proxy;
    public static CreativeTabs vnjtab;
    public static Logger logger;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(new VnjEvents());
        proxy.preInit(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, proxy);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        VnjConfig.init();
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new VnjCommand());
    }



}
