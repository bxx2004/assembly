package net.bxx2004.assembly.application.handler

import net.bxx2004.assembly.application.entity.CustomRequest
import net.bxx2004.assembly.network.controller.PacketSender

/**
 * @author 6hisea
 * @date  2026/2/7 11:44
 * @description: None
 */
interface CustomRequestHandler {
    fun onReceive(sender: PacketSender, packet: CustomRequest)
}