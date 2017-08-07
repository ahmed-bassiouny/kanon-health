package com.germanitlab.kanonhealth.callback;

import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;

import java.util.ArrayList;

/**
 * Created by ahmed on 10/06/17.
 */


public interface Message {
    void returnChoseSpecialityList(ArrayList<Speciality> specialitiesArrayList);
    void returnChoseLanguageList(ArrayList<Language> languageArrayList);
    void returnChoseDoctorList(ArrayList<UserInfo> doctorArrayList);
}
