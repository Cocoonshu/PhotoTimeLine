package com.cocoonshu.example.phototimeline;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.cocoonshu.example.phototimeline.config.Config;
import com.cocoonshu.example.phototimeline.data.DateTimeAlbum;
import com.cocoonshu.example.phototimeline.utils.Debugger;
import com.cocoonshu.example.phototimeline.utils.PermissionUtils;

import java.util.ArrayList;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity";

    private Toolbar               mToolbar         = null;
    private FloatingActionButton  mFabModeSwitcher = null;
    private DrawerLayout          mDrawerLayout    = null;
    private ActionBarDrawerToggle mDrawerToggle    = null;
    private NavigationView        mNavigationBar   = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Debugger.i(TAG, "[onCreate]");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GalleryApplication application = (GalleryApplication) getApplication();
        application.getThemedConfig(this);

        setupActionBar();
        findViews();
        setupListeners();
    }

    @Override
    protected void onStart() {
        Debugger.i(TAG, "[onStart]");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Debugger.i(TAG, "[onResume]");
        super.onResume();
        if (PermissionUtils.checkSelfPermissions(GalleryActivity.this)) {
            GalleryApplication application = (GalleryApplication) getApplication();
            application.getThreadPool().resume();
            application.getDataManager().resume();
        } else {
            PermissionUtils.requestPermissions(GalleryActivity.this);
        }

        final GalleryApplication application = (GalleryApplication) getApplication();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    DateTimeAlbum album = new DateTimeAlbum(application);
                    album.updateTimeInfo();
                } catch (Throwable thr) {
                    Debugger.e(TAG, "[run] DataTimeAlbum update time information failed", thr);
                }
            }
        };
        application.getWorkExecutor().submit(runnable);
    }

    @Override
    protected void onPause() {
        Debugger.i(TAG, "[onPause]");
        super.onPause();
        GalleryApplication application = (GalleryApplication) getApplication();
        application.getThreadPool().pause();
        application.getDataManager().pause();
    }

    @Override
    protected void onStop() {
        Debugger.i(TAG, "[onStop]");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Debugger.i(TAG, "[onDestroy]");
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void findViews() {
        mFabModeSwitcher = (FloatingActionButton) findViewById(R.id.fab);
        mNavigationBar   = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout    = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle    = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    private void setupListeners() {
        mFabModeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();
            }
        });

        mNavigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_camera) {

                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(GalleryActivity.this, AboutActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean grantPermission() {
        Debugger.i(TAG, "[grantPermission]");
        PackageManager    packageManager = getPackageManager();
        ArrayList<String> noGrantList    = new ArrayList<>();
        String            packageName    = getPackageName();

        for (String permission : Config.Permission.Permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                noGrantList.add(permission);
            }
        }

        if (noGrantList.size() > 0) {
            String[] permissions = noGrantList.toArray(new String[noGrantList.size()]);
            requestPermissions(permissions, Config.Permission.REQUEST_CODE_PERMISSIONS);
            for (String permission : permissions) {
                Debugger.i(TAG, "[grantPermission] Request permission for " + permission);
            }
            return false;
        } else {
            return true;
        }
    }
}
