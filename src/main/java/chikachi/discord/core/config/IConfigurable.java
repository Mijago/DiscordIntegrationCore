package chikachi.discord.core.config;

import java.io.Serializable;

public interface IConfigurable extends Serializable {
    void fillFields();
}
