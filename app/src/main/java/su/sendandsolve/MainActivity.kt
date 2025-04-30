package su.sendandsolve

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import su.sendandsolve.core.items.button.NavigationBarFragment
import su.sendandsolve.features.tasks.group.ui.GroupListFragment

@AndroidEntryPoint
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

        val groupFragment = GroupListFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.group_fragment_container, groupFragment)
            addToBackStack(null)
        }

        val navigationBarFragment: NavigationBarFragment = NavigationBarFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.navigation_bar_fragment_container, navigationBarFragment)
            addToBackStack(null)
        }
    }
}