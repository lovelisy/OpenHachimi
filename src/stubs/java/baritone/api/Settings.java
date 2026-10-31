package baritone.api;

import java.awt.Color;

/** 仅用于编译的 Baritone API 桩：真实依赖是玩家自己装的可选 mod（见 fabric.mod.json 与 SCOPE.md）。 */
public final class Settings {
    public static final class Setting<T> {
        public T value;
    }

    public final Setting<Boolean> allowBreak = new Setting<>();
    public final Setting<Boolean> allowDiagonalAscend = new Setting<>();
    public final Setting<Boolean> allowDiagonalDescend = new Setting<>();
    public final Setting<Boolean> allowDownward = new Setting<>();
    public final Setting<Boolean> allowInventory = new Setting<>();
    public final Setting<Boolean> allowJumpAt256 = new Setting<>();
    public final Setting<Boolean> allowParkour = new Setting<>();
    public final Setting<Boolean> allowParkourAscend = new Setting<>();
    public final Setting<Boolean> allowParkourPlace = new Setting<>();
    public final Setting<Boolean> allowPlace = new Setting<>();
    public final Setting<Boolean> allowSprint = new Setting<>();
    public final Setting<Boolean> allowVines = new Setting<>();
    public final Setting<Boolean> allowWaterBucketFall = new Setting<>();
    public final Setting<Boolean> antiCheatCompatibility = new Setting<>();
    public final Setting<Float> blockReachDistance = new Setting<>();
    public final Setting<Boolean> censorCoordinates = new Setting<>();
    public final Setting<Boolean> censorRanCommands = new Setting<>();
    public final Setting<Boolean> chatControl = new Setting<>();
    public final Setting<Boolean> chatDebug = new Setting<>();
    public final Setting<Color> colorCurrentPath = new Setting<>();
    public final Setting<Color> colorGoalBox = new Setting<>();
    public final Setting<Color> colorMostRecentConsidered = new Setting<>();
    public final Setting<Color> colorNextPath = new Setting<>();
    public final Setting<Boolean> disconnectOnArrival = new Setting<>();
    public final Setting<Boolean> freeLook = new Setting<>();
    public final Setting<Boolean> legitMine = new Setting<>();
    public final Setting<Boolean> strictLiquidCheck = new Setting<>();
}
