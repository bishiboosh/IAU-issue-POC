package eu.sweetlygeek.iau.poc

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.ktx.requestUpdateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InAppUpdateTest {

    private lateinit var appUpdateManager: FakeAppUpdateManager

    @Before
    fun setup() {
        appUpdateManager = FakeAppUpdateManager(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testFlow() {
        runTest {
            appUpdateManager.setUpdateNotAvailable()
            assertEquals(
                "Should not be available",
                appUpdateManager.requestAppUpdateInfo().updateAvailability(),
                UpdateAvailability.UPDATE_NOT_AVAILABLE
            )
            appUpdateManager
                .requestUpdateFlow()
                .test {
                    appUpdateManager.setUpdateAvailable(42)
                    assertTrue(awaitItem() is AppUpdateResult.Available)
                }
        }
    }

    @Test
    fun testWithoutFlow() {
        runTest {
            appUpdateManager.setUpdateNotAvailable()
            assertEquals(
                "Should not be available",
                appUpdateManager.requestAppUpdateInfo().updateAvailability(),
                UpdateAvailability.UPDATE_NOT_AVAILABLE
            )
            appUpdateManager.setUpdateAvailable(42)
            assertEquals(
                "Should be available",
                appUpdateManager.requestAppUpdateInfo().updateAvailability(),
                UpdateAvailability.UPDATE_AVAILABLE
            )
        }
    }
}