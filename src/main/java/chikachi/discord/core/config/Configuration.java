/*
 * Copyright (C) 2017 Chikachi
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package chikachi.discord.core.config;

import chikachi.discord.core.CoreConstants;
import chikachi.discord.core.config.linking.LinkingWrapper;

import java.io.File;

public class Configuration {
    private static File directory;

    private static ConfigurationFile<ConfigWrapper> config;
    private static ConfigurationFile<LinkingWrapper> linking;

    public static void onPreInit(String directoryPath) {
        directory = new File(directoryPath);

        //noinspection ResultOfMethodCallIgnored
        directory.mkdirs();

        File configFile = new File(directory, CoreConstants.MODID + ".json");
        config = new ConfigurationFile<>(ConfigWrapper.class, configFile);
        config.load();

        File linkingFile = new File(directory, CoreConstants.MODID + "_links.json");
        linking = new ConfigurationFile<>(LinkingWrapper.class, linkingFile);
        linking.load();
    }

    public static void loadConfig() {
        config.load();
    }

    /**
     * Saves all config files
     */
    public static void save() {
        config.save();
        linking.save();
    }

    public static void saveConfig() {
        config.save();
    }

    public static void saveLinking() {
        linking.save();
    }
    public static void saveClean() {
        config.saveClean(directory);
    }

    public static void saveCleanConfig() {
        config.saveClean(directory);
    }

    public static void saveCleanLinking() {
        linking.saveClean(directory);
    }

    public static ConfigWrapper getConfig() {
        return config.getWrapper();
    }

    public static LinkingWrapper getLinking() {
        return linking.getWrapper();
    }
}
