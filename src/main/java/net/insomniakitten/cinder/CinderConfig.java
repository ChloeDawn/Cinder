package net.insomniakitten.cinder;

import net.minecraftforge.common.config.Config;

@Config(modid = Cinder.ID, name = Cinder.ID, category = Cinder.ID)
public final class CinderConfig { // TODO

    @Config.Name("igniters")
    @Config.Comment({"The registry names of valid items that can be used to ignited a light source.",
                     "When used, Cinder will attempt to \"damage\" the item, so be careful."})
    public static String[] igniterNames = { "minecraft:flint_and_steel" };

    @Config.Name("burn_time")
    @Config.Comment("The burn time of a light source, in ticks. The light source will decay over this time until it is extinguished.")
    public static int burnTime = 36000;

}
