package baritone.api;

import baritone.api.behavior.IPathingBehavior;
import baritone.api.process.ICustomGoalProcess;

/** 仅用于编译的 Baritone API 桩。 */
public final class BaritoneAPI {
    public interface IBaritone {
        IPathingBehavior getPathingBehavior();

        ICustomGoalProcess getCustomGoalProcess();
    }

    public interface IBaritoneProvider {
        IBaritone getPrimaryBaritone();
    }

    private static final Settings SETTINGS = new Settings();

    public static Settings getSettings() {
        return SETTINGS;
    }

    public static IBaritoneProvider getProvider() {
        throw new UnsupportedOperationException("compile-time stub");
    }
}
