
/**
* Item Author: Katrina S
* nekosune
**/

package com.nekokittygames.modjam.UnDeath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TestItem extends Item {

	public TestItem(int par1) {
		super(par1);
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("testSpawner");
		func_111206_d("undeath:DebugItem");
	}
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, World par2World, int x, int y, int z, int side, float xOffset, float yOffset, float zOffSet)
	{
		if (!par2World.isRemote)
		{
			int i1 = par2World.getBlockId(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d0 = 0.0D;

            if (side == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
            {
                d0 = 0.5D;
            }
			Iterator iterator = EntityList.entityEggs.values().iterator();
			List<Integer> ids=new ArrayList<Integer>();
			while (iterator.hasNext())
			{
			    EntityEggInfo entityegginfo = (EntityEggInfo)iterator.next();
			    ids.add(entityegginfo.spawnedID);
			}
			// shuffle and pick from ids
			Collections.shuffle(ids);
			int id=ids.get(0);
			Entity entity = null;
			entity = EntityList.createEntityByID(id,par2World);
			 
			if (entity != null && entity instanceof EntityLivingBase)
			{
			        EntityLiving entityliving = (EntityLiving)entity;
			entity.setLocationAndAngles(x+0.5d, y+0.5d, z+0.5d, MathHelper.wrapAngleTo180_float(par2World.rand.nextFloat() * 360.0F), 0.0F);
			                    entityliving.rotationYawHead = entityliving.rotationYaw;
			                    entityliving.renderYawOffset = entityliving.rotationYaw;
			                    entityliving.func_110161_a((EntityLivingData)null);
			                   
			                    par2World.spawnEntityInWorld(entity);
			                    entityliving.playLivingSound();
			}
			
		}
		return true;
	}

}
