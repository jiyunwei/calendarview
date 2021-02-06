package com.example.myviewlist.net;

import com.example.myviewlist.bean.NetWorkBaseBean;

public interface OnHttpRequestCallBack<T extends NetWorkBaseBean> {

    void onSuccess(T t);
}
