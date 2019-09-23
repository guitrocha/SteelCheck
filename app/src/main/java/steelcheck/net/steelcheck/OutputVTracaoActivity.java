package steelcheck.net.steelcheck;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class OutputVTracaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;

    private LinearLayout scroll_results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_vtracao);
        Bundle extras = getIntent().getExtras();
        DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
        database.open();
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_id);


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

                System.out.println("\nntrd = " + NTRD + "\n\nesb x = " + esbeltez_x +"\nesb y = " + esbeltez_y +
                        "\nesb = " + esbeltez + "\n\ncoef ut = " + coef_uti);

                double valor = coef_uti;
                        valor = Double.valueOf(String.format(Locale.US, "%.3f", valor));
                System.out.println("\nvalor conv = " + valor);



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

                System.out.println("\nntrd_sb = " + NTRD_sb + "\nntrd_sl = " + NTRD_sl + "\nntrd = " + NTRD + "\n\nesb x = " + esbeltez_x +"\nesb y = " + esbeltez_y +
                        "\nesb = " + esbeltez + "\n\ncoef ut = " + coef_uti);




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
}
