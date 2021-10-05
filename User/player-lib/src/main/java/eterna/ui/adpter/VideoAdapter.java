package eterna.ui.adpter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;

import eterna.R;
import eterna.data.schema.VideosTable;

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
        View child = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video,parent,false);
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

    public class SongHolder extends RecyclerView.ViewHolder {

        final TextView titleTv, descTv;
        final VideoView videoView;
//        private SimpleExoPlayer player;
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

//                if (player == null) {
//                    DefaultTrackSelector trackSelector = new DefaultTrackSelector(mContext);
//                    trackSelector.setParameters(
//                            trackSelector.buildUponParameters().setMaxVideoSizeSd());
//                    player = new SimpleExoPlayer.Builder(mContext)
//                            .setTrackSelector(trackSelector)
//                            .build();
//                }
//                videoView.setPlayer(player);
//                MediaItem mediaItem = new MediaItem.Builder()
//                        .setUri(mContext.getString(R.string.media_url_dash))
//                        .setMimeType(MimeTypes.APPLICATION_MPD)
//                        .build();
//                player.setMediaItem(mediaItem);
//                player.setPlayWhenReady(true);
//                player.prepare();

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
