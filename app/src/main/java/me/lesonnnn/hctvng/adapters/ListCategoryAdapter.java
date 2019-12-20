package me.lesonnnn.hctvng.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.models.ListTv;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {
    private List<ListTv> mData;
    private IOnItemClicklistener mIOnItemClicklistener;

    public ListCategoryAdapter(List<ListTv> data) {
        mData = data;
    }

    public void setIOnItemClicklistener(IOnItemClicklistener IOnItemClicklistener) {
        mIOnItemClicklistener = IOnItemClicklistener;
    }

    public void updateData(List<ListTv> newData) {
        mData.clear();
        mData.addAll(newData);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mTitle.setText(mData.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIOnItemClicklistener.onItemClick(mData.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        ViewHolder(@NonNull View view) {
            super(view);
            mTitle = view.findViewById(R.id.tvTitle);
            mTitle.setSelected(true);
        }
    }

    public interface IOnItemClicklistener {
        void onItemClick(int categoryId);
    }
}
