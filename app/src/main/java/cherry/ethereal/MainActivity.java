package cherry.ethereal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cherry.ethereal.Service.Binder.ComputeBinder;
import cherry.ethereal.Service.ComputeService;
import cherry.ethereal.adapter.LeftMenuAdapter;
import cherry.ethereal.data.GlideImageLoader;
import cherry.ethereal.data.Lrc.SongJson;
import cherry.ethereal.data.MusicList.LocalMusicList;
import cherry.ethereal.data.MusicList.MusicListBase;
import cherry.ethereal.data.MusicList.OnlineMusicList;
import cherry.ethereal.data.PlayModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements MusicFragment.OnFragmentInteractionListener,
        LrcFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        CoverFragment.OnFragmentInteractionListener,
        MusicListFragment.OnMusicListFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private MusicFragment musicFragment;
    private MainFragment mainFragment;
    private DrawerLayout mDrawerLayout;
    private MediaPlayer mediaPlayer;
    private ComputeBinder binder = null;
    private int playIndex = 0;//当前播放在歌曲列表中的位置
    private boolean flagBtn = true;
    private boolean isStop = false;
    private Thread thread;
    private ListView left_menu_list;
    private Boolean isOnlineMusic = false;
    private Banner banner;
//    private ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            binder = (ComputeBinder) iBinder;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };

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
//        musicFragment = ((MusicFragment) fragmentManager.findFragmentById(R.id.musicFragment));
        mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.mainFragment);
//        musicFragment.musicSeekBar.setOnSeekBarChangeListener(new SeekBarOnChange());
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(musicFragment);
//        fragmentTransaction.commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //菜单栏设置
        left_menu_list = (ListView) findViewById(R.id.left_menu_list);
        left_menu_list.setAdapter(new LeftMenuAdapter(this));
        left_menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, MusicTypeActivity.class);
                        intent.putExtra("data", 2);
                        intent.putExtra("title", "新碟上架");
                        startActivityForResult(intent, Activity.RESULT_FIRST_USER);

                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, EveryDayActivity.class);
                        startActivityForResult(intent, 0x0001);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case 2:
                        if (musicFragment == null) {
                            musicFragment = MusicFragment.newInstance();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.musicFragment, musicFragment).commit();
                        } else {
                            MainActivity.this.ShowOrHidePlayerWindow();
                        }
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
            }
        });


        banner = (Banner) findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add("http://p1.music.126.net/sLR8-BRur-Wn7Iye12Bmcg==/109951163272896720.jpg");
        images.add("http://p1.music.126.net/_bCCuvK9vMp-7razCBbE-w==/109951163273215796.jpg");
        //设置图片集合
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.CubeOut);
        banner.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //启动音乐服务
//        Intent intent = new Intent(this, ComputeService.class);
//        intent.setAction("ethereal.music.service");
//        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    public void setSeekBar(SeekBar seek) {
        seek.setOnSeekBarChangeListener(new SeekBarOnChange());
    }

    /**
     * 显示或者隐藏播放器
     */
    public void ShowOrHidePlayerWindow() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (musicFragment.isHidden()) {
            fragmentTransaction.show(musicFragment);
            banner.stopAutoPlay();
        } else {
            fragmentTransaction.hide(musicFragment);
            banner.startAutoPlay();
        }
        fragmentTransaction.commit();
    }


    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManagerInfo() {
        return getSupportFragmentManager();
    }

    @Override
    public void hideCover() {
        MainActivity.this.ShowOrHidePlayerWindow();
    }


    @Override
    public void onBackPresseds(SearchView mSearchView) {

    }

    @Override
    protected void onDestroy() {
//        unbindService(conn);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!mainFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }


    public void readyPlay(Integer ID, Integer position) {
        if (musicFragment == null) {
            musicFragment = MusicFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.musicFragment, musicFragment).commit();
            fragmentManager.executePendingTransactions();
        }
        //点击音乐列表 1、加载音乐；2、加载封面cover；3、获取歌曲歌词
        musicFragment.loadLrcAndCover(ID);
        GetMusicUrl(ID);
        playIndex = position;

    }

    public void readyPlay(Integer ID) {
        if (musicFragment == null) {
            musicFragment = MusicFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.musicFragment, musicFragment).commit();
            fragmentManager.executePendingTransactions();
        }
        //点击音乐列表 1、加载音乐；2、加载封面cover；3、获取歌曲歌词
        musicFragment.loadLrcAndCover(ID);
        GetMusicUrl(ID);
        OnlineMusicList list = new OnlineMusicList(MainActivity.this);
        playIndex = list.getList().getMusics().size();

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
    public void playOrPause() {

        if (flagBtn == true) {
            if (mediaPlayer == null) {
                //当前无任何播放服务执行
                playIndex = 0;
                OnlineMusicList onlineMusicList = new OnlineMusicList(this);
                if (onlineMusicList.getList() != null) {
                    readyPlay(onlineMusicList.getList().getMusics().get(playIndex).getId(), playIndex);

                    setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(), onlineMusicList.getList().getMusics().get(playIndex).getSong_author());
                }

            } else {
                mediaPlayer.start();
                musicFragment.mplayBtn.setText(R.string.icons_pause);
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
    public void next() {
        isStop = false;
        musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playIndex++;

        OnlineMusicList onlineMusicList = new OnlineMusicList(this);
        if (onlineMusicList.getList() == null) {
            Toast.makeText(MainActivity.this, "请先添加歌曲", Toast.LENGTH_SHORT).show();
            return;
        }
        if (playIndex >= onlineMusicList.getList().getMusics().size()) {
            playIndex = 0;
        }
        mediaPlayer = null;
        musicFragment.loadLrcAndCover(onlineMusicList.getList().getMusics().get(playIndex).getId());
        GetMusicUrl(onlineMusicList.getList().getMusics().get(playIndex).getId());
        setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(), onlineMusicList.getList().getMusics().get(playIndex).getSong_author());
    }

    //上一曲
    public void previous() {
        musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playIndex--;
        OnlineMusicList onlineMusicList = new OnlineMusicList(this);
        if (onlineMusicList.getList() == null) {
            Toast.makeText(MainActivity.this, "请先添加歌曲", Toast.LENGTH_SHORT).show();
            return;
        }
        if (playIndex < 0) {
            playIndex = onlineMusicList.getList().getMusics().size() - 1;
        }
        mediaPlayer = null;
        GetMusicUrl(onlineMusicList.getList().getMusics().get(playIndex).getId());
        musicFragment.loadLrcAndCover(onlineMusicList.getList().getMusics().get(playIndex).getId());
        setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(), onlineMusicList.getList().getMusics().get(playIndex).getSong_author());
    }

    MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            musicFragment.setPlayAbt(mediaPlayer.getDuration());
            musicFragment.setLrcUpdateToTime(0);
            musicFragment.setPlaySize(String.valueOf(convert(mediaPlayer.getDuration())));
            isStop = true;
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
            playModelSelect(PlayModel.getPlayModelIndex());
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
    public void GetMusicUrl(Integer songID) {
        if (songID.equals(null)) {
            Toast.makeText(MainActivity.this, "请先添加歌曲", Toast.LENGTH_SHORT).show();
            return;
        }
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
            Log.i("设置", String.valueOf(isStop));
        }
    }

    // 自定义的线程
    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (mediaPlayer != null) {
                if (isStop == true) {
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
    public void timeToPlay(long time) {
        mediaPlayer.seekTo((int) time);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        onActivityResultCall(intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void onActivityResultCall(Intent intent) {
        if (intent != null) {
            Integer songID = Integer.valueOf(intent.getStringExtra("songID"));
            String songName = intent.getStringExtra("songName");
            String songAuthor = intent.getStringExtra("songAuthor");
            Boolean flag = true;//判断歌曲是否存在 true 当前待播放歌曲不存在歌曲列表中，false反之
            MusicListBase musicListBase = null;
            List<MusicListBase.Musics> musicsList = null;

            OnlineMusicList list = new OnlineMusicList(MainActivity.this);
            if (list.getList() != null) {
                musicListBase = list.getList();
                musicsList = list.getList().getMusics();
            } else {
                musicListBase = new MusicListBase();
                musicsList = new ArrayList<>();
            }

            for (MusicListBase.Musics music : musicsList) {
                if (music.getId().equals(songID)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                MusicListBase.Musics musics = musicListBase.new Musics();
                musics.setSong_author(songAuthor);
                musics.setLrc_url("歌词地址");
                musics.setCover_url("封面地址");
                musics.setSong_paly_times(1);
                musics.setSong_type("");
                musics.setId(songID);
                musics.setSong_name(songName);
                musicsList.add(musics);
                musicListBase.setMusics(musicsList);
                if (list.saveList(musicListBase)) {
                    Log.i("保存歌曲","成功");
//                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("保存歌曲","失败");
//                Toast.makeText(MainActivity.this, "歌曲已存在歌曲列表中", Toast.LENGTH_SHORT).show();
            }
            setTilteAndAuthor(songName,songAuthor);
            readyPlay(songID);
            isOnlineMusic = true;
            if (!musicFragment.isVisible()) {
                this.ShowOrHidePlayerWindow();
            }

        }
    }

    /**
     * 播放模式
     */
    public void playModelSelect(Integer indexModel) {
        switch (indexModel) {
            case 1:
                //列表循环
                next();
                break;
            case 2:
                //单曲循环
                playIndex--;
                next();
                break;
            case 3:
                //随机播放
                OnlineMusicList onlineMusicList = new OnlineMusicList(this);
                Integer index = onlineMusicList.getList().getMusics().size();
                Log.i("index--------", String.valueOf(index));
                Random random = new Random();
                playIndex = random.nextInt(index);
                Log.i("playIndex--------", String.valueOf(playIndex));
                isStop = false;
                musicFragment.mplayBtn.setText(this.getString(R.string.icons_play));
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = null;
                musicFragment.loadLrcAndCover(onlineMusicList.getList().getMusics().get(playIndex).getId());
                GetMusicUrl(onlineMusicList.getList().getMusics().get(playIndex).getId());
                setTilteAndAuthor(onlineMusicList.getList().getMusics().get(playIndex).getSong_name(), onlineMusicList.getList().getMusics().get(playIndex).getSong_author());

                break;
        }
    }


}


