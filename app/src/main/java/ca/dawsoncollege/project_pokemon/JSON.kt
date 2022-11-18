package ca.dawsoncollege.project_pokemon

import android.content.Context
import java.io.IOException

class JSON {
    companion object Helper {
        fun getAssetJsonData(context: Context, fileName: String): String? {
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