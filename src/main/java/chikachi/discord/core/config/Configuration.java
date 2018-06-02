/*
 * Copyright (C) 2018 Chikachi and other contributors
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
import chikachi.discord.core.config.secrets.SecretsWrapper;

import java.io.File;

public class Configuration {
    private static File directory;

    private static ConfigurationFile<ConfigWrapper> config;
    private static ConfigurationFile<LinkingWrapper> linking;
    private static ConfigurationFile<SecretsWrapper> secrets;

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

        File secretsFile = new File(directory, CoreConstants.MODID + "_secrets.json");
        secrets = new ConfigurationFile<>(SecretsWrapper.class, secretsFile);
        secrets.load();
    }

    public static void loadConfig() {
        config.load();
    }

    /**
     * Saves all config files
     */
    public static void save() {
        saveConfig();
        saveLinking();
        saveSecrets();
    }

    public static void saveConfig() {
        config.save();
    }

    public static void saveLinking() {
        linking.save();
    }

    public static void saveSecrets() {
        secrets.save();
    }

    public static void saveClean() {
        saveCleanConfig();
        saveCleanSecrets();
    }

    public static void saveCleanConfig() {
        config.saveClean(directory);
    }

    public static void saveCleanLinking() {
        linking.saveClean(directory);
    }

    public static void saveCleanSecrets() {
        secrets.saveClean(directory);
    }

    public static ConfigWrapper getConfig() {
        return config.getWrapper();
    }

    public static LinkingWrapper getLinking() {
        return linking.getWrapper();
    }

    public static SecretsWrapper getSecrets() {
        return secrets.getWrapper();
    }
}
