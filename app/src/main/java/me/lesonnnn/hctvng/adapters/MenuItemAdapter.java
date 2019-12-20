package me.lesonnnn.hctvng.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.activities.MainActivity;
import me.lesonnnn.hctvng.models.MenuItem;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItem> mData;
    private IOnMenuItemClicklistener mOnClickListener;
    private MainActivity.MENU_ITEM mCurrentMenu;

    public MenuItemAdapter(List<MenuItem> data) {
        this.mData = data;
    }

    public void setItemListener(IOnMenuItemClicklistener listener) {
        mOnClickListener = listener;
    }

    public void setItemSelected(MainActivity.MENU_ITEM itemId) {
        this.mCurrentMenu = itemId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MenuItem menu = mData.get(position);
        holder.tvName.setText(menu.getName());
        holder.imvImage.setImageResource(menu.getResId());
        if (mCurrentMenu != null && mCurrentMenu == menu.getId()) {
            holder.menuGroup.setSelected(true);
        } else {
            holder.menuGroup.setSelected(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentMenu = mData.get(position).getId();
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(mData.get(position).getId());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imvImage;
        private View menuGroup;

        ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_item);
            tvName.setSelected(true);
            imvImage = view.findViewById(R.id.imv_item);
            menuGroup = view.findViewById(R.id.menu_group);
        }
    }

    public interface IOnMenuItemClicklistener {
        void onItemClick(MainActivity.MENU_ITEM menuId);
    }
}
