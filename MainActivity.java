package com.ajsilva.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    public final String EXPRESSION = "current expression";
    public CharSequence mCharSeq;
    public int base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCharSeq = "";
        base = 16;
    }

    @Override
    protected void onStart()    {
        super.onStart();
    }

    @Override
    protected void onResume()    {
        super.onResume();
    }

    @Override
    protected void onPause()    {
        super.onPause();
    }

    @Override
    protected void onStop()    {
        super.onStop();
    }

    @Override
    protected void onDestroy()    {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(EXPRESSION, mCharSeq);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mCharSeq = savedInstanceState.getCharSequence(EXPRESSION);
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(mCharSeq);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
*/
    public void clickButton(View view)    {
        //View v = (View) findViewById(R.id.Base);
        View v = view;
        TextView tv = (TextView) findViewById(R.id.textView);

        CharSequence string = tv.getText();
        int sLen = string.length();
        char last = '\0';
        if (sLen > 0) {
            last = string.charAt(string.length() - 1);
        }

        switch ( v.getId() )
        {
            case R.id.buttonZero:
                if (tv.getText() != "0")
                    tv.append("0");
                break;
            case R.id.button1: tv.append("1");
                break;
            case R.id.button2: tv.append("2");
                break;
            case R.id.button3: tv.append("3");
                break;
            case R.id.button4: tv.append("4");
                break;
            case R.id.button5: tv.append("5");
                break;
            case R.id.button6: tv.append("6");
                break;
            case R.id.button7: tv.append("7");
                break;
            case R.id.button8: tv.append("8");
                break;
            case R.id.button9: tv.append("9");
                break;
            case R.id.buttonA: tv.append("a");
                break;
            case R.id.buttonB: tv.append("b");
                break;
            case R.id.buttonC: tv.append("c");
                break;
            case R.id.buttonD: tv.append("d");
                break;
            case R.id.buttonE: tv.append("e");
                break;
            case R.id.buttonF: tv.append("f");
                break;
            case R.id.buttonMode:
                if (sLen > 1)
                    tv.setText( string.subSequence(0, string.length()-1) );
                else
                    tv.setText("");
                break;
            case R.id.buttonAdd:
                if (isOperand(last)) {
                    tv.append("+");
                }
                else if (sLen > 1){
                    tv.setText(string.subSequence(0, string.length() - 1));
                    tv.append("+");
                }
                break;
            case R.id.buttonSub:
                if (isOperand(last)) {
                    tv.append("-");
                }else if (sLen > 1){
                    tv.setText(string.subSequence(0, string.length() - 1));
                    tv.append("-");
                }
                break;
            case R.id.buttonMul:
                if (isOperand(last)) {
                    tv.append("*");
                }
                else if (sLen > 1){
                    tv.setText(string.subSequence(0, string.length() - 1));
                    tv.append("*");
                }
                break;
            case R.id.buttonDiv:
                if (isOperand(last)) {
                    tv.append("/");
                }
                else if (sLen > 1){
                    tv.setText(string.subSequence(0, string.length() - 1));
                    tv.append("/");
                }
                break;
            case R.id.buttonMod:
                if (isOperand(last)) {
                    tv.append("%");
                }
                else if (sLen > 1){
                    tv.setText(string.subSequence(0, string.length() - 1));
                    tv.append("%");
                }
                break;
            case R.id.buttonEquals:
                tv.setText(parse(string));
                break;
            default: tv.append("~");

        }

        mCharSeq = tv.getText();
    }

    public boolean isOperand(char c)    {
        if ( (int) c >= 48 && (int) c <= 57)
            return true;
        if ( (int) c >= 97 && (int) c <= 102)
            return true;
        return false;
    }

    public boolean isOperator(char c)    {
        if (c == '+' || c == '-' || c == '/' || c == '*' || c == '%')
            return true;
        return false;
    }

    public CharSequence parse(CharSequence exp)    {

        Pattern pat = Pattern.compile("[0-9a-fA-F]+");
        Pattern pat2 = Pattern.compile("[-/*+%]");
        Matcher mat = pat.matcher(exp);

        int numNds = 0;
        int numtors = 0;

        Stack<BigInteger> operands = new Stack<BigInteger>();
        String operators = "";

        while ( mat.find() ) {
            operands.push(new BigInteger(exp.subSequence(mat.start(), mat.end()).toString(), base));
        }
        mat = pat2.matcher(exp);
        while ( mat.find() ) {
            operators += exp.charAt(mat.start());
        }

        while (operands.size() > 1 && operators != "") {
            BigInteger right = operands.pop();
            BigInteger left = operands.pop();
            switch( operators.charAt( operators.length()-1 ) ) {
                case '+':
                    operands.push(left.add(right));
                    break;
                case '-':
                    operands.push(left.subtract(right));
                    break;
                case '*':
                    operands.push(left.multiply(right));
                    break;
                case '/':
                    operands.push(left.divide(right));
                    break;
                case '%':
                    operands.push(left.remainder(right));
                    break;

            }
        }

        if (operands.size() != 1)
            exp = "Error";
        else {
            exp = operands.pop().toString(16);
        }
        return exp;
    }

    public int hexToDecimal(StringBuilder sb) {
        int result = 0, curInt;
        char cur;
        int sum = 0;
        for (int i = sb.length() - 1, hexCount = 0; i >= 0; i--, hexCount++) {
            cur = sb.charAt(i);
            if (cur >= '0' && cur <= '9') {
                sum += Math.pow(16.0d, (double) hexCount) * (cur-48);
            }
        }
        return -1;
    }

}

