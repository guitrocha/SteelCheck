package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class OutputDFlexaoActivity extends AppCompatActivity {

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
            String show_ordem = "";
            if(flag)
                show_ordem = generate_string_ordem(ordem,database,i);
            else
                show_ordem = generate_string_ordem(ordem,database,-1);
            Show_Results_LaminadoW(flex,database,i,flag,database.get_perfil(i), show_ordem,analise, fy,
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
    public double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }
    //CRIACAO DE LAYOUT
    private void Show_Results_LaminadoW(OutputVFlexaoActivity flex, DatabaseAccess db, int pos, boolean flag, String perfil, String ordem,int analise, double fy, double msdx, double msdy, double cb, double vsdx, double vsdy, double flecha, double vao
            , String FLM, double FLM_lambda_b, double FLM_lambda_p, double FLM_lambda_r, double mnflmx, double mnflmy
            , String FLA, double FLA_lambda_b, double FLA_lambda_p, double FLA_lambda_r, double mnfla
            , String FLT, double lb, double FLT_lambda_b, double lp, double FLT_lambda_p, double lr, double FLT_lambda_r, double cb_mnflt
            , double mrdx, double mrdy, double vrdx, double vrdy, double flechaadm, double flechareal)
    {
        ScrollView sv = new ScrollView(OutputDFlexaoActivity.this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));

        LinearLayout scroll_results = new LinearLayout(OutputDFlexaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rel);
        rl.addView(sv);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        if(!flag)
        {
            //1 - PERFIL
            TextView TV_perfil = new TextView(OutputDFlexaoActivity.this);
            TV_perfil.setText("PERFIL ADEQUADO");
            TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil);
            TV_perfil.setGravity(Gravity.CENTER);
            TV_perfil.setTextColor(Color.WHITE);
            TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_perfil.setPadding(50,20,50,20);

            TextView TV_perfil_dimen = new TextView(OutputDFlexaoActivity.this);
            TV_perfil_dimen.setText("NÃO ENCONTRADO");
            TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil_dimen.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil_dimen);
            TV_perfil_dimen.setGravity(Gravity.CENTER);
            TV_perfil_dimen.setTextColor(Color.WHITE);
            TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_Nok));
            TV_perfil_dimen.setPadding(50,20,50,20);

            TextView TV_param = new TextView(OutputDFlexaoActivity.this);
            TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
            TV_param.setTextSize(tam_dimens);
            TV_param.setPadding(50,15,0,50);
            scroll_results.addView(TV_param);

            TextView TV_nenhum = new TextView(OutputDFlexaoActivity.this);
            TV_nenhum.setText(Html.fromHtml("Nenhum perfil atende às condições impostas."));
            TV_nenhum.setTextSize(tam_pequeno);
            TV_nenhum.setPadding(50,15,50,15);
            scroll_results.addView(TV_nenhum);
            TV_nenhum.setTextColor(Color.BLACK);

            //2 - Parametros
            TextView TV_parametros = new TextView(OutputDFlexaoActivity.this);
            TV_parametros.setText("PARÂMETROS DO MATERIAL");
            TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_parametros.setTextSize(tam_grande);
            scroll_results.addView(TV_parametros);
            TV_parametros.setGravity(Gravity.CENTER);
            TV_parametros.setTextColor(Color.WHITE);
            TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_parametros.setPadding(50,20,50,20);

            TextView TV_elasticidade = new TextView(OutputDFlexaoActivity.this);
            TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
            TV_elasticidade.setTextSize(tam_pequeno);
            TV_elasticidade.setPadding(50,15,0,15);
            scroll_results.addView(TV_elasticidade);
            TV_elasticidade.setTextColor(Color.BLACK);

            TextView TV_fy = new TextView(OutputDFlexaoActivity.this);
            TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
            TV_fy.setTextSize(tam_pequeno);
            TV_fy.setPadding(50,15,0,50);
            scroll_results.addView(TV_fy);
            TV_fy.setTextColor(Color.BLACK);

            //3 - Solicitacoes e contorno
            TextView TV_solic = new TextView(OutputDFlexaoActivity.this);
            TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
            TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_solic.setTextSize(tam_grande);
            scroll_results.addView(TV_solic);
            TV_solic.setGravity(Gravity.CENTER);
            TV_solic.setTextColor(Color.WHITE);
            TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_solic.setPadding(50,20,50,20);

            LinearLayout contorno = new LinearLayout(OutputDFlexaoActivity.this);
            contorno.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(contorno);

            LinearLayout contorno_1 = new LinearLayout(OutputDFlexaoActivity.this);
            contorno_1.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_1);

            LinearLayout contorno_2 = new LinearLayout(OutputDFlexaoActivity.this);
            contorno_2.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_2);


            TextView TV_msdx = new TextView(OutputDFlexaoActivity.this);
            TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
            TV_msdx.setTextSize(tam_pequeno);
            TV_msdx.setPadding(50,15,0,15);
            contorno_1.addView(TV_msdx);
            TV_msdx.setTextColor(Color.BLACK);

            TextView TV_msdy = new TextView(OutputDFlexaoActivity.this);
            TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
            TV_msdy.setTextSize(tam_pequeno);
            TV_msdy.setPadding(50,15,0,15);
            contorno_1.addView(TV_msdy);
            TV_msdy.setTextColor(Color.BLACK);

            TextView TV_cb = new TextView(OutputDFlexaoActivity.this);
            TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
            TV_cb.setTextSize(tam_pequeno);
            TV_cb.setPadding(50,15,0,50);
            contorno_1.addView(TV_cb);
            TV_cb.setTextColor(Color.BLACK);

            //**ATRIBUTOS ANALISE
            if (analise == 1 || analise == 3)
            {
                TextView TV_vsdx = new TextView(OutputDFlexaoActivity.this);
                TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
                TV_vsdx.setTextSize(tam_pequeno);
                TV_vsdx.setPadding(50,15,0,15);
                contorno_2.addView(TV_vsdx);
                TV_vsdx.setTextColor(Color.BLACK);

                TextView TV_vsdy = new TextView(OutputDFlexaoActivity.this);
                TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
                TV_vsdy.setTextSize(tam_pequeno);
                TV_vsdy.setPadding(50,15,0,15);
                contorno_2.addView(TV_vsdy);
                TV_vsdy.setTextColor(Color.BLACK);
            }
            if( analise == 2 || analise == 3)
            {
                TextView TV_flecha = new TextView(OutputDFlexaoActivity.this);
                TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
                TV_flecha.setTextSize(tam_pequeno);
                TV_flecha.setPadding(50,15,0,15);
                contorno_2.addView(TV_flecha);
                TV_flecha.setTextColor(Color.BLACK);

                TextView TV_vao = new TextView(OutputDFlexaoActivity.this);
                TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
                TV_vao.setTextSize(tam_pequeno);
                TV_vao.setPadding(50,15,0,50);
                contorno_2.addView(TV_vao);
                TV_vao.setTextColor(Color.BLACK);
            }
            return;
        }

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDFlexaoActivity.this);
        TV_perfil.setText("PERFIL ADEQUADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputDFlexaoActivity.this);
        TV_perfil_dimen.setText(perfil);
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_ok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_param = new TextView(OutputDFlexaoActivity.this);
        TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
        TV_param.setTextSize(tam_dimens);
        TV_param.setPadding(50,15,0,50);
        scroll_results.addView(TV_param);
        flex.Show_Dimensoes_Database_Perfil(db,scroll_results,OutputDFlexaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputDFlexaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputDFlexaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputDFlexaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputDFlexaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputDFlexaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputDFlexaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputDFlexaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);


        TextView TV_msdx = new TextView(OutputDFlexaoActivity.this);
        TV_msdx.setText(Html.fromHtml("M<small><sub>Sd,x</sub></small> = " + casasDecimais(msdx,2) + " kNm"));
        TV_msdx.setTextSize(tam_pequeno);
        TV_msdx.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdx);
        TV_msdx.setTextColor(Color.BLACK);

        TextView TV_msdy = new TextView(OutputDFlexaoActivity.this);
        TV_msdy.setText(Html.fromHtml("M<small><sub>Sd,y</sub></small> = " + casasDecimais(msdy,2) + " kNm"));
        TV_msdy.setTextSize(tam_pequeno);
        TV_msdy.setPadding(50,15,0,15);
        contorno_1.addView(TV_msdy);
        TV_msdy.setTextColor(Color.BLACK);

        TextView TV_cb = new TextView(OutputDFlexaoActivity.this);
        TV_cb.setText(Html.fromHtml("C<small><sub>b</sub></small> = " + casasDecimais(cb,3) ));
        TV_cb.setTextSize(tam_pequeno);
        TV_cb.setPadding(50,15,0,50);
        contorno_1.addView(TV_cb);
        TV_cb.setTextColor(Color.BLACK);

        //**ATRIBUTOS ANALISE
        if (analise == 1 || analise == 3)
        {
            TextView TV_vsdx = new TextView(OutputDFlexaoActivity.this);
            TV_vsdx.setText(Html.fromHtml("V<small><sub>Sd,x</sub></small> = " + casasDecimais(vsdx,2) + " kN"));
            TV_vsdx.setTextSize(tam_pequeno);
            TV_vsdx.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdx);
            TV_vsdx.setTextColor(Color.BLACK);

            TextView TV_vsdy = new TextView(OutputDFlexaoActivity.this);
            TV_vsdy.setText(Html.fromHtml("V<small><sub>Sd,y</sub></small> = " + casasDecimais(vsdy,2) + " kN"));
            TV_vsdy.setTextSize(tam_pequeno);
            TV_vsdy.setPadding(50,15,0,15);
            contorno_2.addView(TV_vsdy);
            TV_vsdy.setTextColor(Color.BLACK);
        }
        if( analise == 2 || analise == 3)
        {
            TextView TV_flecha = new TextView(OutputDFlexaoActivity.this);
            TV_flecha.setText(Html.fromHtml("δ<small><sub>max</sub></small> = " + casasDecimais(flecha,2) + " mm"));
            TV_flecha.setTextSize(tam_pequeno);
            TV_flecha.setPadding(50,15,0,15);
            contorno_2.addView(TV_flecha);
            TV_flecha.setTextColor(Color.BLACK);

            TextView TV_vao = new TextView(OutputDFlexaoActivity.this);
            TV_vao.setText(Html.fromHtml("Vão = " + casasDecimais(vao,2) + " m"));
            TV_vao.setTextSize(tam_pequeno);
            TV_vao.setPadding(50,15,0,50);
            contorno_2.addView(TV_vao);
            TV_vao.setTextColor(Color.BLACK);
        }

        //FLM
        TextView TV_FLM = new TextView(OutputDFlexaoActivity.this);
        TV_FLM.setText("FLAMBAGEM LOCAL DA MESA");
        TV_FLM.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_FLM.setTextSize(tam_grande);
        scroll_results.addView(TV_FLM);
        TV_FLM.setGravity(Gravity.CENTER);
        TV_FLM.setTextColor(Color.WHITE);
        TV_FLM.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLM.setPadding(50,20,50,20);

        TextView TV_secaoflm = new TextView(OutputDFlexaoActivity.this);
        TV_secaoflm.setText(FLM);
        TV_secaoflm.setTextSize(tam_pequeno);
        TV_secaoflm.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflm);
        TV_secaoflm.setTextColor(Color.BLACK);

        LinearLayout tab_flm = new LinearLayout(OutputDFlexaoActivity.this);
        tab_flm.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flm);

        LinearLayout col_1_flm = new LinearLayout(OutputDFlexaoActivity.this);
        col_1_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_1_flm);

        LinearLayout col_2_flm = new LinearLayout(OutputDFlexaoActivity.this);
        col_2_flm.setOrientation(LinearLayout.VERTICAL);
        tab_flm.addView(col_2_flm);

        TextView TV_flm_b = new TextView(OutputDFlexaoActivity.this);
        TV_flm_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLM_lambda_b, 2)));
        TV_flm_b.setTextSize(tam_pequeno);
        TV_flm_b.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_b);
        TV_flm_b.setTextColor(Color.BLACK);

        TextView TV_flm_p = new TextView(OutputDFlexaoActivity.this);
        TV_flm_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLM_lambda_p, 2)));
        TV_flm_p.setTextSize(tam_pequeno);
        TV_flm_p.setPadding(50,15,0,15);
        col_1_flm.addView(TV_flm_p);
        TV_flm_p.setTextColor(Color.BLACK);

        TextView TV_flm_r = new TextView(OutputDFlexaoActivity.this);
        TV_flm_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLM_lambda_r, 2)));
        TV_flm_r.setTextSize(tam_pequeno);
        TV_flm_r.setPadding(50,15,0,50);
        col_1_flm.addView(TV_flm_r);
        TV_flm_r.setTextColor(Color.BLACK);

        TextView TV_flm_mnx = new TextView(OutputDFlexaoActivity.this);
        TV_flm_mnx.setText(Html.fromHtml("M<small><sub>n,FLM,x</sub></small> = " + casasDecimais(mnflmx, 2) + " kNm"));
        TV_flm_mnx.setTextSize(tam_pequeno);
        TV_flm_mnx.setPadding(100,15,0,15);
        col_2_flm.addView(TV_flm_mnx);
        TV_flm_mnx.setTextColor(Color.BLACK);

        TextView TV_flm_mny = new TextView(OutputDFlexaoActivity.this);
        TV_flm_mny.setText(Html.fromHtml("M<small><sub>n,FLM,y</sub></small> = " + casasDecimais(mnflmy, 2) + " kNm"));
        TV_flm_mny.setTextSize(tam_pequeno);
        TV_flm_mny.setPadding(100,15,0,50);
        col_2_flm.addView(TV_flm_mny);
        TV_flm_mny.setTextColor(Color.BLACK);

        //FLA
        TextView TV_FLA = new TextView(OutputDFlexaoActivity.this);
        TV_FLA.setText("FLAMBAGEM LOCAL DA ALMA");
        TV_FLA.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLA.setTextSize(tam_grande);
        scroll_results.addView(TV_FLA);
        TV_FLA.setGravity(Gravity.CENTER);
        TV_FLA.setTextColor(Color.WHITE);
        TV_FLA.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLA.setPadding(50,20,50,20);

        TextView TV_secaofla = new TextView(OutputDFlexaoActivity.this);
        TV_secaofla.setText(FLA);
        TV_secaofla.setTextSize(tam_pequeno);
        TV_secaofla.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaofla);
        TV_secaofla.setTextColor(Color.BLACK);

        LinearLayout tab_fla = new LinearLayout(OutputDFlexaoActivity.this);
        tab_fla.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_fla);

        LinearLayout col_1_fla = new LinearLayout(OutputDFlexaoActivity.this);
        col_1_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_1_fla);

        LinearLayout col_2_fla = new LinearLayout(OutputDFlexaoActivity.this);
        col_2_fla.setOrientation(LinearLayout.VERTICAL);
        tab_fla.addView(col_2_fla);

        TextView TV_fla_b = new TextView(OutputDFlexaoActivity.this);
        TV_fla_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLA_lambda_b,2) ));
        TV_fla_b.setTextSize(tam_pequeno);
        TV_fla_b.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_b);
        TV_fla_b.setTextColor(Color.BLACK);

        TextView TV_fla_p = new TextView(OutputDFlexaoActivity.this);
        TV_fla_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLA_lambda_p,2) ));
        TV_fla_p.setTextSize(tam_pequeno);
        TV_fla_p.setPadding(50,15,0,15);
        col_1_fla.addView(TV_fla_p);
        TV_fla_p.setTextColor(Color.BLACK);

        TextView TV_fla_r = new TextView(OutputDFlexaoActivity.this);
        TV_fla_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLA_lambda_r,2) ));
        TV_fla_r.setTextSize(tam_pequeno);
        TV_fla_r.setPadding(50,15,0,50);
        col_1_fla.addView(TV_fla_r);
        TV_fla_r.setTextColor(Color.BLACK);

        TextView TV_fla_mn = new TextView(OutputDFlexaoActivity.this);
        TV_fla_mn.setText(Html.fromHtml("M<small><sub>n,FLA</sub></small> = " + casasDecimais(mnfla,2) + " kNm" ));
        TV_fla_mn.setTextSize(tam_pequeno);
        TV_fla_mn.setPadding(100,15,0,15);
        col_2_fla.addView(TV_fla_mn);
        TV_fla_mn.setTextColor(Color.BLACK);

        //FLT
        TextView TV_FLT = new TextView(OutputDFlexaoActivity.this);
        TV_FLT.setText("FLAMBAGEM LATERAL COM TORÇÃO");
        TV_FLT.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_FLT.setTextSize(tam_grande);
        scroll_results.addView(TV_FLT);
        TV_FLT.setGravity(Gravity.CENTER);
        TV_FLT.setTextColor(Color.WHITE);
        TV_FLT.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_FLT.setPadding(50,20,50,20);

        TextView TV_secaoflt = new TextView(OutputDFlexaoActivity.this);
        TV_secaoflt.setText(FLT);
        TV_secaoflt.setTextSize(tam_pequeno);
        TV_secaoflt.setPadding(50,15,0,15);
        scroll_results.addView(TV_secaoflt);
        TV_secaoflt.setTextColor(Color.BLACK);

        LinearLayout tab_flt = new LinearLayout(OutputDFlexaoActivity.this);
        tab_flt.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_flt);

        LinearLayout col_1_flt = new LinearLayout(OutputDFlexaoActivity.this);
        col_1_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_1_flt);

        LinearLayout col_2_flt = new LinearLayout(OutputDFlexaoActivity.this);
        col_2_flt.setOrientation(LinearLayout.VERTICAL);
        tab_flt.addView(col_2_flt);

        TextView TV_flt_lb = new TextView(OutputDFlexaoActivity.this);
        TV_flt_lb.setText(Html.fromHtml("ℓ<small><sub>b</sub></small> = " + casasDecimais(lb,2) + " cm"));
        TV_flt_lb.setTextSize(tam_pequeno);
        TV_flt_lb.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lb);
        TV_flt_lb.setTextColor(Color.BLACK);

        TextView TV_flt_b = new TextView(OutputDFlexaoActivity.this);
        TV_flt_b.setText(Html.fromHtml("λ<small><sub>b</sub></small> = " + casasDecimais(FLT_lambda_b,2)));
        TV_flt_b.setTextSize(tam_pequeno);
        TV_flt_b.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_b);
        TV_flt_b.setTextColor(Color.BLACK);

        TextView TV_flt_lp = new TextView(OutputDFlexaoActivity.this);
        TV_flt_lp.setText(Html.fromHtml("ℓ<small><sub>p</sub></small> = " + casasDecimais(lp,2) + " cm"));
        TV_flt_lp.setTextSize(tam_pequeno);
        TV_flt_lp.setPadding(50,15,0,15);
        col_1_flt.addView(TV_flt_lp);
        TV_flt_lp.setTextColor(Color.BLACK);

        TextView TV_flt_p = new TextView(OutputDFlexaoActivity.this);
        TV_flt_p.setText(Html.fromHtml("λ<small><sub>p</sub></small> = " + casasDecimais(FLT_lambda_p,2)));
        TV_flt_p.setTextSize(tam_pequeno);
        TV_flt_p.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_p);
        TV_flt_p.setTextColor(Color.BLACK);

        TextView TV_flt_lr = new TextView(OutputDFlexaoActivity.this);
        TV_flt_lr.setText(Html.fromHtml("ℓ<small><sub>r</sub></small> = " + casasDecimais(lr,2) + " cm"));
        TV_flt_lr.setTextSize(tam_pequeno);
        TV_flt_lr.setPadding(50,15,0,100);
        col_1_flt.addView(TV_flt_lr);
        TV_flt_lr.setTextColor(Color.BLACK);

        TextView TV_flt_r = new TextView(OutputDFlexaoActivity.this);
        TV_flt_r.setText(Html.fromHtml("λ<small><sub>r</sub></small> = " + casasDecimais(FLT_lambda_r,2)));
        TV_flt_r.setTextSize(tam_pequeno);
        TV_flt_r.setPadding(100,15,0,15);
        col_2_flt.addView(TV_flt_r);
        TV_flt_r.setTextColor(Color.BLACK);

        TextView TV_flt_cbmn = new TextView(OutputDFlexaoActivity.this);
        TV_flt_cbmn.setText(Html.fromHtml("C<small><sub>b</sub></small> . M<small><sub>n,FLT</sub></small> = " + casasDecimais(cb_mnflt,2) + " kNm"));
        TV_flt_cbmn.setTextSize(tam_pequeno);
        TV_flt_cbmn.setPadding(50,15,0,50);
        scroll_results.addView(TV_flt_cbmn);
        TV_flt_cbmn.setTextColor(Color.BLACK);

        //ANALISE
        //**resis
        TextView TV_momento = new TextView(OutputDFlexaoActivity.this);
        TV_momento.setText("ESFORÇOS RESISTENTES DE CÁLCULO");
        TV_momento.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TV_momento.setTextSize(tam_grande);
        scroll_results.addView(TV_momento);
        TV_momento.setGravity(Gravity.CENTER);
        TV_momento.setTextColor(Color.WHITE);
        TV_momento.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_momento.setPadding(50,20,50,20);

        LinearLayout tab_esf = new LinearLayout(OutputDFlexaoActivity.this);
        tab_esf.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(tab_esf);

        LinearLayout col_1_esf = new LinearLayout(OutputDFlexaoActivity.this);
        col_1_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_1_esf);

        LinearLayout col_2_esf = new LinearLayout(OutputDFlexaoActivity.this);
        col_2_esf.setOrientation(LinearLayout.VERTICAL);
        tab_esf.addView(col_2_esf);

        TextView TV_mrdx = new TextView(OutputDFlexaoActivity.this);
        TV_mrdx.setText(Html.fromHtml("M<small><sub>Rd,x</sub></small> = " + casasDecimais(mrdx, 2) + " kNm"));
        TV_mrdx.setTextSize(tam_pequeno);
        TV_mrdx.setPadding(50,15,0,15);
        col_1_esf.addView(TV_mrdx);
        TV_mrdx.setTextColor(Color.BLACK);

        TextView TV_mrdy = new TextView(OutputDFlexaoActivity.this);
        TV_mrdy.setText(Html.fromHtml("M<small><sub>Rd,y</sub></small> = " + casasDecimais(mrdy, 2) + " kNm"));
        TV_mrdy.setTextSize(tam_pequeno);
        TV_mrdy.setPadding(50,15,0,50);
        col_1_esf.addView(TV_mrdy);
        TV_mrdy.setTextColor(Color.BLACK);

        //**cortante
        if(analise == 1 || analise == 3)
        {
            TextView TV_vrdx = new TextView(OutputDFlexaoActivity.this);
            TV_vrdx.setText(Html.fromHtml("V<small><sub>Rd,x</sub></small> = " + casasDecimais(vrdx,2) + " kN"));
            TV_vrdx.setTextSize(tam_pequeno);
            TV_vrdx.setPadding(50,15,0,15);
            col_2_esf.addView(TV_vrdx);
            TV_vrdx.setTextColor(Color.BLACK);

            TextView TV_vrdy = new TextView(OutputDFlexaoActivity.this);
            TV_vrdy.setText(Html.fromHtml("V<small><sub>Rd,y</sub></small> = " + casasDecimais(vrdy,2) + " kN"));
            TV_vrdy.setTextSize(tam_pequeno);
            TV_vrdy.setPadding(50,15,0,50);
            col_2_esf.addView(TV_vrdy);
            TV_vrdy.setTextColor(Color.BLACK);
        }

        //**flecha
        if(analise == 2 || analise == 3)
        {
            TextView TV_flechaadm = new TextView(OutputDFlexaoActivity.this);
            TV_flechaadm.setText("FLECHA ADMISSÍVEL");
            TV_flechaadm.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_flechaadm.setTextSize(tam_grande);
            scroll_results.addView(TV_flechaadm);
            TV_flechaadm.setGravity(Gravity.CENTER);
            TV_flechaadm.setTextColor(Color.WHITE);
            TV_flechaadm.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_flechaadm.setPadding(50,20,50,20);

            TextView TV_fadm = new TextView(OutputDFlexaoActivity.this);
            TV_fadm.setText(Html.fromHtml("δ<small><sub>adm</sub></small> = " + casasDecimais(flechaadm,2) + " mm"));
            TV_fadm.setTextSize(tam_pequeno);
            TV_fadm.setPadding(50,15,0,50);
            scroll_results.addView(TV_fadm);
            TV_fadm.setTextColor(Color.BLACK);
        }

    }

}
