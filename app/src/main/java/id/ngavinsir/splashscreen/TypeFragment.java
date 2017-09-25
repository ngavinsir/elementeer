package id.ngavinsir.splashscreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by GAVIN on 9/20/2017.
 */

public class TypeFragment extends Fragment {

    private TextView txt;

    public static Fragment newInstance(MainAct context, String s, Float f) {
        Bundle b = new Bundle();
        b.putString("txt", s);
        b.putFloat("scale", f);
        return Fragment.instantiate(context, TypeFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_type, container, false);
        txt = view.findViewById(R.id.txt);
        txt.setText(this.getArguments().getString("txt"));
        view.findViewById(R.id.txt).setScaleX(.4f + (MainAct.ds * .2f));
        view.findViewById(R.id.txt).setScaleY(.4f + (MainAct.ds * .2f));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)view.findViewById(R.id.txt).getLayoutParams();
        lp.setMargins(0, ((int)(MainAct.ds*19.33 - 18)), 0, 0);
        view.findViewById(R.id.txt).setLayoutParams(lp);

        TypeLayout cl = view.findViewById(R.id.custom_lay);
        float scale = this.getArguments().getFloat("scale");
        cl.setScaleBoth(scale);


        return view;
    }

}
