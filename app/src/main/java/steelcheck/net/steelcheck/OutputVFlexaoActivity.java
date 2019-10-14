package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class OutputVFlexaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;
    private final double E_aco = 20000.0; // kN/cm²
    private final double G = 7700.0; // kN/cm²
    final int tam_grande = 25, tam_pequeno = 18;

    private LinearLayout scroll_results;
    private int plus_controler = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_vcompressao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();



        if(extras != null)
        {

        }

    }


    public class DatabaseCustom{
        public double h;
        public double mesa;
        public double aba;
        public double cg;
        public double ag;
        public double ix;
        public double wx;
        public double rx;
        public double zx;
        public double iy;
        public double wy;
        public double ry;
        public double zy;
        public double j;
        public double cw;

        public double getAba() {
            return aba;
        }

        public double getH() {
            return h;
        }

        public double getMesa() {
            return mesa;
        }

        public double getCg() {
            return cg;
        }

        public double getAg() {
            return ag;
        }

        public double getIx() {
            return ix;
        }

        public double getWx() {
            return wx;
        }

        public double getRx() {
            return rx;
        }

        public double getZx() {
            return zx;
        }

        public double getIy() {
            return iy;
        }

        public double getWy() {
            return wy;
        }

        public double getRy() {
            return ry;
        }

        public double getZy() {
            return zy;
        }

        public double getJ() {
            return j;
        }

        public double getCw() {
            return cw;
        }

        public void calcularValores(double d, double tw, double bf, double tf)
        {
            h       = d - 2*tf;
            mesa    = h/tw;
            aba     = 0.5*bf/tf;
            cg      = (tf*bf*tf/2+h*tw*(tf+h/2)+tf*bf*(d-tf/2))/(tf*bf+tw*h+tf*bf);
            ag      = (2*bf*tf+h*tw)/100;
            ix      = ((bf*Math.pow(tf,3))/12+bf*tf*Math.pow(cg-tf/2,2)+tw*Math.pow(h,3)/12+tw*h*Math.pow(tf+h/2-cg,2)+bf*Math.pow(tf,3)/12+bf*tf*Math.pow(d-cg-tf/2,2))/10000;
            wx      = ix/(d/10-cg/10);
            rx      = Math.pow(ix/ag,0.5);
            zx      = (tf*bf*(d-cg-tf/2)+tw*(d-cg-tf)*(d-cg-tf)/2+tf*bf*(cg-tf/2)+tw*(cg-tf)*(cg-tf)/2)/1000;
            iy      = (tf*Math.pow(bf,3)/12+tf*Math.pow(bf,3)/12+h*Math.pow(tw,3)/12)/10000;
            wy      = iy/(0.5*bf/10);
            ry      = Math.pow(iy/ag,0.5);
            zy      = (h*Math.pow(tw,2)/4+tf*Math.pow(bf,2)/4+tf*Math.pow(bf,2)/4)/1000;
            j       = (bf*Math.pow(tf,3)/3+bf*Math.pow(tf,3)/3+(h+tf/2+tf/2)*Math.pow(tw,3)/3)/10000;
            cw      = Math.pow(d-tf,2)/12*(tf*Math.pow(bf,3)*tf*Math.pow(bf,3))/(tf*Math.pow(bf,3)+tf*Math.pow(bf,3))/1000000;
        }
    }

}
