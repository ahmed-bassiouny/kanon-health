package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;

import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geram IT Lab on 15/05/2017.
 */

public class UserRepository {
    private DatabaseHelper db;
    Dao<User, Integer> doctorsDao;
    public UserRepository(Context context){
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        try {
            doctorsDao = db.getUsersDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(User doctor){
        try {
            doctor.setJsonInfo(new Gson().toJson(doctor.getInfo()));
            return doctorsDao.create(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(User doctor){
        try {
            return doctorsDao.update(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int updateColoumn(User doctor){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int insertDocument(User doctor){
        UpdateBuilder<User, Integer> updateBuilder = doctorsDao.updateBuilder();
        try {
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", doctor.get_Id());
            String json = new Gson().toJson(doctor.getDocuments());
// update the value of your field(s)
            updateBuilder.updateColumnValue("jsonDocument" /* column */, json/* value */);
            updateBuilder.update();
        }
        catch (Exception e){

        }
        return 0;

    }
    public int delete(User doctor){
        try {
            return doctorsDao.delete(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void deleteAll(){
        try {
            DeleteBuilder<User, Integer> db = doctorsDao.deleteBuilder();
            db.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public double count(){
        try {
            return doctorsDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getAll(){
        try {
            return doctorsDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public User getDoctor(User doctor){
        try {

            QueryBuilder<User, Integer> queryBuilder =
                    doctorsDao.queryBuilder();
// the 'password' field must be equal to "qwerty"
            queryBuilder.where().eq("id", doctor.get_Id());
// prepare our sql statement
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
// query for all accounts that have "qwerty" as a password
            List<User> accountList = doctorsDao.query(preparedQuery);
            if(accountList.size() == 1)
                return accountList.get(0);
        }
        catch (Exception e){
            Log.d("Exceptoion", e.toString());
        }
        return null ;
    }
    public boolean isExist(User doctor){
        try {
            return doctorsDao.idExists(doctor.get_Id());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false ;
    }
    public ArrayList<Message> getDocument(User doctor){
        QueryBuilder<User, Integer> queryBuilder = doctorsDao.queryBuilder();
        try {
            queryBuilder.where().eq("id", doctor.get_Id());
            List<User> doctorsList = queryBuilder.query();
            if(doctorsList.size() == 1)
                return doctorsList.get(0).getDocuments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
