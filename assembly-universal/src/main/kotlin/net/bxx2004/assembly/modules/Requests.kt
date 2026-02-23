package net.bxx2004.assembly.modules

import net.bxx2004.assembly.application.entity.CustomRequest
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.network.packet.entity.sendWithResponse
import net.bxx2004.script.module.IModule
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * @author 6hisea
 * @date  2026/1/5 14:04
 * @description: None
 */
object Requests : IModule(){
    override val isInject: Boolean = true
    override fun name(): Array<String> {
        return arrayOf("Request","request")
    }
    fun custom(sender: PacketSender, path:String, data:Map<String,Any?>, func: AssemblyEntity.() -> Unit){
        AssemblyEntity.build<CustomRequest> {
            this.path = path
            this.data = data
        }.sendWithResponse<AssemblyEntity>(sender){
            func(this)
        }
    }
    fun custom(sender: PacketSender, path:String, data:Map<String,Any?>,timeout:Long = 20000,timeoutFunc:()->Unit, func: AssemblyEntity.() -> Unit){
        AssemblyEntity.build<CustomRequest> {
            this.path = path
            this.data = data
        }.sendWithResponse<AssemblyEntity>(sender,timeout,timeoutFunc){
            func(this)
        }
    }
    fun get(url: String, header: Map<String, String>, error:(String) -> Unit, func: (String) -> Unit) {
        thread {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                // 设置请求头
                header.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    func(response.toString())
                } else {
                    error(responseCode.toString())
                }
                connection.disconnect()
            } catch (e: Exception) {
                error(e.message?:"")
            }
        }
    }

    fun post(
        url: String,
        header: Map<String, String> = emptyMap(),
        body: String = "",
        error: (String) -> Unit,
        func: (String) -> Unit
    ) {
        thread {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.doOutput = true  // 允许输出数据
                connection.doInput = true   // 允许输入数据

                // 设置请求头
                header.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }

                // 如果没有指定 Content-Type，默认使用 application/json
                if (!header.containsKey("Content-Type")) {
                    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                }

                // 写入请求体
                if (body.isNotEmpty()) {
                    val outputStream = connection.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                    writer.write(body)
                    writer.flush()
                    writer.close()
                    outputStream.close()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    inputStream.close()

                    func(response.toString())
                } else {
                    // 尝试读取错误信息
                    val errorStream = connection.errorStream
                    if (errorStream != null) {
                        val reader = BufferedReader(InputStreamReader(errorStream))
                        val errorResponse = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            errorResponse.append(line)
                        }
                        reader.close()
                        errorStream.close()

                        error("$responseCode: ${errorResponse.toString()}")
                    } else {
                        error(responseCode.toString())
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                error(e.message ?: "Unknown error")
            }
        }
    }
}