package com.nekokittygames.modjam.UnDeath.client;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;

public class ThreadDownloadZombieImageDataINNER1 extends Thread {
	final ThreadDownloadZombieImageData field_110932_a;

    ThreadDownloadZombieImageDataINNER1(ThreadDownloadZombieImageData par1ThreadDownloadImageData)
    {
        this.field_110932_a = par1ThreadDownloadImageData;
    }

    public void run()
    {
        HttpURLConnection httpurlconnection = null;

        try
        {
            httpurlconnection = (HttpURLConnection)(new URL(ThreadDownloadZombieImageData.func_110554_a(this.field_110932_a))).openConnection(Minecraft.getMinecraft().func_110437_J());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 != 2)
            {
                return;
            }

            BufferedImage bufferedimage = ImageIO.read(httpurlconnection.getInputStream());

            if (ThreadDownloadZombieImageData.func_110555_b(this.field_110932_a) != null)
            {
                bufferedimage = ThreadDownloadZombieImageData.func_110555_b(this.field_110932_a).parseUserSkin(bufferedimage);
            }

            this.field_110932_a.func_110556_a(bufferedimage);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            if (httpurlconnection != null)
            {
                httpurlconnection.disconnect();
            }
        }
    }
}