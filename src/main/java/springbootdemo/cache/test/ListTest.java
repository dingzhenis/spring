package springbootdemo.cache.test;

import java.util.Scanner;

public class ListTest {
    public static void main(String[] args) {
    int n=5,arrays[]=new int[n];
    Scanner sc=new Scanner(System.in);
        System.out.println("请输入5个整数");
    for (int i=0;i<arrays.length;i++){
        arrays[i]=sc.nextInt();
    }
    int min=arrays[0];
    for(int wang:arrays){
        min=wang<min?wang:min;
    }
        System.out.println("你输出的5个数里面最小的是：");
        System.out.printf("%4d",min);
    }}
