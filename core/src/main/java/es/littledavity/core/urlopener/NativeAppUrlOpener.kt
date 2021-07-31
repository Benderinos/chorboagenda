/*
 * Copyright 2021 dev.id
 */
package es.littledavity.core.urlopener

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.paulrybitskyi.hiltbinder.BindType
import es.littledavity.commons.ui.extensions.canUrlBeOpenedByNativeApp
import es.littledavity.commons.ui.extensions.getNativeAppPackageForUrl
import es.littledavity.core.commons.SdkInfo
import es.littledavity.core.utils.attachNewTaskFlagIfNeeded
import javax.inject.Inject

@BindType(withQualifier = true)
@UrlOpenerKey(UrlOpenerKey.Type.NATIVE_APP)
internal class NativeAppUrlOpener @Inject constructor() : UrlOpener {
    override fun openUrl(url: String, context: Context) = if (SdkInfo.IS_AT_LEAST_11) {
        openUrlInNewWay(url, context)
    } else {
        openUrlInLegacyWay(url, context)
    }

    private fun openUrlInNewWay(url: String, context: Context): Boolean {
        val intent = createIntent(url, context).apply {
            addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (error: Throwable) {
            false
        }
    }

    private fun openUrlInLegacyWay(url: String, context: Context): Boolean {
        if (!context.packageManager.canUrlBeOpenedByNativeApp(url)) return false

        val nativeAppPackage = context.packageManager.getNativeAppPackageForUrl(url)
        val intent = createIntent(url, context).apply {
            `package` = nativeAppPackage
        }

        context.startActivity(intent)

        return true
    }

    private fun createIntent(url: String, context: Context): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            attachNewTaskFlagIfNeeded(context)
        }
    }
}
