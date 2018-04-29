package cherry.ethereal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cherry.ethereal.adapter.MusicListAdapter;
import cherry.ethereal.data.MusicList.MusicListBase;
import cherry.ethereal.data.MusicList.OnlineMusicList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link MusicListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button musicBackBtn;
    private ListView mlistView;
    private MusicListAdapter musicListAdapter;
    private OnlineMusicList onlineMusicList;
    private OnMusicListFragmentInteractionListener mListener;

    public MusicListFragment() {
        // Required empty public constructor
    }

    public interface OnMusicListFragmentInteractionListener {
        // TODO: Update argument type and name
        void readyPlay(Integer ID,Integer position);
        void setTilteAndAuthor(String Name,String Author);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MusicListFragment.OnMusicListFragmentInteractionListener) {
            mListener = (MusicListFragment.OnMusicListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicListFragment newInstance(String param1, String param2) {
        MusicListFragment fragment = new MusicListFragment();
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
        View view = inflater.inflate(R.layout.music_list, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        musicBackBtn = (Button) view.findViewById(R.id.music_back_btn);
        mlistView = (ListView) view.findViewById(R.id.music_list_view);
        onlineMusicList = new OnlineMusicList(getActivity());
        final MusicListBase musicListBase = onlineMusicList.getList();
        if(musicListBase!=null) {

            musicListAdapter = new MusicListAdapter(musicListBase.getMusics(), getContext());
            mlistView.setAdapter(musicListAdapter);
            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.i("点击位置", String.valueOf(position));
                    mListener.readyPlay(musicListBase.getMusics().get(position).getId(), position);
                    mListener.setTilteAndAuthor(musicListBase.getMusics().get(position).getSong_name(), musicListBase.getMusics().get(position).getSong_author());
                    showOrHideWindow();

                }
            });
        }
        onBackClickEvent();
        super.onViewCreated(view, savedInstanceState);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

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
    public void showOrHideWindow() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (this.isVisible()) {
            fragmentTransaction.hide(this);
        } else {
            fragmentTransaction.show(this);
            musicListAdapter=null;

            onlineMusicList = new OnlineMusicList(getActivity());
            final MusicListBase musicListBase = onlineMusicList.getList();
            if(musicListBase!=null) {
                musicListAdapter = new MusicListAdapter(musicListBase.getMusics(), getContext());
                mlistView.setAdapter(musicListAdapter);
            }
        }
        fragmentTransaction.commit();
    }

    public void onBackClickEvent() {
        musicBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showOrHideWindow();
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.beginTransaction().remove(MusicListFragment.this).addToBackStack(null).commit();
            }
        });

    }


}
