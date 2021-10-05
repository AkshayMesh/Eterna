package com.eterna.admin.data.helper.base;

import android.view.View;

public interface OnItemClickListener<T>{
    void onClicked(View view, T t);
}
