package com.game.desktop;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class DFS {

    private BoardState boardState;
    public DFS(int[][] matrix, Vector2 pos)
    {
        boardState = new BoardState(matrix,pos);
    }
    public List<Vector2> Get()
    {
        List<BoardState> list = new ArrayList<>();
        list.add(boardState);
        while (list.size()>0)
        {
            BoardState b = list.get(0);
            list.remove(b);

            for (Vector2 dir : b.GetDirects())
            {
                BoardState newB = b.Clone();
                newB.Run(dir);
                if (newB.IsFilled()) return newB.moved;
                if (!list.contains(newB)) list.add(newB);
            }
        }
        return null;
    }

    public static int[][] ToMatrix(String data)
    {
        String[] s = data.split(":");
        int width = Integer.parseInt(s[0]);
        int height = Integer.parseInt(s[1]);
        int[][] matrix = new int[width][height];
        String st[] = s[2].split(",");
        for(int i=0;i<st.length;i++)
        {
            int x = i/height;
            int y = i%height;
            matrix[x][y] = Integer.parseInt(st[i]);
        }
        return matrix;
    }
    public static String ToString(int[][] matrix)
    {
        String st = "";
        int width = matrix.length;
        int height = matrix[0].length;

        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++)
            {
                String tail = ",";
                if (i==width-1&&j==height-1) tail="";
                st+= matrix[i][j]+tail;
            }
        return width+":"+height+":"+st;
    }
}
