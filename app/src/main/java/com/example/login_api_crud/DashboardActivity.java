package com.example.login_api_crud;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RequestQueue requestQueue;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<Category> categories=new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private CategoryAdapter categoryAdapter;
    private Button logout;
    private String url="https://5f8131745b1f3f00161a66fe.mockapi.io/user/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipedown);
        recyclerView=(RecyclerView)findViewById(R.id.category);

        dialog=new Dialog(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                categories.clear();
                getData();
            }
        });
    }
    private void getData() {
        refreshLayout.setRefreshing(true);
        arrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                for (int i=0;i<response.length();i++){
                    try {
                        jsonObject=response.getJSONObject(i);
                        Category cat=new Category();
                        cat.setId(jsonObject.getInt("id"));
                        cat.setCategory(jsonObject.getString("category"));
                        categories.add(cat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterPush(categories);
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Category> categories) {
        categoryAdapter=new CategoryAdapter(this, categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryAdapter);
    }

    public void addCategory(View view){
        TextView titleDialog, close;
        final EditText editCatDialog;
        final Button submit;

        dialog.setContentView(R.layout.activity_modcat);

        close=(TextView) dialog.findViewById(R.id.close);
        titleDialog=(TextView)dialog.findViewById(R.id.titleDialog);
        titleDialog.setText("Thêm");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editCatDialog=(EditText)dialog.findViewById(R.id.editCatDialog);
        submit=(Button)dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=editCatDialog.getText().toString();
                Submit(data);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(final String data) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        categories.clear();
                        getData();
                    }
                });

                Toast.makeText(getApplicationContext(), "Add successfully", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Add not successfully",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params=new HashMap<>();
                params.put("category",data);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onRefresh() {
        categories.clear();
        getData();
    }
}
