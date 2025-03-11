package su.sendandsolve

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.repository.*
import su.sendandsolve.data.domain.model.*
import java.time.Instant
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomDbInstrumentedTest {
    private lateinit var db: RoomAppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var teamRepository: TeamRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var noteRepository: NoteRepository
    private lateinit var deviceRepository: DeviceRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomAppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userRepository = UserRepository(db)
        teamRepository = TeamRepository(db)
        taskRepository = TaskRepository(db)
        tagRepository = TagRepository(db)
        sessionRepository = SessionRepository(db)
        resourceRepository = ResourceRepository(db)
        noteRepository = NoteRepository(db)
        deviceRepository = DeviceRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createAndReadUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val tUser = db.userDao().getById(user.uuid)
        assertEquals(user.uuid, tUser?.uuid)
    }

    @Test
    fun updateUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val new = "newLogin"
        val t = user.copy(login = new)
        userRepository.update(t)

        val result = db.userDao().getById(user.uuid)
        assertEquals(new, result?.login)
    }

    @Test
    fun deleteUser(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        userRepository.delete(user)

        val result = userRepository.getById(user.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadTeam(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val team: Team = Team(UUID.randomUUID(), "testTeam", user.uuid, lastModified = Instant.now())
        teamRepository.insert(team)

        val t = teamRepository.getById(team.uuid)
        assertEquals(team.uuid, t?.uuid)
    }

    @Test
    fun updateTeam(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val team: Team = Team(UUID.randomUUID(), "testTeam", user.uuid, lastModified = Instant.now())
        teamRepository.insert(team)

        val new = "newTeam"
        val t = team.copy(name = new)
        teamRepository.update(t)

        val result = teamRepository.getById(team.uuid)
        assertEquals(new, result?.name)
    }

    @Test
    fun deleteTeam(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val team: Team = Team(UUID.randomUUID(), "testTeam", user.uuid, lastModified = Instant.now())
        teamRepository.insert(team)

        teamRepository.delete(team)
        userRepository.delete(user)

        val result = teamRepository.getById(team.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadTask(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val task: Task = Task(UUID.randomUUID(), "testTask", "testDescription", "testStatus", 10, Instant.now(), null, 0,  user.uuid, "personal", null, Instant.now())
        taskRepository.insert(task)

        val t = taskRepository.getById(task.uuid)
        assertEquals(task.uuid, t?.uuid)
    }

    @Test
    fun updateTask(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val task: Task = Task(UUID.randomUUID(), "testTask", "testDescription", "testStatus", 10, Instant.now(), null, 0,  user.uuid, "personal", null, Instant.now())
        taskRepository.insert(task)

        val new = "newTitle"
        val t = task.copy(title = new)
        taskRepository.update(t)

        val result = taskRepository.getById(task.uuid)
        assertEquals(new, result?.title)
    }

    @Test
    fun deleteTask(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val task: Task = Task(UUID.randomUUID(), "testTask", "testDescription", "testStatus", 10, Instant.now(), null, 0,  user.uuid, "personal", null, Instant.now())
        taskRepository.insert(task)

        taskRepository.delete(task)
        userRepository.delete(user)

        val result = teamRepository.getById(task.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadTag(): Unit = runBlocking {
        val tag: Tag = Tag(UUID.randomUUID(), "testTag", mutableMapOf(), mutableMapOf())
        tagRepository.insert(tag)

        val t = tagRepository.getById(tag.uuid)
        assertEquals(tag.uuid, t?.uuid)
    }

    @Test
    fun updateTag(): Unit = runBlocking {
        val tag: Tag = Tag(UUID.randomUUID(), "testTag", mutableMapOf(), mutableMapOf())
        tagRepository.insert(tag)

        val new = "newName"
        val t = tag.copy(name = new)
        tagRepository.update(t)

        val result = tagRepository.getById(tag.uuid)
        assertEquals(new, result?.name)
    }

    @Test
    fun deleteTag(): Unit = runBlocking {
        val tag: Tag = Tag(UUID.randomUUID(), "testTag", mutableMapOf(), mutableMapOf())
        tagRepository.insert(tag)

        tagRepository.delete(tag)

        val result = tagRepository.getById(tag.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadSession(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        val session: Session = Session(UUID.randomUUID(), user.uuid, "testToken", Instant.now(), Instant.now(), device.uuid)
        sessionRepository.insert(session)

        val t = sessionRepository.getById(session.uuid)
        assertEquals(session.uuid, t?.uuid)
    }

    @Test
    fun updateSession(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        val session: Session = Session(UUID.randomUUID(), user.uuid, "testToken", Instant.now(), Instant.now(), device.uuid)
        sessionRepository.insert(session)

        val new = "newToken"
        val t = session.copy(token = new)
        sessionRepository.update(t)

        val result = sessionRepository.getById(session.uuid)
        assertEquals(new, result?.token)
    }

    @Test
    fun deleteSession(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        val session: Session = Session(UUID.randomUUID(), user.uuid, "testToken", Instant.now(), Instant.now(), device.uuid)
        sessionRepository.insert(session)

        sessionRepository.delete(session)

        val result = tagRepository.getById(session.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadResource(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        val session: Session = Session(UUID.randomUUID(), user.uuid, "testToken", Instant.now(), Instant.now(), device.uuid)
        sessionRepository.insert(session)

        val t = sessionRepository.getById(session.uuid)
        assertEquals(session.uuid, t?.uuid)
    }

    @Test
    fun updateResource(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val resource: Resource = Resource(UUID.randomUUID(), user.uuid, Instant.now(), 123456L, "hash", "filepath", emptyMap<String, String>())
        resourceRepository.insert(resource)

        val new = "newHash"
        val t = resource.copy(hash = new)
        resourceRepository.update(t)

        val result = resourceRepository.getById(resource.uuid)
        assertEquals(new, result?.hash)
    }

    @Test
    fun deleteResource(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val resource: Resource = Resource(UUID.randomUUID(), user.uuid, Instant.now(), 123456L, "hash", "filepath", emptyMap<String, String>())
        resourceRepository.insert(resource)

        resourceRepository.delete(resource)

        val result = resourceRepository.getById(resource.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadNote(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val note: Note = Note(UUID.randomUUID(), "testTitle", "testToken", user.uuid, Instant.now())
        noteRepository.insert(note)

        val t = noteRepository.getById(note.uuid)
        assertEquals(note.uuid, t?.uuid)
    }

    @Test
    fun updateNote(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val note: Note = Note(UUID.randomUUID(), "testTitle", "testToken", user.uuid, Instant.now())
        noteRepository.insert(note)

        val new = "newTitle"
        val t = note.copy(title = new)
        noteRepository.update(t)

        val result = noteRepository.getById(note.uuid)
        assertEquals(new, result?.title)
    }

    @Test
    fun deleteNote(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val note: Note = Note(UUID.randomUUID(), "testTitle", "testToken", user.uuid, Instant.now())
        noteRepository.insert(note)

        noteRepository.delete(note)

        val result = noteRepository.getById(note.uuid)
        assertNull(result)
    }

    @Test
    fun createAndReadDevice(): Unit = runBlocking {
        val user: User = User(UUID.randomUUID(), "testLogin", "testPass", "testNick")
        userRepository.insert(user)

        val note: Note = Note(UUID.randomUUID(), "testTitle", "testToken", user.uuid, Instant.now())
        noteRepository.insert(note)

        val t = noteRepository.getById(note.uuid)
        assertEquals(note.uuid, t?.uuid)
    }

    @Test
    fun updateDevice(): Unit = runBlocking {
        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        val new = "newName"
        val t = device.copy(name = new)
        deviceRepository.update(t)

        val result = deviceRepository.getById(device.uuid)
        assertEquals(new, result?.name)
    }

    @Test
    fun deleteDevice(): Unit = runBlocking {
        val device: Device = Device(UUID.randomUUID(), "testName", Instant.now())
        deviceRepository.insert(device)

        deviceRepository.delete(device)

        val result = deviceRepository.getById(device.uuid)
        assertNull(result)
    }
}