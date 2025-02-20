package com.monaum.money.restApi;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.monaum.money.entity.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIRequestDao {
    String baseUrl ="http://192.168.20.135:3000/student";

    public interface ApiCallback {
        void onSuccess(List<Student> students);
        void onError(String errorMessage);
    }

    // Method to fetch the list of students asynchronously
    public void getList(Context con, final ApiCallback callback) {
        List<Student> studentList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(con);

        // StringRequest to make the API request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response and populate the student list
                            JSONArray ja = new JSONArray(response);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jb = ja.getJSONObject(i);
                                Student student = new Student(
                                        jb.getString("id"),
                                        jb.getString("name"),
                                        jb.getString("username"),
                                        jb.getString("password"));
                                studentList.add(student);
                            }

                            // Notify the callback with the list of students
                            callback.onSuccess(studentList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Failed to parse JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Notify the callback with the error message
                callback.onError("Error: " + error.getLocalizedMessage());
            }
        });

        // Add the request to the Volley queue
        queue.add(stringRequest);
    }



    public void addStudent(Context con, Student std){

        RequestQueue queue = Volley.newRequestQueue(con);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Response: --"+response);
                        Toast.makeText(con, "Massage: "+ response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("error--"+ error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", std.getUsername());
                paramV.put("name", std.getName());
                paramV.put("password", std.getPassword());
                return paramV;
            }
        };
        queue.add(stringRequest);


    }



    public void updateStudent(Context con, Student std){

        RequestQueue queue = Volley.newRequestQueue(con);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Response: --"+response);
                        Toast.makeText(con, "Massage: "+ response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("error--"+ error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("id", std.getId().toString());
                paramV.put("username", std.getUsername());
                paramV.put("name", std.getName());
                paramV.put("password", std.getPassword());
                return paramV;
            }
        };
        queue.add(stringRequest);


    }


    public void deleteStudent(Context con, Integer id){


        RequestQueue queue = Volley.newRequestQueue(con);
        String url ="http://192.168.20.46:3000/users/"+ id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Response: --"+response);
                        Toast.makeText(con, "Massage: "+ response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("error--"+ error.getLocalizedMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                return paramV;
            }
        };
        queue.add(stringRequest);


    }
}