package cherry.ethereal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import cherry.ethereal.data.Cover.CoverJson;
import cherry.ethereal.data.Lrc.LrcJson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoverFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView coverImageView;
    private TextView songNameTextView;
    private TextView songAuthorTextView;

    public CoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoverFragment newInstance(String param1, String param2) {
        CoverFragment fragment = new CoverFragment();
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
        View view = inflater.inflate(R.layout.music_cover_page, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void init(View view) {
        coverImageView = (ImageView) view.findViewById(R.id.coverImgView);
        songNameTextView = (TextView) view.findViewById(R.id.titleText);
        songAuthorTextView = (TextView) view.findViewById(R.id.authorText);
    }

    int i = 0;

    public void changeCover(String Url) {
        Glide.with(this).load(Url).into(coverImageView);
    }

    public void changeCover(Integer songID) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://music.pengbobo.com/song/detail?ids=" + String.valueOf(songID))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.i("歌词JSON:",json);
                final CoverJson.MusicSong.als base = CoverObjectToJson(json);
                Log.i("歌词对象:",base.picUrl);
                if(base.picUrl!=null) {
                    coverImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(CoverFragment.this).load(base.picUrl).into(coverImageView);
                        }
                    });
                }else{
//                    Glide.with(CoverFragment.this).load().into(coverImageView);
                }
            }
        });
    }

    public void changeMusicInfo(String name, String author) {
        songNameTextView.setText(name);
        songAuthorTextView.setText(author);
    }
    //音乐封面图
    private CoverJson.MusicSong.als CoverObjectToJson(String json) {
        CoverJson cover = new Gson().fromJson(json, CoverJson.class);
        List<CoverJson.MusicSong> list = cover.songs;
        CoverJson.MusicSong find = list.get(0);
        CoverJson.MusicSong.als coverPth = find.al;
        Log.i("-------123---", coverPth.toString());
        return coverPth;
    }
}
