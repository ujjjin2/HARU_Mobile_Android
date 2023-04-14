//package com.object.haru.Fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.object.haru.Adapter.MyNoticeRecyclerViewAdapter;
//import com.object.haru.R;
//import com.object.haru.Fragment.placeholder.PlaceholderContent;
//
//public class NoticeFragment extends Fragment {
//
//    public NoticeFragment() {
//    }
//
//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static NoticeFragment newInstance(int columnCount) {
//        NoticeFragment fragment = new NoticeFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
//        RecyclerView recyclerView = view.findViewById(R.id.NoticeList);
//
//        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화
//
//        return view;
//    }
//}