package com.project.rudolf.reactiveled;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import top.defaults.colorpicker.ColorPickerView;

/**
 * Created by Rudolf Nagel on 2019/02/16.
 */

public class HomeActivity extends AppCompatActivity {

    private Button btnNext;
    private Button btnSound;
    private Button btnRainbow;
    private Button btnCylon;
    private Button btnMeteor;
    private Button btnApplyColor;
    private Button btnStatic;
    private ColorPickerView colorPickerView;
    private TextView txtCurrentEffect;
    private ScrollView scrollView;
    private LinearLayout colorLayout;

    private String ipAddress;

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    private RequestQueue requestQueue;

    private int effectId;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ipAddress = getIntent().getStringExtra("ip");

        btnNext = (Button) findViewById(R.id.btnNext);
        btnSound = (Button) findViewById(R.id.btnSound);
        btnRainbow = (Button) findViewById(R.id.btnRainbow);
        btnCylon = (Button) findViewById(R.id.btnCylon);
        btnMeteor = (Button) findViewById(R.id.btnMeteor);
        btnApplyColor = (Button) findViewById(R.id.btnApplyColor);
        btnStatic = (Button) findViewById(R.id.btnStatic);

        txtCurrentEffect = (TextView) findViewById(R.id.txtCurrentEffect);

        colorPickerView = (ColorPickerView) findViewById(R.id.colorPicker);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        colorLayout = (LinearLayout) findViewById(R.id.colorLayout);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNextEffectRequest();
            }
        });

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCertainEffectRequest(0);
            }
        });

        btnRainbow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCertainEffectRequest(1);
            }
        });

        btnCylon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCertainEffectRequest(2);
            }
        });

        btnMeteor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCertainEffectRequest(3);
            }
        });

        btnStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCertainEffectRequest(4);
            }
        });

        btnApplyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendColorRequest();
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        sendInitialEffectRequest();
        sendInitialColorRequest();

        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> {
            red = Color.red(color);
            green = Color.green(color);
            blue = Color.blue(color);
        });
    }

    private void sendInitialEffectRequest() {
        String url = "http://" + ipAddress + "/initial";
        StringRequest arrReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            effectId = Integer.valueOf(response);
                            switch (effectId) {
                                case 0:
                                    txtCurrentEffect.setText("Current Effect: Sound Reative");
                                    break;
                                case 1:
                                    txtCurrentEffect.setText("Current Effect: Rainbow");
                                    break;
                                case 2:
                                    txtCurrentEffect.setText("Current Effect: Cylon");
                                    break;
                                case 3:
                                    txtCurrentEffect.setText("Current Effect: Meteor");
                                    break;
                                case 4:
                                    txtCurrentEffect.setText("Current Effect: Static Color");
                                    break;
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        arrReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(arrReq);
    }

    private void sendInitialColorRequest() {
        String url = "http://" + ipAddress + "/getcolor";
        StringRequest arrReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            red = Integer.valueOf(response.substring(0, response.indexOf(',')));
                            response = response.substring(response.indexOf(',') + 1, response.length());
                            green = Integer.valueOf(response.substring(0, response.indexOf(',')));
                            blue = Integer.valueOf(response.substring(response.indexOf(',') + 1, response.length()));

                            int rgb = red;
                            rgb = (rgb << 8) + green;
                            rgb = (rgb << 8) + blue;

                            colorPickerView.setInitialColor(rgb);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        arrReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(arrReq);
    }

    private void sendNextEffectRequest() {
        String url = "http://" + ipAddress + "/next";
        StringRequest arrReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            effectId = Integer.valueOf(response);

                            switch (effectId) {
                                case 0:
                                    txtCurrentEffect.setText("Current Effect: Sound Reative");
                                    break;
                                case 1:
                                    txtCurrentEffect.setText("Current Effect: Rainbow");
                                    break;
                                case 2:
                                    txtCurrentEffect.setText("Current Effect: Cylon");
                                    break;
                                case 3:
                                    txtCurrentEffect.setText("Current Effect: Meteor");
                                    break;
                                case 4:
                                    txtCurrentEffect.setText("Current Effect: Static Color");
                                    break;
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        arrReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(arrReq);
    }

    private void sendCertainEffectRequest(int id) {
        String url = "http://" + ipAddress + "/effect?id=" + id;
        StringRequest arrReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        switch (id) {
                            case 0:
                                txtCurrentEffect.setText("Current Effect: Sound Reative");
                                break;
                            case 1:
                                txtCurrentEffect.setText("Current Effect: Rainbow");
                                break;
                            case 2:
                                txtCurrentEffect.setText("Current Effect: Cylon");
                                break;
                            case 3:
                                txtCurrentEffect.setText("Current Effect: Meteor");
                                break;
                            case 4:
                                txtCurrentEffect.setText("Current Effect: Static Color");
                                break;
                        }
                    }
                }
        );
        arrReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(arrReq);
    }

    private void sendColorRequest() {
        String url = "http://" + ipAddress + "/color?r=" + red + "&g=" + green + "&b=" + blue;
        StringRequest arrReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            effectId = Integer.valueOf(response);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        arrReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(arrReq);
    }

}
