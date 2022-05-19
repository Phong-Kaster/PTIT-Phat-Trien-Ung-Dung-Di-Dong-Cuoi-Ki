package com.example.prudentialfinance.Activities.Transaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.TransactionViewModel;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionStatementActivity extends AppCompatActivity {

    private EditText fromDate;
    private EditText toDate;
    private EditText search;
    private EditText length;

    private final Calendar myCalendar= Calendar.getInstance();
    private static Map<String, String > headers = null;

    private Spinner natureSpinner, columnSpinner;
    private AppCompatButton buttonCreate, buttonPreview;

    private String dateFrom;//  fromdate
    private String dateTo;//    todate
    private String keyword;//   search
    private String quantity;//  length
    private String nature;//    order[dir]
    private String column;//    order[column]

    private final HashMap<String, String> natureOptions = new HashMap<>();
    private final HashMap<String, String> columnOptions = new HashMap<>();
    private ImageButton buttonGoBack;
    private TransactionViewModel transactionViewModel = null;
    private Alert alert;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_statement);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*Step 1*/
        headers = ((GlobalVariable)getApplication()).getHeaders();

        /*Step 2*/
        setComponent();
        setViewModel();
        setDefaultOption();

        /*Step 3*/
        setEvent();
    }

    private void setViewModel() {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        transactionViewModel.getAnimation().observe(this, aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });
    }


    /**
     * @author Phong-Kaster
     * set default options for 2 spinner: natureSpinner & columnSpinner
     * Step 1: for nature spinner
     * Step 2: for column spiner
     * */
    private void setDefaultOption() {
        /*Step 1*/
        natureOptions.put("Từ bé tới lớn", "asc");
        natureOptions.put("Từ lớn tới bé", "desc");


        /*Step 2*/
        columnOptions.put("ID","id");
        columnOptions.put("Tên","name");
        columnOptions.put("Số tiền","amount");
        columnOptions.put("Tham chiếu","reference");
        columnOptions.put("Ngày", "transactiondate");


        /*Step 3*/
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.CHINESE);
        Date date = new Date();


        /*Step 4*/
        String value = dateFormat.format(date);
        fromDate.setText(value);
        toDate.setText(value);
    }

    /**
     * @author Phong-Kaster
     * */
    private void setComponent() {
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);

        search = findViewById(R.id.search);
        length = findViewById(R.id.length);

        natureSpinner = findViewById(R.id.sortByNatureSpinner);
        columnSpinner = findViewById(R.id.sortByColumnSpinner);

        buttonCreate = findViewById(R.id.buttonCreateStatement);
        buttonPreview = findViewById(R.id.buttonPreviewStatement);
        buttonGoBack = findViewById(R.id.buttonGoBack);
        alert = new Alert(this, 1);
        loadingDialog = new LoadingDialog(this);
    }


    /**
     * @author Phong-Kaster
     * listen event from componets
     * */
    private void setEvent()
    {
        initializeFromDatePicker();
        initializeToDatePicker();


        initializeNatureSpinner();
        initializeColumnSpinner();

        buttonGoBack.setOnClickListener(view -> finish());

        buttonCreate.setOnClickListener(view->{
            dateFrom = Helper.convertStringToValidDate( fromDate.getText().toString() );
            dateTo = Helper.convertStringToValidDate(   toDate.getText().toString() );
            keyword = search.getText().toString().trim();
            quantity = length.getText().toString();

            /*nature & column is gotten from natureSpinner & columnSpinner*/
            createStatementWithBodyRequest(dateFrom, dateTo, keyword, quantity, nature, column);
        });

        buttonPreview.setOnClickListener(view->{
            dateFrom = Helper.convertStringToValidDate( fromDate.getText().toString() );
            dateTo = Helper.convertStringToValidDate(   toDate.getText().toString() );
            keyword = search.getText().toString().trim();
            quantity = length.getText().toString();

            Intent intent = new Intent(TransactionStatementActivity.this, TransactionPreviewStatementActivity.class);
            intent.putExtra("dateFrom", dateFrom);
            intent.putExtra("dateTo",dateTo);
            intent.putExtra("length", quantity);
            intent.putExtra("keyword", keyword);
            intent.putExtra("quantity", quantity);
            intent.putExtra("column", column);
            intent.putExtra("nature", nature);
            startActivity(intent);
        });
    }

    /**
     * @author Phong-Kaster
     * send a HTTP request to server to retrieve transactions which is filtered
     * Step 1: create body which is attached with headers
     * Step 2: query to server
     * */
    private void createStatementWithBodyRequest(String dateFrom, String dateTo, String keyword, String quantity, String nature, String column) {
        /*Step 1*/
        Map<String, String> body = new HashMap<>();
        body.put("fromdate", dateFrom);
        body.put("todate", dateTo);
        body.put("length", quantity);
        body.put("order[column]",column);
        body.put("order[dir]",nature);
        body.put("search",keyword);

        /*Step 2*/
        transactionViewModel.createStatement(headers, body);

        /*Step 3*/
        transactionViewModel.getTransactionCreationStatement().observe(this, homeLatestTransactions -> {
            int result = homeLatestTransactions.getResult();
            List<TransactionDetail> data = homeLatestTransactions.getData();
            if( result == 1)
            {
                try {
                    createAndExportPDF(data, dateFrom, dateTo);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            else
            {
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
            }
        });
    }

    /**
     * @author Phong-Kaster
     * create PDF file with itext 7 library
     *
     * open View->Tool Windows->Device File Explorer->com.example.prudential->files->transaction-statement.pdf
     *
     * Step 1: create path to know where to store file
     * Step 2: declare neccessary variable
     * */
    private void createAndExportPDF(List<TransactionDetail> data, String dateFrom, String dateTo) throws FileNotFoundException {

        User AuthUser = ((GlobalVariable)getApplication()).getAuthUser();
        /*Step 1*/
//        String path = getApplicationContext().getFilesDir().getPath();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        System.out.println("Path: " + path);
        File file = new File(path, "transactionStatement.pdf");


        /*Step 2*/
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument, PageSize.A4);


        /*Step 3*/
        float dimension[] = { 1, 3, 2 ,3, 2, 5, 2};// # - date - ID - reference - name - description - amount
        Table table = new Table(UnitValue.createPercentArray(dimension));


        /*Step 4 - HEADERS*/
        Paragraph header1 = new Paragraph("Vietnam central bank");
        Paragraph header2 = new Paragraph("Branch: PRUDENTIAL BANK");
        Paragraph header3 = new Paragraph("Account holder: " + AuthUser.getFirstname() + " " + AuthUser.getLastname());
        Paragraph header4 = new Paragraph("Account name: " + AuthUser.getFirstname() + " " + AuthUser.getLastname());
        Paragraph header5 = new Paragraph(String.format("Date: " + new Date(), "dd-MM-yyyy"));
        Paragraph header6 = new Paragraph("E-mail: " + AuthUser.getEmail());
        Paragraph header7 = new Paragraph("From: " + Helper.revertStringToReadableDate(dateFrom));
        Paragraph header8 = new Paragraph("To: " + Helper.revertStringToReadableDate(dateTo));

        Cell cell = new Cell(1, 10)
                .add(header1)
                .add(header2)
                .add(header3)
                .add(header4)
                .add(header5)
                .add(header6)
                .add(header7)
                .add(header8)
                .setFontSize(14)
                .setBorder(Border.NO_BORDER)
                .setBold()
                .setFontColor(DeviceGray.BLACK)
                .setBackgroundColor(DeviceGray.WHITE)
                .setTextAlignment(TextAlignment.LEFT);

        table.addHeaderCell(cell);


        Paragraph headerContent = new Paragraph("ACCOUNT STATEMENT");
        Cell cellTitle = new Cell(1, 10)
                .add(headerContent)
                .setBold()
                .setFontSize(24)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(DeviceGray.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cellTitle);


        /*Step 5*/
        for (int i = 0; i < 2; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("#")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("Date")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("ID")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("Reference")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("Name")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("Description")),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(1f)).add(new Paragraph("Credit"))
            };

            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    table.addHeaderCell(hfCell);
                } else {
                    table.addFooterCell(hfCell);
                }
            }
        }


        for (int i = 0; i < data.size() ; i++) {

            String date = Helper.revertStringToReadableDate(data.get(i).getTransactiondate());
            String id = data.get(i).getId().toString();
            String reference = data.get(i).getReference();
            String name = data.get(i).getName();
            String description = data.get(i).getDescription();
            String credit = data.get(i).getAmount().toString();

            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(i + 1))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(date)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(id)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(reference)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(name)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(description)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(credit)));
        }

        /*Step 6*/

        Paragraph footer1 = new Paragraph("\nThank you for using Prudential Bank's service\n*************************\n");
        Paragraph footer2 = new Paragraph("\nPRUDENTIAL BANK - Together for the future");
        Paragraph footer3 = new Paragraph("\nNote: This statement does not create any Prudential Bank's commitments or guarantee in the present or future" +
                "regarding customer's obligation with the third party");

        Cell footerCell1 = new Cell(1, 10)
                .add(footer1)
                .setFontSize(12)
                .setBorder(Border.NO_BORDER)
                .setItalic()
                .setBold()
                .setFontColor(DeviceGray.BLACK)
                .setBackgroundColor(DeviceGray.WHITE)
                .setTextAlignment(TextAlignment.CENTER);

        Cell footerCell2 = new Cell(1, 10)
                .add(footer2)
                .setFontSize(14)
                .setBorder(Border.NO_BORDER)
                .setItalic()
                .setBold()
                .setFontColor(DeviceGray.BLACK)
                .setBackgroundColor(DeviceGray.WHITE)
                .setTextAlignment(TextAlignment.CENTER);

        Cell footerCell3 = new Cell(1, 10)
                .add(footer3)
                .setFontSize(12)
                .setBorder(Border.NO_BORDER)
                .setItalic()
                .setFontColor(DeviceGray.BLACK)
                .setBackgroundColor(DeviceGray.WHITE)
                .setTextAlignment(TextAlignment.LEFT);

        table.addFooterCell(footerCell1);
        table.addFooterCell(footerCell2);
        table.addFooterCell(footerCell3);


        /*Step 7*/
        try {
            document.add(table);
            document.close();
            Toast.makeText(this,"File được lưu tại " + path.toString(), Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            System.out.println("PDF Error: " + ex.getMessage());
        }


    }

    /**
     * @author Phong-Kaster
     * create sort ascending or descending options
     * */
    private void initializeNatureSpinner() {
        String[] spinnerOptions = { "Từ bé tới lớn", "Từ lớn tới bé"};


        SpinnerAdapter adapter = new ArrayAdapter(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,spinnerOptions);

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

    /**
     * @author Phong-Kaster
     * create column spinner
     * */
    private void initializeColumnSpinner() {
        String[] spinnerOptions = { "ID", "Tên", "Số tiền", "Tham chiếu", "Ngày"};


        SpinnerAdapter adapter = new ArrayAdapter(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,spinnerOptions);

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

    /**
     * @author Phong-Kaster
     * initialize DatePicker
     * */
    private void initializeFromDatePicker()
    {
        DatePickerDialog.OnDateSetListener datePicker = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);


            /*set text to date*/
            String format = "dd/MM/yyyy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.CHINESE);
            fromDate.setText(dateFormat.format(myCalendar.getTime()));
        };

        fromDate.setOnClickListener(view-> new DatePickerDialog(TransactionStatementActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }

    /**
     * @author Phong-Kaster
     * initialize DatePicker
     * */
    private void initializeToDatePicker()
    {
        DatePickerDialog.OnDateSetListener datePicker = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);


            /*set text to date*/
            String format = "dd/MM/yyyy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.CHINESE);
            toDate.setText(dateFormat.format(myCalendar.getTime()));
        };

        toDate.setOnClickListener(view-> new DatePickerDialog(TransactionStatementActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }
}