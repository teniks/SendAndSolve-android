package su.sendandsolve.core.items

data class ButtonItem(
    val id: String,
    val text: String,
    val action: () -> Unit
)
