package com.project.app.ui.dialog;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project.app.R;
import com.project.app.adapter.CountSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectdCountDialog extends BottomSheetDialog {
    private final Context mContext;
    private RecyclerView rlv_sea;
    private CountSelectAdapter mAdapter;
    private final List<String> mData = new ArrayList<>();

    public SelectdCountDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public SelectdCountDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_select_count);
        rlv_sea = findViewById(R.id.rlv_selectCount);
        for(int i=0;i<15;i++){
            mData.add(i+"");
        }
        mAdapter = new CountSelectAdapter(mData);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rlv_sea.setLayoutManager(manager);
        rlv_sea.setAdapter(mAdapter);
    }



}
