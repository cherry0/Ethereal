package cherry.ethereal;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cherry.ethereal.adapter.ViewAdapter;
import cherry.ethereal.data.PlayModel;

import static cherry.ethereal.CustomRender.rsBlur;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment implements View.OnTouchListener{
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
    public ImageView imageView;
    public TextView mplayBtn;
    public TextView mpreviousBtn;
    public TextView mnextBtn;
    public TextView mlistBtn;
    public TextView startTime;
    public TextView endTime;
    public TextView mplayOptionBtn;
    private android.app.FragmentManager fragmentManager;
    private MusicListFragment musicListFragment;
    private android.support.v4.app.Fragment coverFragment;
    private android.support.v4.app.Fragment lrcFragment;
    public SeekBar musicSeekBar;
    public Integer playModelIndex = 1;

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
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
        view.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
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

        void playOrPause();

        void next();

        void previous();

        void setSeekBar(SeekBar seek);
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
        lrcFragment = new LrcFragment();
        coverFragment = new CoverFragment();

        musicSeekBar = (SeekBar) view.findViewById(R.id.musicSeekBar);
        List<android.support.v4.app.Fragment> fr_list = new ArrayList<android.support.v4.app.Fragment>();
        //组织数据源
        fr_list.add(coverFragment);
        fr_list.add(lrcFragment);

        //设置适配器
        viewAdapter = new ViewAdapter(mListener.getSupportFragmentManagerInfo(), fr_list);
        mviewPager.setAdapter(viewAdapter);


        //设置背景图片
        imageView = (ImageView) view.findViewById(R.id.coverBackground);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap newBitmap = rsBlur(getContext(), bitmap, 25, 1);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(newBitmap);
        imageView.setBackground(bitmapDrawable);

        mplayBtn = (TextView) view.findViewById(R.id.playBtn);
        mnextBtn = (TextView) view.findViewById(R.id.nextBtn);
        mpreviousBtn = (TextView) view.findViewById(R.id.previousBtn);
        mlistBtn = (TextView) view.findViewById(R.id.listBtn);
        mplayOptionBtn = (TextView) view.findViewById(R.id.playOptionBtn);
        startTime = (TextView) view.findViewById(R.id.timeStartText);
        endTime = (TextView) view.findViewById(R.id.timeEndText);


        onPlayClickEvent();
        onNextClickEvent();
        onPauseClickEvent();
        onListClickEvent();
        onPlayOptionClickEvent();

        mListener.setSeekBar(musicSeekBar);


        Typeface iconfont = Typeface.createFromAsset(getResources().getAssets(), "iconfont.ttf");
        mplayBtn.setTypeface(iconfont);
        mnextBtn.setTypeface(iconfont);
        mpreviousBtn.setTypeface(iconfont);
        mlistBtn.setTypeface(iconfont);
        mplayOptionBtn.setTypeface(iconfont);
//        hiddenMusicWindowBtn.setTypeface(iconfont);
        fragmentManager = getChildFragmentManager();


    }

    /**
     * 播放按钮点击事件
     */
    public void onPlayClickEvent() {
        mplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.playOrPause();
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
                mListener.previous();
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
                mListener.next();
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
//                musicListFragment.showOrHideWindow();
                musicListFragment = new MusicListFragment();
                //开启FragmentTransaction事务
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //通过事务向Activity的布局中添加MyFragment
                fragmentTransaction.add(R.id.music_list_fragment, musicListFragment);
                //提交事务
                fragmentTransaction.commit();
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
                playModelIndex++;
                if(playModelIndex>3){
                    playModelIndex=1;
                }
                switch (playModelIndex) {
                    case 1:
                        mplayOptionBtn.setText(R.string.icons_list_loop_play);
                        PlayModel.setPlayModelIndex(1);
                        Toast.makeText(getContext(),"循环列表",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        mplayOptionBtn.setText(R.string.icons_once_loop_play);
                        PlayModel.setPlayModelIndex(2);
                        Toast.makeText(getContext(),"单曲循环",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        mplayOptionBtn.setText(R.string.icons_random_play);
                        PlayModel.setPlayModelIndex(3);
                        Toast.makeText(getContext(),"随机播放",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        mplayOptionBtn.setText(R.string.icons_list_loop_play);
                        PlayModel.setPlayModelIndex(1);
                        Toast.makeText(getContext(),"循环列表",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    //加载歌词和歌曲封面
    public void loadLrcAndCover(Integer songID) {
        CoverFragment coverFragmentClass = (CoverFragment) ((ViewAdapter) mviewPager.getAdapter()).getItem(0);
        LrcFragment lrcFragmentClass = (LrcFragment) ((ViewAdapter) mviewPager.getAdapter()).getItem(1);
        coverFragmentClass.changeCover(songID);
        lrcFragmentClass.loadLrc(songID);
    }

    //设置歌曲标题和作者名称
    public void setTitleAndAuthor(String Name, String Author) {
        CoverFragment coverFragmentClass = (CoverFragment) ((ViewAdapter) mviewPager.getAdapter()).getItem(0);

        coverFragmentClass.changeMusicInfo(Name, Author);
    }

    //设置歌词位置
    public void setLrcUpdateToTime(Integer time) {
        LrcFragment lrcFragmentClass = (LrcFragment) ((ViewAdapter) mviewPager.getAdapter()).getItem(1);
        lrcFragmentClass.updateTime(time);
    }

    //设置播放属性
    public void setPlayAbt(int seekMax) {
        musicSeekBar.setMax(seekMax);
    }

    //设置音乐播放时间
    public void setPlaySize(String time) {
        endTime.setText(time);
    }


}
