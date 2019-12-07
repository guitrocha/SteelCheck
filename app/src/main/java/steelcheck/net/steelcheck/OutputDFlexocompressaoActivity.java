package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class OutputDFlexocompressaoActivity extends AppCompatActivity {


    final int tam_grande = 20, tam_pequeno = 16, tam_dimens = 13;

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
            else
                show_ordem = generate_string_ordem(ordem,database,-1);
            Show_Results_LaminadoW(flex,database,i,flag,database.get_perfil(i),show_ordem, isAmp, fy,
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
        if(i == -1)
        {
            switch(ordem) {
                case "massa":
                    show = "Massa Linear";
                    break;
                case "d":
                    show = "d";
                    break;
                case "bf":
                    show = "b<small><sub>f</sub></small>";
                    break;
                case "tf":
                    show = "t<small><sub>f</sub></small>";
                    break;
                case "tw":
                    show = "t<small><sub>w</sub></small>";
                    break;
                case "area":
                    show = "A<small><sub>g</sub></small>";
                    break;
                case "ix":
                    show = "I<small><sub>x</sub></small>";
                    break;
                case "iy":
                    show = "I<small><sub>y</sub></small>";
                    break;
                case "wx":
                    show = "W<small><sub>x</sub></small>";
                    break;
                case "wy":
                    show = "W<small><sub>y</sub></small>";
                    break;
                case "zx":
                    show = "Z<small><sub>x</sub></small>";
                    break;
                case "zy":
                    show = "Z<small><sub>y</sub></small>";
                    break;
            }
        }
        else
        {
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
    private void Show_Results_LaminadoW(OutputVFlexaoActivity flex, DatabaseAccess db, int pos, boolean flag, String perfil, String ordem, boolean isAmp, double fy,
                                        double msdx, double msdy, double cb, double cmx, double cmy
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double rx, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                        double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double b1x, double b1y, double msdxmax, double msdymax)
    {
        ScrollView sv = new ScrollView(OutputDFlexocompressaoActivity.this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));

        LinearLayout scroll_results = new LinearLayout(OutputDFlexocompressaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relflexcomp);
        rl.addView(sv);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        if(!flag)
        {
            //1 - PERFIL
            TextView TV_perfil = new TextView(OutputDFlexocompressaoActivity.this);
            TV_perfil.setText("PERFIL ADEQUADO");
            TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil);
            TV_perfil.setGravity(Gravity.CENTER);
            TV_perfil.setTextColor(Color.WHITE);
            TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_perfil.setPadding(50,20,50,20);

            TextView TV_perfil_dimen = new TextView(OutputDFlexocompressaoActivity.this);
            TV_perfil_dimen.setText("NÃO ENCONTRADO");
            TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil_dimen.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil_dimen);
            TV_perfil_dimen.setGravity(Gravity.CENTER);
            TV_perfil_dimen.setTextColor(Color.WHITE);
            TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_Nok));
            TV_perfil_dimen.setPadding(50,20,50,20);

            TextView TV_param = new TextView(OutputDFlexocompressaoActivity.this);
            TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
            TV_param.setTextSize(tam_dimens);
            TV_param.setPadding(50,15,0,50);
            scroll_results.addView(TV_param);

            TextView TV_nenhum = new TextView(OutputDFlexocompressaoActivity.this);
            TV_nenhum.setText(Html.fromHtml("Nenhum perfil atende às condições impostas."));
            TV_nenhum.setTextSize(tam_pequeno);
            TV_nenhum.setPadding(50,15,50,15);
            scroll_results.addView(TV_nenhum);
            TV_nenhum.setTextColor(Color.BLACK);

            //2 - Parametros
            TextView TV_parametros = new TextView(OutputDFlexocompressaoActivity.this);
            TV_parametros.setText("PARÂMETROS DO MATERIAL");
            TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_parametros.setTextSize(tam_grande);
            scroll_results.addView(TV_parametros);
            TV_parametros.setGravity(Gravity.CENTER);
            TV_parametros.setTextColor(Color.WHITE);
            TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_parametros.setPadding(50,20,50,20);

            TextView TV_elasticidade = new TextView(OutputDFlexocompressaoActivity.this);
            TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
            TV_elasticidade.setTextSize(tam_pequeno);
            TV_elasticidade.setPadding(50,15,0,15);
            scroll_results.addView(TV_elasticidade);
            TV_elasticidade.setTextColor(Color.BLACK);

            TextView TV_fy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
            TV_fy.setTextSize(tam_pequeno);
            TV_fy.setPadding(50,15,0,50);
            scroll_results.addView(TV_fy);
            TV_fy.setTextColor(Color.BLACK);

            //3 - Solicitacoes e contorno
            TextView TV_solic = new TextView(OutputDFlexocompressaoActivity.this);
            TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
            TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_solic.setTextSize(tam_grande);
            scroll_results.addView(TV_solic);
            TV_solic.setGravity(Gravity.CENTER);
            TV_solic.setTextColor(Color.WHITE);
            TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_solic.setPadding(50,20,50,20);

            LinearLayout contorno = new LinearLayout(OutputDFlexocompressaoActivity.this);
            contorno.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(contorno);

            LinearLayout contorno_1 = new LinearLayout(OutputDFlexocompressaoActivity.this);
            contorno_1.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_1);

            LinearLayout contorno_2 = new LinearLayout(OutputDFlexocompressaoActivity.this);
            contorno_2.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_2);

            //comp
            TextView TV_comp = new TextView(OutputDFlexocompressaoActivity.this);
            TV_comp.setText("COMPRESSÃO");
            TV_comp.setTextSize(tam_pequeno);
            TV_comp.setPadding(50,15,0,50);
            contorno_1.addView(TV_comp);
            TV_comp.setTextColor(Color.BLACK);

            TextView TV_ncsd = new TextView(OutputDFlexocompressaoActivity.this);
            TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd, 2) + " kN"));
            TV_ncsd.setTextSize(tam_pequeno);
            TV_ncsd.setPadding(50,15,0,15);
            contorno_1.addView(TV_ncsd);
            TV_ncsd.setTextColor(Color.BLACK);

            TextView TV_kx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx, 2)));
            TV_kx.setTextSize(tam_pequeno);
            TV_kx.setPadding(50,15,0,15);
            contorno_1.addView(TV_kx);
            TV_kx.setTextColor(Color.BLACK);

            TextView TV_ky = new TextView(OutputDFlexocompressaoActivity.this);
            TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky, 2)));
            TV_ky.setTextSize(tam_pequeno);
            TV_ky.setPadding(50,15,0,15);
            contorno_1.addView(TV_ky);
            TV_ky.setTextColor(Color.BLACK);

            TextView TV_kz = new TextView(OutputDFlexocompressaoActivity.this);
            TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz, 2)));
            TV_kz.setTextSize(tam_pequeno);
            TV_kz.setPadding(50,15,0,15);
            contorno_1.addView(TV_kz);
            TV_kz.setTextColor(Color.BLACK);

            TextView TV_lx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx, 2) + " cm"));
            TV_lx.setTextSize(tam_pequeno);
            TV_lx.setPadding(50,15,0,15);
            contorno_1.addView(TV_lx);
            TV_lx.setTextColor(Color.BLACK);

            TextView TV_ly = new TextView(OutputDFlexocompressaoActivity.this);
            TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly, 2) + " cm"));
            TV_ly.setTextSize(tam_pequeno);
            TV_ly.setPadding(50,15,0,15);
            contorno_1.addView(TV_ly);
            TV_ly.setTextColor(Color.BLACK);

            TextView TV_lz = new TextView(OutputDFlexocompressaoActivity.this);
            TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz, 2) + " cm"));
            TV_lz.setTextSize(tam_pequeno);
            TV_lz.setPadding(50,15,0,50);
            contorno_1.addView(TV_lz);
            TV_lz.setTextColor(Color.BLACK);

            //flex
            TextView TV_FLEX = new TextView(OutputDFlexocompressaoActivity.this);
            TV_FLEX.setText("FLEXÃO");
            TV_FLEX.setTextSize(tam_pequeno);
            TV_FLEX.setPadding(100,15,0,50);
            contorno_2.addView(TV_FLEX);
            TV_FLEX.setTextColor(Color.BLACK);

            TextView TV_msdx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msdx.setTextSize(tam_pequeno);
            TV_msdx.setPadding(100,15,0,15);
            contorno_2.addView(TV_msdx);
            TV_msdx.setTextColor(Color.BLACK);

            TextView TV_msdy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msdy.setTextSize(tam_pequeno);
            TV_msdy.setPadding(100,15,0,15);
            contorno_2.addView(TV_msdy);
            TV_msdy.setTextColor(Color.BLACK);

            TextView TV_cb = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb, 3)));
            TV_cb.setTextSize(tam_pequeno);
            TV_cb.setPadding(100,15,0,15);
            contorno_2.addView(TV_cb);
            TV_cb.setTextColor(Color.BLACK);

            // cmx cmy
            if (isAmp) {
                TextView TV_cmx = new TextView(OutputDFlexocompressaoActivity.this);
                TV_cmx.setText(Html.fromHtml("C<small><sub>m,x</sub></small> = " + casasDecimais(cmx, 2)));
                TV_cmx.setTextSize(tam_pequeno);
                TV_cmx.setPadding(100,15,0,15);
                contorno_2.addView(TV_cmx);
                TV_cmx.setTextColor(Color.BLACK);

                TextView TV_cmy = new TextView(OutputDFlexocompressaoActivity.this);
                TV_cmy.setText(Html.fromHtml("C<small><sub>m,y</sub></small> = " + casasDecimais(cmy, 2)));
                TV_cmy.setTextSize(tam_pequeno);
                TV_cmy.setPadding(100,15,0,50);
                contorno_2.addView(TV_cmy);
                TV_cmy.setTextColor(Color.BLACK);
            }
            return;
        }

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDFlexocompressaoActivity.this);
        TV_perfil.setText("PERFIL ADEQUADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputDFlexocompressaoActivity.this);
        TV_perfil_dimen.setText(perfil);
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_ok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_param = new TextView(OutputDFlexocompressaoActivity.this);
        TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
        TV_param.setTextSize(tam_dimens);
        TV_param.setPadding(50,15,0,50);
        scroll_results.addView(TV_param);
        flex.Show_Dimensoes_Database_Perfil(db,scroll_results,OutputDFlexocompressaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputDFlexocompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputDFlexocompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputDFlexocompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputDFlexocompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputDFlexocompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputDFlexocompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        //comp
        TextView TV_comp = new TextView(OutputDFlexocompressaoActivity.this);
        TV_comp.setText("COMPRESSÃO");
        TV_comp.setTextSize(tam_pequeno);
        TV_comp.setPadding(50,15,0,50);
        contorno_1.addView(TV_comp);
        TV_comp.setTextColor(Color.BLACK);

        TextView TV_ncsd = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd, 2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx, 2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky, 2)));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputDFlexocompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz, 2)));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,15);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx, 2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_1.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly, 2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_1.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputDFlexocompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz, 2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,50);
        contorno_1.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        //flex
        TextView TV_FLEX = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLEX.setText("FLEXÃO");
        TV_FLEX.setTextSize(tam_pequeno);
        TV_FLEX.setPadding(100,15,0,50);
        contorno_2.addView(TV_FLEX);
        TV_FLEX.setTextColor(Color.BLACK);

        TextView TV_msdx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(100,15,0,15);
        contorno_2.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb, 3)));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(100,15,0,15);
        contorno_2.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

        // cmx cmy
        if (isAmp) {
            TextView TV_cmx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cmx.setText(Html.fromHtml("C<small><sub>m,x</sub></small> = " + casasDecimais(cmx, 2)));
            TV_cmx.setTextSize(tam_pequeno);
            TV_cmx.setPadding(100,15,0,15);
            contorno_2.addView(TV_cmx);
            TV_cmx.setTextColor(Color.BLACK);

            TextView TV_cmy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cmy.setText(Html.fromHtml("C<small><sub>m,y</sub></small> = " + casasDecimais(cmy, 2)));
            TV_cmy.setTextSize(tam_pequeno);
            TV_cmy.setPadding(100,15,0,50);
            contorno_2.addView(TV_cmy);
            TV_cmy.setTextColor(Color.BLACK);
        }

        //**solic
        if (isAmp) {
            TextView TV_cortante = new TextView(OutputDFlexocompressaoActivity.this);
            TV_cortante.setText("AMPLIFICAÇÃO DE MOMENTOS FLETORES");
            TV_cortante.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            TV_cortante.setTextSize(tam_grande);
            scroll_results.addView(TV_cortante);
            TV_cortante.setGravity(Gravity.CENTER);
            TV_cortante.setTextColor(Color.WHITE);
            TV_cortante.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_cortante.setPadding(50,20,50,20);

            LinearLayout tab_solic = new LinearLayout(OutputDFlexocompressaoActivity.this);
            tab_solic.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(tab_solic);

            LinearLayout col_1_solic = new LinearLayout(OutputDFlexocompressaoActivity.this);
            col_1_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_1_solic);

            LinearLayout col_2_solic = new LinearLayout(OutputDFlexocompressaoActivity.this);
            col_2_solic.setOrientation(LinearLayout.VERTICAL);
            tab_solic.addView(col_2_solic);

            TextView TV_msnex = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msnex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex, 2) + " kN"));
            TV_msnex.setTextSize(tam_pequeno);
            TV_msnex.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msnex);
            TV_msnex.setTextColor(Color.BLACK);

            TextView TV_msney = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney, 2) + " kN"));
            TV_msney.setTextSize(tam_pequeno);
            TV_msney.setPadding(50,15,0,15);
            col_1_solic.addView(TV_msney);
            TV_msney.setTextColor(Color.BLACK);

            TextView TV_b1x = new TextView(OutputDFlexocompressaoActivity.this);
            TV_b1x.setText(Html.fromHtml("β<small><sub>1,x</sub></small> = " + casasDecimais(b1x, 3)));
            TV_b1x.setTextSize(tam_pequeno);
            TV_b1x.setPadding(50,15,0,15);
            col_1_solic.addView(TV_b1x);
            TV_b1x.setTextColor(Color.BLACK);

            TextView TV_b1y = new TextView(OutputDFlexocompressaoActivity.this);
            TV_b1y.setText(Html.fromHtml("β<small><sub>1,y</sub></small> = " + casasDecimais(b1y, 3)));
            TV_b1y.setTextSize(tam_pequeno);
            TV_b1y.setPadding(50,15,0,50);
            col_1_solic.addView(TV_b1y);
            TV_b1y.setTextColor(Color.BLACK);

            TextView TV_msmsdx = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx, 2) + " kNm"));
            TV_msmsdx.setTextSize(tam_pequeno);
            TV_msmsdx.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdx);
            TV_msmsdx.setTextColor(Color.BLACK);

            TextView TV_msmsdy = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy, 2) + " kNm"));
            TV_msmsdy.setTextSize(tam_pequeno);
            TV_msmsdy.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdy);
            TV_msmsdy.setTextColor(Color.BLACK);

            TextView TV_msmsdxmax = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdxmax.setText(Html.fromHtml("M<small><sub>Sd,max,x</sub></small> = " + casasDecimais(msdxmax, 2) + " kNm"));
            TV_msmsdxmax.setTextSize(tam_pequeno);
            TV_msmsdxmax.setPadding(50,15,0,15);
            col_2_solic.addView(TV_msmsdxmax);
            TV_msmsdxmax.setTextColor(Color.BLACK);

            TextView TV_msmsdymax = new TextView(OutputDFlexocompressaoActivity.this);
            TV_msmsdymax.setText(Html.fromHtml("M<small><sub>Sd,max,y</sub></small> = " + casasDecimais(msdymax, 2) + " kNm"));
            TV_msmsdymax.setTextSize(tam_pequeno);
            TV_msmsdymax.setPadding(50,15,0,50);
            col_2_solic.addView(TV_msmsdymax);
            TV_msmsdymax.setTextColor(Color.BLACK);
        }



        //FLM
        TextView TV_FLM = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputDFlexocompressaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputDFlexocompressaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputDFlexocompressaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputDFlexocompressaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputDFlexocompressaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputDFlexocompressaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputDFlexocompressaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputDFlexocompressaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //flambagem
        TextView TV_flamblocal = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        LinearLayout tab_loc = new LinearLayout(OutputDFlexocompressaoActivity.this);
        tab_loc.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_loc);

        LinearLayout col_1_loc = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_1_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_1_loc);

        LinearLayout col_2_loc = new LinearLayout(OutputDFlexocompressaoActivity.this);
        col_2_loc.setOrientation(LinearLayout.VERTICAL);
        tab_loc.addView(col_2_loc);

        TextView TV_locfla = new TextView(OutputDFlexocompressaoActivity.this);
        TV_locfla.setText("FLA\n"+FLA);
        TV_locfla.setTextSize(tam_pequeno);
        TV_locfla.setPadding(50,15,0,15);
        col_1_loc.addView(TV_locfla);
        TV_locfla.setTextColor(Color.BLACK);

        TextView TV_locflm = new TextView(OutputDFlexocompressaoActivity.this);
        TV_locflm.setText("FLM\n"+FLM);
        TV_locflm.setTextSize(tam_pequeno);
        TV_locflm.setPadding(50,15,0,15);
        col_2_loc.addView(TV_locflm);
        TV_locflm.setTextColor(Color.BLACK);

        TextView TV_Qa = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        col_1_loc.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        col_2_loc.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputDFlexocompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        col_1_loc.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputDFlexocompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputDFlexocompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputDFlexocompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputDFlexocompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputDFlexocompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputDFlexocompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esb.setText("ESBELTEZ GLOBAL");
        TV_esb.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esb.setTextSize(tam_grande);
        scroll_results.addView(TV_esb);
        TV_esb.setGravity(Gravity.CENTER);
        TV_esb.setTextColor(Color.WHITE);
        TV_esb.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esb.setPadding(50,20,50,20);

        TextView TV_esbX = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esbX.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,3) ));
        TV_esbX.setTextSize(tam_pequeno);
        TV_esbX.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbX);
        TV_esbX.setTextColor(Color.BLACK);

        TextView TV_esbY = new TextView(OutputDFlexocompressaoActivity.this);
        TV_esbY.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,3) ));
        TV_esbY.setTextSize(tam_pequeno);
        TV_esbY.setPadding(50,15,0,50);
        scroll_results.addView(TV_esbY);
        TV_esbY.setTextColor(Color.BLACK);

        //ANALISE

        //**resis
        TextView TV_momento = new TextView(OutputDFlexocompressaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        TextView TV_mrdx = new TextView(OutputDFlexocompressaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputDFlexocompressaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,15);
        scroll_results.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

        TextView TV_compNCRD = new TextView(OutputDFlexocompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd, 2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);


    }
}
