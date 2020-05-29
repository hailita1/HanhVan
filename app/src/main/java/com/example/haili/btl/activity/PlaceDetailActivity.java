package com.example.haili.btl.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.haili.btl.R;
import com.example.haili.btl.model.Place;
import com.example.haili.btl.sqlite.DatabaseUtil;
import com.example.haili.btl.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String TAG = PlaceDetailActivity.class.getName();

    private static final String KEY_PLACE_PUT_EXTRA = "key_place";
    private static final String ACTION_EDIT = "edit";
    public static final String ACTION_DIRECT = "action_direct";

    private static final int REQUEST_CODE_EDIT = 11;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_place_detail)
    ImageView ivPlaceDetail;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_diretion)
    ImageView ivDiretion;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_adress)
    EditText edtAdress;
    @BindView(R.id.edt_description)
    EditText edtDescription;

    private DatabaseUtil databaseUtil;
    private Place place;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initProgressDialog();
        init();
    }

    private void init() {
        databaseUtil = DatabaseUtil.getInstance(this);
        Intent intent = getIntent();
        String placeId = intent.getStringExtra(KEY_PLACE_PUT_EXTRA);
        new GetPlaceFromDatabase(databaseUtil).execute(placeId);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(PlaceDetailActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    @OnClick({R.id.iv_delete, R.id.iv_edit, R.id.iv_diretion})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                showAlertDialogDelete();
                break;
            case R.id.iv_edit:
                Intent intent = new Intent(PlaceDetailActivity.this, AddEditActivity.class);
                intent.setAction(ACTION_EDIT);
                intent.putExtra(KEY_PLACE_PUT_EXTRA, place.getPlaceId());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
                break;
            case R.id.iv_diretion:
                Intent intentMap = new Intent(PlaceDetailActivity.this, MapsActivity.class);
                intentMap.setAction(ACTION_DIRECT);

                Log.e("Detail ", place.getLat() + " " + place.getLng());
                intentMap.putExtra("lat", String.valueOf(place.getLat()));
                intentMap.putExtra("lng", String.valueOf(place.getLng()));
                intentMap.putExtra("name", place.getName());
                startActivity(intentMap);
                break;
        }
    }

    public void showAlertDialogDelete() {
        ActivityUtils.showOkCancelConfirmAlertDialog(this, true, "",
                getString(R.string.do_you_want_delete_place),
                getString(R.string.yes),
                getString(R.string.dismiss),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUtil.deletePlace(place);
                        dialog.dismiss();
                        Intent intent = new Intent(PlaceDetailActivity.this, CategoriesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    class GetPlaceFromDatabase extends AsyncTask<String, Void, Place> {

        private DatabaseUtil databaseUtil;

        public GetPlaceFromDatabase(DatabaseUtil databaseUtil) {
            this.databaseUtil = databaseUtil;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Place doInBackground(String... params) {
            String placeId = params[0];
            return databaseUtil.getPlace(placeId);
        }

        @Override
        protected void onPostExecute(Place data) {
            super.onPostExecute(data);
            place = data;

            edtName.setText(place.getName());
            edtAdress.setText(place.getAddress());
            edtDescription.setText(place.getDescription());
            Bitmap placeImage = BitmapFactory.decodeByteArray(place.getImage(), 0, place.getImage().length);
            ivPlaceDetail.setImageBitmap(placeImage);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}