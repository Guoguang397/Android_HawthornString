package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

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
}
