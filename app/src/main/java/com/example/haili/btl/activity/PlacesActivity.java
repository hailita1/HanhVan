package com.example.haili.btl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haili.btl.R;
import com.example.haili.btl.adapter.PlaceAdapter;
import com.example.haili.btl.model.Category;
import com.example.haili.btl.model.Place;
import com.example.haili.btl.sqlite.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = PlacesActivity.class.getName();

    private static final String KEY_CATEGORY_PUT_EXTRA = "key_category";
    private static final String KEY_PLACE_PUT_EXTRA = "key_place";
    private static final String ACTION_ADD = "add";
    public static final String ACTION_SHOW_ALL_PLACE = "action_show_all";

    private static final int REQUEST_CODE_ADD = 10;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_places)
    ListView lvPlaces;
    @BindView(R.id.txt_place_no_data)
    TextView txtPlaceNoData;
    @BindView(R.id.float_add_place)
    FloatingActionButton floatAddPlace;
//    @BindView(R.id.btn_show_all_on_map)
//    Button btnShowAllOnMap;

    private DatabaseUtil databaseUtil;
    private Category category;
    private List<Place> placeList = new ArrayList<>();
    private PlaceAdapter placeAdapter;
    private EditText tim;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initProgressDialog();
        init();
        setOnClick();
        tim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PlacesActivity.this.PlaceA
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init() {
        databaseUtil = DatabaseUtil.getInstance(this);
        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(KEY_CATEGORY_PUT_EXTRA);
        String categoryId = category.getId();
        new GetPlacesFromDatabase(databaseUtil).execute(categoryId);
        tim=(EditText) findViewById(R.id.editTextFilter);

        placeAdapter = new PlaceAdapter(this, placeList);
        lvPlaces.setAdapter(placeAdapter);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(PlacesActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    private void setOnClick() {
        lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = placeList.get(position);
                Intent intent = new Intent(PlacesActivity.this, PlaceDetailActivity.class);
                intent.putExtra(KEY_PLACE_PUT_EXTRA, place.getPlaceId());
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.float_add_place})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_add_place:
                Intent intent = new Intent(PlacesActivity.this,AddEditActivity.class);
                intent.setAction(ACTION_ADD);
                intent.putExtra(KEY_CATEGORY_PUT_EXTRA,category);
                startActivityForResult(intent,REQUEST_CODE_ADD);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class GetPlacesFromDatabase extends AsyncTask<String, Void, List<Place>> {

        private DatabaseUtil databaseUtil;

        public GetPlacesFromDatabase(DatabaseUtil databaseUtil) {
            this.databaseUtil = databaseUtil;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Place> doInBackground(String... params) {
            String caregoryId = params[0];
            return databaseUtil.getListPlacesWithCategory(caregoryId);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            placeList = places;
            if (!placeList.isEmpty()) {
                txtPlaceNoData.setVisibility(View.GONE);
                placeAdapter.updateList(placeList);
            } else txtPlaceNoData.setVisibility(View.VISIBLE);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
