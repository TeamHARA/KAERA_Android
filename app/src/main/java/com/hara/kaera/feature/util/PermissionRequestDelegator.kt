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
    private val activity: AppCompatActivity,
) {

    /*
    현재 생각 플로우
    안드로이드 12.0 에서는 알림권한이 요청창 필요없이 무조건 활성화 그래서 마이페이지 토글버튼에서 on off 때 알림창으로 이동시켜주는 로직만 주는걸로

    하지만 13.0 이후에서는 다음과 같이 권한요청을 따로 받아와야함 따라서 권한요청 플로우대로 첫번째,두번째 거절 케이스 별로 따로 대응을 해준다.
    다만, 최초 앱시작단계에서는 한번만 물어보고 이후에는(두번째 거절) 마이페이지에서만 권한요청 활성화를 물어보는 형식으로 권한요청이 이루어지도록 할 예정

    그래서 일단 해당 런처는 권한요청이 들어오면 무조건 최초/첫번째/두번째 케이스를 모두 검사하지만 이후 실적용에서는
    스플래시 or 홈 액티비티에서 한번 최초검사만 하는 런처 (shouldShowRequestPermissionRationale == true 일 경우 검사 안함)
    마이페이지에서 두번째 거절 / 영구 거절 처리하는 런처를  코드나 함수로 일부 분리해야 할듯
     */

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

    fun checkPermissions(): Boolean?{
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
                    return true
                }else{
                    launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                }

            }
        }
        return null
    }


}