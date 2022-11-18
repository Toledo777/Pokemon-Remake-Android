package ca.dawsoncollege.project_pokemon

import android.content.Context
import java.io.IOException
import com.google.gson.Gson

class JSON {
    companion object Helper {
        private val gson = Gson()

        fun getJsonData(context: Context, fileName: String, dataClass: Class<*>?): Any {
            val jsonString = getAssetJson(context, fileName)
            return gson.fromJson(jsonString, dataClass)
        }

        private fun getAssetJson(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }
}
