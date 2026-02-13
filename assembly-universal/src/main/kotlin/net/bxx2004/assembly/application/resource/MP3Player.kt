package net.bxx2004.assembly.application.resource


import com.goxr3plus.streamplayer.stream.StreamPlayer
import java.io.InputStream

class MP3Player(val stream: InputStream):AutoCloseable {
    val player = StreamPlayer()
    init {
        player.open(stream)
    }

    fun play(){
        player.play()
    }
    fun stop(){
        player.stop()
    }
    fun setVolume(volume: Double){
        player.setGain(volume)
    }
    fun getVolume(): Float{
        return player.gainValue
    }
    fun isPlaying(): Boolean{
        return player.isPlaying
    }
    fun isStopped(): Boolean{
        return player.isStopped
    }
    fun pause(){
        player.pause()
    }
    fun resume(){
        player.resume()
    }
    fun setSpeed(speed: Double){
        player.speedFactor = speed
    }

    fun getSpeed(): Double{
        return player.speedFactor
    }

    fun seek(v:Int){
        player.seekTo(v)
    }

    override fun close() {
        player.stop()
        stream.close()
    }
}
fun InputStream.toMP3Player(): MP3Player {
    return MP3Player(this)
}