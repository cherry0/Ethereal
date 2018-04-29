package cherry.ethereal.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cherry.ethereal.R;
import cherry.ethereal.data.MusicTypeJson;

public class MusicTypeAdapter extends BaseAdapter {
    private List<MusicTypeJson.PlayList.Tracks> musicTypeList;
    private Context context;

    public MusicTypeAdapter(List<MusicTypeJson.PlayList.Tracks> _musicTypeList, Context _context) {
        this.musicTypeList = _musicTypeList;
        this.context = _context;
    }
    @Override
    public int getCount() {
        return musicTypeList.size();
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
        MusicTypeAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            layoutInflater=LayoutInflater.from(this.context);
            view=layoutInflater.inflate(R.layout.type_music_list_item,viewGroup,false);
            viewHolder = new MusicTypeAdapter.ViewHolder(view);
            viewHolder.mMusicname=view.findViewById(R.id.music_list_name);
            viewHolder.mMusicAuthor=view.findViewById(R.id.music_list_author);
            viewHolder.mMusic_id=view.findViewById(R.id.music_id);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(MusicTypeAdapter.ViewHolder)view.getTag();
        }
        viewHolder.mMusicAuthor.setTextColor(context.getResources().getColor(R.color.fontColorSmart));
        viewHolder.mMusicname.setTextColor(context.getResources().getColor(R.color.fontColor));
        viewHolder.mMusicname.setText(musicTypeList.get(position).name);
        viewHolder.mMusic_id.setText(String.valueOf(musicTypeList.get(position).id));
        viewHolder.mMusicAuthor.setText(musicTypeList.get(position).ar.get(0).name);
        return view;
    }
    class ViewHolder {
        private View mview;
        public TextView mMusicname;
        public TextView mMusicAuthor;
        public TextView mMusic_id;
        public ViewHolder(View view){
            mview=view;
        }

    }
}
