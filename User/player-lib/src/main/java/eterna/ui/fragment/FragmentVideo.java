package eterna.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import eterna.R;
import eterna.data.schema.VideosTable;
import eterna.ui.activity.DemoMain;
import eterna.ui.activity.LoadMood;
import eterna.ui.adpter.VideoAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentVideo extends Fragment {

    private VideoAdapter sga;
    private ArrayList<VideosTable> sgList;
    private VideosTable sngRow;
    private DatabaseReference db;

    private SwipeRefreshLayout srl;
    private ViewPager2 videoPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoadMood.mp!=null){
            LoadMood.mp.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(LoadMood.mp!=null){
            LoadMood.mp.stop();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        srl = view.findViewById(R.id.swipeRefreshLt);
        videoPager = view.findViewById(R.id.recycleSg);
        sgList = new ArrayList<>();
        db = VideosTable.geReference().child(DemoMain.Emotion.trim());
        srl.setOnRefreshListener(this::getVideos);
        getVideos();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getVideos() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sgList.clear();
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    sngRow = postsnapshot.getValue(VideosTable.class);
                    sgList.add(sngRow);
                }
                Collections.shuffle(sgList);
                if (getContext()!=null)
                sga = new VideoAdapter(sgList, getContext());
                videoPager.setAdapter(sga);
                srl.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void happyMood(){

    }

    private void sadMood(){

    }

    private void crazyMood(){

    }

    private void normalMood(){

    }
}
