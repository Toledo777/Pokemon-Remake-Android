package ca.dawsoncollege.project_pokemon

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// db
@Database(entities = [PlayerTrainer::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Dao
interface UserDao {

}