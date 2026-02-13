package net.bxx2004.assembly_neoforge.mixin;

import net.bxx2004.assembly.application.AssemblyInstance;
import net.bxx2004.assembly_minecraft.application.font.FontApplication;
import net.bxx2004.assembly_minecraft.application.key.KeyApplication;
import net.bxx2004.assembly_minecraft.application.key.instances.SimpleKey;
import net.bxx2004.assembly_neoforge.application.font.NTrueTypeFont;
import net.bxx2004.assembly_neoforge.application.key.NSimpleKey;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author 6hisea
 * @date 2026/2/12 16:47
 * @description: None
 */
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(at = @At("HEAD"),method = "keyPress")
    public void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci){
        List<AssemblyInstance> ins = KeyApplication.Companion.getInstances(KeyApplication.INSTANCE);
        List<NSimpleKey> smkey =  ins.stream().filter((is)->is instanceof NSimpleKey).map(
                (e)-> (NSimpleKey)e
        ).toList();
        for (NSimpleKey simpleKey : smkey) {
            if (simpleKey.getKey() == key){
                simpleKey.getCallback().invoke(windowPointer,key,scanCode,action,modifiers);
            }
        }
    }
}
