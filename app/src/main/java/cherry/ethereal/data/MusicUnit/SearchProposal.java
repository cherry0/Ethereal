package cherry.ethereal.data.MusicUnit;

import java.util.List;

public class SearchProposal {
    //HTTP状态码
    public Integer code;
    public Content result;

    public class Content {
        public List<Songs> songs;

        public class Songs {
            //歌曲ID
            public Integer id;
            //歌曲名称
            public String name;
            public List<Artists> artists;

            public String getAuthors(List<Artists> artists) {
                String strAuthor = "";
                for (Artists artist : artists) {
                    strAuthor = strAuthor + artist.name + ",";
                }
                return strAuthor.substring(0, strAuthor.length() - 1);
            }

            public class Artists {
                //作者ID
                public Integer id;
                //作者名称
                public String name;
            }
        }
    }
}
