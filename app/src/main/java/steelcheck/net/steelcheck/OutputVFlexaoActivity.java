package steelcheck.net.steelcheck;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class OutputVFlexaoActivity extends AppCompatActivity {
    public final double gama_a1 = 1.10;
    public final double gama_a2 = 1.35;
    public final double E_aco = 20000.0; // kN/cm²
    public final double G = 7700.0; // kN/cm²
    final int tam_grande = 20, tam_pequeno = 16, tam_dimens = 13;

    private LinearLayout scroll_results;
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
        setContentView(R.layout.activity_output_vflexao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();



        if(extras != null)
        {
            double msdx = extras.getDouble("msdx");
            double msdy = extras.getDouble("msdy");
            double fy = extras.getDouble("fy");
            double lb = extras.getDouble("lb");
            double cb = extras.getDouble("cb");
            int secao = extras.getInt("secao");
            int analise = extras.getInt("analise");
            double vsdx = 0;
            double vsdy = 0;
            double flecha = 0;
            double vao = 0;
            if(analise == 1)
            {
                vsdx = extras.getDouble("vsdx");
                vsdy = extras.getDouble("vsdy");
            }
            else if(analise == 2)
            {
                vao = extras.getDouble("vao");
                flecha = extras.getDouble("flecha");
            }
            else if(analise == 3)
            {
                vsdx = extras.getDouble("vsdx");
                vsdy = extras.getDouble("vsdy");
                vao = extras.getDouble("vao");
                flecha = extras.getDouble("flecha");
            }

            if(secao == 1)
            {
                DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                database.open();
                database.order_by("id");
                int perfil = extras.getInt("perfil");
                double FLM_lambda_b = FLM_lambda_b(database.get_aba(perfil));
                double FLM_lambda_p = FLM_lambda_p(fy);
                double FLM_lambda_r = FLM_lambda_r_LaminadoW(fy);
                double mpx = Mpx(database.get_zx(perfil),fy);
                double mpy = Mpy(database.get_zy(perfil),fy);
                double FLM_mrx = FLM_Mrx(database.get_wx(perfil),fy);
                double FLM_mry = FLM_Mry(database.get_wy(perfil),fy);
                double FLM_mnx = Mn_FLM_x_LaminadoW(mpx,FLM_mrx,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.get_wx(perfil));
                double FLM_mny = Mn_FLM_y_LaminadoW(mpy,FLM_mry,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.get_wy(perfil));
                String FLM = FLM(FLM_lambda_b,FLM_lambda_p,FLM_lambda_r);
                double FLA_lambda_b = FLM_lambda_b(database.get_mesa(perfil));
                double FLA_lambda_p = FLA_lambda_p(fy);
                double FLA_lambda_r = FLA_lambda_r(fy);
                double FLA_mr = FLA_Mrx(database.get_wx(perfil),fy);
                double FLA_mn = FLA_Mn(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,mpx,FLA_mr);
                String FLA = FLA(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r);
                double FLT_lambda_b = FLT_lambda_b(lb,database.get_ry(perfil));
                double FLT_l_p = FLT_l_p(database.get_ry(perfil),fy);
                double FLT_lambda_p = FLT_lambda_p(FLT_l_p,database.get_ry(perfil));
                double FLT_l_r = FLT_l_r(database.get_iy(perfil),database.get_j(perfil),database.get_ry(perfil),database.get_cw(perfil),fy,database.get_wx(perfil));
                double FLT_lambda_r = FLT_lambda_r(FLT_l_r,database.get_ry(perfil));
                double FLT_mn = FLT_Mn(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r,mpx,FLM_mrx,cb,database.get_iy(perfil),database.get_cw(perfil),database.get_j(perfil),lb);
                String FLT = FLT(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r);
                double mrdx = Mrdx(mpx,FLM_mnx,FLA_mn,FLT_mn);
                double mrdy = Mrdy(mpy,FLM_mny);
                double vrdx = Vrdx_LaminadoW(database.get_mesa(perfil),fy,database.get_d(perfil),database.get_tw(perfil));
                double vrdy = Vrdy(database.get_aba(perfil),fy,database.get_bf(perfil),database.get_tf(perfil));
                double flechaadm = FlechaAdm(vao);

                if(FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b,fy,database.get_bf(perfil),database.get_tf(perfil),FLA_lambda_r);
                else
                     Show_Results_LaminadoW(database,perfil,database.get_perfil(perfil),analise,fy,
                        msdx,msdy,cb,vsdx,vsdy,flecha,vao,
                        FLM,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,FLM_mnx,FLM_mny,
                        FLA,FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,FLA_mn,
                        FLT,lb,FLT_lambda_b,FLT_l_p,FLT_lambda_p,FLT_l_r,FLT_lambda_r,FLT_mn,mrdx,mrdy,
                        vrdx,vrdy,flechaadm);
                database.close();
            }
            else if(secao == 2)
            {
                double d = extras.getDouble("d");
                double tw = extras.getDouble("tw");
                double bf = extras.getDouble("bf");
                double tf = extras.getDouble("tf");
                DatabaseCustom database = new DatabaseCustom();
                database.calcularValores(d,tw,bf,tf);
                double FLM_lambda_b = FLM_lambda_b(database.getAba());
                double FLM_lambda_p = FLM_lambda_p(fy);
                double kc = Kc(database.getH(),tw);
                double FLM_lambda_r = FLM_lambda_r_SoldadoCustom(fy,kc);
                double mpx = Mpx(database.getZx(),fy);
                double mpy = Mpy(database.getZy(),fy);
                double FLM_mrx = FLM_Mrx(database.getWx(),fy);
                double FLM_mry = FLM_Mry(database.getWy(),fy);
                double FLM_mnx = Mn_FLM_x_SoldadoCustom(mpx,FLM_mrx,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.getWx(),kc);
                double FLM_mny = Mn_FLM_y_SoldadoCustom(mpy,FLM_mry,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.getWy(),kc);
                String FLM = FLM(FLM_lambda_b,FLM_lambda_p,FLM_lambda_r);
                double FLA_lambda_b = FLM_lambda_b(database.getMesa());
                double FLA_lambda_p = FLA_lambda_p(fy);
                double FLA_lambda_r = FLA_lambda_r(fy);
                double FLA_mr = FLA_Mrx(database.getWx(),fy);
                double FLA_mn = FLA_Mn(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,mpx,FLA_mr);
                String FLA = FLA(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r);
                double FLT_lambda_b = FLT_lambda_b(lb,database.getRy());
                double FLT_l_p = FLT_l_p(database.getRy(),fy);
                double FLT_lambda_p = FLT_lambda_p(FLT_l_p,database.getRy());
                double FLT_l_r = FLT_l_r(database.getIy(),database.getJ(),database.getRy(),database.getCw(),fy,database.getWx());
                double FLT_lambda_r = FLT_lambda_r(FLT_l_r,database.getRy());
                double FLT_mn = FLT_Mn(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r,mpx,FLM_mrx,cb,database.getIy(),database.getCw(),database.getJ(),lb);
                String FLT = FLT(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r);
                double mrdx = Mrdx(mpx,FLM_mnx,FLA_mn,FLT_mn);
                double mrdy = Mrdy(mpy,FLM_mny);
                double vrdx = Vrdx_SoldadoCustom(database.getMesa(),fy,d,tw);
                double vrdy = Vrdy(database.getAba(),fy,bf,tf);
                double flechaadm = FlechaAdm(vao);
                System.out.println("kc = " + kc);
                if(FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b,fy,bf,tf,FLA_lambda_r);
                else if(kc <0.35 || kc >0.76)
                    Show_Kc_erro(kc,database.getH(),tw);
                else
                    Show_Results_SoldadoCustom(database,"CUSTOMIZADO",analise,fy,d,tw,bf,tf,database.getRy(),database.getZx(),database.getIy(),database.getJ()
                        ,database.getCw(),database.getWx(),database.getMesa(),database.getAba(),
                        msdx,msdy,cb,vsdx,vsdy,flecha,vao,
                        FLM,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,FLM_mnx,FLM_mny,
                        FLA,FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,FLA_mn,
                        FLT,lb,FLT_lambda_b,FLT_l_p,FLT_lambda_p,FLT_l_r,FLT_lambda_r,FLT_mn,mrdx,mrdy,
                        vrdx,vrdy,flechaadm);

            }
        }

    }

    //CALCULOS DE VERIFICACAO
    public double FLM_lambda_b(double aba)
    {
        return aba;
    }
    public double FLM_lambda_p(double fy)
    {
        return 0.38 * Math.sqrt(E_aco/(fy/10));
    }
    public double FLM_lambda_r_LaminadoW(double fy)
    {
        return 0.83 * Math.sqrt(1*E_aco/(0.7*(fy/10)));
    }
    public double FLM_lambda_r_SoldadoCustom(double fy, double kc)
    {
        return 0.95 * Math.sqrt(kc*E_aco/(0.7*(fy/10)));
    }
    public double Kc(double h, double tw)
    {
        return 4 / Math.sqrt(h/tw);
    }
    public double Mpx(double zx, double fy)
    {
        return zx*(fy/1000);
    }
    public double Mpy(double zy, double fy)
    {
        return zy*(fy/1000);
    }
    public double FLM_Mrx(double wx, double fy)
    {
        return 0.7*wx*(fy/1000);
    }
    public double FLM_Mry(double wy, double fy)
    {
        return 0.7*wy*(fy/1000);
    }
    public double Mn_FLM_x_LaminadoW(double mpx, double mrx, double lambda_b, double lambda_p, double lambda_r, double wx)
    {
        if(lambda_b <= lambda_p)
            return mpx;
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return ( mpx ) - ( ( ( lambda_b - lambda_p )/( lambda_r - lambda_p ) )*( mpx - mrx ) );
        return 0.69*E_aco*wx/Math.pow(lambda_b,2);
    }
    public double Mn_FLM_x_SoldadoCustom(double mpx, double mrx, double lambda_b, double lambda_p, double lambda_r, double wx, double kc)
    {
        if(lambda_b <= lambda_p)
            return mpx;
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return ( mpx ) - ( ( ( lambda_b - lambda_p )/( lambda_r - lambda_p ) )*( mpx - mrx ) );
        return 0.9*E_aco*kc*wx/Math.pow(lambda_b,2);
    }
    public double Mn_FLM_y_LaminadoW(double mpy, double mry, double lambda_b, double lambda_p, double lambda_r, double wy)
    {
        if(lambda_b <= lambda_p)
            return mpy;
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return ( mpy ) - ( ( ( lambda_b - lambda_p )/( lambda_r - lambda_p ) )*( mpy - mry ) );
        return 0.69*E_aco*wy/Math.pow(lambda_b,2);
    }
    public double Mn_FLM_y_SoldadoCustom(double mpy, double mry, double lambda_b, double lambda_p, double lambda_r, double wy, double kc)
    {
        if(lambda_b <= lambda_p)
            return mpy;
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return ( mpy ) - ( ( ( lambda_b - lambda_p )/( lambda_r - lambda_p ) )*( mpy - mry ) );
        return 0.9*E_aco*kc*wy/Math.pow(lambda_b,2);
    }
    public String FLM(double lambda_b, double lambda_p, double lambda_r)
    {
        if(lambda_b <= lambda_p)
            return "Seção Compacta";
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return "Seção Semicompacta";
        return "Seção Esbelta";
    }
    public double FLA_lambda_b(double mesa)
    {
        return mesa;
    }
    public double FLA_lambda_p(double fy)
    {
        return 3.76 * Math.sqrt(E_aco/(fy/10));
    }
    public double FLA_lambda_r(double fy)
    {
        return 5.7 * Math.sqrt(E_aco/(fy/10));
    }
    public double FLA_Mrx(double wx, double fy)
    {
        return wx * fy/1000;
    }
    public double FLA_Mn(double lambda_b, double lambda_p, double lambda_r, double mpx, double mr)
    {
        if(lambda_b <= lambda_p)
            return mpx;
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return ( mpx ) - ( ( ( lambda_b - lambda_p )/( lambda_r - lambda_p ) )*( mpx - mr ) );
        return 0; //nao vai acontecer (msg de aviso)
    }
    public String FLA(double lambda_b, double lambda_p, double lambda_r)
    {
        if(lambda_b <= lambda_p)
            return "Seção Compacta";
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return "Seção Semicompacta";
        return "Seção Esbelta"; // nao vai acontecer
    }

    public double FLT_lambda_b(double lb, double ry)
    {
        return lb/ry;
    }
    public double FLT_l_p(double ry, double fy)
    {
        return 1.76*ry*Math.sqrt(E_aco/(fy/10));
    }
    public double FLT_lambda_p(double lp, double ry)
    {
        return lp/ry;
    }
    public double beta1(double fy, double wx, double j)
    {
        return (0.7*(fy/10)*wx)/(E_aco*j);
    }
    public double FLT_l_r(double iy, double j, double ry, double cw, double fy, double wx)
    {   double beta = beta1(fy,wx,j);
        return ( ( 1.38*Math.sqrt(iy*j) )/( j*beta) )*( Math.sqrt(1 + Math.sqrt(1 + ( (27*cw*Math.pow(beta,2))/iy ))) );
    }
    public double FLT_lambda_r(double lr, double ry)
    {
        return lr/ry;
    }
    public double FLT_Mn(double lambda_b, double lambda_p, double lambda_r, double mpx, double mr,
                          double cb, double iy, double cw, double j, double lb)
    {
        if(lambda_b <= lambda_p)
            return mpx;
        if(lambda_b > lambda_p && lambda_b <= lambda_r) {
            double aux = cb*((mpx) - (((lambda_b - lambda_p) / (lambda_r - lambda_p)) * (mpx - mr)));
            System.out.println(" cb*mrx = " + aux);
            System.out.println(" mpx = " + mpx);
            if(aux > mpx)
                return mpx;
            else
                return aux;
        }
        return (( cb*Math.pow(Math.PI,2)*E_aco*iy * ( Math.sqrt( (cw/iy)*( 1+(0.039*j*Math.pow(lb,2)/cw) ) ) ) )/( Math.pow(lb,2) )) / 100;
    }
    public String FLT(double lambda_b, double lambda_p, double lambda_r)
    {
        if(lambda_b <= lambda_p)
            return "Viga Curta";
        if(lambda_b > lambda_p && lambda_b <= lambda_r)
            return "Viga Intermediária";
        return "Viga Longa";
    }

    public double Mrflx(double mpx, double flmx, double fla, double flt)
    {   double min = mpx;
        if(flmx < min)
            min = flmx;
        if(fla < min)
            min = fla;
        if(flt < min)
            min = flt;
        return min;
    }
    public double Mrfly(double mpy, double flmy)
    {
        if(mpy < flmy)
            return mpy;
        return flmy;
    }
    public double Mrdx(double mpx, double flmx, double fla, double flt)
    {
        double mrflx = Mrflx(mpx,flmx,fla,flt);
        return mrflx/gama_a1;
    }
    public double Mrdy(double mpy, double flmy)
    {
        double mrfly = Mrfly(mpy,flmy);
        return mrfly/gama_a1;
    }

    public double Vrdx_LaminadoW(double mesa, double fy, double d, double tw)
    {
        double lambda_vx = mesa;
        double kvx = 5.0;
        double lim1x = 1.10*Math.sqrt(kvx*E_aco/(fy/10));
        double lim2x = 1.37*Math.sqrt(kvx*E_aco/(fy/10));
        double vplx = 0.6*d*tw*fy/1000;

        if(lambda_vx <= lim1x)
            return vplx/gama_a1;
        if((lambda_vx > lim1x) && (lambda_vx <= lim2x))
            return (lim1x/lambda_vx)*(vplx/gama_a1);
        return 1.24*Math.pow(lim1x/lambda_vx,2)*(vplx/gama_a1);
    }
    public double Vrdx_SoldadoCustom(double mesa, double fy, double h, double tw)
    {
        double lambda_vx = mesa;
        double kvx = 5.0;
        double lim1x = 1.10*Math.sqrt(kvx*E_aco/(fy/10));
        double lim2x = 1.37*Math.sqrt(kvx*E_aco/(fy/10));
        double vplx = 0.6*h*tw*fy/1000;

        if(lambda_vx <= lim1x)
            return vplx/gama_a1;
        if((lambda_vx > lim1x) && (lambda_vx <= lim2x))
            return (lim1x/lambda_vx)*(vplx/gama_a1);
        return 1.24*Math.pow(lim1x/lambda_vx,2)*(vplx/gama_a1);
    }
    public double Vrdy(double aba, double fy, double bf, double tf)
    {
        double lambda_vy = aba;
        double kvy = 1.2;
        double lim1y = 1.10*Math.sqrt(kvy*E_aco/(fy/10));
        double lim2y = 1.37*Math.sqrt(kvy*E_aco/(fy/10));
        double vply = 0.6*2*bf*tf*fy/1000;

        if(lambda_vy <= lim1y)
            return vply/gama_a1;
        if((lambda_vy > lim1y) && (lambda_vy <= lim2y))
            return (lim1y/lambda_vy)*(vply/gama_a1);
        return 1.24*Math.pow(lim1y/lambda_vy,2)*(vply/gama_a1);
    }

    public double FlechaAdm(double vao)
    {
        return 1000*vao/350;
    }

    public double Momento(double msdx, double msdy, double mrdx, double mrdy)
    {
        return ( ( msdx/mrdx ) ) + ( ( msdy/mrdy ) );
    }
    public double Cortante(double vsdx, double vsdy, double vrdx, double vrdy)
    {
        return ( ( vsdx/vrdx ) ) + ( ( vsdy/vrdy ) );
    }

    //ARREDONDAMENTO
    public double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }
    //CRIACAO DE LAYOUT
    public void Show_Dimensoes_Database_Perfil(DatabaseAccess db, LinearLayout scroll, Context context, int pos)
    {
        LinearLayout switch_layout = new LinearLayout(context);
        switch_layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView nao = new TextView(context);
        nao.setText("Mostrar Dimensões:\tNão");
        switch_layout.addView(nao);
        Switch sw_dimens = new Switch(context);
        switch_layout.addView(sw_dimens);
        TextView sim = new TextView(context);
        sim.setText("Sim");
        switch_layout.addView(sim);
        switch_layout.setPadding(50,20,0,50);

        scroll.addView(switch_layout);

        final LinearLayout dimens_layout = new LinearLayout(context);
        dimens_layout.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(dimens_layout);

        final LinearLayout col_1 = new LinearLayout(context);
        col_1.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_1);

        final LinearLayout col_2 = new LinearLayout(context);
        col_2.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_2);
        dimens_layout.setGravity(Gravity.CENTER);

        final TextView bf = new TextView(context);
        bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + db.get_bf(pos) + " mm"));
        bf.setTextSize(tam_dimens);
        bf.setPadding(50,15,0,15);
        bf.setTextColor(Color.BLACK);

        final TextView tf = new TextView(context);
        tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + db.get_tf(pos) + " mm"));
        tf.setTextSize(tam_dimens);
        tf.setPadding(50,15,0,15);
        tf.setTextColor(Color.BLACK);

        final TextView h0 = new TextView(context);
        h0.setText(Html.fromHtml("h<small><sub>0</sub></small> = " + db.get_h(pos) + " mm"));
        h0.setTextSize(tam_dimens);
        h0.setPadding(50,15,0,15);
        h0.setTextColor(Color.BLACK);

        final TextView hw = new TextView(context);
        hw.setText(Html.fromHtml("h<small><sub>w</sub></small> = " + db.get_dlinha(pos) + " mm"));
        hw.setTextSize(tam_dimens);
        hw.setPadding(50,15,0,15);
        hw.setTextColor(Color.BLACK);

        final TextView tw = new TextView(context);
        tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + db.get_tw(pos) + " mm"));
        tw.setTextSize(tam_dimens);
        tw.setPadding(50,15,0,15);
        tw.setTextColor(Color.BLACK);

        final TextView ag = new TextView(context);
        ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + db.get_area(pos) + " cm²"));
        ag.setTextSize(tam_dimens);
        ag.setPadding(50,15,0,15);
        ag.setTextColor(Color.BLACK);

        final TextView ix = new TextView(context);
        ix.setText(Html.fromHtml("I<small><sub>x</sub></small> = " + db.get_ix(pos) + " cm<small><sup>4</sup></small>"));
        ix.setTextSize(tam_dimens);
        ix.setPadding(50,15,0,15);
        ix.setTextColor(Color.BLACK);

        final TextView wx = new TextView(context);
        wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + db.get_wx(pos) + " cm<small><sup>3</sup></small>"));
        wx.setTextSize(tam_dimens);
        wx.setPadding(50,15,0,15);
        wx.setTextColor(Color.BLACK);

        final TextView zx = new TextView(context);
        zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + db.get_zx(pos) + " cm<small><sup>3</sup></small>"));
        zx.setTextSize(tam_dimens);
        zx.setPadding(50,15,0,50);
        zx.setTextColor(Color.BLACK);

        final TextView rx = new TextView(context);
        rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + db.get_rx(pos) + " cm"));
        rx.setTextSize(tam_dimens);
        rx.setPadding(50,15,0,15);
        rx.setTextColor(Color.BLACK);

        final TextView iy = new TextView(context);
        iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + db.get_iy(pos) + " cm<small><sup>4</sup></small>"));
        iy.setTextSize(tam_dimens);
        iy.setPadding(50,15,0,15);
        iy.setTextColor(Color.BLACK);

        final TextView wy = new TextView(context);
        wy.setText(Html.fromHtml("W<small><sub>y</sub></small> = " + db.get_wy(pos) + " cm<small><sup>3</sup></small>"));
        wy.setTextSize(tam_dimens);
        wy.setPadding(50,15,0,15);
        wy.setTextColor(Color.BLACK);

        final TextView zy = new TextView(context);
        zy.setText(Html.fromHtml("Z<small><sub>y</sub></small> = " + db.get_zy(pos) + " cm<small><sup>3</sup></small>"));
        zy.setTextSize(tam_dimens);
        zy.setPadding(50,15,0,15);
        zy.setTextColor(Color.BLACK);

        final TextView ry = new TextView(context);
        ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + db.get_ry(pos) + " cm"));
        ry.setTextSize(tam_dimens);
        ry.setPadding(50,15,0,15);
        ry.setTextColor(Color.BLACK);

        final TextView rt = new TextView(context);
        rt.setText(Html.fromHtml("r<small><sub>t</sub></small> = " + db.get_rt(pos) + " cm"));
        rt.setTextSize(tam_dimens);
        rt.setPadding(50,15,0,15);
        rt.setTextColor(Color.BLACK);

        final TextView it = new TextView(context);
        it.setText(Html.fromHtml("I<small><sub>t</sub></small> = " + db.get_j(pos) + " cm<small><sup>4</sup></small>"));
        it.setTextSize(tam_dimens);
        it.setPadding(50,15,0,15);
        it.setTextColor(Color.BLACK);

        final TextView cw = new TextView(context);
        cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + db.get_cw(pos) + " cm<small><sup>6</sup></small>"));
        cw.setTextSize(tam_dimens);
        cw.setPadding(50,15,0,15);
        cw.setTextColor(Color.BLACK);

        final TextView massa = new TextView(context);
        massa.setText(Html.fromHtml("M. linear = " + db.get_massa(pos) + " kg/m"));
        massa.setTextSize(tam_dimens);
        massa.setPadding(50,15,0,50);
        massa.setTextColor(Color.BLACK);

        sw_dimens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    col_1.addView(bf);
                    col_1.addView(tf);
                    col_1.addView(h0);
                    col_1.addView(hw);
                    col_1.addView(tw);
                    col_1.addView(ag);
                    col_1.addView(ix);
                    col_1.addView(wx);
                    col_1.addView(zx);
                    col_2.addView(rx);
                    col_2.addView(iy);
                    col_2.addView(wy);
                    col_2.addView(zy);
                    col_2.addView(ry);
                    col_2.addView(rt);
                    col_2.addView(it);
                    col_2.addView(cw);
                    col_2.addView(massa);
                }
                else
                {
                    col_1.removeAllViews();
                    col_2.removeAllViews();
                }
            }
        });
    }
    public void Show_Dimensoes_Database_Perfil(DatabaseCustom db, LinearLayout scroll, Context context )
    {
        LinearLayout switch_layout = new LinearLayout(context);
        switch_layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView nao = new TextView(context);
        nao.setText("Mostrar Dimensões:\tNão");
        switch_layout.addView(nao);
        Switch sw_dimens = new Switch(context);
        switch_layout.addView(sw_dimens);
        TextView sim = new TextView(context);
        sim.setText("Sim");
        switch_layout.addView(sim);
        switch_layout.setPadding(50,20,0,50);

        scroll.addView(switch_layout);

        final LinearLayout dimens_layout = new LinearLayout(context);
        dimens_layout.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(dimens_layout);

        final LinearLayout col_1 = new LinearLayout(context);
        col_1.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_1);

        final LinearLayout col_2 = new LinearLayout(context);
        col_2.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_2);
        dimens_layout.setGravity(Gravity.CENTER);

        final TextView bf = new TextView(context);
        bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + casasDecimais(db.getBf(),2) + " mm"));
        bf.setTextSize(tam_dimens);
        bf.setPadding(50,15,0,15);
        bf.setTextColor(Color.BLACK);

        final TextView tf = new TextView(context);
        tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + casasDecimais(db.getTf(),2) + " mm"));
        tf.setTextSize(tam_dimens);
        tf.setPadding(50,15,0,15);
        tf.setTextColor(Color.BLACK);

        final TextView h0 = new TextView(context);
        h0.setText(Html.fromHtml("h<small><sub>0</sub></small> = " + casasDecimais(db.getH(),2) + " mm"));
        h0.setTextSize(tam_dimens);
        h0.setPadding(50,15,0,15);
        h0.setTextColor(Color.BLACK);

        final TextView tw = new TextView(context);
        tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + casasDecimais(db.getTw(),2) + " mm"));
        tw.setTextSize(tam_dimens);
        tw.setPadding(50,15,0,15);
        tw.setTextColor(Color.BLACK);

        final TextView ag = new TextView(context);
        ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + casasDecimais(db.getAg(),2) + " cm²"));
        ag.setTextSize(tam_dimens);
        ag.setPadding(50,15,0,15);
        ag.setTextColor(Color.BLACK);

        final TextView ix = new TextView(context);
        ix.setText(Html.fromHtml("I<small><sub>x</sub></small> = " + casasDecimais(db.getIx(),2) + " cm<small><sup>4</sup></small>"));
        ix.setTextSize(tam_dimens);
        ix.setPadding(50,15,0,15);
        ix.setTextColor(Color.BLACK);

        final TextView wx = new TextView(context);
        wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(db.getWx(),2) + " cm<small><sup>3</sup></small>"));
        wx.setTextSize(tam_dimens);
        wx.setPadding(50,15,0,15);
        wx.setTextColor(Color.BLACK);

        final TextView zx = new TextView(context);
        zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(db.getZx(),2) + " cm<small><sup>3</sup></small>"));
        zx.setTextSize(tam_dimens);
        zx.setPadding(50,15,0,50);
        zx.setTextColor(Color.BLACK);

        final TextView rx = new TextView(context);
        rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + casasDecimais(db.getRx(),2) + " cm"));
        rx.setTextSize(tam_dimens);
        rx.setPadding(50,15,0,15);
        rx.setTextColor(Color.BLACK);

        final TextView iy = new TextView(context);
        iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(db.getIy(),2) + " cm<small><sup>4</sup></small>"));
        iy.setTextSize(tam_dimens);
        iy.setPadding(50,15,0,15);
        iy.setTextColor(Color.BLACK);

        final TextView wy = new TextView(context);
        wy.setText(Html.fromHtml("W<small><sub>y</sub></small> = " + casasDecimais(db.getWy(),2) + " cm<small><sup>3</sup></small>"));
        wy.setTextSize(tam_dimens);
        wy.setPadding(50,15,0,15);
        wy.setTextColor(Color.BLACK);

        final TextView zy = new TextView(context);
        zy.setText(Html.fromHtml("Z<small><sub>y</sub></small> = " + casasDecimais(db.getZy(),2) + " cm<small><sup>3</sup></small>"));
        zy.setTextSize(tam_dimens);
        zy.setPadding(50,15,0,15);
        zy.setTextColor(Color.BLACK);

        final TextView ry = new TextView(context);
        ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(db.getRy(),2) + " cm"));
        ry.setTextSize(tam_dimens);
        ry.setPadding(50,15,0,15);
        ry.setTextColor(Color.BLACK);

        final TextView rt = new TextView(context);
        rt.setText(Html.fromHtml("r<small><sub>t</sub></small> = " + casasDecimais(db.getRt(),2) + " cm"));
        rt.setTextSize(tam_dimens);
        rt.setPadding(50,15,0,15);
        rt.setTextColor(Color.BLACK);

        final TextView it = new TextView(context);
        it.setText(Html.fromHtml("I<small><sub>t</sub></small> = " + casasDecimais(db.getJ(),2) + " cm<small><sup>4</sup></small>"));
        it.setTextSize(tam_dimens);
        it.setPadding(50,15,0,15);
        it.setTextColor(Color.BLACK);

        final TextView cw = new TextView(context);
        cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(db.getCw(),2) + " cm<small><sup>6</sup></small>"));
        cw.setTextSize(tam_dimens);
        cw.setPadding(50,15,0,50);
        cw.setTextColor(Color.BLACK);

        sw_dimens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    col_1.addView(bf);
                    col_1.addView(tf);
                    col_1.addView(h0);
                    col_1.addView(tw);
                    col_1.addView(ag);
                    col_1.addView(ix);
                    col_1.addView(wx);
                    col_1.addView(zx);
                    col_2.addView(rx);
                    col_2.addView(iy);
                    col_2.addView(wy);
                    col_2.addView(zy);
                    col_2.addView(ry);
                    col_2.addView(rt);
                    col_2.addView(it);
                    col_2.addView(cw);
                    //col_2.addView(massa);
                }
                else
                {
                    col_1.removeAllViews();
                    col_2.removeAllViews();
                }
            }
        });
    }
    private void Show_Results_LaminadoW(DatabaseAccess db, int pos, String perfil, int analise, double fy, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVFlexaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVFlexaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVFlexaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVFlexaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVFlexaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVFlexaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVFlexaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVFlexaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVFlexaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);


        TextView TV_msdx = new TextView(OutputVFlexaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputVFlexaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputVFlexaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(50,15,0,50);
        contorno_1.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

            //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputVFlexaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdx);
            TV_vsdx.setTextColor(Color.BLACK);

            TextView TV_vsdy = new TextView(OutputVFlexaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdy);
            TV_vsdy.setTextColor(Color.BLACK);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputVFlexaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(50,15,0,15);
            contorno_2.addView(TV_flecha);
            TV_flecha.setTextColor(Color.BLACK);

            TextView TV_vao = new TextView(OutputVFlexaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(50,15,0,50);
            contorno_2.addView(TV_vao);
            TV_vao.setTextColor(Color.BLACK);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputVFlexaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputVFlexaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputVFlexaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputVFlexaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputVFlexaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputVFlexaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputVFlexaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputVFlexaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputVFlexaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputVFlexaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputVFlexaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputVFlexaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputVFlexaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputVFlexaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputVFlexaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputVFlexaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputVFlexaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputVFlexaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputVFlexaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputVFlexaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //ANALISE
        //**resis
        TextView TV_momento = new TextView(OutputVFlexaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        LinearLayout tab_esf = new LinearLayout(OutputVFlexaoActivity.this);
        tab_esf.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_esf);

        LinearLayout col_1_esf = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_1_esf);

        LinearLayout col_2_esf = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_2_esf);

        TextView TV_mrdx = new TextView(OutputVFlexaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        col_1_esf.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputVFlexaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,50);
        col_1_esf.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

            //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_vrdx = new TextView(OutputVFlexaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(50,15,0,15);
            col_2_esf.addView(TV_vrdx);
            TV_vrdx.setTextColor(Color.BLACK);

            TextView TV_vrdy = new TextView(OutputVFlexaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(50,15,0,50);
            col_2_esf.addView(TV_vrdy);
            TV_vrdy.setTextColor(Color.BLACK);
        }

            //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputVFlexaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);
            TV_flechaadm.setGravity(Gravity.CENTER);
            TV_flechaadm.setTextColor(Color.WHITE);
            TV_flechaadm.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_flechaadm.setPadding(50,20,50,20);

            TextView TV_fadm = new TextView(OutputVFlexaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(50,15,0,50);
            scroll_results.addView(TV_fadm);
            TV_fadm.setTextColor(Color.BLACK);
        }


        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexaoActivity.this);
        TV_verifica.setText("VERIFICAÇÃO");
        TV_verifica.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);
        TV_verifica.setGravity(Gravity.CENTER);
        TV_verifica.setTextColor(Color.WHITE);
        TV_verifica.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verifica.setPadding(50,20,50,20);

        double momento = Momento(msdx,msdy,mrdx,mrdy);

        TextView TV_mom = new TextView(OutputVFlexaoActivity.this);
        TV_mom.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small> = " + casasDecimais(momento,3) ));
        TV_mom.setTextSize(tam_pequeno);
        TV_mom.setPadding(50,15,0,15);
        scroll_results.addView(TV_mom);
        TV_mom.setTextColor(Color.BLACK);

        if (momento <= 1.0) //ok
        {
            TextView TV_mom_ok = new TextView(OutputVFlexaoActivity.this);
            TV_mom_ok.setText(Html.fromHtml("Perfil atende ao momento fletor! "));
            TV_mom_ok.setTextSize(tam_pequeno);
            TV_mom_ok.setPadding(50,15,0,50);
            TV_mom_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_mom_ok);
        }
        else
        {
            TextView TV_mom_nok = new TextView(OutputVFlexaoActivity.this);
            TV_mom_nok.setText(Html.fromHtml("Perfil NÃO atende ao momento fletor!"));
            TV_mom_nok.setTextSize(tam_pequeno);
            TV_mom_nok.setPadding(50,15,0,50);
            TV_mom_nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_mom_nok);
        }

        if(analise == 1 || analise == 3)
        {
            double cortante = Cortante(vsdx,vsdy,vrdx,vrdy);

            TextView TV_cort = new TextView(OutputVFlexaoActivity.this);
            TV_cort.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> / V<small><sub>Rd,x</sub></small>  +  V<small><sub>Sd,y</sub></small> / V<small><sub>Rd,y</sub></small> = " + casasDecimais(cortante,3) ));
            TV_cort.setTextSize(tam_pequeno);
            TV_cort.setPadding(50,15,0,15);
            scroll_results.addView(TV_cort);
            TV_cort.setTextColor(Color.BLACK);

            if (cortante <= 1.0) //ok
            {
                TextView TV_cort_ok = new TextView(OutputVFlexaoActivity.this);
                TV_cort_ok.setText(Html.fromHtml("Perfil atende à força cortante! "));
                TV_cort_ok.setTextSize(tam_pequeno);
                TV_cort_ok.setPadding(50,15,0,50);
                TV_cort_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_cort_ok);
            }
            else
            {
                TextView TV_cort_nok = new TextView(OutputVFlexaoActivity.this);
                TV_cort_nok.setText(Html.fromHtml("Perfil NÃO atende à força cortante!"));
                TV_cort_nok.setTextSize(tam_pequeno);
                TV_cort_nok.setPadding(50,15,0,50);
                TV_cort_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_cort_nok);
            }

        }

        if(analise == 2 || analise == 3)
        {

            if (flecha <= flechaadm) //ok
            {
                TextView TV_fle_ok = new TextView(OutputVFlexaoActivity.this);
                TV_fle_ok.setText(Html.fromHtml("Flecha máxima menor que flecha admissível: OK! "));
                TV_fle_ok.setTextSize(tam_pequeno);
                TV_fle_ok.setPadding(50,15,50,50);
                TV_fle_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_fle_ok);
            }
            else
            {
                TextView TV_fle_nok = new TextView(OutputVFlexaoActivity.this);
                TV_fle_nok.setText(Html.fromHtml("NÃO OK! A flecha máxima deve ser menor que a admissível."));
                TV_fle_nok.setTextSize(tam_pequeno);
                TV_fle_nok.setPadding(50,15,50,50);
                TV_fle_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_fle_nok);
            }

        }


    }
    private void Show_Results_SoldadoCustom(DatabaseCustom db, String perfil, int analise, double fy, double d, double tw, double bf, double tf, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVFlexaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVFlexaoActivity.this);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVFlexaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVFlexaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVFlexaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVFlexaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVFlexaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVFlexaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVFlexaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);


        TextView TV_msdx = new TextView(OutputVFlexaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputVFlexaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputVFlexaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(50,15,0,50);
        contorno_1.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

        //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputVFlexaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdx);
            TV_vsdx.setTextColor(Color.BLACK);

            TextView TV_vsdy = new TextView(OutputVFlexaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdy);
            TV_vsdy.setTextColor(Color.BLACK);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputVFlexaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(50,15,0,15);
            contorno_2.addView(TV_flecha);
            TV_flecha.setTextColor(Color.BLACK);

            TextView TV_vao = new TextView(OutputVFlexaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(50,15,0,50);
            contorno_2.addView(TV_vao);
            TV_vao.setTextColor(Color.BLACK);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputVFlexaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputVFlexaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputVFlexaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputVFlexaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputVFlexaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputVFlexaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputVFlexaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputVFlexaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputVFlexaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputVFlexaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputVFlexaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputVFlexaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputVFlexaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputVFlexaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputVFlexaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputVFlexaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputVFlexaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputVFlexaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputVFlexaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputVFlexaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputVFlexaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //ANALISE
        //**resis
        TextView TV_momento = new TextView(OutputVFlexaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        LinearLayout tab_esf = new LinearLayout(OutputVFlexaoActivity.this);
        tab_esf.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_esf);

        LinearLayout col_1_esf = new LinearLayout(OutputVFlexaoActivity.this);
        col_1_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_1_esf);

        LinearLayout col_2_esf = new LinearLayout(OutputVFlexaoActivity.this);
        col_2_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_2_esf);

        TextView TV_mrdx = new TextView(OutputVFlexaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        col_1_esf.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputVFlexaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,50);
        col_1_esf.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

        //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_vrdx = new TextView(OutputVFlexaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(50,15,0,15);
            col_2_esf.addView(TV_vrdx);
            TV_vrdx.setTextColor(Color.BLACK);

            TextView TV_vrdy = new TextView(OutputVFlexaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(50,15,0,50);
            col_2_esf.addView(TV_vrdy);
            TV_vrdy.setTextColor(Color.BLACK);
        }

        //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputVFlexaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);
            TV_flechaadm.setGravity(Gravity.CENTER);
            TV_flechaadm.setTextColor(Color.WHITE);
            TV_flechaadm.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_flechaadm.setPadding(50,20,50,20);

            TextView TV_fadm = new TextView(OutputVFlexaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(50,15,0,50);
            scroll_results.addView(TV_fadm);
            TV_fadm.setTextColor(Color.BLACK);
        }


        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexaoActivity.this);
        TV_verifica.setText("VERIFICAÇÃO");
        TV_verifica.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);
        TV_verifica.setGravity(Gravity.CENTER);
        TV_verifica.setTextColor(Color.WHITE);
        TV_verifica.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verifica.setPadding(50,20,50,20);

        double momento = Momento(msdx,msdy,mrdx,mrdy);

        TextView TV_mom = new TextView(OutputVFlexaoActivity.this);
        TV_mom.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small> = " + casasDecimais(momento,3) ));
        TV_mom.setTextSize(tam_pequeno);
        TV_mom.setPadding(50,15,0,15);
        scroll_results.addView(TV_mom);
        TV_mom.setTextColor(Color.BLACK);

        if (momento <= 1.0) //ok
        {
            TextView TV_mom_ok = new TextView(OutputVFlexaoActivity.this);
            TV_mom_ok.setText(Html.fromHtml("Perfil atende ao momento fletor! "));
            TV_mom_ok.setTextSize(tam_pequeno);
            TV_mom_ok.setPadding(50,15,0,50);
            TV_mom_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_mom_ok);
        }
        else
        {
            TextView TV_mom_nok = new TextView(OutputVFlexaoActivity.this);
            TV_mom_nok.setText(Html.fromHtml("Perfil NÃO atende ao momento fletor!"));
            TV_mom_nok.setTextSize(tam_pequeno);
            TV_mom_nok.setPadding(50,15,0,50);
            TV_mom_nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_mom_nok);
        }

        if(analise == 1 || analise == 3)
        {
            double cortante = Cortante(vsdx,vsdy,vrdx,vrdy);

            TextView TV_cort = new TextView(OutputVFlexaoActivity.this);
            TV_cort.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> / V<small><sub>Rd,x</sub></small>  +  V<small><sub>Sd,y</sub></small> / V<small><sub>Rd,y</sub></small> = " + casasDecimais(cortante,3) ));
            TV_cort.setTextSize(tam_pequeno);
            TV_cort.setPadding(50,15,0,15);
            scroll_results.addView(TV_cort);
            TV_cort.setTextColor(Color.BLACK);

            if (cortante <= 1.0) //ok
            {
                TextView TV_cort_ok = new TextView(OutputVFlexaoActivity.this);
                TV_cort_ok.setText(Html.fromHtml("Perfil atende à força cortante! "));
                TV_cort_ok.setTextSize(tam_pequeno);
                TV_cort_ok.setPadding(50,15,0,50);
                TV_cort_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_cort_ok);
            }
            else
            {
                TextView TV_cort_nok = new TextView(OutputVFlexaoActivity.this);
                TV_cort_nok.setText(Html.fromHtml("Perfil NÃO atende à força cortante!"));
                TV_cort_nok.setTextSize(tam_pequeno);
                TV_cort_nok.setPadding(50,15,0,50);
                TV_cort_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_cort_nok);
            }

        }

        if(analise == 2 || analise == 3)
        {

            if (flecha <= flechaadm) //ok
            {
                TextView TV_fle_ok = new TextView(OutputVFlexaoActivity.this);
                TV_fle_ok.setText(Html.fromHtml("Flecha máxima menor que flecha admissível: OK! "));
                TV_fle_ok.setTextSize(tam_pequeno);
                TV_fle_ok.setPadding(50,15,50,50);
                TV_fle_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_fle_ok);
            }
            else
            {
                TextView TV_fle_nok = new TextView(OutputVFlexaoActivity.this);
                TV_fle_nok.setText(Html.fromHtml("NÃO OK! A flecha máxima deve ser menor que a admissível."));
                TV_fle_nok.setTextSize(tam_pequeno);
                TV_fle_nok.setPadding(50,15,50,50);
                TV_fle_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_fle_nok);
            }

        }

    }
    private void Show_Kc_erro(double kc, double h, double tw)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexaoActivity.this);
        TV_perfil.setText("ERRO - PERFIL CUSTOMIZADO\n");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_Nok));
        scroll_results.addView(TV_perfil);

        ImageView IV_warning = new ImageView(OutputVFlexaoActivity.this);
        IV_warning.setImageResource(android.R.drawable.ic_delete);
        scroll_results.addView(IV_warning);

        TextView TV_kc = new TextView(OutputVFlexaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText("\n\nKc = 4/( √(h/tw) ) \n= 4/( √(" + casasDecimais(h,2) + "/" + casasDecimais(tw,2) + ") ) \n= " + casasDecimais(kc,2));
        TV_kc.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);

        TextView TV_restricao = new TextView(OutputVFlexaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText("\nE Kc precisa atender a restrição :\n 0.35 ≤ Kc ≤ 0.76");
        TV_restricao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);


    }
    private void Show_lambdab_erro(double lambda_b, double fy, double bf, double tf, double lim)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexaoActivity.this);
        TV_perfil.setText("ERRO - VIGA ESBELTA\n");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_Nok));
        scroll_results.addView(TV_perfil);

        ImageView IV_warning = new ImageView(OutputVFlexaoActivity.this);
        IV_warning.setImageResource(android.R.drawable.ic_delete);
        scroll_results.addView(IV_warning);

        TextView TV_kc = new TextView(OutputVFlexaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText(Html.fromHtml("<br/><br/> λ<small><sub>b</sub></small> = 0.5*( bf/tf ) <br/>= 0.5*( " + casasDecimais(bf,2) + "/" + casasDecimais(tf,2) + " ) <br/>= " + casasDecimais(lambda_b,2)));
        TV_kc.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);

        TextView TV_restricao = new TextView(OutputVFlexaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText(Html.fromHtml("<br/><br/>E  λ<small><sub>b</sub></small> precisa atender a restrição :<br/><br/>  λ<small><sub>b</sub></small> ≤ 5.70*( √(E_aco/(fy/10)) ) <br/>" +
                "<br/>λ<small><sub>b</sub></small> ≤ 5.70*( √(20000/(" + casasDecimais(fy,2) + "/10) )\n" +
                "<br/>λ<small><sub>b</sub></small> ≤ " + casasDecimais(lim,3)));
        TV_restricao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);


    }

}
