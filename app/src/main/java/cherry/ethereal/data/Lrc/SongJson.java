package cherry.ethereal.data.Lrc;

import java.util.List;

public class SongJson {
    //注意变量名与字段名一致
    public Integer code;
    public List<MusicBase> data;

    public class MusicBase {
        public Integer id;
        public String url;
        public String br;
        public String size;
        public String md5;
        public String code;
    }
}
