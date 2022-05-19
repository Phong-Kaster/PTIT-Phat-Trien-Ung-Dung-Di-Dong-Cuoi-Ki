package com.example.prudentialfinance.Activities.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.CategoryMonthly;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CategoryExportViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryExportActivity extends AppCompatActivity {

    private ImageButton btnMenu, backBtn;
    private AppCompatButton exportBtn;
    private PopupMenu popupMenu;
    private Spinner natureSpinner, columnSpinner;


    private CategoryExportViewModel viewModel;
    private LoadingDialog loadingDialog;
    private Alert alert;
    private Map<String, String> headers;

    private String typeCategory;
    private String nature;
    private String column;

    private TextView topTitle;
    private EditText numOfRow;
    private ArrayList<CategoryMonthly> data;

    private final HashMap<String, String> natureOptions = new HashMap<>();
    private final HashMap<String, String> columnOptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_export);

        setComponent();
        setControl();

        setEvent();

        loadData();
    }

    private void loadData() {
        typeCategory = "income";
        nature = "asc";
        column = "category";
        data = new ArrayList<>();
        natureOptions.put("A-Z", "asc");
        natureOptions.put("Z-A", "desc");


        columnOptions.put(getString(R.string.name), "name");
        columnOptions.put(getString(R.string.jan), "jan");
        columnOptions.put(getString(R.string.feb), "feb");
        columnOptions.put(getString(R.string.mar), "mar");
        columnOptions.put(getString(R.string.apr), "apr");
        columnOptions.put(getString(R.string.may), "may");
        columnOptions.put(getString(R.string.may), "may");
        columnOptions.put(getString(R.string.jul), "jul");
        columnOptions.put(getString(R.string.aug), "aug");
        columnOptions.put(getString(R.string.sep), "sep");
        columnOptions.put(getString(R.string.oct), "oct");
        columnOptions.put(getString(R.string.nov), "nov");
        columnOptions.put(getString(R.string.dec), "dec");
        columnOptions.put(getString(R.string.total), "total");



        initializeNatureSpinner();
        initializeColumnSpinner();
    }

    private void setComponent() {
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(CategoryExportViewModel.class);
    }

    private void setEvent() {
        btnMenu.setOnClickListener(view -> {
            popupMenu.show();
        });

        backBtn.setOnClickListener(view -> finish());

        exportBtn.setOnClickListener(view -> {
            int length = 0;
            try {
                length = Integer.parseInt(numOfRow.getText().toString());
            }catch (Exception ex){
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertNumber), R.drawable.ic_close);
                return;
            }

            if(length > 0){
                viewModel.getData(headers, typeCategory, nature, length, column);
            }else{
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertNumber), R.drawable.ic_close);
            }
        });

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.incomeMenu:
                    typeCategory = "income";
                    topTitle.setText(getString(R.string.report_category_income));
                    break;
                case R.id.expenseMenu:
                    typeCategory = "expense";
                    topTitle.setText(getString(R.string.report_category_expense));
                    break;
            }
            return true;
        });

        viewModel.getObject().observe(this, object -> {
            System.out.println(object);
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                data.clear();
                data.addAll(object.getData());
                createExcel();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });


        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

    }

    private void initializeNatureSpinner() {
        String[] spinnerOptions = { "A-Z", "Z-A"};

        SpinnerAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,spinnerOptions);

        natureSpinner.setAdapter(adapter);
        natureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*get nature which is selected from category spinner*/
                String key = spinnerOptions[i];
                nature = natureOptions.get(key);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializeColumnSpinner() {
        String[] spinnerOptions = {
                getString(R.string.name_col),
                getString(R.string.jan), getString(R.string.feb), getString(R.string.mar), getString(R.string.apr), getString(R.string.may), getString(R.string.jun),
                getString(R.string.jul), getString(R.string.aug), getString(R.string.sep), getString(R.string.oct), getString(R.string.nov), getString(R.string.dec),
                getString(R.string.total)};


        SpinnerAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,spinnerOptions);

        columnSpinner.setAdapter(adapter);
        columnSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*get nature which is selected from category spinner*/
                String key = spinnerOptions[i];
                column = columnOptions.get(key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setControl() {
        btnMenu = findViewById(R.id.btnMenu);
        backBtn = findViewById(R.id.backBtn);
        exportBtn = findViewById(R.id.exportBtn);
        topTitle = findViewById(R.id.topTitle);
        numOfRow = findViewById(R.id.number_of_row);

        natureSpinner = findViewById(R.id.sortByNatureSpinner);
        columnSpinner = findViewById(R.id.sortByColumnSpinner);

        popupMenu = new PopupMenu(this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.category_menu, popupMenu.getMenu());

    }

    public void createExcel()
    {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        ArrayList<String> headers = new ArrayList<>();
        headers.add(getString(R.string.category));
        headers.add(getString(R.string.jan));
        headers.add(getString(R.string.feb));
        headers.add(getString(R.string.mar));
        headers.add(getString(R.string.apr));
        headers.add(getString(R.string.may));
        headers.add(getString(R.string.may));
        headers.add(getString(R.string.jul));
        headers.add(getString(R.string.aug));
        headers.add(getString(R.string.sep));
        headers.add(getString(R.string.oct));
        headers.add(getString(R.string.nov));
        headers.add(getString(R.string.dec));
        headers.add(getString(R.string.total));

        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < headers.size(); i++){
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(headers.get(i));
        }

        for (int i = 0; i < data.size(); i++){
            HSSFRow hssfRow1 = hssfSheet.createRow(i+1);

            HSSFCell hssfCell = hssfRow1.createCell(0);
            hssfCell.setCellValue(data.get(i).getCategory());

            hssfCell = hssfRow1.createCell(1);
            hssfCell.setCellValue(data.get(i).getJan());

            hssfCell = hssfRow1.createCell(2);
            hssfCell.setCellValue(data.get(i).getFeb());

            hssfCell = hssfRow1.createCell(3);
            hssfCell.setCellValue(data.get(i).getMar());

            hssfCell = hssfRow1.createCell(4);
            hssfCell.setCellValue(data.get(i).getApr());

            hssfCell = hssfRow1.createCell(5);
            hssfCell.setCellValue(data.get(i).getMay());

            hssfCell = hssfRow1.createCell(6);
            hssfCell.setCellValue(data.get(i).getJun());

            hssfCell = hssfRow1.createCell(7);
            hssfCell.setCellValue(data.get(i).getJul());

            hssfCell = hssfRow1.createCell(8);
            hssfCell.setCellValue(data.get(i).getAug());

            hssfCell = hssfRow1.createCell(9);
            hssfCell.setCellValue(data.get(i).getSep());

            hssfCell = hssfRow1.createCell(10);
            hssfCell.setCellValue(data.get(i).getOct());

            hssfCell = hssfRow1.createCell(11);
            hssfCell.setCellValue(data.get(i).getNov());

            hssfCell = hssfRow1.createCell(12);
            hssfCell.setCellValue(data.get(i).getDec());

            hssfCell = hssfRow1.createCell(13);
            hssfCell.setCellValue(data.get(i).getTotal());
        }

        try {
            String path = getApplicationContext().getFilesDir().getPath();
            File file = new File(path, "thong-ke-thu-chi-the-loai-theo-thang.xls");
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