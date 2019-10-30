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

public class OutputVFlexocompressaoActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_output_vflexocompressao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();



        if(extras != null)
        {
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

            OutputVCompressaoActivity comp = new OutputVCompressaoActivity();
            OutputVFlexaoActivity flex = new OutputVFlexaoActivity();


            if(secao == 1)
            {
                DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                database.open();
                int perfil = extras.getInt("perfil");
                double FLM_lambda_b = flex.FLM_lambda_b(database.get_aba(perfil));
                double FLM_lambda_p = flex.FLM_lambda_p(fy);
                double FLM_lambda_r = flex.FLM_lambda_r_LaminadoW(fy);
                double mpx = flex.Mpx(database.get_zx(perfil),fy);
                double mpy = flex.Mpy(database.get_zy(perfil),fy);
                double FLM_mrx = flex.FLM_Mrx(database.get_wx(perfil),fy);
                double FLM_mry = flex.FLM_Mry(database.get_wy(perfil),fy);
                double FLM_mnx = flex.Mn_FLM_x_LaminadoW(mpx,FLM_mrx,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.get_wx(perfil));
                double FLM_mny = flex.Mn_FLM_y_LaminadoW(mpy,FLM_mry,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.get_wy(perfil));
                String FLM = flex.FLM(FLM_lambda_b,FLM_lambda_p,FLM_lambda_r);
                double FLA_lambda_b = flex.FLM_lambda_b(database.get_mesa(perfil));
                double FLA_lambda_p = flex.FLA_lambda_p(fy);
                double FLA_lambda_r = flex.FLA_lambda_r(fy);
                double FLA_mr = flex.FLA_Mrx(database.get_wx(perfil),fy);
                double FLA_mn = flex.FLA_Mn(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,mpx,FLA_mr);
                String FLA = flex.FLA(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r);
                double FLT_lambda_b = flex.FLT_lambda_b(lb,database.get_ry(perfil));
                double FLT_l_p = flex.FLT_l_p(database.get_ry(perfil),fy);
                double FLT_lambda_p = flex.FLT_lambda_p(FLT_l_p,database.get_ry(perfil));
                double FLT_l_r = flex.FLT_l_r(database.get_iy(perfil),database.get_j(perfil),database.get_ry(perfil),database.get_cw(perfil),fy,database.get_wx(perfil));
                double FLT_lambda_r = flex.FLT_lambda_r(FLT_l_r,database.get_ry(perfil));
                double FLT_mn = flex.FLT_Mn(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r,mpx,FLM_mrx,cb,database.get_iy(perfil),database.get_cw(perfil),database.get_j(perfil),lb);
                String FLT = flex.FLT(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r);
                double mrdx = flex.Mrdx(mpx,FLM_mnx,FLA_mn,FLT_mn);
                double mrdy = flex.Mrdy(mpy,FLM_mny);


                if(FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b,fy,database.get_bf(perfil),database.get_tf(perfil),FLA_lambda_r);
                else
                     Show_Results_LaminadoW(database.get_perfil(perfil),analise,fy,database.get_ry(perfil),database.get_zx(perfil),database.get_iy(perfil),database.get_j(perfil)
                ,database.get_cw(perfil),database.get_wx(perfil),database.get_mesa(perfil),database.get_aba(perfil),
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
                double FLM_lambda_b = flex.FLM_lambda_b(database.getAba());
                double FLM_lambda_p = flex.FLM_lambda_p(fy);
                double kc = flex.Kc(database.getH(),tw);
                double FLM_lambda_r = flex.FLM_lambda_r_SoldadoCustom(fy,kc);
                double mpx = flex.Mpx(database.getZx(),fy);
                double mpy = flex.Mpy(database.getZy(),fy);
                double FLM_mrx = flex.FLM_Mrx(database.getWx(),fy);
                double FLM_mry = flex.FLM_Mry(database.getWy(),fy);
                double FLM_mnx = flex.Mn_FLM_x_SoldadoCustom(mpx,FLM_mrx,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.getWx(),kc);
                double FLM_mny = flex.Mn_FLM_y_SoldadoCustom(mpy,FLM_mry,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,database.getWy(),kc);
                String FLM = flex.FLM(FLM_lambda_b,FLM_lambda_p,FLM_lambda_r);
                double FLA_lambda_b = flex.FLM_lambda_b(database.getMesa());
                double FLA_lambda_p = flex.FLA_lambda_p(fy);
                double FLA_lambda_r = flex.FLA_lambda_r(fy);
                double FLA_mr = flex.FLA_Mrx(database.getWx(),fy);
                double FLA_mn = flex.FLA_Mn(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,mpx,FLA_mr);
                String FLA = flex.FLA(FLA_lambda_b,FLA_lambda_p,FLA_lambda_r);
                double FLT_lambda_b = flex.FLT_lambda_b(lb,database.getRy());
                double FLT_l_p = flex.FLT_l_p(database.getRy(),fy);
                double FLT_lambda_p = flex.FLT_lambda_p(FLT_l_p,database.getRy());
                double FLT_l_r = flex.FLT_l_r(database.getIy(),database.getJ(),database.getRy(),database.getCw(),fy,database.getWx());
                double FLT_lambda_r = flex.FLT_lambda_r(FLT_l_r,database.getRy());
                double FLT_mn = flex.FLT_Mn(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r,mpx,FLM_mrx,cb,database.getIy(),database.getCw(),database.getJ(),lb);
                String FLT = flex.FLT(FLT_lambda_b,FLT_lambda_p,FLT_lambda_r);
                double mrdx = flex.Mrdx(mpx,FLM_mnx,FLA_mn,FLT_mn);
                double mrdy = flex.Mrdy(mpy,FLM_mny);
                System.out.println("kc = " + kc);
                if(FLA_lambda_b > FLA_lambda_r)
                    Show_lambdab_erro(FLA_lambda_b,fy,bf,tf,FLA_lambda_r);
                else if(kc <0.35 || kc >0.76)
                    Show_Kc_erro(kc,database.getH(),tw);
                else
                    Show_Results_SoldadoCustom("CUSTOMIZADO",analise,fy,d,tw,bf,tf,database.getRy(),database.getZx(),database.getIy(),database.getJ()
                        ,database.getCw(),database.getWx(),database.getMesa(),database.getAba(),
                        msdx,msdy,cb,vsdx,vsdy,flecha,vao,
                        FLM,FLM_lambda_b,FLM_lambda_p,FLM_lambda_r,FLM_mnx,FLM_mny,
                        FLA,FLA_lambda_b,FLA_lambda_p,FLA_lambda_r,FLA_mn,
                        FLT,lb,FLT_lambda_b,FLT_l_p,FLT_lambda_p,FLT_l_r,FLT_lambda_r,FLT_mn,mrdx,mrdy,
                        vrdx,vrdy,flechaadm);

            }
        }

    }


    //ARREDONDAMENTO
    private double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }
    //CRIACAO DE LAYOUT
    private void Show_Results_LaminadoW(String perfil, int analise, double fy, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputVFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,60);
        scroll_results.addView(TV_fy);

        TextView TV_ry = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry,2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0,100,0,15);
        scroll_results.addView(TV_ry);

        TextView TV_zx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx,2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0,15,0,15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy,2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0,15,0,15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputVFlexocompressaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j,2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0,15,0,15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw,2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0,15,0,15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0,15,0,15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa,2) ));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0,15,0,15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputVFlexocompressaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba,2) ));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0,15,0,60);
        scroll_results.addView(TV_aba);

        TextView TV_msdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdx);

        TextView TV_msdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdy);

        TextView TV_cb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(0,15,0,15);
        scroll_results.addView(TV_cb);

            //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdx);

            TextView TV_vsdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdy);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputVFlexocompressaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(0,15,0,15);
            scroll_results.addView(TV_flecha);

            TextView TV_vao = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(0,15,0,15);
            scroll_results.addView(TV_vao);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLM.setText("FLM - " + FLM);
        TV_FLM.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        TV_FLM.setPadding(0,100,0,0);
        scroll_results.addView(TV_FLM);

        TextView TV_flm_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b,2) ));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_b);

        TextView TV_flm_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p,2) ));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_p);

        TextView TV_flm_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r,2) ));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_r);

        TextView TV_flm_mnx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx,2) + " kNm" ));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_mnx);

        TextView TV_flm_mny = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy,2)  + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(0,15,0,100);
        scroll_results.addView(TV_flm_mny);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLA.setText("FLA - " + FLA);
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);

        TextView TV_fla_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_b);

        TextView TV_fla_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_p);

        TextView TV_fla_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_r);

        TextView TV_fla_mn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(0,15,0,100);
        scroll_results.addView(TV_fla_mn);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLT.setText("FLT - " + FLT);
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);

        TextView TV_flt_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) +
                " cm | λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_b);

        TextView TV_flt_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) +
                " cm | λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_p);

        TextView TV_flt_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) +
                " cm | λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_r);

        TextView TV_flt_cbmn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(0,15,0,100);
        scroll_results.addView(TV_flt_cbmn);

        //ANALISE

            //**momento
        TextView TV_momento = new TextView(OutputVFlexocompressaoActivity.this);
        TV_momento.setText("MOMENTO RESISTENTE DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);

        TextView TV_mrdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx,2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_mrdx);

        TextView TV_mrdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy,2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(0,15,0,100);
        scroll_results.addView(TV_mrdy);

            //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_cortante = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cortante.setText("CORTANTE RESISTENTE DE CÁLCULO");
            TV_cortante.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);

            TextView TV_vrdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vrdx);

            TextView TV_vrdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(0,15,0,100);
            scroll_results.addView(TV_vrdy);
        }

            //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputVFlexocompressaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);

            TextView TV_fadm = new TextView(OutputVFlexocompressaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(0,15,0,100);
            scroll_results.addView(TV_fadm);
        }


        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexocompressaoActivity.this);
        TV_verifica.setText("VERIFICAÇÃO");
        TV_verifica.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);

        double momento = Momento(msdx,msdy,mrdx,mrdy);

        TextView TV_mom = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mom.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small> = " + casasDecimais(momento,3) ));
        TV_mom.setTextSize(tam_pequeno);
        TV_mom.setPadding(0,15,0,15);
        scroll_results.addView(TV_mom);

        if (momento <= 1.0) //ok
        {
            TextView TV_mom_ok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_mom_ok.setText(Html.fromHtml("Perfil atende ao momento fletor! "));
            TV_mom_ok.setTextSize(tam_pequeno);
            TV_mom_ok.setPadding(0,15,0,70);
            TV_mom_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_mom_ok);
        }
        else
        {
            TextView TV_mom_nok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_mom_nok.setText(Html.fromHtml("Perfil NÃO atende ao momento fletor!"));
            TV_mom_nok.setTextSize(tam_pequeno);
            TV_mom_nok.setPadding(0,15,0,70);
            TV_mom_nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_mom_nok);
        }

        if(analise == 1 || analise == 3)
        {
            double cortante = Cortante(vsdx,vsdy,vrdx,vrdy);

            TextView TV_cort = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cort.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> / V<small><sub>Rd,x</sub></small>  +  V<small><sub>Sd,y</sub></small> / V<small><sub>Rd,y</sub></small> = " + casasDecimais(cortante,3) ));
            TV_cort.setTextSize(tam_pequeno);
            TV_cort.setPadding(0,15,0,15);
            scroll_results.addView(TV_cort);

            if (cortante <= 1.0) //ok
            {
                TextView TV_cort_ok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_cort_ok.setText(Html.fromHtml("Perfil atende à força cortante! "));
                TV_cort_ok.setTextSize(tam_pequeno);
                TV_cort_ok.setPadding(0,15,0,70);
                TV_cort_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_cort_ok);
            }
            else
            {
                TextView TV_cort_nok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_cort_nok.setText(Html.fromHtml("Perfil NÃO atende à força cortante!"));
                TV_cort_nok.setTextSize(tam_pequeno);
                TV_cort_nok.setPadding(0,15,0,70);
                TV_cort_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_cort_nok);
            }

        }

        if(analise == 2 || analise == 3)
        {

            if (flecha <= flechaadm) //ok
            {
                TextView TV_fle_ok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_fle_ok.setText(Html.fromHtml("Flecha máxima menor que flecha admissível: OK! "));
                TV_fle_ok.setTextSize(tam_pequeno);
                TV_fle_ok.setPadding(0,15,0,70);
                TV_fle_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_fle_ok);
            }
            else
            {
                TextView TV_fle_nok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_fle_nok.setText(Html.fromHtml("NÃO OK! A flecha máxima deve ser menor que a admissível."));
                TV_fle_nok.setTextSize(tam_pequeno);
                TV_fle_nok.setPadding(0,15,0,70);
                TV_fle_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_fle_nok);
            }

        }


    }
    private void Show_Results_SoldadoCustom(String perfil, int analise, double fy, double d, double tw, double bf, double tf, double ry, double zx, double iy, double j, double cw, double wx
            , double mesa, double aba, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idflexao);

        TextView TV_perfil = new TextView(OutputVFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputVFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,60);
        scroll_results.addView(TV_fy);

        //custom
        TextView TV_d = new TextView(OutputVFlexocompressaoActivity.this);
        TV_d.setText(Html.fromHtml("d = " + casasDecimais(d,2) + " mm"));
        TV_d.setTextSize(tam_pequeno);
        TV_d.setPadding(0,100,0,15);
        scroll_results.addView(TV_d);

        TextView TV_tw = new TextView(OutputVFlexocompressaoActivity.this);
        TV_tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + casasDecimais(tw,2) + " mm"));
        TV_tw.setTextSize(tam_pequeno);
        TV_tw.setPadding(0,15,0,15);
        scroll_results.addView(TV_tw);

        TextView TV_bf = new TextView(OutputVFlexocompressaoActivity.this);
        TV_bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + casasDecimais(bf,2) + " mm"));
        TV_bf.setTextSize(tam_pequeno);
        TV_bf.setPadding(0,15,0,15);
        scroll_results.addView(TV_bf);

        TextView TV_tf = new TextView(OutputVFlexocompressaoActivity.this);
        TV_tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + casasDecimais(tf,2) + " mm"));
        TV_tf.setTextSize(tam_pequeno);
        TV_tf.setPadding(0,15,0,15);
        scroll_results.addView(TV_tf);

        TextView TV_ry = new TextView(OutputVFlexocompressaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry,2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0,100,0,15);
        scroll_results.addView(TV_ry);

        TextView TV_zx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx,2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0,15,0,15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy,2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0,15,0,15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputVFlexocompressaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j,2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0,15,0,15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw,2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0,15,0,15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0,15,0,15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa,2) ));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0,15,0,15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputVFlexocompressaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba,2) ));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0,15,0,60);
        scroll_results.addView(TV_aba);

        TextView TV_msdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdx);

        TextView TV_msdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(0,15,0,15);
        scroll_results.addView(TV_msdy);

        TextView TV_cb = new TextView(OutputVFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(0,15,0,15);
        scroll_results.addView(TV_cb);

        //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdx);

            TextView TV_vsdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(0,15,0,15);
            scroll_results.addView(TV_vsdy);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputVFlexocompressaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(0,15,0,15);
            scroll_results.addView(TV_flecha);

            TextView TV_vao = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(0,15,0,15);
            scroll_results.addView(TV_vao);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLM.setText("FLM - " + FLM);
        TV_FLM.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        TV_FLM.setPadding(0,100,0,0);
        scroll_results.addView(TV_FLM);

        TextView TV_flm_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b,2) ));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_b);

        TextView TV_flm_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p,2) ));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_p);

        TextView TV_flm_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r,2) ));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_r);

        TextView TV_flm_mnx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx,2) + " kNm" ));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(0,15,0,15);
        scroll_results.addView(TV_flm_mnx);

        TextView TV_flm_mny = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy,2)  + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(0,15,0,100);
        scroll_results.addView(TV_flm_mny);

        //FLA
        TextView TV_FLA = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLA.setText("FLA - " + FLA);
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);

        TextView TV_fla_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_b);

        TextView TV_fla_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_p);

        TextView TV_fla_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_fla_r);

        TextView TV_fla_mn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(0,15,0,100);
        scroll_results.addView(TV_fla_mn);

        //FLT
        TextView TV_FLT = new TextView(OutputVFlexocompressaoActivity.this);
        TV_FLT.setText("FLT - " + FLT);
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);

        TextView TV_flt_b = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) +
                " cm | λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_b);

        TextView TV_flt_p = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) +
                " cm | λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_p);

        TextView TV_flt_r = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) +
                " cm | λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(0,15,0,15);
        scroll_results.addView(TV_flt_r);

        TextView TV_flt_cbmn = new TextView(OutputVFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(0,15,0,100);
        scroll_results.addView(TV_flt_cbmn);

        //ANALISE

        //**momento
        TextView TV_momento = new TextView(OutputVFlexocompressaoActivity.this);
        TV_momento.setText("MOMENTO RESISTENTE DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);

        TextView TV_mrdx = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx,2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(0,15,0,15);
        scroll_results.addView(TV_mrdx);

        TextView TV_mrdy = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy,2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(0,15,0,100);
        scroll_results.addView(TV_mrdy);

        //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_cortante = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cortante.setText("CORTANTE RESISTENTE DE CÁLCULO");
            TV_cortante.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);

            TextView TV_vrdx = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(0,15,0,15);
            scroll_results.addView(TV_vrdx);

            TextView TV_vrdy = new TextView(OutputVFlexocompressaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(0,15,0,100);
            scroll_results.addView(TV_vrdy);
        }

        //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputVFlexocompressaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);

            TextView TV_fadm = new TextView(OutputVFlexocompressaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(0,15,0,100);
            scroll_results.addView(TV_fadm);
        }


        ///VERIFICAÇOES

        TextView TV_verifica = new TextView(OutputVFlexocompressaoActivity.this);
        TV_verifica.setText("VERIFICAÇÃO");
        TV_verifica.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verifica.setTextSize(tam_grande);
        scroll_results.addView(TV_verifica);

        double momento = Momento(msdx,msdy,mrdx,mrdy);

        TextView TV_mom = new TextView(OutputVFlexocompressaoActivity.this);
        TV_mom.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> / M<small><sub>Rd,x</sub></small>  +  M<small><sub>Sd,y</sub></small> / M<small><sub>Rd,y</sub></small> = " + casasDecimais(momento,3) ));
        TV_mom.setTextSize(tam_pequeno);
        TV_mom.setPadding(0,15,0,15);
        scroll_results.addView(TV_mom);

        if (momento <= 1.0) //ok
        {
            TextView TV_mom_ok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_mom_ok.setText(Html.fromHtml("Perfil atende ao momento fletor! "));
            TV_mom_ok.setTextSize(tam_pequeno);
            TV_mom_ok.setPadding(0,15,0,70);
            TV_mom_ok.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_mom_ok);
        }
        else
        {
            TextView TV_mom_nok = new TextView(OutputVFlexocompressaoActivity.this);
            TV_mom_nok.setText(Html.fromHtml("Perfil NÃO atende ao momento fletor!"));
            TV_mom_nok.setTextSize(tam_pequeno);
            TV_mom_nok.setPadding(0,15,0,70);
            TV_mom_nok.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_mom_nok);
        }

        if(analise == 1 || analise == 3)
        {
            double cortante = Cortante(vsdx,vsdy,vrdx,vrdy);

            TextView TV_cort = new TextView(OutputVFlexocompressaoActivity.this);
            TV_cort.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> / V<small><sub>Rd,x</sub></small>  +  V<small><sub>Sd,y</sub></small> / V<small><sub>Rd,y</sub></small> = " + casasDecimais(cortante,3) ));
            TV_cort.setTextSize(tam_pequeno);
            TV_cort.setPadding(0,15,0,15);
            scroll_results.addView(TV_cort);

            if (cortante <= 1.0) //ok
            {
                TextView TV_cort_ok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_cort_ok.setText(Html.fromHtml("Perfil atende à força cortante! "));
                TV_cort_ok.setTextSize(tam_pequeno);
                TV_cort_ok.setPadding(0,15,0,70);
                TV_cort_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_cort_ok);
            }
            else
            {
                TextView TV_cort_nok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_cort_nok.setText(Html.fromHtml("Perfil NÃO atende à força cortante!"));
                TV_cort_nok.setTextSize(tam_pequeno);
                TV_cort_nok.setPadding(0,15,0,70);
                TV_cort_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_cort_nok);
            }

        }

        if(analise == 2 || analise == 3)
        {

            if (flecha <= flechaadm) //ok
            {
                TextView TV_fle_ok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_fle_ok.setText(Html.fromHtml("Flecha máxima menor que flecha admissível: OK! "));
                TV_fle_ok.setTextSize(tam_pequeno);
                TV_fle_ok.setPadding(0,15,0,70);
                TV_fle_ok.setTextColor(getResources().getColor(R.color.color_ok));
                scroll_results.addView(TV_fle_ok);
            }
            else
            {
                TextView TV_fle_nok = new TextView(OutputVFlexocompressaoActivity.this);
                TV_fle_nok.setText(Html.fromHtml("NÃO OK! A flecha máxima deve ser menor que a admissível."));
                TV_fle_nok.setTextSize(tam_pequeno);
                TV_fle_nok.setPadding(0,15,0,70);
                TV_fle_nok.setTextColor(getResources().getColor(R.color.color_Nok));
                scroll_results.addView(TV_fle_nok);
            }

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
