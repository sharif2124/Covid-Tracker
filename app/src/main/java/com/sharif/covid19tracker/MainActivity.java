package com.sharif.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sharif.covid19tracker.Api.ApiUtilities;
import com.sharif.covid19tracker.Api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
private TextView totalConfirm,totalActive,totalRecoverd,totalDeath,totalTests;
private TextView todayConfirm,todayRecoverd,todayDeath,dateTv;
private PieChart pieChart;
    ProgressDialog dialog;

private List<CountryData> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        inti();
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                    list.addAll(response.body());
                    for(int i =0;i<list.size();i++){
                        if(list.get(i).getCountry().equals("Bangladesh")){
                            int confirm =Integer.parseInt( list.get(i).getCases());
                            int active =Integer.parseInt( list.get(i).getActive());
                            int recovered =Integer.parseInt( list.get(i).getRecovered());
                            int death=Integer.parseInt( list.get(i).getDeaths());
                            totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                            totalActive.setText(NumberFormat.getInstance().format(active));
                            totalRecoverd.setText(NumberFormat.getInstance().format(recovered));
                            totalDeath.setText(NumberFormat.getInstance().format(death));


                            todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                            todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                            todayRecoverd.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                            totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                            setText(list.get(i).getUpdated());

                            pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                            pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));
                            pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                            pieChart.addPieSlice(new PieModel("Death",death,getResources().getColor(R.color.red_pie)));
                            pieChart.startAnimation();
                            dialog.dismiss();
                        }
                        else
                        {
                            setText(list.get(i).getUpdated());
                            dialog.dismiss();
                        }

                    }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM,dd,yyy");
        long milliseconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        dateTv.setText("Updated at:"+format.format(calendar.getTime()));
    }

    private void inti() {
        totalConfirm = findViewById(R.id.totalconfirm);
        totalActive = findViewById(R.id.totalactive);
        totalRecoverd = findViewById(R.id.totalrecovered);
        totalDeath = findViewById(R.id.totaldeath);
        totalTests = findViewById(R.id.totaltests);
        todayConfirm = findViewById(R.id.todayconfirm);
        todayRecoverd = findViewById(R.id.todayrecovered);
        todayDeath = findViewById(R.id.todaydeath);
        pieChart = findViewById(R.id.pieChart);
        dateTv = findViewById(R.id.updatedDate);


    }
}