package cherry.ethereal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cherry.ethereal.data.Lrc.LrcJson;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import cherry.ethereal.broadcast.VolumeReceiver;
import me.wcy.lrcview.LrcView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LrcFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LrcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LrcFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LrcView lrcView;
    private TextView voiceTextView;
    private SeekBar mvolumeSeekBar;
    private AudioManager am;
    private VolumeReceiver volumeReceiver;

    public LrcFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LrcFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LrcFragment newInstance(String param1, String param2) {
        LrcFragment fragment = new LrcFragment();
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
        View view = inflater.inflate(R.layout.music_lrc_page, null);
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
        void timeToPlay(long time);
    }

    public void init(View view) {
        lrcView = (LrcView) view.findViewById(R.id.lrc_view);
        lrcView.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
            @Override
            public boolean onPlayClick(long time) {
                mListener.timeToPlay(time);
                return true;
            }
        });
        mvolumeSeekBar = (SeekBar) view.findViewById(R.id.volumeSeekBar);
        voiceTextView=(TextView)view.findViewById(R.id.voiceTextView);
        Typeface iconfont = Typeface.createFromAsset(getResources().getAssets(), "iconfont.ttf");
        voiceTextView.setTypeface(iconfont);

        setVolume();
    }

    public String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    public void setVolume() {
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mvolumeSeekBar.setMax(maxVolume);
        //获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mvolumeSeekBar.setProgress(currentVolume);
        mvolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        volumeReceiver = new VolumeReceiver(am, mvolumeSeekBar);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getActivity().registerReceiver(volumeReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(volumeReceiver);
    }

    public void loadLrc(String lrcContent) {
        lrcView.loadLrc(lrcContent);
    }

    public void loadLrc(Integer songID) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://music.pengbobo.com/lyric?id=" + String.valueOf(songID))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.i("歌词JSON:",json);
                final LrcJson.LrcText base = LrcToJson(json);
                Log.i("歌词对象:",base.lyric);
                lrcView.post(new Runnable() {
                    @Override
                    public void run() {
                        lrcView.loadLrc(base.lyric);
                    }
                });
            }
        });
    }


    private LrcJson.LrcText LrcToJson(String json) {
        LrcJson lrcJson = new Gson().fromJson(json, LrcJson.class);
        return lrcJson.lrc;
    }

    //根据字节数组构建UTF-8字符串
    private String getStringByBytes(byte[] bytes) throws UnsupportedEncodingException {
        String str = new String(bytes, "UTF-8");
        return str;
    }

    //变更歌词位置
    public void updateTime(Integer time)
    {
        lrcView.updateTime(time);
    }
}