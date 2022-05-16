package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btn_newGame).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_rankList).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RankListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_about).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }
}