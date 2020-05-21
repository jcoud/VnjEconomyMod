package vnj.vnjeconomymod.common.tileEntities;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;
import vnj.vnjeconomymod.common.container.ContainerBase;
import vnj.vnjeconomymod.common.gui.GuiContainerBase;
import vnj.vnjeconomymod.common.inventory.DisplayOnlyInv;
import vnj.vnjeconomymod.common.inventory.InvHelper;
import vnj.vnjeconomymod.common.inventory.MarketInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public abstract class MarketBaseTile extends TileEntity implements MarketTEMethods {
    public static final String NBT_KEY_OWNER = "owner";
    public static final String NBT_KEY_PRICE = "price";
    public static final String NBT_KEY_INVOUT = "_invOut";
    public static final String NBT_KEY_INVIN = "_intIn";

    private InvHelper fakeInv, inventoryInput, inventoryOutput;
    private long price;
    private String owner;
    private TypeEnumBlocks type;

    public MarketBaseTile(TypeEnumBlocks type) {
        this.type = type;
        price = 0;
        owner = "";

        switch (type) {
            case BUYER:
                inventoryOutput = new MarketInventory(this, 18, type.getName(), false);
                break;
            case SELLER:
                inventoryInput = new MarketInventory(this, 18, type.getName(), true);
                break;
            case TRADER:
                inventoryInput = new MarketInventory(this, 18, type.getName(), true);
                inventoryOutput = new MarketInventory(this, 18, type.getName(), false);
                break;
        }
        fakeInv = new DisplayOnlyInv(this, 2, "display_" + type.getName());
    }
    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public TypeEnumBlocks getMarketBlockType() {return type;}


    @Override public void setPrice(long l) {price = l; markDirty();}
    @Override public void setOwner(String ownerName) {owner = ownerName; markDirty();}

    @Override public String getOwner() {return owner;}
    @Override public long getPrice() {return price;}
    @Override public DisplayOnlyInv getDisplayInv() {return (DisplayOnlyInv) fakeInv;}
    @Override public MarketInventory getInventoryInput() {return (MarketInventory) inventoryInput;}
    @Override public MarketInventory getInventoryOutput() {return (MarketInventory) inventoryOutput;}

    @Override
    public ContainerBase createContainer(MarketBaseTile tile, EntityPlayer player) {
        return new ContainerBase(tile, player);
    }

    @Override
    public GuiContainer createGui(MarketBaseTile tile, EntityPlayer player) {
        return new GuiContainerBase(tile, player);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);

    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (type) {
                case BUYER: return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryOutput);
                case SELLER: return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryInput);
                case TRADER: {
                    if (facing == EnumFacing.DOWN)
                        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryOutput);
                    else
                        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryInput);
                }
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = super.getUpdateTag();
        writeToNBT(updateTag);
        return updateTag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new SPacketUpdateTileEntity(pos, 1, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(writeInternalData(compound));
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        readInternalData(compound);
        super.readFromNBT(compound);
    }

    /* NBT read & write internal methods */

    /* NBT write */

    private NBTTagCompound writeInternalData(NBTTagCompound compound) {
        if (compound == null) compound = new NBTTagCompound();
        compound.setString(NBT_KEY_OWNER, owner);
        compound.setLong(NBT_KEY_PRICE, getPrice());
        return writeInvData(compound);
    }

    private NBTTagCompound writeInvData(NBTTagCompound nbttagcompound){
        if (inventoryInput != null) {
            nbttagcompound.setTag(inventoryInput.getInvName() + NBT_KEY_INVIN, inventoryInput.serializeNBT());
        }
        if (inventoryOutput != null) {
            nbttagcompound.setTag(inventoryOutput.getInvName() + NBT_KEY_INVOUT, inventoryOutput.serializeNBT());
        }
        nbttagcompound.setTag(fakeInv.getInvName(), fakeInv.serializeNBT());
        return nbttagcompound;
    }

    /* NBT read */

    private void readInternalData(NBTTagCompound compound) {
        if (compound == null) return;
        if (compound.hasKey(NBT_KEY_PRICE)) price = compound.getLong(NBT_KEY_PRICE);
        if (compound.hasKey(NBT_KEY_OWNER)) owner = compound.getString(NBT_KEY_OWNER);
        readInvData(compound);
    }

    private void readInvData(NBTTagCompound nbttagcompound){
        if (inventoryInput != null && nbttagcompound.hasKey(inventoryInput.getInvName() + NBT_KEY_INVIN)) {
                inventoryInput.deserializeNBT(nbttagcompound.getCompoundTag(inventoryInput.getInvName() + NBT_KEY_INVIN));
        }
        if (inventoryOutput != null && nbttagcompound.hasKey(inventoryOutput.getInvName() + NBT_KEY_INVOUT)) {
                inventoryOutput.deserializeNBT(nbttagcompound.getCompoundTag(inventoryOutput.getInvName() + NBT_KEY_INVOUT));
            }
        if (nbttagcompound.hasKey(fakeInv.getInvName())) {
            fakeInv.deserializeNBT(nbttagcompound.getCompoundTag(fakeInv.getInvName()));
        }
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(type.getName());
    }

    @Override
    public String toString() {
        return getClass().toString().replace("vnj.vnjeconomymod.common.tileEntities.", "");
    }
}