import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WordCounter {


    public static Callable<Integer> countWords(String fileName){
        return () ->{
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            int count = 0;
            for(String line: lines) {
                if(!line.isEmpty()) {
                    count++;
                }
            }
            return count;
        };
    }


    public static void main(String[] args) throws Exception{
        String[] fileNames = {"hello.txt", "cool.txt", "dank.txt"};
        int total = 0;
        ExecutorService service = Executors.newCachedThreadPool();
        Callable<Integer> task;
        List<Callable<Integer>> tasks = new ArrayList<>();
        for(String fileName: fileNames){
            task = countWords(fileName);
            Future<Integer> resultSolo = service.submit(task);
            System.out.println(fileName + " has " + resultSolo.get());
            tasks.add(countWords(fileName));
        }

        List<Future<Integer>> futureList = service.invokeAll(tasks);

        for(Future<Integer> results: futureList){
            total += results.get();
        }

        System.out.println("All words from all files are: " + total);



        service.shutdown();




    }
}
