package rest;
import okhttp3.*;

import java.io.IOException;

public class Http {

    private String token;
    private final OkHttpClient httpClient = new OkHttpClient();

    public Http(){
    }

    public void authenticate_student(String username, String password){
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8000/auth/login/")
                .post(body)
                .build();

        try(Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful()) throw  new IOException("Unexpected code " + response);
            this.token = response.body().string();
            System.out.println(this.token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer get_student_id(){
        return 0;
    }


}
