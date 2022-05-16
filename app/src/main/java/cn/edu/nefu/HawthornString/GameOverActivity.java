package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity {

    EditText et_nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        et_nickname = findViewById(R.id.et_nickname);

        findViewById(R.id.btn_submitScore).setOnClickListener( v -> {
            String nickname = et_nickname.getText().toString();
            if(nickname.isEmpty()) {
                Toast.makeText(this, "请输入昵称后提交！", Toast.LENGTH_SHORT).show();
                return;
            }
            SQLiteHelper sqLiteHelper = new SQLiteHelper(GameOverActivity.this);
            int score = getIntent().getIntExtra("score", 0);
            int rank = sqLiteHelper.queryrank(score);
            sqLiteHelper.insertData(nickname, score, rank);
            Intent intent = new Intent(GameOverActivity.this, RankListActivity.class);
            startActivity(intent);
            finish();
        });
    }
}