package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly_neoforge.AssemblyPayload;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.neoforged.neoforge.client.network.registration.ClientNetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author 6hisea
 * @date 2025/10/17 13:58
 * @description: None
 */
@Mixin(ClientNetworkRegistry.class)
public class ClientNetworkRegistryMixin {
    @Inject(at=@At("HEAD"),method = "handleModdedPayload",cancellable = true)
    private static void handleModdedPayloadInject(ClientCommonPacketListener listener, ClientboundCustomPayloadPacket packet, CallbackInfo ci){
        if (packet.payload() instanceof AssemblyPayload) {
            ci.cancel();
        }
    }
}
