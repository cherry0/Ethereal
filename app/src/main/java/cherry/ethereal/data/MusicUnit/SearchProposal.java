package cherry.ethereal.data.MusicUnit;

import java.util.List;

public class SearchProposal {
    //HTTP状态码
    public Integer code;
    public Content result;
    public class Content{
        public List<Songs> songs;
        public class Songs{
            //歌曲ID
            public Integer id;
            //歌曲名称
            public String name;
            public List<Artists> artists;
            public class Artists{
                //作者ID
                public Integer id;
                //作者名称
                public String name;
            }
        }
    }
}
