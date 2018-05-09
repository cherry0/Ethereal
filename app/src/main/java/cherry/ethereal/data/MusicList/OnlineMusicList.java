package cherry.ethereal.data.MusicList;

import android.app.Activity;

import java.util.Collections;


/**
 * Created by many on 2018/4/5.
 * 歌词清单
 */

public class OnlineMusicList extends Base implements MusicList {

    public OnlineMusicList(MusicList _base, Activity _activity) {
        super(_base, _activity);
    }

    public OnlineMusicList(Activity _activity) {
        super(_activity);
    }

    @Override
    public Boolean saveList(MusicListBase musicListBase) {
        return this.saveMusicList(this.mActivity, musicListBase);
    }

    @Override
    public MusicListBase getList() {
        MusicListBase musicListBase = this.loadMusicList(this.mActivity);
        return musicListBase;
    }

    @Override
    public Boolean deleteSingleList() {
        return null;
    }

    @Override
    public Boolean deleteAllList() {
        return null;
    }
}
