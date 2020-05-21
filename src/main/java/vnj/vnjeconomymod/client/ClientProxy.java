/**************************************************************************************************
 * A testmod for testing.                                                                         *
 * Copyright (C) 2017 Jcoud                                                                       *
 *                                                                                                *
 * This program is free software: you can redistribute it and/or modify                           *
 * it under the terms of the GNU General Public License as published by                           *
 * the Free Software Foundation, either version 3 of the License, or                              *
 * (at your option) any later version.                                                            *
 *                                                                                                *
 * This program is distributed in the hope that it will be useful,                                *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                  *
 * GNU General Public License for more details.                                                   *
 *                                                                                                *
 * You should have received a copy of the GNU General Public License                              *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.                          *
 **************************************************************************************************/

package vnj.vnjeconomymod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import vnj.vnjeconomymod.client.render.MarketItemRendering;
import vnj.vnjeconomymod.common.CommonProxy;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof MarketBaseTile) {
            MarketBaseTile tcte = (MarketBaseTile) te;
            return tcte.createGui(tcte, player);
        }
        return null;
    }

    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        for (TypeEnumBlocks type : TypeEnumBlocks.values())
            ClientRegistry.bindTileEntitySpecialRenderer(type.getTileClass(), new MarketItemRendering());
        super.preInit(e);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? mc.player : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx));
    }
}