package cherry.ethereal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import cherry.ethereal.adapter.MusicTypeAdapter;
import cherry.ethereal.data.Lrc.SongJson;
import cherry.ethereal.data.MusicTypeJson;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicTypeActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;
    private TextView showTitle;
    private ListView typeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_type);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        final Intent intent = getIntent();
        Integer typeID = intent.getIntExtra("data", 1);
        String title = intent.getStringExtra("title");
        showTitle = (TextView) findViewById(R.id.showTitle);
        showTitle.setText(title);

        typeListView = (ListView) findViewById(R.id.typeListView);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl(typeID))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                MusicTypeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        typeListView.setAdapter(new MusicTypeAdapter(BaseObjectToJson(json), MusicTypeActivity.this));
                        typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                TextView musicIDText = view.findViewById(R.id.music_id);
                                TextView musicNameText = view.findViewById(R.id.music_list_name);
                                TextView musicAuthorText = view.findViewById(R.id.music_list_author);
                                Intent intent1 = new Intent();
                                intent1.putExtra("songID", musicIDText.getText());
                                intent1.putExtra("songName",musicNameText.getText());
                                intent1.putExtra("songAuthor",musicAuthorText.getText());
                                setResult(0x0004, intent1);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    //音乐基础信息
    private List<MusicTypeJson.PlayList.Tracks> BaseObjectToJson(String json) {
        MusicTypeJson musicType = new Gson().fromJson(json, MusicTypeJson.class);
        List<MusicTypeJson.PlayList.Tracks> tracks = musicType.playlist.tracks;
        return tracks;
    }

    public String getUrl(Integer id) {
        String str = "";
        switch (id) {
            case 1:
                str = "8";
                break;
            case 2:
                str = "1";
                break;
            case 3:
                str = "2";
                break;
            case 4:
                str = "3";
                break;
            case 5:
                str = "4";
                break;
            case 6:
                str = "5";
                break;
        }
        return getResources().getString(R.string.domain) + "/top/list?idx=" + str;
    }

}
