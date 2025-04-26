package su.sendandsolve

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.core.adapters.ButtonAdapter
import su.sendandsolve.core.items.ButtonItem
import su.sendandsolve.features.tasks.group.ui.GroupListFragment
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

        val fragment = GroupListFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }

        setupRecycleViews(findViewById(R.id.main))
        loadTestData(findViewById(R.id.main))
    }

    private fun setupRecycleViews(view: View){
        view.findViewById<RecyclerView>(R.id.button_recycleview).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = (ButtonAdapter { button -> button.action()}).apply {
                submitList(emptyList())
            }
        }
    }

    private fun loadTestData(view: View) {
        (view.findViewById<RecyclerView>(R.id.button_recycleview).adapter as ButtonAdapter).submitList(createTestButtons())
    }

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