package com.ungratz.okunurmu.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.ungratz.okunurmu.ClassesForAsync.AsyncClassForCollectionSearching;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentMeetingsBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingsFragment extends Fragment {

    private AsyncClassForCollectionSearching async;
    private FragmentMeetingsBinding binding;
    private SearchParameters sp;
    private SearchResult sResult;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final int MAX_DOWNLOAD_SIZE = 2048*2048;
    private Bitmap bm;
    public MeetingsFragment(){
        super(R.layout.fragment_search);
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMeetingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private int iterationNo = 1;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){


        String filter = "studentID, mentorID";

        sp = new SearchParameters()
                .q(CurrentUser.getID())
                .queryBy(filter)
                .sortBy("dateAndTime:asc");

        async = new AsyncClassForCollectionSearching(this, sp, 0);
        executor.execute(async);


        binding.showMoreMeetings.setOnClickListener(v -> createPreviews(sResult, iterationNo));
    }

    private int index = 0;
    public void createPreviews(SearchResult sr, int iterationNo){

        sResult = sr;

        if ((sResult.getHits().size() == 0) || (sResult == null)){
            binding.showMoreMeetings.setText("There are no meetings");
        }

        else{

            if((sResult.getHits().size()-index) < 5){
                while(index<sResult.getHits().size()){
                    addMeetingPreview(sResult.getHits().get(index).getDocument());
                    index++;
                }
            }

            else{
                while (index<iterationNo*5){
                    addMeetingPreview(sResult.getHits().get(index).getDocument());
                    index++;
                }
                iterationNo++;
            }
        }
    }

    public void addMeetingPreview(Map<String, Object> document){

        ConstraintLayout box = (ConstraintLayout) getLayoutInflater().inflate(R.layout.meeting_boxes, binding.linearLayoutForMeetingBoxes, false);

        ImageView iv = box.findViewById(R.id.meetingPP);
        TextView actualDate = box.findViewById(R.id.actualDate);
        TextView uniAndDepartmentText = box.findViewById(R.id.uniAndDepartmentForSearch);

        String idToDownloadFrom;
        if (CurrentUser.getIsMentor()) {idToDownloadFrom = document.get("studentID").toString();}
        else {idToDownloadFrom = document.get("mentorID").toString();}

        CurrentUser.getStorageRef().child(idToDownloadFrom+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        iv.setImageBitmap(bm);

                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MainActivity)MeetingsFragment.this.getActivity()).switchToProfileExamine(idToDownloadFrom);
                            }
                        });
                        binding.linearLayoutForMeetingBoxes.addView(box);
                    }
                });
    }
}
