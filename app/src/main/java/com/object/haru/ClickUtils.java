package com.object.haru;

import android.os.SystemClock;
import android.view.View;

public class ClickUtils {

    private static long lastClickTime = 0;

    // 중복 클릭인지 아닌지를 판단하는 상수
    private static final long MIN_CLICK_INTERVAL = 3000; // 중복 클릭 방지를 위한 시간 간격 (1초)

    // 클릭 이벤트를 처리하는 메서드
    public static void handleClick(View view, View.OnClickListener clickListener) {
        long currentClickTime = SystemClock.elapsedRealtime();
        if (currentClickTime - lastClickTime >= MIN_CLICK_INTERVAL) {
            lastClickTime = currentClickTime;
            clickListener.onClick(view);
        }
    }
}
