import java.util.ArrayList;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P1{

        int mini=0;
        @SuppressWarnings("rawtypes")
        ArrayList<ArrayList> list1=new ArrayList<ArrayList>();

        @SuppressWarnings("rawtypes")
        public static void main(String[] args) throws IOException{
                P1 obj=new P1();

                BufferedReader in = new BufferedReader(
                                 new InputStreamReader(System.in));

                String source;// in.readLine( );
                String dest;// in.readLine( );
                String third;

                if((source=in.readLine())==null && (dest=in.readLine())==null){
                                 System.out.println("invalid input");
                                 System.exit(0);
                }

                if((third=in.readLine())!=null){
                                 System.out.println("invalid input");
                                 System.exit(0);
                }

                P1.error_handling(source);
                P1.error_handling(dest);

                int m=source.length();
                int n=dest.length();
                int arr[][]=new int[m+1][n+1];

                P1.matrix(source,dest,arr,m,n);
                ArrayList<ArrayList> B=obj.recurse(arr,m,n);
                P1.getSequences(B,source,dest,arr);
        }
        
        //error handling        
        private static void error_handling(String expression) {
                char[] tokens = expression.toCharArray();
                 for (int i = 0; i < tokens.length; i++) {
                         if(!Character.isAlphabetic(tokens[i])) {
                                 System.out.println("invalid input");
                                 System.exit(0);
                         }
                 }
        }

        //Divide the list into Levenshtein sequences 
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private static void getSequences(ArrayList<ArrayList> B, String source, String dest, int[][] arr){
                Stack<ArrayList> stk = new Stack<ArrayList>();
                int counter = 0;
                int seqNo=0;
                int number=0;
                int location =0;

                for(ArrayList a: B) {
                        counter += 1;
                        if(Integer.parseInt(a.get(0).toString())==0 && Integer.parseInt(a.get(1).toString())==0) {
                                number=number+parse_func(source,dest,stk,seqNo,arr);
                                Stack<ArrayList> stk2 = new Stack<ArrayList>();
                                for(ArrayList a1: stk) {
                                        stk2.add(a1);
                                }
                                while(!stk.isEmpty()) {
                                        stk.pop();
                                }

                                if(counter>=B.size()){
                                        break;
                                }
                                ArrayList<Integer> next = B.get(counter);
                                if(Integer.parseInt(next.get(0).toString())==source.length() && Integer.parseInt(next.get(1).toString())==dest.length()) {
                                        while(!stk.isEmpty()) {
                                                stk.pop();
                                        }
                                }
                                else {
                                                        int h=location;
                                                        ArrayList<Integer> compa = stk2.get(h);
                                                        while(compa.get(1)!=next.get(1) || compa.get(0)!=next.get(0)) {
                                                                stk.push(compa);
                                                                h++;
                                                                compa = stk2.get(h);
                                                        }
                                }

                        }
                        else {
                                stk.push(a);
                        }
                }
                System.out.println("\nThere are total of "+number+" sequences");
        }


        //Generate matrix to get edit distances
        private static void matrix(String source, String dest, int[][] arr, int m, int n) {

                for(int i=0;i<=m;i++) {
                        arr[i][0]=i;
                }
                for(int j=0;j<=n;j++) {
                        arr[0][j]=j;
                }
                String source1=source.toLowerCase();
                String dest1=dest.toLowerCase();
                for(int i=1;i<=m;i++) {
                        for(int j=1;j<=n;j++) {
                                if(source1.charAt(i-1)==dest1.charAt(j-1)) {
                                        arr[i][j]=Math.min(Math.min(arr[i-1][j]+1,arr[i][j-1]+1),arr[i-1][j-1]);
                                }
                                else{
                                        arr[i][j]=Math.min(Math.min(arr[i-1][j]+1,arr[i][j-1]+1),arr[i-1][j-1]+1);
                                }
                        }
                }
        }


        //Parse source string according to the sequence to replace/insert/delete
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private static int parse_func(String source, String dest, Stack<ArrayList> stk, int num, int[][] arr) {

                int max=arr[arr.length-1][arr[0].length-1];
                Stack<ArrayList> newstk = new Stack<ArrayList>();

                ArrayList<Integer> temp = stk.peek();
                if(temp.get(0)==0 && temp.get(1)==0) {
                        return 0;
                }

                ArrayList<Integer> zero=new ArrayList<Integer>();
                zero.add(0);
                zero.add(0);
                stk.push(zero);

                newstk.push(stk.peek());

                String text = source;

                StringBuilder sb1 = new StringBuilder(text);
                String op;
                int opNum=0;

                        while(stk.size()>1) {
                                ArrayList<Integer> temp1 = stk.pop();
                                ArrayList<Integer> temp2 = stk.pop();
                                int x = temp2.get(0) - temp1.get(0);
                                int y = temp2.get(1) - temp1.get(1);
                                        //Insert
                                        if(x==0) {

                                                StringBuilder sb = new StringBuilder(text);
                                                int pos=temp2.get(1);
                                                if(pos>sb.length()) {
                                                        sb.append(dest.charAt(temp2.get(1)-1));
                                                }
                                                else {
                                                        sb.insert(pos-1, dest.charAt(temp2.get(1)-1));
                                                }
                                                text=sb.toString();
                                                op="  Insert "+dest.charAt(temp2.get(1)-1)+"-> ";
                                                opNum++;
                                                sb1.append(op);
                                                sb1.append(text);
                                        }
                                        
                                        //Case for replace or equal character
                                        else if(x==1 && y==1) {

                                                char t1=source.toLowerCase().charAt(temp2.get(0)-1);
                                                char t2=dest.toLowerCase().charAt(temp2.get(1)-1);
                                                if(t1==t2) {
                                                        stk.push(temp2);
                                                        newstk.push(temp2);
                                                        continue;
                                                }
                                                else if(arr[temp2.get(0)][temp2.get(1)] - arr[temp1.get(0)][temp1.get(1)] == 1) {
                                                        int charcount=0;
                                                        for( int i=0; i<source.length(); i++ ) {
                                                            if( source.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                        charcount++;
                                                            }
                                                        }
                                                        int relative=0;
                                                        for( int i=0; i<temp2.get(0); i++ ) {
                                                            if( source.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                        relative++;
                                                            }
                                                        }
                                                        int same=0;
                                                        for( int i=0; i<text.length(); i++ ) {
                                                            if( text.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                        same++;
                                                                        if(same==relative) {
                                                                                text=text.substring(0, i) + dest.charAt(temp2.get(1)-1) +text.substring(i+1);
                                                                        }
                                                            }
                                                        }
                                                        op="  Replace "+source.charAt(temp2.get(0)-1)+" by "+dest.charAt(temp2.get(1)-1)+"-> ";
                                                        opNum++;
                                                        sb1.append(op);
                                                        sb1.append(text);
                                                }

                                        }
                                        //Delete
                                        else if(x==1 && y==0) {

                                                int charcount=0;
                                                for( int i=0; i<source.length(); i++ ) {
                                                    if( source.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                charcount++;
                                                    }
                                                }
                                                int relative=0;
                                                for( int i=0; i<temp2.get(0); i++ ) {
                                                    if( source.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                relative++;
                                                    }
                                                }
                                                int same=0;
                                                for( int i=0; i<text.length(); i++ ) {
                                                    if( text.charAt(i) == source.charAt(temp2.get(0)-1) ) {
                                                                same++;
                                                                if(same==relative) {
                                                                        text=text.substring(0, i) +text.substring(i+1);
                                                                }
                                                    }
                                                }
                                                op="  Delete "+source.charAt(temp2.get(0)-1)+"-> ";
                                                opNum++;
                                                sb1.append(op);
                                                sb1.append(text);
                                        }
                                stk.push(temp2);
                                newstk.push(temp2);
                                }
                        if(!stk.isEmpty()) {
                                stk.pop();
                        }
                        while(!newstk.isEmpty()) {
                                stk.push(newstk.pop());
                        }
                        if(max==opNum) {
                                System.out.println(sb1);
                                num++;
                        }
                        return num;
                }


        //Backtracking to get list of possible Levenshtein sequences
        @SuppressWarnings("rawtypes")
        private ArrayList<ArrayList> recurse(int[][] arr, int i, int j) {
                if (i==0 && j==0) {
                        ArrayList<Integer> arr1 = new ArrayList<Integer>();
                        arr1.add(i);
                        arr1.add(j);
                        list1.add(arr1);
                        return list1;
                }
                int mini=0;
                if(i>0 && j>0) {
                        mini=Math.min(Math.min(arr[i-1][j],arr[i][j-1]),arr[i-1][j-1]);
                }
                else if(i>0) {
                        mini=arr[i-1][j];
                }
                else if(j>0) {
                        mini=arr[i][j-1];
                }

                if(i-1>=0 && arr[i-1][j]==mini) {
                        ArrayList<Integer> arr1 = new ArrayList<Integer>();
                        arr1.add(i);
                        arr1.add(j);
                        list1.add(arr1);
                        recurse(arr,i-1,j);
                }
                if(i-1>=0 && j-1>=0 && arr[i-1][j-1]==mini) {
                        ArrayList<Integer> arr1 = new ArrayList<Integer>();
                        arr1.add(i);
                        arr1.add(j);
                        list1.add(arr1);
                        recurse(arr,i-1,j-1);
                }
                if(j-1>=0 && arr[i][j-1]==mini) {
                        ArrayList<Integer> arr1 = new ArrayList<Integer>();
                        arr1.add(i);
                        arr1.add(j);
                        list1.add(arr1);
                        recurse(arr,i,j-1);
                }

                return list1;
        }
}
                                                        
