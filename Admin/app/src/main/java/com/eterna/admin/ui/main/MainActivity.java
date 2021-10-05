package com.eterna.admin.ui.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.eterna.admin.R;
import com.eterna.admin.data.helper.database.DatabaseUtil;
import com.eterna.admin.data.helper.models.QuotesTable;
import com.eterna.admin.data.helper.models.UserTable;
import com.eterna.admin.data.helper.models.VideosTable;
import com.eterna.admin.data.index.Constants;
import com.eterna.admin.ui.quote.AllQuoteActivity;
import com.eterna.admin.ui.rating.RatingActivity;
import com.eterna.admin.ui.user.UsersActivity;
import com.eterna.admin.ui.videos.AllVideoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import static com.eterna.admin.data.index.Constants.StringConstants.HAPPY;
import static com.eterna.admin.data.index.Constants.StringConstants.NORMAL;
import static com.eterna.admin.data.index.Constants.StringConstants.SAD;
import static com.eterna.admin.data.index.Constants.StringConstants.SMILE;

public class MainActivity extends AppCompatActivity {

    private CardView allVideoCard, allUserCard, allQuoteCard, ratingCard;
    private TextView allVideoTv, allUserTv, allQuoteTv, ratingTv;
    private long totalQuotes, totalVideos, smileV, sadV, normalV, happyV
            , smileQ, sadQ, happyQ, normalQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        loadDashBoard();
    }

    private void initViews() {
        allUserCard = findViewById(R.id.user_card);
        allVideoCard = findViewById(R.id.video_card);
        allQuoteCard = findViewById(R.id.quote_card);
        ratingCard = findViewById(R.id.rating_card);
        allVideoTv = findViewById(R.id.video_count_tv);
        allUserTv = findViewById(R.id.user_count_tv);
        allQuoteTv = findViewById(R.id.quote_count_tv);
        ratingTv = findViewById(R.id.rating_count_tv);
    }

    private void setListeners() {
        allUserCard.setOnClickListener(v -> startActivity(new Intent(
                MainActivity.this, UsersActivity.class)));
        allVideoCard.setOnClickListener(v -> startActivity(new Intent(
                MainActivity.this, AllVideoActivity.class)));
        allQuoteCard.setOnClickListener(v -> startActivity(new Intent(
                MainActivity.this, AllQuoteActivity.class)));
        ratingCard.setOnClickListener(v -> startActivity(new Intent(
                MainActivity.this, RatingActivity.class)));
    }

    private void loadDashBoard() {
        if (Constants.getInstance(this).isOnline()){
            //videos
            VideosTable.geReference().child(SMILE.trim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    smileV = 0;
                    smileV = snapshot.getChildrenCount();
                    totalVideos = totalVideos + smileV;
                    allVideoTv.setText(String.valueOf(totalVideos));
                    vAddToLocal(SMILE.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VideosTable.geReference().child(HAPPY.trim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    happyV = 0;
                    happyV = snapshot.getChildrenCount();
                    totalVideos = totalVideos + happyV;
                    allVideoTv.setText(String.valueOf(totalVideos));
                    vAddToLocal(HAPPY.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VideosTable.geReference().child(NORMAL.trim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    normalV = 0;
                    normalV = snapshot.getChildrenCount();
                    totalVideos = totalVideos + normalV;
                    allVideoTv.setText(String.valueOf(totalVideos));
                    vAddToLocal(NORMAL.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VideosTable.geReference().child(SAD.trim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sadV = 0;
                    sadV = snapshot.getChildrenCount();
                    totalVideos = totalVideos + sadV;
                    allVideoTv.setText(String.valueOf(totalVideos));
                    vAddToLocal(SAD.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //quotes
            QuotesTable.geReference().child(SAD).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sadQ = 0;
                    sadQ = snapshot.getChildrenCount();
                    totalQuotes = totalQuotes + sadQ;
                    allQuoteTv.setText(String.valueOf(totalQuotes));
                    qAddToLocal(SAD.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            QuotesTable.geReference().child(SMILE).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    smileQ = 0;
                    smileQ = snapshot.getChildrenCount();
                    totalQuotes = totalQuotes + smileQ;
                    allQuoteTv.setText(String.valueOf(totalQuotes));
                    qAddToLocal(SMILE.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            QuotesTable.geReference().child(HAPPY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    happyQ = 0;
                    happyQ = snapshot.getChildrenCount();
                    totalQuotes = totalQuotes + happyQ;
                    allQuoteTv.setText(String.valueOf(totalQuotes));
                    qAddToLocal(HAPPY.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            QuotesTable.geReference().child(NORMAL).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    normalQ = 0;
                    normalQ = snapshot.getChildrenCount();
                    totalQuotes = totalQuotes + normalQ;
                    allQuoteTv.setText(String.valueOf(totalQuotes));
                    qAddToLocal(NORMAL.trim(), snapshot.getChildren());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            UserTable.getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        allUserTv.setText(String.valueOf(snapshot.getChildrenCount()));
                        assignValues(snapshot.getChildren());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void qAddToLocal(String opt, Iterable<DataSnapshot> children) {
        for (DataSnapshot postSnap : children){
            QuotesTable item = postSnap.getValue(QuotesTable.class);
            if (item!=null){
                item.qotCat = opt;
                AsyncTask.execute(()-> {
                    DatabaseUtil.on().getNamesDao().killCat(opt);
                    DatabaseUtil.on().getNamesDao().insertAll(item);
                });
            }
        }
    }

    private void vAddToLocal(String opt, Iterable<DataSnapshot> children) {
        for (DataSnapshot postSnap : children){
            VideosTable item = postSnap.getValue(VideosTable.class);
            if (item!=null){
                item.vidCat = opt;
                AsyncTask.execute(()-> {
                    DatabaseUtil.on().getCodeDao().killCat(opt);
                    DatabaseUtil.on().getCodeDao().insertAll(item);
                });
            }
        }
    }

    private void assignValues(Iterable<DataSnapshot> snaps) {
        int totalRating = 0, count = 0;
        float avgRating = 0.0f;
        for (DataSnapshot postSnap : snaps){
            UserTable uModel = postSnap.getValue(UserTable.class);
            if (uModel!=null){
                totalRating = totalRating + uModel.rating;
                if (uModel.rating!=0)
                    count++;
            }
        }
        if (count!=0)
        avgRating = (float) totalRating/count;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        ratingTv.setText(df.format(avgRating));
    }
}