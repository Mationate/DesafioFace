package com.desafiolatam.desafioface.network;

import android.os.AsyncTask;
import android.util.Log;

import com.desafiolatam.desafioface.models.Developer;
import com.desafiolatam.desafioface.network.users.UserInterceptor;
import com.desafiolatam.desafioface.network.users.Users;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class GetUsers extends AsyncTask<Map<String,String>,Integer, Integer> {

    private int aditionalPages;
    private Map<String,String> queryMap;
    private int result;
    private final Users request = new UserInterceptor().get();

    public GetUsers(int aditionalPages) {
        this.aditionalPages = aditionalPages;
    }

    @Override
    protected Integer doInBackground(Map<String, String>... maps) {
        queryMap = maps[0];
        if (aditionalPages < 0){
            while (200 == connect()){
                increasePages();
            }
        } else {
            while (aditionalPages >= 0){
                aditionalPages--;
                connect();
                increasePages();
            }
        }




        return null;
    }


    private void increasePages(){

        int page = Integer.parseInt(queryMap.get("page"));
        page++;
        queryMap.put("page", String.valueOf(page));

    }

    private int connect(){
        int code = 666;
        Call<Developer[]> call = request.get(queryMap);
        try {
            Response<Developer[]> response = call.execute();

            code = response.code();
            if (200 == code && response.isSuccessful()){
                Developer[] developers = response.body();
                if (developers != null && developers.length > 0) {
                    Log.d("DEVELOPERS", String.valueOf(developers.length));
                    for (Developer servDev :
                            developers) {
                        List<Developer> localDevs = Developer.find(Developer.class, "serverId = ?", String.valueOf(servDev.getId()));
                        if (localDevs != null && localDevs.size() > 0){
                            Developer local = localDevs.get(0);
                            local.setEmail(servDev.getEmail());
                            local.setPhoto_url(servDev.getPhoto_url());
                            local.save();
                        } else {
                            servDev.create();
                        }
                    }
                }

            } else {
                code = 777;

            }


        } catch (IOException e) {
            e.printStackTrace();
            code = 888;
        }
        result = code;
        return  result;
    }







}
