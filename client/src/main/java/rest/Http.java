package rest;
import okhttp3.*;

import java.io.IOException;


public class Http {

    private String token;
    private final OkHttpClient httpClient = new OkHttpClient();

    public Http(){
    }

    public void authenticateStudent(String username, String password){
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
            this.token = response.body().string().split("\"")[3];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStudentId() throws IOException{
        Request request = new Request.Builder()
                .url("http://localhost:8000/auth/user/")
                .addHeader("Authorization", "Token "+this.token)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw  new IOException("Unexpected code " + response);
        String idStudent = response.body().string().split("\"")[2].replace(":","").replace(",","");
        return idStudent;

    }


}
