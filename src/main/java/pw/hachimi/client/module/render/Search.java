package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.model.render.ShapeMode;
import org.joml.Vector3f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Box;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.movement.MoveToWorldEvent;
import pw.hachimi.client.event.world.OnChunkDataEvent;
import pw.hachimi.client.event.world.SetBlockStateEvent;
import pw.hachimi.client.event.world.UnloadBlockEntitiesEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.SearchPlayerWorldRendererUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Search extends ClientModule {
   private final Map map = new ConcurrentHashMap();
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
   Setting blocks = this.registerSetting(
      new BlockListSetting("Blocks", "Valid search blocks", 
         Blocks.NETHER_PORTAL,
         Blocks.END_PORTAL,
         Blocks.ENDER_CHEST,
         Blocks.CHEST,
         Blocks.TRAPPED_CHEST,
         Blocks.DISPENSER,
         Blocks.DROPPER,
         Blocks.SHULKER_BOX,
         Blocks.BLACK_SHULKER_BOX,
         Blocks.BLUE_SHULKER_BOX,
         Blocks.LIGHT_BLUE_SHULKER_BOX,
         Blocks.GREEN_SHULKER_BOX,
         Blocks.CYAN_SHULKER_BOX,
         Blocks.BROWN_SHULKER_BOX,
         Blocks.GRAY_SHULKER_BOX,
         Blocks.LIGHT_GRAY_SHULKER_BOX,
         Blocks.LIME_SHULKER_BOX,
         Blocks.MAGENTA_SHULKER_BOX,
         Blocks.ORANGE_SHULKER_BOX,
         Blocks.PINK_SHULKER_BOX,
         Blocks.PURPLE_SHULKER_BOX,
         Blocks.RED_SHULKER_BOX,
         Blocks.WHITE_SHULKER_BOX,
         Blocks.YELLOW_SHULKER_BOX,
         Blocks.SPAWNER,
         Blocks.END_PORTAL_FRAME
      )
   );
   Setting tracers = this.registerSetting(new BooleanSetting("Tracers", "Draws tracers to highlighted blocks", false));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the tracer", 1.0F, 1.0F, 5.0F, this::isWidthVisible));
   Setting fill = this.registerSetting(new BooleanSetting("Fill", "Fills the render", true));
   Setting softReload = this.registerSetting(new BooleanSetting("SoftReload", "Reloads without clearing the renders", false));
   Setting asynchronous = this.registerSetting(new BooleanSetting("Asynchronous", "Performs calculations on separate threads", true));
   private boolean i;

   public Search() {
      super("Search", "Highlights specified blocks in the world", ModuleCategory.RENDER);
   }

   @Override
   public void awd() {
      if (client.world != null && client.getNetworkHandler() != null) {
         if ((Boolean)this.asynchronous.atx()) {
            ExecutorService temp = this.executorService;
            Runnable runnable = this::Po;
            temp.submit(runnable);
         } else {
            this.Po();
         }
      }
   }

   private void Po() {
      Iterator iterator2 = CollisionUtils.aiu().iterator();

      while (iterator2.hasNext()) {
         Chunk chunk2 = (Chunk)iterator2.next();
         int n = chunk2.getPos().getStartX();
         int n2 = chunk2.getPos().getStartZ();

         for (int n3 = chunk2.getBottomY(); n3 < chunk2.getHeight(); n3++) {
            for (int n4 = n; n4 < n + 16; n4++) {
               for (int n5 = n2; n5 < n2 + 16; n5++) {
                  BlockPos pos = new BlockPos(n4, n3, n5);
                  BlockState state = chunk2.getBlockState(pos);
                  if (!state.isAir() && this.Py(state) && !this.map.containsKey(pos)) {
                     this.map.put(pos, state);
                  }
               }
            }
         }
      }

      MinecraftClient temp = client;
      Runnable runnable = this::PA;
      temp.executeSync(runnable);
      if (!this.i) {
         this.hr(Formatting.RED + "This module may cause lag when loading new chunks");
         this.i = true;
      }
   }

   @Override
   public String hy() {
      return this.map.size() + "";
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.map.clear();
   }

   @EventListener
   public void onMoveToWorld(MoveToWorldEvent moveToWorldEvent) {
      this.map.clear();
   }

   @EventListener
   public void onOnChunkData(OnChunkDataEvent onChunkDataEvent) {
      ClientChunkManager temp = client.world.getChunkManager();
      int n = onChunkDataEvent.getChunkDataS2CPacket().getChunkZ();
      int n2 = onChunkDataEvent.getChunkDataS2CPacket().getChunkX();
      WorldChunk worldChunk = temp.getWorldChunk(n2, n, false);
      int n3 = worldChunk.getPos().getStartX();
      int n4 = worldChunk.getPos().getStartZ();

      for (int n5 = worldChunk.getBottomY(); n5 < worldChunk.getHeight(); n5++) {
         for (int n6 = n3; n6 < n3 + 16; n6++) {
            for (int n7 = n4; n7 < n4 + 16; n7++) {
               BlockPos pos = new BlockPos(n6, n5, n7);
               BlockState state = worldChunk.getBlockState(pos);
               this.Ps(pos, state);
            }
         }
      }
   }

   public void Ps(BlockPos pos, BlockState state) {
      if (!state.isAir() && this.Py(state) && !this.map.containsKey(pos)) {
         this.map.put(pos, state);
      }
   }

   @EventListener
   public void onUnloadBlockEntities(UnloadBlockEntitiesEvent unloadBlockEntitiesEvent) {
      Set temp = this.map.entrySet();
      final UnloadBlockEntitiesEvent unloadBlockEntitiesEvent2 = unloadBlockEntitiesEvent;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object unloadBlockEntitiesEvent) {
            return Search.Pz(unloadBlockEntitiesEvent2, (Entry)unloadBlockEntitiesEvent);
         }
      };
      temp.removeIf(predicate);
   }

   @EventListener
   public void onSetBlockState(SetBlockStateEvent setBlockStateEvent) {
      if (this.Py(setBlockStateEvent.getState())) {
         Map temp = this.map;
         BlockPos pos = setBlockStateEvent.getPos();
         if (!temp.containsKey(pos)) {
            temp = this.map;
            BlockState state = setBlockStateEvent.getState();
            BlockPos pos2 = setBlockStateEvent.getPos();
            temp.put(pos2, state);
         }
      } else {
         Map map2 = this.map;
         BlockPos pos3 = setBlockStateEvent.getPos();
         map2.remove(pos3);
      }
   }

   @EventListener
   public void Pv(UnknownEvent3 unknownEvent3) {
      if (unknownEvent3.getSetting() == this.blocks && unknownEvent3.getEventPhase() == EventPhase.POST) {
         this.awc();
         this.awb();
      }
   }

   @EventListener
   public void Pw(Render3DContext render3DContext) {
      Entity entity2 = client.getCameraEntity();
      if (entity2 instanceof PlayerEntity) {
         PlayerEntity player2 = (PlayerEntity)entity2;
         if ((Boolean)this.tracers.atx()) {
            Vector3f vector3f = new Vector3f(0.0F, 0.0F, 1.0F);
            if ((Boolean)client.options.getBobView().getValue() && !Freecam.gP().awg()) {
               MatrixStack matrices = new MatrixStack();
               float f = client.getRenderTickCounter().getTickDelta(true);
               float f2 = player2.horizontalSpeed - player2.prevHorizontalSpeed;
               float f3 = -(player2.horizontalSpeed + f2 * f);
               float f4 = MathHelper.lerp(f, player2.prevStrideDistance, player2.strideDistance);
               matrices.translate(-(double)(MathHelper.sin(f3 * (float) Math.PI) * f4) * 0.5, Math.abs(MathHelper.cos(f3 * (float) Math.PI) * f4), 0.0);
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(f3 * (float) Math.PI) * f4 * 3.0F));
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(f3 * (float) Math.PI - 0.2F) * f4) * 5.0F));
               vector3f.mulPosition(matrices.peek().getPositionMatrix().invert());
            }

            Vec3d vec = new Vec3d(vector3f.x, -vector3f.y, vector3f.z);
            vec = vec.rotateX(-(float)Math.toRadians(client.gameRenderer.getCamera().getPitch()));
            vec = vec.rotateY(-(float)Math.toRadians(client.gameRenderer.getCamera().getYaw()));
            vec = vec.add(client.gameRenderer.getCamera().getPos());
            Iterator iterator2 = this.map.entrySet().iterator();

            while (iterator2.hasNext()) {
               Entry entry = (Entry)iterator2.next();
               BlockPos pos2 = (BlockPos)entry.getKey();
               Color color = this.Px(pos2, (BlockState)entry.getValue(), 255);
               render3DContext.boxRenderer.YL(vec.x, vec.y, vec.z, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5, color, ((Float)this.width.atx()).floatValue());
            }
         }

         Iterator iterator3 = this.map.entrySet().iterator();

         while (iterator3.hasNext()) {
            Entry entry2 = (Entry)iterator3.next();
            BlockPos pos3 = (BlockPos)entry2.getKey();
            VoxelShape voxelShape = ((BlockState)entry2.getValue()).getOutlineShape((BlockView)client.world, pos3);
            if (!voxelShape.isEmpty()) {
               Box box = voxelShape.getBoundingBox();
               Box box2 = new Box(
                  pos3.getX() + box.minX, pos3.getY() + box.minY, pos3.getZ() + box.minZ, pos3.getX() + box.maxX, pos3.getY() + box.maxY, pos3.getZ() + box.maxZ
               );
               Color color2 = this.Px(pos3, (BlockState)entry2.getValue(), 40);
               Color color3 = this.Px(pos3, (BlockState)entry2.getValue(), 145);
               ShapeMode shapeMode = (Boolean)this.fill.atx() ? ShapeMode.Both : ShapeMode.Lines;
               render3DContext.boxRenderer.Zd(box2, color2, color3, shapeMode);
            }
         }
      }
   }

   private Color Px(BlockPos pos, BlockState state, int n) {
      ClientWorld world2 = client.world;
      int n2 = state.getMapColor((BlockView)world2, pos).color;
      if (state.getBlock() == Blocks.NETHER_PORTAL) {
         n2 = -7141377;
      }

      int n3 = Argb.getRed(n2);
      int n4 = Argb.getGreen(n2);
      int n5 = Argb.getBlue(n2);
      return new Color(n3, n4, n5, n);
   }

   private boolean Py(BlockState state) {
      return ((BlockListSetting)this.blocks).afW(state.getBlock());
   }

   static boolean Pz(UnloadBlockEntitiesEvent unloadBlockEntitiesEvent, Entry entry) {
      return unloadBlockEntitiesEvent.yr((BlockPos)entry.getKey());
   }

   private void PA() {
      SearchPlayerWorldRendererUtils.UA((Boolean)this.softReload.atx());
   }

   private Boolean isWidthVisible() {
      return (Boolean)this.tracers.atx();
   }
}
