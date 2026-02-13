package net.bxx2004.assembly_neoforge.mixin;


import net.bxx2004.assembly_minecraft.application.font.FontApplication;
import net.bxx2004.assembly_minecraft.application.font.instances.TrueTypeFont;
import net.bxx2004.assembly_neoforge.application.font.NTrueTypeFont;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.bxx2004.assembly.application.*;
import java.util.List;
import java.util.Optional;

/**
 * @author 6hisea
 * @date 2026/2/11 11:14
 * @description: None
 */
@Mixin(Font.class)
public class FontMixin {
    @Inject(at=@At("HEAD"),method = "getFontSet",cancellable = true)
    public void getFontSet(ResourceLocation fontLocation, CallbackInfoReturnable<FontSet> cir){
        List<AssemblyInstance> ins = FontApplication.Companion.getInstances(FontApplication.INSTANCE);
        List<NTrueTypeFont> nttf =  ins.stream().filter((is)->is instanceof NTrueTypeFont).map(
                (e)-> (NTrueTypeFont)e
        ).toList();
        for (NTrueTypeFont item : nttf) {
            if (
                    item.getId().getNamespace().equalsIgnoreCase(fontLocation.getNamespace()) &&
                            item.getId().getPath().equalsIgnoreCase(fontLocation.getPath())
            ){
                if (item.isReady().getFirst()){
                    cir.setReturnValue(item.getCache());
                    return;
                }
            }
        }
        var gnttf =nttf.stream().filter(TrueTypeFont::getGlobal).findFirst();
        if (!gnttf.isEmpty()){
            if (gnttf.get().isReady().getFirst()){
                cir.setReturnValue(gnttf.get().getCache());
            }
        }
    }
}