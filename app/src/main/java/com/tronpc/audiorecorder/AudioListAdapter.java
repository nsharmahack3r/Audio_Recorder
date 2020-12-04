package com.tronpc.audiorecorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private AudioItem[] audioItems;
    private onItemListClick onItemListClick;

    public AudioListAdapter(AudioItem[] audioItems, onItemListClick onItemListClick){
       this.audioItems = audioItems;
       this.onItemListClick = onItemListClick;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_list_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.list_title.setText(audioItems[position].title);
        holder.list_type.setText(audioItems[position].type);
    }

    @Override
    public int getItemCount() {
        return audioItems.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title;
        private TextView list_type;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_image = itemView.findViewById(R.id.list_image_view);
            list_title = itemView.findViewById(R.id.list_title);
            list_type = itemView.findViewById(R.id.list_type);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListClick.onClickListener(audioItems[getAdapterPosition()], getAdapterPosition());

        }
    }

    public interface onItemListClick {
        void onClickListener(AudioItem file, int position);
    }
}
