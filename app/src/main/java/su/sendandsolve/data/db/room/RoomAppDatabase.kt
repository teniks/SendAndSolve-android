package su.sendandsolve.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.TypeConverters
import su.sendandsolve.data.db.room.dao.DeviceDao
import su.sendandsolve.data.db.room.dao.NoteDao
import su.sendandsolve.data.db.room.dao.NoteTagDao
import su.sendandsolve.data.db.room.dao.NoteTaskDao
import su.sendandsolve.data.db.room.dao.ResourceDao
import su.sendandsolve.data.db.room.dao.SessionDao
import su.sendandsolve.data.db.room.dao.TagDao
import su.sendandsolve.data.db.room.dao.TaskAssignmentDao
import su.sendandsolve.data.db.room.dao.TaskDao
import su.sendandsolve.data.db.room.dao.TaskHierarchyDao
import su.sendandsolve.data.db.room.dao.TaskResourceDao
import su.sendandsolve.data.db.room.dao.TaskTagDao
import su.sendandsolve.data.db.room.dao.TeamDao
import su.sendandsolve.data.db.room.dao.TeamMemberDao
import su.sendandsolve.data.db.room.dao.UserDao
import su.sendandsolve.data.db.room.entity.Note
import su.sendandsolve.data.db.room.entity.NoteTag
import su.sendandsolve.data.db.room.entity.Resource
import su.sendandsolve.data.db.room.entity.ChangeLog
import su.sendandsolve.data.db.room.entity.Device
import su.sendandsolve.data.db.room.entity.NoteTask
import su.sendandsolve.data.db.room.entity.User
import su.sendandsolve.data.db.room.entity.Session
import su.sendandsolve.data.db.room.entity.Tag
import su.sendandsolve.data.db.room.entity.Task
import su.sendandsolve.data.db.room.entity.TaskHierarchy
import su.sendandsolve.data.db.room.entity.TaskResource
import su.sendandsolve.data.db.room.entity.TaskTag
import su.sendandsolve.data.db.room.entity.TaskAssignment
import su.sendandsolve.data.db.room.entity.Team
import su.sendandsolve.data.db.room.entity.TeamMember
import su.sendandsolve.data.db.room.util.Converters

@Database(
    entities = [ChangeLog::class, Device::class, Note::class, Resource::class,
        Session::class, Tag::class, Task::class, Team::class, User::class, NoteTag::class,
        TaskTag::class, TaskHierarchy::class, TaskResource::class, TeamMember::class,
        TaskAssignment::class, NoteTask::class],
    version = 1,
    exportSchema = false // Отключаем экспорт схемы для упрощения
)
@TypeConverters(Converters::class)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun noteDao(): NoteDao
    abstract fun noteTagDao(): NoteTagDao
    abstract fun noteTaskDao(): NoteTaskDao
    abstract fun resourceDao(): ResourceDao
    abstract fun sessionDao(): SessionDao
    abstract fun tagDao(): TagDao
    abstract fun taskDao(): TaskDao
    abstract fun taskAssignmentDao() : TaskAssignmentDao
    abstract fun taskHierarchyDao() : TaskHierarchyDao
    abstract fun taskResourceDao() : TaskResourceDao
    abstract fun taskTagDao() : TaskTagDao
    abstract fun teamDao(): TeamDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: RoomAppDatabase? = null

        fun getAppDataBase(context: Context): RoomAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomAppDatabase::class.java,
                    "core.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}