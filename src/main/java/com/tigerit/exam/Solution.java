package com.tigerit.exam;


import static com.tigerit.exam.IO.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collections;


/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable
{

    static int[][][] data = new int[12][105][105];
    static String[][] col_Name = new String[12][105];
    static String query_part[] = new String[150];
    static int nR[] = new int[12];
    static int nC[] = new int[12];
    static boolean vis[][] = new boolean[15][150];
    static String divide_fst_part;
    static String divide_snd_part;

    static void Divide(String str)
    {
        String tmp = str;
        int len = tmp.length();

        for (int k = 0; k < len; k++)
        {
            if (tmp.charAt(k) == '.')
            {
                divide_fst_part = tmp.substring(0, k);
                divide_snd_part = tmp.substring(k + 1);
            }
        }
    }

    static int determineColumn(int table, String snd_part)
    {
        for (int k = 0; k < nC[table]; k++)
        {
            if (col_Name[table][k].compareTo(snd_part) == 0)
            {
                return k;
            }
        }
        return 0;
    }



    static HashMap<String, Integer> M = new HashMap<String, Integer>();

    static void QUERY(String qury)
    {
        String tmp = "";



        int sz = 0;
        int len = qury.length();
        for (int i = 0; i < len; i++)
        {
            if (qury.charAt(i) == ' ')
            {
                query_part[sz++] = tmp;
                tmp = "";
            }
            else
            {
                tmp = tmp + qury.charAt(i);
            }
        }
        query_part[sz++] = tmp;

        HashMap<String, Integer> short_name = new HashMap<String, Integer>();
        short_name.clear();

        int table1 = 0, table2 = 0, col1 = 0, col2 = 0;
        boolean check = false;

        for (int i = 0; i < sz; i++)
        {
            if (query_part[i].compareTo("JOIN") == 0)
            {
                if (query_part[i + 2].compareTo("ON") == 0)
                {
                    table1 = (int) M.get(query_part[i - 1]);
                    table2 = (int) M.get(query_part[i + 1]);

                    Divide(query_part[i + 3]);
                    String fst_part = divide_fst_part;
                    String snd_part = divide_snd_part;

                    col1 = determineColumn(table1, snd_part);

                    Divide(query_part[i + 5]);
                    fst_part = divide_fst_part;
                    snd_part = divide_snd_part;
                    col2 = determineColumn(table2, snd_part);
                }
                else
                {
                    table1 = M.get(query_part[i - 2]);
                    table2 = M.get(query_part[i + 1]);
                    short_name.put(query_part[i - 1], table1);
                    short_name.put(query_part[i + 2], table2);
                    int j = i + 3;

                    Divide(query_part[j + 1]);
                    String fst_part = divide_fst_part;
                    String snd_part = divide_snd_part;

                    col1 = determineColumn(table1, snd_part);

                    // <second_table_short_name>.<column_name>
                    Divide(query_part[j + 3]);
                    fst_part = divide_fst_part;
                    snd_part = divide_snd_part;
                    col2 = determineColumn(table2, snd_part);

                    check = true;
                }// esle
                break;
            }//fi
        }//rof

        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 104; j++)
            {
                vis[i][j] = false;
            }
        }

        // check whether we have to print all
        if (query_part[1].compareTo("*") == 0)
        {
            for (int i = 0; i < 15; i++)
            {
                for (int j = 0; j < 104; j++)
                {
                    vis[i][j] = true;
                }
            }
        }
        else
        {
            for (int i = 1; query_part[i].compareTo("FROM") != 0; i++)
            {
                int l = query_part[i].length();
                if (query_part[i].charAt(l - 1) == ',')
                {
                    query_part[i] = query_part[i].substring(0, l - 1);
                }
                Divide(query_part[i]);
                String fst_part = divide_fst_part;
                String snd_part = divide_snd_part;


                int prnt_tbl = 0;
                if (check)
                {
                    prnt_tbl = (int) short_name.get(fst_part);
                }
                else
                {
                    prnt_tbl = (int) M.get(fst_part);
                }
//                cout<<fst_part<<" "<<snd_part<<" "<<prnt_tbl<<endl;
                for (int k = 0; k < nC[prnt_tbl]; k++)
                {
                    if (col_Name[prnt_tbl][k].compareTo(snd_part) == 0)
                    {
//                        cout<<prnt_tbl<<" "<<k<<endl;
                        vis[prnt_tbl][k] = true;
                        break;
                    }
                }
            }
        }

//        Vector ans = new Vector();
        ArrayList< ArrayList<Integer> > ans = new ArrayList<ArrayList<Integer>>();
        ans.clear();

        for (int i = 0; i < nR[table1]; i++)
        {
            for (int j = 0; j < nR[table2]; j++)
            {
                if (data[table1][i][col1] == data[table2][j][col2])
                {
//                    cout<<data[table1][i][col1] <<" "<<data[table2][j][col2]<<endl;
                    ArrayList<Integer> take = new ArrayList<Integer>();

                    for (int k = 0; k < nC[table1]; k++)
                    {
                        if (vis[table1][k])
                        {
                            take.add(data[table1][i][k]);
                        }
                    }
                    for (int k = 0; k < nC[table2]; k++)
                    {
                        if (vis[table2][k])
                        {
                            take.add(data[table2][j][k]);
                        }
                    }
                    ans.add(take);
                }
            }
        }
//        Arrays.sort(ans);

        int ans_size = ans.size();

        int take_size = ((ArrayList<Integer>) ans.get(0)).size();


        for(int i = 0; i < ans_size; i++)
        {
            for(int j = 0; j < ans_size - 1; j++)
            {
                for(int k = 0; k < take_size; k++)
                {
                    if(ans.get(j).get(k) > ans.get(j+1).get(k))
                    {
                        Collections.swap(ans, j, j+1);
                        break;
                    }
                    else if(ans.get(j).get(k) < ans.get(j+1).get(k))
                    {
                        break;
                    }
                }
            }
        }



        ArrayList<String>column_name = new ArrayList<String>();


        for(int k = 0; k < nC[table1]; k++)
        {
            if(vis[ table1 ][ k ])
                column_name.add(col_Name[table1][k]);
        }

        for(int k = 0; k < nC[table2]; k++)
        {
            if(vis[ table2 ][ k ])
                column_name.add(col_Name[table2][k]);
        }

        int siz = column_name.size();
        for(int i = 0; i < siz; i++)
        {
//        cout<<column_name[i];
            System.out.print(column_name.get(i));
            if(i == siz-1)
                System.out.println();
            else
                System.out.print(" ");
        }




        for (int i = 0; i < ans_size; i++)
        {
            ArrayList<Integer> tt =  (ans.get(i));
            for (int j = 0; j < take_size; j++)
            {
//                cout<<ans[i][j];

                System.out.print((tt.get(j)));
                if (j == take_size - 1)
                {
                    System.out.println();
                }
                else
                {
                    System.out.print(" ");

                }
            }
        }
    }

    @Override
    public void run()
    {
        // your application entry point

        // sample input process

        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        int i, j, k;
        int n, num, m, q;
        int t, nT, cases = 1;
        t = sc.nextInt();
        while (t > 0)
        {
            t--;
            nT = sc.nextInt();
            M.clear();
            String name, qury;
            for (i = 0; i < 15; i++)
            {
                for (j = 0; j < 104; j++)
                {
                    vis[i][j] = false;
                }
            }
//            Arrays.fill(vis, 0);

            /* ****** input goes here ******* */
            for (i = 0; i < nT; i++)
            {
                // i'th table name
                name = sc.next();
                // map this name to the table number
                M.put(name, i);
                // number of row, column in the i'th table
                nR[i] = sc.nextInt();
                nC[i] = sc.nextInt();

                // column name of the i'th table
                for (j = 0; j < nC[i]; j++)
                {
                    col_Name[i][j] = sc.next();
                }

                // table data.. i = table number, j = table row, k = table column
                for (j = 0; j < nR[i]; j++)
                {
                    for (k = 0; k < nC[i]; k++)
                    {
                        data[i][j][k] = sc.nextInt();
                    }
                }
            }
            int nQ;
            // number of query
            nQ = sc.nextInt();
            String ch = sc.next();  // temporary value get here
            System.out.println("Test: " + cases);
            cases++;

            while (nQ > 0)
            {
                nQ--;
                // query
                qury = sc.nextLine();
                QUERY(qury);
                System.out.println();
            }

        }
    }



}
