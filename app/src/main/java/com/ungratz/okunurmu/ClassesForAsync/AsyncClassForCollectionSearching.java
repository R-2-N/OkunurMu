package com.ungratz.okunurmu.ClassesForAsync;

import com.ungratz.okunurmu.fragments.SearchFragment;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

public class AsyncClassForCollectionSearching implements Runnable{
    private SearchFragment fragment;
    private SearchParameters sp;
    private int iterationNo;
    private SearchResult sr;

    public AsyncClassForCollectionSearching(SearchFragment fragment, SearchParameters sp, int iterationNo){
        super();
        this.fragment = fragment;
        this.sp = sp;
        this.iterationNo = iterationNo;
    }
    @Override
    public void run() {
        try {
            sr = CurrentUser.getTypensenseClient().collections("users").documents().search(sp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        fragment.createProfilePreviews(sr, iterationNo);
    }
}
