package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly_neoforge.AssemblyPayload;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author 6hisea
 * @date 2025/10/17 14:08
 * @description: None
 */
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(at = @At("HEAD"),method = "handleCustomPayload", cancellable = true)
    public void handleCustomPayload(CustomPacketPayload packet, CallbackInfo ci) {
        if (packet instanceof AssemblyPayload payload){
            ci.cancel();
        }
    }
}
