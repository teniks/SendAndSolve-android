package su.sendandsolve.core.items.button

data class ButtonItem(
    val id: String,
    val text: String,
    val action: () -> Unit
)
