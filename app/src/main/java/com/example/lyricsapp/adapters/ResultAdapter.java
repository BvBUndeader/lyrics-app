package com.example.lyricsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyricsapp.R;
import com.example.lyricsapp.entities.SongResult;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private List<SongResult> results;
    private final ClickListener clickListener;

    public ResultAdapter(List<SongResult> results, ClickListener clickListener) {
        this.results = results;
        this.clickListener = clickListener;
    }

    public List<SongResult> getResults() {
        return results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        SongResult result = results.get(position);
        holder.resultTitleTV.setText(result.getTitle());
        holder.resultAlbumTV.setText(result.getAlbum());
        holder.resultArtistTV.setText(result.getArtist());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(List<SongResult> newResults){
        this.results.clear();
        this.results.addAll(newResults);
        notifyDataSetChanged();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView resultTitleTV;
        TextView resultAlbumTV;
        TextView resultArtistTV;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultTitleTV = itemView.findViewById(R.id.resultTitleTV);
            resultAlbumTV = itemView.findViewById(R.id.resultAlbumTV);
            resultArtistTV = itemView.findViewById(R.id.resultArtistTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position >= 0){
                SongResult clickedItem = results.get(position);
                clickListener.onItemClick(clickedItem, v);
            }
        }
    }

    public interface ClickListener{
        void onItemClick(SongResult item, View v);
    }
}
