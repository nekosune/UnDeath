/*package com.nekokittygames.modjam.UnDeath.client;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ThreadDownloadZombieImageData extends AbstractTexture {

	 private final String field_110562_b;
	    private final IImageBuffer field_110563_c;
	    private BufferedImage field_110560_d;
	    public BufferedImage getField_110560_d() {
			return field_110560_d;
		}

		public void setField_110560_d(BufferedImage field_110560_d) {
			this.field_110560_d = field_110560_d;
		}

		private Thread field_110561_e;
	    private SimpleTexture field_110558_f;
	    private boolean field_110559_g;

	    public ThreadDownloadZombieImageData(String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
	    {
            super();
	        this.field_110562_b = par1Str;
	        this.field_110563_c = par3IImageBuffer;
	        this.field_110558_f = par2ResourceLocation != null ? new SimpleTexture(par2ResourceLocation) : null;
	    }

	    public int func_110552_b()
	    {
	        int i = super.getGlTextureId();

	        if (!this.field_110559_g && this.field_110560_d != null)
	        {
	            TextureUtil.uploadTextureImage(i, this.field_110560_d);
	            this.field_110559_g = true;
	        }

	        return i;
	    }

	    public void func_110556_a(BufferedImage par1BufferedImage)
	    {
	        this.field_110560_d = par1BufferedImage;
	    }

	    public void loadTexture(IResourceManager par1ResourceManager) throws IOException
	    {
	        if (this.field_110560_d == null)
	        {
	            if (this.field_110558_f != null)
	            {
	                this.field_110558_f.func_110551_a(par1ResourceManager);
	                this.field_110553_a = this.field_110558_f.func_110552_b();
	            }
	        }
	        else
	        {
	            TextureUtil.func_110987_a(this.func_110552_b(), this.field_110560_d);
	        }

	        if (this.field_110561_e == null)
	        {
	            this.field_110561_e = new ThreadDownloadZombieImageDataINNER1(this);
	            this.field_110561_e.setDaemon(true);
	            this.field_110561_e.setName("Skin downloader: " + this.field_110562_b);
	            this.field_110561_e.start();
	        }
	    }

	    public boolean fff()
	    {
	        this.func_110552_b();
	        return this.field_110559_g;
	    }

	    static String func_110554_a(ThreadDownloadZombieImageData par0ThreadDownloadImageData)
	    {
	        return par0ThreadDownloadImageData.field_110562_b;
	    }

	    static IImageBuffer func_110555_b(ThreadDownloadZombieImageData par0ThreadDownloadImageData)
	    {
	        return par0ThreadDownloadImageData.field_110563_c;
	    }

}*/
