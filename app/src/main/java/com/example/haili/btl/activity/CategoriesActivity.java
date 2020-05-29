package com.example.haili.btl.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.haili.btl.R;
import com.example.haili.btl.model.Category;
import com.example.haili.btl.sqlite.DatabaseUtil;
import com.example.haili.btl.util.CountDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesActivity extends AppCompatActivity {

    private static final String TAG = CategoriesActivity.class.getName();
    private static final String KEY_CATEGORY_PUT_EXTRA = "key_category";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.constraint_restaurent)
    ConstraintLayout constraintRestaurent;
    @BindView(R.id.constraint_cimema)
    ConstraintLayout constraintCimema;
    @BindView(R.id.constraint_fashion)
    ConstraintLayout constraintFashion;
    @BindView(R.id.constraint_atm)
    ConstraintLayout constraintAtm;

    private List<Category> listCategory;
    private DatabaseUtil databaseUtil;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        databaseUtil = DatabaseUtil.getInstance(this);
        listCategory = databaseUtil.getListCategories();
    }

    private void startPlacesAtc(Category category) {
        Intent placeIntent = new Intent(this, PlacesActivity.class);
        placeIntent.putExtra(KEY_CATEGORY_PUT_EXTRA, category);
        startActivity(placeIntent);
    }

    @OnClick({R.id.constraint_restaurent, R.id.constraint_cimema, R.id.constraint_fashion, R.id.constraint_atm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.constraint_restaurent:
                startPlacesAtc(listCategory.get(0));
                break;
            case R.id.constraint_cimema:
                startPlacesAtc(listCategory.get(1));
                break;
            case R.id.constraint_fashion:
                startPlacesAtc(listCategory.get(2));
                break;
            case R.id.constraint_atm:
                startPlacesAtc(listCategory.get(3));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(menu,this, "9");
        return true;
    }

    public void setCount(Menu menu, Context context, String count) {
        MenuItem menuItem = menu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }
}
