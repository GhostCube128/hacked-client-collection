package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends Biome {
   private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
   private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
   private static final WorldGenMegaPineTree MEGA_PINE_GENERATOR = new WorldGenMegaPineTree(false, false);
   private static final WorldGenMegaPineTree MEGA_SPRUCE_GENERATOR = new WorldGenMegaPineTree(false, true);
   private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR;
   private final BiomeTaiga.Type type;

   public BiomeTaiga(BiomeTaiga.Type typeIn, Biome.BiomeProperties properties) {
      super(properties);
      this.type = typeIn;
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
      this.theBiomeDecorator.treesPerChunk = 10;
      if (typeIn != BiomeTaiga.Type.MEGA && typeIn != BiomeTaiga.Type.MEGA_SPRUCE) {
         this.theBiomeDecorator.grassPerChunk = 1;
         this.theBiomeDecorator.mushroomsPerChunk = 1;
      } else {
         this.theBiomeDecorator.grassPerChunk = 7;
         this.theBiomeDecorator.deadBushPerChunk = 1;
         this.theBiomeDecorator.mushroomsPerChunk = 3;
      }

   }

   public WorldGenAbstractTree genBigTreeChance(Random rand) {
      if ((this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) && rand.nextInt(3) == 0) {
         return this.type != BiomeTaiga.Type.MEGA_SPRUCE && rand.nextInt(13) != 0 ? MEGA_PINE_GENERATOR : MEGA_SPRUCE_GENERATOR;
      } else {
         return (WorldGenAbstractTree)(rand.nextInt(3) == 0 ? PINE_GENERATOR : SPRUCE_GENERATOR);
      }
   }

   public WorldGenerator getRandomWorldGenForGrass(Random rand) {
      return rand.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
   }

   public void decorate(World worldIn, Random rand, BlockPos pos) {
      int i1;
      int j1;
      int k1;
      int l1;
      if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
         i1 = rand.nextInt(3);

         for(j1 = 0; j1 < i1; ++j1) {
            k1 = rand.nextInt(16) + 8;
            l1 = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(k1, 0, l1));
            FOREST_ROCK_GENERATOR.generate(worldIn, rand, blockpos);
         }
      }

      DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

      for(i1 = 0; i1 < 7; ++i1) {
         j1 = rand.nextInt(16) + 8;
         k1 = rand.nextInt(16) + 8;
         l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
         DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
      }

      super.decorate(worldIn, rand, pos);
   }

   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
      if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
         this.topBlock = Blocks.GRASS.getDefaultState();
         this.fillerBlock = Blocks.DIRT.getDefaultState();
         if (noiseVal > 1.75D) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
         } else if (noiseVal > -0.95D) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
         }
      }

      this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
   }

   static {
      FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
   }

   public static enum Type {
      NORMAL,
      MEGA,
      MEGA_SPRUCE;
   }
}