import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import com.felipe.aprendamosalimpiar.data.models.ConfiguracionJuego


class TTSHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var isReady = false


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            setVoice(ConfiguracionJuego.voz) // Configura la voz al iniciar
        } else {
            Log.e("TTS_ERROR", "Error al inicializar TTS")
        }
    }


    fun speak(text: String) {
        if (!isReady ) return

        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, "TTS_UTTERANCE")
        }else {
            Log.e("TTS_ERROR", "TTS no inicializado. Intenta más tarde.")
            // Opcional: Reintentar después de un breve retraso
            Handler(Looper.getMainLooper()).postDelayed({
                speak(text)
            }, 1000)
        }
    }

    private fun setVoice(tipoVoz: ConfiguracionJuego.TipoVoz) {
        when (tipoVoz) {
            ConfiguracionJuego.TipoVoz.FEMENINA ->
                tts.voice = tts.voices.firstOrNull {
                    it.name.contains(
                        "es-es-x-eec-local",
                        ignoreCase = true
                    )
                }

            ConfiguracionJuego.TipoVoz.MASCULINA ->
                tts.voice = tts.voices.firstOrNull {
                    it.name.contains(
                        "es-es-x-eef-local",
                        ignoreCase = true
                    )
                }

            else -> {}
        }
    }


    fun shutdown() {
        tts.shutdown()
        isReady = false
    }
}