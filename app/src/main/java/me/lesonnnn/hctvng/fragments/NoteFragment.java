package me.lesonnnn.hctvng.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.Objects;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.activities.MainActivity;

public class NoteFragment extends BaseFragment implements MainActivity.OnClickBtnSave {
    private EditText myEditText;
    private SharedPreferences.Editor mEditor;

    @Override
    protected int initLayout() {
        return R.layout.fragment_note;
    }

    @SuppressLint({ "ClickableViewAccessibility", "CommitPrefEdits" })
    @Override
    protected void initComponents(View view) {
        myEditText = view.findViewById(R.id.edtContent);
        ScrollView scroll = view.findViewById(R.id.scrollView);
        MainActivity mainActivity = (MainActivity) getContext();
        assert mainActivity != null;
        mainActivity.setOnClickBtnSave(this);
        SharedPreferences preferences = Objects.requireNonNull(getContext())
                .getSharedPreferences("txtContent", Context.MODE_PRIVATE);
        mEditor = preferences.edit();
        myEditText.setText(preferences.getString("txtContent", ""));

        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myEditText.setFocusableInTouchMode(true);
                myEditText.setFocusable(true);
                myEditText.requestFocus();
                myEditText.setSelection(myEditText.getText().length());
                if (!myEditText.hasFocus()) {
                    InputMethodManager imm =
                            (InputMethodManager) Objects.requireNonNull(getContext())
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.showSoftInput(myEditText, InputMethodManager.SHOW_IMPLICIT);
                }
                return false;
            }
        });
    }

    @Override
    protected void addListener() {

    }

    @Override
    public void onClickSave() {
        mEditor.putString("txtContent", myEditText.getText().toString().trim());
        mEditor.commit();
        Toast.makeText(getContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
    }
}
