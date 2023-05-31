package com.ungratz.okunurmu.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SearchFragment extends Fragment {
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
                createProfilePreviews(sr, iterationNo);
        });

        binding.searchButton.setOnClickListener(v -> {
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
        });


        /*
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.profile_preview, binding.linearLayoutForSearch, false);
        TextView t = cl.findViewById(R.id.mentorNameForSearch);
        t.setText("Amogus");

        binding.linearLayoutForSearch.addView(cl);

        ConstraintLayout cl2 = (ConstraintLayout) getLayoutInflater().inflate(R.layout.profile_preview, binding.linearLayoutForSearch, false);
        TextView t2 = cl2.findViewById(R.id.mentorNameForSearch);
        t2.setText("amogus2electricboogaloo");

        View v = cl2.findViewById(R.id.sendMessageToTutorView);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t12 = cl2.findViewById(R.id.sendMeetingRequestToTutorText);
                t12.setText("amongusboogaloo");
            }
        });

        binding.linearLayoutForSearch.addView(cl2);
        */
    }

    // CurrentUser.getTypensenseClient().collections("users").documents().search(sp);
    // Method on top requires

    private int iterationNo = 1;
    public void searchByFilter(String filter){

        if (filter.equals("")){filter = "realName, university, department";}

        sp = new SearchParameters()
                .q(binding.searchText.getText().toString())
                .queryBy(filter)
                .filterBy("isMentor: true")
                .prioritizeExactMatch(true);

        //asyncClassForSearchResult acfsr = new asyncClassForSearchResult();
        executor.execute(new AsyncClassForCollectionSearching(this, sp, iterationNo));
    }

    private int index = 0;
    public void createProfilePreviews(SearchResult sr, int iterationNo){

        if ((sr.getHits().size() == 0) || (sr == null)){
            //something to show that no matches were found
        }

        else{

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
        View sendMeetingRequestView = pp.findViewById(R.id.sendMeetingRequestToTutorText);



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


                                iv.setOnClickListener(v -> ((MainActivity)SearchFragment.this.getActivity()).switchToTutorProfile(idOfMentorForSearch));

                                binding.linearLayoutForSearch.addView(pp);
                            });
                });
    }

    public void sendMeetingRequestToTutor(String idOfTutor){

    }

}
