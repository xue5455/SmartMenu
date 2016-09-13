package com.jake.smartmenu;

import android.view.View;

/**
 * Created by XUE on 2016/9/13.
 */
public interface ItemEventListener {
    public void onEventNotify(View view, int position,Object... data);
}
