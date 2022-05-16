package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static MainActivity Instance;
    UIManager uiManager;
    GameManager gameManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        Instance = this;
        uiManager = UIManager.GetInstance();
//        findViewById(R.id.btn_test).setOnClickListener(v -> SoundManager.PlayPickupSound());
        
    }

    @Override
    protected void onResume() {
//        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        uiManager.matchResolution();
        gameManager = GameManager.getSingleton();
        super.onResume();
    }

    public static class GameOverBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int score = intent.getIntExtra("score", 0);
            Intent it = new Intent();
            it.setClass(MainActivity.Instance, GameOverActivity.class);
            it.putExtra("score", score);
            MainActivity.Instance.startActivity(it);
            MainActivity.Instance.finish();
        }
    }
}
