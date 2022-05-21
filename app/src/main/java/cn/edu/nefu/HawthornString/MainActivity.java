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

        Instance = this;
        uiManager = UIManager.GetInstance();
        
    }

    @Override
    protected void onResume() {
        uiManager.matchResolution();
        gameManager = GameManager.getSingleton();
        super.onResume();
    }

    public static class GameOverBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received");
            int score = intent.getIntExtra("score", 0);
            Intent it = new Intent();
            it.setClass(MainActivity.Instance, GameOverActivity.class);
            it.putExtra("score", score);
            it.setPackage(MainActivity.Instance.getPackageName());
            MainActivity.Instance.startActivity(it);
            MainActivity.Instance.finish();
        }
    }
}
