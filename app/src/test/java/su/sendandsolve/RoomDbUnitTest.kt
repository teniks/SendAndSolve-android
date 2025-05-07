package su.sendandsolve

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.repository.UserRepository
import su.sendandsolve.features.tasks.domain.model.User
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomDbUnitTest {
    private lateinit var db: RoomAppDatabase
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomAppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userRepository = UserRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createAndReadUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", salt = "testSalt", nickname = "testNick")
        userRepository.insert(user)
        val tUser = db.userDao().getById(user.uuid)
        //assertSame(tUser, user)
        assertEquals(user.login, tUser?.login)
        assertEquals(user.uuid, tUser?.uuid)
    }

    @Test
    fun updateUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", salt = "testSalt", nickname = "testNick")
        userRepository.insert(user)

        val newLogin = "newLogin"
        userRepository.insert(user)
        val tUser = user.copy(login = newLogin)
        userRepository.update(tUser)

        val result = db.userDao().getById(user.uuid)
        assertSame(newLogin, result?.login)
    }

    @Test
    fun deleteUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", salt = "testSalt", nickname = "testNick")
        userRepository.insert(user)

        userRepository.delete(user)

        val result = db.userDao().getById(user.uuid)
        assertNull(result)
    }
}