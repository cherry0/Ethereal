package cherry.ethereal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cherry.ethereal.Service.Binder.ComputeBinder;
import cherry.ethereal.Service.ComputeService;
import cherry.ethereal.data.Lrc.SongJson;
import cherry.ethereal.data.MusicList.LocalMusicList;
import cherry.ethereal.data.MusicList.MusicListBase;
import cherry.ethereal.data.MusicList.OnlineMusicList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements MusicFragment.OnFragmentInteractionListener,
        LrcFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        MusicListFragment.OnMusicListFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private MusicFragment musicFragment;
    private MainFragment mainFragment;
    private Button mshowPlayerBtn;
    private Button mstopService;
    private MediaPlayer mediaPlayer;
    private ComputeBinder binder = null;
    private int playIndex=0;//当前播放在歌曲列表中的位置
    private boolean flagBtn=true;
    private boolean isStop = false;
    private Thread thread;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (ComputeBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 数据初始化
     */
    private void init() {
        fragmentManager = getFragmentManager();
        musicFragment = ((MusicFragment) fragmentManager.findFragmentById(R.id.musicFragment));
        mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.mainFragment);
        musicFragment.musicSeekBar.setOnSeekBarChangeListener(new SeekBarOnChange());
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(musicFragment);
//        fragmentTransaction.commit();
        mshowPlayerBtn = (Button) findViewById(R.id.showPlayerBtn);
        mstopService = (Button) findViewById(R.id.stopService);
        //启动音乐服务
        Intent intent = new Intent(this, ComputeService.class);
        intent.setAction("ethereal.music.service");
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, ComputeService.class);
        intent.setAction("ethereal.music.service");
        stopService(intent);
//        Log.i("服务","停止");
        String newstr = binder.count("小红");
        Toast.makeText(MainActivity.this, newstr, Toast.LENGTH_SHORT).show();

    }

    /**
     * 显示播放器按钮点击事件
     *
     * @param view
     */
    public void ShowPlayer(View view) {
        ShowOrHidePlayerWindow();
//
//        LocalMusicList list = new LocalMusicList(this);
//        MusicListBase musicListBase = list.getList();
//        if (musicListBase != null) {
//            musicListBase.getMusics();
//        }
    }

    /**
     * 显示或者隐藏播放器
     */
    public void ShowOrHidePlayerWindow() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (musicFragment.isHidden()) {
            fragmentTransaction.show(musicFragment);
        } else {
            fragmentTransaction.hide(musicFragment);
        }
        fragmentTransaction.commit();
    }


    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManagerInfo() {
        return getSupportFragmentManager();
    }



    @Override
    public void onBackPresseds(SearchView mSearchView) {

    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!mainFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }


    public void readyPlay(Integer ID,Integer position) {
        //点击音乐列表 1、加载音乐；2、加载封面cover；3、获取歌曲歌词
        musicFragment.loadLrcAndCover(ID);
        GetMusicUrl(ID);
        playIndex=position;

    }

    public void setTilteAndAuthor(String Name, String Author) {
        musicFragment.setTitleAndAuthor(Name, Author);
    }


    ///////////////////////////音乐播放相关///////////////////
    public void ready(String url) {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        //设置播放按钮
        musicFragment.mplayBtn.setText(this.getString(R.string.icons_pause));
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(preparedListener);
            mediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(completionListener);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //播放或者暂停
    public void playOrPause(){
        isStop=false;
        if (flagBtn == true) {
            if (mediaPlayer == null) {
                playIndex = 0;
                OnlineMusicList onlineMusicList=new OnlineMusicList(this);
                readyPlay(onlineMusicList.getList().getMusics().get(playIndex).getId(),playIndex);
                setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(),onlineMusicList.getList().getMusics().get(playIndex).getSong_author());

            } else {
                mediaPlayer.start();
            }
            flagBtn = false;
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
            }

            flagBtn = true;
        }
    }

    //下一曲
    public void next(){
        isStop=false;
        musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playIndex++;
        OnlineMusicList onlineMusicList=new OnlineMusicList(this);
        if(playIndex>=onlineMusicList.getList().getMusics().size()){
            playIndex=0;
        }
        mediaPlayer = null;
        musicFragment.loadLrcAndCover(onlineMusicList.getList().getMusics().get(playIndex).getId());
        GetMusicUrl(onlineMusicList.getList().getMusics().get(playIndex).getId());
        setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(),onlineMusicList.getList().getMusics().get(playIndex).getSong_author());
    }

    //上一曲
    public void previous(){
        musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playIndex--;
        OnlineMusicList onlineMusicList=new OnlineMusicList(this);
        if(playIndex<0){
            playIndex=onlineMusicList.getList().getMusics().size()-1;
        }
        mediaPlayer = null;
        GetMusicUrl(onlineMusicList.getList().getMusics().get(playIndex).getId());
        musicFragment.loadLrcAndCover(onlineMusicList.getList().getMusics().get(playIndex).getId());
        setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(),onlineMusicList.getList().getMusics().get(playIndex).getSong_author());
    }
    MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            musicFragment.setPlayAbt(mediaPlayer.getDuration());
            musicFragment.setLrcUpdateToTime(0);
            musicFragment.setPlaySize(String.valueOf(convert(mediaPlayer.getDuration())));
            isStop=true;
            musicFragment.setLrcUpdateToTime(0);
            try {
                // 创建一个线程
                thread = new Thread(new SeekBarThread());
                // 启动线程
                thread.start();
            } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
                e.printStackTrace();
            }

        }
    };

    //缓存进度
    MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            Log.i("位置：", String.valueOf(i));
//            textView22.setText("缓存进度:" + String.valueOf(i) + "%");
        }
    };

    //播放完成后
    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            next();
        }
    };

    private String convert(int duration) {
        //总秒
        int second = duration / 1000;
        //总分
        String minute = String.valueOf(second / 60);
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        //剩余秒数
        String miao = String.valueOf(second % 60);
        if (miao.length() < 2) {
            miao = "0" + miao;
        }
        return minute + ":" + miao;

    }

    //获取音乐播放地址
    public String GetMusicUrl(Integer songID) {
        String url = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://music.pengbobo.com/music/url?id=" + songID)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                SongJson.MusicBase base = BaseObjectToJson(json);
                final String playUrl = base.url;
                Log.i("歌曲地址:", base.url);
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ready(playUrl);
                    }
                });
            }
        });
        return url;
    }

    //音乐基础信息
    private SongJson.MusicBase BaseObjectToJson(String json) {
        SongJson musicInfos = new Gson().fromJson(json, SongJson.class);
        List<SongJson.MusicBase> list = musicInfos.data;
        return list.get(0);
    }

    class SeekBarOnChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//            showVoice1.setText(String.valueOf(convert(i)));
            musicFragment.startTime.setText(String.valueOf(convert(i)));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isStop = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            isStop = true;
            musicFragment.setLrcUpdateToTime(seekBar.getProgress());
            Log.i("设置",String.valueOf(isStop));
        }
    }
    // 自定义的线程
    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (mediaPlayer != null) {
                if(isStop==true) {
                    musicFragment.setLrcUpdateToTime(mediaPlayer.getCurrentPosition());
                    musicFragment.musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                try {
                    // 每500毫秒更新一次位置
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    //滚动歌词列表播放
    public void timeToPlay(long time){
        mediaPlayer.seekTo((int) time);
    }
}


