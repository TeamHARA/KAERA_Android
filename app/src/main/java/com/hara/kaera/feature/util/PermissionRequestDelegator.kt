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
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key } // 거절되어서 value가 false인것들

        when {
            deniedPermissionList.isNotEmpty() -> {
                val map = deniedPermissionList.groupBy { permission->
                    if (shouldShowRequestPermissionRationale(activity, permission)) "denied" else "explained"
                }
                map["denied"]?.let {
                    // 진짜!!!!! 최초 첫번째 거절을함 (테스트 결과 앱 설치후 진짜 최초 거절 한번만 들어오게 되는 케이스)
                    Timber.e("first from installed")
                }
                map["explained"]?.let {
                    Timber.e("second or first after permission disabled")
                    // 두번째 거절을 함 or 권한 만료나 해제 이후 거절을 함

                    // 만약 알림권한을 받아온후 다시 권한이 없어져서 권한을 다시 받아와야 한다면 그때는
                    // 위의 케이스 없이 해당 케이스로 들어오게 되고 이후 영구 거절처리됨
                    // 이 시점부터 권한 요청이 완전히 막혀버림 (알림 권한 창으로 이동시켜줌)
                    activity.startActivity(Intent().setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, activity.baseContext.packageName))
                }
            }
            else -> {
                // 사용자가 허용을 눌러서 권한이 부여됨
                Unit
            }
        }
    }

    fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(activity.baseContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            // 권한 부여된 상태
            Unit
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)){
                    // 한번 거절된 상태
                    // rationale 다이얼로그 보여주고 이후 권한요청
                    // 이때 콜백 함수(런처)를 타지않게 해주어서 이후 거절된 케이스는 모두 알림 설정창으로 가도록 유도
                    // TODO rationale dialog
                    Timber.e("rationale")
                    requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                }else{
                    launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                }

            }
        }
    }


}