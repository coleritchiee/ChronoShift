package net.iicosahedra.chronoshift.util;

import net.iicosahedra.chronoshift.ChronoShift;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@EventBusSubscriber(modid = ChronoShift.MODID)
public class TickScheduler {
    private static class ScheduledTask {
        int remainingTicks;
        Runnable task;

        public ScheduledTask(int delayTicks, Runnable task) {
            this.remainingTicks = delayTicks;
            this.task = task;
        }
    }

    private static final List<ScheduledTask> taskList = new ArrayList<>();

    public static void schedule(int delayTicks, Runnable task) {
        taskList.add(new ScheduledTask(delayTicks, task));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        Iterator<ScheduledTask> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            ScheduledTask scheduledTask = iterator.next();
            scheduledTask.remainingTicks--;

            if (scheduledTask.remainingTicks <= 0) {
                scheduledTask.task.run();
                iterator.remove();
            }
        }
    }
}