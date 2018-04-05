package cherry.ethereal.data.MusicList;

import java.util.List;

/**
 * Created by many on 2018/4/5.
 */

public  class MusicListBase {
    private List<Musics> musics;

    public class Musics {
        private Integer id;
        private String cover_url;
        private String song_url;
        private String lrc_url;
        private String song_name;
        private String song_author;
        private Integer song_paly_times;
        private String song_type;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getSong_url() {
            return song_url;
        }

        public void setSong_url(String song_url) {
            this.song_url = song_url;
        }

        public String getLrc_url() {
            return lrc_url;
        }

        public void setLrc_url(String lrc_url) {
            this.lrc_url = lrc_url;
        }

        public String getSong_name() {
            return song_name;
        }

        public void setSong_name(String song_name) {
            this.song_name = song_name;
        }

        public String getSong_author() {
            return song_author;
        }

        public void setSong_author(String song_author) {
            this.song_author = song_author;
        }

        public Integer getSong_paly_times() {
            return song_paly_times;
        }

        public void setSong_paly_times(Integer song_paly_times) {
            this.song_paly_times = song_paly_times;
        }

        public String getSong_type() {
            return song_type;
        }

        public void setSong_type(String song_type) {
            this.song_type = song_type;
        }
    }

    public List<Musics> getMusics() {
        return musics;
    }

    public void setMusics(List<Musics> musics) {
        this.musics = musics;
    }
}
