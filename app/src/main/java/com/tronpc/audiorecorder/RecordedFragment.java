package com.tronpc.audiorecorder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecordedFragment extends Fragment implements AudioListAdapter.onItemListClick{
    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView audioList;
    private ArrayList<AudioItem> allAudioItems;
    private AudioListAdapter audioListAdapter;

    private File[] allRecordedFiles;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private AudioItem sourceToPlay = null;

    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;

    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    private File directory;

    public RecordedFragment() {
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recorded_fragment,container,false);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadSampleMusic();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.audio_list_view);

        playBtn = view.findViewById(R.id.player_play_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_filename);

        playerSeekbar = view.findViewById(R.id.player_seekbar);

        allAudioItems = new ArrayList<>();

//        loadSampleMusic();
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    pauseAudio();
                } else {
                    if(sourceToPlay != null){
                        resumeAudio();
                    }
                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });
    }

    private void updateList(){
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        directory = new File(path);
        allRecordedFiles = directory.listFiles();
        loadRecordedAudios(allRecordedFiles);
            AudioItem[] finalAudioItems = new AudioItem[allAudioItems.size()];

            for(int i=0;i<allAudioItems.size();i++){
                finalAudioItems[i] = allAudioItems.get(i);
            }

            audioListAdapter = new AudioListAdapter(finalAudioItems, this);
            audioList.setHasFixedSize(true);
            audioList.setLayoutManager(new LinearLayoutManager(getContext()));
            audioList.setAdapter(audioListAdapter);
            audioList.setVisibility(View.VISIBLE);
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio() {
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        isPlaying = true;

        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void stopAudio() {
        //Stop The Audio
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(AudioItem file) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(file.source);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        playerFilename.setText(file.title);
        playerHeader.setText("Playing");
        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    private void loadRecordedAudios(File[] files){
        for(File f : files){
            AudioItem audioItem = new AudioItem(f.getName(),f.getAbsolutePath(),"Recording");
            allAudioItems.add(audioItem);
        }
    }

    private void loadSampleMusic(){
        allAudioItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Music> call = api.getOnlineMusic();
        call.enqueue(new Callback<Music>() {
            @Override
            public void onResponse(Call<Music> call, Response<Music> response) {

                List<OnlineFile>data = response.body().getMusic();
                for(OnlineFile f : data){
                    AudioItem audioItem = new AudioItem(f.getTitle(),f.getSite()+"/"+f.getSource(),"Sample Audio");
                    System.out.println(f.getTitle());
                    allAudioItems.add(audioItem);
                }
                updateList();
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch online library", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                updateList();
            }
        });
    }

    @Override
    public void onClickListener(AudioItem file, int position) {
        sourceToPlay = file;
        if(isPlaying){
            stopAudio();
            playAudio(sourceToPlay);
        } else {
            playAudio(sourceToPlay);
        }
    }
}