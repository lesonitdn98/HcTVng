package me.lesonnnn.hctvng.activities;

import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.adapters.MenuItemAdapter;
import me.lesonnnn.hctvng.fragments.ListFragment;
import me.lesonnnn.hctvng.fragments.NoteFragment;
import me.lesonnnn.hctvng.models.MenuItem;

public class MainActivity extends BaseActivity
        implements MenuItemAdapter.IOnMenuItemClicklistener, DrawerLayout.DrawerListener {
    public static final int RESULT_CODE = 1111;
    public static boolean CHECK_NOTI = false;
    public static int NOTI_ID = -1;

    public enum MENU_ITEM {LIST_MENU, NOTE_MENU}

    private DrawerLayout mDrawerLayout;
    private View mLayoutSlideMenu;
    private MENU_ITEM mCurrentMenu;
    private UpdateListCategory mUpdateListCategory;
    private OnClickBtnSave mOnClickBtnSave;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComponents() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mLayoutSlideMenu = findViewById(R.id.layout_left_menu);

        RecyclerView recyclerViewMenu = findViewById(R.id.recyclerview_menu);
        recyclerViewMenu.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(MENU_ITEM.LIST_MENU, R.drawable.ic_menu_list, "Danh Sách"));
        menuItems.add(new MenuItem(MENU_ITEM.NOTE_MENU, R.drawable.ic_menu_note, "Ghi Chú"));
        MenuItemAdapter menuAdapter = new MenuItemAdapter(menuItems);
        menuAdapter.setItemListener(this);
        recyclerViewMenu.setAdapter(menuAdapter);
        menuAdapter.setItemSelected(MENU_ITEM.LIST_MENU);
        mDrawerLayout.addDrawerListener(this);

        setTitle(getString(R.string.danh_s_ch));
        showNavLeft(R.drawable.ic_menu, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                hideKeyBoard();
            }
        });
        showNavRight(R.drawable.ic_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class),
                        RESULT_CODE);
            }
        });
        setNewPage(new ListFragment());
    }

    @Override
    protected void addListener() {

    }

    @Override
    public void onItemClick(MENU_ITEM menuId) {
        mCurrentMenu = menuId;
        mDrawerLayout.closeDrawer(mLayoutSlideMenu);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        if (mCurrentMenu == null) {
            return;
        }
        switch (mCurrentMenu) {
            case LIST_MENU:
                setTitle(getString(R.string.danh_s_ch));
                showNavRight(R.drawable.ic_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, AddActivity.class));
                    }
                });
                replaceFragment(new ListFragment());
                break;
            case NOTE_MENU:
                setTitle(getString(R.string.ghi_chu));
                showNavRight(R.drawable.ic_done, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnClickBtnSave.onClickSave();
                        hideKeyBoard();
                    }
                });
                replaceFragment(new NoteFragment());
                break;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            mUpdateListCategory.onUpdate();
        }
    }

    public void setOnClickBtnSave(OnClickBtnSave onClickBtnSave) {
        mOnClickBtnSave = onClickBtnSave;
    }

    public void setUpdateListCategory(UpdateListCategory updateListCategory) {
        mUpdateListCategory = updateListCategory;
    }

    public interface UpdateListCategory {
        void onUpdate();
    }

    public interface OnClickBtnSave {
        void onClickSave();
    }
}
