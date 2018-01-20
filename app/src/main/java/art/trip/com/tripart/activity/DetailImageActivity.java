package art.trip.com.tripart.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;


import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

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

    private boolean statusIcon;
    int prevCenterPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        ButterKnife.bind(this);
        statusIcon = true;
        DesignUtil.setDesignColor(this, R.color.black);
        Image model = (Image) getIntent().getSerializableExtra(Setting.IMAGE);
        titleText.setText(model.getTitle());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageRecycler.setLayoutManager(linearLayoutManager);
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(Setting.imageList, this);
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
                int centerPos = imageRecycler.getChildAdapterPosition(centerView);

                if (prevCenterPos != centerPos) {
                    prevCenterPos = centerPos;
                    Log.e("pos", centerPos + "");
                    titleText.setText(Setting.imageList.get(centerPos).getTitle());

                }
            }
        });


    }

    @OnClick(R.id.back_btn)
    public void actionBack() {
        onBackPressed();
    }

    private void hideAndShowButton() {
        if (statusIcon) {
            hideView(headerView, 1.0f, 0.0f);
            hideView(footerView, 1.0f, 0.0f);
            statusIcon = false;
        } else {
            hideView(headerView, 0.0f, 1.0f);
            hideView(footerView, 0.0f, 1.0f);
            statusIcon = true;
        }
    }

    private void hideView(final View view, float startValue, float endValue) {
        Animation animation = new AlphaAnimation(startValue, endValue);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(statusIcon ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(300);
        view.startAnimation(animation);
    }
}