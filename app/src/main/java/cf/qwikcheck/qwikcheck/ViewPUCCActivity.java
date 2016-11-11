package cf.qwikcheck.qwikcheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cf.qwikcheck.qwikcheck.helper.SessionHelper;
import cf.qwikcheck.qwikcheck.utils.Constants;

public class ViewPUCCActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pucc);

        final TextView pucc_details = (TextView) findViewById(R.id.pucc_details);

        final String vehicle_id = getIntent().getStringExtra("vehicle_number");

        final ProgressDialog LoadingDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        RequestQueue queue = Volley.newRequestQueue(this);

        SessionHelper sessionHelper = new SessionHelper(this);
        final String apiKey = sessionHelper.getAPIKey();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.API_BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(!success) {
                                new AlertDialog.Builder(ViewPUCCActivity.this)
                                        .setTitle("Error")
                                        .setMessage(jsonObject.getString("error"))
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                LoadingDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {

                                JSONObject details = jsonObject.getJSONObject("details");

                                pucc_details.setText(details.toString());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LoadingDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        new AlertDialog.Builder(ViewPUCCActivity.this)
                                .setTitle("Error")
                                .setMessage("There was an error connecting to server. Make sure your internet is connected.")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoadingDialog.dismiss();
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", apiKey);
                params.put("vehicle_number", vehicle_id);
                params.put("details","PUCC");

                return params;
            }
        };
        queue.add(postRequest);
    }
}
