package com.eterna.admin.ui.videos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eterna.admin.R;
import com.eterna.admin.data.helper.database.DatabaseUtil;
import com.eterna.admin.data.helper.models.VideosTable;
import com.eterna.admin.data.index.Constants;

import java.util.ArrayList;

public class AllVideoActivity extends AppCompatActivity {

    private RecyclerView videoList;
    private LinearLayout noContent;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<VideosTable> allVideos;
    private Spinner selectionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_video);
        initViews();
        loadVideos();
    }

    private void initViews() {
        ImageView backIv = findViewById(R.id.back_iv);
        noContent = findViewById(R.id.no_content);
        refreshLayout = findViewById(R.id.refresh);
        selectionView = findViewById(R.id.selection);
        allVideos = new ArrayList<>();
        backIv.setOnClickListener(v -> onBackPressed());
        videoList = findViewById(R.id.video_view);
        videoList.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        refreshLayout.setOnRefreshListener(this::loadVideos);

        selectionView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadCatCodes(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadCatCodes(int pos) {
        AsyncTask.execute(()->{
            allVideos.clear();
            switch (pos) {
                case 0 :
                default:
                    allVideos.addAll(DatabaseUtil.on().getCodeDao().getScrollableCodes());
                    break;
                case 1 :
                    allVideos.addAll(DatabaseUtil.on().getCodeDao().getCodeByCategory(
                            Constants.StringConstants.SMILE.trim()));
                    break;
                case 2:
                    allVideos.addAll(DatabaseUtil.on().getCodeDao().getCodeByCategory(
                            Constants.StringConstants.SAD.trim()));
                    break;
                case 3 :
                    allVideos.addAll(DatabaseUtil.on().getCodeDao().getCodeByCategory(
                            Constants.StringConstants.HAPPY.trim()));
                    break;
                case 4:
                    allVideos.addAll(DatabaseUtil.on().getCodeDao().getCodeByCategory(
                            Constants.StringConstants.NORMAL.trim()));
                    break;
            }
            if (allVideos.size()>1){
                runOnUiThread(()->{
                    videoList.setVisibility(View.VISIBLE);
                    noContent.setVisibility(View.GONE);
                    VideoAdapter va = new VideoAdapter(allVideos, AllVideoActivity.this);
                    videoList.setAdapter(va);
                    refreshLayout.setRefreshing(false);
                });
            }else {
                runOnUiThread(this::showNoContent);
            }
        });
    }

    private void showNoContent() {
        videoList.setVisibility(View.GONE);
        noContent.setVisibility(View.VISIBLE);
    }

    private void loadVideos() {
        selectionView.setSelection(0, true);
        AsyncTask.execute(()->{
            allVideos.clear();
            allVideos.addAll(DatabaseUtil.on().getCodeDao().getScrollableCodes());
            if (allVideos.size()>1){
                runOnUiThread(()->{
                    videoList.setVisibility(View.VISIBLE);
                    noContent.setVisibility(View.GONE);
                    VideoAdapter va = new VideoAdapter(allVideos, AllVideoActivity.this);
                    videoList.setAdapter(va);
                    refreshLayout.setRefreshing(false);
                });
            }else {
                runOnUiThread(this::showNoContent);
            }
        });
    }
}
