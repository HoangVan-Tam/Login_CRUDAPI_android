package com.example.login_api_crud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private String url="https://5f8131745b1f3f00161a66fe.mockapi.io/user/";

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context=context;
        this.categories=categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.category_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, final int position) {
        holder.title.setText(categories.get(position).getCategory());
        holder.no.setText("#"+String.valueOf(position+1));
        holder.editCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=categories.get(position).getId();
                String value=categories.get(position).getCategory();
                editCategory(id, value);
            }
        });
        holder.deleteCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =categories.get(position).getId();
                deleteCategory(id);
            }
        });
    }

    private void deleteCategory(final int id) {
        TextView titleDialog, close;
        final EditText editCatDialog;
        final Button submit;
        final Dialog dialog;

        dialog=new Dialog(context);

        dialog.setContentView(R.layout.delete_category);

        close=(TextView) dialog.findViewById(R.id.close);
        titleDialog=(TextView)dialog.findViewById(R.id.titleDialog);
        titleDialog.setText("Delete confirm");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit=(Button)dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit("DELETE","", dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void editCategory(final int id, String value) {
        TextView titleDialog, close;
        final EditText editCatDialog;
        final Button submit;
        final Dialog dialog;

        dialog=new Dialog(context);

        dialog.setContentView(R.layout.activity_modcat);

        close=(TextView) dialog.findViewById(R.id.close);
        titleDialog=(TextView)dialog.findViewById(R.id.titleDialog);
        titleDialog.setText("Update");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editCatDialog=(EditText)dialog.findViewById(R.id.editCatDialog);
        submit=(Button)dialog.findViewById(R.id.submit);

        editCatDialog.setText(value);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=editCatDialog.getText().toString();
                Submit("PUT", data, dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(String method, final String data, final Dialog dialog, final int id) {
        if(method=="PUT") {
            StringRequest request = new StringRequest(Request.Method.PUT, url+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Update successfully. Swipe down to load update", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Update not successfully", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("category", data);
                    params.put("id", String.valueOf(id));
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else if (method=="DELETE"){
            StringRequest request = new StringRequest(Request.Method.DELETE, url+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Delete successfully. Swipe down to load update", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Delete not successfully", Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(context).add(request);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, no;
        private ImageView editCate, deleteCate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            no=(TextView)itemView.findViewById(R.id.no);
            title=(TextView)itemView.findViewById(R.id.nameCat);
            editCate=(ImageView)itemView.findViewById(R.id.editCat);
            deleteCate=(ImageView)itemView.findViewById(R.id.deleteCat);

        }
    }
}

