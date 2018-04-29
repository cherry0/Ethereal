package cherry.ethereal.adapter;

import android.content.Context;
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

public class LeftMenuAdapter extends BaseAdapter {
    private Context context;
    private String[] ListName={"新碟上架","每日推荐","显示/隐藏"};
    public LeftMenuAdapter(Context _context) {
        this.context = _context;
    }

    @Override
    public int getCount() {
        return ListName.length;
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
        View view = null;
        LayoutInflater layoutInflater = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            layoutInflater = LayoutInflater.from(this.context);
            view = layoutInflater.inflate(R.layout.left_menu_list_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            viewHolder.mMusicname =(TextView)view.findViewById(R.id.left_menu_item);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mMusicname.setText(ListName[position]);
        return view;
    }

    class ViewHolder {
        private View mview;
        public TextView mMusicname;

        public ViewHolder(View view) {
            mview = view;
        }

    }
}
