package com.youtubevideos;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.youtubevideos.api.APIService;
import com.youtubevideos.api.Constants;
import com.youtubevideos.api.model.Item;
import com.youtubevideos.api.model.Snippet;
import com.youtubevideos.api.model.VideoResponse;
import com.youtubevideos.api.model.YoutubeResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YouTubeListAdapter extends RecyclerView.Adapter<YouTubeListAdapter.MyViewHolder>{

    private Context context;
    private List<Item> youtubeData;
    private int lastPosition = -1;
    private List<Item> items = new ArrayList<>();

    public YouTubeListAdapter(Context context, List<Item> youtubeData) {
        this.context = context;
        this.youtubeData = youtubeData;
        this.setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Snippet snippet = youtubeData.get(position).getSnippet();
        final String url = snippet.getThumbnails().getDefault().getUrl();
        Glide.with(context).load(url).into(holder.logo);
//        Glide.with(context).load(url).into(holder.thumbnail);

        holder.title.setText(snippet.getTitle());


        Log.e("videoid",youtubeData.get(position).getId().getVideoId());

        Call<VideoResponse> videoResponseCall = APIService.youtubeApi.videoDetails("statistics", youtubeData.get(position).getId().getVideoId(), Constants.YOUTUBE_API_KEY);
        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, final Response<VideoResponse> response) {
                if (response.isSuccessful()){
                    Log.e("response", String.valueOf(response.body().getItems().get(0).getStatistics().getLikeCount()));
                    holder.tvLike.setText(response.body().getItems().get(0).getStatistics().getLikeCount());

                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ivLike.isSelected()){
                    holder.ivLike.setSelected(false);
                    int likes = Integer.parseInt(holder.tvLike.getText().toString().trim());
                    holder.tvLike.setText(likes-1);
                }
                else {
                    holder.ivLike.setSelected(true);
                    int likes = Integer.parseInt(holder.tvLike.getText().toString().trim());
                    holder.tvLike.setText(likes+1);
                }
            }
        });

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivComment.setVisibility(View.GONE);
                holder.etComment.setVisibility(View.VISIBLE);
                holder.ivCross.setVisibility(View.VISIBLE);
            }
        });

        holder.etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (holder.etComment.getText().toString().trim().length() == 0){
                    holder.etComment.setError("Enter comment");
                }
                else {
                    holder.etComment.setVisibility(View.GONE);
                    holder.ivCross.setVisibility(View.GONE);
                    holder.ivComment.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        holder.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.etComment.setVisibility(View.GONE);
                holder.ivCross.setVisibility(View.GONE);
                holder.ivComment.setVisibility(View.VISIBLE);
            }
        });

    }

    public void addAll(Collection<Item> items) {
        int currentItemCount = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(currentItemCount, items.size());
    }

    public void replaceWith(List<Item> items) {
        if (items != null) {
            int oldCount = this.items.size();
            int newCount = items.size();
            int delCount = oldCount - newCount;
            this.items.clear();
            this.items.addAll(items);
            if (delCount > 0) {
                notifyItemRangeChanged(0, newCount);
                notifyItemRangeRemoved(newCount, delCount);
            } else if (delCount < 0) {
                notifyItemRangeChanged(0, oldCount);
                notifyItemRangeInserted(oldCount, -delCount);
            } else {
                notifyItemRangeChanged(0, newCount);
            }
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView logo, ivLike, ivComment, ivCross;
        TextView title, tvLike;
        EditText etComment;


        public MyViewHolder(View itemView) {
            super(itemView);

            logo        = itemView.findViewById(R.id.logo);
            ivLike      = itemView.findViewById(R.id.ivLike);
            ivComment   = itemView.findViewById(R.id.ivComment);
            ivCross     = itemView.findViewById(R.id.ivCross);
            title       = itemView.findViewById(R.id.title);
            tvLike      = itemView.findViewById(R.id.tvLike);
            etComment   = itemView.findViewById(R.id.etComment);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return youtubeData.size();
    }


    public void updateList(List<Item> list) {
        youtubeData = list;
        notifyDataSetChanged();
    }

}
