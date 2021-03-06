package com.nekokittygames.modjam.UnDeath.client;

import com.google.common.collect.Lists;
import com.nekokittygames.modjam.UnDeath.EntityPlayerZombie;
import com.nekokittygames.modjam.UnDeath.EntityPlayerZombiePigmen;
import com.nekokittygames.modjam.UnDeath.UnDeath;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.swing.text.html.parser.Entity;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

@SideOnly(Side.CLIENT)
public class ResourceLayeredTexture extends AbstractTexture {
    public final ThreadDownloadImageData data;
    public final ResourceLocation loc;
    public ResourceLayeredTexture(ThreadDownloadImageData par1ArrayOfStr,ResourceLocation location)
    {
        data= par1ArrayOfStr;
        loc=location;
    }

    @Override
    public void loadTexture(IResourceManager par1ResourceManager)
    {
        BufferedImage bufferedimage = null;



                try
                {
                    BufferedImage bufferedimage1 = ClientUtils.getBufferedImageSkin(data);

                    if (bufferedimage == null)
                    {
                        bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
                    }


                    bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, (ImageObserver)null);
                    InputStream inputstream = par1ResourceManager.getResource(loc).getInputStream();
                    bufferedimage1 = ImageIO.read(inputstream);
                    bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, (ImageObserver)null);
                }
                catch(Exception e)
                {
                    UnDeath.logging.log(Level.SEVERE, "Couldn\'t load layered image", e);
                    e.printStackTrace();
                    return;
                }
        //TextureUtil.func_110987_a(this.func_110552_b(), bufferedimage);
        TextureUtil.uploadTextureImage(this.getGlTextureId(),bufferedimage);
    }
}