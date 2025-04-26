package su.sendandsolve.features.tasks.group.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Group

class GroupListViewModelFactory(
    private val repository: Repository<Group>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupListViewModel(repository) as T
    }
}