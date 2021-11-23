package com.ymplans.patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jos
 */
public class FlyweightExample {
    public static void main(String[] args) {

    }
}

// 享元类
class ChessPieceUnit{
    private final int id;
    private final String text;
    private final Boolean color;

    public ChessPieceUnit(int id, String text, Boolean color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public Boolean getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
// 享元工厂
class ChessPieceUnitFactory {
    private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();

    static {
        pieces.put(1, new ChessPieceUnit(1, "帅", true));
        pieces.put(2, new ChessPieceUnit(2, "将", false));
        // ...
    }
    public static ChessPieceUnit getChessPiece(int chessPieceId) {
        return pieces.get(chessPieceId);
    }
}

class ChessPiece{
    private ChessPieceUnit chessPieceUnit;
    private int positionX;
    private int positionY;
    public ChessPiece(ChessPieceUnit chessPieceUnit, int positionX, int positionY) {
        this.chessPieceUnit = chessPieceUnit;
        this.positionX = positionX;
        this.positionY = positionY;
    }


    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public ChessPieceUnit getChessPieceUnit() {
        return chessPieceUnit;
    }

    public void setChessPieceUnit(ChessPieceUnit chessPieceUnit) {
        this.chessPieceUnit = chessPieceUnit;
    }
}


class ChessBoard {
    private final Map<Integer, ChessPiece> chessPieces = new HashMap<>();

    ChessBoard(){
        init();
    }
    // 初始化棋盘
    private void init(){
        chessPieces.put(1, new ChessPiece(ChessPieceUnitFactory.getChessPiece(1), 1,5));
        // ...
    }
}

