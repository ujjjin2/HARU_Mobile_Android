package com.object.haru.Activity;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.object.haru.Adapter.DoAdapter;
import com.object.haru.Adapter.DongAdapter;
import com.object.haru.Adapter.SiAdapter;
import com.object.haru.R;
import com.object.haru.UpdateDongListener;
import com.object.haru.UpdateListener;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog;

    com.google.android.material.bottomsheet.BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;

    private View rootview;
    private ImageView cancel;

    private ArrayList<String> list;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (com.google.android.material.bottomsheet.BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        cancel = rootview.findViewById(R.id.cancel_button);

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerview1);
        RecyclerView recyclerView1 = rootview.findViewById(R.id.recyclerview2);
        RecyclerView recyclerView2 = rootview.findViewById(R.id.recyclerview3);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setHasFixedSize(true);
        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        list = new ArrayList<>();

        list.add("서울");
        list.add("경기");
        list.add("인천");
        list.add("부산");
        list.add("대구");
        list.add("광주");
        list.add("대전");
        list.add("울산");
        list.add("경남");
        list.add("경북");
        list.add("충남");
        list.add("충북");
        list.add("전남");
        list.add("전북");
        list.add("강원");
        list.add("제주");
        list.add("세종");
        list.add("전체");

        DoAdapter doAdapter = new DoAdapter(list, new UpdateListener() {
            @Override
            public void onUpdate(ArrayList<String> list) {
                SiAdapter siAdapter = new SiAdapter(list, new UpdateDongListener() {
                    @Override
                    public void onUpdate(ArrayList<String> list) {
                        DongAdapter dongAdapter = new DongAdapter(list);
                        recyclerView2.setAdapter(dongAdapter);
                        dongAdapter.notifyDataSetChanged();
                    }
                });
                recyclerView1.setAdapter(siAdapter);
                siAdapter.notifyDataSetChanged();
            }
        });

//        DoAdapter doAdapter = new DoAdapter(list, new UpdateListener() {
//            @Override
//            public void onUpdate(ArrayList<String> list) {
//                SiAdapter siAdapter = new SiAdapter(list);
//                recyclerView1.setAdapter(siAdapter);
//                siAdapter.notifyDataSetChanged();
//            }
//        }, new UpdateDongListener() {
//            @Override
//            public void onUpdate(ArrayList<String> list) {
//
//            }
//        });


                recyclerView.setAdapter(doAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootview;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setDraggable(false);

        CoordinatorLayout layout = dialog.findViewById(R.id.bottom_sheet_layout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }


}
