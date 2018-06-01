package chikachi.discord.core.config;

import chikachi.discord.core.DiscordIntegrationLogger;
import chikachi.discord.core.config.types.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Since;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

@Since(4.0)
public class ConfigurationFile<T extends IConfigurable> {
    /**
     * This is used to create a new instance of the config, if necessary.
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

        if (!file.exists()) {
            createDefaultConfiguration();
        } else {
            try (FileReader fileReader = new FileReader(this.file)) {
                Gson gson = createGson();
                wrapper = gson.fromJson(fileReader, wrapperClass);
                if (wrapper == null) {
                    wrapper = createAndFillWrapperInstance();
                } else {
                    wrapper.fillFields();
                }
            } catch (Exception e) {
                if (e instanceof JsonSyntaxException) {
                    DiscordIntegrationLogger.Log("Config had invalid syntax - Please check it using a JSON tool ( https://jsonlint.com/ ) or make sure it have the right content", true);
                }

                e.printStackTrace();

                if (wrapper == null) {
                    wrapper = createAndFillWrapperInstance();
                }
            }
        }
    }

    private void createDefaultConfiguration() {
        wrapper = createAndFillWrapperInstance();
        if (wrapper != null) {
            save();
        }
    }

    private T createAndFillWrapperInstance() {
        T result = null;
        try {
            result = wrapperClass.newInstance();
            result.fillFields();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Could not create a wrapper instance.");
            e.printStackTrace();
        }
        return result;
    }

    public void save() {
        Gson gson = createGson();

        if (wrapper == null) {
            wrapper = createAndFillWrapperInstance();
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            writer.write(gson.toJson(wrapper));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveClean(File directory) {
        Gson gson = createGson();
        try (FileWriter writer = new FileWriter(this.file.getPath().replace(".json", "_clean.json"));) {
            ConfigWrapper cleanConfig = new ConfigWrapper();
            cleanConfig.fillFields();
            writer.write(gson.toJson(cleanConfig));
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
            .setVersion(4.0)
            .setPrettyPrinting()
            .create();
    }
}
