package com.nekokittygames.modjam.UnDeath;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.UUID;

public class EntityPlayerSkellington extends EntityMob implements IEntityAdditionalSpawnData,IRangedAttackMob {

	public static int EntityId;
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

	public InventoryPlayerSkellington inventory;
	private int itemInUseCount=100; //TODO: For now until I can experiment with how to deal withthis
	private String SkellingtonName="";
    private UUID playerUUID=null;
    public GameProfile gameProfile=null;

	public static final ResourceLocation field_110314_b = new ResourceLocation("textures/entity/steve.png");
	public static final ResourceLocation overlay=new ResourceLocation("undeath","textures/entity/playerSkellington.png");
	private ThreadDownloadImageData field_110316_a;
	private ThreadDownloadImageData field_110315_c;
	private ResourceLocation mmmm;
	private ResourceLocation tsch;
	private boolean usingBow;
	@SideOnly(Side.CLIENT)
	private String LayeredName;
	private boolean dropItems=true;
	@SideOnly(Side.CLIENT)
	public String getLayeredName() {
		if(LayeredName==null)
			BuildLayeredName();
		return LayeredName;
	}
	@SideOnly(Side.CLIENT)
	public void setLayeredName(String layeredName) {
		LayeredName = layeredName;
	}
	@SideOnly(Side.CLIENT)
	public void BuildLayeredName()
	{
		LayeredName="skins/" + StringUtils.stripControlCodes(getSkellingtonName())+"/skellington";
	}
	@SideOnly(Side.CLIENT)
	public ResourceLocation[] getSkins()
	{
		return new ResourceLocation[] {this.func_110306_p(),overlay};
	}
	public String getSkellingtonName() {
		return SkellingtonName;
	}

	public void setSkellingtonName(String zombieName) {

		SkellingtonName = zombieName;
		this.setCustomNameTag(getCorruptedName());
	}

	public String getCorruptedName()
	{
		return SkellingtonName.replace("e", "\u00A7ke\u00A7r").replace("a", "\u00A7ka\u00A7r").replace("i", "\u00A7ki\u00A7r").replace("o", "\u00A7ko\u00A7r").replace("u", "\u00A7ku\u00A7r");
	}
	public EntityPlayerSkellington(World par1World) {
		super(par1World);
		inventory=new InventoryPlayerSkellington(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

	}
    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

	@SideOnly(Side.CLIENT)
	protected void func_110302_j()
	{
		System.out.println("Setting up custom skins");

		if (this.getSkellingtonName() != null && !this.getSkellingtonName().isEmpty())
		{
			this.mmmm = func_110311_f(this.getSkellingtonName());
			this.tsch = func_110299_g(this.getSkellingtonName());
			this.field_110316_a = func_110304_a(this.mmmm, this.getSkellingtonName());
			this.field_110315_c = func_110307_b(tsch, this.getSkellingtonName());
		}
	}
	@SideOnly(Side.CLIENT)
	public ThreadDownloadImageData func_110309_l()
	{
		return this.field_110316_a;
	}
	@SideOnly(Side.CLIENT)
	public ThreadDownloadImageData func_110310_o()
	{
		return this.field_110315_c;
	}
	@SideOnly(Side.CLIENT)
	public ResourceLocation func_110306_p()
	{
		return this.mmmm;
	}
	@SideOnly(Side.CLIENT)
	public ResourceLocation func_110303_q()
	{
		return this.tsch;
	}
	@SideOnly(Side.CLIENT)
	public static ThreadDownloadImageData func_110304_a(ResourceLocation par0ResourceLocation, String par1Str)
	{
		return func_110301_a(par0ResourceLocation, func_110300_d(par1Str), field_110314_b, new ImageBufferDownload());
	}
	@SideOnly(Side.CLIENT)
	public static ThreadDownloadImageData func_110307_b(ResourceLocation par0ResourceLocation, String par1Str)
	{
		return func_110301_a(par0ResourceLocation, func_110308_e(par1Str), (ResourceLocation)null, (IImageBuffer)null);
	}
	@SideOnly(Side.CLIENT)
	private static ThreadDownloadImageData func_110301_a(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(par0ResourceLocation);

		if (object == null)
		{
			object = new ThreadDownloadImageData(null,par1Str, par2ResourceLocation, par3IImageBuffer);
			texturemanager.loadTexture(par0ResourceLocation, (ITextureObject) object);
		}

		return (ThreadDownloadImageData)object;
	}
	@SideOnly(Side.CLIENT)
	public static String func_110300_d(String par0Str)
	{
		return String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] {StringUtils.stripControlCodes(par0Str)});
	}
	@SideOnly(Side.CLIENT)
	public static String func_110308_e(String par0Str)
	{
		return String.format("http://skins.minecraft.net/MinecraftCloaks/%s.png", new Object[] {StringUtils.stripControlCodes(par0Str)});
	}
	@SideOnly(Side.CLIENT)
	public static ResourceLocation func_110311_f(String par0Str)
	{
		return new ResourceLocation(getSkinName(par0Str));
	}
	@SideOnly(Side.CLIENT)
	private static String getSkinName(String par0Str) {
		return "sskins/" + StringUtils.stripControlCodes(par0Str);
	}
	@SideOnly(Side.CLIENT)
	public static ResourceLocation func_110299_g(String par0Str)
	{
		return new ResourceLocation("scloaks/" + StringUtils.stripControlCodes(par0Str));
	}
	@SideOnly(Side.CLIENT)
	public static ResourceLocation func_110305_h(String par0Str)
	{
		return new ResourceLocation("sskull/" + StringUtils.stripControlCodes(par0Str));
	}


	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		this.inventory.readFromNBT(nbttaglist);
		this.inventory.currentItem = par1NBTTagCompound.getInteger("SelectedItemSlot");
		this.setSkellingtonName(par1NBTTagCompound.getString("skellingtonName"));
        this.gameProfile= NBTUtil.func_152459_a(par1NBTTagCompound.getCompoundTag("gameProfile"));
		this.dropItems=par1NBTTagCompound.getBoolean("dropItems");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
		par1NBTTagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
		par1NBTTagCompound.setString("skellingtonName", getSkellingtonName());
        par1NBTTagCompound.setString("playerUUID",gameProfile.getId().toString());
        NBTTagCompound cmp=new NBTTagCompound();
        NBTUtil.func_152460_a(cmp,gameProfile);
        par1NBTTagCompound.setTag("gameProfile",cmp);
		par1NBTTagCompound.setBoolean("dropItems", dropItems);
	}

	public int getItemInUseCount()
	{
		return this.itemInUseCount;
	}
	
	public void InitFromPlayer(EntityPlayer par7EntityPlayer) {
		this.setSkellingtonName(par7EntityPlayer.getCommandSenderName());
        this.gameProfile=par7EntityPlayer.getGameProfile();
        this.playerUUID=this.gameProfile.getId();
		//this.setSkellingtonName("nekosune");
		this.inventory.copyInventory(par7EntityPlayer.inventory);
		this.inventory.currentItem=1;
		//TODO: The skellington version of this!
		findBestEquipment();
		if(!this.worldObj.isRemote)
			setDropItems();
		copyPotionEffects(par7EntityPlayer);
		//
	}

	private void setDropItems() {
		GameRules gr=this.worldObj.getGameRules();
		dropItems=!gr.getGameRuleBooleanValue("keepInventory");
	}
	public void copyPotionEffects(EntityPlayer player)
	{
		Collection<PotionEffect> effects=player.getActivePotionEffects();
		for(PotionEffect effect:effects)
		{
			PotionEffect toEffect=new PotionEffect(effect);
			this.addPotionEffect(toEffect);
		}
		
	}

private void findBestEquipment() {
		
		int bestScore=-1;
		ItemStack bestWeapon=null;
		int bestLocation=0;
		ItemStack currentCheck;
		int currentScore;
		boolean hasBow=false;
		int arrows=0;
		for(int i=0;i<this.inventory.mainInventory.length;i++)
		{
			currentCheck=this.inventory.mainInventory[i];
			if(currentCheck==null)
				continue;
			if(currentCheck.getItem() instanceof ItemBow)
			{
				hasBow=true;
				bestLocation=i;
				if(EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, currentCheck) > 0)
				{
					arrows=2;
					break;
					
				}
			}
			if(currentCheck.getItem() == Items.arrow)
			{
				arrows+=currentCheck.stackSize;
			}
		}
		if(hasBow && arrows>0)
		{
			UnDeath.logging.info("Using bow found");
			usingBow=true;
			this.setCurrentItem(bestLocation);
			
			return;
			
		}
		for(int i=0;i<this.inventory.mainInventory.length;i++)
		{
			currentCheck=this.inventory.mainInventory[i];
			if(currentCheck==null)
				continue;
			Multimap map=currentCheck.getAttributeModifiers();
            Collection Attributes=(Collection)map.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
			
			if(Attributes.size()==0)
				currentScore=0;
			else
				currentScore=(int)((AttributeModifier)Attributes.toArray()[0]).getAmount();
			NBTTagList enchList=currentCheck.getEnchantmentTagList();
			if(enchList==null)
				currentScore+=0;
			else
			{
				for(int j=0;j<enchList.tagCount();j++)
				{
					NBTTagCompound comp=(NBTTagCompound)enchList.getCompoundTagAt(j);
					int enchId=comp.getShort("id");
					int enchLvl=comp.getShort("lvl");
					switch(enchId)
					{
					case 16:
						currentScore+=(1*enchLvl);
						break;
					case 19:
						currentScore+=(1*enchLvl);
						break;
					case 20:
						currentScore+=(2*enchLvl);
						break;
					default:
						currentScore+=1;
					}
				}
			}
			UnDeath.logging.info(String.format("Item %s got score %d", currentCheck.toString(),currentScore));
			if(currentScore>bestScore)
			{
				bestWeapon=currentCheck;
				bestLocation=i;
				bestScore=currentScore;
			}
		}
		if(bestScore==-1)
		{
			UnDeath.logging.info("No weapons found");
			this.inventory.currentItem=-1;
			return;
		}
		UnDeath.logging.info(String.format("Best Weapon is %s with score %d", bestWeapon.toString(),bestScore));
		usingBow=false;
		this.setCurrentItem(bestLocation);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte)0));
	}
	public boolean isAIEnabled()
	{
		return true;
	}

	// Add in other funcs here

	protected String getLivingSound()
	{
		return "mob.skeleton.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.skeleton.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}





	public boolean attackEntityAsMob(Entity par1Entity)
	{
		if (super.attackEntityAsMob(par1Entity))
		{
			//TODO: decide if want a potion effect on hit
			//if (this.getSkeletonType() == 1 && par1Entity instanceof EntityLivingBase)
			//{
			//    ((EntityLivingBase)par1Entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			//}

			return true;
		}
		else
		{
			return false;
		}
	}
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	 /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getCurrentItemOrArmor(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }

        if (this.worldObj.isRemote && this.getSkeletonType() == 1)
        {
            this.setSize(0.72F, 2.34F);
        }
        if(usingBow && !inventory.hasItem(Items.arrow) )
        {
        	findBestEquipment();
        }
        super.onLivingUpdate();
    }
    
    public void updateRidden()
    {
        super.updateRidden();

        if (this.ridingEntity instanceof EntityCreature)
        {
            EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }
    public int getSkeletonType()
    {
        return this.dataWatcher.getWatchableObjectByte(13);
    }
    public void setSkeletonType(int par1)
    {
        this.dataWatcher.updateObject(13, Byte.valueOf((byte)par1));

        if (par1 == 1)
        {
            this.setSize(0.72F, 2.34F);
        }
        else
        {
            this.setSize(0.6F, 1.8F);
        }
    }
    
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		NBTTagCompound compound=new NBTTagCompound();
		//compound.setName("Skellington");
		compound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
		compound.setInteger("SelectedItemSlot", this.inventory.currentItem);
		compound.setString("skellingtonName", getSkellingtonName());
        NBTTagCompound cmp=new NBTTagCompound();
        NBTUtil.func_152460_a(cmp,gameProfile);
        compound.setTag("gameProfile",cmp);
		compound.setBoolean("dropItems", dropItems);
		try {
            ByteBufUtils.writeTag(buffer, compound);
		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		NBTTagCompound compound;
		try {
            compound=ByteBufUtils.readTag(additionalData);
			NBTTagList nbttaglist = compound.getTagList("Inventory",Constants.NBT.TAG_COMPOUND);
			this.inventory.readFromNBT(nbttaglist);
			this.inventory.currentItem = compound.getInteger("SelectedItemSlot");
			this.setSkellingtonName(compound.getString("skellingtonName"));
            this.gameProfile= NBTUtil.func_152459_a(compound.getCompoundTag("gameProfile"));
			this.dropItems=compound.getBoolean("dropItems");
			if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
			{
				this.func_110302_j();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

	// Theese zombies can't be converted back
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		return false;
	}

	/**
	 * 0 = item, 1-n is armor
	 */
	public ItemStack getCurrentItemOrArmor(int par1)
	{

		if(par1==0)
			if(this.inventory.currentItem==-1)
				return null;
			else
				return this.inventory.mainInventory[this.inventory.currentItem];
		return this.inventory.armorInventory[par1-1];
	}

	public ItemStack func_130225_q(int i)
	{
		return this.inventory.armorInventory[i];
	}
	
	public void setCurrentItem(int location)
	{
		this.inventory.currentItem=location;
		if (!this.worldObj.isRemote )
        {
            this.setCombatTask();
        }
	}
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
	{
		if(par1==0)
			this.inventory.mainInventory[this.inventory.currentItem]=par2ItemStack;
		else
			this.inventory.armorInventory[par1-1]=par2ItemStack;
		if (!this.worldObj.isRemote && par1 == 0)
        {
            this.setCombatTask();
        }
	}
	public double getYOffset()
    {
        return super.getYOffset() - 0.5D;
    }
	public ItemStack[] getLastActiveItems()
	{
		if(this.inventory.currentItem==-1)
			return ArrayUtils.addAll(new ItemStack[] { null},this.inventory.armorInventory);
		return ArrayUtils.addAll(new ItemStack[] { this.inventory.mainInventory[this.inventory.currentItem]},this.inventory.armorInventory);
	}

	@Override
	protected void dropEquipment(boolean par1, int par2)
	{
		if(dropItems)
			this.inventory.dropAllItems();
	}

	public ItemStack getHeldItem()
	{
		if(this.inventory.currentItem==-1)
			return null;
		return this.inventory.mainInventory[this.inventory.currentItem];
	}

	public void setCombatTask()
	{
		this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
		if(usingBow && this.getCurrentItemOrArmor(0).getItem() instanceof ItemBow )
			this.tasks.addTask(4, this.aiArrowAttack);
		else
			this.tasks.addTask(4, this.aiAttackOnCollide);
	}
	
	//TODO: make this use ininv arrows and such
	@Override
	public void attackEntityWithRangedAttack(
			EntityLivingBase par1EntityLivingBase, float par2) {
        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, par1EntityLivingBase, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage((double)(par2 * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1)
        {
            entityarrow.setFire(100);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
        if(!(EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, this.getCurrentItemOrArmor(0)) > 0))
        	inventory.consumeInventoryItem(Items.arrow);
        

	}
	@Override
	protected boolean canDespawn() {
		return false;
	}


}
