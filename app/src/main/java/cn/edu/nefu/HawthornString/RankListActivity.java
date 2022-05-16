package cn.edu.nefu.HawthornString;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RankListActivity extends AppCompatActivity {

    ListView lv_rankList;
    String[] from = {"_rank", "_name", "_score"};
    int[] to = {R.id.tv_rank, R.id.tv_name, R.id.tv_score};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);

        lv_rankList = findViewById(R.id.lv_rankList);
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.rank_item, sqLiteHelper.getListViewCursorByModel(),
                from, to, 0);
        lv_rankList.setAdapter(cursorAdapter);
    }
}