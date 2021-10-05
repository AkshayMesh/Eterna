package com.eterna.admin.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.eterna.admin.data.index.Constants.Extension.EXE;
import static com.eterna.admin.data.index.Constants.PostFile.NOTIFY;

public interface POST_API {
    @FormUrlEncoded
    @POST(NOTIFY + EXE)
    Call<String> booking(@Field("key") String key,
                                      @Field("title") String title,
                                      @Field("subTitle") String sub_title,
                                      @Field("topic") String topic);
}