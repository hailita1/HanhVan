package com.example.haili.btl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.example.haili.btl.R;
import com.example.haili.btl.model.Category;
import com.example.haili.btl.model.Place;
import com.example.haili.btl.network.api.ApiUtils;
import com.example.haili.btl.network.api.MapService;
import com.example.haili.btl.network.pojo.GeocodingRoot;
import com.example.haili.btl.sqlite.DatabaseUtil;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditActivity extends AppCompatActivity {

    private static final String TAG = AddEditActivity.class.getName();
    private static final String KEY_CATEGORY_PUT_EXTRA = "key_category";
    private static final String KEY_PLACE_PUT_EXTRA = "key_place";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_EDIT = "edit";
    private static final int REQUEST_CODE_ADD = 10;
    private static final int REQUEST_CODE_EDIT = 11;
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 1;

    @BindView(R.id.iv_place_edit)
    ImageView ivPlaceEdit;
    @BindView(R.id.edt_name_edit)
    EditText edtNameEdit;
    @BindView(R.id.edt_adress_edit)
    EditText edtAdressEdit;
    @BindView(R.id.edt_description_edit)
    EditText edtDescriptionEdit;
    @BindView(R.id.btn_save)
    Button btnSave;

    private Category category;
    private DatabaseUtil databaseUtil;
    private Place place;
    private ProgressDialog progressDialog;
    private String placeID;
    private boolean isAdd = false;
    private boolean hasImage = false;
    private boolean allowSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);

        initProgressDialog();
        init();
    }

    //hàm ktra nếu k chụp hoặc thêm ảnh thì báo lỗi, nếu đã thêm thì lưu lại
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                //thêm mới
                if (placeID == null) {
                    hasImage = false;
                    allowSave = false;
                } else { //cập nhật
                    hasImage = true;
                }
            } else {
                hasImage = true;
                allowSave = true;
                Bitmap placeImage = (Bitmap) data.getExtras().get("data");
                ivPlaceEdit.setImageBitmap(placeImage);
            }
        }
    }

    private void init() {
        databaseUtil = DatabaseUtil.getInstance(this);
        Intent intent = getIntent();
        Log.e(TAG, intent.getAction());

        if (intent.getAction() == ACTION_ADD) {
            isAdd = true;
            Glide.with(this).load(R.drawable.icon_no_image).into(ivPlaceEdit);
            category = (Category) intent.getSerializableExtra(KEY_CATEGORY_PUT_EXTRA);
            Log.e(TAG, category.getName());

        } else if (intent.getAction() == ACTION_EDIT) {
            isAdd = false;
            Log.e(TAG, isAdd + "");
//            if (place.getImage() != null) {
//
//            } else {
//                Glide.with(this).load(R.drawable.icon_no_image).into(ivPlaceEdit);
//            }
            String placeId = intent.getStringExtra(KEY_PLACE_PUT_EXTRA);
            new GetPlaceFromDatabase(databaseUtil).execute(placeId);
        }
        if (placeID != null) {
            hasImage = true;
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }
    //Chuyển kiểu dữ liệu từ ImageView sang ByteArray để lưu vào csdl
    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        progressDialog.setMessage("Saving");
        progressDialog.show();
        String placeName = edtNameEdit.getText().toString();
        String placeDes = edtDescriptionEdit.getText().toString();
        String placeAddress = edtAdressEdit.getText().toString();

        if (validate(placeName, placeAddress, placeDes)) {
            if (isAdd) {
                addPlaceToDatabase(category);
            } else {
                if (place != null)
                    editPlaceToDatabase(place);
            }
        }
    }

    private void addPlaceToDatabase(Category category) {
        String placeName = edtNameEdit.getText().toString();
        String placeDes = edtDescriptionEdit.getText().toString();
        String placeAddress = edtAdressEdit.getText().toString();

        final Place.Builder builder = new Place.Builder()
                .setName(placeName)
                .setDescription(placeDes)
                .setAddress(placeAddress)
                .setCategoryId(category.getId())
                .setImage(convertImageViewToByteArray(ivPlaceEdit));

        MapService mapService = ApiUtils.getMapService();
        Call<GeocodingRoot> call = mapService.getLocationResults(
                placeAddress, getString(R.string.google_api_key));
        call.enqueue(new Callback<GeocodingRoot>() {
            @Override
            public void onResponse(Call<GeocodingRoot> call, Response<GeocodingRoot> response) {
                GeocodingRoot geocodingRoot = response.body();
                Log.e(TAG, geocodingRoot.getStatus());
                if (geocodingRoot.getStatus().equals("OK")) {
                    double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
                    double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();
                    builder.setLng(lng);
                    builder.setLat(lat);

                    Log.e("Lat ", String.valueOf(lat));
                    Log.e("Lng ", String.valueOf(lng));

                    Toast.makeText(AddEditActivity.this, "Lat " + lat + " Lng " + lng, Toast.LENGTH_SHORT).show();

                    Place place = builder.build();
                    databaseUtil.insertPlace(place);
                    Toast.makeText(AddEditActivity.this, "Save Succesfull", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEditActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeocodingRoot> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void editPlaceToDatabase(Place place) {
        String placeName = edtNameEdit.getText().toString();
        String placeDes = edtDescriptionEdit.getText().toString();
        String placeAddress = edtAdressEdit.getText().toString();

        final Place.Builder builder = new Place.Builder()
                .setName(placeName)
                .setDescription(placeDes)
                .setAddress(placeAddress)
                .setPlaceId(place.getPlaceId())
                .setCategoryId(place.getCategoryId())
                .setImage(convertImageViewToByteArray(ivPlaceEdit));
        MapService mapService = ApiUtils.getMapService();
        Call<GeocodingRoot> call = mapService.getLocationResults(
                placeAddress, getString(R.string.google_api_key));
        call.enqueue(new Callback<GeocodingRoot>() {
            @Override
            public void onResponse(Call<GeocodingRoot> call, Response<GeocodingRoot> response) {
                GeocodingRoot geocodingRoot = response.body();
                Log.e(TAG, geocodingRoot.getStatus());
                if (geocodingRoot.getStatus().equals("OK")) {
                    double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
                    double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();
                    builder.setLng(lng);
                    builder.setLat(lat);

                    Log.e("Lat ", String.valueOf(lat));
                    Log.e("Lng ", String.valueOf(lng));

                    Toast.makeText(AddEditActivity.this, "Lat " + lat + " Lng " + lng, Toast.LENGTH_SHORT).show();

                    Place place = builder.build();
                    databaseUtil.updatePlace(place);
                    Toast.makeText(AddEditActivity.this, "Edit Succesfull", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEditActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeocodingRoot> call, Throwable t) {
                Log.e(TAG, t.getMessage());
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

            edtNameEdit.setText(place.getName());
            edtAdressEdit.setText(place.getAddress());
            edtDescriptionEdit.setText(place.getDescription());
            //Hiện ảnh lên ImageView
            Bitmap placeImage = BitmapFactory.decodeByteArray(place.getImage(), 0, place.getImage().length);
            ivPlaceEdit.setImageBitmap(placeImage);


            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private boolean validate(String name, String address, String des) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            return false;
        }
        return true;
    }
    //Chuyển Intent máy ảnh của điện thoại
    @OnClick(R.id.iv_place_edit)
    public void openCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE);
    }
}
