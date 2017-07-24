package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Geram IT Lab on 15/05/2017.
 */

public class UserRepository {
    private DatabaseHelper db;
    Dao<User, Integer> doctorsDao;
    private Context context;

    public UserRepository(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        this.context = context;
        try {
            doctorsDao = db.getUsersDao();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public Dao.CreateOrUpdateStatus create(User user) {
        try {
            Gson gson = new Gson();
            user.setJsonInfo(gson.toJson(user.getInfo()));
            user.setJson_members_at(gson.toJson(user.getMembers_at()));
            user.setJson_open_time(gson.toJson(user.getOpen_time()));
            user.setJson_specialities(gson.toJson(user.getSpecialities()));
            user.setJson_supported_lang(gson.toJson(user.getSupported_lang()));
            user.setJsonDocument(gson.toJson(user.getDocuments()));
            user.setJson_questions(gson.toJson(user.getQuestions()));
            user.setJson_rate_percentage(gson.toJson(user.getRate_percentage()));
            return doctorsDao.createOrUpdate(user);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_create_database), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public int update(User doctor) {
        try {
            return doctorsDao.update(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getChat(int type) {
        try {
            switch (type) {
                case User.DOCTOR_TYPE:
                    return doctorsDao.queryBuilder().orderByRaw("isOpen COLLATE NOCASE DESC ,last_msg_date COLLATE NOCASE DESC").where().eq("is_chat", 1).and().eq("isDoc", 1).query();
                case User.CLINICS_TYPE:
                    return doctorsDao.queryBuilder().orderByRaw("isOpen COLLATE NOCASE DESC ,last_msg_date COLLATE NOCASE DESC").where().eq("is_chat", 1).and().eq("isClinic", 1).query();
                case User.CLIENT_TYPE :
                    return doctorsDao.queryBuilder().orderByRaw("isOpen COLLATE NOCASE DESC ,last_msg_date COLLATE NOCASE DESC").where().eq("is_chat", 1).and().eq("isClinic", 0).and().eq("isDoc", 0).query();
                case User.DOCTOR_AND_CLINICS_TYPE:
                    return doctorsDao.queryBuilder().orderByRaw("isOpen COLLATE NOCASE DESC , isDoc COLLATE NOCASE DESC, isClinic COLLATE NOCASE DESC  ,last_msg_date COLLATE NOCASE DESC").where().eq("is_chat", 1).and().eq("isClinic", 1).or().eq("isDoc", 1).query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateColoumn(User doctor) {
        try {
            doctorsDao.delete(doctor);
            doctor.setJsonInfo(new Gson().toJson(doctor.getInfo()));
            doctorsDao.create(doctor);
            UpdateBuilder<User, Integer> updateBuilder = doctorsDao.updateBuilder();
// set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", doctor.get_Id());
            Gson gson = new Gson();
            String json = gson.toJson(doctor.getDocuments());
            updateBuilder.updateColumnValue("jsonDocument" /* column */, json/* value */);

            updateBuilder.update();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return 0;
    }


    public int insertDocument(User doctor) {
        UpdateBuilder<User, Integer> updateBuilder = doctorsDao.updateBuilder();
        try {
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", doctor.get_Id());
            String json = new Gson().toJson(doctor.getDocuments());
// update the value of your field(s)
            updateBuilder.updateColumnValue("jsonDocument" /* column */, json/* value */);
            updateBuilder.update();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return 0;

    }

    public int delete(User doctor) {
        try {
            return doctorsDao.delete(doctor);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return 0;
    }

    public void deletetype(int type) {
        try {
            if (type == 2)
                doctorsDao.delete(doctorsDao.queryForEq("isDoc", 1));
            else if (type == 3)
                doctorsDao.delete(doctorsDao.queryForEq("isClinic", 1));
            else {
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void deleteAll() {
        try {
            DeleteBuilder<User, Integer> db = doctorsDao.deleteBuilder();
            db.delete();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }


    public double count() {
        try {
            return doctorsDao.countOf();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return 0;
    }

    public List<User> getAll(int type) {
        try {
            List<User> userList;
            switch (type) {
                case User.DOCTOR_TYPE:
                    userList = doctorsDao.queryBuilder().orderByRaw("first_name COLLATE NOCASE ASC").where().eq("isDoc", 1).query();
                    break;
                case User.CLINICS_TYPE:
                    userList = doctorsDao.queryBuilder().orderByRaw("first_name COLLATE NOCASE ASC").where().eq("isClinic", 1).query();
                    break;
                case User.CLIENT_TYPE:
                    ////////////////start handle from here the query of the user
                    /// handle full scenario of the database from start of the app and the progress dialoge
                    userList = doctorsDao.queryBuilder().orderByRaw("first_name COLLATE NOCASE ASC ,first_name COLLATE NOCASE ASC").where().eq("isDoc", 0).and().eq("isClinic", 0).query();
                    break;
                case User.DOCTOR_AND_CLINICS_TYPE:
                    userList = doctorsDao.queryBuilder().orderByRaw("first_name COLLATE NOCASE ASC").where().eq("isDoc", 1).or().eq("isClinic", 1).query();
                    break;
                default:
                    userList = doctorsDao.queryBuilder().orderByRaw("first_name COLLATE NOCASE ASC").where().eq("isDoc", 1).query();
                    break;
            }
            setJsonData(userList);
            return userList;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_getting_database), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void setJsonData(List<User> userList) {
        Gson gson = new Gson();
        for (User user : userList
                ) {
            user.setInfo(gson.fromJson(user.getJsonInfo(), Info.class));
            user.setMembers_at((List<ChooseModel>) gson.fromJson(user.getJson_members_at(), new TypeToken<List<ChooseModel>>() {
            }.getType()));
            user.setSupported_lang((List<ChooseModel>) gson.fromJson(user.getJson_supported_lang(), new TypeToken<List<ChooseModel>>() {
            }.getType()));
            user.setSpecialities((List<ChooseModel>) gson.fromJson(user.getJson_specialities(), new TypeToken<List<ChooseModel>>() {
            }.getType()));
            user.setOpen_time((List<Table>) gson.fromJson(user.getJson_open_time(), new TypeToken<List<Table>>() {
            }.getType()));
            user.setDocuments((ArrayList<Message>) gson.fromJson(user.getJsonDocument(), new TypeToken<ArrayList<Message>>() {
            }.getType()));
            user.setQuestions((LinkedHashMap<String, String>) gson.fromJson(user.getJson_questions(), new TypeToken<HashMap<String, String>>() {
            }.getType()));
            user.setRate_percentage((HashMap<String, String>) gson.fromJson(user.getJson_rate_percentage(), new TypeToken<HashMap<String, String>>() {
            }.getType()));
        }
    }

    public User getDoctor(User doctor) {
        try {

            QueryBuilder<User, Integer> queryBuilder =
                    doctorsDao.queryBuilder();
// the 'password' field must be equal to "qwerty"
            queryBuilder.where().eq("id", doctor.get_Id());
// prepare our sql statement
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
// query for all accounts that have "qwerty" as a password
            List<User> accountList = doctorsDao.query(preparedQuery);

            if (accountList.size() == 1)
                return accountList.get(0);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_getting_database), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public boolean isExist(User doctor) {
        try {
            return doctorsDao.idExists(doctor.get_Id());
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return false;
    }

    public ArrayList<Message> getDocument(User doctor) {
        QueryBuilder<User, Integer> queryBuilder = doctorsDao.queryBuilder();
        try {
            queryBuilder.where().eq("id", doctor.get_Id());
            List<User> doctorsList = queryBuilder.query();
            if (doctorsList.size() == 1)
                return doctorsList.get(0).getDocuments();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
}
