package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Instance = this;
        findViewById(R.id.btn_test).setOnClickListener(v -> SoundManager.PlayPickupSound());
    }
}
