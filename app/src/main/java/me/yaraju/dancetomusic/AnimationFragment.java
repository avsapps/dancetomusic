package me.yaraju.dancetomusic;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnimationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnimationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimationFragment extends Fragment implements SoundChangeListener {

    private static final String ARG_RES_ID = "resId";
    private OnFragmentInteractionListener mListener;
    private GifDrawable animation = null;


    private int resId = R.drawable.lemon_dance;
    private TextView mStatusView;



    public AnimationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resId Resource ID of animation drawable.
     * @return A new instance of fragment AnimationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnimationFragment newInstance(int resId) {
        AnimationFragment fragment = new AnimationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RES_ID, resId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resId = getArguments().getInt(ARG_RES_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_animation, container, false);

        // Inflate the layout for this fragment
        ImageView imageView = rootView.findViewById(R.id.animationImageView);
        mStatusView = rootView.findViewById(R.id.status);
        Glide.with(this).load(resId).into(new DrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (resource instanceof GifDrawable) {
                    animation = (GifDrawable) resource;
                    animation.stop();
                }
            }
        });
//        animation = (GifDrawable) imageView.getDrawable();
//        animation.stop();
        // Used to record voice

        Activity thisActivity = getActivity();
        if (thisActivity == null) return rootView;
        // Here, thisActivity is the current activity
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public boolean isReady() {
        boolean visible = isVisible();
        boolean detached = isDetached();
        return visible && !detached;
    }

    @Override
    public void onSoundStarted() {
        mStatusView.setText(R.string.sound_present);
        if (animation != null) animation.start();
        getView().setBackgroundColor(getResources().getColor(R.color.noisy_background_color));

    }

    @Override
    public void onSoundStopped() {
        if (animation != null) animation.stop();
        mStatusView.setText(R.string.sound_absent);
        getView().setBackgroundColor(getResources().getColor(R.color.quiet_background_color));

    }

    @Override
    public int getListenerId() {
        return getArguments().getInt(ARG_RES_ID);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
