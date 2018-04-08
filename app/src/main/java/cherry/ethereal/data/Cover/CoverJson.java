package cherry.ethereal.data.Cover;

import java.util.List;

public class CoverJson {
    public Integer code;
    public List<MusicSong> songs;

    public class MusicSong {
        public String name;
        public Integer id;
        public als al;

        public class als {
            public Integer id;
            public String picUrl;
        }
    }

}
