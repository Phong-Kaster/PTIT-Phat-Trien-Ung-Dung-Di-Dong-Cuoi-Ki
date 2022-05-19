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
import com.example.prudentialfinance.Model.AccountBalance;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Accounts.AccountReportViewModel;
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

public class AccountReportActivity extends AppCompatActivity {

    AccountReportViewModel viewModel;
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
    ArrayList<AccountBalance> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_report);

        setComponent();

        setControl();
        setEvent();

        loadDataChart();


        viewModel.getData(headers);

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(AccountReportViewModel.class);
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
            viewModel.getData(headers);
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
        data = new ArrayList<>();
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new ValueDataEntry("", 0));

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
                data.clear();
                data.addAll(object.getData());
                List<DataEntry> list = new ArrayList<>();
                for (AccountBalance item : object.getData()) {
                    list.add(new ValueDataEntry(item.getName(), item.getBalance()));
                }
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
        headers.add("Tên");
        headers.add("Số dư");
        headers.add("Chi tiêu");
        headers.add("Thu nhập");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < headers.size(); i++){
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(headers.get(i));
        }

        for (int i = 0; i < data.size(); i++){
            HSSFRow hssfRow1 = hssfSheet.createRow(i+1);

            HSSFCell hssfCell = hssfRow1.createCell(0);
            hssfCell.setCellValue(data.get(i).getName());

            HSSFCell hssfCell1 = hssfRow1.createCell(1);
            hssfCell1.setCellValue(data.get(i).getBalance());

            HSSFCell hssfCell2 = hssfRow1.createCell(2);
            hssfCell2.setCellValue(data.get(i).getExpense());

            HSSFCell hssfCell3 = hssfRow1.createCell(3);
            hssfCell3.setCellValue(data.get(i).getIncome());
        }

        try {
            String path = getApplicationContext().getFilesDir().getPath();
            File file = new File(path, "thong-ke-so-du.xls");
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