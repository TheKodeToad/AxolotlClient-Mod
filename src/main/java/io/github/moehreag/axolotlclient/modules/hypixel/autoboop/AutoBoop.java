package io.github.moehreag.axolotlclient.modules.hypixel.autoboop;

import io.github.moehreag.axolotlclient.config.options.BooleanOption;
import io.github.moehreag.axolotlclient.config.options.OptionCategory;
import io.github.moehreag.axolotlclient.modules.hypixel.AbstractHypixelMod;
import io.github.moehreag.axolotlclient.util.Util;
import net.minecraft.text.Text;

public class AutoBoop implements AbstractHypixelMod {
    public static AutoBoop Instance = new AutoBoop();

    protected OptionCategory cat = new OptionCategory("autoBoop");
    protected BooleanOption enabled = new BooleanOption("enabled", false);

    @Override
    public void init() {

        cat.add(enabled);
    }

    @Override
    public OptionCategory getCategory() {
        return cat;
    }

    public void onMessage(Text message){
        if(message.getString().contains("Friend >") && message.getString().contains("joined.")){
            String player = message.getString().substring(message.getString().indexOf(">") + 2, message.getString().lastIndexOf(" "));
            Util.sendChatMessage( "/boop "+player);
        }
    }
}
