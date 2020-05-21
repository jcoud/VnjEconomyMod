package vnj.vnjeconomymod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.common.Market;
import vnj.vnjeconomymod.common.commands.VnjCommand;
import vnj.vnjeconomymod.common.inventory.InvHelper;
import vnj.vnjeconomymod.common.items.ItemHolder;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.common.tileEntities.MarketTEMethods;

import javax.annotation.Nullable;

public class MarketBaseBlock extends Block {
    private static final PropertyEnum<TypeEnumBlocks> BLOCK_TYPE = PropertyEnum.create("type", TypeEnumBlocks.class);

    public MarketBaseBlock() {
        super(Material.GLASS);
//        translucent = true;
        setLightOpacity(1);
        setRegistryName("market");
        setTranslationKey(String.format("%s.%s", VnjEconomyMod.MOD_ID, getRegistryName().getPath()));
        setDefaultState(blockState.getBaseState().withProperty(BLOCK_TYPE, TypeEnumBlocks.BUYER));
        setBlockUnbreakable();
        setResistance(999F);
    }



    /*META STATE*/
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BLOCK_TYPE, TypeEnumBlocks.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        return blockState.getValue(BLOCK_TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BLOCK_TYPE);
    }
    /*===*/
    /*rendering*/
    @Override
    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.doesSideBlockRendering(state, world, pos, face);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL ;
    }


    /*====*/

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        MarketTEMethods tile = (MarketTEMethods) worldIn.getTileEntity(pos);
        if (tile != null) {
            switch (tile.getMarketBlockType()) {
                case BUYER:
                    InvHelper.dropItems(worldIn, pos, tile.getInventoryOutput());
                    break;
                case SELLER:
                    InvHelper.dropItems(worldIn, pos, tile.getInventoryInput());
                    break;
                case TRADER:
                    InvHelper.dropItems(worldIn, pos, tile.getInventoryInput());
                    InvHelper.dropItems(worldIn, pos, tile.getInventoryOutput());
                    break;
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(BLOCK_TYPE).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int meta = stack.getMetadata();
        MarketBaseTile tile = TypeEnumBlocks.values()[meta].createTile();
        if (tile == null) return;

        NBTTagCompound nbt = stack.getTagCompound();
        worldIn.setBlockState(pos, state.withProperty(BLOCK_TYPE, TypeEnumBlocks.values()[meta]),2);
        if (nbt != null && nbt.hasKey("preConfigured") && nbt.getBoolean("preConfigured")) {
            tile.setOwner(nbt.getString(MarketBaseTile.NBT_KEY_OWNER));
            tile.setPrice(nbt.getLong(MarketBaseTile.NBT_KEY_PRICE));
        } else tile.setOwner(placer.getName());
//        System.out.println(placer.getName());
        worldIn.setTileEntity(pos, tile);

    }

    private void dismantle(World world, BlockPos pos, EntityPlayer player, IBlockState state, boolean dropInv) {
        ItemStack dismantledItem = new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), dismantledItem);
        removedByPlayer(state, world, pos, player, dropInv);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.notifyBlockUpdate(pos, state, state, 3);
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Only execute on the server
//        if (world.isRemote) return true;

        TileEntity te = world.getTileEntity(pos);
        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY) return false;
        MarketTEMethods ite = (MarketTEMethods) te;
        boolean f1 = player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemHolder.marketBreakingTool;
        boolean f2 = player.isSneaking();
        boolean f3 = ite.getOwner() != null && player.getName().equals(ite.getOwner());

//        System.out.println(ite.getOwner());
//
//        String s1 = (f1 ? TextFormatting.GREEN : TextFormatting.RED) + "" + f1 + TextFormatting.RESET;
//        String s2 = (f2 ? TextFormatting.GREEN : TextFormatting.RED) + "" + f2 + TextFormatting.RESET;
//        String s3 = (f3 ? TextFormatting.GREEN : TextFormatting.RED) + "" + f3 + TextFormatting.RESET;

//        player.sendMessage(new TextComponentString("item: " + s1 + " | sneaking: " + s2 + " | id: " + s3 + TextFormatting.RESET));

        if (f1 && f2 && f3) {
            dismantle(world, pos, player, state, true);
        } else if (f3 || VnjCommand.MARKET_ACCESSING) {
            player.openGui(VnjEconomyMod.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return Market.customerClicked(player, (MarketBaseTile) te);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (TypeEnumBlocks type : TypeEnumBlocks.values()) {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(BLOCK_TYPE).createTile();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return super.getItem(worldIn, pos, state);
    }
}
