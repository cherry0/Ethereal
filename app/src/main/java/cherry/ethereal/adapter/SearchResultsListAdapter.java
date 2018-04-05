package cherry.ethereal.adapter;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.util.Util;

import cherry.ethereal.data.ColorWrapper;
import cherry.ethereal.R;
import cherry.ethereal.data.MusicList.LocalMusicList;
import cherry.ethereal.data.MusicList.MusicListBase;
import cherry.ethereal.data.MusicUnit.MusicSuggestion;
import cherry.ethereal.data.MusicUnit.SearchProposal;

import java.util.ArrayList;
import java.util.List;


public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<SearchProposal.Content.Songs> mDataSet = new ArrayList<>();

    private int mLastAnimatedItemPosition = -1;
    private Activity activity;

    public interface OnItemClickListener {
        void onClick(SearchProposal.Content.Songs colorWrapper);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mColorName;
        public final TextView mColorValue;
        public final View mTextContainer;

        public ViewHolder(View view) {
            super(view);
            mColorName = (TextView) view.findViewById(R.id.music_name);
            mColorValue = (TextView) view.findViewById(R.id.music_author);
            mTextContainer = view.findViewById(R.id.text_container);
        }
    }

    public void swapData(List<SearchProposal.Content.Songs> mNewDataSet, Activity _activity) {
        mDataSet = mNewDataSet;
        this.activity = _activity;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchResultsListAdapter.ViewHolder holder, final int position) {
                SearchProposal.Content.Songs colorSuggestion = mDataSet.get(position);
        holder.mColorName.setText(colorSuggestion.name);
        holder.mColorValue.setText(colorSuggestion.getAuthors(colorSuggestion.artists));
        this.setItemsOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(SearchProposal.Content.Songs colorWrapper) {
                Boolean flag = true;//判断歌曲是否存在 true 当前待播放歌曲不存在歌曲列表中，false反之
                MusicListBase musicListBase=null;
                List<MusicListBase.Musics> musicsList =null;

                LocalMusicList list = new LocalMusicList(activity);
                if(list.getList()!=null) {
                    musicListBase = list.getList();
                    musicsList = list.getList().getMusics();
                }else{
                    musicListBase = new MusicListBase();
                    musicsList = new ArrayList<>();
                }

                for (MusicListBase.Musics music : musicsList) {
                    if (music.getId().equals(colorWrapper.id)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    MusicListBase.Musics musics = musicListBase.new Musics();
                    musics.setSong_author(colorWrapper.getAuthors(colorWrapper.artists));
                    musics.setLrc_url("歌词地址");
                    musics.setCover_url("封面地址");
                    musics.setSong_paly_times(1);
                    musics.setSong_type("");
                    musics.setId(colorWrapper.id);
                    musics.setSong_name(colorWrapper.name);
                    musicsList.add(musics);
                    musicListBase.setMusics(musicsList);
                    if (list.saveList(musicListBase)) {
                        Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "歌曲已存在歌曲列表中", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        if (mItemsOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(mDataSet.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}
