package cherry.ethereal.data.MusicList;

/**
 * Created by many on 2018/4/5.
 */

interface MusicList {
    public Boolean saveList(MusicListBase musicListBase);

    public MusicListBase getList();

    public Boolean deleteSingleList();

    public Boolean deleteAllList();
}
