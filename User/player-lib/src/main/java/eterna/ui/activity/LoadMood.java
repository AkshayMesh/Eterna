package eterna.ui.activity;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import eterna.R;
import eterna.data.Constants;
import eterna.data.DataPreference;
import eterna.data.schema.UserTable;
import eterna.ui.adpter.SectionsPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class LoadMood extends AppCompatActivity {

    public static String picDesc;
    private TextView title, subtitle;
    public static MediaPlayer mp = null;
    private static boolean showD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.sub_title);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(0);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        ImageView button = findViewById(R.id.back);
        button.setOnClickListener(v -> onBackPressed());

        title.setText(String.format("Current Mood : %s", DemoMain.Emotion));
        subtitle.setText(String.format("%s", picDesc));

        if (!showD)
        afterTime();
    }

    private void afterTime() {
        Timer timer = new Timer();
        TimerTask ttsk = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    Dialog dialog = new Dialog(LoadMood.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_rating);
                    try {
                        dialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    TextView submit = dialog.findViewById(R.id.submit_area);
                    RatingBar ratingBar = dialog.findViewById(R.id.rating);

                    submit.setOnClickListener((l)->{
                        UserTable.getReference().child(DataPreference.getUserLog(
                                LoadMood.this).id).child("rating")
                                .setValue(ratingBar.getRating());
                        Toast.makeText(LoadMood.this, "Thanks for rating", Toast.LENGTH_SHORT).show();
                        showD = true;
                        dialog.dismiss();
                    });

//                    Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/meakshay-716cf.appspot.com/o/first_class.mp3?alt=media&token=7be0d0d8-7be3-4724-900f-0f66af6661ab");
//                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                    intent.setDataAndType(uri,"audio/*");
//                    startActivity(intent);
                });
            }
        };
        if (DataPreference.getLocaleFlag(this, Constants.StringConstants.RATE_AVAILABILITY))
        timer.schedule(ttsk,13000);
    }

}