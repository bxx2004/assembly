package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly.application.client.ClientResourceManager;
import net.bxx2004.assembly_neoforge.AssemblyResourcePack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author 6hisea
 * @date 2025/10/25 17:00
 * @description: None
 */
@Mixin(ReloadableResourceManager.class)
public class ResourceManagerMixin {
    @Inject(at = @At("HEAD"),method = "getResource",cancellable = true)
    private void getResource(ResourceLocation location, CallbackInfoReturnable<Optional<Resource>> cir) {
        if (location.getNamespace().equals("ref")) {
            Resource resource = AssemblyResourcePack.INSTANCE.getResource(ClientResourceManager.INSTANCE,location);
            if (resource != null) {
                cir.setReturnValue(Optional.of(resource));
            }else {
                cir.setReturnValue(Optional.empty());
            }
        }
    }
}
