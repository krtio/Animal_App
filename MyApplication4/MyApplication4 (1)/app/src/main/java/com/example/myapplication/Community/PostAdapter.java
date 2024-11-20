package com.example.myapplication.Community;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnItemClickListener listener;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvContent.setText(post.getContent());
        holder.tvCategory.setText(post.getCategory());

        // 이미지 데이터가 있을 경우, ImageView에 이미지를 설정
        if (post.getImageData() != null && !post.getImageData().isEmpty()) {
            // 이미지 경로를 사용하여 비트맵을 디코딩
            Bitmap bitmap = BitmapFactory.decodeFile(post.getImageData());
            if (bitmap != null) {
                holder.ivPostImage.setImageBitmap(bitmap); // 이미지 설정
                holder.ivPostImage.setVisibility(View.VISIBLE); // 이미지가 있을 때만 보이게
            }
        } else {
            holder.ivPostImage.setVisibility(View.GONE); // 이미지가 없으면 ImageView 숨기기
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvCategory;
        ImageView ivPostImage;  // 이미지 표시할 ImageView 추가

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ivPostImage = itemView.findViewById(R.id.ivPostImage); // ImageView 초기화
        }
    }
}
