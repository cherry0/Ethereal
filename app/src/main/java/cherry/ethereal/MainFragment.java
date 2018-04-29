package cherry.ethereal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;

import cherry.ethereal.adapter.SearchResultsListAdapter;
import cherry.ethereal.data.MusicList.LocalMusicList;
import cherry.ethereal.data.MusicList.MusicListBase;
import cherry.ethereal.data.MusicList.OnlineMusicList;
import cherry.ethereal.data.MusicUnit.MusicDataHelper;
import cherry.ethereal.data.MusicUnit.MusicSuggestion;
import cherry.ethereal.data.MusicUnit.SearchProposal;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private FloatingSearchView mSearchView;
    private String mLastQuery = "标题";
    private final String TAG = "哈哈";
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private boolean mIsDarkSearchTheme = false;
    private RecyclerView mSearchResultsList;
    public SearchResultsListAdapter mSearchResultsAdapter;
    private AppBarLayout mAppBar;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    private Button newMusicBtn,everyDayBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        init(view);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void init(View view) {
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
//        mSearchResultsList = (RecyclerView) view.findViewById(R.id.search_results_list);
        mAppBar = (AppBarLayout) view.findViewById(R.id.appbar);

        mAppBar.addOnOffsetChangedListener(this);
        setupFloatingSearch();
        setupResultsList();
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        imageView5 = (ImageView) view.findViewById(R.id.imageView5);
        imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        newMusicBtn=(Button)view.findViewById(R.id.newMusicPublicBtn);
        everyDayBtn=(Button)view.findViewById(R.id.everyDayBtn);

        imageView1.setOnClickListener(MyListener);
        imageView2.setOnClickListener(MyListener);
        imageView3.setOnClickListener(MyListener);
        imageView4.setOnClickListener(MyListener);
        imageView5.setOnClickListener(MyListener);
        imageView6.setOnClickListener(MyListener);
        newMusicBtn.setOnClickListener(MyListener);
        everyDayBtn.setOnClickListener(MyListener);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mSearchView.setTranslationY(verticalOffset);
    }
    private View.OnClickListener MyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(getContext(), MusicTypeActivity.class);
            switch (view.getId()) {
                case R.id.imageView1:
                    intent.putExtra("data",1);
                    intent.putExtra("title","iTunes");
                    break;
                case R.id.imageView2:
                    intent.putExtra("data",2);
                    intent.putExtra("title","热歌榜");
                    break;
                case R.id.imageView3:
                    intent.putExtra("data",3);
                    intent.putExtra("title","原创榜");
                    break;
                case R.id.imageView4:
                    intent.putExtra("data",4);
                    intent.putExtra("title","ACG榜");
                    break;
                case R.id.imageView5:
                    intent.putExtra("data",5);
                    intent.putExtra("title","UK排行榜");
                    break;
                case R.id.imageView6:
                    intent.putExtra("data",6);
                    intent.putExtra("title","KTV榜");
                    break;
                case R.id.newMusicPublicBtn:
                    intent.putExtra("data",2);
                    intent.putExtra("title","新碟上架");
                    break;
                case R.id.everyDayBtn:
                    intent.setClass(getContext(), EveryDayActivity.class);
                    startActivityForResult(intent, 0x0001);
                    break;
            }
            startActivityForResult(intent, Activity.RESULT_FIRST_USER);
        }

    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBackPresseds(SearchView mSearchView);
        void onActivityResultCall(Intent intent);
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();
                    //simulates a query call to a data source
                    //with a new query.
                    MusicDataHelper.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new MusicDataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<MusicSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                MusicSuggestion colorSuggestion = (MusicSuggestion) searchSuggestion;
                MusicDataHelper.findColors(getActivity(), colorSuggestion.getBody(),
                        new MusicDataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<SearchProposal.Content.Songs> results) {
                                mSearchResultsAdapter.swapData(results, getActivity());
                            }

                        });
                if (colorSuggestion.getBody().contains("-")) {
                    String[] info = colorSuggestion.getBody().split("-");
                    Boolean flag = true;//判断歌曲是否存在 true 当前待播放歌曲不存在歌曲列表中，false反之
                    MusicListBase musicListBase = null;
                    List<MusicListBase.Musics> musicsList = null;

                    OnlineMusicList list = new OnlineMusicList(getActivity());
                    if (list.getList() != null) {
                        musicListBase = list.getList();
                        musicsList = list.getList().getMusics();
                    } else {
                        musicListBase = new MusicListBase();
                        musicsList = new ArrayList<>();
                    }

                    for (MusicListBase.Musics music : musicsList) {

                        if (music.getId().equals(info[2])) {
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        MusicListBase.Musics musics = musicListBase.new Musics();
                        musics.setSong_author(info[1]);
                        musics.setLrc_url("歌词地址");
                        musics.setCover_url("封面地址");
                        musics.setSong_paly_times(1);
                        musics.setSong_type("");
                        musics.setId(Integer.valueOf(info[2]));
                        musics.setSong_name(info[0]);
                        musicsList.add(musics);
                        musicListBase.setMusics(musicsList);
                        if (list.saveList(musicListBase)) {
                            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "歌曲已存在歌曲列表中", Toast.LENGTH_SHORT).show();
                    }
                }
                //Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;

                MusicDataHelper.findColors(getActivity(), query,
                        new MusicDataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<SearchProposal.Content.Songs> results) {
                                mSearchResultsAdapter.swapData(results, getActivity());
                            }

                        });
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(MusicDataHelper.getHistory(getActivity(), 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_change_colors) {

                    mIsDarkSearchTheme = true;

                    //demonstrate setting colors for items
                    mSearchView.setBackgroundColor(Color.parseColor("#787878"));
                    mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {

                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                MusicSuggestion colorSuggestion = (MusicSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));

                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
//                Log.i(mSearchView.getQuery(),colorSuggestion.getBody());
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
//                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {

                Log.d(TAG, "onClearSearchClicked()");
            }
        });

    }

    public Boolean onBackPressed() {
        if (!mSearchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListAdapter();
//        mSearchResultsList.setAdapter(mSearchResultsAdapter);
//        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("子提示","YES");
        mListener.onActivityResultCall(intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
