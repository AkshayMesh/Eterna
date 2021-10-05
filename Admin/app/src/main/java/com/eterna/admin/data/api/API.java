package com.eterna.admin.data.api;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>By developer : AKSHAY MESHRAM<b>
 * created at Maya Labs. all rights reserved</p>
 * <strong>Private Resources : use of this Class may subject to copyright</strong>
 */
public class API {

    /**
     * @param no receiver contact
     * @param msg text to send
     * @return intent to sms
     */
    public static Intent sendSMS(String no, String msg) {
        Uri uri = Uri.parse("smsto:"+no);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", msg);
        return it;
    }

    /**
     * todo add API SERVICE
     * abstract class Request Booking
     * response of request from server is handled by this class
     */
    public static abstract class RequestBooking{

        /**
         * Sending HTTP request to API
         * @param title title
         * @param sub_title subtitle
         * @param topic topic
         */
        public void with(String title, String sub_title, String topic){
            ApiService.request().booking("", title, sub_title,topic)
                    .enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call
                        , @NonNull Response<String> response){
                    if (response.body()!=null){
                        onSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    onFail();
                }
            });
        }

        /**
         * API call is successful
         * @param response Response From API
         */
        public abstract void onSuccess(String response);

        /**
         * API call failed error in API
         */
        public abstract void onFail();
    }

}