package com.example.prudentialfinance.Activities.Card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.AccountMonthly;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Accounts.AccountChartViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountChartActivity extends AppCompatActivity {
    AccountChartViewModel viewModel;
    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;
    User authUser;
    SiteSettings appInfo;
    Map<String, String> headers;

    ImageButton backBtn;
    AppCompatButton exportBtn;
    SwipeRefreshLayout swipeRefreshLayout;

    AnyChartView anyChartView;
    Cartesian cartesian;
    Column column;
    AccountMonthly data;

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_chart);


        account = getIntent().getParcelableExtra("account");
        setComponent();

        setControl();
        setEvent();

        loadDataChart();

        viewModel.getData(headers, account);

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(AccountChartViewModel.class);
        authUser = global.getAuthUser();
        appInfo = global.getAppInfo();
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        exportBtn.setOnClickListener(view -> {
            createExcel(view);
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getData(headers, account);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        exportBtn = findViewById(R.id.exportBtn);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
    }

    private void loadDataChart(){
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new ValueDataEntry("", 0.0));

        cartesian = AnyChart.column();
        column = cartesian.column(seriesData);

        column.fill("#059142 0.7");
        column.stroke("#059142");
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format(getString(R.string.money_string) + ": {%Value} " + appInfo.getCurrency());

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        anyChartView.setChart(cartesian);

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                data = object.getData();
                System.out.println(data.toString());
                List<DataEntry> list = new ArrayList<>();
                list.add(new ValueDataEntry(getString(R.string.jan_short), data.getJan()));
                list.add(new ValueDataEntry(getString(R.string.feb_short), data.getFeb()));
                list.add(new ValueDataEntry(getString(R.string.mar_short), data.getMar()));
                list.add(new ValueDataEntry(getString(R.string.apr_short), data.getApr()));
                list.add(new ValueDataEntry(getString(R.string.may_short), data.getMay()));
                list.add(new ValueDataEntry(getString(R.string.jun_short), data.getJun()));
                list.add(new ValueDataEntry(getString(R.string.jul_short), data.getJul()));
                list.add(new ValueDataEntry(getString(R.string.aug_short), data.getAug()));
                list.add(new ValueDataEntry(getString(R.string.sep_short), data.getSep()));
                list.add(new ValueDataEntry(getString(R.string.oct_short), data.getOct()));
                list.add(new ValueDataEntry(getString(R.string.nov_short), data.getNov()));
                list.add(new ValueDataEntry(getString(R.string.dec_short), data.getDec()));
                column.data(list);
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

    }

    public void createExcel(View view)
    {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        ArrayList<String> headers = new ArrayList<>();
        headers.add(getString(R.string.jan));
        headers.add(getString(R.string.feb));
        headers.add(getString(R.string.mar));
        headers.add(getString(R.string.apr));
        headers.add(getString(R.string.may));
        headers.add(getString(R.string.jun));
        headers.add(getString(R.string.jul));
        headers.add(getString(R.string.aug));
        headers.add(getString(R.string.sep));
        headers.add(getString(R.string.oct));
        headers.add(getString(R.string.nov));
        headers.add(getString(R.string.dec));

        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < headers.size(); i++){
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(headers.get(i));
        }

        HSSFRow hssfRow1 = hssfSheet.createRow(1);

        HSSFCell hssfCell = hssfRow1.createCell(0);
        hssfCell.setCellValue(data.getJan());

        hssfCell = hssfRow1.createCell(1);
        hssfCell.setCellValue(data.getFeb());

        hssfCell = hssfRow1.createCell(2);
        hssfCell.setCellValue(data.getMar());

        hssfCell = hssfRow1.createCell(3);
        hssfCell.setCellValue(data.getApr());

        hssfCell = hssfRow1.createCell(4);
        hssfCell.setCellValue(data.getMay());

        hssfCell = hssfRow1.createCell(5);
        hssfCell.setCellValue(data.getJun());

        hssfCell = hssfRow1.createCell(6);
        hssfCell.setCellValue(data.getJul());

        hssfCell = hssfRow1.createCell(7);
        hssfCell.setCellValue(data.getAug());

        hssfCell = hssfRow1.createCell(8);
        hssfCell.setCellValue(data.getSep());

        hssfCell = hssfRow1.createCell(9);
        hssfCell.setCellValue(data.getOct());

        hssfCell = hssfRow1.createCell(10);
        hssfCell.setCellValue(data.getNov());

        hssfCell = hssfRow1.createCell(11);
        hssfCell.setCellValue(data.getDec());


        try {
            String path = getApplicationContext().getFilesDir().getPath();
            File file = new File(path, "thong-ke-giao-dich-theo-tai-khoan.xls");
            if(!file.exists()){
                file.createNewFile();
            }

            FileOutputStream output = new FileOutputStream(file);
            hssfWorkbook.write(output);

            if(output != null){
                output.flush();
                output.close();
            }

            hssfWorkbook.cloneSheet(0);
            hssfWorkbook.close();

            FancyToast.makeText(this,getString(R.string.export_success), FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,R.drawable.ic_check,true).show();


        } catch (Exception e) {
            alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
        }


    }
}