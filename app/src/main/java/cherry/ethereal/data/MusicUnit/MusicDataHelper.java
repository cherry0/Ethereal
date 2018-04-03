package cherry.ethereal.data.MusicUnit;

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

import android.content.Context;
import android.util.Log;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cherry.ethereal.R;
import cherry.ethereal.data.ColorSuggestion;
import cherry.ethereal.data.ColorWrapper;
import cherry.ethereal.data.MusicUnit.MusicSuggestion;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cherry.ethereal.data.EncodeUrl.toURLEncoded;

public class MusicDataHelper {

    private static final String COLORS_FILE_NAME = "music.json";

    private static List<SearchProposal.Content.Songs> sColorWrappers = new ArrayList<>();

    private static List<MusicSuggestion> sColorSuggestions =
            new ArrayList<>(Arrays.asList(
                    new MusicSuggestion("Hello"),
                    new MusicSuggestion("不想说话"),
                    new MusicSuggestion("男孩"),
                    new MusicSuggestion("purple")
            ));

    public interface OnFindColorsListener {
        void onResults(List<SearchProposal.Content.Songs> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<MusicSuggestion> results);
    }

    public static List<MusicSuggestion> getHistory(Context context, int count) {

        List<MusicSuggestion> suggestionList = new ArrayList<>();
        MusicSuggestion colorSuggestion;
        for (int i = 0; i < sColorSuggestions.size(); i++) {
            colorSuggestion = sColorSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (MusicSuggestion colorSuggestion : sColorSuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(final Context context, final String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Url = context.getString(R.string.domain) + "/search/suggest?keywords=" + toURLEncoded(query);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Url)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String songString = response.body().string();
                    Log.i("获取网络数据", songString);
                    if (!songString.equals("")) {
                        sColorWrappers = deserializeColors(songString);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    sColorSuggestions.add(new MusicSuggestion("最爱"));
//                sColorSuggestions.add(new MusicSuggestion("哈哈"));
//                    Thread.sleep(simulatedDelay);
                sColorSuggestions.clear();
                List<SearchProposal.Content.Songs> songs = sColorWrappers;
                for (SearchProposal.Content.Songs song : songs) {
                    sColorSuggestions.add(new MusicSuggestion(song.name));
                }
                MusicDataHelper.resetSuggestionsHistory();
                List<MusicSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (MusicSuggestion suggestion : sColorSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<MusicSuggestion>() {
                    @Override
                    public int compare(MusicSuggestion lhs, MusicSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<MusicSuggestion>) results.values);
                }
            }
        }.filter(query);

    }


    public static void findColors(Context context, String query, final OnFindColorsListener listener) {
        initColorWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<SearchProposal.Content.Songs> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (SearchProposal.Content.Songs color : sColorWrappers) {
                        if (color.name.toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<SearchProposal.Content.Songs>) results.values);
                }
            }
        }.filter(query);

    }

    private static void initColorWrapperList(Context context) {

        if (sColorWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sColorWrappers = deserializeColors(jsonString);
        }
    }

    private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private static List<SearchProposal.Content.Songs> deserializeColors(String jsonString) {

        SearchProposal searchMusicList = new Gson().fromJson(jsonString, SearchProposal.class);
        SearchProposal.Content objResult = searchMusicList.result;
        SearchProposal.Content.Songs So = objResult.songs.get(0);
        return objResult.songs;
    }

}