package com.cookandroid.traveler_maps;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.util.Log;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Arrays;

public class GangwonActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    Region[] regions;
    Path[] paths;
    Matrix matrix;

    private static final String TAG = "GangwonActivity";
    private static final String PREFS_NAME = "GangwonPrefs";
    private static final String SELECTED_REGIONS_KEY = "GangwonSelectedRegions";
    private static final String IS_FILLED_KEY = "IsFilledArray";
    public static boolean[] Gangwon_isFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gangwon);
        imageView = findViewById(R.id.gangwonImageView);
        imageView.setImageResource(R.drawable.gangwon_do);

        // 색칠 여부 배열 초기화
        Gangwon_isFilled = new boolean[18];
        for (int i = 0; i < 18; i++) {
            Gangwon_isFilled[i] = false;
        }

        loadGangwonIsFilledArray();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.topMargin = -imageView.getHeight() / 4;
        imageView.setLayoutParams(params);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = imageView.getDrawable();
                Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
                int imageViewWidth = imageView.getWidth();
                int imageViewHeight = imageView.getHeight();

                bitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);

                float scaleX = (float) imageViewWidth / originalBitmap.getWidth() * 0.9f;
                float scaleY = (float) imageViewHeight / originalBitmap.getHeight() * 0.9f;
                float scale = Math.min(scaleX, scaleY);
                float dx = (imageViewWidth - originalBitmap.getWidth() * scale) / 2;
                float dy = (imageViewHeight - originalBitmap.getHeight() * scale) / 2;

                matrix = new Matrix();
                matrix.setScale(scale, scale);
                matrix.postTranslate(dx, dy);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                params.topMargin = -imageView.getHeight() / 2;
                params.leftMargin = -imageView.getWidth() / 4;
                imageView.setLayoutParams(params);

                canvas.drawBitmap(originalBitmap, matrix, null);
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                initializeRegions();
                loadSelectedRegions();

                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setImageMatrix(matrix);

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            float[] eventCoords = new float[]{event.getX(), event.getY()};
                            Matrix invertMatrix = new Matrix();
                            imageView.getImageMatrix().invert(invertMatrix);
                            invertMatrix.mapPoints(eventCoords);
                            float bitmapX = eventCoords[0];
                            float bitmapY = eventCoords[1];

                            Log.d(TAG, "Touch coordinates: (" + bitmapX + ", " + bitmapY + ")");

                            for (int i = 0; i < regions.length; i++) {
                                if (regions[i].contains((int) bitmapX, (int) bitmapY)) {
                                    if (Gangwon_isFilled[i]) {
                                        // 색칠 제거는 기본 배경색과 동일한 색으로 다시 그려주면 됨
                                        paint.setColor(Color.WHITE); // 흰색으로 설정
                                        canvas.drawPath(paths[i], paint);
                                        Gangwon_isFilled[i] = false;
                                    } else {
                                        paint.setColor(Color.GREEN); // 초록색으로 설정
                                        canvas.drawPath(paths[i], paint);
                                        Gangwon_isFilled[i] = true;
                                    }
                                    imageView.invalidate();
                                    saveSelectedRegion(i);
                                    break;
                                }
                            }
                        }
                        return true;
                    }
                });
            }
        });
    }


    private void initializeRegions() {
        // 영역 초기화
        regions = new Region[18]; // 강원도에 18개 시/군/구
        paths = new Path[18];

        // 철원
        paths[0] = new Path();
        paths[0].moveTo(172.00726f, 643.5445f);
        paths[0].lineTo(174.35219f, 635.71027f);
        paths[0].lineTo(178.26929f, 630.2584f);
        paths[0].lineTo(186.85101f, 629.47955f);
        paths[0].lineTo(197.80363f, 631.03723f);
        paths[0].lineTo(214.2188f, 634.93146f);
        paths[0].lineTo(224.3987f, 641.9868f);
        paths[0].lineTo(230.63475f, 645.1022f);
        paths[0].lineTo(261.9204f, 627.8761f);
        paths[0].lineTo(279.10907f, 631.03723f);
        paths[0].lineTo(304.9306f, 634.93146f);
        paths[0].lineTo(310.39392f, 639.6045f);
        paths[0].lineTo(340.10742f, 637.26794f);
        paths[0].lineTo(354.95117f, 647.43866f);
        paths[0].lineTo(358.09552f, 654.494f);
        paths[0].lineTo(351.83276f, 656.83057f);
        paths[0].lineTo(343.22583f, 651.3329f);
        paths[0].lineTo(334.6441f, 653.7152f);
        paths[0].lineTo(327.58337f, 655.2729f);
        paths[0].lineTo(324.46497f, 661.5036f);
        paths[0].lineTo(319.00165f, 668.5589f);
        paths[0].lineTo(314.31104f, 672.4532f);
        paths[0].lineTo(308.049f, 669.3378f);
        paths[0].lineTo(302.5857f, 665.44354f);
        paths[0].lineTo(294.75153f, 663.0612f);
        paths[0].lineTo(287.716f, 664.66473f);
        paths[0].lineTo(285.3711f, 664.66473f);
        paths[0].lineTo(280.6812f, 661.5036f);
        paths[0].lineTo(276.76416f, 655.2729f);
        paths[0].lineTo(272.07355f, 654.494f);
        paths[0].lineTo(273.64575f, 660.7248f);
        paths[0].lineTo(268.18243f, 665.44354f);
        paths[0].lineTo(259.5747f, 673.232f);
        paths[0].lineTo(259.5747f, 681.0662f);
        paths[0].lineTo(254.11137f, 681.0662f);
        paths[0].lineTo(250.19429f, 682.62384f);
        paths[0].lineTo(250.19429f, 688.07574f);
        paths[0].lineTo(238.46895f, 692.0158f);
        paths[0].lineTo(237.67024f, 698.2464f);
        paths[0].lineTo(236.12404f, 701.40765f);
        paths[0].lineTo(233.7791f, 709.196f);
        paths[0].lineTo(226.7436f, 717.809f);
        paths[0].lineTo(232.97966f, 725.64325f);
        paths[0].lineTo(226.7436f, 733.4317f);
        paths[0].lineTo(215.01826f, 737.37164f);
        paths[0].lineTo(214.2188f, 741.2658f);
        paths[0].lineTo(207.18405f, 735.814f);
        paths[0].lineTo(195.45795f, 747.5424f);
        paths[0].lineTo(187.6497f, 741.2658f);
        paths[0].lineTo(183.7326f, 740.487f);
        paths[0].lineTo(171.23376f, 745.1601f);
        paths[0].lineTo(172.00726f, 643.5445f);

        // 화천
        paths[1] = new Path();
        paths[1].moveTo(355.74982f, 649.7752f);
        paths[1].lineTo(353.4049f, 655.2729f);
        paths[1].lineTo(347.14294f, 652.93634f);
        paths[1].lineTo(331.5005f, 652.93634f);
        paths[1].lineTo(324.46497f, 658.38824f);
        paths[1].lineTo(316.65674f, 668.5589f);
        paths[1].lineTo(315.85724f, 672.4532f);
        paths[1].lineTo(307.2763f, 668.5589f);
        paths[1].lineTo(298.66858f, 663.0612f);
        paths[1].lineTo(285.3711f, 664.66473f);
        paths[1].lineTo(275.2179f, 654.494f);
        paths[1].lineTo(271.30084f, 653.7152f);
        paths[1].lineTo(268.95514f, 656.83057f);
        paths[1].lineTo(273.64575f, 663.0612f);
        paths[1].lineTo(269.72864f, 668.5589f);
        paths[1].lineTo(261.9204f, 669.3378f);
        paths[1].lineTo(257.2298f, 676.3931f);
        paths[1].lineTo(249.42155f, 680.2873f);
        paths[1].lineTo(248.62285f, 690.45807f);
        paths[1].lineTo(236.12404f, 697.4676f);
        paths[1].lineTo(233.7791f, 706.0806f);
        paths[1].lineTo(229.08852f, 715.4726f);
        paths[1].lineTo(234.55183f, 727.201f);
        paths[1].lineTo(225.9449f, 734.9893f);
        paths[1].lineTo(210.32767f, 739.7082f);
        paths[1].lineTo(206.41055f, 736.59283f);
        paths[1].lineTo(205.61186f, 738.9294f);
        paths[1].lineTo(208.75548f, 744.38116f);
        paths[1].lineTo(211.10115f, 760.0496f);
        paths[1].lineTo(218.13591f, 795.2348f);
        paths[1].lineTo(242.36009f, 801.4655f);
        paths[1].lineTo(250.19429f, 801.4655f);
        paths[1].lineTo(250.19429f, 791.29474f);
        paths[1].lineTo(261.1469f, 789.7371f);
        paths[1].lineTo(264.26532f, 775.6722f);
        paths[1].lineTo(259.5747f, 763.9438f);
        paths[1].lineTo(258.0033f, 755.33075f);
        paths[1].lineTo(268.18243f, 756.8885f);
        paths[1].lineTo(273.64575f, 762.38617f);
        paths[1].lineTo(276.76416f, 770.1745f);
        paths[1].lineTo(283.7989f, 774.11456f);
        paths[1].lineTo(293.20526f, 772.5568f);
        paths[1].lineTo(297.09644f, 783.5064f);
        paths[1].lineTo(311.16742f, 781.9029f);
        paths[1].lineTo(318.20294f, 781.9029f);
        paths[1].lineTo(320.54785f, 787.4006f);
        paths[1].lineTo(330.727f, 781.9029f);
        paths[1].lineTo(340.10742f, 783.5064f);
        paths[1].lineTo(345.57074f, 793.6313f);
        paths[1].lineTo(351.06f, 803.02313f);
        paths[1].lineTo(362.78534f, 797.57135f);
        paths[1].lineTo(376.85638f, 793.6313f);
        paths[1].lineTo(376.85638f, 774.11456f);
        paths[1].lineTo(394.84375f, 774.11456f);
        paths[1].lineTo(402.65198f, 770.1745f);
        paths[1].lineTo(397.18866f, 756.8885f);
        paths[1].lineTo(394.04504f, 749.10004f);
        paths[1].lineTo(384.6646f, 742.82355f);
        paths[1].lineTo(385.46332f, 727.201f);
        paths[1].lineTo(388.58173f, 706.0806f);
        paths[1].lineTo(383.1184f, 697.4676f);
        paths[1].lineTo(375.28418f, 690.45807f);
        paths[1].lineTo(376.85638f, 672.4532f);
        paths[1].lineTo(379.2013f, 654.494f);
        paths[1].lineTo(358.09552f, 652.93634f);

        // 양구
        paths[2] = new Path();
        paths[2].moveTo(375.28418f, 654.494f);    // 시작점
        paths[2].lineTo(367.47595f, 674.7896f);   // 다음 좌표
        paths[2].lineTo(377.6291f, 690.45807f);   // 다음 좌표
        paths[2].lineTo(389.35443f, 712.35724f);  // 다음 좌표
        paths[2].lineTo(387.00952f, 739.7082f);   // 다음 좌표
        paths[2].lineTo(398.73486f, 760.8284f);   // 다음 좌표
        paths[2].lineTo(400.30707f, 784.2852f);   // 다음 좌표
        paths[2].lineTo(413.6046f, 791.29474f);   // 다음 좌표
        paths[2].lineTo(418.29517f, 784.2852f);   // 다음 좌표
        paths[2].lineTo(422.21228f, 781.9029f);   // 다음 좌표
        paths[2].lineTo(432.36542f, 787.4006f);   // 다음 좌표
        paths[2].lineTo(433.93762f, 799.12897f);  // 다음 좌표
        paths[2].lineTo(441.74585f, 791.29474f);  // 다음 좌표
        paths[2].lineTo(463.65106f, 788.1794f);   // 다음 좌표
        paths[2].lineTo(462.85162f, 775.6722f);   // 다음 좌표
        paths[2].lineTo(476.14917f, 783.5064f);   // 다음 좌표
        paths[2].lineTo(484.75684f, 774.11456f);  // 다음 좌표
        paths[2].lineTo(482.41193f, 764.7226f);   // 다음 좌표
        paths[2].lineTo(474.5777f, 749.10004f);   // 다음 좌표
        paths[2].lineTo(480.83972f, 741.2658f);   // 다음 좌표
        paths[2].lineTo(473.0315f, 721.7033f);    // 다음 좌표
        paths[2].lineTo(476.92267f, 698.2464f);   // 다음 좌표
        paths[2].lineTo(487.10175f, 689.67926f);  // 다음 좌표
        paths[2].lineTo(488.648f, 673.232f);      // 다음 좌표
        paths[2].lineTo(502.7442f, 657.6094f);    // 다음 좌표
        paths[2].lineTo(505.8626f, 634.1526f);    // 다음 좌표
        paths[2].lineTo(495.70947f, 627.8761f);   // 다음 좌표
        paths[2].lineTo(476.92267f, 630.2584f);   // 다음 좌표
        paths[2].lineTo(469.11444f, 645.881f);    // 다음 좌표
        paths[2].lineTo(451.12628f, 649.7752f);   // 다음 좌표
        paths[2].lineTo(430.0205f, 656.83057f);   // 다음 좌표
        paths[2].lineTo(396.38995f, 639.6045f);   // 다음 좌표
        paths[2].lineTo(378.4278f, 653.7152f);    // 다음 좌표
        paths[2].lineTo(375.28418f, 653.7152f);   // 마지막 좌표로 돌아감

// 인재
        paths[3] = new Path();
        paths[3].moveTo(518.36145f, 610.6958f);
        paths[3].lineTo(502.7442f, 638.8256f);
        paths[3].lineTo(504.2904f, 654.494f);
        paths[3].lineTo(488.648f, 677.95087f);
        paths[3].lineTo(474.5777f, 712.35724f);
        paths[3].lineTo(488.648f, 774.8934f);
        paths[3].lineTo(433.93762f, 796.7924f);
        paths[3].lineTo(435.48383f, 829.5953f);
        paths[3].lineTo(504.2904f, 859.3287f);
        paths[3].lineTo(521.50507f, 892.1773f);
        paths[3].lineTo(559.02673f, 868.7205f);
        paths[3].lineTo(580.932f, 892.1773f);
        paths[3].lineTo(605.9289f, 865.60516f);
        paths[3].lineTo(618.4537f, 871.8359f);
        paths[3].lineTo(635.64233f, 853.0522f);
        paths[3].lineTo(635.64233f, 821.80695f);
        paths[3].lineTo(645.02277f, 785.84296f);
        paths[3].lineTo(607.5011f, 770.1745f);
        paths[3].lineTo(607.5011f, 748.3212f);
        paths[3].lineTo(629.4063f, 734.2105f);
        paths[3].lineTo(605.9289f, 707.63837f);
        paths[3].lineTo(618.4537f, 688.90045f);
        paths[3].lineTo(613.7631f, 665.44354f);
        paths[3].lineTo(584.0504f, 671.67426f);
        paths[3].lineTo(565.28955f, 652.93634f);
        paths[3].lineTo(555.9091f, 623.20306f);
        paths[3].lineTo(540.2659f, 606.02277f);
        paths[3].lineTo(524.6235f, 606.02277f);
        paths[3].lineTo(519.93365f, 609.1381f);


// 고성
        paths[4] = new Path();
        paths[4].moveTo(584.0504f, 492.633f);
        paths[4].lineTo(578.5871f, 504.3614f);
        paths[4].lineTo(573.8965f, 502.0249f);
        paths[4].lineTo(573.8965f, 512.9744f);
        paths[4].lineTo(572.3243f, 521.5417f);
        paths[4].lineTo(569.9794f, 523.924f);
        paths[4].lineTo(572.3243f, 539.5466f);
        paths[4].lineTo(572.3243f, 548.9385f);
        paths[4].lineTo(562.94385f, 556.72687f);
        paths[4].lineTo(562.14514f, 576.2895f);
        paths[4].lineTo(556.6818f, 577.0683f);
        paths[4].lineTo(545.72925f, 583.34485f);
        paths[4].lineTo(544.9565f, 592.6909f);
        paths[4].lineTo(544.9565f, 606.80164f);
        paths[4].lineTo(545.72925f, 620.0877f);
        paths[4].lineTo(551.2185f, 623.20306f);
        paths[4].lineTo(562.14514f, 623.9819f);
        paths[4].lineTo(566.06226f, 645.1022f);
        paths[4].lineTo(566.06226f, 662.2824f);
        paths[4].lineTo(581.7047f, 660.7248f);
        paths[4].lineTo(587.96747f, 670.11664f);
        paths[4].lineTo(597.3479f, 670.89545f);
        paths[4].lineTo(609.846f, 666.2225f);
        paths[4].lineTo(611.41815f, 667.3907f);
        paths[4].lineTo(616.10876f, 692.0158f);
        paths[4].lineTo(617.0327f, 692.0158f);
        paths[4].lineTo(632.5239f, 698.2464f);
        paths[4].lineTo(674.73627f, 681.0662f);
        paths[4].lineTo(656.7741f, 635.71027f);
        paths[4].lineTo(644.25006f, 606.80164f);
        paths[4].lineTo(621.5721f, 568.45526f);
        paths[4].lineTo(599.6928f, 512.9744f);


// 속초
        paths[5] = new Path();
        paths[5].moveTo(615.33527f, 690.45807f);    // 시작점
        paths[5].lineTo(616.88153f, 696.6888f);    // 다음 좌표
        paths[5].lineTo(609.846f, 698.2464f);      // 다음 좌표
        paths[5].lineTo(608.2746f, 704.523f);      // 다음 좌표
        paths[5].lineTo(616.10876f, 717.809f);     // 다음 좌표
        paths[5].lineTo(625.4892f, 725.64325f);    // 다음 좌표
        paths[5].lineTo(627.8341f, 733.4317f);     // 다음 좌표
        paths[5].lineTo(637.21454f, 727.201f);     // 다음 좌표
        paths[5].lineTo(651.2848f, 725.64325f);    // 다음 좌표
        paths[5].lineTo(654.42914f, 721.7033f);    // 다음 좌표
        paths[5].lineTo(666.928f, 720.14557f);     // 다음 좌표
        paths[5].lineTo(668.49945f, 711.53253f);   // 다음 좌표
        paths[5].lineTo(683.3432f, 713.91486f);    // 다음 좌표
        paths[5].lineTo(684.1167f, 703.7442f);     // 다음 좌표
        paths[5].lineTo(674.73627f, 677.95087f);   // 다음 좌표
        paths[5].lineTo(654.42914f, 684.9604f);    // 다음 좌표
        paths[5].lineTo(635.64233f, 699.80414f);   // 다음 좌표
        paths[5].lineTo(616.10876f, 692.7946f);    // 마지막 좌표로 돌아감

// 양양
        paths[6] = new Path();
        paths[6].moveTo(625.4892f, 735.814f);    // 시작점
        paths[6].lineTo(609.07324f, 741.2658f); // 다음 좌표
        paths[6].lineTo(604.3827f, 740.487f);   // 다음 좌표
        paths[6].lineTo(601.2391f, 750.6578f);  // 다음 좌표
        paths[6].lineTo(605.1562f, 759.2708f);  // 다음 좌표
        paths[6].lineTo(609.846f, 772.5568f);   // 다음 좌표
        paths[6].lineTo(617.6802f, 767.83795f); // 다음 좌표
        paths[6].lineTo(630.9525f, 772.5568f);  // 다음 좌표
        paths[6].lineTo(633.2974f, 770.1745f);  // 다음 좌표
        paths[6].lineTo(643.47656f, 785.84296f);// 다음 좌표
        paths[6].lineTo(646.595f, 796.0136f);   // 다음 좌표
        paths[6].lineTo(642.67786f, 799.9078f); // 다음 좌표
        paths[6].lineTo(641.13165f, 811.63617f);// 다음 좌표
        paths[6].lineTo(631.7512f, 815.53046f); // 다음 좌표
        paths[6].lineTo(628.6069f, 829.5953f);  // 다음 좌표
        paths[6].lineTo(631.7512f, 836.6507f);  // 다음 좌표
        paths[6].lineTo(633.2974f, 850.71564f); // 다음 좌표
        paths[6].lineTo(647.3677f, 857.77094f); // 다음 좌표
        paths[6].lineTo(659.119f, 850.71564f);  // 다음 좌표
        paths[6].lineTo(675.5349f, 868.7205f);  // 다음 좌표
        paths[6].lineTo(691.95087f, 857.77094f);// 다음 좌표
        paths[6].lineTo(698.2129f, 850.71564f); // 다음 좌표
        paths[6].lineTo(713.05664f, 852.27325f);// 다음 좌표
        paths[6].lineTo(727.1277f, 846.8215f);  // 다음 좌표
        paths[6].lineTo(769.3392f, 838.98724f); // 다음 좌표
        paths[6].lineTo(757.61383f, 822.58575f);// 다음 좌표
        paths[6].lineTo(734.1624f, 789.7371f);  // 다음 좌표
        paths[6].lineTo(709.93823f, 756.8885f); // 다음 좌표
        paths[6].lineTo(685.68884f, 713.91486f);// 다음 좌표
        paths[6].lineTo(670.04565f, 713.91486f);// 마지막 좌표로 돌아감

// 춘천
        paths[7] = new Path();
        paths[7].moveTo(247.84938f, 799.9078f);
        paths[7].lineTo(250.19429f, 813.9727f);
        paths[7].lineTo(254.11137f, 822.58575f);
        paths[7].lineTo(268.95514f, 822.58575f);
        paths[7].lineTo(276.76416f, 829.5953f);
        paths[7].lineTo(279.90778f, 846.04254f);
        paths[7].lineTo(279.90778f, 858.54987f);
        paths[7].lineTo(275.99066f, 865.60516f);
        paths[7].lineTo(265.83746f, 865.60516f);
        paths[7].lineTo(261.1469f, 872.6147f);
        paths[7].lineTo(249.42155f, 879.6701f);
        paths[7].lineTo(242.36009f, 887.45844f);
        paths[7].lineTo(243.93227f, 896.0715f);
        paths[7].lineTo(246.27718f, 900.74457f);
        paths[7].lineTo(242.36009f, 905.46344f);
        paths[7].lineTo(246.27718f, 917.19183f);
        paths[7].lineTo(251.76646f, 925.02594f);
        paths[7].lineTo(240.81386f, 935.15094f);
        paths[7].lineTo(234.55183f, 940.6486f);
        paths[7].lineTo(238.46895f, 944.5428f);
        paths[7].lineTo(243.93227f, 943.764f);
        paths[7].lineTo(250.19429f, 946.1004f);
        paths[7].lineTo(257.2298f, 943.764f);
        paths[7].lineTo(265.83746f, 942.20624f);
        paths[7].lineTo(269.72864f, 946.1004f);
        paths[7].lineTo(272.87225f, 952.377f);
        paths[7].lineTo(277.5628f, 952.377f);
        paths[7].lineTo(279.90778f, 967.9996f);
        paths[7].lineTo(287.716f, 957.8288f);
        paths[7].lineTo(292.40656f, 957.8288f);
        paths[7].lineTo(293.20526f, 967.9996f);
        paths[7].lineTo(297.09644f, 958.6077f);
        paths[7].lineTo(308.049f, 957.8288f);
        paths[7].lineTo(308.8225f, 948.4827f);
        paths[7].lineTo(328.38208f, 939.0909f);
        paths[7].lineTo(335.4176f, 935.92975f);
        paths[7].lineTo(340.10742f, 939.0909f);
        paths[7].lineTo(347.14294f, 935.92975f);
        paths[7].lineTo(353.4049f, 935.92975f);
        paths[7].lineTo(361.2132f, 932.0356f);
        paths[7].lineTo(370.59363f, 935.15094f);
        paths[7].lineTo(373.73798f, 926.5837f);
        paths[7].lineTo(379.2013f, 915.6341f);
        paths[7].lineTo(369.02216f, 905.46344f);
        paths[7].lineTo(366.6765f, 895.29266f);
        paths[7].lineTo(363.55884f, 885.122f);
        paths[7].lineTo(369.82086f, 882.00665f);
        paths[7].lineTo(377.6291f, 885.9008f);
        paths[7].lineTo(383.1184f, 882.00665f);
        paths[7].lineTo(390.92664f, 871.8359f);
        paths[7].lineTo(397.18866f, 873.3936f);
        paths[7].lineTo(408.1153f, 885.9008f);
        paths[7].lineTo(414.3781f, 885.122f);
        paths[7].lineTo(413.6046f, 872.6147f);
        paths[7].lineTo(422.21228f, 862.44403f);
        paths[7].lineTo(429.247f, 850.71564f);
        paths[7].lineTo(433.93762f, 835.0931f);
        paths[7].lineTo(435.48383f, 827.25885f);
        paths[7].lineTo(430.0205f, 819.4704f);
        paths[7].lineTo(431.5927f, 801.4655f);
        paths[7].lineTo(431.5927f, 792.07367f);
        paths[7].lineTo(430.0205f, 785.84296f);
        paths[7].lineTo(418.29517f, 783.5064f);
        paths[7].lineTo(414.3781f, 790.5159f);
        paths[7].lineTo(402.65198f, 786.62177f);
        paths[7].lineTo(394.84375f, 776.451f);
        paths[7].lineTo(382.3197f, 774.11456f);
        paths[7].lineTo(378.4278f, 774.11456f);
        paths[7].lineTo(376.85638f, 788.1794f);
        paths[7].lineTo(369.02216f, 796.0136f);
        paths[7].lineTo(351.83276f, 799.9078f);
        paths[7].lineTo(336.1903f, 783.5064f);
        paths[7].lineTo(330.727f, 780.3453f);
        paths[7].lineTo(326.80988f, 785.84296f);
        paths[7].lineTo(314.31104f, 783.5064f);
        paths[7].lineTo(296.32367f, 783.5064f);
        paths[7].lineTo(296.32367f, 774.11456f);
        paths[7].lineTo(287.716f, 772.5568f);
        paths[7].lineTo(276.76416f, 766.28033f);
        paths[7].lineTo(269.72864f, 762.38617f);
        paths[7].lineTo(261.9204f, 758.4461f);
        paths[7].lineTo(258.0033f, 763.165f);
        paths[7].lineTo(265.03802f, 771.778f);
        paths[7].lineTo(265.03802f, 780.3453f);
        paths[7].lineTo(254.11137f, 788.1794f);
        paths[7].lineTo(250.19429f, 793.6313f);
        paths[7].lineTo(250.19429f, 799.9078f);

// 홍천
        paths[8] = new Path();
        paths[8].moveTo(435.48383f, 830.42f);
        paths[8].lineTo(433.93762f, 836.6507f);
        paths[8].lineTo(431.5927f, 838.98724f);
        paths[8].lineTo(431.5927f, 846.04254f);
        paths[8].lineTo(426.10345f, 849.1579f);
        paths[8].lineTo(426.10345f, 856.2133f);
        paths[8].lineTo(426.10345f, 862.44403f);
        paths[8].lineTo(418.29517f, 864.7806f);
        paths[8].lineTo(416.72302f, 875.73004f);
        paths[8].lineTo(412.0324f, 884.3431f);
        paths[8].lineTo(408.1153f, 887.45844f);
        paths[8].lineTo(397.18866f, 877.33356f);
        paths[8].lineTo(390.92664f, 870.27826f);
        paths[8].lineTo(386.23682f, 878.11237f);
        paths[8].lineTo(386.23682f, 884.3431f);
        paths[8].lineTo(372.93927f, 882.00665f);
        paths[8].lineTo(365.13025f, 880.4489f);
        paths[8].lineTo(362.78534f, 882.00665f);
        paths[8].lineTo(365.13025f, 889.0162f);
        paths[8].lineTo(369.02216f, 896.0715f);
        paths[8].lineTo(367.47595f, 901.56915f);
        paths[8].lineTo(372.93927f, 909.3576f);
        paths[8].lineTo(377.6291f, 915.6341f);
        paths[8].lineTo(376.85638f, 920.3072f);
        paths[8].lineTo(371.39307f, 924.20135f);
        paths[8].lineTo(372.93927f, 930.47784f);
        paths[8].lineTo(369.02216f, 932.8144f);
        paths[8].lineTo(362.78534f, 928.9202f);
        paths[8].lineTo(357.29608f, 932.8144f);
        paths[8].lineTo(349.48785f, 932.8144f);
        paths[8].lineTo(333.8454f, 932.8144f);
        paths[8].lineTo(308.049f, 950.04047f);
        paths[8].lineTo(303.3592f, 956.2712f);
        paths[8].lineTo(293.20526f, 966.44183f);
        paths[8].lineTo(286.94324f, 956.2712f);
        paths[8].lineTo(279.90778f, 970.3361f);
        paths[8].lineTo(276.76416f, 954.71344f);
        paths[8].lineTo(272.87225f, 952.377f);
        paths[8].lineTo(267.38373f, 944.5428f);
        paths[8].lineTo(259.5747f, 940.6486f);
        paths[8].lineTo(258.0033f, 942.98505f);
        paths[8].lineTo(254.11137f, 975.8338f);
        paths[8].lineTo(247.84938f, 982.8433f);
        paths[8].lineTo(257.2298f, 991.45636f);
        paths[8].lineTo(273.64575f, 979.72797f);
        paths[8].lineTo(322.12006f, 1014.13434f);
        paths[8].lineTo(345.57074f, 1012.5766f);
        paths[8].lineTo(355.74982f, 1017.2497f);
        paths[8].lineTo(373.73798f, 1032.918f);
        paths[8].lineTo(386.23682f, 1028.1992f);
        paths[8].lineTo(394.84375f, 1010.2402f);
        paths[8].lineTo(415.94952f, 1003.18475f);
        paths[8].lineTo(427.6756f, 987.5622f);
        paths[8].lineTo(448.00787f, 997.73285f);
        paths[8].lineTo(478.4948f, 979.72797f);
        paths[8].lineTo(483.18463f, 977.3914f);
        paths[8].lineTo(486.30304f, 964.1054f);
        paths[8].lineTo(500.3993f, 966.44183f);
        paths[8].lineTo(513.67084f, 977.3914f);
        paths[8].lineTo(523.8507f, 982.0645f);
        paths[8].lineTo(535.57605f, 989.1198f);
        paths[8].lineTo(545.72925f, 989.89874f);
        paths[8].lineTo(565.28955f, 953.93463f);
        paths[8].lineTo(570.75287f, 956.2712f);
        paths[8].lineTo(591.85864f, 952.377f);
        paths[8].lineTo(617.6802f, 958.6077f);
        paths[8].lineTo(625.4892f, 939.8697f);
        paths[8].lineTo(635.64233f, 935.92975f);
        paths[8].lineTo(651.2848f, 936.75433f);
        paths[8].lineTo(655.9754f, 914.8553f);
        paths[8].lineTo(670.04565f, 892.9561f);
        paths[8].lineTo(677.8798f, 889.84076f);
        paths[8].lineTo(678.6533f, 869.4993f);
        paths[8].lineTo(660.6652f, 852.27325f);
        paths[8].lineTo(650.5121f, 858.54987f);
        paths[8].lineTo(634.8696f, 850.71564f);
        paths[8].lineTo(616.10876f, 865.60516f);
        paths[8].lineTo(612.19165f, 858.54987f);
        paths[8].lineTo(589.51373f, 876.509f);
        paths[8].lineTo(589.51373f, 885.9008f);
        paths[8].lineTo(577.0149f, 885.9008f);
        paths[8].lineTo(566.06226f, 878.11237f);
        paths[8].lineTo(559.02673f, 866.384f);
        paths[8].lineTo(544.9565f, 871.8359f);
        paths[8].lineTo(542.6116f, 883.5643f);
        paths[8].lineTo(531.659f, 889.84076f);
        paths[8].lineTo(526.1957f, 892.1773f);
        paths[8].lineTo(508.20752f, 875.73004f);
        paths[8].lineTo(509.77972f, 862.44403f);
        paths[8].lineTo(495.70947f, 853.0522f);
        paths[8].lineTo(480.06622f, 850.71564f);
        paths[8].lineTo(455.0434f, 838.98724f);
        paths[8].lineTo(447.20917f, 829.5953f);
        paths[8].lineTo(433.13892f, 831.1988f);

// 횡성
        paths[9] = new Path();
        paths[9].moveTo(375.28418f, 1032.918f);
        paths[9].lineTo(373.73798f, 1038.3699f);
        paths[9].lineTo(361.9867f, 1038.3699f);
        paths[9].lineTo(341.67957f, 1056.3748f);
        paths[9].lineTo(341.67957f, 1063.3845f);
        paths[9].lineTo(350.26135f, 1067.3245f);
        paths[9].lineTo(358.09552f, 1072.7764f);
        paths[9].lineTo(358.09552f, 1088.3989f);
        paths[9].lineTo(366.6765f, 1087.6201f);
        paths[9].lineTo(380.7735f, 1111.0769f);
        paths[9].lineTo(384.6646f, 1111.0769f);
        paths[9].lineTo(388.58173f, 1076.6704f);
        paths[9].lineTo(400.30707f, 1075.1128f);
        paths[9].lineTo(408.91473f, 1056.3748f);
        paths[9].lineTo(414.3781f, 1056.3748f);
        paths[9].lineTo(418.29517f, 1079.8315f);
        paths[9].lineTo(430.0205f, 1080.6106f);
        paths[9].lineTo(443.31805f, 1080.6106f);
        paths[9].lineTo(448.00787f, 1087.6201f);
        paths[9].lineTo(465.19733f, 1087.6201f);
        paths[9].lineTo(479.26758f, 1106.4038f);
        paths[9].lineTo(472.23206f, 1111.8557f);
        paths[9].lineTo(472.23206f, 1124.3628f);
        paths[9].lineTo(459.734f, 1127.5242f);
        paths[9].lineTo(456.6156f, 1146.262f);
        paths[9].lineTo(486.30304f, 1161.9304f);
        paths[9].lineTo(492.56506f, 1147.8198f);
        paths[9].lineTo(510.55322f, 1147.8198f);
        paths[9].lineTo(511.32593f, 1147.8198f);
        paths[9].lineTo(507.4348f, 1140.0312f);
        paths[9].lineTo(515.24304f, 1140.0312f);
        paths[9].lineTo(513.67084f, 1115.7957f);
        paths[9].lineTo(537.14825f, 1114.238f);
        paths[9].lineTo(537.921f, 1114.238f);
        paths[9].lineTo(548.8736f, 1104.8462f);
        paths[9].lineTo(539.49316f, 1071.9976f);
        paths[9].lineTo(541.0394f, 1071.9976f);
        paths[9].lineTo(562.14514f, 1048.5408f);
        paths[9].lineTo(558.254f, 1016.4709f);
        paths[9].lineTo(555.1097f, 997.73285f);
        paths[9].lineTo(542.6116f, 993.0141f);
        paths[9].lineTo(522.27856f, 979.72797f);
        paths[9].lineTo(512.12463f, 982.0645f);
        paths[9].lineTo(500.3993f, 974.27606f);
        paths[9].lineTo(491.01886f, 965.663f);
        paths[9].lineTo(483.18463f, 978.1702f);
        paths[9].lineTo(470.68585f, 981.2856f);
        paths[9].lineTo(445.66296f, 997.73285f);
        paths[9].lineTo(444.09076f, 994.5717f);
        paths[9].lineTo(428.44836f, 989.89874f);
        paths[9].lineTo(418.29517f, 999.68f);
        paths[9].lineTo(418.29517f, 999.2906f);
        paths[9].lineTo(401.08057f, 1006.3001f);
        paths[9].lineTo(394.84375f, 1021.18964f);
        paths[9].lineTo(379.2013f, 1030.5356f);

// 평창
        paths[10] = new Path();
        paths[10].moveTo(676.3084f, 889.0162f);
        paths[10].lineTo(672.39136f, 895.29266f);
        paths[10].lineTo(666.1545f, 893.73505f);
        paths[10].lineTo(659.119f, 904.6845f);
        paths[10].lineTo(655.9754f, 920.3072f);
        paths[10].lineTo(655.9754f, 932.8144f);
        paths[10].lineTo(641.13165f, 932.8144f);
        paths[10].lineTo(629.4063f, 935.92975f);
        paths[10].lineTo(619.22644f, 953.93463f);
        paths[10].lineTo(609.846f, 956.2712f);
        paths[10].lineTo(594.20355f, 954.71344f);
        paths[10].lineTo(582.4782f, 954.71344f);
        paths[10].lineTo(570.75287f, 955.4924f);
        paths[10].lineTo(566.83575f, 950.04047f);
        paths[10].lineTo(555.1097f, 967.22076f);
        paths[10].lineTo(550.4198f, 986.7834f);
        paths[10].lineTo(544.9565f, 990.67755f);
        paths[10].lineTo(555.1097f, 993.0141f);
        paths[10].lineTo(555.1097f, 1001.62714f);
        paths[10].lineTo(556.6818f, 1010.2402f);
        paths[10].lineTo(560.59894f, 1032.1392f);
        paths[10].lineTo(565.28955f, 1047.7617f);
        paths[10].lineTo(543.38434f, 1064.1633f);
        paths[10].lineTo(548.8736f, 1104.8462f);
        paths[10].lineTo(557.4553f, 1104.8462f);
        paths[10].lineTo(566.83575f, 1116.5745f);
        paths[10].lineTo(574.67f, 1118.1323f);
        paths[10].lineTo(574.67f, 1135.3125f);
        paths[10].lineTo(577.0149f, 1147.8198f);
        paths[10].lineTo(595.77576f, 1165.0459f);
        paths[10].lineTo(608.2746f, 1161.1516f);
        paths[10].lineTo(613.7631f, 1137.6948f);
        paths[10].lineTo(627.0606f, 1140.0312f);
        paths[10].lineTo(633.2974f, 1147.8198f);
        paths[10].lineTo(629.4063f, 1159.5481f);
        paths[10].lineTo(641.13165f, 1154.0962f);
        paths[10].lineTo(648.9399f, 1161.9304f);
        paths[10].lineTo(671.61786f, 1165.0459f);
        paths[10].lineTo(682.57043f, 1169.7188f);
        paths[10].lineTo(687.26025f, 1165.0459f);
        paths[10].lineTo(680.2255f, 1154.0962f);
        paths[10].lineTo(673.96277f, 1136.0913f);
        paths[10].lineTo(668.49945f, 1126.7454f);
        paths[10].lineTo(668.49945f, 1118.1323f);
        paths[10].lineTo(660.6652f, 1106.4038f);
        paths[10].lineTo(651.2848f, 1103.2883f);
        paths[10].lineTo(652.857f, 1090.7812f);
        paths[10].lineTo(652.857f, 1079.0527f);
        paths[10].lineTo(658.3203f, 1076.6704f);
        paths[10].lineTo(667.70074f, 1079.0527f);
        paths[10].lineTo(670.04565f, 1057.1538f);
        paths[10].lineTo(677.8798f, 1059.4902f);
        paths[10].lineTo(677.8798f, 1043.8677f);
        paths[10].lineTo(680.2255f, 1025.084f);
        paths[10].lineTo(688.03375f, 1018.0285f);
        paths[10].lineTo(694.2958f, 1017.2497f);
        paths[10].lineTo(699.7591f, 1030.5356f);
        paths[10].lineTo(707.5933f, 1026.6416f);
        paths[10].lineTo(716.97375f, 1025.084f);
        paths[10].lineTo(715.40155f, 1014.91315f);
        paths[10].lineTo(719.31866f, 1017.2497f);
        paths[10].lineTo(718.51996f, 1008.68243f);
        paths[10].lineTo(723.2106f, 1005.5213f);
        paths[10].lineTo(732.6162f, 1007.0789f);
        paths[10].lineTo(734.96185f, 996.95404f);
        paths[10].lineTo(740.4252f, 996.95404f);
        paths[10].lineTo(741.19794f, 975.055f);
        paths[10].lineTo(752.9233f, 967.22076f);
        paths[10].lineTo(744.3423f, 928.1413f);
        paths[10].lineTo(727.1277f, 914.8553f);
        paths[10].lineTo(710.71173f, 914.8553f);
        paths[10].lineTo(698.9864f, 908.5788f);
        paths[10].lineTo(686.4616f, 915.6341f);
        paths[10].lineTo(678.6533f, 889.84076f);

// 강릉
        paths[11] = new Path();
        paths[11].moveTo(678.6533f, 866.384f);
        paths[11].lineTo(676.3084f, 884.3431f);
        paths[11].lineTo(682.57043f, 893.73505f);
        paths[11].lineTo(685.68884f, 914.8553f);
        paths[11].lineTo(698.2129f, 908.5788f);
        paths[11].lineTo(714.62885f, 915.6341f);
        paths[11].lineTo(724.782f, 913.29755f);
        paths[11].lineTo(744.3423f, 928.9202f);
        paths[11].lineTo(750.57837f, 962.54767f);
        paths[11].lineTo(741.19794f, 974.27606f);
        paths[11].lineTo(742.7701f, 997.73285f);
        paths[11].lineTo(733.38965f, 996.95404f);
        paths[11].lineTo(733.38965f, 1008.68243f);
        paths[11].lineTo(721.6643f, 1005.5213f);
        paths[11].lineTo(718.51996f, 1018.8073f);
        paths[11].lineTo(736.5081f, 1037.5911f);
        paths[11].lineTo(745.115f, 1026.6416f);
        paths[11].lineTo(762.3037f, 1028.978f);
        paths[11].lineTo(774.055f, 1048.5408f);
        paths[11].lineTo(788.89874f, 1044.6465f);
        paths[11].lineTo(795.93427f, 1012.5766f);
        paths[11].lineTo(805.3147f, 1004.7425f);
        paths[11].lineTo(812.35016f, 1025.8628f);
        paths[11].lineTo(823.3028f, 1029.7568f);
        paths[11].lineTo(835.02814f, 1028.1992f);
        paths[11].lineTo(839.71796f, 1036.8123f);
        paths[11].lineTo(850.6706f, 1028.978f);
        paths[11].lineTo(861.62317f, 1029.7568f);
        paths[11].lineTo(870.20496f, 1025.084f);
        paths[11].lineTo(874.122f, 1002.40594f);
        paths[11].lineTo(881.1568f, 998.5118f);
        paths[11].lineTo(867.08655f, 977.3914f);
        paths[11].lineTo(874.122f, 961.76886f);
        paths[11].lineTo(772.4836f, 841.32367f);
        paths[11].lineTo(713.83014f, 853.0522f);
        paths[11].lineTo(698.2129f, 852.27325f);
        paths[11].lineTo(678.6533f, 865.60516f);

// 원주
        paths[12] = new Path();
        paths[12].moveTo(355.74982f, 1090.7812f);
        paths[12].lineTo(351.83276f, 1106.4038f);
        paths[12].lineTo(347.14294f, 1125.9663f);
        paths[12].lineTo(341.67957f, 1125.9663f);
        paths[12].lineTo(345.57074f, 1159.5481f);
        paths[12].lineTo(337.7625f, 1161.1516f);
        paths[12].lineTo(343.22583f, 1176.7742f);
        paths[12].lineTo(336.1903f, 1205.6829f);
        paths[12].lineTo(345.57074f, 1235.4163f);
        paths[12].lineTo(358.8683f, 1241.647f);
        paths[12].lineTo(383.1184f, 1235.4163f);
        paths[12].lineTo(392.49884f, 1233.0796f);
        paths[12].lineTo(398.73486f, 1236.9739f);
        paths[12].lineTo(404.22418f, 1228.3608f);
        paths[12].lineTo(410.4862f, 1229.9185f);
        paths[12].lineTo(409.6875f, 1201.7886f);
        paths[12].lineTo(428.44836f, 1185.3872f);
        paths[12].lineTo(445.66296f, 1188.5027f);
        paths[12].lineTo(453.4712f, 1202.5674f);
        paths[12].lineTo(453.4712f, 1217.4111f);
        paths[12].lineTo(487.87524f, 1208.844f);
        paths[12].lineTo(495.70947f, 1194.7334f);
        paths[12].lineTo(507.4348f, 1202.5674f);
        paths[12].lineTo(515.24304f, 1193.9543f);
        paths[12].lineTo(527.7419f, 1186.166f);
        paths[12].lineTo(529.3141f, 1169.7188f);
        paths[12].lineTo(519.93365f, 1161.9304f);
        paths[12].lineTo(511.32593f, 1171.2766f);
        paths[12].lineTo(488.648f, 1172.8799f);
        paths[12].lineTo(484.75684f, 1161.1516f);
        paths[12].lineTo(458.9605f, 1154.0962f);
        paths[12].lineTo(457.3883f, 1134.5337f);
        paths[12].lineTo(469.11444f, 1126.7454f);
        paths[12].lineTo(470.68585f, 1110.2981f);
        paths[12].lineTo(478.4948f, 1110.2981f);
        paths[12].lineTo(469.11444f, 1093.1177f);
        paths[12].lineTo(451.925f, 1084.5046f);
        paths[12].lineTo(443.31805f, 1085.2834f);
        paths[12].lineTo(435.48383f, 1079.0527f);
        paths[12].lineTo(424.5572f, 1081.3894f);
        paths[12].lineTo(416.72302f, 1075.1128f);
        paths[12].lineTo(410.4862f, 1063.3845f);
        paths[12].lineTo(412.83185f, 1057.1538f);
        paths[12].lineTo(409.6875f, 1055.596f);
        paths[12].lineTo(401.87927f, 1073.5552f);
        paths[12].lineTo(390.15393f, 1075.1128f);
        paths[12].lineTo(385.46332f, 1088.3989f);
        paths[12].lineTo(388.58173f, 1106.4038f);
        paths[12].lineTo(384.6646f, 1110.2981f);
        paths[12].lineTo(379.2013f, 1110.2981f);
        paths[12].lineTo(373.73798f, 1093.1177f);
        paths[12].lineTo(367.47595f, 1086.8413f);
        paths[12].lineTo(359.64172f, 1088.3989f);

// 영월
        paths[13] = new Path();
        paths[13].moveTo(484.75684f, 1161.1516f);
        paths[13].lineTo(487.87524f, 1173.6589f);
        paths[13].lineTo(513.67084f, 1174.4377f);
        paths[13].lineTo(521.50507f, 1163.488f);
        paths[13].lineTo(526.9684f, 1166.6035f);
        paths[13].lineTo(526.1957f, 1188.5027f);
        paths[13].lineTo(535.57605f, 1196.291f);
        paths[13].lineTo(543.38434f, 1196.291f);
        paths[13].lineTo(550.4198f, 1205.6829f);
        paths[13].lineTo(572.3243f, 1200.231f);
        paths[13].lineTo(577.7876f, 1204.1252f);
        paths[13].lineTo(577.7876f, 1211.9595f);
        paths[13].lineTo(564.4901f, 1219.7478f);
        paths[13].lineTo(564.4901f, 1224.4666f);
        paths[13].lineTo(555.1097f, 1224.4666f);
        paths[13].lineTo(550.4198f, 1231.4761f);
        paths[13].lineTo(565.28955f, 1243.2046f);
        paths[13].lineTo(573.8965f, 1239.3103f);
        paths[13].lineTo(587.96747f, 1233.0796f);
        paths[13].lineTo(593.43085f, 1235.4163f);
        paths[13].lineTo(602.0378f, 1235.4163f);
        paths[13].lineTo(608.2746f, 1247.1445f);
        paths[13].lineTo(612.99036f, 1253.3752f);
        paths[13].lineTo(619.22644f,    1258.873f);
        paths[13].lineTo(624.7157f, 1256.5364f);
        paths[13].lineTo(640.33295f, 1256.5364f);
        paths[13].lineTo(646.595f, 1248.7021f);
        paths[13].lineTo(655.2019f, 1259.6519f);
        paths[13].lineTo(663.0109f, 1259.6519f);
        paths[13].lineTo(666.1545f, 1267.4861f);
        paths[13].lineTo(675.5349f, 1267.4861f);
        paths[13].lineTo(698.9864f, 1268.2649f);
        paths[13].lineTo(734.1624f, 1291.7217f);
        paths[13].lineTo(741.19794f, 1290.1182f);
        paths[13].lineTo(753.7227f, 1294.8369f);
        paths[13].lineTo(756.0676f, 1290.897f);
        paths[13].lineTo(752.9233f, 1276.832f);
        paths[13].lineTo(762.3037f, 1265.1038f);
        paths[13].lineTo(774.055f, 1268.2649f);
        paths[13].lineTo(787.32733f, 1271.3801f);
        paths[13].lineTo(792.81586f, 1280.772f);
        paths[13].lineTo(804.54193f, 1287.0027f);
        paths[13].lineTo(808.4331f, 1283.8875f);
        paths[13].lineTo(813.14886f, 1283.8875f);
        paths[13].lineTo(815.4938f, 1276.0532f);
        paths[13].lineTo(804.54193f, 1275.2744f);
        paths[13].lineTo(809.23175f, 1258.873f);
        paths[13].lineTo(800.6248f, 1252.5964f);
        paths[13].lineTo(808.4331f, 1245.5869f);
        paths[13].lineTo(810.778f, 1233.8584f);
        paths[13].lineTo(806.88684f, 1221.3513f);
        paths[13].lineTo(788.12604f, 1223.6877f);
        paths[13].lineTo(752.9233f, 1204.1252f);
        paths[13].lineTo(730.2453f, 1213.5171f);
        paths[13].lineTo(707.5933f, 1217.4111f);
        paths[13].lineTo(697.4142f, 1204.904f);
        paths[13].lineTo(686.4616f, 1202.5674f);
        paths[13].lineTo(681.7717f, 1198.6733f);
        paths[13].lineTo(673.96277f, 1179.1106f);
        paths[13].lineTo(679.4261f, 1173.6589f);
        paths[13].lineTo(670.04565f, 1165.0459f);
        paths[13].lineTo(659.8925f, 1165.0459f);
        paths[13].lineTo(650.5121f, 1162.7092f);
        paths[13].lineTo(644.25006f, 1157.9905f);
        paths[13].lineTo(634.8696f, 1154.875f);
        paths[13].lineTo(627.8341f, 1141.5891f);
        paths[13].lineTo(613.7631f, 1135.3125f);
        paths[13].lineTo(609.07324f, 1145.4832f);
        paths[13].lineTo(609.07324f, 1161.1516f);
        paths[13].lineTo(599.6928f, 1163.488f);
        paths[13].lineTo(582.4782f, 1151.7598f);
        paths[13].lineTo(576.2414f, 1149.4231f);
        paths[13].lineTo(573.8965f, 1119.69f);
        paths[13].lineTo(559.02673f, 1111.8557f);
        paths[13].lineTo(556.6818f, 1103.2883f);
        paths[13].lineTo(548.8736f, 1104.0674f);
        paths[13].lineTo(545.72925f, 1111.0769f);
        paths[13].lineTo(522.27856f, 1111.8557f);
        paths[13].lineTo(515.24304f, 1118.1323f);
        paths[13].lineTo(515.24304f, 1136.0913f);
        paths[13].lineTo(515.24304f, 1141.5891f);
        paths[13].lineTo(508.20752f, 1140.0312f);
        paths[13].lineTo(511.32593f, 1149.4231f);
        paths[13].lineTo(492.56506f, 1145.4832f);
        paths[13].lineTo(487.10175f, 1157.2117f);

// 태백
        paths[14] = new Path();
        paths[14].moveTo(816.2673f, 1198.6733f);
        paths[14].lineTo(820.1844f, 1231.4761f);
        paths[14].lineTo(812.35016f, 1235.4163f);
        paths[14].lineTo(807.6596f, 1239.3103f);
        paths[14].lineTo(809.23175f, 1248.7021f);
        paths[14].lineTo(800.6248f, 1254.9329f);
        paths[14].lineTo(808.4331f, 1258.873f);
        paths[14].lineTo(805.3147f, 1271.3801f);
        paths[14].lineTo(807.6596f, 1278.3896f);
        paths[14].lineTo(815.4938f, 1278.3896f);
        paths[14].lineTo(826.4205f, 1264.3247f);
        paths[14].lineTo(834.25464f, 1262.7671f);
        paths[14].lineTo(835.8009f, 1269.0437f);
        paths[14].lineTo(851.4441f, 1264.3247f);
        paths[14].lineTo(878.8119f, 1274.4956f);
        paths[14].lineTo(885.84735f, 1263.5459f);
        paths[14].lineTo(894.4543f, 1255.7576f);
        paths[14].lineTo(891.3107f, 1233.8584f);
        paths[14].lineTo(885.84735f, 1223.6877f);
        paths[14].lineTo(866.31305f, 1219.7478f);
        paths[14].lineTo(857.7061f, 1208.0193f);
        paths[14].lineTo(855.3612f, 1198.6733f);
        paths[14].lineTo(858.4788f, 1196.291f);
        paths[14].lineTo(855.3612f, 1190.8391f);
        paths[14].lineTo(847.527f, 1188.5027f);
        paths[14].lineTo(857.7061f, 1178.3318f);
        paths[14].lineTo(855.3612f, 1171.2766f);
        paths[14].lineTo(861.62317f, 1167.3823f);
        paths[14].lineTo(854.5877f, 1158.7693f);
        paths[14].lineTo(861.62317f, 1153.3174f);
        paths[14].lineTo(854.5877f, 1145.4832f);
        paths[14].lineTo(855.3612f, 1133.7549f);
        paths[14].lineTo(854.5877f, 1129.8606f);
        paths[14].lineTo(850.6706f, 1135.3125f);
        paths[14].lineTo(838.94525f, 1135.3125f);
        paths[14].lineTo(835.02814f, 1150.2021f);
        paths[14].lineTo(831.111f, 1154.0962f);
        paths[14].lineTo(827.9926f, 1165.0459f);
        paths[14].lineTo(823.3028f, 1168.94f);
        paths[14].lineTo(827.2199f, 1182.2261f);
        paths[14].lineTo(822.5293f, 1185.3872f);
        paths[14].lineTo(816.2673f, 1197.8945f);

// 삼척
        paths[15] = new Path();
        paths[15].moveTo(832.6832f, 1076.6704f);
        paths[15].lineTo(818.6122f, 1079.0527f);
        paths[15].lineTo(811.5767f, 1083.7258f);
        paths[15].lineTo(809.23175f, 1097.012f);
        paths[15].lineTo(799.8514f, 1103.2883f);
        paths[15].lineTo(795.1615f, 1115.0168f);
        paths[15].lineTo(792.01715f, 1133.7549f);
        paths[15].lineTo(801.3976f, 1139.2524f);
        paths[15].lineTo(799.8514f, 1147.8198f);
        paths[15].lineTo(796.70776f, 1154.0962f);
        paths[15].lineTo(803.7425f, 1165.0459f);
        paths[15].lineTo(808.4331f, 1169.7188f);
        paths[15].lineTo(803.7425f, 1175.2166f);
        paths[15].lineTo(811.5767f, 1179.1106f);
        paths[15].lineTo(815.4938f, 1193.1755f);
        paths[15].lineTo(819.3857f, 1204.1252f);
        paths[15].lineTo(819.3857f, 1188.5027f);
        paths[15].lineTo(827.9926f, 1184.6084f);
        paths[15].lineTo(823.3028f, 1169.7188f);
        paths[15].lineTo(831.111f, 1161.1516f);
        paths[15].lineTo(831.111f, 1154.0962f);
        paths[15].lineTo(838.94525f, 1138.4736f);
        paths[15].lineTo(850.6706f, 1136.0913f);
        paths[15].lineTo(857.7061f, 1130.6394f);
        paths[15].lineTo(857.7061f, 1135.3125f);
        paths[15].lineTo(850.6706f, 1143.1467f);
        paths[15].lineTo(859.27826f, 1149.4231f);
        paths[15].lineTo(855.3612f, 1157.9905f);
        paths[15].lineTo(858.4788f, 1166.6035f);
        paths[15].lineTo(855.3612f, 1175.2166f);
        paths[15].lineTo(851.4441f, 1184.6084f);
        paths[15].lineTo(857.7061f, 1186.9448f);
        paths[15].lineTo(856.1339f, 1202.5674f);
        paths[15].lineTo(867.85925f, 1217.4111f);
        paths[15].lineTo(885.84735f, 1221.3513f);
        paths[15].lineTo(893.6816f, 1235.4163f);
        paths[15].lineTo(892.8829f, 1253.3752f);
        paths[15].lineTo(896.8f, 1256.5364f);
        paths[15].lineTo(909.29803f, 1269.0437f);
        paths[15].lineTo(933.5482f, 1283.8875f);
        paths[15].lineTo(949.1906f, 1283.8875f);
        paths[15].lineTo(949.9641f, 1272.938f);
        paths[15].lineTo(964.80786f, 1255.7576f);
        paths[15].lineTo(968.725f, 1247.1445f);
        paths[15].lineTo(977.3319f, 1248.7021f);
        paths[15].lineTo(983.5947f, 1241.647f);
        paths[15].lineTo(995.32007f, 1232.3008f);
        paths[15].lineTo(1004.70044f, 1231.4761f);
        paths[15].lineTo(999.2112f, 1219.7478f);
        paths[15].lineTo(999.2112f, 1201.7886f);
        paths[15].lineTo(996.0928f, 1192.3967f);
        paths[15].lineTo(999.2112f, 1186.166f);
        paths[15].lineTo(990.6294f, 1172.8799f);
        paths[15].lineTo(990.6294f, 1162.7092f);
        paths[15].lineTo(979.6776f, 1157.2117f);
        paths[15].lineTo(978.9041f, 1149.4231f);
        paths[15].lineTo(964.03436f, 1141.5891f);
        paths[15].lineTo(956.99963f, 1119.69f);
        paths[15].lineTo(956.99963f, 1115.0168f);
        paths[15].lineTo(934.3217f, 1094.6753f);
        paths[15].lineTo(931.976f, 1080.6106f);
        paths[15].lineTo(924.1678f, 1068.1033f);
        paths[15].lineTo(917.13226f, 1063.3845f);
        paths[15].lineTo(908.5253f, 1072.7764f);
        paths[15].lineTo(894.4543f, 1071.2185f);
        paths[15].lineTo(882.72894f, 1069.6609f);
        paths[15].lineTo(871.77637f, 1071.9976f);
        paths[15].lineTo(865.51434f, 1079.0527f);
        paths[15].lineTo(858.4788f, 1086.8413f);
        paths[15].lineTo(855.3612f, 1090.7812f);
        paths[15].lineTo(835.8009f, 1080.6106f);


// 동해
        paths[16] = new Path();
        paths[16].moveTo(839.71796f, 1036.0334f);
        paths[16].lineTo(835.02814f, 1044.6465f);
        paths[16].lineTo(839.71796f, 1055.596f);
        paths[16].lineTo(846.7535f, 1055.596f);
        paths[16].lineTo(846.7535f, 1059.4902f);
        paths[16].lineTo(840.49146f, 1069.6609f);
        paths[16].lineTo(835.02814f, 1079.0527f);
        paths[16].lineTo(842.83636f, 1085.2834f);
        paths[16].lineTo(854.5877f, 1088.3989f);
        paths[16].lineTo(862.39594f, 1081.3894f);
        paths[16].lineTo(873.3485f, 1071.9976f);
        paths[16].lineTo(891.3107f, 1071.2185f);
        paths[16].lineTo(906.1804f, 1071.2185f);
        paths[16].lineTo(913.21515f, 1068.1033f);
        paths[16].lineTo(920.2507f, 1061.8267f);
        paths[16].lineTo(909.29803f, 1053.2136f);
        paths[16].lineTo(903.062f, 1041.4854f);
        paths[16].lineTo(899.1449f, 1032.1392f);
        paths[16].lineTo(897.5727f, 1022.7474f);
        paths[16].lineTo(898.3714f, 1008.68243f);
        paths[16].lineTo(887.41956f, 1003.18475f);
        paths[16].lineTo(881.9303f, 998.5118f);
        paths[16].lineTo(874.122f, 1000.8482f);
        paths[16].lineTo(873.3485f, 1011.019f);
        paths[16].lineTo(870.20496f, 1024.3049f);
        paths[16].lineTo(859.27826f, 1028.1992f);
        paths[16].lineTo(850.6706f, 1028.978f);
        paths[16].lineTo(842.06366f, 1032.918f);

// 정선
        paths[17] = new Path();
        paths[17].moveTo(717.74725f, 1020.4108f);
        paths[17].lineTo(707.5933f, 1026.6416f);
        paths[17].lineTo(698.9864f, 1032.1392f);
        paths[17].lineTo(695.0693f, 1014.91315f);
        paths[17].lineTo(686.4616f, 1018.8073f);
        paths[17].lineTo(682.57043f, 1024.3049f);
        paths[17].lineTo(682.57043f, 1030.5356f);
        paths[17].lineTo(678.6533f, 1043.8677f);
        paths[17].lineTo(678.6533f, 1060.269f);
        paths[17].lineTo(671.61786f, 1053.2136f);
        paths[17].lineTo(667.70074f, 1063.3845f);
        paths[17].lineTo(670.8451f, 1075.8916f);
        paths[17].lineTo(655.2019f, 1075.8916f);
        paths[17].lineTo(650.5121f, 1079.0527f);
        paths[17].lineTo(652.857f, 1088.3989f);
        paths[17].lineTo(650.5121f, 1097.012f);
        paths[17].lineTo(651.2848f, 1104.0674f);
        paths[17].lineTo(662.2374f, 1107.9614f);
        paths[17].lineTo(666.928f, 1122.8052f);
        paths[17].lineTo(666.928f, 1129.8606f);
        paths[17].lineTo(676.3084f, 1141.5891f);
        paths[17].lineTo(680.2255f, 1154.875f);
        paths[17].lineTo(685.68884f, 1168.94f);
        paths[17].lineTo(677.8798f, 1171.2766f);
        paths[17].lineTo(673.96277f, 1180.6685f);
        paths[17].lineTo(680.2255f, 1192.3967f);
        paths[17].lineTo(681.7717f, 1200.231f);
        paths[17].lineTo(694.2958f, 1200.231f);
        paths[17].lineTo(698.9864f, 1211.9595f);
        paths[17].lineTo(706.0211f, 1215.8535f);
        paths[17].lineTo(723.2106f, 1215.8535f);
        paths[17].lineTo(749.8056f, 1204.1252f);
        paths[17].lineTo(767.79297f, 1208.844f);
        paths[17].lineTo(783.4354f, 1219.7478f);
        paths[17].lineTo(793.58936f, 1224.4666f);
        paths[17].lineTo(807.6596f, 1223.6877f);
        paths[17].lineTo(809.23175f, 1235.4163f);
        paths[17].lineTo(819.3857f, 1233.0796f);
        paths[17].lineTo(817.04004f, 1204.1252f);
        paths[17].lineTo(813.14886f, 1194.7334f);
        paths[17].lineTo(811.5767f, 1181.4473f);
        paths[17].lineTo(806.88684f, 1172.8799f);
        paths[17].lineTo(804.54193f, 1165.8247f);
        paths[17].lineTo(799.8514f, 1161.1516f);
        paths[17].lineTo(799.0527f, 1150.981f);
        paths[17].lineTo(801.3976f, 1141.5891f);
        paths[17].lineTo(795.1615f, 1135.3125f);
        paths[17].lineTo(788.89874f, 1134.5337f);
        paths[17].lineTo(799.8514f, 1106.4038f);
        paths[17].lineTo(807.6596f, 1100.1272f);
        paths[17].lineTo(812.35016f, 1085.2834f);
        paths[17].lineTo(831.111f, 1075.8916f);
        paths[17].lineTo(835.02814f, 1075.8916f);
        paths[17].lineTo(847.527f, 1059.4902f);
        paths[17].lineTo(846.7535f, 1055.596f);
        paths[17].lineTo(838.94525f, 1055.596f);
        paths[17].lineTo(838.94525f, 1049.3196f);
        paths[17].lineTo(835.02814f, 1047.7617f);
        paths[17].lineTo(839.71796f, 1033.697f);
        paths[17].lineTo(835.02814f, 1028.1992f);
        paths[17].lineTo(824.8742f, 1032.1392f);
        paths[17].lineTo(817.04004f, 1025.8628f);
        paths[17].lineTo(811.5767f, 1024.3049f);
        paths[17].lineTo(809.23175f, 1011.019f);
        paths[17].lineTo(803.7425f, 1006.3001f);
        paths[17].lineTo(797.5064f, 1006.3001f);
        paths[17].lineTo(793.58936f, 1018.0285f);
        paths[17].lineTo(793.58936f, 1029.7568f);
        paths[17].lineTo(784.2089f, 1041.4854f);
        paths[17].lineTo(777.9469f, 1049.3196f);
        paths[17].lineTo(770.1379f, 1045.4253f);
        paths[17].lineTo(766.22076f, 1036.0334f);
        paths[17].lineTo(756.84033f, 1030.5356f);
        paths[17].lineTo(750.57837f, 1029.7568f);
        paths[17].lineTo(745.115f, 1028.1992f);
        paths[17].lineTo(741.9966f, 1028.978f);
        paths[17].lineTo(737.30676f, 1036.0334f);
        paths[17].lineTo(732.6162f, 1037.5911f);
        paths[17].lineTo(725.5555f, 1030.5356f);
        paths[17].lineTo(721.6643f, 1024.3049f);
        paths[17].lineTo(716.97375f, 1020.4108f);


        // Region 객체 생성 및 초기화
        for (int i = 0; i < 18; i++) {
            regions[i] = new Region();
            RectF boundsF = new RectF();
            paths[i].computeBounds(boundsF, true);
            Rect bounds = new Rect();
            boundsF.round(bounds);
            regions[i].setPath(paths[i], new Region(bounds));
        }
    }

    private void saveGangwonIsFilledArray() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder stringBuilder = new StringBuilder();
        for (boolean b : Gangwon_isFilled) {
            stringBuilder.append(b).append(",");
        }
        editor.putString(IS_FILLED_KEY, stringBuilder.toString());
        editor.apply();
    }

    private void loadGangwonIsFilledArray() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedString = prefs.getString(IS_FILLED_KEY, null);
        if (savedString != null) {
            String[] split = savedString.split(",");
            for (int i = 0; i < split.length; i++) {
                Gangwon_isFilled[i] = Boolean.parseBoolean(split[i]);
            }
        }
    }

    private void saveSelectedRegion(int index) {
        if (index < 0 || index >= regions.length) {
            return; // Prevent invalid index
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the selected region index as a comma-separated string
        String selectedRegions = prefs.getString(SELECTED_REGIONS_KEY, "");
        HashSet<String> selectedRegionsSet = new HashSet<>(Arrays.asList(selectedRegions.split(",")));

        if (Gangwon_isFilled[index]) {
            selectedRegionsSet.add(String.valueOf(index));
        } else {
            selectedRegionsSet.remove(String.valueOf(index));
        }

        editor.putString(SELECTED_REGIONS_KEY, String.join(",", selectedRegionsSet));
        editor.apply();

        // Save the updated Gangwon_isFilled array
        saveGangwonIsFilledArray();
    }

    private void loadSelectedRegions() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedRegions = prefs.getString(SELECTED_REGIONS_KEY, "");

        if (!selectedRegions.isEmpty()) {
            String[] indices = selectedRegions.split(",");
            for (String index : indices) {
                try {
                    int regionIndex = Integer.parseInt(index);
                    if (regionIndex >= 0 && regionIndex < regions.length) { // Prevent invalid index
                        paint.setColor(Color.GREEN); // 초록색으로 설정
                        canvas.drawPath(paths[regionIndex], paint);
                        Gangwon_isFilled[regionIndex] = true; // Gangwon_isFilled 배열 업데이트
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            imageView.invalidate();
        }
    }

    public static int getSelectedRegionsCount(SharedPreferences prefs) {
        String selectedRegions = prefs.getString(SELECTED_REGIONS_KEY, "");
        if (selectedRegions.isEmpty()) {
            return 0;
        }
        // Filter out any invalid indices by converting to Set and back to List to remove duplicates
        return new HashSet<>(Arrays.asList(selectedRegions.split(","))).size();
    }
}