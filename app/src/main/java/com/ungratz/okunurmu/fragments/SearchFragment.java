package com.ungratz.okunurmu.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ungratz.okunurmu.ClassesForAsync.AsyncClassForCollectionSearching;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentSearchBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SearchFragment extends Fragment {
    private AsyncClassForCollectionSearching async;
    private FragmentSearchBinding binding;
    private SearchParameters sp;
    private SearchResult sr;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String[] filterOptions = {"None", "Name", "University", "Department"};

    private final int MAX_DOWNLOAD_SIZE = 2048*2048;
    private Bitmap bm;

    public SearchFragment(){
        super(R.layout.fragment_search);
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filterOptions);
        binding.searchSpinner.setAdapter(filterAdapter);
        binding.searchSpinner.setPrompt("Set Filter");

        binding.showMoreResults.setOnClickListener(v -> {
            if (sr != null)
                createPreviews(sr, iterationNo);
        });

        binding.searchButton.setOnClickListener(v -> {

            SearchFragment.this.searchFunction();
        });

    }

    public void searchFunction(){

        binding.linearLayoutForSearch.removeAllViews();
        binding.showMoreResults.setVisibility(View.VISIBLE);
        binding.showMoreResults.setClickable(true);
        index = 0;
        iterationNo = 1;
        switch (binding.searchSpinner.getSelectedItem().toString()){
            case "Name":
                searchByFilter("realName");
                break;

            case "University":
                searchByFilter("university");
                break;

            case "Department":
                searchByFilter("department");
                break;

            default:
                searchByFilter("");
                break;
        }
    }

    private int iterationNo = 1;
    public void searchByFilter(String filter){

        if (filter.equals("")){filter = "realName, university, department";}

        sp = new SearchParameters()
                .q(binding.searchText.getText().toString())
                .queryBy(filter)
                .filterBy("isMentor: true")
                .prioritizeExactMatch(true);

        //asyncClassForSearchResult acfsr = new asyncClassForSearchResult();
        async = new AsyncClassForCollectionSearching(this, sp, iterationNo);
        executor.execute(async);
    }

    private int index = 0;
    public void createPreviews(SearchResult sr, int iterationNo){

        if ((sr.getHits().size() == 0) || (sr == null)){
            binding.showMoreResults.setText("Could not find any results");
        }

        else{
            binding.searchButton.setClickable(false);
            if((sr.getHits().size()-index) < 5){
                while(index<sr.getHits().size()){
                    addProfilePreview(sr.getHits().get(index).getDocument());
                    index++;
                }
                binding.showMoreResults.setVisibility(View.INVISIBLE);
                binding.showMoreResults.setClickable(false);
            }

            else{
                while (index<iterationNo*5){
                    addProfilePreview(sr.getHits().get(index).getDocument());
                    index++;
                }
                iterationNo++;
            }
        }
    }

    public void addProfilePreview(Map<String, Object> document){

        String idOfMentorForSearch = document.get("id").toString();

        ConstraintLayout pp = (ConstraintLayout) getLayoutInflater().inflate(R.layout.profile_preview, binding.linearLayoutForSearch, false);

        ImageView iv = pp.findViewById(R.id.mentorPpForSearch);
        TextView mentorNameText = pp.findViewById(R.id.mentorNameForSearch);
        TextView uniAndDepartmentText = pp.findViewById(R.id.uniAndDepartmentForSearch);
        View sendMessageView = pp.findViewById(R.id.sendMessageToTutorView);
        View sendMeetingRequestView = pp.findViewById(R.id.sendMeetingRequestToTutorView);


        CurrentUser.getStorageRef().child(idOfMentorForSearch+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {

                    bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    iv.setImageBitmap(bm);

                    CurrentUser.getUsersCollectionReference().document(idOfMentorForSearch).get()
                            .addOnCompleteListener(task -> {

                                mentorNameText.setText(document.get("realName").toString());
                                uniAndDepartmentText.setText((document.get("university").toString()) + " - " + document.get("department").toString());

                                sendMessageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Stuff for when we get the messaging function
                                    }
                                });

                                sendMeetingRequestView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Stuff for when we get this function which is actually my job hahahahaha
                                        sendMeetingRequestToTutor(idOfMentorForSearch);
                                    }
                                });


                                iv.setOnClickListener(v -> ((MainActivity)SearchFragment.this.getActivity()).switchToProfileExamine(idOfMentorForSearch));

                                binding.linearLayoutForSearch.addView(pp);

                                binding.searchButton.setClickable(true);
                                binding.searchButton.setOnClickListener(v -> SearchFragment.this.searchFunction());
                            })
                            .addOnFailureListener(e -> {
                                binding.searchButton.setClickable(true);
                                binding.searchButton.setOnClickListener(v -> SearchFragment.this.searchFunction());
                            });
                })
                .addOnFailureListener(e -> binding.searchButton.setOnClickListener(v -> {
                    binding.searchButton.setClickable(true);
                    binding.searchButton.setOnClickListener(v1 -> SearchFragment.this.searchFunction());
                }));
    }

    public void sendMeetingRequestToTutor(String idOfTutor){

        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.meeting_pop_up);
        d.show();
        d.findViewById(R.id.exitForMeetingArrangement).setOnClickListener(v -> d.dismiss());

        d.findViewById(R.id.sendRequestForMeetingArrangement).setOnClickListener(v -> {

            EditText day = d.findViewById(R.id.meetingDay);
            EditText month = d.findViewById(R.id.meetingMonth);
            EditText year = d.findViewById(R.id.meetingYear);
            EditText hour = d.findViewById(R.id.meetingHour);
            EditText minute = d.findViewById(R.id.meetingMinute);

            Map<String, Object> meetingFields = new HashMap<>();
            meetingFields.put("studentID", CurrentUser.getID());
            meetingFields.put("mentorID", idOfTutor);
            meetingFields.put("day", day.getText().toString());
            meetingFields.put("month", month.getText().toString());
            meetingFields.put("year", year.getText().toString());
            meetingFields.put("hour", hour.getText().toString());
            meetingFields.put("minute", minute.getText().toString());
            meetingFields.put("dateAndTime", ("20"+year+"-"+month+"-"+day+" "+hour+":"+minute));

            CurrentUser.getFirebaseFirestore().collection("meetings")
                    .add(meetingFields);
        });

    }

}
