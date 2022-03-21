package com.sallet.cold.utils.js;


import com.sallet.cold.utils.Entity;

public class JsResult extends Entity {
    public static final JsResult FAIL_RESULT = new JsResult(-1, null, null);
    public static final int STATUS_FAIL = -1;
    public static final int STATUS_SUCCESS = 0;
    public String[] data;
    public String message;
    public int status;

    public JsResult(int i, String[] strArr, String str) {
        this.status = i;
        this.data = strArr;
        this.message = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String[] getData() {
        return this.data;
    }

    public void setData(String[] strArr) {
        this.data = strArr;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }
}
