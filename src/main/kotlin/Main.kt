import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import kotlin.math.sin
import kotlin.math.PI

fun main(args: Array<String>) {

    val str = PSK31().text("Hello, World!\n")

    val  pulse = 256

    val sampleRate = 8000
    val buffer = ByteArray(pulse * str.length)

    var vol = 127.0f

    var n = 0
    var k = pulse

    for (j in str.indices) {
        if(str[j].code.toChar() == '0') {
            vol = -vol
            for (i in n until k) {
                buffer[i] = (sin(2.0 * PI * i / (sampleRate/1000)) * vol).toInt().toByte()
            }
        } else {
            for (i in n until k) {
                buffer[i] = (sin(2.0 * PI * i / (sampleRate/1000)) * vol).toInt().toByte()
            }
        }

        n += pulse
        k += pulse
    }

    val format = AudioFormat(sampleRate.toFloat(), 8, 1, true, true)
    val line = AudioSystem.getSourceDataLine(format)

    with (line) {
        open(format)
        start()
        write(buffer, 0, buffer.size)
        drain()
        close()
    }
}