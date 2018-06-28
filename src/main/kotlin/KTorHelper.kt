import com.google.gson.Gson
import io.ktor.application.ApplicationCall
import io.ktor.request.receiveText

val gson = Gson()

suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    return gson.fromJson(this.receiveText(), T::class.java)
}