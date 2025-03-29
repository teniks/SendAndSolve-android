package su.sendandsolve.data.items

data class ButtonItem(
    val id: String,
    val text: String,
    val action: () -> Unit
)
