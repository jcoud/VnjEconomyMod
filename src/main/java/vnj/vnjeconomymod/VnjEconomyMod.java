package vnj.vnjeconomymod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import vnj.vnjeconomymod.objects.screen.RenderGuiHandler;
import vnj.vnjeconomymod.proxy.CommonProxy;
import org.apache.logging.log4j.Logger;


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
    public static VnjEconomyMod INSTANCE;

    @SidedProxy(
        clientSide = "vnj.vnjeconomymod.proxy.ClientProxy",
        serverSide = "vnj.vnjeconomymod.proxy.ServerProxy"
    )
    public static CommonProxy proxy;

    public static Logger logger;
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
        proxy.postInit(event);
    }
}
