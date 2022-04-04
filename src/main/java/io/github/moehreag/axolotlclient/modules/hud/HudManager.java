package io.github.moehreag.axolotlclient.modules.hud;

import io.github.moehreag.axolotlclient.Axolotlclient;
import io.github.moehreag.axolotlclient.config.options.OptionCategory;
import io.github.moehreag.axolotlclient.modules.AbstractModule;
import io.github.moehreag.axolotlclient.modules.hud.gui.AbstractHudEntry;
import io.github.moehreag.axolotlclient.modules.hud.gui.hud.ArmorHud;
import io.github.moehreag.axolotlclient.modules.hud.gui.hud.CPSHud;
import io.github.moehreag.axolotlclient.modules.hud.gui.hud.FPSHud;
import io.github.moehreag.axolotlclient.modules.hud.gui.hud.PingHud;
import io.github.moehreag.axolotlclient.modules.hud.util.Rectangle;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This implementation of Hud modules is based on KronHUD.
 * https://github.com/DarkKronicle/KronHUD
 * Licensed under GPL-3.0
 */


public class HudManager extends AbstractModule {

    private final Map<Identifier, AbstractHudEntry> entries = new HashMap<>();

    private final OptionCategory hudCategory = new OptionCategory(new Identifier("hud"), "hud");

    private final MinecraftClient client = MinecraftClient.getInstance();
    private static final HudManager INSTANCE = new HudManager();

    static KeyBinding key = new KeyBinding("key.openHud", 54, "category.axolotlclient");

    public void save(){

    }

    public static HudManager getINSTANCE(){
        return INSTANCE;
    }

    public void init(){

        KeyBindingHelper.registerKeyBinding(key);

        Axolotlclient.CONFIG.addCategory(hudCategory);

        HudRenderCallback.EVENT.register((tickDelta, v)->render());

        add(new PingHud());
        add(new FPSHud());
        add(new CPSHud());
        add(new ArmorHud());
    }

    public static void tick(){
        if(key.isPressed()) MinecraftClient.getInstance().openScreen(new HudEditScreen());
        INSTANCE.entries.forEach((identifier, abstractHudEntry) -> {
            if(abstractHudEntry.tickable())abstractHudEntry.tick();
        });
    }

    public HudManager add(AbstractHudEntry entry) {
        entries.put(entry.getId(), entry);
        hudCategory.addSubCategory(entry.getAllOptions());
        return this;
    }

    public List<AbstractHudEntry> getEntries() {
        if (entries.size() > 0) {
            return new ArrayList<>(entries.values());
        }
        return new ArrayList<>();
    }

    public List<AbstractHudEntry> getMovableEntries() {
        if (entries.size() > 0) {
            return entries.values().stream().filter((entry) -> entry.isEnabled() && entry.movable()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public AbstractHudEntry get(Identifier identifier) {
        return entries.get(identifier);
    }

    public void render() {
        if (!(client.currentScreen instanceof HudEditScreen) && !client.options.debugEnabled) {
            for (AbstractHudEntry hud : getEntries()) {
                if (hud.isEnabled()) {
                    hud.renderHud();
                }
            }
        }
    }

    public Optional<AbstractHudEntry> getEntryXY(int x, int y) {
        for (AbstractHudEntry entry : getMovableEntries()) {
            Rectangle bounds = entry.getScaledBounds();
            if (bounds.x <= x && bounds.x + bounds.width >= x && bounds.y <= y && bounds.y + bounds.height >= y) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    public void renderPlaceholder() {
        for (AbstractHudEntry hud : getEntries()) {
            if (hud.isEnabled()) {
                hud.renderPlaceholder();
            }
        }
    }

    public List<Rectangle> getAllBounds() {
        ArrayList<Rectangle> bounds = new ArrayList<>();
        for (AbstractHudEntry entry : getMovableEntries()) {
            bounds.add(entry.getScaledBounds());
        }
        return bounds;
    }
}
