package vnj.vnjeconomymod.objects;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vnj.vnjeconomymod.objects.ingame.blocks.testcotainer.TestContainerBlock;

public class BlockHolder {
    @GameRegistry.ObjectHolder("vnjeconomymod:testcontainerblock")
    public static TestContainerBlock testContainerBlock;
//    @SideOnly(Side.CLIENT)
//    public static void initModels(){
//        testContainerBlock.initModel();
//    }
}
