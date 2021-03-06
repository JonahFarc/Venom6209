/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.DPoint;
import com.qualcomm.ftcrobotcontroller.Point;

import java.util.*;
/**
 *
 * @author Brendan Hollaway
 */
public class OldRobotics {

    /**
     * @param args the command line arguments
     */
    // red = 0; blue = 1; green = 2
    static final byte r = 0;
    static final byte b = 1;
    static final byte g = 2;
    public static ArrayList<ArrayList<Point>> red_solutions;
    public static ArrayList<ArrayList<Point>> blue_solutions;
    public static final byte[][] RGB_photo =   {{r,r,g,g,g},
                                                {r,g,g,r,b},
                                                {b,g,g,g,g},
                                                {g,b,b,r,r},
                                                {g,b,r,r,r},
                                                {g,b,b,r,r},
                                                {g,g,g,g,g},
                                                {r,g,g,b,b},
                                                {r,g,g,b,b}};
    public static final byte[][] matcher =     {{r,r},
                                                {r,r},
                                                {r,r}};
    public OldRobotics() {
    }
    public static byte[][] RGB_photo_copy = new byte[RGB_photo.length][RGB_photo[0].length];
    public static void main(String[] args) {
        for(int i = 0; i < RGB_photo.length; i++)
            RGB_photo_copy[i] = Arrays.copyOf(RGB_photo[i], RGB_photo[i].length);
        red_solutions = new ArrayList<ArrayList<Point>>();
        blue_solutions = new ArrayList<ArrayList<Point>>();
        for(byte x = 0; x < RGB_photo.length; x++)
            for(byte y = 0; y < RGB_photo[0].length; y++)
            {
                solve_red(x, y, (byte)red_solutions.size());
                solve_blue(x, y, (byte)blue_solutions.size());
            }
        TreeMap<Double, ArrayList<Point>> compared_solutions = new TreeMap<Double, ArrayList<Point>>();
        for(ArrayList<Point> arr : red_solutions)
        {
           /* Iterator<Point> it = arr.iterator();
            System.out.print("Solution " + ": ");
            while(it.hasNext())
            {
                Point next = it.next();
                System.out.print(String.format("x=%d, y=%d; ",next.x, next.y));
            }
            System.out.println();*/
            //System.out.printf("Compare result: %.2f\n", compare(matcher, arr));
            compared_solutions.put(compare(matcher, arr), arr);
        }
        System.out.println(compared_solutions.lastKey());
        
        
        //Uncomment if you need to see the solutions generated
        /*for(int i = 0; i < blue_solutions.size(); i++)
        {
            Iterator<DPoint> it = blue_solutions.get(i).iterator();
            System.out.print("Solution " + i + ": ");
            while(it.hasNext())
            {
                DPoint next = it.next();
                System.out.print(String.format("x=%d, y=%d; ",next.x, next.y));
            }
            System.out.println();
        }*/
    }
    public static void solve_red(byte x, byte y, byte index)
    {
        if(x > -1 && x < RGB_photo_copy.length && y > -1 && y < RGB_photo_copy[0].length)
        {
            if(RGB_photo_copy[x][y] == r)
            {
                if(!(index < red_solutions.size()))
                    red_solutions.add(new ArrayList<Point>());
                red_solutions.get(index).add(new Point(x,y));
                RGB_photo_copy[x][y] = g;
                //all solutions upwards
                solve_red((byte)(x+1), y, index);
                solve_red((byte)(x+1), (byte)(y+1), index);
                solve_red((byte)(x+1), (byte)(y-1), index);
                //all solutions leftwards and rightwards
                solve_red((byte)(x), (byte)(y+1), index);
                solve_red((byte)(x), (byte)(y-1), index);
                //all solutions downwards
                solve_red((byte)(x-1), (byte)(y+1), index);
                solve_red((byte)(x-1), y, index);
                solve_red((byte)(x-1), (byte)(y-1), index);
            }
        }
    }
    public static void solve_blue(byte x, byte y, byte index)
    {
        if(x > -1 && x < RGB_photo_copy.length && y > -1 && y < RGB_photo_copy[0].length)
        {
            if(RGB_photo_copy[x][y] == b)
            {
                if(!(index < blue_solutions.size()))
                    blue_solutions.add(new ArrayList<Point>());
                blue_solutions.get(index).add(new Point(x,y));
                RGB_photo_copy[x][y] = g;
                //all solutions upwards
                solve_blue((byte)(x+1), y, index);
                solve_blue((byte)(x+1), (byte)(y+1), index);
                solve_blue((byte)(x+1), (byte)(y-1), index);
                //all solutions leftwards and rightwards
                solve_blue((byte)(x), (byte)(y+1), index);
                solve_blue((byte)(x), (byte)(y-1), index);
                //all solutions downwards
                solve_blue((byte)(x-1), (byte)(y+1), index);
                solve_blue((byte)(x-1), y, index);
                solve_blue((byte)(x-1), (byte)(y-1), index);
            }
        }
    }
    public static DPoint center(ArrayList<Point> hash)
    {
        double x = 0;
        double y = 0;
        Iterator<Point> it = hash.iterator();
        while(it.hasNext())
        {
            Point next = it.next();
            x += next.getX();
            y += next.getY();
        }
        x /= (double)hash.size();
        y /= (double)hash.size();
        DPoint point = new DPoint(x,y);
        return point;
    }
    public static double compare(byte[][] template, ArrayList<Point> test)
    {
        int min_x = 0;
        int min_y = 0;
        Iterator<Point> it = test.iterator();
        while(it.hasNext())
        {
            Point next = it.next();
            min_x = next.x < min_x ? next.x : min_x;
            min_y = next.y < min_y ? next.y : min_y;
        }
        adjust(test, min_x, min_y);
        Point test_center = DPoint.toPoint(center(test));
        ArrayList<Point> ALtemplate = new ArrayList<Point>();
        for(byte x = 0; x < template.length; x++)
            for(byte y = 0; y < template[0].length; y++)
                if(template[x][y] == r)
                    ALtemplate.add(new Point(x,y));
        Point template_center_min   =   DPoint.toPoint(center(ALtemplate));
        Point template_center_max_x =   DPoint.toMaxXPoint(center(ALtemplate));
        Point template_center_max_y =   DPoint.toMaxYPoint(center(ALtemplate));
        Point template_center_max   =   DPoint.toMaxPoint(center(ALtemplate));
        equalize_centers(ALtemplate, template_center_min, test_center);
        ArrayList<Double> solutions = new ArrayList<Double>();
        solutions.add(proportional_equal(ALtemplate, test));
        if(!template_center_min.equals(template_center_max_x))
        {
            equalize_centers(ALtemplate, template_center_max_x, test_center);
            solutions.add(proportional_equal(ALtemplate, test));
        }
        if(!template_center_min.equals(template_center_max_y))
        {
            equalize_centers(ALtemplate, template_center_max_y, test_center);
            solutions.add(proportional_equal(ALtemplate, test));
        }
        if(!template_center_max_x.equals(template_center_max) && !template_center_max_y.equals(template_center_max))
        {
            equalize_centers(ALtemplate, template_center_max, test_center);
            solutions.add(proportional_equal(ALtemplate, test));
        }
        return max(solutions);
    }
    public static double max(ArrayList<Double> solutions)
    {
        double max = Double.MIN_VALUE;
        for(double d : solutions)
            if(d > max)
                max = d;
        //System.out.println("max is: " + max);
        return max;
    }
    public static double proportional_equal(ArrayList<Point> template, ArrayList<Point> test)
    {
        int prev_temp_size = template.size();
        int prev_test_size = test.size();
        template.retainAll(test);
        test.retainAll(template);
        //test.size() = template.size() now
        int dTemp = prev_temp_size - template.size();
        //int dTest = prev_test_size - test.size();
        return test.size() / ((double)dTemp + prev_test_size);
        //this return is the slight optimization of:
        //return test.size() / ((double)dTemp + dTest + test.size());
    }            
    public static void equalize_centers(ArrayList<Point> template, Point template_center, Point test_center)
    {
        if(template_center == test_center)
            return;
        int x_offset = test_center.x - template_center.x;
        int y_offset = test_center.y - template_center.y;
        adjust(template, x_offset, y_offset);
    }
    public static void adjust(ArrayList<Point> arr, int x_offset, int y_offset)
    {
        for(int i = 0; i < arr.size(); i++)
            arr.set(i, new Point(arr.get(i).x + x_offset, arr.get(i).y + y_offset));
    }
    public static void reset_RGB()
    {
        for(byte[] byt : RGB_photo_copy)
            Arrays.fill(byt, g);
    }
}