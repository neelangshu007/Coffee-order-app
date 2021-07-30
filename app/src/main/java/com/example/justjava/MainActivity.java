package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;


/* This app displays an order form to order coffee */

public class MainActivity extends AppCompatActivity {

    int numberOfCoffees = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* This method is called when the order button is clicked
    *   this method create an order summary and also called an Intent */
    public void submitOrder(View view) {
//        displayPrice(numberOfCoffees*5);
        CheckBox WhippedCreamCheckBoxView = (CheckBox) findViewById(R.id.whipped_check_box);
        boolean hasWhippedCream = WhippedCreamCheckBoxView.isChecked();
        CheckBox chocolateCheckBoxView = (CheckBox) findViewById(R.id.chocolate_check_box);
        boolean hasChocolate = chocolateCheckBoxView.isChecked();
//        Log.v("MainActivity", "checkbox:"+ hasWhippedCream);
        EditText nameEditTextView = (EditText) findViewById(R.id.name_text);
        String customerName = nameEditTextView.getText().toString();    // it is returning Editable object and the Editable class has a method call tostring()
        int price = calculatePrice(5, hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, customerName);

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:neelangshunath@gmail.com"));
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, customerName));
        email.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        }
        else{
            Toast.makeText(this, getString(R.string.problem), Toast.LENGTH_SHORT).show();
        }

        displayMessage(priceMessage);
    }

    /* This method is called when the + button is clicked and when the number of coffees becomes more than 100 display a toast message */
    public void incrementCoffees(View view) {
        if (numberOfCoffees < 100) {
            numberOfCoffees += 1;
            displayQuantity(numberOfCoffees);
        } else {
            Toast.makeText(this, getString(R.string.not_more_100), Toast.LENGTH_SHORT).show();
        }
    }

    /* This method is called when the - button is clicked and when the number of coffees becomes less than 1 display a toast message*/
    public void decrementCoffees(View view) {
        if (numberOfCoffees > 1) {
            numberOfCoffees -= 1;
            displayQuantity(numberOfCoffees);
        } else {
            Toast.makeText(this, getString(R.string.not_less_1), Toast.LENGTH_SHORT).show();
        }
    }
     /*This method is used to calculate the total price of coffees according to the total number of cups of coffees
     @param pricePerCup is the price of one cup of coffee
     @addWhippedCream Take a boolean value True or False according to the checked condition of the WhippedCream check box and is also used in the if condition
     @addChocolate Take a boolean value True or False according to the checked condition of the Chocolate check box and is also used in the if condition
     */
    private int calculatePrice(int pricePerCup, boolean addWhippedCream, boolean addChocolate) {
        if (addWhippedCream) {
            pricePerCup += 1;
        }

        if (addChocolate) {
            pricePerCup += 2;
        }

        int price = numberOfCoffees * pricePerCup;
        return price;
    }

    //This method is used to create the order summary according to the input param
    /*@TotalPrice :-Takes Total price of the coffees purchased (integer)
    * @ addWhippedCream :- Take a boolean value True or False according to the checked condition of the WhippedCream check box
    * @ addChocolate:- Take a boolean value True or False according to the checked condition of the Chocolate check box
    * @*name:- Take a string , it contain the name of the customer who is ordering the coffees*/
    private String createOrderSummary(int TotalPrice, boolean addWhippedCream, boolean addChocolate, String name) {
        String summary = getString(R.string.order_summary_name, name) + getString(R.string.order_summary_whipped_cream, addWhippedCream)+ getString(R.string.order_summary_chocolate, addChocolate) + getString(R.string.order_summary_quantity, numberOfCoffees) + getString(R.string.order_summary_price, TotalPrice) + getString(R.string.thank_you);
        return summary;
    }


    /* This method displays the given quantity of coffees on the screen
        @ number:- It take the  number of Coffees at present time*/
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /* This method displays the given price on the screen.*/
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(number));
    }

     /* This method is used to display the order summary on the screen*
     @message:- it take string of order summary*/
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

}