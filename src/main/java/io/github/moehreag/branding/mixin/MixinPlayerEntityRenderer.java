package io.github.moehreag.branding.mixin;

import io.github.moehreag.branding.Axolotlclient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Target;
import java.util.Objects;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer { //extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>{

	/*public MixinPlayerEntityRenderer(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowSize) {
		super(dispatcher, model, shadowSize);
	}*/
	private String badge = "abc";

	@Inject(method = "renderLabelIfPresent",
		at = @At(value = "INVOKE",
			//target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			//ordinal = 1),
			target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1)
	)//cancellable = true)
	private void addAxololtclientBadge(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci){
		//Objects.requireNonNull(this.gameProfile.getId());

		text = new LiteralText(badge + text.asString());
		//ci.cancel();
/**
		try {
			if (Axolotlclient.CONFIG.showBadge && Axolotlclient.isUsingClient(this.gameProfile.getId())) {
				text = new LiteralText("✵ " + this.gameProfile.getName());
				ci.cancel();
			}

		} catch( Exception e){
			System.out.println("Is the Game running in Offline Mode? Ignoring...");
			if (Axolotlclient.CONFIG.showBadge) {
				text = new LiteralText("✵ " + text.asString());
			//}
			ci.cancel();
		//}*/
	}
}
