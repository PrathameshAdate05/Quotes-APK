package com.prathamesh.quotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView content, author;
    private ProgressBar progressBar;
    private Button next, share;
    private ConstraintLayout background;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(R.id.TVContent);
        author = findViewById(R.id.TVAuthor);

        next = findViewById(R.id.BTNNext);
        share = findViewById(R.id.BTNShare);
        background = (ConstraintLayout) findViewById(R.id.background);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        loadQuote();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                loadQuote();
                if(counter%2 == 0){
                    background.setBackgroundResource(R.drawable.mainback3);
                }
                else {
                    background.setBackgroundResource(R.drawable.mainback4);
                }

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quote = content.getText().toString()+"\n\n\n\n\n\n"+author.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,quote);
                startActivity(Intent.createChooser(intent,"Share via.."));
            }
        });
    }

    public void loadQuote(){

        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String contentString = response.getString("content");
                    String authorString = "-"+response.getJSONObject("originator").getString("name");
                    Log.d("response", "content - " + content);
                    Log.d("response", "author - " + author);
                    progressBar.setVisibility(View.GONE);
                    content.setText(contentString);
                    author.setText(authorString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","error - "+error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Check your connection and try again..!!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host",Constants.Host);
                params.put("x-rapidapi-key",Constants.Key);

                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}