package models.interview;

import java.util.Arrays;

public class MagicCube {

    public static void main(String[] args) {
        char[] midPoints = {'W', 'Y', 'R', 'O', 'G', 'B'};

        Side w = new Side('W');
        Side y = new Side('Y');
        Side r = new Side('R');
        Side o = new Side('O');
        Side g = new Side('G');
        Side b = new Side('B');

//        Relation wr = new Relation(w, g, o, b, r);
//        Relation or = new Relation(o, g, o, b, r);
//        Relation wr = new Relation(w, g, o, b, r);
//        Relation wr = new Relation(w, g, o, b, r);
//        Relation wr = new Relation(w, g, o, b, r);

        Relation rr = new Relation(r, g, w, b, y);
    }

}


class Side {
    private char midPoint;

    private char[] points = new char[9];

    private int[] printOrders = {4, 6, 3, 0, 1, 2, 5, 8, 7};

    private int[] rotateOrders = {};

    Side(char midPoint) {
        this.midPoint = midPoint;
        Arrays.fill(points, midPoint);
    }

    void rotate() {
        char[] newPoints = new char[9];

        for (int i = 0; i < 9; i++) {
            newPoints[i] = points[rotateOrders[i]];
        }

        points = newPoints;
    }

    void print() {
        for (int printOrder: printOrders) {
            System.out.print(points[printOrder]);
        }
    }
}

class Relation {
    private Side self;

    private Side left;

    private Side up;

    private Side right;

    private Side down;

    Relation(Side self, Side left, Side up, Side right, Side down) {
        this.self = self;
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
    }

    void rotate() {
        self.rotate();
    }
}