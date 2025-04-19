package su.sendandsolve

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.core.adapters.ButtonAdapter
import su.sendandsolve.core.items.ButtonItem
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.model.Group
import su.sendandsolve.features.tasks.domain.model.Task
import su.sendandsolve.features.tasks.group.ui.GroupAdapter
import java.time.Instant
import java.util.UUID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecycleViews(findViewById(R.id.main))
        loadTestData(findViewById(R.id.main))
    }

    private fun setupRecycleViews(view: View){
        view.findViewById<RecyclerView>(R.id.group_recyclerview).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = GroupAdapter().apply {
                submitList(emptyList())
            }
        }

        view.findViewById<RecyclerView>(R.id.button_recycleview).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = (ButtonAdapter { button -> button.action()}).apply {
                submitList(emptyList())
            }
        }
    }

    private fun loadTestData(view: View) {
        (view.findViewById<RecyclerView>(R.id.group_recyclerview).adapter as GroupAdapter).submitList(createTestGroups())
        (view.findViewById<RecyclerView>(R.id.button_recycleview).adapter as ButtonAdapter).submitList(createTestButtons())
    }

    private fun createTestGroups() = listOf(
        Group(
            uuid = UUID.randomUUID(),
            name = "Test group 1",
            tasks = (1..5).map {
                DomainState.Read
            }.associateBy {
                Task(
                    uuid = UUID.randomUUID(),
                    title = "Test task",
                    description = "Test task description",
                    status = "Test status",
                    priority = 0,
                    startDate = Instant.now(),
                    progress = 0,
                    creatorId = UUID.randomUUID(),
                    scope = "Test scope $it",
                    creationDate = Instant.now())  } as MutableMap<Task, DomainState>,
            creatorId = UUID.randomUUID(),
            criteria = mapOf("test" to "test")
        ),
        Group(
            uuid = UUID.randomUUID(),
            name = "Test group 2",
            tasks = (1..5).map {
                DomainState.Read
            }.associateBy {
                Task(
                    uuid = UUID.randomUUID(),
                    title = "Test task",
                    description = "Test task description",
                    status = "Test status",
                    priority = 0,
                    startDate = Instant.now(),
                    progress = 0,
                    creatorId = UUID.randomUUID(),
                    scope = "Test scope $it",
                    creationDate = Instant.now())  } as MutableMap<Task, DomainState>,
            creatorId = UUID.randomUUID(),
            criteria = mapOf("test" to "test")
        )
    )

    private fun createTestButtons() = listOf(
        ButtonItem(
            id = UUID.randomUUID().toString(),
            text = "Пред.",
            action = {  }
        ),
        ButtonItem(
            id = UUID.randomUUID().toString(),
            text = "След.",
            action = {  }
        ),
        ButtonItem(
            id = UUID.randomUUID().toString(),
            text = "Фильтр",
            action = {  }
        )
    )
}