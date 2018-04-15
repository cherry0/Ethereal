package cherry.ethereal.data;

import java.util.List;

public class MusicTypeJson {
    public PlayList playlist;
    public class PlayList{
        public List<Tracks> tracks;
        public class Tracks{
            public String name;
            public Integer id;
            public List<AR> ar;
            public class AR{
                public String name;
            }
        }
    }
}
