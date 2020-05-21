package vnj.vnjeconomymod.common;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.client.gui.GuiScreenText;
import vnj.vnjeconomymod.client.gui.hud.CustomerHud;


public class VnjEvents {

//    @SubscribeEvent
//    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
//        Minecraft.getMinecraft().addScheduledTask(() -> {
//            MinecraftForge.EVENT_BUS.register(new A());
//        });
//    }
//
//    static class A {
//        @SubscribeEvent
//        public void a(EntityJoinWorldEvent e) {
//            if (e.getEntity() == null || !(e.getEntity() instanceof EntityPlayer) || !e.getWorld().isRemote) return;
//            if (!VnjUtil.FromApi.isVnjemsLoaded()) return;
//            IUser user = VnjUtil.FromApi.getUserByName(e.getEntity().getName());
//            if (user == null) return;
//            e.getEntity().sendMessage(new TextComponentString(user.getName() + " : " + user.getUuid() + " : " + user.getBalance()));
//        }
//    }

    @SubscribeEvent()
    public void onRenderGui(RenderGameOverlayEvent.Post event){
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        new GuiScreenText(Minecraft.getMinecraft());
        new CustomerHud(Minecraft.getMinecraft());
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(VnjEconomyMod.MOD_ID)) {
            ConfigManager.sync(VnjEconomyMod.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
