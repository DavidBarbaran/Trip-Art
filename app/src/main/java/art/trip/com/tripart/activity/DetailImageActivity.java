package art.trip.com.tripart.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.adapter.ImageViewAdapter;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.listener.OnItemClick;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.util.DesignUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailImageActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    Button backBtn;

    @BindView(R.id.like_btn)
    Button likeBtn;

    @BindView(R.id.share_btn)
    Button shareBtn;

    @BindView(R.id.download_btn)
    Button downloadBtn;

    @BindView(R.id.info_btn)
    Button infoBtn;

    @BindView(R.id.footer_view)
    View footerView;

    @BindView(R.id.title_text)
    TextView titleText;

    @BindView(R.id.header_view)
    View headerView;

    @BindView(R.id.image_recycler)
    RecyclerView imageRecycler;

    @BindView(R.id.title_info_text)
    TextView titleInfoText;

    @BindView(R.id.author_info_text)
    TextView authorInfoText;

    @BindView(R.id.description_info_text)
    TextView descriptionInfoText;

    @BindView(R.id.info_view)
    View infoView;

    private boolean statusIcon;
    private int statusBack;
    int prevCenterPos;
    int centerPos;
    private long lastDownload = -1L;
    private DownloadManager mgr = null;
    Image model;
    boolean isFinishAnimation;
    boolean isFinishLoad;
    List<Image> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        isFinishAnimation = false;
        isFinishLoad = false;
        ButterKnife.bind(this);

        statusIcon = true;
        statusBack = 1;
        DesignUtil.setDesignColor(this, R.color.black);
        model = (Image) getIntent().getSerializableExtra(Setting.IMAGE);
        titleText.setText(model.getTitle());

        switch (getIntent().getExtras().getInt(Setting.LIST_IMAGE)){
            case 1:
                list = Setting.imageList;
                break;
            case 2:
                list = Setting.imageListFilter;
                break;
            default:
                break;
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageRecycler.setLayoutManager(linearLayoutManager);
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(list, this);
        imageViewAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClick(int position) {
                hideAndShowButton();
            }
        });

        imageRecycler.setAdapter(imageViewAdapter);
        linearLayoutManager.scrollToPositionWithOffset(model.getPosition(), 0);
        prevCenterPos = model.getPosition() - 1;

        imageRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int center = imageRecycler.getWidth() / 2;
                View centerView = imageRecycler.findChildViewUnder(center, imageRecycler.getTop());
                centerPos = imageRecycler.getChildAdapterPosition(centerView);

                if (prevCenterPos != centerPos) {
                    prevCenterPos = centerPos;
                    titleText.setText(list.get(centerPos).getTitle());


                }
            }
        });
        initServiceDownload();
    }
    private void initServiceDownload() {
        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    @OnClick(R.id.back_btn)
    public void actionBack() {
        onBackPressed();
    }

    @OnClick(R.id.download_btn)
    public void actionDownload() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionDialog();
        } else {

            Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .mkdirs();

            lastDownload =
                    mgr.enqueue(new DownloadManager.Request(Uri.parse(list.get(centerPos).getPath()))
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Demo")
                            .setDescription("Something useful. No, really.")
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                    list.get(centerPos).getTitle() + (list.get(centerPos).getPath().endsWith(".gif") ? ".gif" : ".png")));
        }
    }

    private void permissionDialog() {
        List<String> permitsList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permitsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permitsList.size() > 0) {
            String[] permissionArray = new String[permitsList.size()];
            permissionArray = permitsList.toArray(permissionArray);

            ActivityCompat.requestPermissions(this, permissionArray, Setting.PERMISSIONS_MULTIPLE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            actionDownload();
        }
    }

    private void hideAndShowButton() {
        if (statusIcon) {
            statusIcon = false;
            alphaAnimation(headerView, 1.0f, 0.0f, statusIcon);
            alphaAnimation(footerView, 1.0f, 0.0f, statusIcon);
        } else {
            statusIcon = true;
            alphaAnimation(headerView, 0.0f, 1.0f, statusIcon);
            alphaAnimation(footerView, 0.0f, 1.0f, statusIcon);
        }
    }

    private void alphaAnimation(final View view, float startValue, float endValue, final boolean visible) {
        Animation animation = new AlphaAnimation(startValue, endValue);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(visible ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(300);
        view.startAnimation(animation);
    }

    @OnClick(R.id.info_btn)
    public void actionInfoBtn() {
        titleInfoText.setText(list.get(centerPos).getTitle());
        authorInfoText.setText(list.get(centerPos).getAuthor());
        descriptionInfoText.setText(list.get(centerPos).getDescription());
        alphaAnimation(infoView, 0.0f, 1.0f, true);

        backBtn.setBackgroundResource(R.drawable.ic_cancel);
        statusBack = 2;

        alphaAnimation(footerView, 1.0f, 0.0f, false);
        alphaAnimation(titleText, 1.0f, 0.0f, false);
    }

    @Override
    public void onBackPressed() {
        switch (statusBack) {
            case 1:
                super.onBackPressed();
                break;
            case 2:
                statusBack = 1;
                alphaAnimation(infoView, 1.0f, 0.0f, false);
                alphaAnimation(footerView, 0.0f, 1.0f, true);
                alphaAnimation(titleText, 0.0f, 1.0f, true);
                backBtn.setBackgroundResource(R.drawable.ic_back_white);
                break;
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Descarga completa", Toast.LENGTH_LONG).show();
        }
    };

    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

}