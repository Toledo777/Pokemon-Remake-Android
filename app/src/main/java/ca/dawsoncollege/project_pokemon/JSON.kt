package ca.dawsoncollege.project_pokemon

import android.content.Context
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JSON {
    companion object Helper {
        private val gson = Gson()

        fun getJsonData(context: Context, fileName: String): Any {
            val jsonString = getAssetJson(context, fileName)
            return gson.toJson(jsonString)
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