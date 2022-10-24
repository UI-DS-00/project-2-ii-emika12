import com.opencsv.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlgorithmProject
{
    public static void main(String[] args) {

        MyList myList =new MyList();
        readingFiles(myList);


        MyList[] myLists=ColumnArr(myList);
        RowArr(myList);

        int order= -1 ;
        Scanner sc=new Scanner(System.in);

        while (order != 0)
        {
            System.out.println("1.add data\n2.remove data\n3.find data\n4.update\n5.print matrix" +
                    "\n6.save data\n7.data in specific row\n8.data in specific column\n0.end\n");

            order=sc.nextInt();
            try {
                switch (order) {
                    case 1:
                        System.out.println("enter the row , column , and the data ");
                        myList.adding(sc.nextInt(), sc.nextInt(), sc.nextInt());
                        break;
                    case 2:
                        System.out.println("enter the row , column , and the data ");
                        myList.delete(sc.nextInt(), sc.nextInt(), sc.nextInt());
                        break;
                    case 3:
                        System.out.println("enter the data you are looking for");
                        myList.finding(sc.nextInt());
                        break;
                    case 4:
                        System.out.println("enter the row , column , and the data ");
                        myList.updating(sc.nextInt(), sc.nextInt(), sc.nextInt());
                        break;
                    case 5:
                        myList.printMatrix(myList);
                        break;
                    case 6:
                        myList.saveFiles();
                        break;
                    case 7:
                        System.out.println("enter the index");
                        myList.getIndexRow(sc.nextInt());
                        break;
                    case 8:
                        System.out.println("enter the index");
                        myList.getIndexColumn(sc.nextInt());
                        break;
                    case 0:
                        return;
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public static void readingFiles(MyList myList)
    {
        try {
            //reading the files
            FileReader filereader = new FileReader("C:/Users/ES/Desktop/project/project-2-ii-emika12/M(10,5).csv");


            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();


            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();

            // Read all data at once
            String [] nextRecord;
            int matrix_row=0;
            int matrix_column=0;

            MyList.Node node;
            //at the end the size will be fine
            MyList.Node previousNode = null;

            while ((nextRecord = csvReader.readNext()) != null) {

                matrix_column=0;
                //matrix_column=nextRecord.length;
                for (String cell : nextRecord) {
                    if (Integer.parseInt(cell) != 0) {
                        node = new MyList.Node(matrix_row, matrix_column, Integer.parseInt(cell));

                        if (myList.head == null )
                            myList.head=node;

                        if (previousNode != null) {
                            node.previous = previousNode;
                            previousNode.next = node;
                        }
                        previousNode = node;

                        myList.tail =node;
                    }
                    ++matrix_column;
                }
                ++matrix_row;
                ++myList.size;
            }
            myList.row = matrix_row;
            myList.column = matrix_column;
        }
        catch (Exception e){
            System.out.println("could not read the file");
        }
    }

    public static MyList[] ColumnArr(MyList myList)
    {


        myList.columnArr=new MyList[myList.column];
        for (int i=0 ; i< myList.columnArr.length ;++i)
        {
            MyList.Node node = myList.head;
            MyList.Node semi_node = null;
            MyList.Node previous_node=null;

            myList.columnArr[i]=new MyList();


            while (node != null)
            {
                if (node.column == i)
                {
                    if (myList.columnArr[i].size==0) {
                        myList.columnArr[i].head = new MyList.Node(node.row , node.column , node.data);
                        semi_node=myList.columnArr[i].head;
                        myList.columnArr[i].tail=myList.columnArr[i].head;
                    }
                    else
                    {
                        semi_node = new MyList.Node(node.row , node.column, node.data);
                        previous_node.next=semi_node;
                        semi_node.previous=previous_node;
                    }

                    ++myList.columnArr[i].size;
                }
                myList.columnArr[i].tail=semi_node;
                previous_node=semi_node;
                node=node.next;
            }
        }

        return myList.columnArr;
    }

    public static MyList[] RowArr(MyList myList)
    {
        myList.rowArr=new MyList[myList.row];
        MyList[] rowList =myList.rowArr;


        for (int i=0 ; i< rowList.length ;++i) {
            rowList[i] = new MyList();


            MyList.Node node = myList.head;
            MyList.Node previous_node=null;
            MyList.Node semi_node = null;

            while (node != null) {
                if (node.row == i) {

                    if (rowList[i].size == 0) {
                        rowList[i].head = new MyList.Node(node.row, node.column, node.data);
                        semi_node = rowList[i].head;
                        rowList[i].tail = rowList[i].head;
                    } else {
                        semi_node = new MyList.Node(node.row, node.column, node.data);
                        previous_node.next = semi_node;
                        semi_node.previous = previous_node;
                    }

                    ++rowList[i].size;
                }
                rowList[i].tail = semi_node;
                previous_node = semi_node;
                node = node.next;
            }
        }
        return rowList;
    }

}


class MyList {

    int size=0;
    Node head;
    Node tail;
    int row=0;
    int column=0;

    MyList rowArr[] ;
    MyList columnArr[];

    static class Node {

        int row;
        int column;
        int data;


        Node previous=null;
        Node next = null;

        public Node(int row, int column, int data) {
            this.row = row;
            this.column = column;
            this.data = data;
        }
    }

    public void adding (int row , int column , int data) throws Exception
    {
        if (row < 0 || row >= this.row || column <0 || column>=this.column )
            throw new Exception("wrong input for row or column !");

        Node node = this.head;

        //for the time the node is before all the other nodes
        if (row < node.column && column < node.column)
        {
            Node newNode= new Node(row, column, data);
            newNode.next=head;
            head.previous=newNode;
            head=newNode;
            return;
        }

        while (node != tail.next)
        {
            if ((node.row <= row && (node.next.row >= row))|| node.next==null)
                if ((node.next.column >= column && node.next.row==row )||(node.next.row > row)
                        || (node.column <= column && node.row == row ))
                {
                    Node newNode=add(node ,data , row , column);
                    if (newNode.next == null)
                        tail=newNode;
                    return;
                }
            node=node.next;
        }

    }

    public Node add(Node node , int data , int row , int column)
    {
        Node new_node=new Node(row, column, data);


        new_node.previous=node;
        new_node.next=node.next;

        if (node.next != null)
            node.next.previous=new_node;
        node.next=new_node;

        ++size;

        return new_node;
    }

    public void delete (int row , int column , int data) throws Exception
    {
        if (row < 0 || row >= this.row || column <0 || column>=this.column )
            throw new Exception("wrong input for row or column !");

        Node node = this.head;
        boolean found = false;
        while (node != null && !found)
        {
            if (node.row == row && node.column == column && node.data==data)
            {
                found = true;
                if (node.previous != null) {
                    node.previous.next = node.next;
                    node.next.previous = node.previous;

                    if (node.next == null)
                        tail=node.previous;
                }
                else
                    node.next.previous=null;

                if (node.previous == null)
                    head=node.next;

                size--;
            }
            node=node.next;
        }
        if (found)
            System.out.println("removed successfully !");
        else
            throw new Exception("not found");
    }

    public void finding (int data)
    {
        if (data != 0) {
            Node node = this.head;

            boolean found = false;
            while (node != null) {
                if (node.data == data) {
                    found = true;
                    System.out.println("data: " + data);
                    System.out.println("row: " + node.row + "  column: " + node.column);
                    System.out.println("==========================");
                }
                node = node.next;
            }

            if (! found )
                System.out.println("could not find any data");

        }else
        {
            for (int i= 0 ; i<row ; ++i)
                for (int j=0 ; j <column ;++j){

                    Node node = this.head;
                    boolean found = false;
                    while (node != tail)
                    {
                        if (node.row == i && node.column == j) {
                            found=true;
                            break;
                        }
                        node= node.next;
                    }
                    if (!found)
                        System.out.println("row : "+i + " column : "+j);

                }
        }

    }

    public void updating (int row , int column , int newData) throws Exception
    {
        if (row < 0 || row >= this.row || column <0 || column>=this.column )
            throw new Exception("wrong input for row or column !");


        Node node=this.head;

        boolean found =false;
        while (node != tail && !found)
        {
            if (node.column ==column && node.row==row) {
                node.data = newData;
                found=true;
            }
            node=node.next;
        }

        if (found)
            return;

        else
            adding(row ,column , newData);

        System.out.println("updated successfully");
    }

    public void printMatrix(MyList myList)
    {
        System.out.println("full matrix 1 \ncompressed matrix 2");
        Scanner sc=new Scanner(System.in);

        int order=sc.nextInt();

        if (order == 1) {
            int columnSize = 0;
            int rowSize = 0;

            Node dataNodes = this.head;
            int row_difference = 0;
            int column_difference = 0;
            int counting=0;

            while (dataNodes != null) {

                int zeros=0;
                if (row_difference == dataNodes.row)
                    zeros=dataNodes.column-column_difference-1;
                else
                    zeros = (myList.column - column_difference - 1)+((dataNodes.row -row_difference-1)*myList.column) + dataNodes.column;
                if (row_difference == 0 && column_difference == 0 && dataNodes.column!=0 && dataNodes.row !=0)
                    zeros++;

                while (zeros > 0)
                {
                    System.out.printf("0 ");
                    ++counting;


                    if (counting == myList.column)
                    {
                        System.out.println();
                        counting=0;
                    }

                    zeros--;
                }
                if (counting  == myList.column) {
                    System.out.println();
                    counting=0;
                }

                System.out.printf("%d ", dataNodes.data);
                ++counting;


                column_difference= dataNodes.column;;
                row_difference= dataNodes.row;
                dataNodes=dataNodes.next;
            }

            int last_numbers =(myList.column-tail.column-1) + ((myList.row - tail.row -1 )*myList.column);
            while (last_numbers > 0)
            {
                System.out.printf("0 ");
                ++counting;


                if (counting == myList.column)
                {
                    System.out.println();
                    counting=0;
                }

                last_numbers--;
            }
            System.out.println();
        }
        else if (order == 2)
        {

            Node node=this.head;

            while (node != null)
            {
                System.out.printf("%d %d %d\n",node.row ,node.column,node.data);
                node=node.next;
            }
        }
        else System.out.println("wrong order");
    }

    public void saveFiles()
    {
        File file = new File("C:/Users/ES/Desktop/project/project-2-ii-emika12/M(10,5)2.csv");

        try {

            FileWriter outputfile = new FileWriter(file);


            CSVWriter writer = new CSVWriter(outputfile);


            List<String []> data = new ArrayList<>();

            int columnSize = 0;
            int rowSize = 0;

            String [] saving=new String[3];

            Node node=this.head;

            data.add(new String[]{"row", "column", "data"});
            while (node != null)
            {
                data.add( new String[]{Integer.toString(node.row ),
                        Integer.toString(node.column),Integer.toString(node.data)});
                node=node.next;
            }

            writer.writeAll(data);


            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getIndexRow(int index)
    {
        MyList list=this.rowArr[index];
        Node node =list.head;

        while (node != null)
        {
            System.out.println("data: " + node.data);
            System.out.println("row: " + node.row + "  column: " + node.column);
            System.out.println("==========================");

            node=node.next;
        }


    }

    public void getIndexColumn(int index)
    {
        MyList list=this.columnArr[index];
        Node node =list.head;

        while (node != null)
        {
            System.out.println("data: " + node.data);
            System.out.println("row: " + node.row + "  column: " + node.column);
            System.out.println("==========================");

            node=node.next;
        }
    }
}

