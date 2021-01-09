package rest;

import okhttp3.*;

import java.io.IOException;

public class Http {

    private String token;
    private final OkHttpClient httpClient = new OkHttpClient();

    public Http(){
    }

    public void authenticateProfessor(String username, String password){
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

    public String createExam(String description, String date, String location) throws IOException{
        RequestBody body = new FormBody.Builder()
                .add("description", description)
                .add("date", date)
                .add("location", location)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8000/exam/")
                .addHeader("Authorization", "Token "+this.token)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw  new IOException("Unexpected code " + response);
        String idExam = response.body().string().split("\"")[2].replace(":","").replace(",","");
        return idExam;
    }

    public void uploadStudentGrade(String idStudent, String idExam, Float grade) throws IOException{
        RequestBody body = new FormBody.Builder()
                .add("user", idStudent)
                .add("exam", idExam)
                .add("grade", grade.toString())
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8000/grades/")
                .addHeader("Authorization", "Token "+this.token)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw  new IOException("Unexpected code " + response);
    }

}
