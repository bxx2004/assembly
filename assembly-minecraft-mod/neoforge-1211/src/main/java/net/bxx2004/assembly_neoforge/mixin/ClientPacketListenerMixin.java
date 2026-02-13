package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly_neoforge.AssemblyPayload;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.neoforged.neoforge.network.payload.MinecraftRegisterPayload;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author 6hisea
 * @date 2025/10/17 14:08
 * @description: None
 */
@Mixin(ClientCommonPacketListenerImpl.class)
public class ClientPacketListenerMixin {
    @Shadow @Final protected Connection connection;

    @Inject(at = @At("HEAD"),method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V", cancellable = true)
    public void handleCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (packet.payload() instanceof MinecraftRegisterPayload payload) {
            NetworkRegistry.onMinecraftUnregister(connection,payload.newChannels());
        }
        if (packet.payload() instanceof AssemblyPayload payload){
            ci.cancel();
        }
    }
}
  