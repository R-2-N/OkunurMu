package com.ungratz.okunurmu;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ungratz.okunurmu.databinding.ProfilePreviewBinding;

public class ProfilePreview extends ConstraintLayout{

    private ProfilePreviewBinding binding;
    private ConstraintLayout cl;

    private String mentorId;
    private String studentId;
    private Bitmap bm;

    public ProfilePreview(Context context, String idOfMentor, String idOfStudent) {
        super(context);
        mentorId = idOfMentor;
        studentId = idOfStudent;
    }

    public void setMentorId(String m){mentorId = m;}
    public void setStudentId(String s){studentId = s;}

    public String getMentorId(){return mentorId;}
    public String getStudentId(){return studentId;}

}
