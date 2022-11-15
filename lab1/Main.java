import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        fileGeneration();
        while (!fileIsSorted("A.txt")){
            sorting();
            clearFile("A.txt");
            unification("A.txt","B.txt","C.txt");
            clearFile("C.txt");
            clearFile("B.txt");
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

    }
    public static boolean fileIsSorted(String fileName){
        long[] previous = readFile(fileName,0);
        while (previous[0] != -1 ){
            long[] current = readFile(fileName, previous[1]);
            if(current[0] != -1) {
                if(IntegerComparatror.compare((int)(previous[0]), (int)current[0])) previous = current;
                else return false;

            }else return true;
        }
        return true;
    }
    public static void clearFile(String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        }catch (IOException exception){
            System.out.println(exception.toString());
        }
    }
    public static void unification(String fileA,String fileB,String fileC){
        long numBytesB = 0;
        long numBytesC = 0;
        File A = new File(fileA);
        File B = new File(fileB);
        File C = new File(fileC);
        Integer a = null;
        while (A.length() < B.length()+C.length()){
            long[] b = readFile(fileB, numBytesB);
            long[] c = readFile(fileC, numBytesC);
            if(b[0] == -1)  {
                while (c[0] != -1){
                    writeFile("A.txt", (int)c[0]);
                    numBytesC = c[1];
                    c = readFile(fileC, numBytesC);
                }
            } else if (c[0] == -1) {
                while (b[0] != -1){
                    writeFile("A.txt", (int)b[0]);
                    numBytesB = b[1];
                    b = readFile(fileB, numBytesB);

                }
            }
            else if(a == null ||( a <= Objects.requireNonNull(b)[0] && a <= Objects.requireNonNull(c)[0]) ||
                    ( a >= Objects.requireNonNull(b)[0] && a >= Objects.requireNonNull(c)[0])){
                if(b[0] < c[0]){
                    writeFile(fileA,a = (int)b[0]);
                    numBytesB = b[1];
                }else{
                    writeFile(fileA,a = (int)c[0]);
                    numBytesC = c[1];
                }
            }else if(a < b[0]){
                writeFile(fileA,a = (int)b[0]);
                numBytesB = b[1];
            }else {
                writeFile(fileA,a = (int)c[0]);
                numBytesC = c[1];
            }
        }
    }
    public static boolean isSorted(ArrayList<Integer> list){
        if(list.size() <= 1) return true;
        Iterator<Integer> iter = list.iterator();
        Integer current, previous = iter.next();
        while (iter.hasNext()) {
            current =  iter.next();
            if (!IntegerComparatror.compare(previous, current)) {
                return false;
            }
            previous = current;
        }
        return true;
    }
    public static void sorting(){
        ArrayList<Integer> fileBArray = new ArrayList<>();
        ArrayList<Integer> fileCArray = new ArrayList<>();
        boolean fileB = true;
        long count = 0;
        while(true){
           long[] arr =  readFile("A.txt",count);
           if(arr[0] == -1) {
               for(int item: fileBArray) writeFile("B.txt",item);
               for(int item: fileCArray) writeFile("C.txt",item);
               break;
           }
           count = arr[1];
           long a = arr[0];
           if(fileB){
               fileBArray.add((int)a);
               if(!isSorted(fileBArray)){
                   fileB = false;
                   fileCArray.add(fileBArray.remove(fileBArray.size()-1));
                   for(int item: fileBArray) writeFile("B.txt",item);
                   fileBArray.clear();
               }
           }else{
               fileCArray.add((int)a);
               if(!isSorted(fileCArray)){
                   fileB = true;
                   fileBArray.add(fileCArray.remove(fileCArray.size()-1));
                   for(int item: fileCArray) writeFile("C.txt",item);
                   fileCArray.clear();
               }
           }
        }
    }
    public static void fileGeneration(){
        try (FileOutputStream fos = new FileOutputStream("A.txt")) {
            File file = new File("A.txt");
            while (file.length() <= 1000_024)
                fos.write((Integer.toString((int) (Math.random() * 1000)) + " ").getBytes());
            fos.close();
            System.out.println(file.length());
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
    public static long[] readFile(String fileName, long numBytes){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            fileInputStream.skipNBytes(numBytes);
            int resultNum = 0;
            int a ;
            int count = 0;
            while ((a = fileInputStream.read()) != ' '){
                if(a == -1 ) {
                    if(resultNum == 0){
                        resultNum = a;
                        break;
                    }
                    else{
                        break;
                    }
                }
                resultNum = resultNum*10 + ((int) a - 48);
                count++;
            }
            fileInputStream.close();
            return new long[]{resultNum,count + numBytes + 1};
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    public static void writeFile(String fileName, int number){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName),true);
            fileOutputStream.write((Integer.toString(number) + " ").getBytes());
            fileOutputStream.close();
        }catch (IOException exception){
            System.out.println(exception.toString());
        }

    }
    public static void partSort(String file){
        long count = 0;
        while (count < new File (file).length()){
        long currentCount = 0;
        ArrayList<Integer> list = new ArrayList<>();
        while (currentCount < 100_000_024){
            long[] countAndInt = readFile(file, currentCount);
            list.add((int)countAndInt[0]);
            currentCount = countAndInt[1];
        }
        count += currentCount;
        Collections.sort(list);
        for(int item: list){writeFile("A.txt",item);}
    }
}
}