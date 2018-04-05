package cherry.ethereal;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cherry.ethereal.adapter.ViewAdapter;

import static cherry.ethereal.CustomRender.rsBlur;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager mviewPager;
    private ViewAdapter viewAdapter;
    private List<View> pageList;
    private OnFragmentInteractionListener mListener;
    private ImageView imageView;
    private TextView mplayBtn;
    private TextView mpreviousBtn;
    private TextView mnextBtn;
    private TextView mlistBtn;
    private TextView mplayOptionBtn;
    private android.app.FragmentManager fragmentManager;
    private MusicListFragment musicListFragment;

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_main, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


//    @Override
//    public void showOrHideWindow() {
//        Toast.makeText(getContext(),"111",Toast.LENGTH_SHORT).show();
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        FragmentManager getSupportFragmentManagerInfo();
    }

    /**
     * 数据初始化
     *
     * @param view
     */
    private void init(View view) {
        mviewPager = (ViewPager) view.findViewById(R.id.music_view_page);
        pageList = new ArrayList<View>();
        //动态加载viewPage
        android.support.v4.app.Fragment lrcFragment = new LrcFragment();
        android.support.v4.app.Fragment coverFragment = new CoverFragment();

        List<android.support.v4.app.Fragment> fr_list = new ArrayList<android.support.v4.app.Fragment>();
        //组织数据源
        fr_list.add(coverFragment);
        fr_list.add(lrcFragment);

        //设置适配器
        viewAdapter = new ViewAdapter(mListener.getSupportFragmentManagerInfo(), fr_list);
        mviewPager.setAdapter(viewAdapter);


        //设置背景图片
//        imageView = (ImageView) view.findViewById(R.id.coverBackground);
//
////        Bitmap newBitmap = rsBlur(getContext(), bitmap, 25, 1);
////        BitmapDrawable bitmapDrawable = new BitmapDrawable(newBitmap);
////        imageView.setBackground(bitmapDrawable);
        mplayBtn = (TextView) view.findViewById(R.id.playBtn);
        mnextBtn = (TextView) view.findViewById(R.id.nextBtn);
        mpreviousBtn = (TextView) view.findViewById(R.id.previousBtn);
        mlistBtn = (TextView) view.findViewById(R.id.listBtn);
        mplayOptionBtn = (TextView) view.findViewById(R.id.playOptionBtn);

        onPlayClickEvent();
        onNextClickEvent();
        onPauseClickEvent();
        onListClickEvent();
        onPlayOptionClickEvent();

        Typeface iconfont = Typeface.createFromAsset(getResources().getAssets(), "iconfont.ttf");
        mplayBtn.setTypeface(iconfont);
        mnextBtn.setTypeface(iconfont);
        mpreviousBtn.setTypeface(iconfont);
        mlistBtn.setTypeface(iconfont);
        mplayOptionBtn.setTypeface(iconfont);
        fragmentManager = getChildFragmentManager();
        musicListFragment = ((MusicListFragment) fragmentManager.findFragmentById(R.id.music_list_fragment));
    }

    /**
     * 播放按钮点击事件
     */
    public void onPlayClickEvent() {
        mplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击播放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 上一曲按钮点击事件
     */
    public void onPauseClickEvent() {
        mpreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击上一曲", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 下一曲按钮点击事件
     */
    public void onNextClickEvent() {
        mnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击下一曲", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 歌词列表按钮点击事件
     */
    public void onListClickEvent() {
        mlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicListFragment.showOrHideWindow();
                Toast.makeText(getContext(), "点击目录列表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 播放设置按钮点击事件
     */
    public void onPlayOptionClickEvent() {
        mplayOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击播放设置", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
