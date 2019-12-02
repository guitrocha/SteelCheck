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

public class OutputVFlexocompressaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;
    private final double E_aco = 20000.0; // kN/cm²
    private final double G = 7700.0; // kN/cm²
    final int tam_grande = 20, tam_pequeno = 16, tam_dimens = 13;

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
        setContentView(R.layout.activity_output_vflexocompressao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            double msdx = extras.getDouble("msdx");
            double msdy = extras.getDouble("msdy");
            double lb = extras.getDouble("lb");
            double cb = extras.getDouble("cb");
            double NCSD = extras.getDouble("ncsd");
            double fy = extras.getDouble("fy");
            double kx = extras.getDouble("kx");
            double ky = extras.getDouble("ky");
            double kz = extras.getDouble("kz");
            double lx = extras.getDouble("lx");
            double ly = extras.getDouble("ly");
            double lz = extras.getDouble("lz");
            int secao = extras.getInt("secao");
            boolean isAmp = extras.getBoolean("amp");
            double cmx = 0;
            double cmy = 0;
            double msdxmax = 0;
            double msdymax = 0;
            double b1x = 0;
            double b1y = 0;

            OutputVCompressaoActivity comp = new OutputVCompressaoActivity();
            OutputVFlexaoActivity flex = new OutputVFlexaoActivity();
            if (isAmp) {
                cmx = extras.getDouble("cmx");
                cmy = extras.getDouble("cmy");
            }


            if (secao == 1) {
                DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                database.open();
                database.order_by("id");
                int perfil = extras.getInt("perfil");
                //flexao
                double FLM_lambda_b = flex.FLM_lambda_b(database.get_aba(perfil));
                double FLM_lambda_p = flex.FLM_lambda_p(fy);
                double FLM_lambda_r = flex.FLM_lambda_r_LaminadoW(fy);
                double mpx = flex.Mpx(database.get_zx(perfil), fy);
                double mpy = flex.Mpy(database.get_zy(perfil), fy);
                double FLM_mrx = flex.FLM_Mrx(database.get_wx(perfil), fy);
                double FLM_mry = flex.FLM_Mry(database.get_wy(perfil), fy);
                double FLM_mnx = flex.Mn_FLM_x_LaminadoW(mpx, FLM_mrx, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.get_wx(perfil));
                double FLM_mny = flex.Mn_FLM_y_LaminadoW(mpy, FLM_mry, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.get_wy(perfil));
                String FLM = flex.FLM(FLM_lambda_b, FLM_lambda_p, FLM_lambda_r);
                double FLA_lambda_b = flex.FLM_lambda_b(database.get_mesa(perfil));
                double FLA_lambda_p = flex.FLA_lambda_p(fy);
                double FLA_lambda_r = flex.FLA_lambda_r(fy);
                double FLA_mr = flex.FLA_Mrx(database.get_wx(perfil), fy);
                double FLA_mn = flex.FLA_Mn(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, mpx, FLA_mr);
                String FLA = flex.FLA(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r);
                double FLT_lambda_b = flex.FLT_lambda_b(lb, database.get_ry(perfil));
                double FLT_l_p = flex.FLT_l_p(database.get_ry(perfil), fy);
                double FLT_lambda_p = flex.FLT_lambda_p(FLT_l_p, database.get_ry(perfil));
                double FLT_l_r = flex.FLT_l_r(database.get_iy(perfil), database.get_j(perfil), database.get_ry(perfil), database.get_cw(perfil), fy, database.get_wx(perfil));
                double FLT_lambda_r = flex.FLT_lambda_r(FLT_l_r, database.get_ry(perfil));
                double FLT_mn = flex.FLT_Mn(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r, mpx, FLM_mrx, cb, database.get_iy(perfil), database.get_cw(perfil), database.get_j(perfil), lb);
                String FLT = flex.FLT(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r);
                double mrdx = flex.Mrdx(mpx, FLM_mnx, FLA_mn, FLT_mn);
                double mrdy = flex.Mrdy(mpy, FLM_mny);
                //compressao
                double qa = comp.Qa(fy, database.get_mesa(perfil), database.get_tw(perfil), database.get_area(perfil), database.get_dlinha(perfil));
                double qs = comp.Qs_laminado(fy, database.get_aba(perfil));
                double q = comp.Q(qa, qs);
                double nex = comp.Nex(database.get_ix(perfil), kx, lx);
                double ney = comp.Ney(database.get_iy(perfil), ky, ly);
                double nez = comp.Nez(database.get_rx(perfil), database.get_ry(perfil), database.get_cw(perfil), kz, lz, database.get_j(perfil));
                double ne = comp.Ne(nex, ney, nez);
                double esbx = comp.esbeltez_x(kx, lx, database.get_rx(perfil));
                double esby = comp.esbeltez_y(ky, ly, database.get_ry(perfil));
                double esb = comp.esbeltez_final(esbx, esby);
                double esbzero = comp.esbeltez_zero(q, database.get_area(perfil), fy, ne);
                double X = comp.X(esbzero);
                double NCRD = comp.NCRD(X, q, database.get_area(perfil), fy);
                double coef = comp.Coeficiente_Utilização(NCSD, NCRD);
                double momento = 0;
                if(isAmp)
                {
                    b1x = beta1x(cmx,NCSD,nex);
                    b1y = beta1y(cmy,NCSD,ney);
                    msdxmax = msdxMAX(b1x,msdx);
                    msdymax = msdyMAX(b1y,msdy);
                    momento = flex.Momento(msdxmax,msdymax,mrdx,mrdy);
                }
                else
                {
                    momento = flex.Momento(msdx,msdy,mrdx,mrdy);
                }
                double verificacao = verificacao(coef,momento);

                if (FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b, fy, database.get_bf(perfil), database.get_tf(perfil), FLA_lambda_r);
                else
                    Show_Results_LaminadoW(database,perfil,database.get_perfil(perfil), isAmp, fy,
                            msdx, msdy, cb, cmx, cmy,
                            FLM, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, FLM_mnx, FLM_mny,
                            FLA, FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, FLA_mn,
                            FLT, lb, FLT_lambda_b, FLT_l_p, FLT_lambda_p, FLT_l_r, FLT_lambda_r, FLT_mn, mrdx, mrdy, database.get_rx(perfil), database.get_area(perfil), NCSD, kx, ky, kz, lx, ly, lz,
                            qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, b1x, b1y, msdxmax, msdymax, coef, verificacao);
                database.close();
            } else if (secao == 2) {
                double d = extras.getDouble("d");
                double tw = extras.getDouble("tw");
                double bf = extras.getDouble("bf");
                double tf = extras.getDouble("tf");
                DatabaseCustom database = new DatabaseCustom();
                database.calcularValores(d, tw, bf, tf);
                //flexao
                double FLM_lambda_b = flex.FLM_lambda_b(database.getAba());
                double FLM_lambda_p = flex.FLM_lambda_p(fy);
                double kc = flex.Kc(database.getH(), tw);
                double FLM_lambda_r = flex.FLM_lambda_r_SoldadoCustom(fy, kc);
                double mpx = flex.Mpx(database.getZx(), fy);
                double mpy = flex.Mpy(database.getZy(), fy);
                double FLM_mrx = flex.FLM_Mrx(database.getWx(), fy);
                double FLM_mry = flex.FLM_Mry(database.getWy(), fy);
                double FLM_mnx = flex.Mn_FLM_x_SoldadoCustom(mpx, FLM_mrx, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.getWx(), kc);
                double FLM_mny = flex.Mn_FLM_y_SoldadoCustom(mpy, FLM_mry, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.getWy(), kc);
                String FLM = flex.FLM(FLM_lambda_b, FLM_lambda_p, FLM_lambda_r);
                double FLA_lambda_b = flex.FLM_lambda_b(database.getMesa());
                double FLA_lambda_p = flex.FLA_lambda_p(fy);
                double FLA_lambda_r = flex.FLA_lambda_r(fy);
                double FLA_mr = flex.FLA_Mrx(database.getWx(), fy);
                double FLA_mn = flex.FLA_Mn(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, mpx, FLA_mr);
                String FLA = flex.FLA(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r);
                double FLT_lambda_b = flex.FLT_lambda_b(lb, database.getRy());
                double FLT_l_p = flex.FLT_l_p(database.getRy(), fy);
                double FLT_lambda_p = flex.FLT_lambda_p(FLT_l_p, database.getRy());
                double FLT_l_r = flex.FLT_l_r(database.getIy(), database.getJ(), database.getRy(), database.getCw(), fy, database.getWx());
                double FLT_lambda_r = flex.FLT_lambda_r(FLT_l_r, database.getRy());
                double FLT_mn = flex.FLT_Mn(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r, mpx, FLM_mrx, cb, database.getIy(), database.getCw(), database.getJ(), lb);
                String FLT = flex.FLT(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r);
                double mrdx = flex.Mrdx(mpx, FLM_mnx, FLA_mn, FLT_mn);
                double mrdy = flex.Mrdy(mpy, FLM_mny);
                System.out.println("kc = " + kc);
                //compressao
                double qa = comp.Qa(fy, database.getMesa(), tw, database.getAg(), database.getH());
                double qs = comp.Qs_custom(fy, database.getAba(), kc);
                double q = comp.Q(qa, qs);
                double nex = comp.Nex(database.getIx(), kx, lx);
                double ney = comp.Ney(database.getIy(), ky, ly);
                double nez = comp.Nez(database.getRx(), database.getRy(), database.getCw(), kz, lz, database.getJ());
                double ne = comp.Ne(nex, ney, nez);
                double esbx = comp.esbeltez_x(kx, lx, database.getRx());
                double esby = comp.esbeltez_y(ky, ly, database.getRy());
                double esb = comp.esbeltez_final(esbx, esby);
                double esbzero = comp.esbeltez_zero(q, database.getAg(), fy, ne);
                double X = comp.X(esbzero);
                double NCRD = comp.NCRD(X, q, database.getAg(), fy);
                double coef = comp.Coeficiente_Utilização(NCSD, NCRD);
                double momento = 0;
                if(isAmp)
                {
                    b1x = beta1x(cmx,NCSD,nex);
                    b1y = beta1y(cmy,NCSD,ney);
                    msdxmax = msdxMAX(b1x,msdx);
                    msdymax = msdyMAX(b1y,msdy);
                    momento = flex.Momento(msdxmax,msdymax,mrdx,mrdy);
                }
                else
                {
                    momento = flex.Momento(msdx,msdy,mrdx,mrdy);
                }
                double verificacao = verificacao(coef,momento);
                if(FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b,fy,bf,tf,FLA_lambda_r);
                else if(kc <0.35 || kc >0.76)
                    Show_Kc_erro(kc,database.getH(),tw);
                else
                    Show_Results_SoldadoCustom(database,"CUSTOMIZADO",isAmp,fy,d,tw,bf,tf,database.getRy(),database.getZx(),database.getIy(),database.getJ()
                        ,database.getCw(),database.getWx(),database.getMesa(),database.getAba(),
                        msdx,msdy,cb,cmx,cmy,
                        FLM,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,FLM_mnx,FLM_mny,
                        FLA,FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,FLA_mn,
                        FLT,lb,FLT_lambda_b,FLT_l_p,FLT_lambda_p,FLT_l_r,FLT_lambda_r,FLT_mn,mrdx,mrdy,database.getRx(),database.getAg(),NCSD, kx, ky, kz, lx, ly, lz,
                            qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, b1x, b1y, msdxmax, msdymax, coef, verificacao);

            }
        }

    }


    //VERIFICACOES E CONTAS
    double verificacao(double coef, double momento)
    {   double retorno = 0;

        if(coef >= 0.2)
            retorno = verificacao1(coef,momento);
        else
            retorno = verificacao2(coef,momento);

        return retorno;
    }
    double verificacao1(double coef, double momento)
    {
        return (coef) + ((8.0/9.0)*(momento));
    }
    double verificacao2(double coef, double momento)
    {
        return ((1.0/2.0)*(coef)) + (momento);
    }
    double beta1x(double cmx, double ncsd, double nex)
    {   double retorno = 1;
        double conta = ( cmx )/( 1 - ( ncsd/nex ) );
        if(conta >= 1)
            retorno = conta;
        return retorno;
    }
    double beta1y(double cmy, double ncsd, double ney)
    {   double retorno = 1;
        double conta = ( cmy )/( 1 - ( ncsd/ney ) );
        if(conta >= 1)
            retorno = conta;
        return retorno;
    }
    double msdxMAX(double b1x, double msdx)
    {
        return b1x*msdx;
    }
    double msdyMAX(double b1y, double msdy)
    {
        return b1y*msdy;
    }
    //ARREDONDAMENTO
    private double casasDecimais(double original, int quant) {
        double valor = original;
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
    private void Show_Results_LaminadoW(DatabaseAccess db, int pos, String perfil, boolean isAmp, double fy,
                                        double msdx, double msdy, double cb, double cmx, double cmy
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double rx, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                        double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double b1x, double b1y, double msdxmax, double msdymax, double coef, double verifvalor)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVFlexocompressaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVFlexocompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVFlexocompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        //comp
        TextView TV_comp = new TextView(OutputVFlexocompressaoActivity.this);
        TV_comp.setText("COMPRESSÃO");
        TV_comp.setTextSize(tam_pequeno);
        TV_comp.setPadding(50,15,0,50);
        contorno_1.addView(TV_comp);
        TV_comp.setTextColor(Color.BLACK);

        TextView TV_ncsd = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd, 2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx, 2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky, 2)));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz, 2)));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,15);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx, 2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_1.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly, 2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_1.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputVFlexocompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz, 2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,50);
        contorno_1.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        //flex
        TextView TV_FLEX = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLEX.setText("FLEXÃO");
        TV_FLEX.setTextSize(tam_pequeno);
        TV_FLEX.setPadding(100,15,0,50);
        contorno_2.addView(TV_FLEX);
        TV_FLEX.setTextColor(Color.BLACK);

        TextView TV_msdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb, 3)));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(100,15,0,15);
        contorno_2.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

        // cmx cmy
        if (isAmp) {
            TextView TV_cmx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cmx.setText(Html.fromHtml("C<small><sub>m,x</sub></small> = " + casasDecimais(cmx, 2)));
            TV_cmx.setTextSize(tam_pequeno);
            TV_cmx.setPadding(100,15,0,15);
            contorno_2.addView(TV_cmx);
            TV_cmx.setTextColor(Color.BLACK);

            TextView TV_cmy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cmy.setText(Html.fromHtml("C<small><sub>m,y</sub></small> = " + casasDecimais(cmy, 2)));
            TV_cmy.setTextSize(tam_pequeno);
            TV_cmy.setPadding(100,15,0,50);
            contorno_2.addView(TV_cmy);
            TV_cmy.setTextColor(Color.BLACK);
        }

        //**solic
        if (isAmp) {
            TextView TV_cortante = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cortante.setText("AMPLIFICAÇÃO DE MOMENTOS FLETORES");
            TV_cortante.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);
            TV_cortante.setGravity(Gravity.CENTER);
            TV_cortante.setTextColor(Color.WHITE);
            TV_cortante.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_cortante.setPadding(50,20,50,20);

            LinearLayout tab_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            tab_solic.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(tab_solic);

            LinearLayout col_1_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            col_1_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_1_solic);

            LinearLayout col_2_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            col_2_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_2_solic);

            TextView TV_msnex = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msnex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex, 2) + " kN"));
            TV_msnex.setTextSize(tam_pequeno);
            TV_msnex.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msnex);
            TV_msnex.setTextColor(Color.BLACK);

            TextView TV_msney = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney, 2) + " kN"));
            TV_msney.setTextSize(tam_pequeno);
            TV_msney.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msney);
            TV_msney.setTextColor(Color.BLACK);

            TextView TV_b1x = new TextView(OutputVFlexocompressaoActivity.this);
            TV_b1x.setText(Html.fromHtml("β<small><sub>1,x</sub></small> = " + casasDecimais(b1x, 3)));
            TV_b1x.setTextSize(tam_pequeno);
            TV_b1x.setPadding(50,15,0,15);
            col_1_solic.addView(TV_b1x);
            TV_b1x.setTextColor(Color.BLACK);

            TextView TV_b1y = new TextView(OutputVFlexocompressaoActivity.this);
            TV_b1y.setText(Html.fromHtml("β<small><sub>1,y</sub></small> = " + casasDecimais(b1y, 3)));
            TV_b1y.setTextSize(tam_pequeno);
            TV_b1y.setPadding(50,15,0,50);
            col_1_solic.addView(TV_b1y);
            TV_b1y.setTextColor(Color.BLACK);

            TextView TV_msmsdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msmsdx.setTextSize(tam_pequeno);
            TV_msmsdx.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdx);
            TV_msmsdx.setTextColor(Color.BLACK);

            TextView TV_msmsdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msmsdy.setTextSize(tam_pequeno);
            TV_msmsdy.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdy);
            TV_msmsdy.setTextColor(Color.BLACK);

            TextView TV_msmsdxmax = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdxmax.setText(Html.fromHtml("M<small><sub>Sd,max,x</sub></small> = " + casasDecimais(msdxmax, 2) + " kNm"));
            TV_msmsdxmax.setTextSize(tam_pequeno);
            TV_msmsdxmax.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdxmax);
            TV_msmsdxmax.setTextColor(Color.BLACK);

            TextView TV_msmsdymax = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdymax.setText(Html.fromHtml("M<small><sub>Sd,max,y</sub></small> = " + casasDecimais(msdymax, 2) + " kNm"));
            TV_msmsdymax.setTextSize(tam_pequeno);
            TV_msmsdymax.setPadding(50,15,0,50);
            col_2_solic.addView(TV_msmsdymax);
            TV_msmsdymax.setTextColor(Color.BLACK);
        }



        //FLM
        TextView TV_FLM = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //flambagem
        TextView TV_flamblocal = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        LinearLayout tab_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_loc.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_loc);

        LinearLayout col_1_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_1_loc);

        LinearLayout col_2_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_2_loc);

        TextView TV_locfla = new TextView(OutputVFlexocompressaoActivity.this);
        TV_locfla.setText("FLA\n"+FLA);
        TV_locfla.setTextSize(tam_pequeno);
        TV_locfla.setPadding(50,15,0,15);
        col_1_loc.addView(TV_locfla);
        TV_locfla.setTextColor(Color.BLACK);

        TextView TV_locflm = new TextView(OutputVFlexocompressaoActivity.this);
        TV_locflm.setText("FLM\n"+FLM);
        TV_locflm.setTextSize(tam_pequeno);
        TV_locflm.setPadding(50,15,0,15);
        col_2_loc.addView(TV_locflm);
        TV_locflm.setTextColor(Color.BLACK);

        TextView TV_Qa = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        col_1_loc.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        col_2_loc.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        col_1_loc.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputVFlexocompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputVFlexocompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputVFlexocompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputVFlexocompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputVFlexocompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esb.setText("ESBELTEZ GLOBAL");
        TV_esb.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esb.setTextSize(tam_grande);
        scroll_results.addView(TV_esb);
        TV_esb.setGravity(Gravity.CENTER);
        TV_esb.setTextColor(Color.WHITE);
        TV_esb.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esb.setPadding(50,20,50,20);

        TextView TV_esbX = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbX.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,3) ));
        TV_esbX.setTextSize(tam_pequeno);
        TV_esbX.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbX);
        TV_esbX.setTextColor(Color.BLACK);

        TextView TV_esbY = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbY.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,3) ));
        TV_esbY.setTextSize(tam_pequeno);
        TV_esbY.setPadding(50,15,0,50);
        scroll_results.addView(TV_esbY);
        TV_esbY.setTextColor(Color.BLACK);

        //ANALISE

        //**resis
        TextView TV_momento = new TextView(OutputVFlexocompressaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        TextView TV_mrdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

        TextView TV_compNCRD = new TextView(OutputVFlexocompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd, 2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);



        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexocompressaoActivity.this);
        TV_verifica.setText("VERIFICAÇÕES");
        TV_verifica.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);
        TV_verifica.setGravity(Gravity.CENTER);
        TV_verifica.setTextColor(Color.WHITE);
        TV_verifica.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verifica.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVFlexocompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3)));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        if(coef >= 0.2)
        {
            TextView TV_verif_valor = new TextView(OutputVFlexocompressaoActivity.this);
            TV_verif_valor.setText(Html.fromHtml("(N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small>)  +  (8/9)*(M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small>)  = " + casasDecimais(verifvalor,3)));
            TV_verif_valor.setTextSize(tam_pequeno);
            TV_verif_valor.setPadding(50,15,0,50);
            scroll_results.addView(TV_verif_valor);
            TV_verif_valor.setTextColor(Color.BLACK);
        }
        else
        {
            TextView TV_verif_valor = new TextView(OutputVFlexocompressaoActivity.this);
            TV_verif_valor.setText(Html.fromHtml("(1/2)*(N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small>)  +  (M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small>)  = " + casasDecimais(verifvalor,3)));
            TV_verif_valor.setTextSize(tam_pequeno);
            TV_verif_valor.setPadding(50,15,0,50);
            scroll_results.addView(TV_verif_valor);
            TV_verif_valor.setTextColor(Color.BLACK);
        }

        if(verifvalor <= 1.0) //ok
        {
            TextView TV_ok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_ok.setText(Html.fromHtml("Perfil OK! "));
            TV_ok.setTextSize(tam_pequeno);
            TV_ok.setPadding(50,15,0,50);
            TV_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_ok);
        }
        else //n ok
        {
            TextView TV_Nok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_Nok.setText(Html.fromHtml("Perfil NÃO OK! "));
            TV_Nok.setTextSize(tam_pequeno);
            TV_Nok.setPadding(50,15,0,50);
            TV_Nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_Nok);
        }


    }
    private void Show_Results_SoldadoCustom(DatabaseCustom db, String perfil, boolean isAmp, double fy, double d, double tw, double bf, double tf, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double cmx, double cmy
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double rx, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                            double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                            double esbzero, double X, double ncrd, double b1x, double b1y, double msdxmax, double msdymax, double coef, double verifvalor)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVFlexocompressaoActivity.this);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVFlexocompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVFlexocompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVFlexocompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        //comp
        TextView TV_comp = new TextView(OutputVFlexocompressaoActivity.this);
        TV_comp.setText("COMPRESSÃO");
        TV_comp.setTextSize(tam_pequeno);
        TV_comp.setPadding(50,15,0,50);
        contorno_1.addView(TV_comp);
        TV_comp.setTextColor(Color.BLACK);

        TextView TV_ncsd = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd, 2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx, 2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky, 2)));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz, 2)));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,15);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx, 2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_1.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly, 2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_1.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputVFlexocompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz, 2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,50);
        contorno_1.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        //flex
        TextView TV_FLEX = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLEX.setText("FLEXÃO");
        TV_FLEX.setTextSize(tam_pequeno);
        TV_FLEX.setPadding(100,15,0,50);
        contorno_2.addView(TV_FLEX);
        TV_FLEX.setTextColor(Color.BLACK);

        TextView TV_msdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb, 3)));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(100,15,0,15);
        contorno_2.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

        // cmx cmy
        if (isAmp) {
            TextView TV_cmx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cmx.setText(Html.fromHtml("C<small><sub>m,x</sub></small> = " + casasDecimais(cmx, 2)));
            TV_cmx.setTextSize(tam_pequeno);
            TV_cmx.setPadding(100,15,0,15);
            contorno_2.addView(TV_cmx);
            TV_cmx.setTextColor(Color.BLACK);

            TextView TV_cmy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cmy.setText(Html.fromHtml("C<small><sub>m,y</sub></small> = " + casasDecimais(cmy, 2)));
            TV_cmy.setTextSize(tam_pequeno);
            TV_cmy.setPadding(100,15,0,50);
            contorno_2.addView(TV_cmy);
            TV_cmy.setTextColor(Color.BLACK);
        }

        //**solic
        if (isAmp) {
            TextView TV_cortante = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cortante.setText("AMPLIFICAÇÃO DE MOMENTOS FLETORES");
            TV_cortante.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);
            TV_cortante.setGravity(Gravity.CENTER);
            TV_cortante.setTextColor(Color.WHITE);
            TV_cortante.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_cortante.setPadding(50,20,50,20);

            LinearLayout tab_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            tab_solic.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(tab_solic);

            LinearLayout col_1_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            col_1_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_1_solic);

            LinearLayout col_2_solic = new LinearLayout(OutputVFlexocompressaoActivity.this);
            col_2_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_2_solic);

            TextView TV_msnex = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msnex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex, 2) + " kN"));
            TV_msnex.setTextSize(tam_pequeno);
            TV_msnex.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msnex);
            TV_msnex.setTextColor(Color.BLACK);

            TextView TV_msney = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney, 2) + " kN"));
            TV_msney.setTextSize(tam_pequeno);
            TV_msney.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msney);
            TV_msney.setTextColor(Color.BLACK);

            TextView TV_b1x = new TextView(OutputVFlexocompressaoActivity.this);
            TV_b1x.setText(Html.fromHtml("β<small><sub>1,x</sub></small> = " + casasDecimais(b1x, 3)));
            TV_b1x.setTextSize(tam_pequeno);
            TV_b1x.setPadding(50,15,0,15);
            col_1_solic.addView(TV_b1x);
            TV_b1x.setTextColor(Color.BLACK);

            TextView TV_b1y = new TextView(OutputVFlexocompressaoActivity.this);
            TV_b1y.setText(Html.fromHtml("β<small><sub>1,y</sub></small> = " + casasDecimais(b1y, 3)));
            TV_b1y.setTextSize(tam_pequeno);
            TV_b1y.setPadding(50,15,0,50);
            col_1_solic.addView(TV_b1y);
            TV_b1y.setTextColor(Color.BLACK);

            TextView TV_msmsdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msmsdx.setTextSize(tam_pequeno);
            TV_msmsdx.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdx);
            TV_msmsdx.setTextColor(Color.BLACK);

            TextView TV_msmsdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msmsdy.setTextSize(tam_pequeno);
            TV_msmsdy.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdy);
            TV_msmsdy.setTextColor(Color.BLACK);

            TextView TV_msmsdxmax = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdxmax.setText(Html.fromHtml("M<small><sub>Sd,max,x</sub></small> = " + casasDecimais(msdxmax, 2) + " kNm"));
            TV_msmsdxmax.setTextSize(tam_pequeno);
            TV_msmsdxmax.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdxmax);
            TV_msmsdxmax.setTextColor(Color.BLACK);

            TextView TV_msmsdymax = new TextView(OutputVFlexocompressaoActivity.this);
            TV_msmsdymax.setText(Html.fromHtml("M<small><sub>Sd,max,y</sub></small> = " + casasDecimais(msdymax, 2) + " kNm"));
            TV_msmsdymax.setTextSize(tam_pequeno);
            TV_msmsdymax.setPadding(50,15,0,50);
            col_2_solic.addView(TV_msmsdymax);
            TV_msmsdymax.setTextColor(Color.BLACK);
        }



        //FLM
        TextView TV_FLM = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputVFlexocompressaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //flambagem
        TextView TV_flamblocal = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        LinearLayout tab_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        tab_loc.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_loc);

        LinearLayout col_1_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_1_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_1_loc);

        LinearLayout col_2_loc = new LinearLayout(OutputVFlexocompressaoActivity.this);
        col_2_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_2_loc);

        TextView TV_locfla = new TextView(OutputVFlexocompressaoActivity.this);
        TV_locfla.setText("FLA\n"+FLA);
        TV_locfla.setTextSize(tam_pequeno);
        TV_locfla.setPadding(50,15,0,15);
        col_1_loc.addView(TV_locfla);
        TV_locfla.setTextColor(Color.BLACK);

        TextView TV_locflm = new TextView(OutputVFlexocompressaoActivity.this);
        TV_locflm.setText("FLM\n"+FLM);
        TV_locflm.setTextSize(tam_pequeno);
        TV_locflm.setPadding(50,15,0,15);
        col_2_loc.addView(TV_locflm);
        TV_locflm.setTextColor(Color.BLACK);

        TextView TV_Qa = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        col_1_loc.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        col_2_loc.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputVFlexocompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        col_1_loc.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputVFlexocompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputVFlexocompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputVFlexocompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputVFlexocompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputVFlexocompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esb.setText("ESBELTEZ GLOBAL");
        TV_esb.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esb.setTextSize(tam_grande);
        scroll_results.addView(TV_esb);
        TV_esb.setGravity(Gravity.CENTER);
        TV_esb.setTextColor(Color.WHITE);
        TV_esb.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esb.setPadding(50,20,50,20);

        TextView TV_esbX = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbX.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,3) ));
        TV_esbX.setTextSize(tam_pequeno);
        TV_esbX.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbX);
        TV_esbX.setTextColor(Color.BLACK);

        TextView TV_esbY = new TextView(OutputVFlexocompressaoActivity.this);
        TV_esbY.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,3) ));
        TV_esbY.setTextSize(tam_pequeno);
        TV_esbY.setPadding(50,15,0,50);
        scroll_results.addView(TV_esbY);
        TV_esbY.setTextColor(Color.BLACK);

        //ANALISE

        //**resis
        TextView TV_momento = new TextView(OutputVFlexocompressaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        TextView TV_mrdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

        TextView TV_compNCRD = new TextView(OutputVFlexocompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd, 2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);



        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexocompressaoActivity.this);
        TV_verifica.setText("VERIFICAÇÕES");
        TV_verifica.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);
        TV_verifica.setGravity(Gravity.CENTER);
        TV_verifica.setTextColor(Color.WHITE);
        TV_verifica.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verifica.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVFlexocompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3)));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        if(coef >= 0.2)
        {
            TextView TV_verif_valor = new TextView(OutputVFlexocompressaoActivity.this);
            TV_verif_valor.setText(Html.fromHtml("(N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small>)  +  (8/9)*(M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small>)  = " + casasDecimais(verifvalor,3)));
            TV_verif_valor.setTextSize(tam_pequeno);
            TV_verif_valor.setPadding(50,15,0,50);
            scroll_results.addView(TV_verif_valor);
            TV_verif_valor.setTextColor(Color.BLACK);
        }
        else
        {
            TextView TV_verif_valor = new TextView(OutputVFlexocompressaoActivity.this);
            TV_verif_valor.setText(Html.fromHtml("(1/2)*(N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small>)  +  (M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small>)  = " + casasDecimais(verifvalor,3)));
            TV_verif_valor.setTextSize(tam_pequeno);
            TV_verif_valor.setPadding(50,15,0,50);
            scroll_results.addView(TV_verif_valor);
            TV_verif_valor.setTextColor(Color.BLACK);
        }

        if(verifvalor <= 1.0) //ok
        {
            TextView TV_ok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_ok.setText(Html.fromHtml("Perfil OK! "));
            TV_ok.setTextSize(tam_pequeno);
            TV_ok.setPadding(50,15,0,50);
            TV_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_ok);
        }
        else //n ok
        {
            TextView TV_Nok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_Nok.setText(Html.fromHtml("Perfil NÃO OK! "));
            TV_Nok.setTextSize(tam_pequeno);
            TV_Nok.setPadding(50,15,0,50);
            TV_Nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_Nok);
        }


    }
    private void Show_Kc_erro(double kc, double h, double tw)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("ERRO - PERFIL CUSTOMIZADO\n");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_Nok));
        scroll_results.addView(TV_perfil);

        ImageView IV_warning = new ImageView(OutputVFlexocompressaoActivity.this);
        IV_warning.setImageResource(android.R.drawable.ic_delete);
        scroll_results.addView(IV_warning);

        TextView TV_kc = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText("\n\nKc = 4/( √(h/tw) ) \n= 4/( √(" + casasDecimais(h,2) + "/" + casasDecimais(tw,2) + ") ) \n= " + casasDecimais(kc,2));
        TV_kc.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);

        TextView TV_restricao = new TextView(OutputVFlexocompressaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText("\nE Kc precisa atender a restrição :\n 0.35 ≤ Kc ≤ 0.76");
        TV_restricao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);


    }
    private void Show_lambdab_erro(double lambda_b, double fy, double bf, double tf, double lim)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("ERRO - VIGA ESBELTA\n");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_Nok));
        scroll_results.addView(TV_perfil);

        ImageView IV_warning = new ImageView(OutputVFlexocompressaoActivity.this);
        IV_warning.setImageResource(android.R.drawable.ic_delete);
        scroll_results.addView(IV_warning);

        TextView TV_kc = new TextView(OutputVFlexocompressaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText(Html.fromHtml("<br/><br/> λ<small><sub>b</sub></small> = 0.5*( bf/tf ) <br/>= 0.5*( " + casasDecimais(bf,2) + "/" + casasDecimais(tf,2) + " ) <br/>= " + casasDecimais(lambda_b,2)));
        TV_kc.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);

        TextView TV_restricao = new TextView(OutputVFlexocompressaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText(Html.fromHtml("<br/><br/>E  λ<small><sub>b</sub></small> precisa atender a restrição :<br/><br/>  λ<small><sub>b</sub></small> ≤ 5.70*( √(E_aco/(fy/10)) ) <br/>" +
                "<br/>λ<small><sub>b</sub></small> ≤ 5.70*( √(20000/(" + casasDecimais(fy,2) + "/10) )\n" +
                "<br/>λ<small><sub>b</sub></small> ≤ " + casasDecimais(lim,3)));
        TV_restricao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);


    }

}
