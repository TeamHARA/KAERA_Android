package com.hara.kaera.feature.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import timber.log.Timber


class PermissionRequestDelegator(
    private val activity: AppCompatActivity
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Timber.e(permissions.toString())
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key } // 거절되어서 value가 false인것들

        if(deniedPermissionList.isNotEmpty()){
            // 영구 거절인 상태 알림 권한 설정창 이동
            Timber.e("forever")
            activity.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS))
        } else {
            // 최초 실행
            Timber.e("first")
            requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }
    }

    fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(activity.baseContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            // 권한 부여
        } else {
            if (shouldShowRequestPermissionRationale(activity,Manifest.permission.POST_NOTIFICATIONS)){
                // 첫번째 거절이후라서 rationale dialog를 보여주고 권한요청을 해야하는 경우
                Timber.e("rationale")
                requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            } else {
                // 최초 요청 혹은 영구 거절
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                }
            }
        }
    }


}