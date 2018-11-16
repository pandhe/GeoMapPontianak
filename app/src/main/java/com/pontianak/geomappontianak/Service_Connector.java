package com.pontianak.geomappontianak;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Service_Connector {
    public static MyConfig myConfig=new MyConfig();
    public static String url=myConfig.main_url;

    final static int timeout=5000;
    private Context ctx;
    private Activity avy;
    final static RetryPolicy policy=new DefaultRetryPolicy(timeout,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


    public Service_Connector(){




    }
    public interface VolleyResponseCookieListener_v2{
        void onError(String message);
        void onResponese(String response);
        void onCookie(NetworkResponse nr);
        void onNoConnection(String message);
        void OnServerError(String message);
        void OnTimeOut();
    }
    public interface VolleyResponseListener_v3{
        void onError(String message);
        void onResponese(String response);
        void onNoConnection(String message);
        void OnServerError(String message);
        void OnTimeOut();
    }
      public static void sendpostrequest(Context ctx, String target, final Map<String,String> myvar, final VolleyResponseListener_v3 listener){
          StringRequest kirim=new StringRequest(Request.Method.POST,url+target,new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  Log.i("ez",response);
                  listener.onResponese(response);
              }

          },new Response.ErrorListener(){
              @Override
              public void onErrorResponse(VolleyError error) {
                  Log.i("ez",error.toString());
                  if (error instanceof ServerError) {
                      String body;
                      final String statusCode = String.valueOf(error.networkResponse.statusCode);
                      //get response body and parse with appropriate encoding
                      try {
                          body = new String(error.networkResponse.data,"UTF-8");
                      } catch (UnsupportedEncodingException e) {
                          // exception
                          body=" ";
                      }
                      listener.OnServerError(body);
                      Log.i("ez",body);

                  }
                  if(error instanceof TimeoutError){
                      listener.OnTimeOut();
                  }

              }

          }) {

              @Override
              protected Map<String,String> getParams(){
                  Map<String,String> params = myvar;

                  return params;
              }



          };
          kirim.setRetryPolicy(policy);
          RequestQueue req;
          req= Volley.newRequestQueue(ctx);
          req.add(kirim);
      }
    public static void sendgetrequest(Context ctx, String target, final VolleyResponseListener_v3 listener){
        StringRequest kirim=new StringRequest(Request.Method.GET,url+target,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ez",response);
                listener.onResponese(response);
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    listener.onNoConnection("Tidak Ada Koneksi");

                }
                else if (error instanceof ServerError) {
                    String body;
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                        body=" ";
                    }
                    Log.i("ez",body);
                    listener.OnServerError(body);

                }
                else {
                    listener.onError(error.toString());
                }


            }

        });
        kirim.setRetryPolicy(policy);
        RequestQueue req;
        req= Volley.newRequestQueue(ctx);
        req.add(kirim);
    }

    public static void sendpostsessionrequest(Context ctx, String target, final Map<String,String> myvar, final VolleyResponseCookieListener_v2 listener){
        StringRequest kirim=new StringRequest(Request.Method.POST,url+target,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponese(response);
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    listener.onNoConnection("Tidak Ada Koneksi");

                }
                else if (error instanceof ServerError) {
                    String body;
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                        body=" ";
                    }
                    listener.OnServerError(body);

                }
                else {
                    listener.onError(error.toString());
                }

            }

        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = myvar;

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    //Log.i("eZ",responseHeaders.toString());
                    String jsonString = new String(response.data, "UTF-8");


                    listener.onCookie(response);
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                }
                catch(UnsupportedEncodingException e){
                    return Response.error(new ParseError(e));
                }

            }
        };
        kirim.setRetryPolicy(policy);
        RequestQueue req;
        req= Volley.newRequestQueue(ctx);
        req.add(kirim);
    }

    public static void sendpostrequestwithsession_v3(Context ctx, final String session, String target, final Map<String,String> myvar, final VolleyResponseListener_v3 listener){

        StringRequest kirim=new StringRequest(Request.Method.POST,url+target,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponese(response);
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    String body;
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                        body=" ";
                    }
                    listener.OnServerError(body);

                }
                if(error instanceof TimeoutError){
                    listener.OnTimeOut();
                }

            }

        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = myvar;

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Cookie",session);

                return params;
            }



        };
        kirim.setRetryPolicy(policy);
        RequestQueue req;
        req= Volley.newRequestQueue(ctx);
        req.add(kirim);
    }
    public static void sendgetrequestwithsession_v3(Context ctx, final String session, String target, final VolleyResponseListener_v3 listener){

        StringRequest kirim=new StringRequest(Request.Method.GET,url+target,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponese(response);
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    String body;
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                        body=" ";
                    }
                    listener.OnServerError(body);

                }
                if(error instanceof TimeoutError){
                    listener.OnTimeOut();
                }

            }

        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Cookie",session);

                return params;
            }



        };
        kirim.setRetryPolicy(policy);
        RequestQueue req;
        req= Volley.newRequestQueue(ctx);
        req.add(kirim);
    }



}
