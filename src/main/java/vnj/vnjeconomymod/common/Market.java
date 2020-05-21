package vnj.vnjeconomymod.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.network.PacketDispatcher;
import vnj.vnjeconomymod.network.server.MsgVnjemsUserRequests;
import vnj.vnjeconomymod.utils.ItemStackUtil;
import vnj.vnjeconomymod.utils.VnjUtil;

import javax.annotation.Nonnull;

public class Market {
    public static boolean customerClicked(EntityPlayer player, MarketBaseTile marketTile) {
        PacketDispatcher.sendToServer(new MsgVnjemsUserRequests(5L));
        switch (marketTile.getMarketBlockType()) {
            case BUYER:
                return buy(player, marketTile);
            case SELLER:
                return sell(player, marketTile);
            case TRADER:
                return trade(player, marketTile);
        }
        return false;
    }
    private static boolean buy(EntityPlayer player, MarketBaseTile marketTile) {
        ItemStack iw = marketTile.getDisplayInv().getItemWant();
        if (iw.isEmpty())
            return false;
        int req = iw.getCount();
        if (!ItemStackUtil.isIdenticalItem(player.getHeldItemMainhand(), iw)) return false;
        return transfer(new PlayerMainInvWrapper(player.inventory), marketTile.getInventoryOutput(), iw);
    }

    private static boolean sell(EntityPlayer player, MarketBaseTile marketTile) {
        ItemStack ih = marketTile.getDisplayInv().getItemHave();
        if (ih.isEmpty()) return false;
        int ret = ih.getCount();
        return transfer(marketTile.getInventoryInput(), new PlayerMainInvWrapper(player.inventory), ih);
    }

    private static boolean trade(EntityPlayer player, MarketBaseTile marketTile) {
        ItemStack ih = marketTile.getDisplayInv().getItemHave();
        ItemStack iw = marketTile.getDisplayInv().getItemWant();
        ItemStack ip = player.getHeldItemMainhand();
        if (iw.isEmpty() || ih.isEmpty()) return false;
        if (!ItemStackUtil.isIdenticalItem(player.getHeldItemMainhand(), iw)) return false;
        int req = iw.getCount();
        int ret = ih.getCount();

        boolean b1 = transfer((IItemHandlerModifiable)VnjUtil.makeCopy(marketTile.getInventoryInput()), (IItemHandlerModifiable)VnjUtil.makeCopy(new PlayerMainInvWrapper(player.inventory)), ih);
        boolean b2 = transfer((IItemHandlerModifiable)VnjUtil.makeCopy(new PlayerMainInvWrapper(player.inventory)), (IItemHandlerModifiable)VnjUtil.makeCopy(marketTile.getInventoryOutput()), iw);
        if (b1 && b2) {
            transfer(marketTile.getInventoryInput(), new PlayerMainInvWrapper(player.inventory), ih);
            transfer(new PlayerMainInvWrapper(player.inventory), marketTile.getInventoryOutput(), iw);
            return true;
        }
        return false;
    }

    private static boolean transfer(IItemHandlerModifiable invFrom, IItemHandlerModifiable invTo, ItemStack item) {
        if (!VnjUtil.isItemStackSameInContent(invFrom, item)) return false;
        IItemHandler invCopy = VnjUtil.makeCopy(invFrom);
        ItemStack stack = extractItemFromInv(invCopy, item);
        if (stack.isEmpty()) return false;
        if (stack.getCount() != item.getCount()) return false;
        if (insertToInv(invTo, stack)) {
            extractItemFromInv(invFrom, item);
            return true;
        }
        return false;
    }



    private static ItemStack extractItemFromInv(IItemHandler inventory, ItemStack item) {
        ItemStack stack = ItemStack.EMPTY;
        int count = item.getCount();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) continue;
            if (!ItemStackUtil.isIdenticalItem(inventory.getStackInSlot(i), item)) continue;
            stack = inventory.extractItem(i, count, false);
            if (stack.isEmpty()) break;
            if (stack.getCount() < item.getCount()) {
                count -= stack.getCount();
                if (count == 0) return item.copy();
                continue;
            }
            break;
        }
        return stack;
    }

    private static boolean insertToInv(IItemHandler inventoryTo, ItemStack stack) {
        return insertItemStacked(inventoryTo, stack, false).isEmpty();
    }

    @Nonnull
    public static ItemStack insertItemStacked(IItemHandler inventory, @Nonnull ItemStack stack, boolean simulate) {
        if (inventory == null || stack.isEmpty())
            return stack;

        // not stackable -> just insert into a new slot
        if (!stack.isStackable()) {
            return ItemHandlerHelper.insertItem(inventory, stack, simulate);
        }

        int sizeInventory = inventory.getSlots();

        // go through the inventory and try to fill up already existing items
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStackInSlot(i);
//            if (ItemStackUtil.isIdenticalItem(slot, stack)) {
            stack = insertItem((IItemHandlerModifiable) inventory, i, stack, simulate);

            if (stack.isEmpty()) {
                break;
            }
//            }
        }

        // insert remainder into empty slots
        if (!stack.isEmpty()) {
            // find empty slot
            for (int i = 0; i < sizeInventory; i++) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    stack = insertItem((IItemHandlerModifiable) inventory, i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }
    private static ItemStack insertItem(IItemHandlerModifiable inv, int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack existing = inv.getStackInSlot(slot);

        int limit = Math.min(inv.getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                inv.setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
//            ((InvHelper) inv).source.markDirty();
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
    }
}
