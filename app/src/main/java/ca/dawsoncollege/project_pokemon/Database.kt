package ca.dawsoncollege.project_pokemon

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// db
@Database(entities = [PlayerTrainer::class], version = 1)
@TypeConverters(DataConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Dao
interface UserDao {
    // save trainer to db
    @Insert
    fun savePlayerTrainer(player: PlayerTrainer)

    // Check if a save is present in the database
    @Query("SELECT EXISTS (SELECT * FROM PlayerTrainer)")
    fun checkSaveInDatabase(): Boolean

    // fetch trainer
    @Query("SELECT * FROM PlayerTrainer")
    fun fetchPlayerSave(): PlayerTrainer

    // clear database
    @Delete
    fun delete(playerTrainer: PlayerTrainer)
}

// type convert to convert pok  emon arraylist to json and vice versa
class DataConverter {
    @TypeConverter
    fun pokeListToJson(pokeList: ArrayList<Pokemon>): String {
        val gson = Gson();
        val type = object : TypeToken<ArrayList<Pokemon>>() {}.type
        return gson.toJson(pokeList, type)
    }

    @TypeConverter
    fun jsonToPokeList(jsonString: String): ArrayList<Pokemon> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Pokemon>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}