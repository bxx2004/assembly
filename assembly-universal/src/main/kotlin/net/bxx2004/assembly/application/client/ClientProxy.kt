package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2025/10/18 13:38
 * @description: None
 */
abstract class ClientProxy {
    init {
        if (Assembly.side != Side.CLIENT){
            throw RuntimeException("class ${this.javaClass.name} is only running with client")
        }
    }
}