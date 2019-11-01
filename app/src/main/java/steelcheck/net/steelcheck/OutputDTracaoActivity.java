package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class OutputDTracaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;

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


                Show_Results_Escoamento(database.get_perfil(perfil_selected_pos), fy,
                        database.get_area(perfil_selected_pos), NTSD,
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

                Show_Results_Ruptura(database.get_perfil(perfil_selected_pos), fy,
                        database.get_area(perfil_selected_pos), An, fu, Ct, NTSD,
                        esbeltez_x, esbeltez_y, esbeltez,
                        NTRD_sb, NTRD_sl, NTRD, coef_uti);


            }


        }
        database.close();
    }

    //CALCULOS DE VERIFICACAO

    private double NTRD_Escoamento_Secao_Bruta(double fy, double Ag)
    {   return ( (Ag * (fy/10)) / (gama_a1) );
    }
    private double NTRD_Ruptura_Secao_Liquida(double Ct, double An, double fu)
    {   return ( (Ct * An * (fu/10)) / (gama_a2) );
    }
    private double NTRD_Final(double NTRD_bruto, double NTRD_liquido)
    {   if(NTRD_bruto < NTRD_liquido)
            return NTRD_bruto;
        return NTRD_liquido;
    }

    private double Esbeltez_x(double lx, double rx)
    {   return ( lx / rx );
    }
    private double Esbeltez_y(double ly, double ry)
    {   return ( ly / ry );
    }
    private double Esbeltez_Final(double e_x, double e_y)
    {   if(e_x > e_y)
            return e_x;
        return e_y;
    }

    private double Coeficiente_Utilização(double NTSD, double NTRD)
    {   return ( NTSD / NTRD );
    }

    //VERIFICACAO

    private boolean NTRD_MaiorIgual_NTSD(double NTRD, double NTSD)
    {   return NTRD >= NTSD;
    }

    private boolean Esbeltez_MenorIgual_300(double esbeltez)
    {   return esbeltez <= 300;
    }

    //CRIACAO LAYOUT
    private void Show_Results_Escoamento(final String perfil, final double fy, final double ag, final double ntsd,
                                         final double esbeltez_x, final double esbeltez_y, final double esbeltez,
                                         final double ntrd, final double coef)
    {   //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);
        final int tam_grande = 25, tam_pequeno = 18;

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDTracaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputDTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,15,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputDTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,15);
        scroll_results.addView(TV_fy);

        TextView TV_ag = new TextView(OutputDTracaoActivity.this);
        TV_ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + ag + " cm²"));
        TV_ag.setTextSize(tam_pequeno);
        TV_ag.setPadding(0,15,0,15);
        scroll_results.addView(TV_ag);

        TextView TV_ntsd = new TextView(OutputDTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(0,15,0,100);
        scroll_results.addView(TV_ntsd);

        //2 - ESBELTEZ
        TextView TV_esbeltez = new TextView(OutputDTracaoActivity.this);
        TV_esbeltez.setText("ESBELTEZ");
        TV_esbeltez.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltez.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltez);

        ImageButton plus = new ImageButton(OutputDTracaoActivity.this);
        plus.setBackgroundResource(android.R.drawable.arrow_down_float);
        plus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        scroll_results.addView(plus);

        final LinearLayout plus_content = new LinearLayout(OutputDTracaoActivity.this);
        plus_content.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(plus_content);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus_esbeletez(v,esbeltez_x,esbeltez_y,tam_pequeno,plus_content);
            }
        });

        TextView TV_lambda = new TextView(OutputDTracaoActivity.this);
        TV_lambda.setText(Html.fromHtml("λ = " + casasDecimais(esbeltez,2) ));
        TV_lambda.setTextSize(tam_pequeno);
        TV_lambda.setPadding(0,15,0,100);
        scroll_results.addView(TV_lambda);

        //3 - ESCOAMENTO
        TextView TV_escoa = new TextView(OutputDTracaoActivity.this);
        TV_escoa.setText("ESCOAMENTO DA SEÇÃO BRUTA");
        TV_escoa.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_escoa.setTextSize(tam_grande);
        scroll_results.addView(TV_escoa);

        TextView TV_ntrd = new TextView(OutputDTracaoActivity.this);
        TV_ntrd.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_ntrd.setTextSize(tam_pequeno);
        TV_ntrd.setPadding(0,15,0,100);
        scroll_results.addView(TV_ntrd);

        //4 - TRACAO NORMAL
        TextView TV_tracao = new TextView(OutputDTracaoActivity.this);
        TV_tracao.setText("TRAÇÃO NORMAL RESISTENTE DE CÁLCULO");
        TV_tracao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_tracao.setTextSize(tam_grande);
        scroll_results.addView(TV_tracao);

        TextView TV_tracNTRD = new TextView(OutputDTracaoActivity.this);
        TV_tracNTRD.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_tracNTRD.setTextSize(tam_pequeno);
        TV_tracNTRD.setPadding(0,15,0,100);
        scroll_results.addView(TV_tracNTRD);

        //5 - COEFICIENTE
        TextView TV_coef = new TextView(OutputDTracaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);

        TextView TV_coefvalor = new TextView(OutputDTracaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> / N<small><sub>t,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(0,15,0,100);
        scroll_results.addView(TV_coefvalor);

        //6 - VERIFICACAO
        TextView TV_verif = new TextView(OutputDTracaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);

        if(NTRD_MaiorIgual_NTSD(ntrd,ntsd))
        {
            TextView TV_nt = new TextView(OutputDTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> maior que N<small><sub>t,Sd</sub></small>: OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(0,15,0,100);
            TV_nt.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nt);
        }
        else
        {
            TextView TV_nt = new TextView(OutputDTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> menor que N<small><sub>t,Sd</sub></small>: NÃO OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(0,15,0,100);
            TV_nt.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nt);
        }
        if(Esbeltez_MenorIgual_300(esbeltez))
        {
            TextView TV_esb = new TextView(OutputDTracaoActivity.this);
            TV_esb.setText("Esbeltez menor que 300: OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(0,15,0,100);
            TV_esb.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb);
        }
        else
        {
            TextView TV_esb = new TextView(OutputDTracaoActivity.this);
            TV_esb.setText("Esbeltez maior que 300: NÃO OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(0,15,0,100);
            TV_esb.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb);
        }

    }
    private void Show_Results_Ruptura(final String perfil,final double fy,final double ag,final double an,final double fu,final double ct,final double ntsd,
                                      final double esbeltez_x, final double esbeltez_y,final double esbeltez,
                                      final double ntrd_sb,final double ntrd_sl,final double ntrd,final double coef)
    {   //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);
        final int tam_grande = 25, tam_pequeno = 18;

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDTracaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputDTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,15,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputDTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,15);
        scroll_results.addView(TV_fy);

        TextView TV_ag = new TextView(OutputDTracaoActivity.this);
        TV_ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + ag + " cm²"));
        TV_ag.setTextSize(tam_pequeno);
        TV_ag.setPadding(0,15,0,60);
        scroll_results.addView(TV_ag);

        TextView TV_an = new TextView(OutputDTracaoActivity.this);
        TV_an.setText(Html.fromHtml("A<small><sub>n</sub></small> = " + casasDecimais(an,2) + " cm²"));
        TV_an.setTextSize(tam_pequeno);
        TV_an.setPadding(0,15,0,15);
        scroll_results.addView(TV_an);

        TextView TV_fu = new TextView(OutputDTracaoActivity.this);
        TV_fu.setText(Html.fromHtml("f<small><sub>u</sub></small> = " + casasDecimais(fu,2) + " MPa"));
        TV_fu.setTextSize(tam_pequeno);
        TV_fu.setPadding(0,15,0,15);
        scroll_results.addView(TV_fu);

        TextView TV_ct = new TextView(OutputDTracaoActivity.this);
        TV_ct.setText(Html.fromHtml("C<small><sub>t</sub></small> = " + casasDecimais(ct,2)));
        TV_ct.setTextSize(tam_pequeno);
        TV_ct.setPadding(0,15,0,60);
        scroll_results.addView(TV_ct);

        TextView TV_ntsd = new TextView(OutputDTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(0,15,0,100);
        scroll_results.addView(TV_ntsd);

        //2 - ESBELTEZ
        TextView TV_esbeltez = new TextView(OutputDTracaoActivity.this);
        TV_esbeltez.setText("ESBELTEZ");
        TV_esbeltez.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltez.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltez);

        ImageButton plus = new ImageButton(OutputDTracaoActivity.this);
        plus.setBackgroundResource(R.drawable.plus);
        plus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        scroll_results.addView(plus);

        final LinearLayout plus_content = new LinearLayout(OutputDTracaoActivity.this);
        plus_content.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(plus_content);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus_esbeletez(v,esbeltez_x,esbeltez_y,tam_pequeno,plus_content);
            }
        });

        TextView TV_lambda = new TextView(OutputDTracaoActivity.this);
        TV_lambda.setText(Html.fromHtml("λ = " + casasDecimais(esbeltez,2) ));
        TV_lambda.setTextSize(tam_pequeno);
        TV_lambda.setPadding(0,15,0,100);
        scroll_results.addView(TV_lambda);

        //3 - ESCOAMENTO
        TextView TV_escoa = new TextView(OutputDTracaoActivity.this);
        TV_escoa.setText("ESCOAMENTO DA SEÇÃO BRUTA");
        TV_escoa.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_escoa.setTextSize(tam_grande);
        scroll_results.addView(TV_escoa);

        TextView TV_sb = new TextView(OutputDTracaoActivity.this);
        TV_sb.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd_sb,2) + " kN"));
        TV_sb.setTextSize(tam_pequeno);
        TV_sb.setPadding(0,15,0,100);
        scroll_results.addView(TV_sb);

        //4 - RUPTURA
        TextView TV_rup = new TextView(OutputDTracaoActivity.this);
        TV_rup.setText("RUPTURA DA SEÇÃO LÍQUIDA");
        TV_rup.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_rup.setTextSize(tam_grande);
        scroll_results.addView(TV_rup);

        TextView TV_sl = new TextView(OutputDTracaoActivity.this);
        TV_sl.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd_sl,2) + " kN"));
        TV_sl.setTextSize(tam_pequeno);
        TV_sl.setPadding(0,15,0,100);
        scroll_results.addView(TV_sl);

        //5 - TRACAO NORMAL
        TextView TV_tracao = new TextView(OutputDTracaoActivity.this);
        TV_tracao.setText("TRAÇÃO NORMAL RESISTENTE DE CÁLCULO");
        TV_tracao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_tracao.setTextSize(tam_grande);
        scroll_results.addView(TV_tracao);

        TextView TV_tracNTRD = new TextView(OutputDTracaoActivity.this);
        TV_tracNTRD.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_tracNTRD.setTextSize(tam_pequeno);
        TV_tracNTRD.setPadding(0,15,0,100);
        scroll_results.addView(TV_tracNTRD);

        //6 - COEFICIENTE
        TextView TV_coef = new TextView(OutputDTracaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);

        TextView TV_coefvalor = new TextView(OutputDTracaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> / N<small><sub>t,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(0,15,0,100);
        scroll_results.addView(TV_coefvalor);

        //6 - VERIFICACAO
        TextView TV_verif = new TextView(OutputDTracaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);

        if(NTRD_MaiorIgual_NTSD(ntrd,ntsd))
        {
            TextView TV_nt = new TextView(OutputDTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> maior que N<small><sub>t,Sd</sub></small>: OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(0,15,0,100);
            TV_nt.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nt);
        }
        else
        {
            TextView TV_nt = new TextView(OutputDTracaoActivity.this);
            TV_nt.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> menor que N<small><sub>t,Sd</sub></small>: NÃO OK! "));
            TV_nt.setTextSize(tam_pequeno);
            TV_nt.setPadding(0,15,0,100);
            TV_nt.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nt);
        }
        if(Esbeltez_MenorIgual_300(esbeltez))
        {
            TextView TV_esb = new TextView(OutputDTracaoActivity.this);
            TV_esb.setText("Esbeltez menor que 300: OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(0,15,0,100);
            TV_esb.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb);
        }
        else
        {
            TextView TV_esb = new TextView(OutputDTracaoActivity.this);
            TV_esb.setText("Esbeltez maior que 300: NÃO OK!");
            TV_esb.setTextSize(tam_pequeno);
            TV_esb.setPadding(0,15,0,100);
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

    //PLUS
    private void plus_esbeletez(View v, double esbeltez_x, double esbeltez_y, int tam, LinearLayout plus_content)
    {
        if(plus_controler == 0)
        {
            TextView TV_lambda_x = new TextView(OutputDTracaoActivity.this);
            TV_lambda_x.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbeltez_x,2) ));
            TV_lambda_x.setTextSize(tam);
            TV_lambda_x.setPadding(0,15,0,100);
            plus_content.addView(TV_lambda_x);

            TextView TV_lambda_y = new TextView(OutputDTracaoActivity.this);
            TV_lambda_y.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esbeltez_y,2) ));
            TV_lambda_y.setTextSize(tam);
            TV_lambda_y.setPadding(60,15,0,100);
            plus_content.addView(TV_lambda_y);

            v.setBackgroundResource(android.R.drawable.arrow_up_float);
            plus_controler = 1;
        }
        else
        {
            plus_content.removeAllViews();
            v.setBackgroundResource(android.R.drawable.arrow_down_float);
            plus_controler = 0;
        }
    }

}
