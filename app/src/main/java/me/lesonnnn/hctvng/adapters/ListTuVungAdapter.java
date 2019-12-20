package me.lesonnnn.hctvng.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.models.TuVung;

public class ListTuVungAdapter extends RecyclerView.Adapter<ListTuVungAdapter.ViewHolder> {
    private List<TuVung> mData;

    public ListTuVungAdapter(List<TuVung> data) {
        mData = data;
    }

    public void updateData(List<TuVung> newData) {
        mData.clear();
        mData.addAll(newData);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_tuvung, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TuVung data = mData.get(position);
        holder.mTuVung.setText(data.getTu());
        holder.mNoiDung.setText(data.getNghia());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTuVung, mNoiDung;

        ViewHolder(View view) {
            super(view);
            mTuVung = view.findViewById(R.id.tvTuVung);
            mNoiDung = view.findViewById(R.id.tvNoiDung);
        }
    }
}
