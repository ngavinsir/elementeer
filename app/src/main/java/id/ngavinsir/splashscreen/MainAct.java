package id.ngavinsir.splashscreen;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class MainAct extends AppCompatActivity {

    private final String API_ACCESS_KEY_URL
            = "http://apilayer.net/api/live?access_key=51cbe33823dfe3f6e5112825d9a88dbd&currencies=USD,IDR,GBP,JPY,KRW,EUR";
    private final double VAL_POUND1 = 453.592;
    private final double VAL_POUND2 = 0.00220462;
    private final double VAL_KG1 = 1000;
    private final double VAL_KG2 = .001;
    private final double VAL_OUNCE1 = 28.3495;
    private final double VAL_OUNCE2 = 0.035274;
    private final double VAL_MPH1 = 1.60934;
    private final double VAL_MPH2 = 0.621371;
    private final double VAL_FPS1 = 1.09728;
    private final double VAL_FPS2 = 0.911344;
    private final double VAL_MPS1 = 3.6;
    private final double VAL_MPS2 = 0.277778;
    private final double VAL_KNOT1 = 1.852;
    private final double VAL_KNOT2 = 0.539957;
    private final double VAL_GAL1 = 0.133681;
    private final double VAL_GAL2 = 7.48052;
    private final double VAL_M31 = 35.3147;
    private final double VAL_M32 = 0.0283168;
    private final double VAL_L1 = 0.0353147;
    private final double VAL_L2 = 28.3168;
    private final double VAL_I31 = 0.000578704;
    private final double VAL_I32 = 1728;
    private final double VAL_M1 = 0.001;
    private final double VAL_M2 = 1000;
    private final double VAL_ML1 = 1.60934;
    private final double VAL_ML2 = 0.621371;
    private final double VAL_Y1 = 0.0009144;
    private final double VAL_Y2 = 1093.61;
    private final double VAL_F1 = 0.0003048;
    private final double VAL_F2 = 3280.84;
    private final double VAL_I1 = 0.0000254;
    private final double VAL_I2 = 39370.1;
    private final double VAL_NM1 = 1.852;
    private final double VAL_NM2 = 0.539957;
    /*private final double VAL_D1 = 111.59;
    private final double VAL_D2 = .008965;
    private final double VAL_IDR1 = 0.008394;
    private final double VAL_IDR2 = 118.99;
    private final double VAL_EU1 = 133.85;
    private final double VAL_EU2 = 0.007471;
    private final double VAL_BR1 = 151.46;
    private final double VAL_BR2 = 0.006602;
    private final double VAL_J1 = 1.00096;
    private final double VAL_J2 = 0.999041;
    private final double VAL_W1 = 0.098904;
    private final double VAL_W2 = 10.11;**/
    private double USD = 1;
    private double IDR = 1;
    private double EUR = 1;
    private double GBP = 1;
    private double JPY = 1;
    private double KRW = 1;

    LinearLayout revealView, buttons;
    RelativeLayout row1, row2;
    boolean flag = true;
    boolean first = true;
    boolean keyOpened = false;
    int state = 0; //0=currency, 1=weight, 2=speed, 3=vol, 4=temp, 5=dist
    int spec1 = 0; //WEIGHT: 0=Kg, 1=Pound, 2=Ounce
    int spec2 = 1; //SPEED: 0=M/H, 1=F/S, 2=M/S, 3=Knot
                   //VOLUME: 0=GAL, 1=M3, 2=Liter, 3=Inch3
                   //TEMP: 0=C, 1=F, 2=K
                   //DISTANCE: 0=METER, 1=MILE, 2=YARD, 3=FOOT, 4=INCH, 5=NAUTICAL MILE
                   //MONEY: 0=USD, 1=IDR, 2=EU, 3=POU, 4=YEN, 5=WON
    ViewPager typee1, typee2, typeweight1, typeweight2, typespeed1, typespeed2, typemoney1, typemoney2, typetemp1, typetemp2,
        typevol1, typevol2, typedist1, typedist2;
    EditText val1, val2;

    float screendens;
    public static float ds;

    int x, y, hypotenuse;

    Handler introHandle;
    Runnable introRun;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currencyAPI();
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        findViewById(R.id.mainmenu).setAlpha(0f);

        findViewById(R.id.mainintro).setVisibility(View.VISIBLE);

        introHandle = new Handler();
        introRun = new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.mainintro).animate().alpha(0f).setDuration(700).start();
                findViewById(R.id.mainintro).animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.menu));

                        setTheme(R.style.AppTheme_NoActionBar);
                        findViewById(R.id.mainintro).setVisibility(View.GONE);
                        findViewById(R.id.mainmenu).animate().alpha(1f).setDuration(400).start();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {}
                    @Override
                    public void onAnimationRepeat(Animator animator) {}
                });
            }
        };
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screendens = metrics.densityDpi;
        ds = getResources().getDisplayMetrics().density;

        TextView tx = (TextView)findViewById(R.id.elementer);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/f_el.ttf");
        tx.setTypeface(custom_font);
        TextView tx1 = (TextView)findViewById(R.id.abtxt1);
        Typeface cf = Typeface.createFromAsset(getAssets(),  "fonts/f_ab.ttf");
        tx1.setTypeface(cf);
        TextView tx2 = (TextView)findViewById(R.id.abtxt2);
        tx2.setTypeface(cf);


        autoHideKey(findViewById(R.id.lay_screen));
        keyDetector();

        revealView = (LinearLayout) findViewById(R.id.reveal);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        row1 = (RelativeLayout) findViewById(R.id.r1);
        row2 = (RelativeLayout) findViewById(R.id.r2);
        val1 = ((EditText)findViewById(R.id.val1));
        val2 = ((EditText)findViewById(R.id.val2));
        setupBut();

        String[] wTitle = {"Kilogram", "Pound", "Ounce"};
        typeweight1 = setupPager(R.id.weight1, new ArrayList<>(Arrays.asList(wTitle)), 1, true);
        typeweight2 = setupPager(R.id.weight2, new ArrayList<>(Arrays.asList(wTitle)), 1, false);

        String[] sTitle = {"Miles/Hour", "Foot/Sec", "Meter/Sec", "Knot"};
        typespeed1 = setupPager(R.id.speed1, new ArrayList<>(Arrays.asList(sTitle)), 2, true);
        typespeed2 = setupPager(R.id.speed2, new ArrayList<>(Arrays.asList(sTitle)), 2, false);

        String[] vTitle = {"Gallon", "Cubic Meter", "Litre", "Cubic Inch"};
        typevol1 = setupPager(R.id.vol1, new ArrayList<>(Arrays.asList(vTitle)), 3, true);
        typevol2 = setupPager(R.id.vol2, new ArrayList<>(Arrays.asList(vTitle)), 3, false);

        String[] tTitle = {"Celcius", "Fahrenheit", "Kelvin"};
        typetemp1 = setupPager(R.id.temp1, new ArrayList<>(Arrays.asList(tTitle)), 4, true);
        typetemp2 = setupPager(R.id.temp2, new ArrayList<>(Arrays.asList(tTitle)), 4, false);

        String[] dTitle = {"Meter", "Mile", "Yard", "Foot", "Inch", "Nautical Mile"};
        typedist1 = setupPager(R.id.dist1, new ArrayList<>(Arrays.asList(dTitle)), 5, true);
        typedist2 = setupPager(R.id.dist2, new ArrayList<>(Arrays.asList(dTitle)), 5, false);

        String[] mTitle = {"US Dollar", "Rupiah", "Euro", "Poundsterling", "Yen", "Won"};
        typemoney1 = setupPager(R.id.money1, new ArrayList<>(Arrays.asList(mTitle)), 0, true);
        typemoney2 = setupPager(R.id.money2, new ArrayList<>(Arrays.asList(mTitle)), 0, false);

        setupCalculator();
        findViewById(R.id.lay_credits).setVisibility(View.VISIBLE);
        findViewById(R.id.lay_credits).setAlpha(0f);

        findViewById(R.id.bt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                findViewById(R.id.lay_credits).animate().alpha(1f).setDuration(700).start();
                findViewById(R.id.bt_about).setEnabled(false);
                findViewById(R.id.close_about).setEnabled(true);
            }
        });

        findViewById(R.id.close_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                findViewById(R.id.lay_credits).animate().alpha(0f).setDuration(700).start();
                findViewById(R.id.close_about).setEnabled(false);
                findViewById(R.id.bt_about).setEnabled(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //introHandle.postDelayed(introRun, 3500L);
    }

    private void currencyAPI()
    {
        RequestQueue q = Volley.newRequestQueue(this);
        /*StringRequest req = new StringRequest(Request.Method.GET, API_ACCESS_KEY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                setCurrencyVal(response);
                introHandle.post(introRun);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });**/
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, API_ACCESS_KEY_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setCurrencyVal(response);
                introHandle.postDelayed(introRun, 700L);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("T", "TIMEOUT");
                introHandle.post(introRun);
            }
        });
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        q.add(req);
    }

    private void setCurrencyVal(JSONObject json)
    {
        //JSONObject j;
        JSONObject job;
        try
        {
            //j = new JSONObject(json);
            //job = j.getJSONObject("quotes");
            job = json.getJSONObject("quotes");
        } catch(Throwable t)
        {
            return;
        }

        //if(j != null && job != null)
        if(job != null)
        {
            try
            {

                IDR = job.getDouble("USDIDR");
                EUR = job.getDouble("USDEUR");
                GBP = job.getDouble("USDGBP");
                JPY = job.getDouble("USDJPY");
                KRW = job.getDouble("USDKRW");
            }catch(Throwable t)
            {

            }
        }
    }

    public void setupCalculator()
    {
        val1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                hideKeyboard(MainAct.this);
                double ogValue = Double.parseDouble(textView.getText() + "");
                if(state == 4)
                {
                    double finalValue = calcTemp(ogValue, true);
                    finalValue = round(finalValue, 3);
                    if(finalValue %1 == 0) val2.setText((int) finalValue + "");
                    else val2.setText(Double.toString(finalValue));
                    return false;
                }
                double cons1 = getCons1();
                double cons2 = getCons2();

                double finalValue = ogValue * cons1 * cons2;
                finalValue = round(finalValue, 3);
                if(finalValue %1 == 0) val2.setText((int) finalValue + "");
                else val2.setText(Double.toString(finalValue));
                return false;
            }
        });
        val2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                hideKeyboard(MainAct.this);
                double ogValue = Double.parseDouble(textView.getText() + "");
                if(state == 4)
                {
                    double finalValue = calcTemp(ogValue, true);
                    finalValue = round(finalValue, 3);
                    if(finalValue %1 == 0) val1.setText((int) finalValue + "");
                    else val1.setText(Double.toString(finalValue));
                    return false;
                }
                double cons1 = getCons1();
                double cons2 = getCons2();

                double finalValue = ogValue / cons2 / cons1;
                finalValue = round(finalValue, 3);
                if(finalValue %1 == 0) val1.setText((int) finalValue + "");
                else val1.setText(Double.toString(finalValue));
                return false;
            }
        });
    }

    public ViewPager setupPager(int id, ArrayList<String> ss, final int st, final boolean first)
    {
        final ViewPager vp = (ViewPager) findViewById(id);
        vp.setVisibility(View.INVISIBLE);
        WeightAdapter wa = new WeightAdapter(getSupportFragmentManager());
        int center = ss.size()/2;
        for (int i = 0; i < ss.size(); i++)
        {
            wa.addFragment(TypeFragment.newInstance(this, ss.get(i), i == center ? 1f : .7f));
        }
        vp.setAdapter(wa);
        vp.setPageTransformer(false, wa);
        vp.setOffscreenPageLimit(5);
        vp.setCurrentItem(first ? spec1 : spec2);
        vp.setPageMargin(-20-((int)(screendens * (.7f + (ds/6.7)))));

        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(st != state) return true;
                return false;
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position)
            {
                TextView t = ((WeightAdapter)vp.getAdapter()).getF(position).getView().findViewById(R.id.txt);
                setSpec(t.getText() + "", first);
                if(first)
                {
                    double ogValue = Double.parseDouble(val1.getText() + "");
                    if(state == 4)
                    {
                        double finalValue = calcTemp(ogValue, true);
                        finalValue = round(finalValue, 3);
                        if(finalValue %1 == 0) val2.setText((int) finalValue + "");
                        else val2.setText(Double.toString(finalValue));
                        return;
                    }
                    double cons1 = getCons1();
                    double cons2 = getCons2();

                    double finalValue = ogValue * cons1 * cons2;
                    finalValue = round(finalValue, 3);
                    if(finalValue%1 == 0) val2.setText((int) finalValue + "");
                    else val2.setText(Double.toString(finalValue));
                } else
                {
                    double ogValue = Double.parseDouble(val2.getText() + "");
                    if(state == 4)
                    {
                        double finalValue = calcTemp(ogValue, true);
                        finalValue = round(finalValue, 3);
                        if(finalValue %1 == 0) val1.setText((int) finalValue + "");
                        else val1.setText(Double.toString(finalValue));
                        return;
                    }
                    double cons1 = getCons1();
                    double cons2 = getCons2();

                    double finalValue = ogValue / cons2 / cons1;
                    finalValue = round(finalValue, 3);
                    if(finalValue %1 == 0) val1.setText((int) finalValue + "");
                    else val1.setText(Double.toString(finalValue));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        return vp;
    }

    public void closeConvert(View v)
    {
        if(flag)
        {
            return;
        }
        v.setEnabled(false);
        findViewById(R.id.cat).setTranslationX(-screendens-(findViewById(R.id.cat).getWidth()/2));
        findViewById(R.id.cat).animate().alpha(1f).translationX(0).setDuration(400).start();
        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
        anim.setDuration(400);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                enBut();
                revealView.setVisibility(View.GONE);
                typee1.setCurrentItem(0);
                typee2.setCurrentItem(1);
                val1.setText(1 + "");
            }
        });

        anim.start();
        flag = true;
    }

    public void openConvert(View v)
    {
        if(!flag)
        {
            return;
        }
        setupConverter(v);
        findViewById(R.id.cat).animate().alpha(0f).translationX(screendens+(findViewById(R.id.cat).getWidth()/2)).setDuration(500).start();
        x = (int) v.getX() + v.getWidth()/2;
        y = (int) v.getY() + v.getHeight()/2;
        float dpp = getResources().getDisplayMetrics().density;
        if(v.getId() == R.id.bt2 || v.getId() == R.id.bt4 || v.getId() == R.id.bt6)
        {
            x = (int) v.getX() + (int)(53*dpp) + v.getWidth()*13/8;
            y = (int) v.getY() + v.getHeight()/2;
        }
        hypotenuse = (int) Math.hypot(revealView.getWidth(), revealView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
        anim.setDuration(first ? 1 : 500);
        first = first ? false : false;

        revealView.setVisibility(View.VISIBLE);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {disBut();}
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {}
        });
        anim.start();

        flag = false;
    }

    private void setupConverter(View v)
    {
        TextView ct = (TextView) findViewById(R.id.c_title);
        (findViewById(R.id.closeConvert)).setEnabled(true);
        switch(v.getId())
        {
            case R.id.bt5:
                revealView.setBackgroundResource(R.color.green);
                ct.setText("Currency");
                ct.setTextColor(Color.parseColor("#1b5e20"));
                setValColor("#ffee58");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typemoney1.setVisibility(View.VISIBLE);
                typemoney2.setVisibility(View.VISIBLE);
                typee1 = typemoney1;
                typee2 = typemoney2;
                state = 0;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
            case R.id.bt1:
                revealView.setBackgroundResource(R.color.colorPrimary);
                ct.setText("Weight");
                ct.setTextColor(Color.parseColor("#B71C1C"));
                setValColor("#ffee58");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typeweight1.setVisibility(View.VISIBLE);
                typeweight2.setVisibility(View.VISIBLE);
                typee1 = typeweight1;
                typee2 = typeweight2;
                state = 1;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
            case R.id.bt3:
                revealView.setBackgroundResource(R.color.blue);
                ct.setText("Speed");
                ct.setTextColor(Color.parseColor("#01579B"));
                setValColor("#ffee58");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typespeed1.setVisibility(View.VISIBLE);
                typespeed2.setVisibility(View.VISIBLE);
                typee1 = typespeed1;
                typee2 = typespeed2;
                state = 2;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
            case R.id.bt6:
                revealView.setBackgroundResource(R.color.yellow);
                ct.setText("Volume");
                ct.setTextColor(Color.parseColor("#F57F17"));
                setValColor("#1B5E20");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typevol1.setVisibility(View.VISIBLE);
                typevol2.setVisibility(View.VISIBLE);
                typee1 = typevol1;
                typee2 = typevol2;
                state = 3;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
            case R.id.bt4:
                revealView.setBackgroundResource(R.color.cardview_light_background);
                ct.setText("Temperature");
                ct.setTextColor(Color.parseColor("#000000"));
                setValColor("#000000");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typetemp1.setVisibility(View.VISIBLE);
                typetemp2.setVisibility(View.VISIBLE);
                typee1 = typetemp1;
                typee2 = typetemp2;
                state = 4;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
            case R.id.bt2:
                revealView.setBackgroundResource(R.color.brown);
                ct.setText("Distance");
                ct.setTextColor(Color.parseColor("#3E2723"));
                setValColor("#ffee58");
                if(typee1 != null && typee2 != null)
                {
                    typee1.setVisibility(View.INVISIBLE);
                    typee2.setVisibility(View.INVISIBLE);
                }
                typedist1.setVisibility(View.VISIBLE);
                typedist2.setVisibility(View.VISIBLE);
                typee1 = typedist1;
                typee2 = typedist2;
                state = 5;
                spec1 = 0;
                spec2 = 1;
                calculateFirst();
                break;
        }
    }

    public void calculateFirst()
    {
        double ogValue = Double.parseDouble(val1.getText() + "");
        if(state == 4)
        {
            double finalValue = calcTemp(ogValue, true);
            finalValue = round(finalValue, 3);
            if(finalValue %1 == 0) val2.setText((int) finalValue + "");
            else val2.setText(Double.toString(finalValue));
            return;
        }
        double cons1 = getCons1();
        double cons2 = getCons2();

        double finalValue = ogValue * cons1 * cons2;
        finalValue = round(finalValue, 3);
        if(finalValue%1 == 0) val2.setText((int) finalValue + "");
        else val2.setText(Double.toString(finalValue));
    }

    public double getCons1()
    {
        switch (state) {
            case 0:
                switch (spec1) {
                    case 0:
                        return 1/USD;
                    case 1:
                        return 1/IDR;
                    case 2:
                        return 1/EUR;
                    case 3:
                        return 1/GBP;
                    case 4:
                        return 1/JPY;
                    case 5:
                        return 1/KRW;
                }
                break;
            case 1:
                switch (spec1) {
                    case 0:
                        return VAL_KG1;
                    case 1:
                        return VAL_POUND1;
                    case 2:
                        return VAL_OUNCE1;
                }
                break;
            case 2:
                switch (spec1) {
                    case 0:
                        return VAL_MPH1;
                    case 1:
                        return VAL_FPS1;
                    case 2:
                        return VAL_MPS1;
                    case 3:
                        return VAL_KNOT1;
                }
                break;
            case 3:
                switch (spec1) {
                    case 0:
                        return VAL_GAL1;
                    case 1:
                        return VAL_M31;
                    case 2:
                        return VAL_L1;
                    case 3:
                        return VAL_I31;
                }
                break;
            case 5:
                switch (spec1) {
                    case 0:
                        return VAL_M1;
                    case 1:
                        return VAL_ML1;
                    case 2:
                        return VAL_Y1;
                    case 3:
                        return VAL_F1;
                    case 4:
                        return VAL_I1;
                    case 5:
                        return VAL_NM1;
                }
                break;
        }
        return 1;
    }

    public double getCons2()
    {
        switch (state)
        {
            case 0:
                switch (spec2) {
                    case 0:
                        return USD;
                    case 1:
                        return IDR;
                    case 2:
                        return EUR;
                    case 3:
                        return GBP;
                    case 4:
                        return JPY;
                    case 5:
                        return KRW;
                }
                break;
            case 1:
                switch (spec2)
                {
                    case 0:
                        return VAL_KG2;
                    case 1:
                        return VAL_POUND2;
                    case 2:
                        return VAL_OUNCE2;
                }
                break;
            case 2:
                switch (spec2) {
                    case 0:
                        return VAL_MPH2;
                    case 1:
                        return VAL_FPS2;
                    case 2:
                        return VAL_MPS2;
                    case 3:
                        return VAL_KNOT2;
                }
                break;
            case 3:
                switch (spec2) {
                    case 0:
                        return VAL_GAL2;
                    case 1:
                        return VAL_M32;
                    case 2:
                        return VAL_L2;
                    case 3:
                        return VAL_I32;
                }
                break;
            case 5:
                switch (spec2) {
                    case 0:
                        return VAL_M2;
                    case 1:
                        return VAL_ML2;
                    case 2:
                        return VAL_Y2;
                    case 3:
                        return VAL_F2;
                    case 4:
                        return VAL_I2;
                    case 5:
                        return VAL_NM2;
                }
                break;
        }
        return 1;
    }

    private void setSpec(String s, boolean first)
    {
        switch(s)
        {
            case "Kilogram":
            case "Miles/Hour":
            case "Gallon":
            case "Celcius":
            case "Meter":
            case "US Dollar":
                if(first) spec1 = 0;
                else spec2 = 0;
                break;
            case "Pound":
            case "Foot/Sec":
            case "Cubic Meter":
            case "Fahrenheit":
            case "Mile":
            case "Rupiah":
                if(first) spec1 = 1;
                else spec2 = 1;
                break;
            case "Ounce":
            case "Meter/Sec":
            case "Litre":
            case "Kelvin":
            case "Yard":
            case "Euro":
                if(first) spec1 = 2;
                else spec2 = 2;
                break;
            case "Knot":
            case "Cubic Inch":
            case "Foot":
            case "Poundsterling":
                if(first) spec1 = 3;
                else spec2 = 3;
                break;
            case "Inch":
            case "Yen":
                if(first) spec1 = 4;
                else spec2 = 4;
                break;
            case "Nautical Mile":
            case "Won":
                if(first) spec1 = 5;
                else spec2 = 5;
                break;
        }
    }

    private void setValColor(String c)
    {
        ((EditText)findViewById(R.id.val1)).setTextColor(Color.parseColor(c));
        ((EditText)findViewById(R.id.val2)).setTextColor(Color.parseColor(c));
    }

    private void setupBut()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getRealSize(screenSize);
        int size = Math.min(screenSize.x, screenSize.y);
        int buttonSize = Math.round(size * 0.1f);
        for (int i = 0; i < row1.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row1.getChildAt(i);
            bt.setScaleX(.4f + (ds * .2f));
            bt.setScaleY(.4f + (ds * .2f));
            bt.setMaxHeight(bt.getWidth());
            bt.setMinimumWidth(((int)(36 * ds)) + buttonSize);
            bt.setMinimumHeight(((int)(36 * ds)) + buttonSize);
        }
        for (int i = 0; i < row2.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row2.getChildAt(i);
            bt.setScaleX(.4f + (ds * .2f));
            bt.setScaleY(.4f + (ds * .2f));
            bt.setMinimumWidth(((int)(36 * ds)) + buttonSize);
            bt.setMinimumHeight(((int)(36 * ds)) + buttonSize);
        }
        customScale((ViewGroup)findViewById(R.id.reveal2));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)findViewById(R.id.type2).getLayoutParams();
        lp.setMargins(0, ((int)(MainAct.ds*19.33 - 18)), 0, 0);
        findViewById(R.id.type2).setLayoutParams(lp);
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)val2.getLayoutParams();
        lp2.setMargins(0, ((int)(MainAct.ds*19.33 - 18)), 0, 0);
        val2.setLayoutParams(lp2);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)val1.getLayoutParams();
        lp1.setMargins(0, ((int)(MainAct.ds*19.33 - 18)), 0, 0);
        val1.setLayoutParams(lp1);

    }

    public void customScale(ViewGroup viewG)
    {
        for (int i = 0; i < viewG.getChildCount(); i++)
        {
            View vvv = viewG.getChildAt(i);
            if(vvv instanceof ViewGroup)
            {
                customScale((ViewGroup)vvv);
            } else
            {
                vvv.setScaleX(.4f + (ds * .21f));
                vvv.setScaleY(.4f + (ds * .21f));
            }
        }
    }

    private void disBut()
    {
        for (int i = 0; i < row1.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row1.getChildAt(i);
            bt.setEnabled(false);
        }
        for (int i = 0; i < row2.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row2.getChildAt(i);
            bt.setEnabled(false);
        }
    }

    private void enBut()
    {
        for (int i = 0; i < row1.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row1.getChildAt(i);
            bt.setEnabled(true);
        }
        for (int i = 0; i < row2.getChildCount(); i++)
        {
            ImageButton bt = (ImageButton) row2.getChildAt(i);
            bt.setEnabled(true);
        }
    }

    private void keyDetector()
    {
        (findViewById(R.id.lay_screen)).getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                (findViewById(R.id.lay_screen)).getWindowVisibleDisplayFrame(r);
                int screenHeight = (findViewById(R.id.lay_screen)).getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) {
                    keyOpened = true;
                }
                else {
                    keyOpened = false;
                }
            }
        });
    }

    private void autoHideKey(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(!keyOpened)
                    {
                        return false;
                    }
                    hideKeyboard(MainAct.this);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                autoHideKey(innerView);
            }
        }
    }

    //0=c, 1=F, 2=K
    public double calcTemp(Double d, boolean first)
    {
        if(first)
        {
            switch(spec1)
            {
                case 0:
                    if(spec2 == 1) return (d * 9/5) + 32;
                    else if (spec2 == 2) return d + 273.15;
                    return d;
                case 1:
                    if(spec2 == 0) return (d - 32) * 5/9;
                    else if (spec2 == 2) return (d - 32) * 5/9 + 273.15;
                    return d;
                case 2:
                    if(spec2 == 0) return d - 273.15;
                    else if (spec2 == 1) return (d - 273.15) * 9/5 + 32;
                    return d;
            }
        } else
        {
            switch(spec2)
            {
                case 0:
                    if(spec1 == 1) return (d * 9/5) + 32;
                    else if (spec1 == 2) return d + 273.15;
                    return d;
                case 1:
                    if(spec1 == 0) return (d - 32) * 5/9;
                    else if (spec1 == 2) return (d - 32) * 5/9 + 273.15;
                    return d;
                case 2:
                    if(spec1 == 0) return d - 273.15;
                    else if (spec1 == 1) return (d - 273.15) * 9/5 + 32;
                    return d;
            }
        }
        return d;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
