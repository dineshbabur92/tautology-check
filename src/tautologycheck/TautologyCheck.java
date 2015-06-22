/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tautologycheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

/**
 *
 * @author dineshbabu.rengasamy
 */
public class TautologyCheck {

    /**
     * @param args the command line arguments
     */
    private Stack<Character> operators;
    private Stack<Boolean> values;
    
    public TautologyCheck(){
        this.operators = new Stack<>();
        this.values = new Stack<>();
    }
    public boolean checkTautology(String voc, String st, int i){
        boolean isTautology;
        if(i<0){
            isTautology = evaluateStatement(st);
            System.out.println(st);
            return isTautology;
        }
        String temp = st.replaceAll(Character.toString(voc.charAt(i)),"0");
        isTautology = checkTautology(voc, temp, i-1);
        if(isTautology){
            temp = st.replaceAll(Character.toString(voc.charAt(i)),"1");
            isTautology = checkTautology(voc, temp, i-1);
        }
        return isTautology;  
    }
    
     private boolean evaluateStatement(String st) {
         System.out.println("statement :"+st);
         for(int i=0; i< st.length(); i++){
                char c = st.charAt(i);
                System.out.println("\tcharacter read:"+c);
                if(c=='0'){
                    this.values.push(Boolean.FALSE);
                    System.out.println("\tfalse pushed");
                }
                else if(c=='1'){
                    this.values.push(Boolean.TRUE);
                    System.out.println("\ttrue pushed");
                }
                else if(c=='('){
                    this.operators.push(c);
                    System.out.println("\t'(' pushed");
                }
                else if(c==')'){
                    while(!operators.empty() && operators.peek()!='('){
                        doOperation();  
                    }
                    if(operators.peek()=='('){
                        operators.pop();
                        System.out.println("\t'(' popped");
                    }
                }
                else if(c=='&' || c=='|' || c=='!'){
                        while(!operators.empty() && !(checkPrecedence(c, operators.peek()))){
                            doOperation();
                        }
                        operators.push(c);
                        System.out.println("\t"+ c + " pushed");
                }
                System.out.println("\t i:"+i+", values: "+values.toString()+", operators:"+operators.toString());
         }
         while(!operators.empty()){
             doOperation();
         }
         System.out.println("RETURN VALUE: "+values.peek());
        return values.pop();
    }
     public void doOperation(){
                char op = this.operators.peek();
                 if(!(op=='!')){
                    values.push(evaluateOperation(values.pop(), values.pop(), operators.pop()));
                 }
                else if(op=='!')
                    values.push(evaluateOperation(true, values.pop(), operators.pop()));
     }
     public boolean checkPrecedence(char c, char op){
         if(op=='(')
             return true;
         if(c==op)
             return false;
         else if(c=='!')
             return true;
         else if(c=='&' && op=='|')
             return true;
        return false;   
     }
     public boolean evaluateOperation(boolean value1, boolean value2, char op){
         if(op=='|'){
             System.out.println("\tevaluating: "+value1+" "+op+" "+value2+" : " + (value2 || value1));
             return value2 || value1;
         }
         else if(op=='&'){
             System.out.println("\tevaluating: "+value1+" "+op+" "+value2+" : " + (value2 && value1));
             return value2 && value1;
         }
         else if(op=='!'){
             System.out.println("\tevaluating: "+op+" "+value2+" : " + !value2);
             return !value2;
         }
         return false;    
     }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder st_buf = new StringBuilder();
        StringBuilder voc_buf = new StringBuilder();
        int value;
        while((value = br.read())!= 10){ //stop when enter pressed
            char c = (char)value;
            //System.out.print(c + Integer.toString(value));
            if(c==' ')
                continue;
            if((value>=65 && value<=90) || (value>=97 && value<=122)){
                voc_buf.append(c);
               // System.out.print(c);
            }
            st_buf.append(c);
            
        }
        System.out.println(voc_buf);
        System.out.println(st_buf);
        
        TautologyCheck tc = new TautologyCheck();
        System.out.println("isTautolgy: " + Boolean.toString(tc.checkTautology(voc_buf.toString(), st_buf.toString(), (voc_buf.length())-1)));
        
        
    }

   
    
}
