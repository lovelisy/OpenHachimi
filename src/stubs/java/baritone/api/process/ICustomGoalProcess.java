package baritone.api.process;

import baritone.api.pathing.goals.Goal;

/** 仅用于编译的 Baritone API 桩。 */
public interface ICustomGoalProcess {
    void setGoalAndPath(Goal goal);
}
