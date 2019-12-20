package me.lesonnnn.hctvng.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.lesonnnn.hctvng.R;

public abstract class BaseFragment extends Fragment {
    private View mView;
    private int mViewId;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    protected abstract int initLayout();

    protected abstract void initComponents(View view);

    protected abstract void addListener();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) parent.removeView(mView);
        }
        int layoutId = initLayout();
        if (layoutId != 0) {
            mViewId = layoutId;
        }
        try {
            mView = LayoutInflater.from(getActivity()).inflate(mViewId, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.txt_waiting));
        initComponents(view);
        addListener();
    }

    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void toast(int messageId) {
        Toast.makeText(mContext, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
