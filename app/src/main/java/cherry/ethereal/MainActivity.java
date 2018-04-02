package cherry.ethereal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;


public class MainActivity extends AppCompatActivity implements MusicFragment.OnFragmentInteractionListener,LrcFragment.OnFragmentInteractionListener,MainFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private MusicFragment musicFragment;
    private MainFragment mainFragment;
    private Button mshowPlayerBtn;


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
        mainFragment=(MainFragment)fragmentManager.findFragmentById(R.id.mainFragment);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(musicFragment);
//        fragmentTransaction.commit();
        mshowPlayerBtn = (Button) findViewById(R.id.showPlayerBtn);

    }

    /**
     * 显示播放器按钮点击事件
     *
     * @param view
     */
    public void ShowPlayer(View view) {
        ShowOrHidePlayerWindow();
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
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onBackPresseds(SearchView mSearchView) {

    }

    @Override
    public void onBackPressed() {
        if(!mainFragment.onBackPressed()){
            super.onBackPressed();
        }
    }

}
