package vnj.vnjeconomymod.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.client.gui.hud.CustomerHud;

public class VnjConfig {
    public static void init() {
        ConfigManager.sync(VnjEconomyMod.MOD_ID, Type.INSTANCE);
    }
    @LangKey("config.hud_options_category.name")
    @Config(modid = VnjEconomyMod.MOD_ID, type = Type.INSTANCE, name = VnjEconomyMod.MOD_ID, category = "hud")
    public static class HUD_OPTIONS {
        @Config.Comment({
                "Indexes:",
                "0 - CORNER_BOTTOM_LEFT",
                "1 - CENTER_LEFT",
                "2 - CORNER_TOP_LEFT",
                "3 - CENTER_TOP"
        })
        @LangKey("config.hud_options_category.hud_enum_pos.name")
        @Config.RangeInt(min = 0, max = 3)
        public static int hud_enum_pos = CustomerHud.HudPosType.CENTER_LEFT.ordinal();
        @LangKey("config.hud_options_category.useCustomPos.name")
        @Config.Comment("If you want to use custom position, set \"useCustomPos\" to true and then change values \"posX\" and/or \"posY\"")
        public static boolean useCustomPos = false;
        @LangKey("config.hud_options_category.posX.name")
        @Config.Comment("Custom position X")
        @Config.RangeInt(min = 0)
        public static int posX = 0;
        @LangKey("config.hud_options_category.posY.name")
        @Config.Comment("Custom position Y")
        @Config.RangeInt(min = 0)
        public static int posY = 0;
    }
}
