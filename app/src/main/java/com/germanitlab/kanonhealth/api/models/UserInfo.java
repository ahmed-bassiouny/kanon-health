package com.germanitlab.kanonhealth.api.models;

import android.text.TextUtils;

import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.germanitlab.kanonhealth.models.Table;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by bassiouny on 02/08/17.
 */

@DatabaseTable
public class UserInfo extends ParentModel {

    //region key
    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDAY = "birh_day";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_DOCTOR_ID = "doctor_id";
    public static final String KEY_DOCUMENTS = "documents";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";
    public static final String KEY_IS_AVAILABLE = "is_available";
    public static final String KEY_ACTIVATED = "activated";
    public static final String KEY_SPECIALITIES = "specialitys";
    public static final String Clinics = "member_at";
    public static final String KEY_MY_CLINICS = "my_clinic";
    public static final String KEY_STREET_NAME = "street_name";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_PROVIDENCE = "providence";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_QUESTIONS_ANSWERS = "questions_answers";
    public static final String KEY_TIME_TABLE= "working_hours";
    public static final String KEY_ISOPEN="is_open";
    public static final String KEY_OPEN_TYPE = "open_type";
    public static final String KEY_REQUEST_ID="request_id";

    public static final String KEY_ID = "clinic_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_COUNTRY = "county";
    public static final String KEY_FAX = "fax";

    // endregion

    // region constraints
    public static final int CLINIC = 3;
    public static final int DOCTOR = 2;
    public static final int PATIENT = 1;
    // endregion

    // region Attributes

    @DatabaseField(generatedId = true)
    private int idLocalDatabase; // it's for local database not use in anything
    @SerializedName(KEY_USERID)
    @DatabaseField()
    private Integer userID;
    @SerializedName(KEY_DOCTOR_ID)
    @DatabaseField
    private Integer doctorID;
    // usertype => 1 patient , 2 doctor
    @SerializedName(KEY_USERTYPE)
    @DatabaseField
    private Integer userType;
    @SerializedName(KEY_FIRSTNAME)
    @DatabaseField
    private String firstName = "";
    @SerializedName(KEY_LASTNAME)
    @DatabaseField
    private String lastName = "";
    @SerializedName(KEY_TITLE)
    @DatabaseField
    private String title = "";
    @SerializedName(KEY_PASSWORD)
    @DatabaseField
    private String password;
    @SerializedName(KEY_COUNTRY_CODE)
    @DatabaseField
    private String country_code;
    @SerializedName(KEY_PHONE)
    @DatabaseField
    private String phone;
    @SerializedName(KEY_GENDER)
    @DatabaseField
    private String gender;
    @SerializedName(KEY_BIRTHDAY)
    @DatabaseField
    private String birthday;
    @SerializedName(KEY_AVATAR)
    @DatabaseField
    private String avatar;
    @SerializedName(KEY_IS_AVAILABLE)
    @DatabaseField
    private Integer isAvailable;
    @SerializedName(KEY_DOCUMENTS)
    private ArrayList<Document> documents;
    @SerializedName(KEY_EMAIL)
    @DatabaseField
    private String email;
    @SerializedName(KEY_RATE_NUM)
    @DatabaseField
    private Float rateNum;
    @SerializedName(KEY_RATE_PERCENTAGE)
    private HashMap<String, Integer> ratePercentage;
    @SerializedName(KEY_STREET_NAME)
    @DatabaseField
    private String streetName;
    @SerializedName(KEY_HOUSE_NUMBER)
    @DatabaseField
    private String houseNumber;
    @SerializedName(KEY_PROVIDENCE)
    @DatabaseField
    private String providence;
    @SerializedName(KEY_ZIP_CODE)
    @DatabaseField
    private String zipCode;
    @SerializedName(KEY_SUPPORTED_LANG)
    private ArrayList<Language> supportedLangs;
    @SerializedName(KEY_ACTIVATED)
    @DatabaseField
    private Integer isActive;
    @SerializedName(KEY_SPECIALITIES)
    private ArrayList<Speciality> specialities;
    @SerializedName(Clinics)
    private ArrayList<UserInfo> clinics;
    @SerializedName(KEY_MY_CLINICS)
    private ArrayList<UserInfo> myClinics;
    @SerializedName(KEY_TIME_TABLE)
    private String timeTable;
    @SerializedName(KEY_ISOPEN)
    @DatabaseField
    private int isSessionOpen;
    @SerializedName(KEY_REQUEST_ID)
    @DatabaseField
    private int requestID;


    private LinkedHashMap<String, String> questionsAnswers;
    // i need key name from backend
    @SerializedName(KEY_OPEN_TYPE)
    @DatabaseField
    private Integer openType;
    @DatabaseField
    private Integer isMyDoc;
    @DatabaseField
    private int haveRate;


    // extra for clinic
    @SerializedName(KEY_ID)
    @DatabaseField()
    private Integer id;
    @SerializedName(KEY_NAME)
    @DatabaseField()
    private String name;
    @SerializedName(KEY_ADDRESS)
    @DatabaseField()
    private String address;
    @SerializedName(KEY_CITY)
    @DatabaseField()
    private String city;
    @SerializedName(KEY_PROVINCE)
    @DatabaseField()
    private String province;
    @SerializedName(KEY_COUNTRY)
    @DatabaseField()
    private String country;
    @SerializedName(KEY_FAX)
    @DatabaseField()
    private String fax;


    // i need key name from backend
    private Double locationLat;
    private Double locationLong;


    // endregion


    // region setter and getter
    public Integer getUserID() {
        if (userID == null)
            userID = 0;
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserType() {
        if (userType == null)
            userType = PATIENT;
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        String displayString = "";
        Date bd = new Date();
        if (!TextUtils.isEmpty(birthday)) {
            bd = DateHelper.FromServerDateStringToServer(birthday);
        }
        displayString = DateHelper.FromDisplayDateToBirthDateString(bd);
        return displayString;
    }

    public void setBirthday(String birthday) {
        this.birthday = DateHelper.FromDisplayDateToServerString(DateHelper.FromBirthDateStringToDisplay(birthday));
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAvailable() {
        if (isAvailable == null)
            isAvailable = 0;
        return isAvailable;
    }

    public void setAvailable(Integer available) {
        isAvailable = available;
    }


    public ArrayList<Language> getSupportedLangs() {
        if(supportedLangs==null)
            supportedLangs=new ArrayList<>();
        return supportedLangs;
    }

    public void setSupportedLangs(ArrayList<Language> supportedLangs) {
        this.supportedLangs = supportedLangs;
    }

    public static String getKeyUserid() {
        return KEY_USERID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    public ArrayList<UserInfo> getClinics() {
        if (clinics == null) {
            clinics = new ArrayList<>();
        }
        return clinics;
    }

    public void setClinics(ArrayList<UserInfo> clinics) {
        this.clinics = clinics;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getRateNum() {
        return rateNum;
    }

    public void setRateNum(Float rateNum) {
        this.rateNum = rateNum;
    }

    public HashMap<String, Integer> getRatePercentage() {
        return ratePercentage;
    }

    public void setRatePercentage(HashMap<String, Integer> ratePercentage) {
        this.ratePercentage = ratePercentage;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public ArrayList<Speciality> getSpecialities() {
        if (specialities == null) {
            specialities = new ArrayList<>();
        }
        return specialities;
    }

    public void setSpecialities(ArrayList<Speciality> specialities) {
        this.specialities = specialities;
    }

    public String getFullName() {
        String fullName = "";
        if (!TextUtils.isEmpty(title)) {
            fullName += title;
        }
        if (!TextUtils.isEmpty(firstName)) {
            fullName += " " + firstName;
        }
        if (!TextUtils.isEmpty(lastName)) {
            fullName += " " + lastName;
        }
        return fullName.trim();
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getProvidence() {
        return providence;
    }

    public void setProvidence(String providence) {
        this.providence = providence;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LinkedHashMap<String, String> getQuestionsAnswers() {
        if (questionsAnswers == null) {
            questionsAnswers = new LinkedHashMap<>();
        }
        return questionsAnswers;
    }

    public void setQuestionsAnswers(LinkedHashMap<String, String> questionsAnswers) {
        this.questionsAnswers = questionsAnswers;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public ArrayList<WorkingHours> getTimeTable() {
        Gson gson = new Gson();
        ArrayList<WorkingHours> workingHourses = gson.fromJson(timeTable, new TypeToken<List<WorkingHours>>(){}.getType());
        if (workingHourses == null) {
            workingHourses = new ArrayList<>();
        }
        return workingHourses;
    }

    public void setTimeTable(ArrayList<WorkingHours> timeTable) {
        this.timeTable = new Gson().toJson(timeTable);
    }


    public Integer getIsMyDoc() {
        if (isMyDoc == null) {
            isMyDoc = 0;
        }
        return isMyDoc;
    }

    public void setIsMyDoc(Integer isMyDoc) {
        this.isMyDoc = isMyDoc;
    }

    public ArrayList<UserInfo> getMyClinics() {
        if (myClinics == null) {
            myClinics = new ArrayList<>();
        }
        return myClinics;
    }

    public void setMyClinics(ArrayList<UserInfo> myClinics) {
        this.myClinics = myClinics;
    }


    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getHaveRate() {
        return haveRate;
    }

    public void setHaveRate(int haveRate) {
        this.haveRate = haveRate;
    }

    public int getIsSessionOpen() {
        return isSessionOpen;
    }

    public void setIsSessionOpen(int isSessionOpen) {
        this.isSessionOpen = isSessionOpen;
    }

    public Integer getId() {
        if(id==null)
            id=0;
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Double getLocationLat() {
        if(locationLat==null)
            locationLat=0.0;
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        if(locationLong==null)
            locationLong=0.0;
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }

    public int getIdLocalDatabase() {
        return idLocalDatabase;
    }

    public void setIdLocalDatabase(int idLocalDatabase) {
        this.idLocalDatabase = idLocalDatabase;
    }
    //endregion
}
