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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class OutputDFlexocompressaoActivity extends AppCompatActivity {

    final int tam_grande = 25, tam_pequeno = 18;

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
        setContentView(R.layout.activity_output_dflexocompressao);
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
            String ordem = extras.getString("ordem");
            boolean isAmp = extras.getBoolean("amp");
            double cmx = 0;
            double cmy = 0;
            double msdxmax = 0;
            double msdymax = 0;
            double b1x = 0;
            double b1y = 0;

            OutputVCompressaoActivity comp = new OutputVCompressaoActivity();
            OutputVFlexaoActivity flex = new OutputVFlexaoActivity();
            OutputVFlexocompressaoActivity fc = new OutputVFlexocompressaoActivity();
            if (isAmp) {
                cmx = extras.getDouble("cmx");
                cmy = extras.getDouble("cmy");
            }


            DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
            database.open();
            database.order_by(ordem);
            double FLM_lambda_b = 0, FLM_lambda_p = 0,FLM_lambda_r=0,mpx=0,mpy=0,FLM_mrx=0,
                    FLM_mry=0,FLM_mnx=0,FLM_mny=0,FLA_lambda_b=0,FLA_lambda_p=0,FLA_lambda_r=0,
                    FLA_mr=0,FLA_mn=0,FLT_lambda_b=0,FLT_l_p=0,FLT_lambda_p=0,FLT_l_r=0,FLT_lambda_r=0,
                    FLT_mn=0,mrdx=0,mrdy=0,vrdx=0,vrdy=0,flechaadm=0,flechareal=0;
            double qa=0,qs=0,q=0,nex=0,ney=0,nez=0,ne=0,esbx=0,esby=0,esb=0,esbzero=0,X=0,NCRD=0,coef=0;
            String FLA="",FLT="",FLM="";
            int i;
            long count = database.quantTuplas();
            boolean flag = false;
            for( i=1 ; i < count && flag == false ; ++i ) {
                //flexao
                FLM_lambda_b = flex.FLM_lambda_b(database.get_aba(i));
                FLM_lambda_p = flex.FLM_lambda_p(fy);
                FLM_lambda_r = flex.FLM_lambda_r_LaminadoW(fy);
                mpx = flex.Mpx(database.get_zx(i), fy);
                mpy = flex.Mpy(database.get_zy(i), fy);
                FLM_mrx = flex.FLM_Mrx(database.get_wx(i), fy);
                FLM_mry = flex.FLM_Mry(database.get_wy(i), fy);
                FLM_mnx = flex.Mn_FLM_x_LaminadoW(mpx, FLM_mrx, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.get_wx(i));
                FLM_mny = flex.Mn_FLM_y_LaminadoW(mpy, FLM_mry, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, database.get_wy(i));
                FLM = flex.FLM(FLM_lambda_b, FLM_lambda_p, FLM_lambda_r);
                FLA_lambda_b = flex.FLM_lambda_b(database.get_mesa(i));
                FLA_lambda_p = flex.FLA_lambda_p(fy);
                FLA_lambda_r = flex.FLA_lambda_r(fy);
                FLA_mr = flex.FLA_Mrx(database.get_wx(i), fy);
                FLA_mn = flex.FLA_Mn(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, mpx, FLA_mr);
                FLA = flex.FLA(FLA_lambda_b, FLA_lambda_p, FLA_lambda_r);
                FLT_lambda_b = flex.FLT_lambda_b(lb, database.get_ry(i));
                FLT_l_p = flex.FLT_l_p(database.get_ry(i), fy);
                FLT_lambda_p = flex.FLT_lambda_p(FLT_l_p, database.get_ry(i));
                FLT_l_r = flex.FLT_l_r(database.get_iy(i), database.get_j(i), database.get_ry(i), database.get_cw(i), fy, database.get_wx(i));
                FLT_lambda_r = flex.FLT_lambda_r(FLT_l_r, database.get_ry(i));
                FLT_mn = flex.FLT_Mn(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r, mpx, FLM_mrx, cb, database.get_iy(i), database.get_cw(i), database.get_j(i), lb);
                FLT = flex.FLT(FLT_lambda_b, FLT_lambda_p, FLT_lambda_r);
                mrdx = flex.Mrdx(mpx, FLM_mnx, FLA_mn, FLT_mn);
                mrdy = flex.Mrdy(mpy, FLM_mny);
                //compressao
                qa = comp.Qa(fy, database.get_mesa(i), database.get_tw(i), database.get_area(i), database.get_dlinha(i));
                qs = comp.Qs_laminado(fy, database.get_aba(i));
                q = comp.Q(qa, qs);
                nex = comp.Nex(database.get_ix(i), kx, lx);
                ney = comp.Ney(database.get_iy(i), ky, ly);
                nez = comp.Nez(database.get_rx(i), database.get_ry(i), database.get_cw(i), kz, lz, database.get_j(i));
                ne = comp.Ne(nex, ney, nez);
                esbx = comp.esbeltez_x(kx, lx, database.get_rx(i));
                esby = comp.esbeltez_y(ky, ly, database.get_ry(i));
                esb = comp.esbeltez_final(esbx, esby);
                esbzero = comp.esbeltez_zero(q, database.get_area(i), fy, ne);
                X = comp.X(esbzero);
                NCRD = comp.NCRD(X, q, database.get_area(i), fy);
                coef = comp.Coeficiente_Utilização(NCSD, NCRD);
                double momento = 0;
                if (isAmp) {
                    b1x = fc.beta1x(cmx, NCSD, nex);
                    b1y = fc.beta1y(cmy, NCSD, ney);
                    msdxmax = fc.msdxMAX(b1x, msdx);
                    msdymax = fc.msdyMAX(b1y, msdy);
                    momento = flex.Momento(msdxmax, msdymax, mrdx, mrdy);
                } else {
                    momento = flex.Momento(msdx, msdy, mrdx, mrdy);
                }
                double verificacao = fc.verificacao(coef, momento);
                flag = verificacao_perfil(verificacao,FLA_lambda_b,FLA_lambda_r);

            }
            i--;
            String show_ordem = "";
            if(flag)
                show_ordem = generate_string_ordem(ordem,database,i);
            Show_Results_LaminadoW(flag,database.get_perfil(i),show_ordem, isAmp, fy, database.get_ry(i), database.get_zx(i), database.get_iy(i), database.get_j(i)
                            , database.get_cw(i), database.get_wx(i), database.get_mesa(i), database.get_aba(i),
                            msdx, msdy, cb, cmx, cmy,
                            FLM, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, FLM_mnx, FLM_mny,
                            FLA, FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, FLA_mn,
                            FLT, lb, FLT_lambda_b, FLT_l_p, FLT_lambda_p, FLT_l_r, FLT_lambda_r, FLT_mn, mrdx, mrdy, database.get_rx(i), database.get_area(i), NCSD, kx, ky, kz, lx, ly, lz,
                            qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, b1x, b1y, msdxmax, msdymax);
            database.close();

        }

    }
    private String generate_string_ordem(String ordem, DatabaseAccess db, int i)
    {
        String show = "";
        switch(ordem)
        {
            case "massa":
                show = "Massa Linear = " + db.get_massa(i) + " kg/m";
                break;
            case "d":
                show = "d = " + db.get_d(i) + " mm";
                break;
            case "bf":
                show = "b<small><sub>f</sub></small> = " + db.get_bf(i) + " mm";
                break;
            case "tf":
                show = "t<small><sub>f</sub></small> = " + db.get_tf(i) + " mm";
                break;
            case "tw":
                show = "t<small><sub>w</sub></small> = " + db.get_tw(i) + " mm";
                break;
            case "area":
                show = "A<small><sub>g</sub></small> = " + db.get_area(i) + " cm²";
                break;
            case "ix":
                show = "I<small><sub>x</sub></small> = " + db.get_ix(i) + " cm<small><sup>4</sup></small>";
                break;
            case "iy":
                show = "I<small><sub>y</sub></small> = " + db.get_iy(i) + " cm<small><sup>4</sup></small>";
                break;
            case "wx":
                show = "W<small><sub>x</sub></small> = " + db.get_wx(i) + " cm<small><sup>3</sup></small>";
                break;
            case "wy":
                show = "W<small><sub>y</sub></small> = " + db.get_wy(i) + " cm<small><sup>3</sup></small>";
                break;
            case "zx":
                show = "Z<small><sub>x</sub></small> = " + db.get_zx(i) + " cm<small><sup>3</sup></small>";
                break;
            case "zy":
                show = "Z<small><sub>y</sub></small> = " + db.get_zy(i) + " cm<small><sup>3</sup></small>";
                break;
        }
        return show;
    }
    //ARREDONDAMENTO
    private double casasDecimais(double original, int quant) {
        double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }

    public boolean verificacao_perfil(double verifvalor, double FLA_lambda_b, double FLA_lambda_r)
    {
        boolean flag = true;

        if (FLA_lambda_b > FLA_lambda_r)
            flag = false;

        if(verifvalor <= 1.0) //ok
        {
            flag = true && flag;
        }
        else //n ok
        {
            flag = false;
        }
        return flag;
    }
    //CRIACAO DE LAYOUT
    private void Show_Results_LaminadoW(boolean flag, String perfil, String ordem, boolean isAmp, double fy, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double cmx, double cmy
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double rx, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                        double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double b1x, double b1y, double msdxmax, double msdymax)
    {
        ScrollView sv = new ScrollView(OutputDFlexocompressaoActivity.this);

        LinearLayout scroll_results = new LinearLayout(OutputDFlexocompressaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relflexcomp);
        rl.addView(sv);

        if(!flag)
        {
            TextView ERRO = new TextView(OutputDFlexocompressaoActivity.this);
            ERRO.setText("Nenhum perfil é adequado!");
            scroll_results.addView(ERRO);
            ERRO.setTextColor(getResources().getColor(R.color.color_Nok));
            ERRO.setTextSize(tam_grande);
            return;
        }

        TextView TV_perfil = new TextView(OutputDFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_ok));
        scroll_results.addView(TV_perfil);

        TextView TV_ordem = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ordem.setText(Html.fromHtml(ordem));
        TV_ordem.setTextSize(tam_pequeno);
        TV_ordem.setPadding(0,50,0,15);
        scroll_results.addView(TV_ordem);

        TextView TV_elasticidade = new TextView(OutputDFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0, 50, 0, 15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy, 2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0, 15, 0, 60);
        scroll_results.addView(TV_fy);

        TextView TV_zx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx, 2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0, 100, 0, 15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy, 2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputDFlexocompressaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j, 2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputDFlexocompressaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw, 2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx, 2) + " cm<small><sup>3</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputDFlexocompressaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa, 2)));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputDFlexocompressaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba, 2)));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_aba);

        TextView TV_rx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + casasDecimais(rx, 2) + " cm"));
        TV_rx.setTextSize(tam_pequeno);
        TV_rx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_rx);

        TextView TV_ry = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry, 2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ry);

        TextView TV_ag = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + casasDecimais(ag, 2) + " cm²"));
        TV_ag.setTextSize(tam_pequeno);
        TV_ag.setPadding(0, 15, 0, 60);
        scroll_results.addView(TV_ag);

        //flex
        TextView TV_msdx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_msdx);

        TextView TV_msdy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_msdy);

        TextView TV_cb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb, 3)));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_cb);

        // cmx cmy
        if (isAmp) {
            TextView TV_cmx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cmx.setText(Html.fromHtml("C<small><sub>m,x</sub></small> = " + casasDecimais(cmx, 2)));
            TV_cmx.setTextSize(tam_pequeno);
            TV_cmx.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_cmx);

            TextView TV_cmy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cmy.setText(Html.fromHtml("C<small><sub>m,y</sub></small> = " + casasDecimais(cmy, 2)));
            TV_cmy.setTextSize(tam_pequeno);
            TV_cmy.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_cmy);
        }

        //comp
        TextView TV_ncsd = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd, 2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ncsd);

        TextView TV_kx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx, 2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_kx);

        TextView TV_ky = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky, 2)));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ky);

        TextView TV_kz = new TextView(OutputDFlexocompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz, 2)));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_kz);

        TextView TV_lx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx, 2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_lx);

        TextView TV_ly = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly, 2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ly);

        TextView TV_lz = new TextView(OutputDFlexocompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz, 2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_lz);


        //FLM
        TextView TV_FLM = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLM.setText("FLM - " + FLM);
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        TV_FLM.setPadding(0, 100, 0, 0);
        scroll_results.addView(TV_FLM);

        TextView TV_flm_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flm_b);

        TextView TV_flm_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flm_p);

        TextView TV_flm_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flm_r);

        TextView TV_flm_mnx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flm_mnx);

        TextView TV_flm_mny = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_flm_mny);

        //FLA
        TextView TV_FLA = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLA.setText("FLA - " + FLA);
        TV_FLA.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);

        TextView TV_fla_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b, 2)));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_fla_b);

        TextView TV_fla_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p, 2)));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_fla_p);

        TextView TV_fla_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r, 2)));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_fla_r);

        TextView TV_fla_mn = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla, 2) + " kNm"));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_fla_mn);

        //FLT
        TextView TV_FLT = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLT.setText("FLT - " + FLT);
        TV_FLT.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);

        TextView TV_flt_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb, 2) +
                " cm | λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b, 2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flt_b);

        TextView TV_flt_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp, 2) +
                " cm | λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p, 2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flt_p);

        TextView TV_flt_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr, 2) +
                " cm | λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r, 2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_flt_r);

        TextView TV_flt_cbmn = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt, 2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_flt_cbmn);

        //flambagem
        TextView TV_flamblocal = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);

        TextView TV_Qa = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa, 3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(0, 50, 0, 15);
        scroll_results.addView(TV_Qa);

        TextView TV_Qs = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs, 3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_Qs);

        TextView TV_Q = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q, 3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_Q);

        TextView TV_flambglobal = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);

        TextView TV_esbx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx, 2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(0, 50, 0, 15);
        scroll_results.addView(TV_esbx);

        TextView TV_esby = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby, 2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_esby);

        TextView TV_esb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb, 2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_esb);

        TextView TV_nex = new TextView(OutputDFlexocompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex, 2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(0, 100, 0, 15);
        scroll_results.addView(TV_nex);

        TextView TV_ney = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney, 2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ney);

        TextView TV_nez = new TextView(OutputDFlexocompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez, 2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_nez);

        TextView TV_ne = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne, 2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_ne);

        TextView TV_esbzero = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero, 3)));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(0, 100, 0, 15);
        scroll_results.addView(TV_esbzero);

        TextView TV_flamb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X, 3)));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_flamb);

        //ANALISE

        //**resis
        TextView TV_momento = new TextView(OutputDFlexocompressaoActivity.this);
        TV_momento.setText("MOMENTO E NORMAL RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);

        TextView TV_mrdx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_mrdx);

        TextView TV_mrdy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(0, 15, 0, 15);
        scroll_results.addView(TV_mrdy);

        TextView TV_compNCRD = new TextView(OutputDFlexocompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd, 2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(0, 15, 0, 100);
        scroll_results.addView(TV_compNCRD);

        //**solic
        TextView TV_cortante = new TextView(OutputDFlexocompressaoActivity.this);
        TV_cortante.setText("MOMENTOS SOLICITANTES");
        TV_cortante.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_cortante.setTextSize(tam_grande);
        scroll_results.addView(TV_cortante);

        if (isAmp) {
            TextView TV_msnex = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msnex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex, 2) + " kN"));
            TV_msnex.setTextSize(tam_pequeno);
            TV_msnex.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msnex);

            TextView TV_msney = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney, 2) + " kN"));
            TV_msney.setTextSize(tam_pequeno);
            TV_msney.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msney);

            TextView TV_b1x = new TextView(OutputDFlexocompressaoActivity.this);
            TV_b1x.setText(Html.fromHtml("β<small><sub>1,x</sub></small> = " + casasDecimais(b1x, 3)));
            TV_b1x.setTextSize(tam_pequeno);
            TV_b1x.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_b1x);

            TextView TV_b1y = new TextView(OutputDFlexocompressaoActivity.this);
            TV_b1y.setText(Html.fromHtml("β<small><sub>1,y</sub></small> = " + casasDecimais(b1y, 3)));
            TV_b1y.setTextSize(tam_pequeno);
            TV_b1y.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_b1y);

            TextView TV_msmsdx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msmsdx.setTextSize(tam_pequeno);
            TV_msmsdx.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msmsdx);

            TextView TV_msmsdy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msmsdy.setTextSize(tam_pequeno);
            TV_msmsdy.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msmsdy);

            TextView TV_msmsdxmax = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdxmax.setText(Html.fromHtml("M<small><sub>Sd,max,x</sub></small> = " + casasDecimais(msdxmax, 2) + " kNm"));
            TV_msmsdxmax.setTextSize(tam_pequeno);
            TV_msmsdxmax.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msmsdxmax);

            TextView TV_msmsdymax = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdymax.setText(Html.fromHtml("M<small><sub>Sd,max,y</sub></small> = " + casasDecimais(msdymax, 2) + " kNm"));
            TV_msmsdymax.setTextSize(tam_pequeno);
            TV_msmsdymax.setPadding(0, 15, 0, 100);
            scroll_results.addView(TV_msmsdymax);
        } else {
            TextView TV_msmsdx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msmsdx.setTextSize(tam_pequeno);
            TV_msmsdx.setPadding(0, 15, 0, 15);
            scroll_results.addView(TV_msmsdx);

            TextView TV_msmsdy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msmsdy.setTextSize(tam_pequeno);
            TV_msmsdy.setPadding(0, 15, 0, 100);
            scroll_results.addView(TV_msmsdy);
        }
    }
}
