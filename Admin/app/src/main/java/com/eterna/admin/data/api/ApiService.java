package com.eterna.admin.data.api;

import retrofit2.Call;

public class ApiService {
    public static POST_API request(){
        return new POST_API() {
            @Override
            public Call<String> booking(String key, String title, String sub_title, String topic) {
                return null;
            }
        };
    }
}
