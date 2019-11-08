package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class OutputDFlexaoActivity extends AppCompatActivity {

    public final int tam_grande = 25, tam_pequeno = 18;

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
        setContentView(R.layout.activity_output_dflexao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();

        OutputVFlexaoActivity flex = new OutputVFlexaoActivity();


        if(extras != null)
        {
            double msdx = extras.getDouble("msdx");
            double msdy = extras.getDouble("msdy");
            double fy = extras.getDouble("fy");
            double lb = extras.getDouble("lb");
            double cb = extras.getDouble("cb");
            int analise = extras.getInt("analise");
            String ordem = extras.getString("ordem");
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


            DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
            database.open();
            database.order_by("id");
            double ix_150 = database.get_ix(1);
            System.out.println("ix " + ix_150);
            database.order_by(ordem);
            long count = database.quantTuplas();
            int i;
            boolean flag =false;
            double FLM_lambda_b = 0, FLM_lambda_p = 0,FLM_lambda_r=0,mpx=0,mpy=0,FLM_mrx=0,
                    FLM_mry=0,FLM_mnx=0,FLM_mny=0,FLA_lambda_b=0,FLA_lambda_p=0,FLA_lambda_r=0,
                    FLA_mr=0,FLA_mn=0,FLT_lambda_b=0,FLT_l_p=0,FLT_lambda_p=0,FLT_l_r=0,FLT_lambda_r=0,
                    FLT_mn=0,mrdx=0,mrdy=0,vrdx=0,vrdy=0,flechaadm=0,flechareal=0;
            String FLA="",FLT="",FLM="";
            for( i=1 ; i < count && flag == false ; ++i ) {
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
                vrdx = flex.Vrdx_LaminadoW(database.get_mesa(i), fy, database.get_d(i), database.get_tw(i));
                vrdy = flex.Vrdy(database.get_aba(i), fy, database.get_bf(i), database.get_tf(i));
                flechareal = flecha_real(flecha,ix_150,database.get_ix(i));
                flechaadm = flex.FlechaAdm(vao);

                flag = verificacao_perfil(msdx,msdy,mrdx,mrdy,analise,vsdx,vsdy,vrdx,vrdy,flechareal,flechaadm,FLA_lambda_b,FLA_lambda_r);

            }
            i--;
            Show_Results_LaminadoW(flag,database.get_perfil(i), analise, fy, database.get_ry(i), database.get_zx(i), database.get_iy(i), database.get_j(i)
                        , database.get_cw(i), database.get_wx(i), database.get_mesa(i), database.get_aba(i),
                        msdx, msdy, cb, vsdx, vsdy, flecha, vao,
                        FLM, FLM_lambda_b, FLM_lambda_p, FLM_lambda_r, FLM_mnx, FLM_mny,
                        FLA, FLA_lambda_b, FLA_lambda_p, FLA_lambda_r, FLA_mn,
                        FLT, lb, FLT_lambda_b, FLT_l_p, FLT_lambda_p, FLT_l_r, FLT_lambda_r, FLT_mn, mrdx, mrdy,
                        vrdx, vrdy, flechaadm, flechareal);

            database.close();
        }
    }
    public double flecha_real(double flecha_usuario, double ix_150, double ix_real)
    {
        return flecha_usuario * (ix_150/ix_real);
    }
    public boolean verificacao_perfil(double msdx, double msdy, double mrdx, double mrdy, int analise, double vsdx, double vsdy, double vrdx, double vrdy, double flecha, double flechaadm, double FLA_lambda_b, double FLA_lambda_r)
    {
        OutputVFlexaoActivity flex = new OutputVFlexaoActivity();
        boolean flag = true;
        double momento = flex.Momento(msdx,msdy,mrdx,mrdy);

        if (FLA_lambda_b > FLA_lambda_r)
            flag = false;

        if (momento <= 1.0) //ok
        {
            flag = true && flag;
        }
        else
        {
            flag = false;
        }

        if(analise == 1 || analise == 3)
        {
            double cortante = flex.Cortante(vsdx,vsdy,vrdx,vrdy);

            if (cortante <= 1.0) //ok
            {
                flag = true && flag;
            }
            else
            {
                flag = false;
            }

        }

        if(analise == 2 || analise == 3)
        {

            if (flecha <= flechaadm) //ok
            {
                flag = true && flag;
            }
            else
            {
                flag = false;
            }

        }
        return flag;
    }
    //ARREDONDAMENTO
    public double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }
    //CRIACAO DE LAYOUT
    private void Show_Results_LaminadoW(boolean flag, String perfil, int analise, double fy, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm, double flechareal)
    {
        ScrollView sv = new ScrollView(OutputDFlexaoActivity.this);

        LinearLayout scroll_results = new LinearLayout(OutputDFlexaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rel);
        rl.addView(sv);

        if(!flag)
        {
            TextView ERRO = new TextView(OutputDFlexaoActivity.this);
            ERRO.setText("Nenhum perfil é adequado!");
            scroll_results.addView(ERRO);
            System.out.println("aqui");

            return;
        }
        System.out.println("aqui");
        TextView TV_perfil = new TextView(OutputDFlexaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputDFlexaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputDFlexaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,60);
        scroll_results.addView(TV_fy);

        TextView TV_ry = new TextView(OutputDFlexaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry,2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0,100,0,15);
        scroll_results.addView(TV_ry);

        TextView TV_zx = new TextView(OutputDFlexaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx,2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0,15,0,15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputDFlexaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy,2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0,15,0,15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputDFlexaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j,2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0,15,0,15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputDFlexaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw,2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0,15,0,15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputDFlexaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0,15,0,15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputDFlexaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa,2) ));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0,15,0,15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputDFlexaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba,2) ));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0,15,0,60);
        scroll_results.addView(TV_aba);

        TextView TV_msdx = new TextView(OutputDFlexaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdx);

        TextView TV_msdy = new TextView(OutputDFlexaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdy);

        TextView TV_cb = new TextView(OutputDFlexaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(0,15,0,15);
        scroll_results.addView(TV_cb);

        //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputDFlexaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdx);

            TextView TV_vsdy = new TextView(OutputDFlexaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdy);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputDFlexaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(0,15,0,15);
            scroll_results.addView(TV_flecha);

            TextView TV_vao = new TextView(OutputDFlexaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(0,15,0,15);
            scroll_results.addView(TV_vao);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputDFlexaoActivity.this);
        TV_FLM.setText("FLM - " + FLM);
        TV_FLM.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        TV_FLM.setPadding(0,100,0,0);
        scroll_results.addView(TV_FLM);

        TextView TV_flm_b = new TextView(OutputDFlexaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b,2) ));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_b);

        TextView TV_flm_p = new TextView(OutputDFlexaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p,2) ));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_p);

        TextView TV_flm_r = new TextView(OutputDFlexaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r,2) ));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_r);

        TextView TV_flm_mnx = new TextView(OutputDFlexaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx,2) + " kNm" ));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_mnx);

        TextView TV_flm_mny = new TextView(OutputDFlexaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy,2)  + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(0,15,0,100);
        scroll_results.addView(TV_flm_mny);

        //FLA
        TextView TV_FLA = new TextView(OutputDFlexaoActivity.this);
        TV_FLA.setText("FLA - " + FLA);
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);

        TextView TV_fla_b = new TextView(OutputDFlexaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_b);

        TextView TV_fla_p = new TextView(OutputDFlexaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_p);

        TextView TV_fla_r = new TextView(OutputDFlexaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_r);

        TextView TV_fla_mn = new TextView(OutputDFlexaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(0,15,0,100);
        scroll_results.addView(TV_fla_mn);

        //FLT
        TextView TV_FLT = new TextView(OutputDFlexaoActivity.this);
        TV_FLT.setText("FLT - " + FLT);
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);

        TextView TV_flt_b = new TextView(OutputDFlexaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) +
                " cm | λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_b);

        TextView TV_flt_p = new TextView(OutputDFlexaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) +
                " cm | λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_p);

        TextView TV_flt_r = new TextView(OutputDFlexaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) +
                " cm | λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_r);

        TextView TV_flt_cbmn = new TextView(OutputDFlexaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(0,15,0,100);
        scroll_results.addView(TV_flt_cbmn);

        //ANALISE

        //**momento
        TextView TV_momento = new TextView(OutputDFlexaoActivity.this);
        TV_momento.setText("MOMENTO RESISTENTE DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);

        TextView TV_mrdx = new TextView(OutputDFlexaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx,2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_mrdx);

        TextView TV_mrdy = new TextView(OutputDFlexaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy,2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(0,15,0,100);
        scroll_results.addView(TV_mrdy);

        //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_cortante = new TextView(OutputDFlexaoActivity.this);
            TV_cortante.setText("CORTANTE RESISTENTE DE CÁLCULO");
            TV_cortante.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);

            TextView TV_vrdx = new TextView(OutputDFlexaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vrdx);

            TextView TV_vrdy = new TextView(OutputDFlexaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(0,15,0,100);
            scroll_results.addView(TV_vrdy);
        }

        //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputDFlexaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);

            TextView TV_fadm = new TextView(OutputDFlexaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(0,15,0,100);
            scroll_results.addView(TV_fadm);

            TextView TV_flechareal = new TextView(OutputDFlexaoActivity.this);
            TV_flechareal.setText("FLECHA ADMISSÍVEL");
            TV_flechareal.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            TV_flechareal.setTextSize(tam_grande);
            scroll_results.addView(TV_flechareal);

            TextView TV_freal = new TextView(OutputDFlexaoActivity.this);
            TV_freal.setText(Html.fromHtml("δ<small><sub>real</sub></small> = " + casasDecimais(flechareal,2) + " mm"));
            TV_freal.setTextSize(tam_pequeno);
            TV_freal.setPadding(0,15,0,100);
            scroll_results.addView(TV_freal);
        }

    }

}
