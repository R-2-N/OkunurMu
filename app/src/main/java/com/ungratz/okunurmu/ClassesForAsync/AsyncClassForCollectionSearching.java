package com.ungratz.okunurmu.ClassesForAsync;

import androidx.fragment.app.Fragment;

import com.ungratz.okunurmu.fragments.MeetingsFragment;
import com.ungratz.okunurmu.fragments.SearchFragment;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

public class AsyncClassForCollectionSearching implements Runnable{
    private Fragment fragment;
    private SearchParameters sp;
    private int iterationNo;
    private SearchResult sr;

    public AsyncClassForCollectionSearching(Fragment fragment, SearchParameters sp, int iterationNo){
        super();
        this.fragment = fragment;
        this.sp = sp;
        this.iterationNo = iterationNo;
    }
    @Override
    public void run() {

        if (fragment.getClass() == SearchFragment.class){
            try {
                sr = CurrentUser.getTypensenseClient().collections("users").documents().search(sp);
                ((SearchFragment)fragment).createPreviews(sr, iterationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else{
            try {
                sr = CurrentUser.getTypensenseClient().collections("meetings").documents().search(sp);
                ((MeetingsFragment)fragment).createPreviews(sr, iterationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
