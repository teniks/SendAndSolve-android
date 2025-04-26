package su.sendandsolve.core.items.button

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID

class NavigationBarViewModel : ViewModel() {
    private val _buttons: MutableLiveData<List<ButtonItem>> = MutableLiveData(emptyList())
    val buttons: LiveData<List<ButtonItem>> = _buttons

    init {
        fetchTestData()
    }

    private fun fetchTestData() {
        _buttons.value = createTestButtons()
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