package com.phone.app.core.privacy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionChecker {
    data class Status(val name: String, val granted: Boolean, val required: Boolean)

    fun checkAll(context: Context): List<Status> {
        val perms = mutableListOf(
            Manifest.permission.CALL_PHONE to true,
            Manifest.permission.READ_CONTACTS to false,
            Manifest.permission.READ_CALL_LOG to false,
            Manifest.permission.POST_NOTIFICATIONS to false
        )
        return perms.map { (p, req) ->
            Status(p, ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED, req)
        }
    }

    fun isGranted(context: Context, perm: String): Boolean =
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
}
