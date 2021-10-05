package com.eterna.admin.ui.videos;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eterna.admin.R;
import com.eterna.admin.data.helper.models.VideosTable;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.SongHolder> {

    private final ArrayList<VideosTable> vidList;
    private Context mContext;

    public VideoAdapter(ArrayList<VideosTable> vidList, Context mContext) {
        this.vidList = vidList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new SongHolder(child);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.setVideoData(vidList.get(position));
    }

    @Override
    public int getItemCount() {
        return vidList.size();
    }

    public static class SongHolder extends RecyclerView.ViewHolder {

        final TextView titleTv, descTv;
        final VideoView videoView;
        final ProgressBar progress;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            titleTv = itemView.findViewById(R.id.title_tv);
            descTv = itemView.findViewById(R.id.description_tv);
            progress = itemView.findViewById(R.id.progress);
        }

        public void setVideoData(VideosTable sTable){
            titleTv.setText(sTable.title);
            descTv.setText(sTable.description);
            try{
                videoView.setVideoPath(sTable.link);
                videoView.setOnPreparedListener(mp -> {
                    progress.setVisibility(View.GONE);
                    mp.start();
                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
                    float scale = videoRatio / screenRatio;
                    if (scale>=1f){
                        videoView.setScaleX(scale);
                    } else {
                        videoView.setScaleY(1f/scale);
                    }
                });
                videoView.setOnCompletionListener(MediaPlayer::start);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
