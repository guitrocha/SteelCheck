package steelcheck.net.steelcheck;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class OutputVTracaoActivity extends AppCompatActivity {
    public final double gama_a1 = 1.10;
    public final double gama_a2 = 1.35;
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
        setContentView(R.layout.activity_output_vtracao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();
        DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
        database.open();
        database.order_by("id");


        if(extras != null)
        {
            int perfil_selected_pos = extras.getInt("perfil");
            double NTSD = extras.getDouble("ntsd");
            double fy = extras.getDouble("fy");
            double lx = extras.getDouble("lx");
            double ly = extras.getDouble("ly");
            int radio = extras.getInt("radio");

            //escoamento secao bruta
            if(radio == 1)
            {
                double NTRD = NTRD_Escoamento_Secao_Bruta(fy, database.get_area(perfil_selected_pos));
                double esbeltez_x = Esbeltez_x(lx, database.get_rx(perfil_selected_pos));
                double esbeltez_y = Esbeltez_y(ly, database.get_ry(perfil_selected_pos));
                double esbeltez = Esbeltez_Final(esbeltez_x, esbeltez_y);
                double coef_uti = Coeficiente_Utilização(NTSD, NTRD);


                Show_Results_Escoamento(database,perfil_selected_pos,database.get_perfil(perfil_selected_pos), fy,
                        lx, ly, NTSD,
                        esbeltez_x, esbeltez_y, esbeltez, NTRD, coef_uti);

            }
            else if(radio == 2)
            {
                double fu = extras.getDouble("fu");
                double Ct = extras.getDouble("ct");
                double An = extras.getDouble("an");
                double NTRD_sb = NTRD_Escoamento_Secao_Bruta(fy, database.get_area(perfil_selected_pos));
                double NTRD_sl = NTRD_Ruptura_Secao_Liquida(Ct, An, fu);
                double NTRD = NTRD_Final(NTRD_sb,NTRD_sl);
                double esbeltez_x = Esbeltez_x(lx, database.get_rx(perfil_selected_pos));
                double esbeltez_y = Esbeltez_y(ly, database.get_ry(perfil_selected_pos));
                double esbeltez = Esbeltez_Final(esbeltez_x, esbeltez_y);
                double coef_uti = Coeficiente_Utilização(NTSD, NTRD);

                Show_Results_Ruptura(database,perfil_selected_pos,database.get_perfil(perfil_selected_pos), fy,
                        lx,ly, An, fu, Ct, NTSD,
                        esbeltez_x, esbeltez_y, esbeltez,
                        NTRD_sb, NTRD_sl, NTRD, coef_uti);


            }


        }
        database.close();
    }

    //CALCULOS DE VERIFICACAO

    public double NTRD_Escoamento_Secao_Bruta(double fy, double Ag)
    {   return ( (Ag * (fy/10)) / (gama_a1) );
    }
    public double NTRD_Ruptura_Secao_Liquida(double Ct, double An, double fu)
    {   return ( (Ct * An * (fu/10)) / (gama_a2) );
    }
    public double NTRD_Final(double NTRD_bruto, double NTRD_liquido)
    {   if(NTRD_bruto < NTRD_liquido)
            return NTRD_bruto;
        return NTRD_liquido;
    }

    public double Esbeltez_x(double lx, double rx)
    {   return ( lx / rx );
    }
    public double Esbeltez_y(double ly, double ry)
    {   return ( ly / ry );
    }
    public double Esbeltez_Final(double e_x, double e_y)
    {   if(e_x > e_y)
            return e_x;
        return e_y;
    }

    public double Coeficiente_Utilização(double NTSD, double NTRD)
    {   return ( NTSD / NTRD );
    }

    //VERIFICACAO

    public boolean NTRD_MaiorIgual_NTSD(double NTRD, double NTSD)
    {   return NTRD >= NTSD;
    }

    public boolean Esbeltez_MenorIgual_300(double esbeltez)
    {   return esbeltez <= 300;
    }

    //CRIACAO LAYOUT
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
    private void Show_Results_Escoamento(DatabaseAccess db, int pos, final String perfil, final double fy, final double lx, final double ly, final double ntsd,
                                         final double esbeltez_x, final double esbeltez_y, final double esbeltez,
                                         final double ntrd, final double coef)
    {   //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVTracaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVTracaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVTracaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVTracaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        TextView TV_ntsd = new TextView(OutputVTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(50,15,0,15);
        scroll_results.addView(TV_ntsd);
        TV_ntsd.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVTracaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        scroll_results.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVTracaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,50);
        scroll_results.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltez = new TextView(OutputVTracaoActivity.this);
        TV_esbeltez.setText("PARÂMETROS DE ESBELTEZ");
        TV_esbeltez.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltez.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltez);
        TV_esbeltez.setGravity(Gravity.CENTER);
        TV_esbeltez.setTextColor(Color.WHITE);
        TV_esbeltez.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltez.setPadding(50,20,50,20);


        final LinearLayout plus_content = new LinearLayout(OutputVTracaoActivity.this);
        plus_content.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(plus_content);

        TextView TV_lambda_x = new TextView(OutputVTracaoActivity.this);
        TV_lambda_x.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbeltez_x,2) ));
        TV_lambda_x.setTextSize(tam_pequeno);
        TV_lambda_x.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_x);
        TV_lambda_x.setTextColor(Color.BLACK);

        TextView TV_lambda_y = new TextView(OutputVTracaoActivity.this);
        TV_lambda_y.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esbeltez_y,2) ));
        TV_lambda_y.setTextSize(tam_pequeno);
        TV_lambda_y.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_y);
        TV_lambda_y.setTextColor(Color.BLACK);

        TextView TV_lambda = new TextView(OutputVTracaoActivity.this);
        TV_lambda.setText(Html.fromHtml("λ = " + casasDecimais(esbeltez,2) ));
        TV_lambda.setTextSize(tam_pequeno);
        TV_lambda.setPadding(50,15,0,50);
        scroll_results.addView(TV_lambda);
        TV_lambda.setTextColor(Color.BLACK);


        //3 - ESCOAMENTO
        TextView TV_escoa = new TextView(OutputVTracaoActivity.this);
        TV_escoa.setText("ESCOAMENTO DA SEÇÃO BRUTA");
        TV_escoa.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_escoa.setTextSize(tam_grande);
        scroll_results.addView(TV_escoa);
        TV_escoa.setGravity(Gravity.CENTER);
        TV_escoa.setTextColor(Color.WHITE);
        TV_escoa.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_escoa.setPadding(50,20,50,20);

        TextView TV_ntrd = new TextView(OutputVTracaoActivity.this);
        TV_ntrd.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_ntrd.setTextSize(tam_pequeno);
        TV_ntrd.setPadding(50,15,0,50);
        scroll_results.addView(TV_ntrd);
        TV_ntrd.setTextColor(Color.BLACK);

        //4 - TRACAO NORMAL
        TextView TV_tracao = new TextView(OutputVTracaoActivity.this);
        TV_tracao.setText("TRAÇÃO RESISTENTE DE CÁLCULO");
        TV_tracao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_tracao.setTextSize(tam_grande);
        scroll_results.addView(TV_tracao);
        TV_tracao.setGravity(Gravity.CENTER);
        TV_tracao.setTextColor(Color.WHITE);
        TV_tracao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_tracao.setPadding(50,20,50,20);

        TextView TV_tracNTRD = new TextView(OutputVTracaoActivity.this);
        TV_tracNTRD.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_tracNTRD.setTextSize(tam_pequeno);
        TV_tracNTRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_tracNTRD);
        TV_tracNTRD.setTextColor(Color.BLACK);

        //5 - COEFICIENTE
        TextView TV_coef = new TextView(OutputVTracaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVTracaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> / N<small><sub>t,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        //6 - VERIFICACAO
        TextView TV_verif = new TextView(OutputVTracaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);
        TV_verif.setGravity(Gravity.CENTER);
        TV_verif.setTextColor(Color.WHITE);
        TV_verif.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verif.setPadding(50,20,50,20);

        if(NTRD_MaiorIgual_NTSD(ntrd,ntsd))
        {
            TextView TV_nt = new TextView(OutputVTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> > N<small><sub>t,Sd</sub></small>: OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(50,15,0,50);
            TV_nt.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nt);
        }
        else
        {
            TextView TV_nt = new TextView(OutputVTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> < N<small><sub>t,Sd</sub></small>: NÃO OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(50,15,0,50);
            TV_nt.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nt);
        }
        if(Esbeltez_MenorIgual_300(esbeltez))
        {
            TextView TV_esb = new TextView(OutputVTracaoActivity.this);
            TV_esb.setText("Esbeltez < 300: OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(50,15,0,50);
            TV_esb.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb);
        }
        else
        {
            TextView TV_esb = new TextView(OutputVTracaoActivity.this);
            TV_esb.setText("Esbeltez > 300: NÃO OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(50,15,0,50);
            TV_esb.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb);
        }

    }
    private void Show_Results_Ruptura(DatabaseAccess db, int pos, final String perfil,final double fy, final double lx, final double ly,final double an,final double fu,final double ct,final double ntsd,
                                      final double esbeltez_x, final double esbeltez_y,final double esbeltez,
                                      final double ntrd_sb,final double ntrd_sl,final double ntrd,final double coef)
    {   //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVTracaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVTracaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVTracaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,15);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        TextView TV_fu = new TextView(OutputVTracaoActivity.this);
        TV_fu.setText(Html.fromHtml("f<small><sub>u</sub></small> = " + casasDecimais(fu,2) + " MPa"));
        TV_fu.setTextSize(tam_pequeno);
        TV_fu.setPadding(50,15,0,50);
        scroll_results.addView(TV_fu);
        TV_fu.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno


        TextView TV_solic = new TextView(OutputVTracaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVTracaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVTracaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVTracaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        TextView TV_ntsd = new TextView(OutputVTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ntsd);
        TV_ntsd.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVTracaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_1.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVTracaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,50);
        contorno_1.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_an = new TextView(OutputVTracaoActivity.this);
        TV_an.setText(Html.fromHtml("A<small><sub>n</sub></small> = " + casasDecimais(an,2) + " cm²"));
        TV_an.setTextSize(tam_pequeno);
        TV_an.setPadding(50,15,0,15);
        contorno_2.addView(TV_an);
        TV_an.setTextColor(Color.BLACK);

        TextView TV_ct = new TextView(OutputVTracaoActivity.this);
        TV_ct.setText(Html.fromHtml("C<small><sub>t</sub></small> = " + casasDecimais(ct,2)));
        TV_ct.setTextSize(tam_pequeno);
        TV_ct.setPadding(50,15,0,15);
        contorno_2.addView(TV_ct);
        TV_ct.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltez = new TextView(OutputVTracaoActivity.this);
        TV_esbeltez.setText("PARÂMETROS DE ESBELTEZ");
        TV_esbeltez.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltez.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltez);
        TV_esbeltez.setGravity(Gravity.CENTER);
        TV_esbeltez.setTextColor(Color.WHITE);
        TV_esbeltez.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltez.setPadding(50,20,50,20);


        final LinearLayout plus_content = new LinearLayout(OutputVTracaoActivity.this);
        plus_content.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(plus_content);

        TextView TV_lambda_x = new TextView(OutputVTracaoActivity.this);
        TV_lambda_x.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbeltez_x,2) ));
        TV_lambda_x.setTextSize(tam_pequeno);
        TV_lambda_x.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_x);
        TV_lambda_x.setTextColor(Color.BLACK);

        TextView TV_lambda_y = new TextView(OutputVTracaoActivity.this);
        TV_lambda_y.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esbeltez_y,2) ));
        TV_lambda_y.setTextSize(tam_pequeno);
        TV_lambda_y.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_y);
        TV_lambda_y.setTextColor(Color.BLACK);

        TextView TV_lambda = new TextView(OutputVTracaoActivity.this);
        TV_lambda.setText(Html.fromHtml("λ = " + casasDecimais(esbeltez,2) ));
        TV_lambda.setTextSize(tam_pequeno);
        TV_lambda.setPadding(50,15,0,50);
        scroll_results.addView(TV_lambda);
        TV_lambda.setTextColor(Color.BLACK);


        //3 - ESCOAMENTO
        TextView TV_escoa = new TextView(OutputVTracaoActivity.this);
        TV_escoa.setText("ESCOAMENTO DA SEÇÃO BRUTA");
        TV_escoa.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_escoa.setTextSize(tam_grande);
        scroll_results.addView(TV_escoa);
        TV_escoa.setGravity(Gravity.CENTER);
        TV_escoa.setTextColor(Color.WHITE);
        TV_escoa.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_escoa.setPadding(50,20,50,20);

        TextView TV_ntrd_bt = new TextView(OutputVTracaoActivity.this);
        TV_ntrd_bt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd_sb,2) + " kN"));
        TV_ntrd_bt.setTextSize(tam_pequeno);
        TV_ntrd_bt.setPadding(50,15,0,50);
        scroll_results.addView(TV_ntrd_bt);
        TV_ntrd_bt.setTextColor(Color.BLACK);

        TextView TV_RUP = new TextView(OutputVTracaoActivity.this);
        TV_RUP.setText("RUPTURA DA SEÇÃO LÍQUIDA");
        TV_RUP.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_RUP.setTextSize(tam_grande);
        scroll_results.addView(TV_RUP);
        TV_RUP.setGravity(Gravity.CENTER);
        TV_RUP.setTextColor(Color.WHITE);
        TV_RUP.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_RUP.setPadding(50,20,50,20);

        TextView TV_ntrd_rp = new TextView(OutputVTracaoActivity.this);
        TV_ntrd_rp.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd_sl,2) + " kN"));
        TV_ntrd_rp.setTextSize(tam_pequeno);
        TV_ntrd_rp.setPadding(50,15,0,50);
        scroll_results.addView(TV_ntrd_rp);
        TV_ntrd_rp.setTextColor(Color.BLACK);

        //4 - TRACAO NORMAL
        TextView TV_tracao = new TextView(OutputVTracaoActivity.this);
        TV_tracao.setText("TRAÇÃO RESISTENTE DE CÁLCULO");
        TV_tracao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_tracao.setTextSize(tam_grande);
        scroll_results.addView(TV_tracao);
        TV_tracao.setGravity(Gravity.CENTER);
        TV_tracao.setTextColor(Color.WHITE);
        TV_tracao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_tracao.setPadding(50,20,50,20);

        TextView TV_tracNTRD = new TextView(OutputVTracaoActivity.this);
        TV_tracNTRD.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_tracNTRD.setTextSize(tam_pequeno);
        TV_tracNTRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_tracNTRD);
        TV_tracNTRD.setTextColor(Color.BLACK);

        //5 - COEFICIENTE
        TextView TV_coef = new TextView(OutputVTracaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVTracaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> / N<small><sub>t,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        //6 - VERIFICACAO
        TextView TV_verif = new TextView(OutputVTracaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);
        TV_verif.setGravity(Gravity.CENTER);
        TV_verif.setTextColor(Color.WHITE);
        TV_verif.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verif.setPadding(50,20,50,20);

        if(NTRD_MaiorIgual_NTSD(ntrd,ntsd))
        {
            TextView TV_nt = new TextView(OutputVTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> > N<small><sub>t,Sd</sub></small>: OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(50,15,0,50);
            TV_nt.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nt);
        }
        else
        {
            TextView TV_nt = new TextView(OutputVTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> < N<small><sub>t,Sd</sub></small>: NÃO OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(50,15,0,50);
            TV_nt.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nt);
        }
        if(Esbeltez_MenorIgual_300(esbeltez))
        {
            TextView TV_esb = new TextView(OutputVTracaoActivity.this);
            TV_esb.setText("Esbeltez < 300: OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(50,15,0,50);
            TV_esb.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb);
        }
        else
        {
            TextView TV_esb = new TextView(OutputVTracaoActivity.this);
            TV_esb.setText("Esbeltez < 300: NÃO OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(50,15,0,50);
            TV_esb.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb);
        }

    }
    //ARREDONDAMENTOS
    private double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }



}
