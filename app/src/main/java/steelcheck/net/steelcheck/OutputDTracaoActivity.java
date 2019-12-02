package steelcheck.net.steelcheck;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class OutputDTracaoActivity extends AppCompatActivity {

    private LinearLayout scroll_results;
    private int plus_controler = 0;
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
                Show_Results_Escoamento(trac, database, i,database.get_perfil(i), show_ordem, fy,lx,ly, NTSD,
                        esbeltez_x, esbeltez_y, esbeltez, NTRD, coef_uti);
            }
            else {
                String show_ordem = generate_string_ordem(ordem,database,-1);
                Show_ERRO(fy,lx,ly,NTSD,show_ordem);
            }
        }

        database.close();
    }

    private String generate_string_ordem(String ordem, DatabaseAccess db, int i)
    {
        String show = "";
        if(i==-1)
        {
            switch(ordem)
            {
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
            }
        }

        return show;
    }
    //CRIACAO LAYOUT
    private void Show_Results_Escoamento(OutputVTracaoActivity trac, DatabaseAccess db, int pos,final String perfil, String ordem, final double fy, final double lx, final double ly, final double ntsd,
                                         final double esbeltez_x, final double esbeltez_y, final double esbeltez,
                                         final double ntrd, final double coef)
    {   //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id_dtrac);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDTracaoActivity.this);
        TV_perfil.setText("PERFIL ADEQUADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputDTracaoActivity.this);
        TV_perfil_dimen.setText(perfil);
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_ok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_param = new TextView(OutputDTracaoActivity.this);
        TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
        TV_param.setTextSize(tam_dimens);
        TV_param.setPadding(50,15,0,50);
        scroll_results.addView(TV_param);
        //TV_param.setTextColor(Color.BLACK);

        trac.Show_Dimensoes_Database_Perfil(db,scroll_results,OutputDTracaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputDTracaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputDTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputDTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputDTracaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        TextView TV_ntsd = new TextView(OutputDTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(50,15,0,15);
        scroll_results.addView(TV_ntsd);
        TV_ntsd.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputDTracaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        scroll_results.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputDTracaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,50);
        scroll_results.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltez = new TextView(OutputDTracaoActivity.this);
        TV_esbeltez.setText("PARÂMETROS DE ESBELTEZ");
        TV_esbeltez.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltez.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltez);
        TV_esbeltez.setGravity(Gravity.CENTER);
        TV_esbeltez.setTextColor(Color.WHITE);
        TV_esbeltez.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltez.setPadding(50,20,50,20);


        final LinearLayout plus_content = new LinearLayout(OutputDTracaoActivity.this);
        plus_content.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(plus_content);

        TextView TV_lambda_x = new TextView(OutputDTracaoActivity.this);
        TV_lambda_x.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbeltez_x,2) ));
        TV_lambda_x.setTextSize(tam_pequeno);
        TV_lambda_x.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_x);
        TV_lambda_x.setTextColor(Color.BLACK);

        TextView TV_lambda_y = new TextView(OutputDTracaoActivity.this);
        TV_lambda_y.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esbeltez_y,2) ));
        TV_lambda_y.setTextSize(tam_pequeno);
        TV_lambda_y.setPadding(50,15,0,15);
        plus_content.addView(TV_lambda_y);
        TV_lambda_y.setTextColor(Color.BLACK);

        TextView TV_lambda = new TextView(OutputDTracaoActivity.this);
        TV_lambda.setText(Html.fromHtml("λ = " + casasDecimais(esbeltez,2) ));
        TV_lambda.setTextSize(tam_pequeno);
        TV_lambda.setPadding(50,15,0,50);
        scroll_results.addView(TV_lambda);
        TV_lambda.setTextColor(Color.BLACK);


        //4 - TRACAO NORMAL
        TextView TV_tracao = new TextView(OutputDTracaoActivity.this);
        TV_tracao.setText("TRAÇÃO RESISTENTE DE CÁLCULO");
        TV_tracao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_tracao.setTextSize(tam_grande);
        scroll_results.addView(TV_tracao);
        TV_tracao.setGravity(Gravity.CENTER);
        TV_tracao.setTextColor(Color.WHITE);
        TV_tracao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_tracao.setPadding(50,20,50,20);

        TextView TV_tracNTRD = new TextView(OutputDTracaoActivity.this);
        TV_tracNTRD.setText(Html.fromHtml("N<small><sub>t,Rd</sub></small> = " + casasDecimais(ntrd,2) + " kN"));
        TV_tracNTRD.setTextSize(tam_pequeno);
        TV_tracNTRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_tracNTRD);
        TV_tracNTRD.setTextColor(Color.BLACK);

        //5 - COEFICIENTE
        TextView TV_coef = new TextView(OutputDTracaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputDTracaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> / N<small><sub>t,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);



    }
    private void Show_ERRO(final double fy, final double lx, final double ly, final double ntsd, String ordem)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id_dtrac);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDTracaoActivity.this);
        TV_perfil.setText("PERFIL ADEQUADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputDTracaoActivity.this);
        TV_perfil_dimen.setText("NÃO ENCONTRADO");
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_Nok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_param = new TextView(OutputDTracaoActivity.this);
        TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
        TV_param.setTextSize(tam_dimens);
        TV_param.setPadding(50,15,0,50);
        scroll_results.addView(TV_param);

        TextView TV_nenhum = new TextView(OutputDTracaoActivity.this);
        TV_nenhum.setText(Html.fromHtml("Nenhum perfil atende às condições impostas."));
        TV_nenhum.setTextSize(tam_pequeno);
        TV_nenhum.setPadding(50,15,50,15);
        scroll_results.addView(TV_nenhum);
        TV_nenhum.setTextColor(Color.BLACK);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputDTracaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputDTracaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputDTracaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputDTracaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        TextView TV_ntsd = new TextView(OutputDTracaoActivity.this);
        TV_ntsd.setText(Html.fromHtml("N<small><sub>t,Sd</sub></small> = " + casasDecimais(ntsd,2) + " kN"));
        TV_ntsd.setTextSize(tam_pequeno);
        TV_ntsd.setPadding(50,15,0,15);
        scroll_results.addView(TV_ntsd);
        TV_ntsd.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputDTracaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        scroll_results.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputDTracaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,50);
        scroll_results.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);
    }
    //ARREDONDAMENTOS
    private double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }
}
