package com.jake.smartmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jake.smart.SmartMenu;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmartMenu smartMenu = (SmartMenu) findViewById(R.id.smart_menu);
        MenuAdapter adapter = new MenuAdapter();
        adapter.setListener(new ItemEventListener() {
            @Override
            public void onEventNotify(View view, int position, Object... data) {
                switch (position) {
                    case 0:
                        toast("ALBUM");
                        break;
                    case 1:
                        toast("COMMENT");
                        break;
                    case 2:
                        toast("DRAFT");
                        break;
                    case 3:
                        toast("LIKE");
                        break;
                }
            }
        });
        smartMenu.setAdapter(adapter);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
