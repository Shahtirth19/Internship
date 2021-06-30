package com.example.application.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.application.R;
import com.github.andreilisun.swipedismissdialog.OnSwipeDismissListener;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDirection;

import org.json.JSONObject;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, "https://backend-test-zypher.herokuapp.com/testData", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String data = response.getString("title");
                    String url = response.getString("success_url");
                    String imge = response.getString("imageURL");
                    showDialogBox(data,url,imge);
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });
        requestQueue.add(jsonArrayRequest);
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    private void showDialogBox(String data, String url, String imge) {
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog, null);
        new SwipeDismissDialog.Builder(getContext())
                .setView(dialog)
                .build()
                .show();
        SwipeDismissDialog.Builder dilog = new SwipeDismissDialog.Builder(getActivity());

        final TextView usernameEditText = (TextView) dialog.findViewById(R.id.ti);
        final ImageView Image = (ImageView) dialog.findViewById(R.id.im);
        Button success = (Button) dialog.findViewById(R.id.btn_add);
        usernameEditText.setText(data);
        Glide.with(getActivity()).load(imge).into(Image);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setPackage("com.android.chrome");
                    try {
                        getContext().startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "unable to open chrome", Toast.LENGTH_SHORT).show();
                        i.setPackage(null);
                        getContext().startActivity(i);
                    }
            }
        });
        dilog.setOnSwipeDismissListener(new OnSwipeDismissListener() {
            @Override
            public void onSwipeDismiss(View view, SwipeDismissDirection direction) {
                Toast.makeText(getContext(), "Swipe dismissed to: " + direction, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }
}