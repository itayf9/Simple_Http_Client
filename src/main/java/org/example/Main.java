package org.example;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static final String BASE_URL = "http://localhost:8989";

    public static String [] hourAndMinute;

    public static OkHttpClient client = new OkHttpClient();

    public static Gson gson = new Gson();



    public static void main(String[] args) {


                String httpGETResponse = sendDataUsingQueryParameters();

                String httpPOSTResponse = sendingDataToTheServerUsingABody(httpGETResponse);

                String httpPUTResponse = updateDataInTheServer(gson.fromJson(httpPOSTResponse, SimpleMessageResp.class).getMessage());

                deleteResource(gson.fromJson(httpPUTResponse, SimpleMessageResp.class).getMessage());

            }

            private static String sendDataUsingQueryParameters() {

                // get the current time
                Date currentTime = new Date();
                SimpleDateFormat simpleDateFormatCurrentTime = new SimpleDateFormat("kk:mm");
                String currentTimeStr = simpleDateFormatCurrentTime.format(currentTime);

                hourAndMinute = currentTimeStr.split(":");

                HttpUrl.Builder httpUrl = HttpUrl.parse(BASE_URL + "/test_get_method").newBuilder()
                        .addQueryParameter("hour", hourAndMinute[0])
                        .addQueryParameter("minute",hourAndMinute[1]);

                Request request = new Request.Builder()
                        .url(httpUrl.build())
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    return response.body().string();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (NullPointerException e) {
                    throw new RuntimeException(e);
                }
            }

            private static String sendingDataToTheServerUsingABody(String httpGETResponse) {

                // builds the json to send
                SimpleTimeAndGetResp time = new SimpleTimeAndGetResp(Integer.parseInt(hourAndMinute[0]),
                        Integer.parseInt(hourAndMinute[1]),
                        httpGETResponse);

                String body = gson.toJson(time);

                HttpUrl.Builder httpUrl = HttpUrl.parse(BASE_URL + "/test_post_method").newBuilder();

                Request request = new Request.Builder()
                        .url(httpUrl.build())
                        .post(RequestBody.create(body.getBytes()))
                        .header("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    return response.body().string();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private static String updateDataInTheServer(String httpPOSTResponse) {

                int hour = Integer.parseInt(hourAndMinute[0]);
                int minute = Integer.parseInt(hourAndMinute[1]);

                hour = (hour+21) %24;
                minute = (minute+13) %60;

                // builds the json to send
                SimpleTime time = new SimpleTime(hour, minute);

                String body = gson.toJson(time);

                HttpUrl.Builder httpUrl = HttpUrl.parse(BASE_URL + "/test_put_method").newBuilder()
                        .addQueryParameter("id", httpPOSTResponse);

                Request request = new Request.Builder()
                        .url(httpUrl.build())
                        .put(RequestBody.create(body.getBytes()))
                        .header("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    return response.body().string();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private static void deleteResource(String httpPUTResponse) {

                // deletes the resource
                HttpUrl.Builder httpUrl = HttpUrl.parse(BASE_URL + "/test_delete_method").newBuilder()
                        .addQueryParameter("id", httpPUTResponse);

                Request request = new Request.Builder()
                        .url(httpUrl.build())
                        .delete()
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }




}