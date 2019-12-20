package me.lesonnnn.hctvng.fragments;

import android.content.Intent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.activities.DetailActivity;
import me.lesonnnn.hctvng.activities.MainActivity;
import me.lesonnnn.hctvng.adapters.ListCategoryAdapter;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.ListTv;

public class ListFragment extends BaseFragment implements MainActivity.UpdateListCategory, ListCategoryAdapter.IOnItemClicklistener {
    private static final int REQUEST_CODE = 1111;
    private ListCategoryAdapter mCategoryAdapter;
    private List<ListTv> mListTvs;

    @Override
    protected int initLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initComponents(View view) {
        RecyclerView rvCategory = view.findViewById(R.id.rvCategory);
        rvCategory.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DatabaseHandler db = new DatabaseHandler(getContext());
        mListTvs = db.getAllCategories();
        mCategoryAdapter = new ListCategoryAdapter(mListTvs);
        mCategoryAdapter.setIOnItemClicklistener(this);
        rvCategory.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void addListener() {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.setUpdateListCategory(this);
    }

    @Override
    public void onUpdate() {
        DatabaseHandler db = new DatabaseHandler(getContext());
        mListTvs = db.getAllCategories();
        mCategoryAdapter.updateData(mListTvs);
    }

    @Override
    public void onItemClick(int categoryId) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == DetailActivity.RESULT_UPDATE) {
            onUpdate();
        }
    }
}
