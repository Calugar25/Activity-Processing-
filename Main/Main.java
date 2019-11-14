package Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Main {


    public static  PrintWriter writer1;
    public static  void printData(ArrayList<MonitoredData> data)
    {
        for (MonitoredData m : data)
        {
            System.out.println(m.display());
        }
    }

    public static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();

        //create the list
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        //create the result map of TimeUnit and difference
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;

        for ( TimeUnit unit : units ) {

            //calculate difference in millisecond
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            //put the result in the map
            result.put(unit,diff);

        }

        return result;
    }

    public static Long[] transform(Long l)
    {   Long []result=new Long[3];

        Long seconds = l;
        Long p1 = seconds % 60;
        Long p2 = seconds / 60;
        Long p3 = p2 % 60;
        p2 = p2 / 60;

        result[0]=p1;
        result[1]=p3;
        result[2]=p2;
        return  result;

    }

    public static String transform2(Long l)
    {  String s="";
        Long []result=new Long[3];

        Long seconds = l;
        Long p1 = seconds % 60;
        Long p2 = seconds / 60;
        Long p3 = p2 % 60;
        p2 = p2 / 60;

        result[0]=p1;
        result[1]=p3;
        result[2]=p2;
        for(int i=2;i>=0;i--)
        {
            s+=Long.toString(result[i]);
            s+=":";
        }
        return s;



    }


    public static void main(String args[]) {


        ArrayList<MonitoredData> data = new ArrayList<MonitoredData>();
        ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            list = (ArrayList<String>) Files.lines(Paths.get("C:\\DDDDDD\\pt\\pt2019_30422_calugar_marius_assignment_5\\pt2019_30422_calugar_marius_assignment_5\\Activities.txt")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : list) {
            Date s_date = new Date();
            Date e_date = new Date();
            String name = "";

            try {

                String[] split = s.split("\\t");

                s_date = format.parse(split[0]);
                e_date = format.parse(split[2]);
                name = split[4];
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MonitoredData m = new MonitoredData(s_date, e_date, name);
            data.add(m);


        }

        //Task 2:How many days of monitoring data appears in the log
        ArrayList<Integer> days=(ArrayList<Integer>)data.stream().map(m->m.getstartDay()).collect(Collectors.toList());
        //data.stream().map(m->m.getEnd_date().getDay()).collect(Collectors.toCollection(() -> Days));

        long l=days.stream().distinct().count();
        try{
            PrintWriter writer = new PrintWriter("Task1.txt", "UTF-8");
            writer.println("The number of days of monitoring data is "+l);
            writer.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        System.out.println("TASK 1 : ");
        System.out.println("The number of days of monitoring data is "+l);

        System.out.println();
        System.out.println();
        Map<String ,Long> task2=data.stream().collect(Collectors.groupingBy(m->m.getLabel(), Collectors.counting()));
        System.out.println("TASK 2 :");
        task2.forEach((String,Long)-> System.out.println(String+ " "+Long));


        try{
            PrintWriter writer = new PrintWriter("Task2.txt", "UTF-8");
            task2.forEach((String,Long)-> writer.println(String+ " "+Long));
            writer.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();

        System.out.println("Task 3 :");

        Map<Integer,Map<String,Long>>task3=data.stream().collect(Collectors.groupingBy
                (n->days.stream().distinct().filter(z->z.equals(n.getstartDay())).
                        collect(Collectors.toList()).get(0),Collectors.groupingBy(n->n.getLabel(),  Collectors.counting())));

        task3.forEach((String,Long)-> System.out.println(String+" "+Long));

        try{
            PrintWriter writer = new PrintWriter("Task3.txt", "UTF-8");
            task3.forEach((String,Long)-> writer.println(String+ " "+Long));
            writer.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Task 4:");
        try {
            writer1 = new PrintWriter("Task4.txt", "UTF-8");
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        for(MonitoredData m:data)
        {
            int hours;
            int minutes;
            int seconds;
            Long p=getDateDiff(m.getStart_date(),m.getEnd_date(),TimeUnit.SECONDS);
            Long []result=transform(p);


                writer1.println(m.display()+" "+result[2]+":"+result[1]+":"+result[0]);


           System.out.println(m.display()+" "+result[2]+":"+result[1]+":"+result[0]);

        }
        writer1.close();

        System.out.println();
        System.out.println();
        System.out.println("Task 5 : Total length for each activity ");

        Map<String ,Long> task5=data.stream().collect(Collectors.groupingBy(n->n.getLabel(),
                Collectors.summingLong(z->z.getDuration())));


        try{
            PrintWriter writer = new PrintWriter("Task5.txt", "UTF-8");

            task5.forEach((String,Long)->writer.println(String+" "+transform2(Long/1000)));

            writer.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        task5.forEach((String,Long)-> System.out.println(String+" "+transform2(Long/1000)));

        System.out.println();
        System.out.println();
        System.out.println("Task 6 : Filter the activities that have 90% of the monitoring records with duration less than 5\n" +
                "minutes");

        Map<String,Long>task6_map=data.stream().collect(Collectors.groupingBy(n->n.getLabel(),Collectors.counting()));

        //task6_map.forEach((String,Long)-> System.out.println(String+" "+Long));

        Map<String,Long> task6_filter=data.stream().filter(n->n.getDuration()<300000).collect(Collectors.groupingBy(m->m.getLabel(),Collectors.counting()));

        //task6_filter.forEach((String,Long)-> System.out.println(String+" "+Long));

        List<String>task6 =(List<String>)task6_filter.entrySet().stream().filter
                (m->task6_map.get(m.getKey())*0.9<=m.getValue()).map(m->m.getKey()).collect(Collectors.toList());

        task6.forEach(String-> System.out.println(String));


        try{
            PrintWriter writer = new PrintWriter("Task6.txt", "UTF-8");


            task6.forEach(String-> writer.println(String));

            writer.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }




    }
}