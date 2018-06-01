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

package chikachi.discord.core.config.secrets;

import chikachi.discord.core.config.IConfigurable;
import chikachi.discord.core.config.discord.DiscordSecretConfig;
import com.google.gson.annotations.Since;

@Since(4.0)
public class SecretsWrapper implements IConfigurable {
    @Since(4.0)
    public DiscordSecretConfig discord;

    public void fillFields() {
        if (this.discord == null) {
            this.discord = new DiscordSecretConfig();
        }
        this.discord.fillFields();
    }
}
