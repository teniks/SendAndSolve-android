package su.sendandsolve

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Аннотация @HiltAndroidApp включает DI для всего приложения
@HiltAndroidApp
class CoreApp : Application() {
}