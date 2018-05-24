
package com.example.loginwithretrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserList {

    @SerializedName("result")
    @Expose
    private List<User> result = null;

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }

}
