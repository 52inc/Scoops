package com.ftinc.scoop.util;

/**
 * This class works as the main entry point for personalized UI
 */
public class ScoopHelper {

    /**
     * @return the List of Flavors currently available in the Scoop instance
     */
    public static List<Flavor> getFlavors() {
        return Scoop.getInstance().getFlavors();
    }

    /**
     * @return the currently selected Flavor for the app
     */
    public static Flavor getCurrentFlavor() {
        return Scoop.getInstance().getCurrentFlavor();
    }

    /**
     * Updates the current app theme to the passed one.
     *
     * Remember to restart your current Activity to see the change in action
     */
    public static void updateThemeTo(Flavor item) {
        Scoop.getInstance().choose(item);
    }
}
