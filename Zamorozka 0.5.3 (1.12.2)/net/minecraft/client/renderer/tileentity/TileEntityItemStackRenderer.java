package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import zamorozka.module.ModuleManager;
import zamorozka.modules.PLAYER.KeepSprint;
import zamorozka.modules.VISUALLY.ShieldESP;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class TileEntityItemStackRenderer
{
    private static final TileEntityShulkerBox[] field_191274_b = new TileEntityShulkerBox[16];
    public static TileEntityItemStackRenderer instance;
    private final TileEntityChest chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
    private final TileEntityChest chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
    private final TileEntityEnderChest enderChest = new TileEntityEnderChest();
    private final TileEntityBanner banner = new TileEntityBanner();
    private final TileEntityBed field_193843_g = new TileEntityBed();
    private final TileEntitySkull skull = new TileEntitySkull();
    private final ModelShield modelShield = new ModelShield();

    public void renderByItem(ItemStack itemStackIn)
    {
        this.func_192838_a(itemStackIn, 1.0F);
    }

    public void func_192838_a(ItemStack p_192838_1_, float p_192838_2_)
    {
        Item item = p_192838_1_.getItem();

        if (item == Items.BANNER)
        {
            this.banner.setItemValues(p_192838_1_, false);
            TileEntityRendererDispatcher.instance.func_192855_a(this.banner, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (item == Items.BED)
        {
            this.field_193843_g.func_193051_a(p_192838_1_);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_193843_g, 0.0D, 0.0D, 0.0D, 0.0F);
        }
        else if (item == Items.SHIELD)
        {
            if (p_192838_1_.getSubCompound("BlockEntityTag") != null)
            {
                this.banner.setItemValues(p_192838_1_, true);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
            }
            else
            {
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
            }
        	GL11.glPushMatrix();
        	{
        		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        		{
        			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        			GL11.glDisable(GL11.GL_TEXTURE_2D);
        			GL11.glDisable(GL11.GL_LIGHTING);
        			GL11.glDisable(GL11.GL_DEPTH_TEST);
        			
        			GL11.glEnable(GL11.GL_LINE_SMOOTH);
        			
        			GL11.glEnable(GL11.GL_BLEND);
        			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        			GL11.glLineWidth(1.5F);
        			GL11.glColor4f(1, 0, 1, 0.5F);
        			if(ModuleManager.getModule(ShieldESP.class).getState()){
        			this.modelShield.render();
        			}

        		}
        		GL11.glPopAttrib();
        	}
        	GL11.glPopMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            this.modelShield.render();
            GlStateManager.popMatrix();
        }
        
        else if (item == Items.SKULL)
        {
            GameProfile gameprofile = null;

            if (p_192838_1_.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = p_192838_1_.getTagCompound();

                if (nbttagcompound.hasKey("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner")))
                {
                    GameProfile gameprofile1 = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    gameprofile = TileEntitySkull.updateGameprofile(gameprofile1);
                    nbttagcompound.removeTag("SkullOwner");
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }

            if (TileEntitySkullRenderer.instance != null)
            {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 180.0F, p_192838_1_.getMetadata(), gameprofile, -1, 0.0F);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }
        else if (item == Item.getItemFromBlock(Blocks.ENDER_CHEST))
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (item == Item.getItemFromBlock(Blocks.TRAPPED_CHEST))
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.chestTrap, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (Block.getBlockFromItem(item) instanceof BlockShulkerBox)
        {
            TileEntityRendererDispatcher.instance.func_192855_a(field_191274_b[BlockShulkerBox.func_190955_b(item).getMetadata()], 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.chestBasic, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
    }

    static
    {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            field_191274_b[enumdyecolor.getMetadata()] = new TileEntityShulkerBox(enumdyecolor);
        }

        instance = new TileEntityItemStackRenderer();
    }
}