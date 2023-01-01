package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenStronghold extends MapGenStructure {
   private final List<Biome> allowedBiomes;
   private boolean ranBiomeCheck;
   private ChunkPos[] structureCoords;
   private double distance;
   private int spread;

   public MapGenStronghold() {
      this.structureCoords = new ChunkPos[128];
      this.distance = 32.0D;
      this.spread = 3;
      this.allowedBiomes = Lists.newArrayList();
      Iterator var1 = Biome.REGISTRY.iterator();

      while(var1.hasNext()) {
         Biome biome = (Biome)var1.next();
         if (biome != null && biome.getBaseHeight() > 0.0F) {
            this.allowedBiomes.add(biome);
         }
      }

   }

   public MapGenStronghold(Map<String, String> p_i2068_1_) {
      this();
      Iterator var2 = p_i2068_1_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, String> entry = (Entry)var2.next();
         if (((String)entry.getKey()).equals("distance")) {
            this.distance = MathHelper.getDouble((String)entry.getValue(), this.distance, 1.0D);
         } else if (((String)entry.getKey()).equals("count")) {
            this.structureCoords = new ChunkPos[MathHelper.getInt((String)((String)entry.getValue()), this.structureCoords.length, 1)];
         } else if (((String)entry.getKey()).equals("spread")) {
            this.spread = MathHelper.getInt((String)((String)entry.getValue()), this.spread, 1);
         }
      }

   }

   public String getStructureName() {
      return "Stronghold";
   }

   public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_) {
      if (!this.ranBiomeCheck) {
         this.generatePositions();
         this.ranBiomeCheck = true;
      }

      BlockPos blockpos = null;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
      double d0 = Double.MAX_VALUE;
      ChunkPos[] var8 = this.structureCoords;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ChunkPos chunkpos = var8[var10];
         blockpos$mutableblockpos.setPos((chunkpos.chunkXPos << 4) + 8, 32, (chunkpos.chunkZPos << 4) + 8);
         double d1 = blockpos$mutableblockpos.distanceSq(pos);
         if (blockpos == null) {
            blockpos = new BlockPos(blockpos$mutableblockpos);
            d0 = d1;
         } else if (d1 < d0) {
            blockpos = new BlockPos(blockpos$mutableblockpos);
            d0 = d1;
         }
      }

      return blockpos;
   }

   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
      if (!this.ranBiomeCheck) {
         this.generatePositions();
         this.ranBiomeCheck = true;
      }

      ChunkPos[] var3 = this.structureCoords;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ChunkPos chunkpos = var3[var5];
         if (chunkX == chunkpos.chunkXPos && chunkZ == chunkpos.chunkZPos) {
            return true;
         }
      }

      return false;
   }

   private void generatePositions() {
      this.initializeStructureData(this.worldObj);
      int i = 0;
      ObjectIterator lvt_2_1_ = this.structureMap.values().iterator();

      while(lvt_2_1_.hasNext()) {
         StructureStart structurestart = (StructureStart)lvt_2_1_.next();
         if (i < this.structureCoords.length) {
            this.structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
         }
      }

      Random random = new Random();
      random.setSeed(this.worldObj.getSeed());
      double d1 = random.nextDouble() * 3.141592653589793D * 2.0D;
      int j = 0;
      int k = 0;
      int l = this.structureMap.size();
      if (l < this.structureCoords.length) {
         for(int i1 = 0; i1 < this.structureCoords.length; ++i1) {
            double d0 = 4.0D * this.distance + this.distance * (double)j * 6.0D + (random.nextDouble() - 0.5D) * this.distance * 2.5D;
            int j1 = (int)Math.round(Math.cos(d1) * d0);
            int k1 = (int)Math.round(Math.sin(d1) * d0);
            BlockPos blockpos = this.worldObj.getBiomeProvider().findBiomePosition((j1 << 4) + 8, (k1 << 4) + 8, 112, this.allowedBiomes, random);
            if (blockpos != null) {
               j1 = blockpos.getX() >> 4;
               k1 = blockpos.getZ() >> 4;
            }

            if (i1 >= l) {
               this.structureCoords[i1] = new ChunkPos(j1, k1);
            }

            d1 += 6.283185307179586D / (double)this.spread;
            ++k;
            if (k == this.spread) {
               ++j;
               k = 0;
               this.spread += 2 * this.spread / (j + 1);
               this.spread = Math.min(this.spread, this.structureCoords.length - i1);
               d1 += random.nextDouble() * 3.141592653589793D * 2.0D;
            }
         }
      }

   }

   protected StructureStart getStructureStart(int chunkX, int chunkZ) {
      MapGenStronghold.Start mapgenstronghold$start;
      for(mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ)) {
      }

      return mapgenstronghold$start;
   }

   public static class Start extends StructureStart {
      public Start() {
      }

      public Start(World worldIn, Random random, int chunkX, int chunkZ) {
         super(chunkX, chunkZ);
         StructureStrongholdPieces.prepareStructurePieces();
         StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
         this.components.add(structurestrongholdpieces$stairs2);
         structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
         List list = structurestrongholdpieces$stairs2.pendingChildren;

         while(!list.isEmpty()) {
            int i = random.nextInt(list.size());
            StructureComponent structurecomponent = (StructureComponent)list.remove(i);
            structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
         }

         this.updateBoundingBox();
         this.markAvailableHeight(worldIn, random, 10);
      }
   }
}