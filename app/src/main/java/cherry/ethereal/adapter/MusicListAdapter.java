package cherry.ethereal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import cherry.ethereal.R;
import cherry.ethereal.data.MusicList.MusicListBase;

/**
 * Created by many on 2018/4/5.
 */

public class MusicListAdapter extends BaseAdapter {
    private List<MusicListBase.Musics> musicList;
    private Context context;

    public MusicListAdapter(List<MusicListBase.Musics> _musicList, Context _context) {
        this.musicList = _musicList;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return this.musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //解析布局文件
        View view=null;
        LayoutInflater layoutInflater=null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            layoutInflater=LayoutInflater.from(this.context);
            view=layoutInflater.inflate(R.layout.music_list_item,viewGroup,false);
            viewHolder = new ViewHolder(view);
            viewHolder.mMusicname=view.findViewById(R.id.music_list_name);
            viewHolder.mMusicAuthor=view.findViewById(R.id.music_list_author);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.mMusicname.setText(musicList.get(position).getSong_name());
        viewHolder.mMusicAuthor.setText(musicList.get(position).getSong_author());
        return view;
    }

    class ViewHolder {
        private View mview;
        public TextView mMusicname;
        public TextView mMusicAuthor;
        public ViewHolder(View view){
            mview=view;
        }

    }
}
