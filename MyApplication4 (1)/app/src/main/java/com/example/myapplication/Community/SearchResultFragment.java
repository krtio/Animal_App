package com.example.myapplication.Community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class SearchResultFragment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_result);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResults = (List<Post>) getIntent().getSerializableExtra("searchResults");
        postAdapter = new PostAdapter(searchResults);
        recyclerView.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener(post -> {
            Intent intent = new Intent(SearchResultFragment.this, PreviewPostActivity.class);
            intent.putExtra("title", post.getTitle());
            intent.putExtra("content", post.getContent());
            intent.putExtra("category", post.getCategory());
            intent.putExtra("created_at", post.getCreatedAt());
            startActivity(intent);
        });
    }
}
