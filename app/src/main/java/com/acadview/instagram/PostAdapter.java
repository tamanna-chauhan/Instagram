package com.acadview.instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostModel> postList = null;

    public PostAdapter(Context context){this.context=context;}


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        PostModel model = postList.get(position);

        holder.userName.setText(model.username);
        Glide.with(context).load(model.getPostImgURL()).into(holder.postImage);

    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    public void swap(ArrayList<PostModel> postList){
        this.postList = postList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView postImage;
        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.text);
            postImage = itemView.findViewById(R.id.postImg);

        }
    }
}
