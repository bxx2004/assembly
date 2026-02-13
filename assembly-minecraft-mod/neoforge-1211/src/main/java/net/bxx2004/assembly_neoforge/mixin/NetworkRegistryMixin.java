package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly.Assembly;
import net.bxx2004.assembly_neoforge.AssemblyPayload;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author 6hisea
 * @date 2025/10/17 13:22
 * @description: None
 */
@Mixin(NetworkRegistry.class)
public class NetworkRegistryMixin {
    @Inject(at = @At("HEAD"),method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V",cancellable = true)
    private static void checkPacketInject(Packet<?> packet, ClientCommonPacketListener listener, CallbackInfo ci) {
        if (packet instanceof ServerboundCustomPayloadPacket customPayloadPacket){
            if (customPayloadPacket.payload() instanceof AssemblyPayload){
                ci.cancel();
            }
        }
    }
    @Inject(at = @At("HEAD"),method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V",cancellable = true)
    private static void checkPacketInject(Packet<?> packet, ServerCommonPacketListener listener, CallbackInfo ci) {
        if (packet instanceof ClientboundCustomPayloadPacket customPayloadPacket){
            if (customPayloadPacket.payload() instanceof AssemblyPayload){
                ci.cancel();
            }
        }
    }
    @Inject(at = @At("HEAD"),method = "getCodec",cancellable = true)
    private static void getCodeCInject(ResourceLocation id, ConnectionProtocol protocol, PacketFlow flow, CallbackInfoReturnable<StreamCodec<? super FriendlyByteBuf, ? extends CustomPacketPayload>> cir){
        if (id.toString().equals(Assembly.INSTANCE.getCHANNEL())){
            cir.setReturnValue(AssemblyPayload.Companion.getSTREAM_CODEC());
        }
    }
}
