package com.game.desktop;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardState {
    private static final List<Vector2> dirs = Arrays.asList(new Vector2(1,0),new Vector2(-1,0),
            new Vector2(0,1),new Vector2(0,-1));
    public Vector2 pos = new Vector2();
    public int[][] matrix;
    private int width,height;
    public List<Vector2> moved = new ArrayList<>();

    public BoardState(){}
    public BoardState(int[][] matrix,Vector2 pos)
    {
        this.matrix = matrix;
        width = matrix.length;
        height = matrix[0].length;
        this. pos = pos;
    }
    public BoardState Clone()
    {
        BoardState clone = new BoardState();
        clone.moved.addAll(moved);
        clone.pos.set(pos);
        clone.width = width;
        clone.height = height;
        clone.matrix = new int[width][height];
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                clone.matrix[i][j] = matrix[i][j];
        return clone;
    }
    public void Run(Vector2 dir)
    {
        moved.add(dir);
        Vector2 n = new Vector2(pos);
        while (Valid(n)){
            pos.set(n);
            Set(n,1);
            n.add(dir);
        }
    }

    private boolean EqualMatrix(int[][] m)
    {
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                if (matrix[i][j]!=m[i][j]) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardState)) return false;
        BoardState that = (BoardState) o;
        return width == that.width && height == that.height && pos.equals(that.pos) && EqualMatrix(that.matrix);
    }

    public boolean IsFilled()
    {
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                if (matrix[i][j]==0) return false;
        return true;
    }
    public List<Vector2> GetDirects()
    {
        List<Vector2> list = new ArrayList<>();
        for (Vector2 d : dirs)
        {
            Vector2 nP = new Vector2(pos).add(d);
            if (Valid(nP)) list.add(d);
        }
        return list;
    }
    private boolean Valid(Vector2 pos)
    {
        if (!InBoard(pos)) return false;
        int i = Get(pos);
        return i!=-1;
    }
    private int Get(Vector2 pos)
    {
        return matrix[(int)pos.x][(int)pos.y];
    }
    private void Set(Vector2 pos,int value)
    {
        matrix[(int)pos.x][(int)pos.y] = value;
    }
    private boolean InBoard(Vector2 pos)
    {
        if (pos.x<0 || pos.x>=width) return false;
        if (pos.y<0 || pos.y>=height) return false;
        return true;
    }
    public void Show()
    {
        for (int j=height-1;j>=0;j--)
        {
            for (int i=0;i<width;i++)
                System.out.print(matrix[i][j]+",");
            System.out.println();
        }
    }
}
