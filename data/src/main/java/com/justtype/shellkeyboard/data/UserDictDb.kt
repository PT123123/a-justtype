package com.justtype.shellkeyboard.data

import android.content.Context
import androidx.room.*
import android.content.Context

/**
 * Room database for user dictionary.
 */
@Database(entities = [UserWord::class], version = 1, exportSchema = false)
abstract class UserDictDb : RoomDatabase() {
    abstract fun userWordDao(): UserWordDao

    companion object {
        @Volatile
        private var INSTANCE: UserDictDb? = null

        fun getInstance(context: Context): UserDictDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDictDb::class.java,
                    "user_dict.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Entity(tableName = "user_words")
data class UserWord(
    @PrimaryKey val word: String,
    val frequency: Int = 1,
    val lastUsed: Long = System.currentTimeMillis()
)

@Dao
interface UserWordDao {
    @Query("SELECT * FROM user_words ORDER BY frequency DESC, lastUsed DESC")
    suspend fun getAll(): List<UserWord>

    @Query("SELECT * FROM user_words WHERE word = :word LIMIT 1")
    suspend fun findByWord(word: String): UserWord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: UserWord)

    @Delete
    suspend fun delete(word: UserWord)

    @Query("DELETE FROM user_words")
    suspend fun deleteAll()
}
