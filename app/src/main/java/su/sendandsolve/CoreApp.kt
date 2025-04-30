package su.sendandsolve

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Аннотация @HiltAndroidApp включает DI для всего приложения
// Необходим для использования контекста приложения
@HiltAndroidApp
class CoreApp : Application()