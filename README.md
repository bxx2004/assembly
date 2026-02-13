# Message negotiation system
You can use this tool to provide convenience for your network message transmission.

## Minecraft Implementation
|  Platform   | minecraft-version | assembly-version | Status |
|  :----:  |:-----------------:|:----------------:|:------:|
| Bukkit  |   [1.9-1.21.9]    |       2.0        |  LTS   |
| Velocity  |         -         |       2.0        |  LTS   |
| Bungeecord  |         -         |       2.0        |   NS   |
| NeoForge  |  [1.21.1-1.21.9)  |       2.0        |  LTS   |
| FabricMC  |  [1.21.1-1.21.9)  |       2.0        |  Plan  |

If you are interested in this project, you can help achieve it

### On Bukkit
- Send

```kotlin
val packet = AssemblyPacket(
    AssemblyPacketMeta(
        AssemblyIdentifier(
            "custom-message",
            "title"
        )
    )
).write("title","Minecraft !!!")
Bukkit.getPlayer("bxx2004")!!.asPacketSender.send(Packet)
```

- Receive

```kotlin
@SubscribeEvent
fun onReceive(e: AssemblyPacketReceiveEvent){
    e.packet.debug()
}
```

### On NeoForge
- Send

```kotlin
val packet = AssemblyPacket(
    AssemblyPacketMeta(
        AssemblyIdentifier(
            "custom-message",
            "title"
        )
    )
).write("title","Minecraft !!!")
NeoForgeSender.send(Packet)
```

- Receive

```kotlin
@SubscribeEvent
fun onReceive(e: AssemblyPacketReceiveEvent){
    e.packet.debug()
}
```
