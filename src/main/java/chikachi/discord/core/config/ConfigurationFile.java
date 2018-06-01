package chikachi.discord.core.config;

import chikachi.discord.core.CoreConstants;
import chikachi.discord.core.DiscordIntegrationLogger;
import chikachi.discord.core.config.types.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Since;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.regex.Pattern;

@Since(4.0)
public class ConfigurationFile<T extends IConfigurable> {
    /**
     * This is used to create a new instance of the config, if necessary
     */
    private Class<T> wrapperClass;

    private T wrapper;
    private File file;

    public ConfigurationFile(@NotNull Class<T> wrapperClass, @NotNull File file) {
        this.wrapperClass = wrapperClass;
        this.file = file;
    }

    public T getWrapper() {
        return wrapper;
    }

    public void load() {
        if (file == null) {
            return;
        }
        Gson gson = createGson();

        if (!file.exists()) {
            createDefaultConfiguration();
        } else {
            try (FileReader fileReader = new FileReader(this.file)){
                wrapper = gson.fromJson(fileReader, wrapperClass);
                if (wrapper == null) {
                    wrapper = createWrapperInstance();
                }
                wrapper.fillFields();
            } catch (Exception e) {
                if (e instanceof JsonSyntaxException) {
                    DiscordIntegrationLogger.Log("Config had invalid syntax - Please check it using a JSON tool ( https://jsonlint.com/ ) or make sure it have the right content", true);
                }

                e.printStackTrace();

                if (wrapper == null) {
                    wrapper = createWrapperInstance();
                    if (wrapper != null) {
                        wrapper.fillFields();
                    }
                }
            }
        }
    }

    private void createDefaultConfiguration() {
        T wrapper = createWrapperInstance();
        if (wrapper == null) return;
        this.wrapper.fillFields();
        save();
    }

    private T createWrapperInstance() {
        T result = null;
        try {
            result = wrapperClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Could not create a wrapper instance.");
            e.printStackTrace();
        }
        return result;
    }

    public void save() {
        Gson gson = createGson();

        //TODO: create empty
        if (wrapper == null)
            return;

        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(gson.toJson(wrapper));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveClean(File directory) {
        Gson gson = createGson();
        try {
            FileWriter writer = new FileWriter(new File(directory, CoreConstants.MODID + "_clean.json"));
            ConfigWrapper cleanConfig = new ConfigWrapper();
            cleanConfig.fillFields();
            writer.write(gson.toJson(cleanConfig));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Gson createGson() {
        return new GsonBuilder()
            .registerTypeAdapter(ChannelConfigType.class, new ChannelConfigTypeAdapter())
            .registerTypeAdapter(DimensionConfigType.class, new DimensionConfigTypeAdapter())
            .registerTypeAdapter(MessageConfig.class, new MessageConfigAdapter())
            .registerTypeAdapter(Pattern.class, new PatternAdapter())
            .setVersion(3.0)
            .setPrettyPrinting()
            .create();
    }
}
