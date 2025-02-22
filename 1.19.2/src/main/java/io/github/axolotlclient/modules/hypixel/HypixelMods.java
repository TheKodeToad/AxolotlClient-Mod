/*
 * Copyright © 2021-2023 moehreag <moehreag@gmail.com> & Contributors
 *
 * This file is part of AxolotlClient.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * For more information, see the LICENSE file.
 */

package io.github.axolotlclient.modules.hypixel;

import io.github.axolotlclient.AxolotlClient;
import io.github.axolotlclient.AxolotlClientConfig.options.EnumOption;
import io.github.axolotlclient.AxolotlClientConfig.options.OptionCategory;
import io.github.axolotlclient.AxolotlClientConfig.options.StringOption;
import io.github.axolotlclient.modules.AbstractModule;
import io.github.axolotlclient.modules.hypixel.autoboop.AutoBoop;
import io.github.axolotlclient.modules.hypixel.autogg.AutoGG;
import io.github.axolotlclient.modules.hypixel.autotip.AutoTip;
import io.github.axolotlclient.modules.hypixel.levelhead.LevelHead;
import io.github.axolotlclient.modules.hypixel.nickhider.NickHider;
import io.github.axolotlclient.modules.hypixel.skyblock.Skyblock;

import java.util.ArrayList;
import java.util.List;

public class HypixelMods extends AbstractModule {

	private static final HypixelMods INSTANCE = new HypixelMods();

	public StringOption hypixel_api_key = new StringOption("hypixel_api_key", "");
	public final EnumOption cacheMode = new EnumOption("cache_mode", HypixelCacheMode.values(),
			HypixelCacheMode.ON_CLIENT_DISCONNECT.toString());

	private final OptionCategory category = new OptionCategory("hypixel-mods");
	private final List<AbstractHypixelMod> subModules = new ArrayList<>();

	public static HypixelMods getInstance() {
		return INSTANCE;
	}

	@Override
	public void init() {
		category.add(hypixel_api_key);
		category.add(cacheMode);

		addSubModule(LevelHead.getInstance());
		addSubModule(AutoGG.getInstance());
		addSubModule(AutoTip.getInstance());
		addSubModule(NickHider.getInstance());
		addSubModule(AutoBoop.getInstance());
		addSubModule(Skyblock.getInstance());

		subModules.forEach(AbstractHypixelMod::init);

		AxolotlClient.CONFIG.addCategory(category);
	}

	public void tick() {
		subModules.forEach(abstractHypixelMod -> {
			if (abstractHypixelMod.tickable())
				abstractHypixelMod.tick();
		});
	}

	@Override
	public void lateInit() {
		HypixelAbstractionLayer.setApiKeySupplier(() -> hypixel_api_key.get());
		HypixelAbstractionLayer.loadApiKey();
	}

	private void addSubModule(AbstractHypixelMod mod) {
		this.subModules.add(mod);
		this.category.addSubCategory(mod.getCategory());
	}

	public OptionCategory getCategory() {
		return null;
	}

	public enum HypixelCacheMode {
		ON_CLIENT_DISCONNECT, ON_PLAYER_DISCONNECT
	}
}
