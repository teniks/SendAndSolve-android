package su.sendandsolve.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import su.sendandsolve.core.database.room.dao.DeviceDao
import su.sendandsolve.core.database.room.dao.GroupDao
import su.sendandsolve.core.database.room.dao.NoteDao
import su.sendandsolve.core.database.room.dao.NoteTagDao
import su.sendandsolve.core.database.room.dao.NoteTaskDao
import su.sendandsolve.core.database.room.dao.ResourceDao
import su.sendandsolve.core.database.room.dao.SessionDao
import su.sendandsolve.core.database.room.dao.TagDao
import su.sendandsolve.core.database.room.dao.TaskAssignmentDao
import su.sendandsolve.core.database.room.dao.TaskDao
import su.sendandsolve.core.database.room.dao.TaskGroupDao
import su.sendandsolve.core.database.room.dao.TaskHierarchyDao
import su.sendandsolve.core.database.room.dao.TaskResourceDao
import su.sendandsolve.core.database.room.dao.TaskTagDao
import su.sendandsolve.core.database.room.dao.TeamDao
import su.sendandsolve.core.database.room.dao.TeamMemberDao
import su.sendandsolve.core.database.room.dao.UserDao
import su.sendandsolve.core.database.room.entity.ChangeLog
import su.sendandsolve.core.database.room.entity.Device
import su.sendandsolve.core.database.room.entity.Group
import su.sendandsolve.core.database.room.entity.Note
import su.sendandsolve.core.database.room.entity.NoteTag
import su.sendandsolve.core.database.room.entity.NoteTask
import su.sendandsolve.core.database.room.entity.Resource
import su.sendandsolve.core.database.room.entity.Session
import su.sendandsolve.core.database.room.entity.Tag
import su.sendandsolve.core.database.room.entity.Task
import su.sendandsolve.core.database.room.entity.TaskAssignment
import su.sendandsolve.core.database.room.entity.TaskGroup
import su.sendandsolve.core.database.room.entity.TaskHierarchy
import su.sendandsolve.core.database.room.entity.TaskResource
import su.sendandsolve.core.database.room.entity.TaskTag
import su.sendandsolve.core.database.room.entity.Team
import su.sendandsolve.core.database.room.entity.TeamMember
import su.sendandsolve.core.database.room.entity.User
import su.sendandsolve.core.database.room.utils.Converters

@Database(
    entities = [ChangeLog::class, Device::class, Note::class, Resource::class,
        Session::class, Tag::class, Task::class, Team::class, User::class, NoteTag::class,
        TaskTag::class, TaskHierarchy::class, TaskResource::class, TeamMember::class,
        TaskAssignment::class, NoteTask::class, Group::class, TaskGroup::class],
    version = 1,
    exportSchema = false // Отключаем экспорт схемы для упрощения
)
@TypeConverters(Converters::class)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun groupDao(): GroupDao
    abstract fun noteDao(): NoteDao
    abstract fun noteTagDao(): NoteTagDao
    abstract fun noteTaskDao(): NoteTaskDao
    abstract fun resourceDao(): ResourceDao
    abstract fun sessionDao(): SessionDao
    abstract fun tagDao(): TagDao
    abstract fun taskDao(): TaskDao
    abstract fun taskGroupDao() : TaskGroupDao
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