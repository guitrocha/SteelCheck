package steelcheck.net.steelcheck;

import android.database.Cursor;
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
        setContentView(R.layout.activity_output_dtracao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();

        OutputVTracaoActivity trac = new OutputVTracaoActivity();

        DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
        database.open();


        if(extras != null)
        {
            double NTSD = extras.getDouble("ntsd");
            double fy = extras.getDouble("fy");
            double lx = extras.getDouble("lx");
            double ly = extras.getDouble("ly");
            String ordem = extras.getString("ordem");
            database.order_by(ordem);
            long count = database.quantTuplas();
            int i;
            double NTRD=0, esbeltez_x=0, esbeltez_y=0, esbeltez=0, coef_uti=0;
            boolean flag =false;
            for( i=1 ; i < count && flag == false ; ++i ) {
                NTRD = trac.NTRD_Escoamento_Secao_Bruta(fy, database.get_area(i));
                esbeltez_x = trac.Esbeltez_x(lx, database.get_rx(i));
                esbeltez_y = trac.Esbeltez_y(ly, database.get_ry(i));
                esbeltez = trac.Esbeltez_Final(esbeltez_x, esbeltez_y);
                coef_uti = trac.Coeficiente_Utilização(NTSD, NTRD);

                flag = trac.Esbeltez_MenorIgual_300(esbeltez) && trac.NTRD_MaiorIgual_NTSD(NTRD,NTSD);
            }
            i--;
            if(flag) {
                String show_ordem = generate_string_ordem(ordem,database,i);
                Show_Results_Escoamento(database.get_perfil(i), show_ordem, fy,
                        database.get_area(i), NTSD,
                        esbeltez_x, esbeltez_y, esbeltez, NTRD, coef_uti);
            }
            else {
                TextView ERRO = new TextView(OutputDTracaoActivity.this);
                scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);
                scroll_results.addView(ERRO);
                ERRO.setText("Nenhum perfil é adequado!");
                ERRO.setTextColor(getResources().getColor(R.color.color_Nok));
                ERRO.setTextSize(25);
            }




        }

        database.close();
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
        }
        return show;
    }
    //CRIACAO LAYOUT
    private void Show_Results_Escoamento(final String perfil, String ordem, final double fy, final double ag, final double ntsd,
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
        TV_perfil.setTextColor(getResources().getColor(R.color.color_ok));
        scroll_results.addView(TV_perfil);

        TextView TV_ordem = new TextView(OutputDTracaoActivity.this);
        TV_ordem.setText(Html.fromHtml(ordem));
        TV_ordem.setTextSize(tam_pequeno);
        TV_ordem.setPadding(0,50,0,15);
        scroll_results.addView(TV_ordem);

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
